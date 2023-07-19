package de.rubymc.database.mongodb;
/**
 * A simple generic class that represents a pair of values.
 *
 * @param <First> the type of the first value
 * @param <Last>  the type of the last value
 */
public class Pair<First, Last> {

    private final First first;
    private final Last last;
    /**
     * Constructs a new Pair with the specified first and last values.
     *
     * @param first the first value of the pair
     * @param last  the last value of the pair
     */
    public Pair(First first, Last last) {
        this.first = first;
        this.last = last;
    }

    /**
     * Retrieves the first value of the pair.
     *
     * @return the first value of the pair
     */
    public First getFirst() {
        return first;
    }

    /**
     * Retrieves the last value of the pair.
     *
     * @return the last value of the pair
     */
    public Last getLast() {
        return last;
    }
    /**
     * Checks if this pair is equal to another object.
     *
     * @param obj the object to compare with
     * @return true if the objects are equal, false otherwise
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }

        Pair<?, ?> pair = (Pair<?, ?>) obj;

        if (first != null ? !first.equals(pair.first) : pair.first != null) {
            return false;
        }
        return last != null ? last.equals(pair.last) : pair.last == null;
    }
    /**
     * Computes the hash code value for this pair.
     *
     * @return the hash code value for this pair
     */
    @Override
    public int hashCode() {
        int result = first != null ? first.hashCode() : 0;
        result = 31 * result + (last != null ? last.hashCode() : 0);
        return result;
    }
    /**
     * Returns a string representation of the pair.
     *
     * @return a string representation of the pair
     */
    @Override
    public String toString() {
        return "Pair{" +
                "first=" + first +
                ", last=" + last +
                '}';
    }
}
