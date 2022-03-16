package euromillions;

import java.util.Arrays;
import java.util.Objects;

import sets.SetOfNaturals;

import java.util.Random;

/**
 * A set of 5 numbers and 2 starts according to the Euromillions ranges.
 *
 * @author ico0
 */
public class Dip {


    static final int NUMBER_OF_STARS = 2;
    static final int NUMBER_OF_NUMBERS = 5;
    static final int MIN_VALUE_STAR = 1;
    static final int MAX_VALUE_STAR = 12;
    static final int MIN_VALUE_NUMBER = 1;
    static final int MAX_VALUE_NUMBER = 50;

    private SetOfNaturals numbers;
    private SetOfNaturals starts;

    public Dip() {
        numbers = new SetOfNaturals();
        starts = new SetOfNaturals();
    }

    public Dip(int[] arrayOfNumbers, int[] arrayOfStarts) {
        this();

        if (NUMBER_OF_NUMBERS == arrayOfNumbers.length && NUMBER_OF_STARS == arrayOfStarts.length) {
            if (Arrays.stream(arrayOfNumbers).anyMatch(i -> i > MAX_VALUE_NUMBER || i < MIN_VALUE_NUMBER))
                throw new IllegalArgumentException("invalid elements in numbers (should be in range of "
                        + MIN_VALUE_NUMBER +" to " + MAX_VALUE_NUMBER +")");
            numbers.add(arrayOfNumbers);

            if (Arrays.stream(arrayOfStarts).anyMatch(i -> i > MAX_VALUE_STAR || i < MIN_VALUE_STAR))
                throw new IllegalArgumentException("invalid elements in stars (should be in range of "
                        + MIN_VALUE_NUMBER +" to " + MAX_VALUE_STAR +")");
            starts.add(arrayOfStarts);
        } else {
            throw new IllegalArgumentException("wrong number of elements in numbers/stars");
        }

    }

    public SetOfNaturals getNumbersColl() {
        return numbers;
    }

    public SetOfNaturals getStarsColl() {
        return starts;
    }

    public static Dip generateRandomDip() {
        Random generator = new Random();

        Dip randomDip = new Dip();
        for (int i = 0; i < NUMBER_OF_NUMBERS; ) {
            int candidate = generator.nextInt(MAX_VALUE_NUMBER - 1) + 1;
            if (!randomDip.getNumbersColl().contains(candidate)) {
                randomDip.getNumbersColl().add(candidate);
                i++;
            }
        }
        for (int i = 0; i < NUMBER_OF_STARS; ) {
            int candidate = generator.nextInt(MAX_VALUE_STAR - 1) + 1;
            if (!randomDip.getStarsColl().contains(candidate)) {
                randomDip.getStarsColl().add(candidate);
                i++;
            }
        }
        return randomDip;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 29 * hash + Objects.hashCode(this.numbers);
        hash = 29 * hash + Objects.hashCode(this.starts);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Dip other = (Dip) obj;
        if (!Objects.equals(this.numbers, other.numbers)) {
            return false;
        }
        return Objects.equals(this.starts, other.starts);
    }


    /**
     * prepares a string representation of the data structure, formated for
     * printing
     *
     * @return formatted string with data
     */
    public String format() {
        StringBuilder sb = new StringBuilder();
        sb.append("N[");
        for (int number : getNumbersColl()) {
            sb.append(String.format("%3d", number));
        }
        sb.append("] S[");
        for (int star : getStarsColl()) {
            sb.append(String.format("%3d", star));
        }
        return sb.append("]").toString();
    }
}
