package view.admin;

import controller.ItemController;
import javafx.application.Application;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class DeclineItemView extends Application {
	ItemController controller = ItemController.getInstance();

	private Scene sc;
	private BorderPane bp;
	private GridPane gpForm, gpItemInfo;
	private VBox vbMain;

	private Label titleLabel, reasonLabel;
	private TextField reasonField;

	private Button submitButton, backButton;

	private String Item_id;
	private String Item_name;
	private String Item_size;
	private String Item_price;
	private String Item_category;
	
	public DeclineItemView(String item_id, String item_name, String item_size, String item_price,
			String item_category) {
		super();
		Item_id = item_id;
		Item_name = item_name;
		Item_size = item_size;
		Item_price = item_price;
		Item_category = item_category;
	}

	private void initialize() {
	    bp = new BorderPane();
	    gpForm = new GridPane();
	    gpItemInfo = new GridPane();
	    vbMain = new VBox(20);

	    titleLabel = new Label("Decline Item Form");
	    titleLabel.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");

	    reasonLabel = new Label("Reason:");
	    reasonField = new TextField();
	    reasonField.setPromptText("Enter reason for declining");

	    submitButton = new Button("Submit");

	    backButton = new Button("Back to HomePage");
	}

	private void layouting() {
	    gpItemInfo.setHgap(10);
	    gpItemInfo.setVgap(5);
	    gpItemInfo.setPadding(new Insets(10));
	    gpItemInfo.setAlignment(Pos.CENTER);

	    addItemInfoRow(gpItemInfo, "Item ID", Item_id, 0);
	    addItemInfoRow(gpItemInfo, "Item Name", Item_name, 1);
	    addItemInfoRow(gpItemInfo, "Item Size", Item_size, 2);
	    addItemInfoRow(gpItemInfo, "Item Price", Item_price, 3);
	    addItemInfoRow(gpItemInfo, "Item Category", Item_category, 4);

	    gpForm.setHgap(10);
	    gpForm.setVgap(15);
	    gpForm.setPadding(new Insets(20));
	    gpForm.setAlignment(Pos.CENTER);

	    gpForm.add(reasonLabel, 0, 0);
	    gpForm.add(reasonField, 1, 0, 2, 1);

	    VBox vbButtons = new VBox(10);
	    submitButton.setPrefWidth(120);
	    submitButton.setPrefHeight(30);
	    backButton.setPrefWidth(120);
	    backButton.setPrefHeight(30); 

	    vbButtons.getChildren().addAll(submitButton, backButton);
	    vbButtons.setAlignment(Pos.CENTER);

	    vbMain.getChildren().clear();
	    vbMain.getChildren().addAll(titleLabel, gpItemInfo, gpForm, vbButtons);
	    vbMain.setAlignment(Pos.TOP_CENTER);
	    vbMain.setPadding(new Insets(30));

	    bp.setCenter(vbMain);

	    sc = new Scene(bp, 400, 400);
	}

	private void addItemInfoRow(GridPane gp, String label, String value, int rowIndex) {
	    Label keyLabel = new Label(label);
	    Label colonLabel = new Label(":");
	    Label valueLabel = new Label(value);

	    gp.add(keyLabel, 0, rowIndex);
	    gp.add(colonLabel, 1, rowIndex);
	    gp.add(valueLabel, 2, rowIndex);
	}

    
    private void setAction() {
    	submitButton.setOnMouseClicked(new EventHandler<Event>() {
			@Override
			public void handle(Event event) {
				// TODO Auto-generated method stub
				//function validasi + delete
				boolean validated = controller.getInstance().DeclineItem(Item_id, reasonField.getText().trim());
				// validasi berhasil + deleted
				if(validated) {
					//pindah view
					System.out.println("Redirecting to View Requested Item...");	           
	                try {
	                    RequestedItemView reqView = new RequestedItemView();
	                    Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
	                    reqView.start(stage);
	                } catch (Exception e) {
	                    e.printStackTrace();
	                }
				} else {
					showAlert("Failed to Decline", "Please make sure to input correct statement.");
				}
			}
		});
    	
    	backButton.setOnMouseClicked(new EventHandler<Event>(){
			@Override
			public void handle(Event event) {
				// TODO Auto-generated method stub
				System.out.println("Redirecting to View Requested Item...");                
                try {
                    RequestedItemView reqView = new RequestedItemView();

                    Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
                    reqView.start(stage);
                } catch (Exception e) {
                    e.printStackTrace();
                }
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
        initialize();
        layouting();
        setAction();

        primaryStage.setScene(sc);
        primaryStage.setTitle("Admin Item Management (Decline Form)");
        primaryStage.show();
    }

}
