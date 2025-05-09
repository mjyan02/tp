package seedu.address.model;

import java.nio.file.Path;
import java.util.function.Predicate;

import javafx.collections.ObservableList;
import seedu.address.commons.core.GuiSettings;
import seedu.address.commons.core.index.Index;
import seedu.address.model.client.Client;
import seedu.address.model.deal.Deal;
import seedu.address.model.event.Event;
import seedu.address.model.property.Property;

/**
 * The API of the Model component.
 */
public interface Model {
    /** {@code Predicate} that always evaluate to true */
    Predicate<Client> PREDICATE_SHOW_ALL_CLIENTS = unused -> true;
    Predicate<Event> PREDICATE_SHOW_ALL_EVENTS = unused -> true;

    /** {@code Predicate} that always evaluate to true */
    Predicate<Property> PREDICATE_SHOW_ALL_PROPERTIES = unused -> true;

    /** {@code Predicate} that always evaluate to true */
    Predicate<Deal> PREDICATE_SHOW_ALL_DEALS = unused -> true;

    /**
     * Replaces user prefs data with the data in {@code userPrefs}.
     */
    void setUserPrefs(ReadOnlyUserPrefs userPrefs);

    /**
     * Returns the user prefs.
     */
    ReadOnlyUserPrefs getUserPrefs();

    /**
     * Returns the user prefs' GUI settings.
     */
    GuiSettings getGuiSettings();

    /**
     * Sets the user prefs' GUI settings.
     */
    void setGuiSettings(GuiSettings guiSettings);

    /**
     * Returns the user prefs' address book file path.
     */
    Path getAddressBookFilePath();

    /**
     * Sets the user prefs' address book file path.
     */
    void setAddressBookFilePath(Path addressBookFilePath);

    /**
     * Replaces address book data with the data in {@code addressBook}.
     */
    void setAddressBook(ReadOnlyAddressBook addressBook);

    /** Returns the AddressBook */
    ReadOnlyAddressBook getAddressBook();

    /**
     * Returns true if a client with the same identity as {@code client} exists in REconnect.
     */
    boolean hasClient(Client client);

    /**
     * Returns true if a client with the same identity as {@code client} exists in REconnect,
     * excluding at that index.
     */
    boolean hasClient(Client client, Index index);

    /**
     * Deletes the given client.
     * The client must exist in REconnect.
     */
    void deleteClient(Client target);

    /**
     * Adds the given client.
     * {@code client} must not already exist in REconnect.
     */
    void addClient(Client client);

    /**
     * Replaces the given client {@code target} with {@code editedClient}.
     * {@code target} must exist in REconnect.
     * The client identity of {@code editedClient} must not be the same as another existing client in REconnect.
     */
    void setClient(Client target, Client editedClient);

    /** Returns an unmodifiable view of the filtered client list */
    ObservableList<Client> getFilteredClientList();

    /** Returns an unmodifiable view of the filtered list of deals */
    ObservableList<Deal> getFilteredDealList();

    /** Returns an unmodifiable view of the filtered list of properties */
    ObservableList<Property> getFilteredPropertyList();

    /** Returns an unmodifiable view of the filtered list of events */
    ObservableList<Event> getFilteredEventList();

    /**
     * Updates the filter of the filtered client list to filter by the given {@code predicate}.
     * @throws NullPointerException if {@code predicate} is null.
     */
    void updateFilteredClientList(Predicate<Client> predicate);

    boolean hasEvent(Event event);

    void addEvent(Event event);

    void deleteEvent(Event event);

    void setEvent(Event target, Event editedEvent);

    void updateFilteredEventList(Predicate<Event> predicate);

    /**
     * Returns true if a property with the same identity as {@code property} exists in REconnect.
     */
    boolean hasProperty(Property property);

    /**
     * Deletes the given property.
     * The property must exist in REconnect.
     */
    void deleteProperty(Property target);

    /**
     * Adds the given property.
     * {@code property} must not already exist in REconnect.
     */
    void addProperty(Property property);

    /**
     * Replaces the given property {@code target} with {@code editedProperty}.
     * {@code target} must exist in REconnect.
     * The property identity of {@code editedProperty} must not be the same as another existing property in the AB.
     */
    void setProperty(Property target, Property editedProperty);

    /**
     * Updates the filter of the filtered property list to filter by the given {@code predicate}.
     * @throws NullPointerException if {@code predicate} is null.
     */
    void updateFilteredPropertyList(Predicate<Property> predicate);

    /** Returns whether there is a deal */
    boolean hasDeal(Deal deal);

    /** Adds a deal */
    void addDeal(Deal deal);

    /**
     * Replaces the given deal {@code target} with {@code editedDeal}.
     * {@code target} must exist in REconnect.
     * The deal identity of {@code editedDeal} must not be the same as another existing deal in REconnect.
     */
    void setDeal(Deal target, Deal editedDeal);

    /**
     * Updates the filter of the filtered deal list to filter by the given {@code predicate}.
     * @throws NullPointerException if {@code predicate} is null.
     */
    void updateFilteredDealList(Predicate<Deal> predicate);
}
