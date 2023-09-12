package com.feeling.emotion.phpassion.gamestate;

import com.feeling.emotion.phpassion.game.GameEngineFactory;
import com.feeling.emotion.phpassion.gamedefinition.GameDefinition;
import com.feeling.emotion.phpassion.planet.IPlanet;
import com.feeling.emotion.phpassion.planet.SpaceObjectStateService;

/**
 * GameState classes are immutable Spielstand wrapper. If something would change a new object would be created.
 */
public class StoneWarsGameState extends GameState {
    private final IPlanet planet;
    // SpaceObjectState wird nicht vorgehalten
    /** current game definition index */
    private final int index;
    /** current game definition */
    private final GameDefinition definition;

    protected StoneWarsGameState(IPlanet planet, int index, Spielstand ss) {
        super(ss);
        this.planet = planet;
        this.index = index;
        this.definition = planet.getGameDefinitions().get(index);
    }

    public static StoneWarsGameState create() {
        IPlanet planet = new GameEngineFactory().getPlanet();
        int index = planet.getCurrentGameDefinitionIndex(); // important: ensure that selectedGame is set
        Spielstand ss = new SpielstandDAO().load(planet, index);
        return new StoneWarsGameState(planet, index, ss);
    }

    @Override
    public GameDefinition createGameDefinition() {
        return getDefinition();
    }

    public IPlanet getPlanet() {
        return planet;
    }

    public int getIndex() {
        return index;
    }

    public GameDefinition getDefinition() {
        return definition;
    }

    @Override
    public void save() {
        new SpielstandDAO().save(planet, index, get());
    }

    public void setOwnerToMe() {
        Spielstand ss = get();
        ss.setOwnerName("");
        ss.setOwnerScore(0);
        ss.setOwnerMoves(0);
        save();
        saveOwner(true);
    }

    public void saveOwner(boolean owner) {
        new SpaceObjectStateService().saveOwner(planet, owner);
    }
}
