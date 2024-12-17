package view.buyer;

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
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
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
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import model.Item;
import model.Transaction;
import model.User;
import view.auth.LoginView;

public class BuyerHomePage extends Application {
	
	UserController userController = UserController.getInstance();
	User user = UserController.getInstance().getCurrentUser();

	private Scene sc;
    private BorderPane bp;
    private VBox vbMain;
    private TableView<Item> itemTable;
    private HBox hbButtons;
    private Button viewWishlistBtn, purchaseHistoryBtn, searchButton, refreshButton;
    Hyperlink logoutLink;
    private TextField searchField;
    private Label titleLabel;

    private ItemController controller = ItemController.getInstance();
    private TransactionController controllerT = TransactionController.getInstance();
    private WishlistController controllerW = WishlistController.getInstance();

    private void initialize() {
        viewWishlistBtn = new Button("View Wishlist");
        purchaseHistoryBtn = new Button("Purchase History");

        itemTable = new TableView<>();
        
        titleLabel = new Label("Welcome to CaLouseIf Home Page");
        titleLabel.setStyle("-fx-font-size: 24px; -fx-font-weight: bold; -fx-text-fill: #333333;");
        
        searchField = new TextField();
        searchField.setPromptText("Search for items...");
        searchField.setStyle("-fx-padding: 5px; -fx-pref-width: 300px;");
        
        searchButton = new Button("Search");
        refreshButton = new Button("Refresh Table");
  
        logoutLink = new Hyperlink("Done with your work? Click here to log out.");
        logoutLink.setStyle("-fx-text-fill: #007BFF; -fx-font-size: 12px;");

    }

    private void layouting() {
    	
    	HBox searchLayout = new HBox(10, searchField, searchButton, refreshButton);
        searchLayout.setAlignment(Pos.CENTER);
        searchLayout.setPadding(new Insets(10));
    	
        TableColumn<Item, String> nameColumn = new TableColumn<>("Name");
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("Item_name"));

        TableColumn<Item, String> sizeColumn = new TableColumn<>("Size");
        sizeColumn.setCellValueFactory(new PropertyValueFactory<>("Item_size"));

        TableColumn<Item, String> priceColumn = new TableColumn<>("Price");
        priceColumn.setCellValueFactory(new PropertyValueFactory<>("Item_price"));

        TableColumn<Item, String> categoryColumn = new TableColumn<>("Category");
        categoryColumn.setCellValueFactory(new PropertyValueFactory<>("Item_category"));
        
        TableColumn<Item, String> offerColumn = new TableColumn<>("Offer Price");
        offerColumn.setCellValueFactory(cellData -> {
            Item item = cellData.getValue();
            String offerStatus = item.getItem_offer_status();
            String price = item.getItem_price();
            
            if (offerStatus != null && !offerStatus.isEmpty()) {
                return new SimpleStringProperty(offerStatus);
            } else {
                return new SimpleStringProperty("N/A");
            }
        });

