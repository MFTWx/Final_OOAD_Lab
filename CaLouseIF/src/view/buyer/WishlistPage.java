package view.buyer;

import java.util.ArrayList;

import controller.ItemController;
import controller.WishlistController;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import model.Item;
import model.User;

public class WishlistPage extends Application{
	
	private User user;
	
	public WishlistPage(User user) {
		super();
		this.user = user;
	}

	Scene sc;
    BorderPane bpMain;

    TableView<Item> wishlistTable;
    
    ItemController controller = ItemController.getInstance();
    WishlistController controllerW = WishlistController.getInstance();

    TableColumn<Item, String> removeColumn;
    TableColumn<Item, String> nameColumn;
    TableColumn<Item, String> sizeColumn;
    TableColumn<Item, String> priceColumn;
    TableColumn<Item, String> categoryColumn;
    TableColumn<Item, String> dateColumn;

    Button backButton;
	
    private void initialize() {
        bpMain = new BorderPane();

        wishlistTable = new TableView<>();
        
        nameColumn = new TableColumn<>("Item Name");
        sizeColumn = new TableColumn<>("Item Size");
        priceColumn = new TableColumn<>("Item Price");
        categoryColumn = new TableColumn<>("Item Category");
        dateColumn = new TableColumn<>("Date Purchased");

        backButton = new Button("Back to HomePage");
    }

    private void layouting() {
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("Item_name"));
        sizeColumn.setCellValueFactory(new PropertyValueFactory<>("Item_size"));
        priceColumn.setCellValueFactory(new PropertyValueFactory<>("Item_price"));
        categoryColumn.setCellValueFactory(new PropertyValueFactory<>("Item_category"));
        
        TableColumn<Item, Void> removeColumn = new TableColumn<>();
        removeColumn.setCellFactory(col -> new TableCell<>() {
            private final Button removeButton = new Button("Remove");

            {
                removeButton.setStyle("-fx-background-color: #FFC107; -fx-text-fill: black; -fx-padding: 5px; -fx-pref-width: 150px;");
                removeButton.setOnAction(event -> {
                    Item item = getTableView().getItems().get(getIndex());
                    
                    boolean validated = controllerW.getInstance().RemoveWishlistByUser(item.getItem_id(), user.getUser_id());
                    
                    if(validated) {
                    	System.out.println("Item deleted from wishlist");
                    } else {
                    	System.out.println("Failed to delete item from wishlist");
                    }
                    refreshTable();
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(removeButton);
                    setAlignment(Pos.CENTER);
                }
            }
        });

        wishlistTable.getColumns().addAll(nameColumn, sizeColumn, priceColumn, categoryColumn, removeColumn);
        wishlistTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        HBox buttonBox = new HBox(10, backButton);
        buttonBox.setAlignment(Pos.CENTER);
        buttonBox.setPadding(new Insets(10));

        bpMain.setCenter(wishlistTable);
        bpMain.setBottom(buttonBox);
        
        ArrayList<Item> items = controller.getInstance().getWishlistItem(user.getUser_id());
        ObservableList<Item> observableItems = FXCollections.observableArrayList(items);
        wishlistTable.setItems(observableItems);

        sc = new Scene(bpMain, 800, 500);
    }
    
    private void setAction() {

        backButton.setOnMouseClicked(event -> {
            System.out.println("Redirecting to Buyer HomePage...");
            try {
                BuyerHomePage buyerHomePage = new BuyerHomePage();

                Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
                buyerHomePage.start(stage);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
    
    private void refreshTable() {
        ArrayList<Item> items = controller.getInstance().getWishlistItem(user.getUser_id());
        ObservableList<Item> observableItems = FXCollections.observableArrayList(items);
        wishlistTable.setItems(observableItems);
    }
    
    public Scene startScene() {
        initialize();
        layouting();
        setAction();
        return sc;
    }

	@Override
	public void start(Stage primaryStage) throws Exception {
		// TODO Auto-generated method stub
		initialize();
        layouting();
        setAction();

        primaryStage.setScene(sc);
        primaryStage.setTitle("Wishlist Page");
        primaryStage.show();
	}

}
