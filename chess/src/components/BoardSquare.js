import React, {useEffect, useState} from 'react'
import Square from './Square'
import Piece from './Piece'
import {useDrop} from 'react-dnd'
import {handleMove} from './Game'
import {gameSubject} from './Game'
import PromotionChoice from './PromotionChoice'

export default function BoardSquare({piece, black, position}) {
    
    const [promotion, setPromotion] = useState(null)
    
    const [ , drop] = useDrop({
        accept: 'piece',
        drop: (item) => {
            let arr = item.id.split('_')
            console.log(item);
            const fromPosition = arr[0];
            handleMove(fromPosition, position)
        },
    })
   
    useEffect(() => {
        const subscribe = gameSubject.subscribe(({pendingPromotion}) =>
            pendingPromotion && pendingPromotion.to === position 
            ? setPromotion(pendingPromotion)
            : setPromotion(null)
        )
        return () => subscribe.unsibscribe()
    }, [])

    return (
        <div className = "board-square" ref = {drop}> 
            <Square black={black}>
                {promotion ?  (
                <PromotionChoice promotion = {promotion}/> 
                ) : piece ? (
                 <Piece piece = {piece} position={position} />
                ) : null}
            </Square>
        </div>
    )
}