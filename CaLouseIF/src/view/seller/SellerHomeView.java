package view.seller;

import java.util.ArrayList;

import controller.ItemController;
import controller.UserController;
import javafx.application.Application;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import model.Item;
import view.auth.LoginView;

public class SellerHomeView extends Application{
	UserController controller = UserController.getInstance();
	ItemController itemController = ItemController.getInstance();
	
	 Scene sc;
	 BorderPane bp;
	 VBox vbMain;
	 HBox hbButtons;
	 Label homeTitle;
	 Button uploadProductBtn;
	 Button editItemBtn;
	 Button deleteItemBtn;
	 TableView<Item> itemTable;
	 Hyperlink logoutLink;
    
    private void initialize() {
    	
    	homeTitle = new Label("Seller Home");
        homeTitle.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");
        uploadProductBtn = new Button("Upload Item");
        editItemBtn = new Button("Edit Item");
        deleteItemBtn = new Button("Delete Item");
        uploadProductBtn.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-font-size: 14px; -fx-padding: 10px 20px; -fx-border-radius: 5px; -fx-background-radius: 5px;");
        editItemBtn.setStyle("-fx-background-color: #2196F3; -fx-text-fill: white; -fx-font-size: 14px; -fx-padding: 10px 20px; -fx-border-radius: 5px; -fx-background-radius: 5px;");
        deleteItemBtn.setStyle("-fx-background-color: #FF0000; -fx-text-fill: white; -fx-font-size: 14px; -fx-padding: 10px 20px; -fx-border-radius: 5px; -fx-background-radius: 5px;");
        itemTable = new TableView<>();
        logoutLink = new Hyperlink("Done with your work? Click here to log out.");
        logoutLink.setStyle("-fx-text-fill: #007BFF; -fx-font-size: 12px;");
    }
    
    private void layouting() {
    	hbButtons = new HBox(10, uploadProductBtn, editItemBtn, deleteItemBtn);
        hbButtons.setStyle("-fx-alignment: center; -fx-padding: 10px;");
        
        TableColumn<Item, String> idColumn = new TableColumn<>("Item ID");
        idColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getItem_id()));

        TableColumn<Item, String> userIdColumn = new TableColumn<>("User ID");
        userIdColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getUser_id()));

        TableColumn<Item, String> nameColumn = new TableColumn<>("Name");
        nameColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getItem_name()));

        TableColumn<Item, String> sizeColumn = new TableColumn<>("Size");
        sizeColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getItem_size()));

        TableColumn<Item, String> priceColumn = new TableColumn<>("Price");
        priceColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getItem_price()));

        TableColumn<Item, String> categoryColumn = new TableColumn<>("Category");
        categoryColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getItem_category()));

        TableColumn<Item, String> statusColumn = new TableColumn<>("Status");
        statusColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getItem_status()));

        TableColumn<Item, String> wishlistColumn = new TableColumn<>("Wishlist");
        wishlistColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getItem_wishlist()));

        TableColumn<Item, String> offerStatusColumn = new TableColumn<>("Offer Status");
        offerStatusColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getItem_offer_status()));
        
        itemTable.getColumns().addAll(
                idColumn, userIdColumn, nameColumn, sizeColumn, priceColumn, 
                categoryColumn, statusColumn, wishlistColumn, offerStatusColumn
           );

        itemTable.setPrefHeight(400);

        vbMain = new VBox(10, homeTitle, logoutLink, hbButtons, itemTable);
        vbMain.setStyle("-fx-padding: 20px; -fx-alignment: center;");

        bp = new BorderPane(vbMain);
        sc = new Scene(bp, 800, 600);
    }
    
    private void setAction() {
    	 String currentUserId = controller.getCurrentUser().getUser_id();
         ArrayList<Item> items = itemController.getSellerItem(currentUserId);
         ObservableList<Item> observableItems = FXCollections.observableArrayList(items);
         itemTable.setItems(observableItems);

         uploadProductBtn.setOnAction(e -> {
             UploadItemView uploadItemPage = new UploadItemView();
             Stage stage = (Stage) ((Button) e.getSource()).getScene().getWindow();
             
             try {
				uploadItemPage.start(stage);
			 } catch (Exception exc) {
					// TODO Auto-generated catch block
				exc.printStackTrace();
			 }
         });

         editItemBtn.setOnAction(e -> {
             EditItemView editItemPage = new EditItemView();
             Stage stage = (Stage) ((Button) e.getSource()).getScene().getWindow();
             
             try {
				editItemPage.start(stage);
			 } catch (Exception exc) {
					// TODO Auto-generated catch block
				exc.printStackTrace();
			 }
         });
         
         deleteItemBtn.setOnAction(e -> {
             DeleteItemView deleteItemPage = new DeleteItemView();
             Stage stage = (Stage) ((Button) e.getSource()).getScene().getWindow();
             
             try {
            	 deleteItemPage.start(stage);
			 } catch (Exception exc) {
					// TODO Auto-generated catch block
				exc.printStackTrace();
			 }
         });
         logoutLink.setOnMouseClicked(event -> {
             System.out.println("Redirecting to Register Form...");
             try {
                 LoginView loginView = new LoginView();

                 Stage stage = (Stage) ((Hyperlink) event.getSource()).getScene().getWindow();
                 stage.setScene(loginView.startScene());
             } catch (Exception e) {
                 e.printStackTrace();
             }
         });
    }
    
    
    
	public SellerHomeView() {
		
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		// TODO Auto-generated method stub
		initialize();
        layouting();
        setAction();

        primaryStage.setScene(sc);
        primaryStage.setTitle("Seller Home Page");
        primaryStage.show();
	}
}
