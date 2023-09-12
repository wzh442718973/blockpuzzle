package com.feeling.emotion.phpassion.game.stonewars;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import com.feeling.emotion.phpassion.game.GameEngineBuilder;
import com.feeling.emotion.phpassion.game.GameEngineModel;
import com.feeling.emotion.phpassion.game.place.IPlaceAction;
import com.feeling.emotion.phpassion.game.stonewars.place.Check4VictoryPlaceAction;
import com.feeling.emotion.phpassion.gamedefinition.GameDefinition;
import com.feeling.emotion.phpassion.gamestate.GameState;
import com.feeling.emotion.phpassion.gamestate.StoneWarsGameState;
import com.feeling.emotion.phpassion.playingfield.PlayingField;

/**
 * Initialization of StoneWarsGameEngine
 */
public class StoneWarsGameEngineBuilder extends GameEngineBuilder {

    // create data ----

    @NotNull
    @Override
    protected GameState createGameState() {
        return StoneWarsGameState.create();
    }

    @Override
    protected List<IPlaceAction> createPlaceActions() {
        List<IPlaceAction> list = super.createPlaceActions();
        list.add(getCheck4VictoryPlaceAction());
        return list;
    }

    @NotNull
    protected Check4VictoryPlaceAction getCheck4VictoryPlaceAction() {
        return new Check4VictoryPlaceAction();
    }

    @Override
    protected void initPlayingField(PlayingField playingField) {
        super.initPlayingField(playingField);
        ((GameDefinition) definition).fillStartPlayingField(playingField);
    }

    // create game engine ----

    @NotNull
    protected StoneWarsGameEngine createGameEngine(GameEngineModel model) {
        return new StoneWarsGameEngine(model);
    }

    // new game ----

    @Override
    protected void initNextGamePieceForNewGame() {
        if (((StoneWarsGameState) gs).getPlanet().isNextGamePieceResetedForNewGame()) {
            nextGamePiece.reset();
        } else { // Daily Planet
            nextGamePiece.load();
        }
    }

    // load game ----

    @Override
    protected void loadGame() {
        super.loadGame();
        if (gs.isLostGame()) {
            gameEngine.showScoreAndMoves(); // display game over text
            playingField.gameOver();
        }
    }
}
