package de.rubymc.database.mongodb;

public class Pair<First, Last> {

    private final First first;
    private final Last last;

    public Pair(First first, Last last) {
        this.first = first;
        this.last = last;
    }

    public Last getLast() {
        return last;
    }

    public First getFirst() {
        return first;
    }
}
