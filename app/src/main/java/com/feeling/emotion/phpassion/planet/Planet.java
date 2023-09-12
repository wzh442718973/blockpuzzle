package com.feeling.emotion.phpassion.planet;

import android.graphics.Paint;

import com.feeling.emotion.phpassion.R;
import com.feeling.emotion.phpassion.gamedefinition.GameDefinition;

public class Planet extends AbstractPlanet {
    public static Paint paint; // set during draw action

    public Planet(int number, int x, int y) {
        super(number, x, y, 5);
    }

    public Planet(int number, int x, int y, GameDefinition gameDefinition) {
        this(number, x, y, 5, gameDefinition);
    }

    public Planet(int number, int x, int y, int gravitation, GameDefinition gameDefinition) {
        super(number, x, y, gravitation, gameDefinition);
    }

    protected Planet(int number, int x, int y, int gravitation) {
        super(number, x, y, gravitation);
    }

    @Override
    public final int getRadius() {
        return 20;
    }

    @Override
    public Paint getPaint() {
        return paint;
    }

    @Override
    public int getName() {
        return R.string.planet;
    }
}
