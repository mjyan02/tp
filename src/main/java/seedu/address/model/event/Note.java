package seedu.address.model.event;

/**
 * Represents a Event in the address book.
 * Guarantees: details are present and not null, field values are validated, immutable.
 * TODO: Implement this class
 * TODO: make all fields final
 */
public class Note {

    public static final String MESSAGE_CONSTRAINTS = "Note should not be blank.";
    private final String note;

    public Note(String note) {
        this.note = note;
    }

    @Override
    public String toString() {
        return note;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof Note note)) {
            return false;
        }

        return this.note.equals(note.note);
    }
}
