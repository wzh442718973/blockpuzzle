package com.feeling.emotion.phpassion.block.special;

import android.view.View;

import java.util.List;
import java.util.Random;

import com.feeling.emotion.phpassion.block.ColorBlockDrawer;
import com.feeling.emotion.phpassion.block.IBlockDrawer;
import com.feeling.emotion.phpassion.gamepiece.GamePiece;
import com.feeling.emotion.phpassion.playingfield.QPosition;

public abstract class SpecialBock implements ISpecialBlock {
    protected static final Random rand = new Random(System.currentTimeMillis());
    private final int blockType;

    public SpecialBock(int blockType) {
        this.blockType = blockType;
    }

    @Override
    public int getBlockType() {
        return blockType;
    }

    @Override
    public IBlockDrawer getBlockDrawer(View view) {
        return ColorBlockDrawer.byRColor(view, getColor(), getColor_i(), getColor_ib());
    }

    public int getColor_i() {
        return getColor();
    }

    public int getColor_ib() {
        return getColor();
    }

    @Override
    public boolean isRelevant(GamePiece p) {
        return rand.nextInt(getRandomMax()) == 1;
    }

    protected abstract int getRandomMax();

    @Override
    public boolean process(GamePiece p) {
        List<QPosition> filledBlocks = p.getAllFilledBlocks();
        if (filledBlocks.isEmpty()) return false; // Programmschutz
        QPosition k = filledBlocks.get(rand.nextInt(filledBlocks.size())); // select block randomly
        process(p, k);
        return true;
    }

    protected void process(GamePiece p, QPosition k) {
        p.setBlockType(k.getX(), k.getY(), getBlockType());
    }

    @Override
    public void placed(GamePiece p, QPosition gpPos, QPosition k) { //
    }
}
