package view.buyer;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import controller.ItemController;
import controller.TransactionController;
import javafx.application.Application;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import model.Item;
import model.User;

public class PurchaseHistoryPage extends Application {
	
	private User user;
	
	public PurchaseHistoryPage(User user) {
		super();
		this.user = user;
	}

	Scene sc;
    BorderPane bpMain;

    TableView<Item> purchaseHistoryTable;
    
    ItemController controller = ItemController.getInstance();
    TransactionController controllerT = TransactionController.getInstance();

    TableColumn<Item, String> nameColumn;
    TableColumn<Item, String> sizeColumn;
    TableColumn<Item, String> priceColumn;
    TableColumn<Item, String> categoryColumn;
    TableColumn<Item, String> dateColumn;

    Button backButton;
	
    private void initialize() {
        bpMain = new BorderPane();

        purchaseHistoryTable = new TableView<>();
        
        nameColumn = new TableColumn<>("Item Name");
        sizeColumn = new TableColumn<>("Item Size");
        priceColumn = new TableColumn<>("Item Price");
        categoryColumn = new TableColumn<>("Item Category");
        dateColumn = new TableColumn<>("Date Purchased");

        backButton = new Button("Back to HomePage");
    }

    private void layouting() {
    	
    	TableColumn<Item, String> IdColumn = new TableColumn<>("Transaction ID");
    	IdColumn.setCellValueFactory(data -> {
    	    Item item = data.getValue();
    	    String transactionId = controllerT.getInstance().getTransactionId(item.getItem_id());
    	    return new SimpleStringProperty(transactionId);
    	});
    	
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("Item_name"));
        sizeColumn.setCellValueFactory(new PropertyValueFactory<>("Item_size"));
        priceColumn.setCellValueFactory(new PropertyValueFactory<>("Item_price"));
        categoryColumn.setCellValueFactory(new PropertyValueFactory<>("Item_category"));
        
        dateColumn = new TableColumn<>("Purchase Date");
        dateColumn.setCellValueFactory(data -> {
            String id = data.getValue().getItem_offer_status();
            
            if (id == null || !id.contains("_")) {
                return new SimpleObjectProperty<>("Unknown Date");
            }

            try {
                String timestampString = id.split("_")[1];
                long timestampMillis = Long.parseLong(timestampString); 

                Instant instant = Instant.ofEpochMilli(timestampMillis);
                LocalDate date = instant.atZone(ZoneId.systemDefault()).toLocalDate();
                String formattedDate = date.format(DateTimeFormatter.ofPattern("MMMM dd, yyyy"));
                return new SimpleObjectProperty<>(formattedDate);
            } catch (NumberFormatException | ArrayIndexOutOfBoundsException e) {
                e.printStackTrace();
                return new SimpleObjectProperty<>("Invalid Date");
            }
        });

        purchaseHistoryTable.getColumns().addAll(IdColumn, nameColumn, sizeColumn, priceColumn, categoryColumn, dateColumn);
        purchaseHistoryTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        HBox buttonBox = new HBox(10, backButton);
        buttonBox.setAlignment(Pos.CENTER);
        buttonBox.setPadding(new Insets(10));

        bpMain.setCenter(purchaseHistoryTable);
        bpMain.setBottom(buttonBox);
        
        ArrayList<Item> items = controller.getInstance().getBuyerItem(user.getUser_id());
        ObservableList<Item> observableItems = FXCollections.observableArrayList(items);
        purchaseHistoryTable.setItems(observableItems);

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
        primaryStage.setTitle("Purchase History");
        primaryStage.show();
	}

}
