package seedu.address.logic.commands.property;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_ADDRESS;
import static seedu.address.logic.parser.CliSyntax.PREFIX_DESCRIPTION;
import static seedu.address.logic.parser.CliSyntax.PREFIX_OWNER;
import static seedu.address.logic.parser.CliSyntax.PREFIX_PRICE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_PROPERTY_NAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_SIZE;
import static seedu.address.model.Model.PREDICATE_SHOW_ALL_PROPERTIES;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.logging.Logger;

import seedu.address.commons.core.LogsCenter;
import seedu.address.commons.core.index.Index;
import seedu.address.commons.util.CollectionUtil;
import seedu.address.commons.util.ToStringBuilder;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.CommandResult;
import seedu.address.logic.commands.EditCommand;
import seedu.address.logic.commands.EditDescriptor;
import seedu.address.logic.commands.deal.UpdateDealCommand;
import seedu.address.logic.commands.deal.UpdateDealCommand.UpdateDealDescriptor;
import seedu.address.logic.commands.event.EditEventCommand;
import seedu.address.logic.commands.event.EditEventCommand.EditEventDescriptor;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.logic.parser.Prefix;
import seedu.address.model.Model;
import seedu.address.model.client.Client;
import seedu.address.model.client.ClientName;
import seedu.address.model.commons.Address;
import seedu.address.model.commons.Price;
import seedu.address.model.deal.Deal;
import seedu.address.model.event.Event;
import seedu.address.model.property.Description;
import seedu.address.model.property.Property;
import seedu.address.model.property.PropertyName;
import seedu.address.model.property.Size;

/**
 * Edits the details of an existing property in REconnect.
 */
public class EditPropertyCommand extends EditCommand<Property> {

    public static final String COMMAND_WORD = "edit_property";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Edits the details of the property identified "
            + "by the index number used in the displayed property list. "
            + "Existing values will be overwritten by the input values.\n"
            + "Parameters: INDEX (must be a positive integer) "
            + "[" + PREFIX_PROPERTY_NAME + "PROPERTY_NAME] "
            + "[" + PREFIX_OWNER + "OWNER_ID] "
            + "[" + PREFIX_ADDRESS + "ADDRESS] "
            + "[" + PREFIX_PRICE + "PRICE (in S$ thousands)] "
            + "[" + PREFIX_SIZE + "SIZE (in square feet)] "
            + "[" + PREFIX_DESCRIPTION + "DESCRIPTION] \n"
            + "Example: " + COMMAND_WORD + " 1 "
            + PREFIX_ADDRESS + "234 Maple Street "
            + PREFIX_PRICE + "2000";

    public static final String MESSAGE_EDIT_PROPERTY_SUCCESS = "Edited Property: %1$s";
    public static final String MESSAGE_NOT_EDITED = "At least one field to edit must be provided.";
    public static final String MESSAGE_DUPLICATE_PROPERTY = "This property already exists in REconnect.";
    public static final String MESSAGE_INVALID_OWNER_ID = "Invalid owner ID.";
    public static final String MESSAGE_NO_CHANGES_MADE = "No changes made to the property.";

    private static final Logger logger = LogsCenter.getLogger(EditPropertyCommand.class);

    private final EditPropertyDescriptor editPropertyDescriptor;

    /**
     * @param index of the property in the filtered property list to edit
     * @param editPropertyDescriptor details to edit the property with
     */
    public EditPropertyCommand(Index index, EditPropertyDescriptor editPropertyDescriptor) {
        super(index, editPropertyDescriptor);
        this.editPropertyDescriptor = new EditPropertyDescriptor(editPropertyDescriptor);
    }

