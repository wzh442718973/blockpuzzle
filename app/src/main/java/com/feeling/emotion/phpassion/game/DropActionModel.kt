package com.feeling.emotion.phpassion.game

import com.feeling.emotion.phpassion.gamepiece.GamePiece
import com.feeling.emotion.phpassion.playingfield.QPosition

/**
 * Player drops a game piece. This class holds the needed data.
 */
data class DropActionModel(
    /** game piece holder index (1, 2, 3, -1) */
    val index: Int,
    /** the game piece to move */
    val gamePiece: GamePiece,
    /** target position in playing field, null if targetIsParking is true */
    val xy: QPosition
)
