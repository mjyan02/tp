package seedu.address.logic.commands.client;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_ADDRESS;
import static seedu.address.logic.parser.CliSyntax.PREFIX_CLIENT_NAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_EMAIL;
import static seedu.address.logic.parser.CliSyntax.PREFIX_PHONE;

import seedu.address.commons.util.ToStringBuilder;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.AddCommand;
import seedu.address.logic.commands.CommandResult;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.logic.parser.Prefix;
import seedu.address.model.Model;
import seedu.address.model.client.Client;

/**
 * Adds a client to REconnect.
 */
public class AddClientCommand extends AddCommand<Client> {

    public static final String COMMAND_WORD = "add_client";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Adds a client to REconnect. "
        + "Parameters: "
        + PREFIX_CLIENT_NAME + "CLIENT_NAME "
        + PREFIX_PHONE + "PHONE "
        + "[" + PREFIX_EMAIL + "EMAIL] "
        + "[" + PREFIX_ADDRESS + "ADDRESS]\n"
        + "Example: " + COMMAND_WORD + " "
        + PREFIX_CLIENT_NAME + "John Doe "
        + PREFIX_PHONE + "98765432 "
        + PREFIX_EMAIL + "johnd@example.com "
        + PREFIX_ADDRESS + "311, Clementi Ave 2, #02-25";

    public static final String MESSAGE_SUCCESS = "New client added: %1$s";
    public static final String MESSAGE_DUPLICATE_CLIENT = "This client already exists in REconnect";

    /**
     * Creates an AddCommand to add the specified {@code Client}
     */
    public AddClientCommand(Client client) {
        super(client);
    }

    /**
     * Adds a command word and its associated prefixes to the command word map.
     */
    public static void addCommandWord() {
        Prefix[] parameterPrefixes = {
            PREFIX_CLIENT_NAME,
            PREFIX_PHONE,
            PREFIX_EMAIL,
            PREFIX_ADDRESS
        };
        initialiseCommandWord(COMMAND_WORD, parameterPrefixes);
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);

        if (model.hasClient(toAdd)) {
            throw new CommandException(MESSAGE_DUPLICATE_CLIENT);
        }

        model.addClient(toAdd);
        return new CommandResult(String.format(MESSAGE_SUCCESS, Messages.formatClient(toAdd)));
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof AddClientCommand otherAddCommand)) {
            return false;
        }

        return toAdd.equals(otherAddCommand.toAdd);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
            .add("toAdd", toAdd)
            .toString();
    }
}
