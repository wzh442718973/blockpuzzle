package com.feeling.emotion.phpassion.gamedefinition;

import androidx.annotation.NonNull;

import com.feeling.emotion.phpassion.game.TopButtonMode;
import com.feeling.emotion.phpassion.gamepiece.INextGamePiece;
import com.feeling.emotion.phpassion.gamepiece.NextGamePieceFromSet;
import com.feeling.emotion.phpassion.gamestate.GameState;
import com.feeling.emotion.phpassion.gamestate.ScoreChangeInfo;
import com.feeling.emotion.phpassion.gamestate.Spielstand;
import com.feeling.emotion.phpassion.global.messages.MessageObjectWithGameState;
import com.feeling.emotion.phpassion.planet.IPlanet;
import com.feeling.emotion.phpassion.playingfield.PlayingField;

/**
 * Stone Wars game definition
 */
public abstract class GameDefinition extends OldGameDefinition {
    private final int gamePieceSetNumber;
    /** R.string constant */
    private Integer territoryName;
    private LiberatedFeature libf = null;
    private IPlanet planet;

    public GameDefinition(int gamePieceSetNumber) {
        this.gamePieceSetNumber = gamePieceSetNumber;
    }

    public void setPlanet(IPlanet planet) {
        this.planet = planet;
    }

    public IPlanet getPlanet() {
        return planet;
    }

    // GAME DEFINITION ----

    @Override
    public int getGravitationStartRow() {
        return getPlanet().getGravitation();
    }

    @Override
    public INextGamePiece createNextGamePieceGenerator(GameState gs) {
        return new NextGamePieceFromSet(gamePieceSetNumber, gs);
    }

    public int getGamePieceSetNumber() {
        return gamePieceSetNumber;
    }

    public Integer getTerritoryName() {
        return territoryName;
    }

    public void setTerritoryName(Integer territoryName) {
        this.territoryName = territoryName;
    }

    /**
     * @return true: Sieg, false: nicht relevant oder kein Sieg
     */
    public boolean wonGameIfPlayingFieldIsEmpty() {
        return false;
    }

    /**
     * @return true: Die Moves-Anzeige der Score-Anzeige bevorzugen. false: andersrum
     */
    public boolean showMoves() {
        return false;
    }


    // GAME INIT PHASE ----

    /**
     * call p.draw() after filling something in
     */
    public void fillStartPlayingField(PlayingField p) { // Template method
    }


    // DISPLAY ----

    /**
     * Returns short game description
     * @param longDisplay true: normal length, false: a bit shorter
     * @return e.g. "CLassic Game MLS8k"
     */
    public abstract String getDescription(boolean longDisplay);


    // QUESTIONS AND EVENTS ----

    /**
     * @return true if planet or territory was liberated by player 1
     */
    public abstract boolean isLiberated(ILiberatedInfo info);

    /**
     * @param info data and services
     * @return MessageObjectWithGameState; info.messages.noMessage if there's no message to be displayed; not null
     */
    @NonNull
    public abstract MessageObjectWithGameState scoreChanged(ScoreChangeInfo info);

    public LiberatedFeature getLiberatedFeature() {
        return libf;
    }

    public void setLiberatedFeature(LiberatedFeature v) {
        libf = v;
    }

    /**
     * If there are no game pieces anymore the player has lost the game by default.
     * If you return true here you can let the player win the game.
     */
    public boolean isWonAfterNoGamePieces(Spielstand ss) {
        return false;
    }

    @Override
    public TopButtonMode getTopButtonMode() {
        return TopButtonMode.UNDO;
    }
}
