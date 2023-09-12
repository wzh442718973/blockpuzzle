package com.feeling.emotion.phpassion.cluster;

public abstract class AbstractRoute implements IRoute {
    private final int from;
    private final int to;

    public AbstractRoute(int from, int to) {
        this.from = from;
        this.to = to;
    }

    @Override
    public int getFrom() {
        return from;
    }

    @Override
    public int getTo() {
        return to;
    }
}
