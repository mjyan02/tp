package seedu.address.model.commons;

/**
 * Represents a Property's price in REconnect.
 * Guarantees: immutable; is valid as declared in {@link #isValidPrice(Long)}
 */
public class Price {

    public static final String MESSAGE_CONSTRAINTS =
            // Upper and lower limits prevents adding of unrealistic price values
            "Price should only contain positive numbers (in S$ thousands) between 3 to 6 digits";

    public static final String VALIDATION_REGEX = "^[1-9]\\d{2,5}$";

    public final Long value;

    /**
     * Constructs a {@code Price}.
     *
     * @param price A valid price value
     */
    public Price(Long price) {
        this.value = price;
    }

    /**
     * Returns true if the given price is more than this price.
     */
    public boolean isMoreThan(Price other) {
        return this.value > other.value;
    }

    /**
     * Returns true if the given price is less than this price.
     */
    public boolean isLessThan(Price other) {
        return this.value < other.value;
    }

    /**
     * Returns true if a given string is a valid price.
     */
    public static boolean isValidPrice(Long test) {
        if (test == null || test <= 0) {
            return false;
        }

        String testString = test.toString();
        return testString.matches(VALIDATION_REGEX);
    }

    /**
     * Returns the price if present, otherwise returns "N/A".
     */
    @Override
    public String toString() {
        return value.toString();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof Price otherPrice)) {
            return false;
        }

        return value.equals(otherPrice.value);
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }
}
