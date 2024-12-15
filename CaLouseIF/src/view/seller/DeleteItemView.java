package view.seller;

import java.util.ArrayList;

import controller.ItemController;
import controller.UserController;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import model.Item;
import model.User;

public class DeleteItemView extends Application {

    private ItemController itemController = ItemController.getInstance();
    private UserController userController = UserController.getInstance();
    private ComboBox<Item> itemDropdown;
    
    private Button deleteButton;
    private Button backButton;
    private User currentUser;
    private Label deleteItemLabel;
    private HBox hbox;
    private Label deleteWarning;

    public DeleteItemView() {
    }

    private void initialize() {
    	hbox = new HBox();
    	hbox.setSpacing(10); 
    	deleteItemLabel = new Label("Delete Item");
    	deleteWarning = new Label("You can only delete items with status of Accepted!");
    	
    	deleteItemLabel.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");
        itemDropdown = new ComboBox<>();
        deleteButton = new Button("Delete Item");
        backButton = new Button("Back");
        deleteButton.setStyle("-fx-background-color: #FF0000; -fx-text-fill: white; -fx-font-size: 14px; -fx-padding: 10px 20px; -fx-border-radius: 5px; -fx-background-radius: 5px;");
        backButton.setStyle("-fx-background-color: #000000; -fx-text-fill: white; -fx-font-size: 14px; -fx-padding: 10px 20px; -fx-border-radius: 5px; -fx-background-radius: 5px;");
        currentUser = userController.getCurrentUser();

        ArrayList<Item> sellerItems = itemController.getApprovedItem(currentUser.getUser_id());
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
    }

    private void layouting(Stage primaryStage) {
        
        hbox.getChildren().addAll(deleteButton, backButton);
        VBox layout = new VBox(10, deleteItemLabel, deleteWarning, itemDropdown, hbox);
        layout.setPadding(new Insets(10));

        // Scene setup
        Scene scene = new Scene(layout, 400, 350);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Delete Item");
    }

    private void setAction(Stage primaryStage) {
        deleteButton.setOnAction(event -> {
            Item selectedItem = itemDropdown.getValue();

            if (selectedItem == null) {
                showAlert(Alert.AlertType.ERROR, "No Item Selected", "Please select an item to delete.");
                return;
            }

           
            boolean deleteItem = itemController.deleteItem(selectedItem.getItem_id());
            if (deleteItem) {
                showAlert(Alert.AlertType.INFORMATION, "Delete Successful", "Item updated successfully.");
                
            } else {
                showAlert(Alert.AlertType.ERROR, "Delete Failed", "Failed to delete the item. Please try again.");
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
