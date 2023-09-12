package com.feeling.emotion.phpassion.playingfield;

import com.feeling.emotion.phpassion.sound.SoundService;

public interface IPlayingFieldView {

    SoundService getSoundService();

    void setPlayingField(PlayingField playingField);

    void draw();

    void clearRows(FilledRows filledRows, Action action);

    void oneColor();

    void gravitation();
}
