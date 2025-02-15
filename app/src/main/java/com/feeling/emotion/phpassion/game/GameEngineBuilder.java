package com.feeling.emotion.phpassion.game;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import com.feeling.emotion.phpassion.block.BlockTypes;
import com.feeling.emotion.phpassion.game.place.ClearRowsPlaceAction;
import com.feeling.emotion.phpassion.game.place.DetectOneColorAreaAction;
import com.feeling.emotion.phpassion.game.place.EmptyScreenBonusPlaceAction;
import com.feeling.emotion.phpassion.game.place.GamePieceScorePlaceAction;
import com.feeling.emotion.phpassion.game.place.IPlaceAction;
import com.feeling.emotion.phpassion.game.place.IncMovesPlaceAction;
import com.feeling.emotion.phpassion.game.place.SendPlacedEventAction;
import com.feeling.emotion.phpassion.game.place.SpecialBlockBonusPlaceAction;
import com.feeling.emotion.phpassion.gamedefinition.OldGameDefinition;
import com.feeling.emotion.phpassion.gamepiece.Holders;
import com.feeling.emotion.phpassion.gamepiece.INextGamePiece;
import com.feeling.emotion.phpassion.gamestate.GameState;
import com.feeling.emotion.phpassion.gamestate.Spielstand;
import com.feeling.emotion.phpassion.global.Features;
import com.feeling.emotion.phpassion.playingfield.PlayingField;
import com.feeling.emotion.phpassion.playingfield.gravitation.GravitationData;

/**
 * Initialization of GameEngine
 */
public class GameEngineBuilder {
    public static final int blocks = 10; // TO-DO später mal die Zugriffe hier drauf prüfen
    protected final BlockTypes blockTypes = new BlockTypes(null);
    protected IGameView view;
    protected GameState gs;
    protected OldGameDefinition definition;
    protected GravitationData gravitation;
    protected PlayingField playingField;
    protected Holders holders;
    protected INextGamePiece nextGamePiece;

    protected GameEngine gameEngine;

    public final GameEngine build(final IGameView pView) {
        view = pView;

        // Create data ----
        gs = createGameState();
        definition = gs.createGameDefinition();

        playingField = new PlayingField(blocks);
        playingField.setView(view.getPlayingFieldView());

        holders = new Holders(view);
        gravitation = new GravitationData();

        nextGamePiece = definition.createNextGamePieceGenerator(gs);

        // Create game engine ----
        GameEngineModel model = new GameEngineModel(blocks, blockTypes, view, gs, definition, playingField, holders, createPlaceActions(), gravitation, nextGamePiece);
        gameEngine = createGameEngine(model);

        // Crushed
        if (definition.isCrushAllowed() || Features.developerMode) {
            playingField.setCrushed(gameEngine);
        }

        // init game ----
        // Gibt es einen Spielstand?
        if (gs.get().getScore() < 0) { // Nein
            newGame(); // Neues Spiel starten!
        } else {
            loadGame(); // Spielstand laden
        }
        return gameEngine;
    }

    // create data ----

    @NotNull
    protected GameState createGameState() {
        return GameState.create();
    }

    protected List<IPlaceAction> createPlaceActions() {
        List<IPlaceAction> ret = new ArrayList<>();
        ret.add(new SendPlacedEventAction());
        ret.add(getDetectOneColorAreaAction());
        ret.add(new IncMovesPlaceAction());
        ret.add(new GamePieceScorePlaceAction());
        ret.add(new SpecialBlockBonusPlaceAction());
        ret.add(new ClearRowsPlaceAction());
        ret.add(new EmptyScreenBonusPlaceAction());
        return ret;
    }

    @NotNull
    protected IPlaceAction getDetectOneColorAreaAction() {
        return new DetectOneColorAreaAction();
    }

    // create game engine ----

    @NotNull
    protected GameEngine createGameEngine(GameEngineModel model) {
        return new GameEngine(model);
    }

    // new game ----

    /** Benutzer startet freiwillig oder nach GameOver neues Spiel. */
    protected void newGame() {
        doNewGame();
        gameEngine.offer(true);
        gameEngine.save(); // TO-DO gs.save() in doNewGame() und save() hier; schauen ob das anders geht
    }
    protected void doNewGame() {
        gs.newGame();
        gravitation.init();
        initNextGamePieceForNewGame();

        initPlayingField(playingField);
        gameEngine.showScoreAndMoves();
        holders.clearParking();

        gs.save();
    }

    protected void initNextGamePieceForNewGame() {
        nextGamePiece.reset();
    }

    protected void initPlayingField(PlayingField playingField) {
        playingField.clear();
    }

    // load game ----

    protected void loadGame() {
        gameEngine.showScoreAndMoves();

        nextGamePiece.load();
        Spielstand ss = gs.get();
        gravitation.load(ss);
        playingField.load(ss);
        holders.load(ss);

        checkGameAfterLoad();
    }

    protected void checkGameAfterLoad() {
        gameEngine.checkGame();
    }
}
