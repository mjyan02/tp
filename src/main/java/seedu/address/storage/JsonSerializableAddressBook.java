package seedu.address.storage;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;

import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.model.AddressBook;
import seedu.address.model.ReadOnlyAddressBook;
import seedu.address.model.client.Client;
import seedu.address.model.deal.Deal;
import seedu.address.model.event.Event;
import seedu.address.model.property.Property;

/**
 * An Immutable AddressBook that is serializable to JSON format.
 */
@JsonRootName(value = "addressbook")
class JsonSerializableAddressBook {

    public static final String MESSAGE_DUPLICATE_CLIENT = "Client list contains duplicate client(s).";
    public static final String MESSAGE_DUPLICATE_PROPERTY = "Property list contains duplicate properties.";
    public static final String MESSAGE_DUPLICATE_DEAL = "Deal list contains duplicate deal(s).";
    public static final String MESSAGE_DUPLICATE_EVENT = "Event list contains duplicate event(s).";

    private final List<JsonAdaptedClient> clients = new ArrayList<>();
    private final List<JsonAdaptedProperty> properties = new ArrayList<>();
    private final List<JsonAdaptedDeal> deals = new ArrayList<>();
    private final List<JsonAdaptedEvent> events = new ArrayList<>();

    /**
     * Constructs a {@code JsonSerializableAddressBook} with the given clients, properties, deals and events.
     */
    @JsonCreator
    public JsonSerializableAddressBook(@JsonProperty("clients") List<JsonAdaptedClient> clients,
                                       @JsonProperty("properties") List<JsonAdaptedProperty> properties,
                                       @JsonProperty("deals") List<JsonAdaptedDeal> deals,
                                       @JsonProperty("events") List<JsonAdaptedEvent> events) {
        this.clients.addAll(clients);
        this.properties.addAll(properties);
        if (deals != null) {
            this.deals.addAll(deals);
        }
        if (events != null) {
            this.events.addAll(events);
        }
    }

    /**
     * Converts a given {@code ReadOnlyAddressBook} into this class for Jackson use.
     *
     * @param source future changes to this will not affect the created {@code JsonSerializableAddressBook}.
     */
    public JsonSerializableAddressBook(ReadOnlyAddressBook source) {
        clients.addAll(source.getClientList().stream().map(JsonAdaptedClient::new).toList());
        properties.addAll(source.getPropertyList().stream().map(JsonAdaptedProperty::new).toList());
        deals.addAll(source.getDealList().stream().map(JsonAdaptedDeal::new).toList());
        events.addAll(source.getEventList().stream().map(JsonAdaptedEvent::new).toList());
    }

    /**
     * Converts this address book into the model's {@code AddressBook} object.
     *
     * @throws IllegalValueException if there were any data constraints violated.
     */
    public AddressBook toModelType() throws IllegalValueException {
        AddressBook addressBook = new AddressBook();

        for (JsonAdaptedClient jsonAdaptedClient : clients) {
            Client client = jsonAdaptedClient.toModelType();
            if (addressBook.hasClient(client)) {
                throw new IllegalValueException(MESSAGE_DUPLICATE_CLIENT);
            }
            addressBook.addClient(client);
        }

        for (JsonAdaptedProperty jsonAdaptedProperty : properties) {
            Property property = jsonAdaptedProperty.toModelType();
            if (addressBook.hasProperty(property)) {
                throw new IllegalValueException(MESSAGE_DUPLICATE_PROPERTY);
            }
            addressBook.addProperty(property);
        }

        for (JsonAdaptedDeal jsonAdaptedDeal : deals) {
            Deal deal = jsonAdaptedDeal.toModelType();
            if (addressBook.hasDeal(deal)) {
                throw new IllegalValueException(MESSAGE_DUPLICATE_DEAL);
            }
            addressBook.addDeal(deal);
        }

        for (JsonAdaptedEvent jsonAdaptedEvent : events) {
            Event event = jsonAdaptedEvent.toModelType();
            if (addressBook.hasEvent(event)) {
                throw new IllegalValueException(MESSAGE_DUPLICATE_EVENT);
            }
            addressBook.addEvent(event);
        }

        return addressBook;
    }
}