    /**
     * Adds a command word and its associated prefixes to the command word map.
     */
    public static void addCommandWord() {
        Prefix[] prefixes = {
            PREFIX_PROPERTY_NAME,
            PREFIX_OWNER,
            PREFIX_ADDRESS,
            PREFIX_PRICE,
            PREFIX_SIZE,
            PREFIX_DESCRIPTION
        };
        initialiseCommandWord(COMMAND_WORD, prefixes);
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        List<Property> lastShownList = model.getFilteredPropertyList();
        List<Deal> lastShownDealList = model.getFilteredDealList();
        List<Event> lastShownEventList = model.getFilteredEventList();
        Optional<PropertyName> optionalPropertyName = editPropertyDescriptor.getPropertyName();

        if (index.getZeroBased() >= lastShownList.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_PROPERTY_DISPLAYED_INDEX);
        }

        Property propertyToEdit = lastShownList.get(index.getZeroBased());
        Property editedProperty = createEditedProperty(propertyToEdit, editPropertyDescriptor, model);

        if (propertyToEdit.equals(editedProperty)) {
            logger.info("Property is the same as before, no changes made.");
            throw new CommandException(MESSAGE_NO_CHANGES_MADE);
        }

        if (!propertyToEdit.isSameProperty(editedProperty) && model.hasProperty(editedProperty)) {
            throw new CommandException(MESSAGE_DUPLICATE_PROPERTY);
        }

        model.setProperty(propertyToEdit, editedProperty);
        model.updateFilteredPropertyList(PREDICATE_SHOW_ALL_PROPERTIES);

        if (optionalPropertyName.isPresent()) {
            PropertyName oldPropertyName = propertyToEdit.getFullName();

            updatePropertyNameInDeals(oldPropertyName, index, lastShownDealList, model);
            updatePropertyNameInEvents(oldPropertyName, index, lastShownEventList, model);
        }

