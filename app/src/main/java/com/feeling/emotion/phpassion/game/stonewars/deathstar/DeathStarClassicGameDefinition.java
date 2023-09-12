package com.feeling.emotion.phpassion.game.stonewars.deathstar;

import java.util.Random;

import com.feeling.emotion.phpassion.block.BlockTypes;
import com.feeling.emotion.phpassion.game.GameEngineBuilder;
import com.feeling.emotion.phpassion.game.TopButtonMode;
import com.feeling.emotion.phpassion.gamedefinition.ClassicGameDefinition;
import com.feeling.emotion.phpassion.gamepiece.GamePiece;
import com.feeling.emotion.phpassion.gamepiece.INextGamePiece;
import com.feeling.emotion.phpassion.gamepiece.INextRound;
import com.feeling.emotion.phpassion.gamepiece.NextGamePieceAdapter;
import com.feeling.emotion.phpassion.gamepiece.NextGamePieceFromSet;
import com.feeling.emotion.phpassion.gamestate.GameState;
import com.feeling.emotion.phpassion.gamestate.Spielstand;
import com.feeling.emotion.phpassion.gamestate.SpielstandDAO;
import com.feeling.emotion.phpassion.gamestate.StoneWarsGameState;
import com.feeling.emotion.phpassion.global.Features;
import com.feeling.emotion.phpassion.global.messages.MessageFactory;
import com.feeling.emotion.phpassion.global.messages.MessageObjectWithGameState;
import com.feeling.emotion.phpassion.playingfield.PlayingField;

public class DeathStarClassicGameDefinition extends ClassicGameDefinition {
    private static final int NEXTROUND_INDEX = 99; // Ich will eine eigene Datei für den Todesstern-NextRound-Wert.
    private static final Spielstand nextRoundSS = new Spielstand();
    private boolean won = false; // TO-DO persistieren

    public DeathStarClassicGameDefinition(int gamePieceSetNumber, int minimumLiberationScore, int name) {
        super(gamePieceSetNumber, minimumLiberationScore);
        setTerritoryName(name);
    }

    @Override
    public TopButtonMode getTopButtonMode() {
        // Wegen dem Reaktorwechsel ist eine Undo-Funktion schwierig.
        return TopButtonMode.NO_BUTTON;
    }

    @Override
    public INextGamePiece createNextGamePieceGenerator(GameState gs) {
        // Spielsteine sollen nicht pro Reaktor sein, sondern für Todesstern.
        final DeathStar planet = (DeathStar) ((StoneWarsGameState) gs).getPlanet();
        final SpielstandDAO dao = new SpielstandDAO();
        INextRound persistence = new INextRound() {
            @Override
            public void saveNextRound(int val) {
                writeNextRound(val, dao);
            }

            @Override
            public int getNextRound() {
                return dao.load(planet, NEXTROUND_INDEX).getNextRound();
            }
        };
        NextGamePieceFromSet nextGamePiece = new NextGamePieceFromSet(planet.getGameDefinitions().get(0).getGamePieceSetNumber(), persistence);
        // Change color of all game pieces. Each reactor has its own color.
        return new NextGamePieceAdapter(nextGamePiece) {
            @Override
            public GamePiece next(BlockTypes blockTypes) {
                GamePiece gp = super.next(blockTypes);
                gp.color(getColor());
                return edit(gp);
            }

            private int getColor() {
                switch (planet.getGameIndex()) {
                    case 0:
                        if (Features.developerMode) return 6; // yellow
                        return 11; // dark blue for 1st reactor
                    case 1:  return  4; // blue for 2nd reactor
                    default:
                        if (Features.developerMode) return 3; // red
                        return  5; // pink for last reactor
                }
            }

            private GamePiece edit(GamePiece gp) {
                // Es soll keine 1er geben! Ersetze alle 1er durch (Todesstern-)Kreuze.
                boolean einser = true;
                for (int x = 0; x < GamePiece.max; x++) {
                    for (int y = 0; y < GamePiece.max; y++) {
                        int value = gp.getBlockType(x, y);
                        if (x == 2 && y == 2) {
                            if (value == 0) {
                                einser = false;
                            }
                        } else {
                            if (value != 0) {
                                einser = false;
                            }
                        }
                    }
                }
                if (einser) {
                    System.out.println("ist einser!");
                    gp.setBlockType(2 - 1, 2, 6);
                    gp.setBlockType(2, 2 - 1, 6);
                    gp.setBlockType(2, 2 + 1, 6);
                    gp.setBlockType(2 + 1, 2, 6);
                    gp.setName("Todessternkreuz");
                }
                return gp;
            }
        };
    }

    public void writeNextRound(int val, SpielstandDAO dao) {
        nextRoundSS.setNextRound(val);
        dao.save(MilkyWayCluster.INSTANCE.get(), NEXTROUND_INDEX, nextRoundSS);
    }

    @Override
    protected MessageObjectWithGameState getPlanetLiberatedText(MessageFactory messages) {
        won = true;
        return messages.getDeathStarDestroyed();
    }

    @Override
    protected MessageObjectWithGameState getTerritoryLiberatedText(MessageFactory messages) {
        won = true;
        return messages.getReactorDestroyed();
    }

    public boolean isWon() {
        return won;
    }

    public void setWon(boolean pWon) {
        won = pWon;
    }

    @Override
    public void fillStartPlayingField(PlayingField p) {
        Random random = new Random();
        int max = 20;
        if (Features.deathstarDeveloperMode) {
            max = 3;
        }
        for (int i = 0; i < max; i++) {
            int x = random.nextInt(GameEngineBuilder.blocks);
            int y = random.nextInt(GameEngineBuilder.blocks);
            int c = random.nextInt(7) + 1;
            p.set(x, y, c);
        }
    }

    @Override
    public boolean isCrushAllowed() {
        return false;
    }
}
