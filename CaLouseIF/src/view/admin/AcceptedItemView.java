package view.admin;

import java.util.ArrayList;

import controller.ItemController;
import javafx.application.Application;
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

public class AcceptedItemView extends Application {

	ItemController controller = ItemController.getInstance();
	
	Scene sc;
    BorderPane bpMain;
    TableView<Item> itemTable;
    ArrayList<Item> itemList;
    ObservableList<Item> itemListShow;
    
    public AcceptedItemView() {
    	itemList = new ArrayList<Item>();
    	// ngambil item status accepted
    	itemList = controller.getInstance().ViewAdminItem();
    	itemListShow = FXCollections.observableArrayList(itemList);
    }
    
    TableColumn<Item, String> idColumn;
    TableColumn<Item, String> nameColumn;
    TableColumn<Item, String> sizeColumn;
    TableColumn<Item, String> priceColumn;
    TableColumn<Item, String> categoryColumn;
    TableColumn<Item, String> statusColumn;
    
    Button backButton;
    
    private void initialize() {
        bpMain = new BorderPane();
        
        itemTable = new TableView<>();
        
        idColumn = new TableColumn<>("Item ID");
        nameColumn = new TableColumn<>("Item Name");
        sizeColumn = new TableColumn<>("Item Size");
        priceColumn = new TableColumn<>("Item Price");
        categoryColumn = new TableColumn<>("Item Category");
        statusColumn = new TableColumn<>("Item Status");

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

        HBox buttonBox = new HBox(10, backButton);
        buttonBox.setAlignment(Pos.CENTER);
        buttonBox.setPadding(new Insets(10));

        bpMain.setCenter(itemTable);
        bpMain.setBottom(buttonBox);

        sc = new Scene(bpMain, 800, 500);
    }
    
    private void setAction() {
    	itemTable.setItems(itemListShow);
	
        backButton.setOnMouseClicked(event -> { // redirect ke admin homepage
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
        primaryStage.setTitle("Admin Item Management (Accepted)");
        primaryStage.show();
	}
	
}
