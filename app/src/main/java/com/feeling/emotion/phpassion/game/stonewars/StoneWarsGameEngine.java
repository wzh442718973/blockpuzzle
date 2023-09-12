package com.feeling.emotion.phpassion.game.stonewars;

import com.feeling.emotion.phpassion.game.GameEngine;
import com.feeling.emotion.phpassion.game.GameEngineModel;
import com.feeling.emotion.phpassion.gamedefinition.GameDefinition;
import com.feeling.emotion.phpassion.gamestate.StoneWarsGameState;

/**
 * Stone Wars game engine
 */
public class StoneWarsGameEngine extends GameEngine {

    public StoneWarsGameEngine(GameEngineModel model) {
        super(model);
        GameDefinition definition = (GameDefinition) model.getDefinition();
        if (definition.getTerritoryName() == null) {
            model.getView().showPlanetNumber(myGS().getPlanet().getNumber());
        } else {
            model.getView().showTerritoryName(definition.getTerritoryName());
        }
    }

    @Override
    public boolean isNewGameButtonVisible() {
        return false;
    }

    @Override
    protected boolean isHandleNoGamePiecesAllowed() {
        return true;
    }

    @Override
    public void onEndGame(boolean wonGame, boolean stopGame) {
        super.onEndGame(wonGame, stopGame);
        if (!wonGame) { // lost game
            myGS().saveOwner(false); // owner is Orange Union or enemy
        }
    }

    private StoneWarsGameState myGS() {
        return (StoneWarsGameState) model.getGs();
    }
}
