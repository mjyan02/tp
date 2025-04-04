package seedu.address.ui.cards;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import seedu.address.model.property.Property;
import seedu.address.ui.UiPart;

/**
 * A UI component that displays information of a {@code Property}.
 */
public class PropertyCard extends UiPart<Region> {

    private static final String FXML = "PropertyListCard.fxml";

    /**
     * Note: Certain keywords such as "location" and "resources" are reserved keywords in JavaFX.
     * As a consequence, UI elements' variable names cannot be set to such keywords
     * or an exception will be thrown by JavaFX during runtime.
     *
     * @see <a href="https://github.com/se-edu/addressbook-level4/issues/336">The issue on AddressBook level 4</a>
     */

    public final Property property;

    @FXML
    private HBox cardPane;
    @FXML
    private Label name;
    @FXML
    private Label id;
    @FXML
    private Label address;
    @FXML
    private Label price;
    @FXML
    private Label size;
    @FXML
    private Label description;
    @FXML
    private FlowPane owner;

    /**
     * Creates a {@code PropertyCode} with the given {@code Property} and index to display.
     */
    public PropertyCard(Property property, int displayedIndex) {
        super(FXML);
        assert property != null : "Property should not be null";
        assert displayedIndex > 0 : "Index should be greater than 0";
        this.property = property;
        id.setText(displayedIndex + ". ");
        name.setText(property.getFullName().fullName);
        address.setText(property.getAddress().value);
        price.setText(String.format("Price: $%sk", property.getPrice().value.toString()));
        String sizeValue = property.getSize().map(s -> s.value).orElse("N/A");
        size.setText(sizeValue.equals("-") ? "Size: -" : String.format("Size: %s square feet", sizeValue));
        description.setText("Desc: " + property.getDescription().map(d -> d.getDescription()).orElse("-"));
        owner.getChildren().add(new Label("Owner: " + property.getOwner().fullName));
    }
}
