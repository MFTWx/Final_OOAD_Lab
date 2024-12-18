package view.admin;

import java.util.ArrayList;

import controller.ItemController;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import model.Item;

public class RequestedItemView extends Application{
	// panggil controllernya - item
	ItemController controller = ItemController.getInstance();
	
	Scene sc;
    BorderPane bpMain;

    TableView<Item> itemTable;
    
    ArrayList<Item> itemList;
    ObservableList<Item> itemListShow;
    
    public RequestedItemView() {
    	// buat show item
    	itemList = new ArrayList<Item>();
    	itemList = controller.getInstance().ViewRequestedItem(); // ngambil item status pending
    	itemListShow = FXCollections.observableArrayList(itemList); // ubah ke observable
    }
    
    
    TableColumn<Item, String> idColumn;
    TableColumn<Item, String> nameColumn;
    TableColumn<Item, String> sizeColumn;
    TableColumn<Item, String> priceColumn;
    TableColumn<Item, String> categoryColumn;
    TableColumn<Item, String> statusColumn;
    
    Button acceptButton, declineButton, backButton;
    
    private void initialize() {
        bpMain = new BorderPane();

        itemTable = new TableView<>();

        idColumn = new TableColumn<>("Item ID");
        nameColumn = new TableColumn<>("Item Name");
        sizeColumn = new TableColumn<>("Item Size");
        priceColumn = new TableColumn<>("Item Price");
        categoryColumn = new TableColumn<>("Item Category");
        statusColumn = new TableColumn<>("Item Status");

        acceptButton = new Button("Accept");
        declineButton = new Button("Decline");
        backButton = new Button("Back to HomePage");
    }

    private void layouting() {
    	idColumn.setCellValueFactory(new PropertyValueFactory<>("Item_id"));
    	nameColumn.setCellValueFactory(new PropertyValueFactory<>("Item_name"));
    	sizeColumn.setCellValueFactory(new PropertyValueFactory<>("Item_size"));
    	priceColumn.setCellValueFactory(new PropertyValueFactory<>("Item_price"));
    	categoryColumn.setCellValueFactory(new PropertyValueFactory<>("Item_category"));
    	statusColumn.setCellValueFactory(new PropertyValueFactory<>("Item_status"));

        itemTable.getColumns().addAll(idColumn, nameColumn, sizeColumn, priceColumn, categoryColumn, statusColumn);
        itemTable.setItems(itemListShow);
        itemTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        HBox buttonBox = new HBox(10, acceptButton, declineButton);
        buttonBox.setAlignment(Pos.CENTER);
        buttonBox.setPadding(new Insets(10));

        VBox fullButtonBox = new VBox(10, buttonBox, backButton);
        fullButtonBox.setAlignment(Pos.CENTER);
        fullButtonBox.setPadding(new Insets(10));

        bpMain.setCenter(itemTable);
        bpMain.setBottom(fullButtonBox);

        sc = new Scene(bpMain, 800, 500);
    }
    
    // fungsi refresh table
    private void refreshTable() {
        itemList = controller.getInstance().ViewRequestedItem();
        
        itemListShow.setAll(itemList);
        
        itemTable.setItems(itemListShow);
    }
    
    private void setAction() {
    	// acc button
        acceptButton.setOnMouseClicked(event -> {
            Item selectedItem = itemTable.getSelectionModel().getSelectedItem();
            // selected
            if (selectedItem != null) {
                controller.getInstance().ApproveItem(selectedItem.getItem_id());              
                
                refreshTable();
            // ga ada yg di select
            } else {
                showAlert("No Item Selected", "Please select an item to accept.");
            }
        });
        
        //decline button
        declineButton.setOnMouseClicked(event -> {
            Item selectedItem = itemTable.getSelectionModel().getSelectedItem();
            if (selectedItem != null) {   	
            	System.out.println("Redirecting to Decline Item Form...");
                try {
                	// mindahin data item
                    DeclineItemView decView = new DeclineItemView(selectedItem.getItem_id(), 
                    		selectedItem.getItem_name(), 
                    		selectedItem.getItem_size(), 
                    		selectedItem.getItem_price(), 
                    		selectedItem.getItem_category());

                    Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
                    decView.start(stage);
                } catch (Exception e) {
                    e.printStackTrace();
                }           
            } else {
                showAlert("No Item Selected", "Please select an item to decline.");
            }
        });
        // back button
        backButton.setOnMouseClicked(event -> {
        	System.out.println("Redirecting to Admin HomePage...");
            
            try {
                AdminHomePage adminHome = new AdminHomePage();

                Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
                adminHome.start(stage);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
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
        primaryStage.setTitle("Admin Item Management (Requested)");
        primaryStage.show();
		
	}

}
