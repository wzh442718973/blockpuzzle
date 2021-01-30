package de.mwvb.blockpuzzle.gamestate;

import de.mwvb.blockpuzzle.game.GameEngineFactory;
import de.mwvb.blockpuzzle.gamedefinition.GameDefinition;
import de.mwvb.blockpuzzle.planet.IPlanet;
import de.mwvb.blockpuzzle.planet.SpaceObjectStateService;

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
        this.definition = planet.getSelectedGame();
    }

    public static StoneWarsGameState create() {
        IPlanet planet = new GameEngineFactory().getPlanet();
        Spielstand ss = new SpielstandDAO().load(planet);
        return new StoneWarsGameState(planet, planet.getGameDefinitions().indexOf(planet.getSelectedGame()), ss);
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
        ss.setState(GamePlayState.WON_GAME);
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