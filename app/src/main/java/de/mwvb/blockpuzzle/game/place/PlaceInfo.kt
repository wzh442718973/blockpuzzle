package de.mwvb.blockpuzzle.game.place

import de.mwvb.blockpuzzle.block.BlockTypes
import de.mwvb.blockpuzzle.game.GameEngineInterface
import de.mwvb.blockpuzzle.game.IGameView
import de.mwvb.blockpuzzle.gamedefinition.GameDefinition
import de.mwvb.blockpuzzle.gamepiece.GamePiece
import de.mwvb.blockpuzzle.gamestate.GameState
import de.mwvb.blockpuzzle.gravitation.GravitationData
import de.mwvb.blockpuzzle.messages.MessageFactory
import de.mwvb.blockpuzzle.playingfield.FilledRows
import de.mwvb.blockpuzzle.playingfield.PlayingField
import de.mwvb.blockpuzzle.playingfield.QPosition

data class PlaceInfo(val index: Int,
                     val gamePiece: GamePiece,
                     val pos: QPosition,
                     val gs: GameState,
                     val filledRows: FilledRows,
                     val blockTypes: BlockTypes,
                     val playingField: PlayingField,
                     val gravitation: GravitationData,
                     val blocks: Int,
                     val messages: MessageFactory,
                     private val view: IGameView, // <- TODO das hier loswerden. Sound abspielen anders lösen
                     val gameEngineInterface: GameEngineInterface
) {
    private var definition: GameDefinition? = null

    fun getDefinition(): GameDefinition {
        return definition!!
    }

    fun setDefinition(definition: GameDefinition) {
        this.definition = definition
    }

    fun playSound(number: Int) {
        view.playSound(number)
    }
}