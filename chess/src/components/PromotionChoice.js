import React, {useEffect, useState} from 'react'
import Square from './Square'
import {move} from './Game'

const promotionPieces = ['r', 'n', 'b', 'q']

export default function PromotionChoice({
    promotion: {from, to, color},
})
{
    return(
       <div className = "board"> 
           {promotionPieces.map((p, i) => (
                <div key = {i} className = "promote-square">
                    <div className = "piece-container" 
                    onClick={()=> move(from, to, p)} >
                        <img 
                        src = {`assets/images/${p}_${color}.png`} 
                        className = "piece cursor-pointer"
                        />
                    </div>
                </div>
           ))}
       </div>
   )

}