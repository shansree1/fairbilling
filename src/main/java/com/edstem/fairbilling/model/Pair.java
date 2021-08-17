package com.edstem.fairbilling.model;

/**
 * generic pair (Tuple2).
 * @param <T>
 */
public class Pair<T> {

    /**
     * start of pair.
     */
    private T start;

    /**
     * end of pair.
     */
    private T end;

    public T getStart() {
        return start;
    }

    public void setStart(T start) {
        this.start = start;
    }

    public T getEnd() {
        return end;
    }

    public void setEnd(T end) {
        this.end = end;
    }

    public Pair(T start, T end) {
        this.start = start;
        this.end = end;
    }

    public Pair() {
    }
}
