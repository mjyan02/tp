package seedu.address.model.client;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.commands.CommandTestUtil.VALID_ADDRESS_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_CLIENT_NAME_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_EMAIL_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_PHONE_BOB;
import static seedu.address.testutil.TypicalClients.ALICE;
import static seedu.address.testutil.TypicalClients.BOB;

import org.junit.jupiter.api.Test;

import seedu.address.testutil.ClientBuilder;

public class ClientTest {

    @Test
    public void isSameClient() {
        // same object -> returns true
        assertTrue(ALICE.isSameClient(ALICE));

        // null -> returns false
        assertFalse(ALICE.isSameClient(null));

        // same name, all other attributes different -> returns true
        Client editedAlice = new ClientBuilder(ALICE).withPhone(VALID_PHONE_BOB).withEmail(VALID_EMAIL_BOB)
                .withAddress(VALID_ADDRESS_BOB).build();
        assertTrue(ALICE.isSameClient(editedAlice));

        // different name, all other attributes same -> returns true
        editedAlice = new ClientBuilder(ALICE).withClientName(VALID_CLIENT_NAME_BOB).build();
        assertTrue(ALICE.isSameClient(editedAlice));

        // name differs in case, all other attributes same -> returns true
        Client editedBob = new ClientBuilder(BOB).withClientName(VALID_CLIENT_NAME_BOB.toLowerCase()).build();
        assertTrue(BOB.isSameClient(editedBob));

        // name has trailing spaces, all other attributes same -> returns true
        String nameWithTrailingSpaces = VALID_CLIENT_NAME_BOB + " ";
        editedBob = new ClientBuilder(BOB).withClientName(nameWithTrailingSpaces).build();
        assertTrue(BOB.isSameClient(editedBob));
    }

    @Test
    public void equals() {
        // same values -> returns true
        Client aliceCopy = new ClientBuilder(ALICE).build();
        assertTrue(ALICE.equals(aliceCopy));

        // same object -> returns true
        assertTrue(ALICE.equals(ALICE));

        // null -> returns false
        assertFalse(ALICE.equals(null));

        // different type -> returns false
        assertFalse(ALICE.equals(5));

        // different client -> returns false
        assertFalse(ALICE.equals(BOB));

        // different name -> returns false
        Client editedAlice = new ClientBuilder(ALICE).withClientName(VALID_CLIENT_NAME_BOB).build();
        assertFalse(ALICE.equals(editedAlice));

        // different phone -> returns false
        editedAlice = new ClientBuilder(ALICE).withPhone(VALID_PHONE_BOB).build();
        assertFalse(ALICE.equals(editedAlice));

        // different email -> returns false
        editedAlice = new ClientBuilder(ALICE).withEmail(VALID_EMAIL_BOB).build();
        assertFalse(ALICE.equals(editedAlice));

        // different address -> returns false
        editedAlice = new ClientBuilder(ALICE).withAddress(VALID_ADDRESS_BOB).build();
        assertFalse(ALICE.equals(editedAlice));
    }

    @Test
    public void toStringMethod() {
        String expected = Client.class.getCanonicalName() + "{clientName=" + ALICE.getFullName() + ", phone="
                + ALICE.getPhone() + ", email=" + ALICE.getEmail() + ", address=" + ALICE.getAddress() + "}";
        assertEquals(expected, ALICE.toString());
    }
}
