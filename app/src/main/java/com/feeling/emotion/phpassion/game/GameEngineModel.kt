package com.feeling.emotion.phpassion.game

import com.feeling.emotion.phpassion.block.BlockTypes
import com.feeling.emotion.phpassion.game.place.IPlaceAction
import com.feeling.emotion.phpassion.gamedefinition.OldGameDefinition
import com.feeling.emotion.phpassion.gamepiece.Holders
import com.feeling.emotion.phpassion.gamepiece.INextGamePiece
import com.feeling.emotion.phpassion.gamestate.GameState
import com.feeling.emotion.phpassion.playingfield.PlayingField
import com.feeling.emotion.phpassion.playingfield.gravitation.GravitationData

/**
 * Game engine model
 */
data class GameEngineModel(
    // Immutable properties only!
    val blocks: Int,
    val blockTypes: BlockTypes,
    val view: IGameView,
    val gs: GameState,
    val definition: OldGameDefinition,
    val playingField: PlayingField,
    val holders: Holders,
    val placeActions: List<IPlaceAction>,
    val gravitation: GravitationData,
    val nextGamePiece: INextGamePiece
) {

    fun save() {
        val ss = gs.get()
        playingField.save(ss)
        gravitation.save(ss)
        holders.save(ss)
        gs.save()
    }
}