        TableColumn<Item, Void> addToWishlistColumn = new TableColumn<>();
        addToWishlistColumn.setCellFactory(col -> new TableCell<>() {
            private final Button addButton = new Button("Add to Wishlist");

            {
            	addButton.setStyle("-fx-background-color: #FFC107; -fx-text-fill: black; -fx-padding: 5px; -fx-pref-width: 150px;");
                addButton.setOnAction(event -> {
                    Item item = getTableView().getItems().get(getIndex());
                    
                    boolean validated = controllerW.getInstance().CheckWishlistByUser(user.getUser_id(), item.getItem_id());
                    
                    if(validated) {
                    	boolean addedToWishlist = controllerW.getInstance().AddWishlist(user, item);
                        
                        if (addedToWishlist) {
                            Alert infoAlert = new Alert(Alert.AlertType.INFORMATION);
                            infoAlert.setTitle("Item Added");
                            infoAlert.setHeaderText("Wishlist Updated");
                            infoAlert.setContentText("The item \"" + item.getItem_name() + "\" has been added to your wishlist.");
                            infoAlert.showAndWait();
                            
                            System.out.println("Item added to wishlist");
                        } else {
                            System.out.println("Failed to add item to wishlist");
                        }
                    } else {
                    	Alert infoAlert = new Alert(Alert.AlertType.ERROR);
                        infoAlert.setTitle("Wishlist Failed");
                        infoAlert.setHeaderText("Item Already In Wishlist");
                        infoAlert.setContentText("The item \"" + item.getItem_name() + "\" has already been added to your wishlist.");
                        infoAlert.showAndWait();
                        System.out.println("Failed to add item to wishlist");
                    }
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(addButton);
                    setAlignment(Pos.CENTER);
                }
            }
        });

        TableColumn<Item, Void> createOfferColumn = new TableColumn<>();
        createOfferColumn.setCellFactory(col -> new TableCell<>() {
            private final Button offerButton = new Button("Create Offer");

            {
                offerButton.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: black; -fx-padding: 5px; -fx-pref-width: 150px;");
                offerButton.setOnAction(event -> {
                    Item item = getTableView().getItems().get(getIndex());

                    TextInputDialog inputDialog = new TextInputDialog();
                    inputDialog.setTitle("Create Offer");
                    inputDialog.setHeaderText("Create an offer for: " + item.getItem_name());
                    inputDialog.setContentText("Enter Offer Price:");

                    Optional<String> result = inputDialog.showAndWait();

                    result.ifPresent(offerPrice -> {
                    	if (offerPrice.isEmpty()) {
                            Alert errorAlert = new Alert(Alert.AlertType.ERROR);
                            errorAlert.setTitle("Invalid Input");
                            errorAlert.setHeaderText("Empty Offer Price");
                            errorAlert.setContentText("Offer price cannot be empty.");
                            errorAlert.showAndWait();
                    	}
                    	else if(isInteger(offerPrice)) {
                    		int price = Integer.parseInt(offerPrice);
                    		
                                if (item.getItem_offer_status().isEmpty()) {
                                    boolean updated = controller.getInstance().OfferPrice(item.getItem_id(), price, user.getUser_id());

                                    if (updated) {
                                        Alert confirmationAlert = new Alert(Alert.AlertType.INFORMATION);
                                        confirmationAlert.setTitle("Offer Created");
                                        confirmationAlert.setHeaderText("Offer Created Successfully");
                                        confirmationAlert.setContentText("Offer price of " + price + " has been set for " + item.getItem_name() + ".");
                                        confirmationAlert.showAndWait();
                                        refreshTable();
                                    } else {
                                        Alert errorAlert = new Alert(Alert.AlertType.ERROR);
                                        errorAlert.setTitle("Update Failed");
                                        errorAlert.setHeaderText("Failed to Update Offer");
                                        errorAlert.setContentText("There was an issue updating the offer price.");
                                        errorAlert.showAndWait();
                                    }
                                } else {
                            		int currentOffer = Integer.parseInt(item.getItem_offer_status());
                                	if(price > currentOffer && price != 0) {
                                		boolean updated = controller.getInstance().OfferPrice(item.getItem_id(), price, user.getUser_id());

                                        if (updated) {
                                            Alert confirmationAlert = new Alert(Alert.AlertType.INFORMATION);
                                            confirmationAlert.setTitle("Offer Created");
                                            confirmationAlert.setHeaderText("Offer Created Successfully");
                                            confirmationAlert.setContentText("Offer price of " + price + " has been set for " + item.getItem_name() + ".");
                                            confirmationAlert.showAndWait();
                                            refreshTable();
                                        } else {
                                            Alert errorAlert = new Alert(Alert.AlertType.ERROR);
                                            errorAlert.setTitle("Update Failed");
                                            errorAlert.setHeaderText("Failed to Update Offer");
                                            errorAlert.setContentText("There was an issue updating the offer price.");
                                            errorAlert.showAndWait();
                                        }
                                	} else {
                                		Alert errorAlert = new Alert(Alert.AlertType.ERROR);
                                        errorAlert.setTitle("Invalid Input");
                                        errorAlert.setHeaderText("Invalid Offer Price");
                                        errorAlert.setContentText("Offer price must be greater than the current offer: " + currentOffer);
                                        errorAlert.showAndWait();
                                	}                                   
                                }
                    		} else {
                    			Alert errorAlert = new Alert(Alert.AlertType.ERROR);
                                errorAlert.setTitle("Invalid Input");
                                errorAlert.setHeaderText("Invalid Offer Price");
                                errorAlert.setContentText("Please enter a valid integer value for the offer price.");
                                errorAlert.showAndWait();
                    		}
                    });
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(offerButton);
                    setAlignment(Pos.CENTER);
                }
            }
        });


        TableColumn<Item, Void> purchaseColumn = new TableColumn<>();
        purchaseColumn.setCellFactory(col -> new TableCell<>() {
            private final Button purchaseButton = new Button("Purchase");

            {
                purchaseButton.setStyle("-fx-background-color: #2196F3; -fx-text-fill: black; -fx-padding: 5px; -fx-pref-width: 150px;");
                purchaseButton.setOnAction(event -> {
                    Item item = getTableView().getItems().get(getIndex());
                    Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION);
                    confirmationAlert.setTitle("Confirm Purchase");
                    confirmationAlert.setHeaderText("Purchase Confirmation");
                    confirmationAlert.setContentText("Are you sure you want to purchase: " + item.getItem_name() + "?");
                    
                    confirmationAlert.showAndWait().ifPresent(response -> {
                        if (response == ButtonType.OK) {
                            boolean validated = controllerT.getInstance().PurchaseItems(user, item);
                            if(validated) {
                            	ArrayList<Transaction> controlledItem = controllerT.getInstance().ViewHistory(user.getUser_id());
                            	for(Transaction items : controlledItem) {
                            		if(items.getItem_id().equals(item.getItem_id())) {
                            			controller.getInstance().PurchasedItem(item.getItem_id(), items.getTransaction_id());
                            			controllerW.getInstance().RemoveWishlist(item.getItem_id());
                            		}
                            	}
                            	System.out.println("Item purchased");
                            } else {
                            	System.out.println("Purchase failed");
                            }
                            refreshTable();
                        } else {
                            System.out.println("Purchase canceled");
                        }
                    });
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(purchaseButton);
                    setAlignment(Pos.CENTER);
                }
            }
        });

        itemTable.getColumns().addAll(nameColumn, sizeColumn, priceColumn, categoryColumn, offerColumn, addToWishlistColumn, createOfferColumn, purchaseColumn);

        itemTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        itemTable.setPrefHeight(400);
        
        ArrayList<Item> items = controller.getInstance().ViewItem();
        ObservableList<Item> observableItems = FXCollections.observableArrayList(items);
        itemTable.setItems(observableItems);
        
        viewWishlistBtn.setStyle("-fx-background-color: #FFC107; -fx-text-fill: black; -fx-padding: 10px 20px;");
        purchaseHistoryBtn.setStyle("-fx-background-color: #2196F3; -fx-text-fill: black; -fx-padding: 10px 20px;");

        hbButtons = new HBox(10, viewWishlistBtn, purchaseHistoryBtn);
        hbButtons.setAlignment(Pos.CENTER);
        hbButtons.setPadding(new Insets(10));

        vbMain = new VBox(20, titleLabel, searchLayout, itemTable, hbButtons, logoutLink);
        vbMain.setPadding(new Insets(20));
        vbMain.setAlignment(Pos.CENTER);

        bp = new BorderPane(vbMain);
        sc = new Scene(bp, 1000, 600);
    }

    private void setAction() {
    	
    	searchButton.setOnMouseClicked(new EventHandler<Event>() {
    		@Override
    		public void handle(Event event) {
				// TODO Auto-generated method stub
				searchItem(searchField.getText());
				System.out.println("Table changed");
			}
		});
    	
    	refreshButton.setOnMouseClicked(new EventHandler<Event>() {
    		@Override
    		public void handle(Event event) {
				// TODO Auto-generated method stub
				refreshTable();
				System.out.println("Table refreshed");
			}
		});

        viewWishlistBtn.setOnMouseClicked(new EventHandler<Event>() {	
			@Override
			public void handle(Event event) {
				// TODO Auto-generated method stub
				try {
	                WishlistPage wishlistPage = new WishlistPage(user);

	                Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
	                System.out.println("Redirecting to View Wishlist...");
	                wishlistPage.start(stage);
	            } catch (Exception e) {
	                e.printStackTrace();
	            }
				
			}
		});
        
        purchaseHistoryBtn.setOnMouseClicked(new EventHandler<Event>() {
			
			@Override
			public void handle(Event event) {
				// TODO Auto-generated method stub
				try {
	                PurchaseHistoryPage historyPage = new PurchaseHistoryPage(user);

	                Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
	                System.out.println("Redirecting to Purchase History...");
	                historyPage.start(stage);
	            } catch (Exception e) {
	                e.printStackTrace();
	            }
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
    
    private void searchItem(String search) {
        ArrayList<Item> updatedItems = controller.getInstance().ViewItem(); 
        ObservableList<Item> observableItems = FXCollections.observableArrayList(updatedItems); 
        SortedList<Item> sortedItems = new SortedList<>(controller.BrowseItem(observableItems, search));
        sortedItems.comparatorProperty().bind(itemTable.comparatorProperty());
        itemTable.setItems(sortedItems);
    }
    
    private void refreshTable() {
        ArrayList<Item> updatedItems = controller.getInstance().ViewItem();
        
        ObservableList<Item> observableItems = FXCollections.observableArrayList(updatedItems);
        
        itemTable.setItems(observableItems);
    }
    
    // sebenernya bisa langsung tpi udh tanggung
    // buat validasi integer atau bukan
    private boolean isInteger(String input) {
        try {
            Integer.parseInt(input);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
  
    public Scene startScene() {
        initialize();
        layouting();
        setAction();
        return sc;
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        initialize();
        layouting();
        setAction();

        primaryStage.setScene(sc);
        primaryStage.setTitle("Buyer Home Page");
        primaryStage.show();
    }

}
