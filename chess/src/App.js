import './App.css';
import React, {useEffect, useState} from 'react'
import {gameSubject, initGame, resetGame} from './components/Game'
import Board from './components/Board'
// import {myUsername, opponentUsername} from './components/Game'

// console.log("opponent username is: " + opponentUsername);
function App() {
  const [board, setBoard] = useState([]);
  const [isGameOver, setIsGameOver] = useState()
  const [result, setResult] = useState()
  const [started, setStarted] = useState()
  const [myUsername, setMyUsername] = useState()
  const [opponentUsername, setOpponentUsername] = useState()
  
  useEffect(() => {
    initGame()
    const subscribe = gameSubject.subscribe((game) => {
      setBoard(game.board)
      setIsGameOver(game.isGameOver)
      setResult(game.result)
      setStarted(game.started)
      setMyUsername(game.myUsername)
      setOpponentUsername(game.opponentUsername)
    })
    return () => subscribe.unsibscribe()
  }, [])

  return (
    
<div> 
  <div className = "searching">
    {started ? "" : "Searching For Game..."}
  </div>

    <div className = "container">

      {isGameOver && (
        <h2 className = "vertical-text"> GAME OVER
          <button onClick={resetGame}> 
          <span className = "vertical-text"> 
            NEW GAME
            </span>
        </button>
        </h2>
        
      )}
      <div className = "board-container">
        <div className = "opponentUsernameBox">
          {opponentUsername}
        </div>
        <Board board = {board}/>
        <div className = "myUsernameBox">
          {myUsername}
        </div>
      </div> 
      {result && <p className = "vertical-text">{result}</p>}
    </div>
</div>
  );
}

export default App;
