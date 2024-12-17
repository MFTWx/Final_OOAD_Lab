package view.seller;

import java.util.ArrayList;
import java.util.Optional;

import controller.ItemController;
import controller.TransactionController;
import controller.UserController;
import controller.WishlistController;
import javafx.application.Application;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextInputDialog;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import model.Item;
import model.Transaction;
import model.User;
import view.auth.LoginView;

public class SellerHomeView extends Application{
	UserController controller = UserController.getInstance();
	ItemController itemController = ItemController.getInstance();
	TransactionController controllerT = TransactionController.getInstance();
	WishlistController controllerW = WishlistController.getInstance();
	
	String currentUserId = controller.getCurrentUser().getUser_id();
	
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

        TableColumn<Item, String> wishlistColumn = new TableColumn<>("Buyer");
        wishlistColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getItem_wishlist()));

        TableColumn<Item, String> offerStatusColumn = new TableColumn<>("Offer Status");
        offerStatusColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getItem_offer_status()));
        
        TableColumn<Item, Void> offerColumn = new TableColumn<>("Accept");
        offerColumn.setCellFactory(col -> new TableCell<>() {
            private final Button acc = new Button("Accept");

            {
                acc.setStyle("-fx-background-color: #2196F3; -fx-text-fill: black; -fx-padding: 5px; -fx-pref-width: 80px;");
                acc.setOnAction(event -> {
                	Item item = getTableView().getItems().get(getIndex());
                    Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION);
                    confirmationAlert.setTitle("Confirm Offer");
                    confirmationAlert.setHeaderText("Offer Confirmation");
                    confirmationAlert.setContentText("Are you sure you want to accept this offer: " + item.getItem_offer_status() + "?");
                    
                    confirmationAlert.showAndWait().ifPresent(response -> {
                        if (response == ButtonType.OK) {
                        	Item selectedItem = getTableView().getItems().get(getIndex());
                        	User user = new User();
                        	user.setUser_id(selectedItem.getItem_wishlist());
                        	
                            boolean validated = controllerT.getInstance().PurchaseItems(user, item);
                            if(validated) {
                            	ArrayList<Transaction> controlledItem = controllerT.getInstance().ViewHistory(user.getUser_id());
                            	for(Transaction items : controlledItem) {
                            		if(items.getItem_id().equals(item.getItem_id())) {
                            			ItemController.getInstance().PurchasedItem(item.getItem_id(), items.getTransaction_id());
                            			ItemController.getInstance().AcceptOffer(item.getItem_id(), selectedItem.getItem_offer_status());
                            			controllerW.getInstance().RemoveWishlist(item.getItem_id());
                            		}
                            	}
                            	System.out.println("Item sold");
                            	refreshTable();
                            } else {
                            	System.out.println("Offer failed");
                            }
                            refreshTable();
                        } else {
                            System.out.println("Offer canceled");
                        }
                    });
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);

                if (!empty) {
                    Item currentItem = getTableView().getItems().get(getIndex());

                    if (currentItem != null && currentItem.getItem_offer_status() != null) {
                        String offerStatus = currentItem.getItem_offer_status();
                        if (!offerStatus.contains("id_") && !offerStatus.isEmpty()) {
                            setGraphic(acc);
                        } else {
                            setGraphic(null);
                        }
                    } else {
                        setGraphic(null);
                    }
                    setAlignment(Pos.CENTER); 
                } else {
                    setGraphic(null);
                }
            }
        });

        TableColumn<Item, Void> declineColumn = new TableColumn<>("Decline");
        declineColumn.setCellFactory(col -> new TableCell<>() {
            private final Button dec = new Button("Decline");
            {
                dec.setStyle("-fx-background-color: #FF0000; -fx-text-fill: black; -fx-padding: 5px; -fx-pref-width: 80px;");
                dec.setOnAction(event -> {
                	Item item = getTableView().getItems().get(getIndex());
                	
                	TextInputDialog inputDialog = new TextInputDialog();
                    inputDialog.setTitle("Decline Offer");
                    inputDialog.setHeaderText("Decline an offer for: " + item.getItem_name());
                    inputDialog.setContentText("Enter reason:");

                    Optional<String> result = inputDialog.showAndWait();
                	
                    result.ifPresent(reason -> {
                    	if (reason.isEmpty()) {
                            Alert errorAlert = new Alert(Alert.AlertType.ERROR);
                            errorAlert.setTitle("Invalid Input");
                            errorAlert.setHeaderText("Empty reason");
                            errorAlert.setContentText("Reason cannot be empty.");
                            errorAlert.showAndWait();
                    	} else {
                    		boolean validated = ItemController.getInstance().declineOffer(item.getItem_id());
                            if(validated) {
                            	System.out.println("Item declined");
                            	refreshTable();
                            } else {
                            	System.out.println("Decline failed");
                            }
                            refreshTable(); 
                    	}
                    });
                });
            }
            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);

                if (!empty) {
                    Item currentItem = getTableView().getItems().get(getIndex());

                    if (currentItem != null && currentItem.getItem_offer_status() != null) {
                        String offerStatus = currentItem.getItem_offer_status();
                        if (!offerStatus.contains("id_") && !offerStatus.isEmpty()) {
                            setGraphic(dec);
                        } else {
                            setGraphic(null);
                        }
                    } else {
                        setGraphic(null);
                    }
                    setAlignment(Pos.CENTER); 
                } else {
                    setGraphic(null);
                }
            }
        });
        
        itemTable.getColumns().addAll(
                idColumn, userIdColumn, nameColumn, sizeColumn, priceColumn, 
                categoryColumn, statusColumn, wishlistColumn, offerStatusColumn,
                offerColumn, declineColumn
           );

        itemTable.setPrefHeight(400);

        vbMain = new VBox(10, homeTitle, logoutLink, hbButtons, itemTable);
        vbMain.setStyle("-fx-padding: 20px; -fx-alignment: center;");

        bp = new BorderPane(vbMain);
        sc = new Scene(bp, 1000, 600);
    }
    
    private void setAction() {
    	 
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
    
    public void refreshTable(){
    	ArrayList<Item> items = itemController.getSellerItem(currentUserId);
        ObservableList<Item> observableItems = FXCollections.observableArrayList(items);
        itemTable.setItems(observableItems);
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
