package com.feeling.emotion.phpassion.game.stonewars.deathstar;

import androidx.annotation.NonNull;

import org.jetbrains.annotations.NotNull;

import com.feeling.emotion.phpassion.game.GameEngineModel;
import com.feeling.emotion.phpassion.game.place.DoNothingPlaceAction;
import com.feeling.emotion.phpassion.game.place.IPlaceAction;
import com.feeling.emotion.phpassion.game.stonewars.StoneWarsGameEngineBuilder;
import com.feeling.emotion.phpassion.game.stonewars.deathstar.place.DeathStarCheck4VictoryPlaceAction;
import com.feeling.emotion.phpassion.game.stonewars.place.Check4VictoryPlaceAction;
import com.feeling.emotion.phpassion.gamestate.SpielstandDAO;

/**
 * Initialization of DeathStarGameEngine
 */
public class DeathStarGameEngineBuilder extends StoneWarsGameEngineBuilder {
    private static final SpielstandDAO dao = new SpielstandDAO();

    // create data ----

    @NonNull
    @Override
    protected IPlaceAction getDetectOneColorAreaAction() {
        // no OneColor bonus
        return new DoNothingPlaceAction();
    }

    @NotNull
    @Override
    protected Check4VictoryPlaceAction getCheck4VictoryPlaceAction() {
        return new DeathStarCheck4VictoryPlaceAction(); // Bei Sieg die Holders clearen.
    }

    // create game engine ----

    @NotNull
    @Override
    protected DeathStarGameEngine createGameEngine(GameEngineModel model) {
        return new DeathStarGameEngine(model);
    }

    // new game ----

    @Override
    protected void doNewGame() {
        super.doNewGame();
        gravitation.setFirstGravitationPlayed(true);
    }

    @Override
    protected void initNextGamePieceForNewGame() {
        nextGamePiece.load();
    }

    // load game ----

    @Override
    protected void loadGame() {
        super.loadGame();
        if (holders.is123Empty()) {
            gameEngine.offer(true/*hier ausnahmsweise true!*/);
        }
        gameEngine.checkGame(); // checkGame must be after offer
    }

    @Override
    protected void checkGameAfterLoad() { //
    }
}