        return new CommandResult(String.format(MESSAGE_EDIT_PROPERTY_SUCCESS, Messages.formatProperty(editedProperty)));
    }

    private void updatePropertyNameInEvents(PropertyName oldPropertyName, Index index,
            List<Event> eventList, Model model) {
        for (int eventPosition = 0; eventPosition < eventList.size(); eventPosition++) {
            Event event = eventList.get(eventPosition);
            EditEventDescriptor descriptor = new EditEventDescriptor();
            if (oldPropertyName.equals(event.getPropertyName())) {
                descriptor.setPropertyId(index);
            }
            if (descriptor.isAnyFieldEdited()) {
                try {
                    new EditEventCommand(Index.fromZeroBased(eventPosition), descriptor).execute(model);
                } catch (CommandException e) {
                    logger.info("An error occurred while executing command to update property");
                }
            }
        }
    }

    private void updatePropertyNameInDeals(PropertyName oldPropertyName, Index index,
            List<Deal> dealList, Model model) {
        for (int dealPosition = 0; dealPosition < dealList.size(); dealPosition++) {
            Deal deal = dealList.get(dealPosition);
            UpdateDealDescriptor descriptor = new UpdateDealDescriptor();
            if (oldPropertyName.equals(deal.getPropertyName())) {
                descriptor.setPropertyId(index);
            }
            if (descriptor.isAnyFieldEdited()) {
                try {
                    new UpdateDealCommand(Index.fromZeroBased(dealPosition), descriptor).execute(model);
                } catch (CommandException e) {
                    logger.info("An error occurred while executing command to update property");
                }
            }
        }
    }

    /**
     * Creates and returns a {@code Property} with the details of {@code propertyToEdit}
     * edited with {@code editPropertyDescriptor}.
     */
    private static Property createEditedProperty(Property propertyToEdit,
                                                 EditPropertyDescriptor editPropertyDescriptor, Model model)
                                                 throws CommandException {
        assert propertyToEdit != null;

        ClientName updatedClientName;
        List<Client> clientList = model.getFilteredClientList();

        PropertyName updatedPropertyName = editPropertyDescriptor.getPropertyName()
                .orElse(propertyToEdit.getFullName());
        Address updatedAddress = editPropertyDescriptor.getAddress().orElse(propertyToEdit.getAddress());
        Price updatedPrice = editPropertyDescriptor.getPrice().orElse(propertyToEdit.getPrice());
        Optional<Size> updatedSize = editPropertyDescriptor.getSize().orElse(propertyToEdit.getSize());
        Optional<Description> updatedDescription = editPropertyDescriptor.getDescription()
                .orElse(propertyToEdit.getDescription());

        Optional<Index> optionalClientId = editPropertyDescriptor.getOwner();
        if (optionalClientId.isPresent()) {
            Index clientId = optionalClientId.get();
            int clientIdZeroBased = clientId.getZeroBased();
            if (clientIdZeroBased >= clientList.size()) {
                throw new CommandException(MESSAGE_INVALID_OWNER_ID);
            }
            Client client = clientList.get(clientIdZeroBased);
            updatedClientName = client.getFullName();
        } else {
            updatedClientName = propertyToEdit.getOwner();
        }

        return new Property(updatedPropertyName, updatedAddress, updatedPrice, updatedSize, updatedDescription,
                updatedClientName);
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof EditPropertyCommand otherEditCommand)) {
            return false;
        }

        return index.equals(otherEditCommand.index)
                && editPropertyDescriptor.equals(otherEditCommand.editPropertyDescriptor);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("index", index)
                .add("editPropertyDescriptor", editPropertyDescriptor)
                .toString();
    }

    /**
     * Stores the details to edit the property with. Each non-empty field value will replace the
     * corresponding field value of the property.
     */
    public static class EditPropertyDescriptor extends EditDescriptor<Property> {
        private PropertyName propertyName;
        private Address address;
        private Price price;
        private Optional<Size> size;
        private Optional<Description> description;
        private Index ownerId;

        public EditPropertyDescriptor() {}

        /**
         * Copy constructor.
         *
         */
        public EditPropertyDescriptor(EditPropertyDescriptor toCopy) {
            setPropertyName(toCopy.propertyName);
            setAddress(toCopy.address);
            setPrice(toCopy.price);
            setSize(toCopy.size);
            setDescription(toCopy.description);
            setOwner(toCopy.ownerId);
        }

        /**
         * Returns true if at least one field is edited.
         */
        @Override
        public boolean isAnyFieldEdited() {
            return CollectionUtil.isAnyNonNull(propertyName, address, price, size, description, ownerId);
        }

        public void setPropertyName(PropertyName propertyName) {
            this.propertyName = propertyName;
        }

        public Optional<PropertyName> getPropertyName() {
            return Optional.ofNullable(propertyName);
        }

        public void setAddress(Address address) {
            this.address = address;
        }

        public Optional<Address> getAddress() {
            return Optional.ofNullable(address);
        }

        public void setPrice(Price price) {
            this.price = price;
        }

        public Optional<Price> getPrice() {
            return Optional.ofNullable(price);
        }

        public void setSize(Optional<Size> size) {
            this.size = size;
        }

        public Optional<Optional<Size>> getSize() {
            return Optional.ofNullable(size);
        }

        public void setDescription(Optional<Description> description) {
            this.description = description;
        }

        public Optional<Optional<Description>> getDescription() {
            return Optional.ofNullable(description);
        }

        public void setOwner(Index ownerId) {
            this.ownerId = ownerId;
        }

        public Optional<Index> getOwner() {
            return Optional.ofNullable(ownerId);
        }

        @Override
        public boolean equals(Object other) {
            if (other == this) {
                return true;
            }

            // instanceof handles nulls
            if (!(other instanceof EditPropertyDescriptor otherEditPropertyDescriptor)) {
                return false;
            }

            return Objects.equals(propertyName, otherEditPropertyDescriptor.propertyName)
                    && Objects.equals(address, otherEditPropertyDescriptor.address)
                    && Objects.equals(price, otherEditPropertyDescriptor.price)
                    && Objects.equals(size, otherEditPropertyDescriptor.size)
                    && Objects.equals(description, otherEditPropertyDescriptor.description)
                    && Objects.equals(ownerId, otherEditPropertyDescriptor.ownerId);
        }

        @Override
        public String toString() {
            return new ToStringBuilder(this)
                    .add("propertyName", propertyName)
                    .add("address", address)
                    .add("price", price)
                    .add("size", size)
                    .add("description", description)
                    .add("ownerId", ownerId)
                    .toString();
        }
    }
}
