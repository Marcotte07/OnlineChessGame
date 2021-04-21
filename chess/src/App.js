import './App.css';
import React, {useEffect, useState} from 'react'
import {gameSubject, initGame, resetGame} from './components/Game'
import Board from './components/Board'

function App() {
  const [board, setBoard] = useState([]);
  const [isGameOver, setIsGameOver] = useState()
  const [result, setResult] = useState()
  const [started, setStarted] = useState()
  
  useEffect(() => {
    initGame()
    const subscribe = gameSubject.subscribe((game) => {
      setBoard(game.board)
      setIsGameOver(game.isGameOver)
      setResult(game.result)
      setStarted(game.started)
    })
    return () => subscribe.unsibscribe()
  }, [])

  return (
    
<div> 
  <div className = "searching">
    {started ? "" : <h1> Searching For Game </h1>}
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
        <Board board = {board}/> 
      </div> 
      {result && <p className = "vertical-text">{result}</p>}
    </div> 
    </div>
  );
}

export default App;
