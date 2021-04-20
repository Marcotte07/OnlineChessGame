import React from 'react'
import {useDrag, DragPreviewImage} from 'react-dnd'

export default function Piece({piece: {type, color}, position}) {
    const [{isDragging} , drag, preview] = useDrag({
        item: {
            type: 'piece', 
            id: `${position}_${type}_${color}`,
        },
        collect: (monitor) => {
            return {isDragging: !!monitor.isDragging()}
        },
    })
    console.log("Image is " + type + " " + color);
    const pieceImg = (`assets/images/${type}_${color}.png`);
    return( 
    <div className = "test">
        <DragPreviewImage connect={preview} src ={pieceImg}>
            {console.log(pieceImg)}
        </DragPreviewImage>
        <div className = "piece-container" ref={drag} style ={{opacity: isDragging ? 0 : 1 }} >
            <img src = {pieceImg}  className = "piece"/>
        </div> 
    </div>
    )
}