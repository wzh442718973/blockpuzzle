package de.mwvb.blockpuzzle.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.View;

import de.mwvb.blockpuzzle.R;
import de.mwvb.blockpuzzle.logic.Game;
import de.mwvb.blockpuzzle.logic.spielstein.GamePiece;

/**
 * Im unteren Bereich die View Komponente, die ein Spielstein (oder einen leeren Spielstein) enthält.
 * Aus der TeilView erfolgt die Drag-and-Drop Operation.
 * Die 4. TeilView ist das Parking Area zum vorübergehenden Ablegen eines Teil.
 * Teil ist der alte Name für Spielstein; daher TeilView.
 */
@SuppressLint("ViewConstructor")
public class GamePieceView extends View {
    private final boolean parking;
    private final Paint p_normal = new Paint(); // TODO final
    private final Paint p_grey = new Paint();
    private final Paint p_drehmodus = new Paint();
    private final Paint p_parking = new Paint();
    private final int index;
    private final SharedPreferences pref;
    private GamePiece gamePiece = null;
    /** grey wenn Teil nicht dem Quadrat hinzugefügt werden kann, weil kein Platz ist */
    private boolean grey = false; // braucht nicht zu persistiert werden
    private boolean drehmodus = false; // wird nicht persistiert
    private boolean dragMode = false; // wird nicht persistiert

    public GamePieceView(Context context, int index, boolean parking, SharedPreferences pref) {
        super(context);
        this.index = index;
        this.parking = parking;
        this.pref = pref;

        p_normal.setColor(getResources().getColor(R.color.colorNormal));
        p_grey.setColor(getResources().getColor(R.color.colorGrey));
        p_drehmodus.setColor(getResources().getColor(R.color.colorDrehmodus));
        p_parking.setColor(getResources().getColor(R.color.colorParking));
    }

    public void setGamePiece(GamePiece v) {
        gamePiece = v;
        draw();
    }

    public GamePiece getGamePiece() {
        return gamePiece;
    }

    // Methode nicht löschen! Die wird als isGrey in MainActivty verwendet.
    public void setGrey(boolean v) {
        grey = v;
    }

    // Methode nicht löschen!
    public boolean isGrey() {
        return grey;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        final float f = getResources().getDisplayMetrics().density;
        int br = PlayingFieldView.w / Game.blocks; // 60px, auf Handy groß = 36
        if (!dragMode) br /= 2;
        float p = br * 0.1f;
        if (parking && !dragMode) {
            canvas.drawRect(0, 0, br * GamePiece.max * f, br * GamePiece.max * f, p_parking);
        }
        if (gamePiece != null) {
            Paint fuellung;
            if (grey) {
                fuellung = p_grey;
            } else if (drehmodus) {
                fuellung = p_drehmodus;
            } else {
                fuellung = p_normal;
            }
            // TODO Ist das doppelter Code zu SpielfeldView?
            for (int x = 0; x < GamePiece.max; x++) {
                for (int y = 0; y < GamePiece.max; y++) {
                    if (gamePiece.filled(x, y)) {
                        float tx = x * br, ty = y * br;
                        canvas.drawRect((tx + p) * f, (ty + p) * f,
                                (tx + br - p) * f, (ty + br - p) * f, fuellung);
                    }
                }
            }
        }
        super.onDraw(canvas);
    }

    public void draw() {
        invalidate();
        requestLayout();
    }

    public void startDragMode() {
        dragMode = true;
        setVisibility(View.INVISIBLE);
    }

    public void endDragMode() {
        dragMode = false;
        setVisibility(View.VISIBLE);
    }

    public void setDrehmodus(boolean d) {
        drehmodus = d;
        draw();
    }

    public void rotate() {
        if (gamePiece != null) {
            gamePiece.rotateToRight();
            draw();
            write();
        }
    }

    @Override
    public boolean performClick() {
        // wegen Warning in MainActivity.initClickListener()
        return super.performClick();
    }

    public void write() {
        SharedPreferences.Editor edit = pref.edit();
        edit.putString(name(index, false), gamePiece == null ? "null" : gamePiece.getClass().getName());
        edit.putInt(name(index, true), gamePiece == null ? 0 : gamePiece.getRotated());
        edit.apply();
    }

    public void read() {
        String cn = pref.getString(name(index, false), "null");
        int rotated = pref.getInt(name(index, true), 0);

        if (cn == null || cn.equals("null") || cn.isEmpty()) {
            gamePiece = null;
        } else {
            try {
// TODO Baustelle   .class -> so geht das nicht mehr!  Ich muss die GamePiece-Matrix speichern!
                gamePiece = (GamePiece) Class.forName(cn).newInstance();
            } catch (Throwable e) {
                throw new RuntimeException(e); // TODO
            }
            if (rotated < 0 || rotated >= 4) {
                throw new RuntimeException("Falscher Wert für rotated: " + rotated);
            }
            for (int i = 1; i <= rotated; i++) {
                gamePiece.rotateToRight();
            }
        }

        draw();
    }

    private String name(int index, boolean rotated) {
        return "GamePieceView" + index + (rotated ? ".rotated" : ".class");
    }
}
