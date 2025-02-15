package com.feeling.emotion.phpassion.game.place;

import java.util.List;

import com.feeling.emotion.phpassion.playingfield.OneColorAreaDetector;
import com.feeling.emotion.phpassion.playingfield.QPosition;

public class DetectOneColorAreaAction implements IPlaceAction {

    @Override
    public void perform(PlaceActionModel info) {
        List<QPosition> r = new OneColorAreaDetector(info.getPlayingField(), 20).getOneColorArea();
        if (r == null) {
            return;
        }
        info.getPlayingField().makeOldColor(); // 10 -> 11, plays also one color sound
        for (QPosition k : r) {
            info.getPlayingField().set(k.getX(), k.getY(), 10);
        }
        int bonus = r.size() * 5;
        if (bonus < 100) {
            bonus = 100;
        }
        info.getGs().addScore(bonus);
    }
}
