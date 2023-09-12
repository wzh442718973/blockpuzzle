package com.feeling.emotion.phpassion.game.stonewars.deathstar.place;

import com.feeling.emotion.phpassion.game.GameEngineInterface;
import com.feeling.emotion.phpassion.game.stonewars.place.Check4VictoryPlaceAction;
import com.feeling.emotion.phpassion.gamestate.StoneWarsGameState;

public class DeathStarCheck4VictoryPlaceAction extends Check4VictoryPlaceAction {

    @Override
    protected void check4Liberation(GameEngineInterface game, StoneWarsGameState gs) {
        game.clearAllHolders(); // Spieler soll keine Spielsteine mehr setzen können. Das bewirkt außerdem auch, dass offer() aufgerufen wird und somit nextGame().
    }
}
