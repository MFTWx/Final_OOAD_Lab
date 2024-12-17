package view.seller;

import controller.ItemController;
import controller.UserController;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import model.Item;
import model.User;

import java.util.ArrayList;

public class EditItemView extends Application {

    private ItemController itemController = ItemController.getInstance();
    private UserController userController = UserController.getInstance();
    private ComboBox<Item> itemDropdown;
    private TextField nameField;
    private TextField categoryField;
    private TextField sizeField;
    private TextField priceField;
    private Button updateButton;
    private Button backButton;
    private User currentUser;
    private Label editItemLabel;
    private HBox hbox;

    public EditItemView() {
    }

    private void initialize() {
    	hbox = new HBox();
    	hbox.setSpacing(10); 
    	editItemLabel = new Label("Edit Item");
    	editItemLabel.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");
        itemDropdown = new ComboBox<>();
        nameField = new TextField();
        categoryField = new TextField();
        sizeField = new TextField();
        priceField = new TextField();
        updateButton = new Button("Update Item");
        backButton = new Button("Back");
        updateButton.setStyle("-fx-background-color: #2196F3; -fx-text-fill: white; -fx-font-size: 14px; -fx-padding: 10px 20px; -fx-border-radius: 5px; -fx-background-radius: 5px;");
        backButton.setStyle("-fx-background-color: #000000; -fx-text-fill: white; -fx-font-size: 14px; -fx-padding: 10px 20px; -fx-border-radius: 5px; -fx-background-radius: 5px;");
        currentUser = userController.getCurrentUser();

        ArrayList<Item> sellerItems = itemController.getSellerItemUpdate(currentUser.getUser_id());
        itemDropdown.getItems().addAll(sellerItems);
        itemDropdown.setPromptText("Select an Item");

        itemDropdown.setCellFactory(comboBox -> new ListCell<>() {
            @Override
            protected void updateItem(Item item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? null : item.getItem_name());
            }
        });

        itemDropdown.setConverter(new StringConverter<>() {
            @Override
            public String toString(Item item) {
                return item == null ? null : item.getItem_name();
            }

            @Override
            public Item fromString(String string) {
                return null;
            }
        });

        itemDropdown.setOnAction(event -> {
            Item selectedItem = itemDropdown.getValue();
            if (selectedItem != null) {
                nameField.setText(selectedItem.getItem_name());
                categoryField.setText(selectedItem.getItem_category());
                sizeField.setText(selectedItem.getItem_size());
                priceField.setText(selectedItem.getItem_price());
            }
        });
    }

    private void layouting(Stage primaryStage) {
        GridPane form = new GridPane();
        form.setPadding(new Insets(10));
        form.setHgap(10);
        form.setVgap(10);

        form.add(new Label("Select Item:"), 0, 0);
        form.add(itemDropdown, 1, 0);

        form.add(new Label("Item Name:"), 0, 1);
        form.add(nameField, 1, 1);

        form.add(new Label("Item Category:"), 0, 2);
        form.add(categoryField, 1, 2);

        form.add(new Label("Item Size:"), 0, 3);
        form.add(sizeField, 1, 3);

        form.add(new Label("Item Price:"), 0, 4);
        form.add(priceField, 1, 4);
        
        hbox.getChildren().addAll(updateButton, backButton);
        VBox layout = new VBox(10, editItemLabel, form, hbox);
        layout.setPadding(new Insets(10));

        // Scene setup
        Scene scene = new Scene(layout, 400, 350);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Edit Item");
    }

    private void setAction(Stage primaryStage) {
        updateButton.setOnAction(event -> {
            Item selectedItem = itemDropdown.getValue();

            if (selectedItem == null) {
                showAlert(Alert.AlertType.ERROR, "No Item Selected", "Please select an item to edit.");
                return;
            }

            String newName = nameField.getText();
            String newCategory = categoryField.getText();
            String newSize = sizeField.getText();
            String newPrice = priceField.getText();
            boolean updateItem = itemController.updateItem(selectedItem.getItem_id(), newName, newCategory, newSize, newPrice);
            if (updateItem) {
                showAlert(Alert.AlertType.INFORMATION, "Update Successful", "Item updated successfully.");
                
            } else {
                showAlert(Alert.AlertType.ERROR, "Update Failed", "Failed to update the item. Please try again.");
            }
            
            SellerHomeView sellerPage = new SellerHomeView();
			Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
			
			 try {
				sellerPage.start(stage);
			 } catch (Exception exc) {
				exc.printStackTrace();
			 }
        });
        
        backButton.setOnAction(event -> {
        	SellerHomeView sellerPage = new SellerHomeView();
			Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
			
			 try {
				sellerPage.start(stage);
			 } catch (Exception exc) {
				exc.printStackTrace();
			 }
        });
    }

    private void showAlert(Alert.AlertType type, String title, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        initialize();
        layouting(primaryStage);
        setAction(primaryStage);
        primaryStage.show();
    }
}
