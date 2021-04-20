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
var ws = new WebSocket("ws://localhost:8088/UGH/Test");
export var color = 'b'


// Now block here for opponents move to come back
// TODO; randomly assign color
ws.onopen = function(event) {
    //   var jsonColor = JSON.parse(event.data);
  //  color = jsonColor.color;//  
}
ws.onmessage = function(event) {
    alert(event.data);

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
        color = jsonMove.color
        updateGame();
    }    
 
}
ws.onclose = function(event) {
    // document.getElementById("mychat").innerHTML += "Disconnected!<br />";
}
ws.onerror = function(event) {
    //alert('check');
}

// change this path if ur on another machine, DO NOT FORGET OR ELSE IT WONT WORK (working=poop)
export function move(from, to, promotion) {
    let tempMove = {from, to}
    if(promotion) {
        tempMove.promotion = promotion
    }
    const legalMove = chess.move(tempMove)
    if(legalMove) {
        updateGame();
        ws.send(`{"from":"${from}","to":"${to}","promotion":"${promotion}","color":"w"}`, function(){});
    }    



}

function updateGame(pendingPromotion) {
    const isGameOver = chess.game_over()
    
    const newGame =  {
        board: chess.board(),
        pendingPromotion,
        isGameOver,
        turn: chess.turn(),
        result: isGameOver ? getGameResult() : null
    }

    gameSubject.next(newGame)
}

function getGameResult() {
    if(chess.in_checkmate()) {
        const winner = (chess.turn() === "w") ? 'BLACK' : 'WHITE'
        return `CHECKMATE - WINNER - ${winner}`
    }
    else if (chess.in_draw()) {
        let reason = '50 - MOVES - RULE'
        if(chess.in_stalemate()) {
            reason = 'STALEMATE'
        }
        else if(chess.in_threefold_repitition()){
            reason = 'REPITITION'
        }
        else if(chess.insufficient_material()){
            reason = 'INSUFFICIENT MATERIAL'
        }
        return `DRAW - ${reason}`
    }
    else{
        return 'UNKNOWN REASON'
    }
}