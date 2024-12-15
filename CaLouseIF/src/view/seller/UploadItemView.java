package view.seller;

import controller.ItemController;
import controller.UserController;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import model.User;

public class UploadItemView extends Application {
    private ItemController itemController = ItemController.getInstance();
    private UserController userController = UserController.getInstance();
    private User currentUser;
    private Label titleLabel;
    private Label nameLabel;
    private Label categoryLabel;
    private Label sizeLabel;
    private Label priceLabel;

    private TextField nameField;
    private TextField categoryField;
    private TextField sizeField;
    private TextField priceField;

    private Button submitButton;
    private Button backButton;
    private HBox hbox;
    

    private void initialize() {
    	hbox = new HBox();
    	hbox.setSpacing(10);
    	currentUser = userController.getCurrentUser();
        titleLabel = new Label("Upload Item");
        titleLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

        nameLabel = new Label("Item Name:");
        categoryLabel = new Label("Item Category:");
        sizeLabel = new Label("Item Size:");
        priceLabel = new Label("Item Price:");

        nameField = new TextField();
        nameField.setPromptText("Enter item name");

        categoryField = new TextField();
        categoryField.setPromptText("Enter item category");

        sizeField = new TextField();
        sizeField.setPromptText("Enter item size");

        priceField = new TextField();
        priceField.setPromptText("Enter item price");

        submitButton = new Button("Submit");
        submitButton.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-font-size: 14px; -fx-padding: 10px 20px; -fx-border-radius: 5px; -fx-background-radius: 5px;");
        backButton = new Button("Back");
        backButton.setStyle("-fx-background-color: #000000; -fx-text-fill: white; -fx-font-size: 14px; -fx-padding: 10px 20px; -fx-border-radius: 5px; -fx-background-radius: 5px;");
    }

    private VBox layouting() {
        GridPane form = new GridPane();
        form.setHgap(10);
        form.setVgap(10);
        form.setPadding(new Insets(20));

        form.add(nameLabel, 0, 0);
        form.add(nameField, 1, 0);
        form.add(categoryLabel, 0, 1);
        form.add(categoryField, 1, 1);
        form.add(sizeLabel, 0, 2);
        form.add(sizeField, 1, 2);
        form.add(priceLabel, 0, 3);
        form.add(priceField, 1, 3);
        hbox.getChildren().addAll(submitButton, backButton);
        VBox layout = new VBox(20, titleLabel, form, hbox);
        layout.setPadding(new Insets(20));
        
        layout.setStyle("-fx-alignment: center;");
        return layout;
    }

    private void setActions(Stage primaryStage) {
        submitButton.setOnAction(event -> {
            String itemName = nameField.getText();
            String itemCategory = categoryField.getText();
            String itemSize = sizeField.getText();
            String itemPrice = priceField.getText();

            if (validateFields(itemName, itemCategory, itemSize, itemPrice)) {
        
                boolean createItem = itemController.UploadItem(
                		currentUser.getUser_id(),
                		itemName,
                		itemSize,
                		itemPrice,
                		itemCategory,
                		"Pending",
                		"","");
                
                if(createItem) {
                	nameField.clear();
                    categoryField.clear();
                    sizeField.clear();
                    priceField.clear();
                    
                    SellerHomeView sellerPage = new SellerHomeView();
					Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
					
					 try {
						sellerPage.start(stage);
					 } catch (Exception exc) {
						exc.printStackTrace();
					 }
                }else {
                	 Alert alert = new Alert(Alert.AlertType.ERROR);
                     alert.setTitle("Upload Failed");
                     alert.setHeaderText("Error");
                     alert.setContentText("Failed to upload the item. Please check the input and try again.");
                     alert.showAndWait();
                }
                

                System.out.println("Item uploaded successfully!");
            } else {
                System.out.println("Please fill out all fields correctly.");
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

    private boolean validateFields(String name, String category, String size, String price) {
        if (name.isEmpty() || category.isEmpty() || size.isEmpty()) {
            return false;
        }
        try {
            double parsedPrice = Double.parseDouble(price);
            if (parsedPrice <= 0) return false;
        } catch (NumberFormatException e) {
            return false;
        }
        return true;
    }
    
    @Override
    public void start(Stage primaryStage) {
        initialize();

        VBox layout = layouting();

        setActions(primaryStage);

        Scene scene = new Scene(layout, 400,350);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Upload Item");
        primaryStage.show();
    }
}
