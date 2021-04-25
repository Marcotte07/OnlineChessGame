import { getByPlaceholderText } from '@testing-library/dom';
import * as Chess from 'chess.js'
import {BehaviorSubject} from 'rxjs'
import PromotionChoice from './PromotionChoice'


const chess = new Chess();


export const gameSubject = new BehaviorSubject({
    board: chess.board
})

export function initGame() {
    updateGame()
}

export function resetGame() {
    chess.reset()
    updateGame()
}

export function handleMove(from, to) {
    // Array of all possible promotions from the current position
    const promotions = chess.moves({verbose: true}).filter(m => m.promotion)
    console.table(promotions)
    // Some method is if one item in array satisfies condition
    if(promotions.some(p => `${p.from}:${p.to}` === `${from}:${to}`)) {
        const pendingPromotion = {from, to, color: promotions[0].color}
        updateGame(pendingPromotion) 
    }

    const {pendingPromotion} = gameSubject.getValue()

    if(!pendingPromotion) {
        move(from, to)
    }
}

//var ws = new WebSocket("ws://localhost:8080/OnlineChessGame/GameEndpoint");
var ws = new WebSocket("ws://ec2-13-58-104-183.us-east-2.compute.amazonaws.com:8080/OnlineChessGame/GameEndpoint");

export var color = 'b';

var myMove = false;
var hasStarted = false;

// Once cookie works this will be good
// export var myUsername = document.cookie
    
export var myUsername;

var result = null;
var tmp = [];
window.location.search
.substr(1)
.split("&")
.forEach(function (item) {
  tmp = item.split("=");
  if (tmp[0] === "name") result = decodeURIComponent(tmp[1]);
});

myUsername = result;

export var opponentUsername = "Waiting For Player To Connect";
// Now block here for opponents move to come back
// TODO; randomly assign color
ws.onopen = function(event) {
    ws.send("username="+myUsername);
}
var firstText = true;
let gameTime = 0;

function incrementTimer(){
    gameTime += 1;
}
ws.onmessage = function(event) {
    
    console.log("Recieved data from server " + event.data)
    // If they send opponents username and any other info: 
    if(event.data.substring(0, 9) == "username="){
        opponentUsername = event.data.substring(9);
        // This is when game timer should start, as opponent connected
        window.setInterval(incrementTimer, 1000)
        updateGame();
    }
    
    else if (!firstText) {
        var jsonMove = JSON.parse(event.data);

        var from = jsonMove.from;
        var to = jsonMove.to;
        var promotion = jsonMove.promotion
        let tempMove = {from, to}
        if(promotion) {
            tempMove.promotion = promotion
        }
        const legalMove = chess.move(tempMove)
        if(legalMove) {
           // color = jsonMove.color
            updateGame();
            myMove = true;
        }    
    } else {
        hasStarted = true;
        firstText = false;
        color = event.data;
        if(color === "w"){
            myMove = true;
        }
        updateGame()
    }
 
}
ws.onclose = function(event) {
    // document.getElementById("mychat").innerHTML += "Disconnected!<br />";
}
ws.onerror = function(event) {
    //alert('check');
}

export function move(from, to, promotion) {
    if(!myMove){
        return;
    }
    let tempMove = {from, to}
    if(promotion) {
        tempMove.promotion = promotion
    }
    const legalMove = chess.move(tempMove)
    if(legalMove) {
        updateGame();
        ws.send(`{"from":"${from}","to":"${to}","promotion":"${promotion}"}`, function(){});
        myMove = false;
    }    
}

function updateGame(pendingPromotion) {
    const isGameOver = chess.game_over()
    
    const newGame =  {
        board: chess.board(),
        pendingPromotion,
        isGameOver,
        turn: chess.turn(),
        result: isGameOver ? getGameResult() : null, 
        myUsername: myUsername,
        opponentUsername: opponentUsername,
        started: hasStarted
    }

    gameSubject.next(newGame)
}

function getGameResult() {
    // Lets send result of game to server in this method!
    let whitePiecesUsername = ""
    let blackPiecesUsername = ""
    if(color == 'w'){
        whitePiecesUsername = myUsername;
        blackPiecesUsername = opponentUsername;
    }
    else{
        whitePiecesUsername = myUsername;
        blackPiecesUsername = opponentUsername;
    }
    if(chess.in_checkmate()) {
        const winner = (chess.turn() === "w") ? 'BLACK' : 'WHITE'
        
        // Didn't win, so dont call it
        // Two cases to send info to server, if white and won, or if black and won
        if(color == 'w' && winner == 'WHITE' ){
            ws.send("GameOver," + whitePiecesUsername + "," 
            + blackPiecesUsername + ",win," + gameTime);
        }
        else if(color == 'b' && winner == 'BLACK') {
            ws.send("GameOver," + whitePiecesUsername + "," 
            + blackPiecesUsername + ",lose," + gameTime);
        }
        return `CHECKMATE - WINNER - ${winner}`
    }
    else if (chess.in_draw()) {
        let reason = '50 - MOVES - RULE'
        if(chess.in_stalemate()) {
            reason = 'STALEMATE'
        }
        else if(chess.in_threefold_repetition()){
            reason = 'REPITITION'
        }
        else if(chess.insufficient_material()){
            reason = 'INSUFFICIENT MATERIAL'
        }
        
        // Arbitrarily only have white call this so both clients dont send the same info
        if(color == 'w'){ 
            ws.send("GameOver," + whitePiecesUsername + "," 
            + blackPiecesUsername + ",tie," +gameTime);
        }

        return `DRAW - ${reason}`

    }
    else{
        return 'UNKNOWN REASON'
    }
}