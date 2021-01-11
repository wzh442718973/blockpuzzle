package de.mwvb.blockpuzzle.planet;

import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Paint;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import de.mwvb.blockpuzzle.R;
import de.mwvb.blockpuzzle.cluster.ClusterView;
import de.mwvb.blockpuzzle.gamedefinition.GameDefinition;
import de.mwvb.blockpuzzle.persistence.IPersistence;

public abstract class AbstractPlanet extends AbstractSpaceObject implements IPlanet {
    // Stammdaten
    private final int gravitation;
    private final List<GameDefinition> gameDefinitions = new ArrayList<>();
    public static Paint ownerMarkerPaint; // set during draw action
    // Bewegungsdaten
    private boolean owner = false;
    // Bewegungsdaten, nicht persistent
    private GameDefinition selectedGame = null;
    private String infoText1;
    private String infoText2;
    private String infoText3;

    public AbstractPlanet(int number, int x, int y, int gravitation) {
        super(number, x, y);
        this.gravitation = gravitation;
    }

    public AbstractPlanet(int number, int x, int y, int gravitation, GameDefinition gameDefinition) {
        this(number, x, y, gravitation);
        gameDefinitions.add(gameDefinition);
    }

    @Override
    public int getGravitation() {
        return gravitation;
    }

    @Override
    public boolean isSelectable() {
        return true;
    }

    @Override
    public boolean isDataExchangeRelevant() {
        return true;
    }

    @Override
    public boolean isShowCoordinates() {
        return true;
    }

    @Override
    public String getInfo(IPersistence persistence, Resources resources) {
        String info = resources.getString(getPlanetTypeResId()) + " #" + getNumber() + ", " + resources.getString(R.string.gravitation) + " " + getGravitation();
        if (getGameDefinitions().size() > 1) {
            getCurrentGameDefinitionIndex(persistence); // ensure game def is selected
            info += "\n" + resources.getString(getSelectedGame().getTerritoryName());
        }
        return info;
    }

    protected abstract int getPlanetTypeResId();

    @Override
    public String getGameInfo(IPersistence per, Resources resources, int gi) {
        if (hasGames()) {
            getCurrentGameDefinitionIndex(per);
            GameDefinition s = gi >= 0 ? getGameDefinitions().get(gi) : getSelectedGame();
            String info = s.getInfo(); // Game definition

            // Scores
            per.setGameID(this, gameDefinitions.indexOf(s));

            int score = per.loadScore();
            int moves = per.loadMoves();
            if (score > 0) {
                info += "\n" + resources.getString(R.string.yourScoreYourMoves, thousand(score), thousand(moves));
            }

            int otherScore = per.loadOwnerScore();
            int otherMoves = per.loadOwnerMoves();
            if (otherScore > 0) {
                info += "\n" + resources.getString(R.string.scoreOfMoves, per.loadOwnerName(), thousand(otherScore), thousand(otherMoves));
            }

            // Liberated?
            if (s.isLiberated(score, moves, otherScore, otherMoves, per, true)) {
                if (userMustSelectTerritory()) {
                    info += "\n" + resources.getString(R.string.liberatedTerritoryByYou);
                } else {
                    info += "\n" + resources.getString(R.string.liberatedPlanetByYou);
                }
            }
            return info;
        } else {
            return resources.getString(R.string.planetNeedsNoLiberation);
        }
    }

    public static String thousand(int n) {
        return new DecimalFormat("#,##0").format(n);
    }

    @Override
    public void draw(Canvas canvas, float f) {
        // draw planet
        canvas.drawCircle(getX() * ClusterView.w * f, getY() * ClusterView.w * f, getRadius() * f, getPaint());

        // draw owner mark
        if (isOwner()) {
            float ax = getX() * ClusterView.w * f + getRadius() * getOwnerMarkXFactor() * f;
            float ay = getY() * ClusterView.w * f - getRadius() * 0.7f * f;
            float bx = ax + 5 * f;
            float by = ay + 5 * f;
            float cx = bx + 5 * f;
            float cy = ay - 10 * f;
            canvas.drawLine(ax, ay, bx, by, ownerMarkerPaint);
            canvas.drawLine(bx, by, cx, cy, ownerMarkerPaint);
        }
    }

    protected abstract Paint getPaint();

    protected float getOwnerMarkXFactor() {
        return 1f;
    }

    @Override
    public boolean isOwner() {
        return owner;
    }

    @Override
    public void setOwner(boolean owner) {
        this.owner = owner;
    }

    public List<GameDefinition> getGameDefinitions() {
        return gameDefinitions;
    }

    @Override
    public boolean hasGames() {
        return !gameDefinitions.isEmpty();
    }

    @Override
    public boolean userMustSelectTerritory() {
        return getGameDefinitions().size() > 1;
    }

    @Override
    public boolean isNextGamePieceResetedForNewGame() {
        return true;
    }

    @Override
    public int getNewLiberationAttemptButtonTextResId() {
        return R.string.newLiberationAttempt;
    }

    @Override
    public int getNewLiberationAttemptQuestionResId() {
        return R.string.newLiberationAttemptQuestion;
    }

    @Override
    public int getCurrentGameDefinitionIndex(IPersistence persistence) {
        return getGameDefinitions().indexOf(getSelectedGame());
    }

    @Override
    public GameDefinition getSelectedGame() {
        return selectedGame == null ? gameDefinitions.get(0) : selectedGame;
    }

    @Override
    public void setSelectedGame(GameDefinition selectedGame) {
        if (!gameDefinitions.contains(selectedGame)) {
            throw new RuntimeException("Given selectedGame is not known for this planet!");
        }
        this.selectedGame = selectedGame;
    }

    @Override
    public String getInfoText(int lineNumber) {
        if (lineNumber == 1) return infoText1;
        if (lineNumber == 2) return infoText2;
        if (lineNumber == 3) return infoText3;
        return "";
    }

    public void setInfoText1(String infoText1) {
        this.infoText1 = infoText1;
    }

    public void setInfoText2(String infoText2) {
        this.infoText2 = infoText2;
    }

    public void setInfoText3(String infoText3) {
        this.infoText3 = infoText3;
    }
}
