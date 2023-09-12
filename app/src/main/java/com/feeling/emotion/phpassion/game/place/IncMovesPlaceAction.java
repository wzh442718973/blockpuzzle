package com.feeling.emotion.phpassion.game.place;

public class IncMovesPlaceAction implements IPlaceAction {

    @Override
    public void perform(PlaceActionModel info) {
        info.getGs().incMoves();
    }
}
