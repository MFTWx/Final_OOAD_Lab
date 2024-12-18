package view.auth;

import controller.UserController;
import javafx.application.Application;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class RegisterView extends Application {
	
	// manggil controllernya
	UserController controller = UserController.getInstance();
	
	Scene sc;
    BorderPane bp;
    GridPane gpForm;
    VBox vbMain;
    HBox hbButtons;

    Label titleLabel, usernameLabel, passwordLabel, phoneLabel, addressLabel, roleLabel, colonLabel1, colonLabel2, colonLabel3, colonLabel4, colonLabel5;
    TextField usernameField, phoneField, addressField;
    PasswordField passwordField;
    RadioButton sellerButton, buyerButton;
    ToggleGroup roleGroup;

    Button registerButton;
    Hyperlink loginLink;

    private void initialize() {
        bp = new BorderPane();
        sc = new Scene(bp, 500, 450);

        gpForm = new GridPane();
        vbMain = new VBox(20);
        hbButtons = new HBox();

        titleLabel = new Label("Registration Form");
        titleLabel.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");

        usernameLabel = new Label("Username");
        passwordLabel = new Label("Password");
        phoneLabel = new Label("Phone Number");
        addressLabel = new Label("Address");
        roleLabel = new Label("Roles");

        colonLabel1 = new Label(":");
        colonLabel2 = new Label(":");
        colonLabel3 = new Label(":");
        colonLabel4 = new Label(":");
        colonLabel5 = new Label(":");

        usernameField = new TextField();
        passwordField = new PasswordField();
        phoneField = new TextField();
        addressField = new TextField();

        roleGroup = new ToggleGroup();
        sellerButton = new RadioButton("Seller");
        sellerButton.setToggleGroup(roleGroup);
        buyerButton = new RadioButton("Buyer");
        buyerButton.setToggleGroup(roleGroup);

        registerButton = new Button("Submit");
        registerButton.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-padding: 8 20;");

        loginLink = new Hyperlink("Already have an account? Click here to log in.");
        loginLink.setStyle("-fx-text-fill: #007BFF; -fx-font-size: 12px;");
        
    }

    private void layouting() {

        gpForm.setHgap(10);
        gpForm.setVgap(15);
        gpForm.setPadding(new Insets(20));
        gpForm.setAlignment(Pos.CENTER);

        gpForm.add(usernameLabel, 0, 0);
        gpForm.add(colonLabel1, 1, 0);
        gpForm.add(usernameField, 2, 0);

        gpForm.add(passwordLabel, 0, 1);
        gpForm.add(colonLabel2, 1, 1);
        gpForm.add(passwordField, 2, 1);

        gpForm.add(phoneLabel, 0, 2);
        gpForm.add(colonLabel3, 1, 2);
        gpForm.add(phoneField, 2, 2);

        gpForm.add(addressLabel, 0, 3);
        gpForm.add(colonLabel4, 1, 3);
        gpForm.add(addressField, 2, 3);

        gpForm.add(roleLabel, 0, 4);
        gpForm.add(colonLabel5, 1, 4);

        HBox roleButtons = new HBox(10, sellerButton, buyerButton);
        sellerButton.setSelected(true);
        roleButtons.setAlignment(Pos.CENTER_LEFT);

        gpForm.add(roleButtons, 2, 4);

        hbButtons.getChildren().add(registerButton);
        hbButtons.setAlignment(Pos.CENTER);
        hbButtons.setPadding(new Insets(10));

        vbMain.getChildren().addAll(titleLabel, gpForm, hbButtons, loginLink);
        vbMain.setAlignment(Pos.CENTER);
        vbMain.setPadding(new Insets(30));

        bp.setCenter(vbMain);
    }
    
    private void setAction() {
    	// onlick register button
        registerButton.setOnMouseClicked(new EventHandler<Event>() {
        	public void handle(Event event) {
        		// ambil user input, inisialisasi data
                String username = usernameField.getText().trim();
                String password = passwordField.getText();
                String phone = phoneField.getText().trim();
                String address = addressField.getText().trim();
                RadioButton selectedRole = (RadioButton) roleGroup.getSelectedToggle();
                
                // panggil function controller buat validation + register
                boolean validation = controller.getInstance().Register(username, password, phone, address, selectedRole.getText());
                
                // failed validation
                if(!validation) {
                	Alert alert = new Alert(AlertType.ERROR);
                    alert.setTitle("Error");
                    alert.setHeaderText(null);
                    alert.setContentText("Invalid Input Credentials");
                    alert.showAndWait();
                    
                // register success
                }else {
                	System.out.println("Redirecting to Login Form...");
                    try {
                        LoginView loginView = new LoginView();

                        Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
                        loginView.start(stage);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
		});
        
        //onclick login button
        loginLink.setOnMouseClicked(event -> {
        	System.out.println("Redirecting to Login Form...");
        	try {
                LoginView loginView = new LoginView();

                Stage stage = (Stage) ((Hyperlink) event.getSource()).getScene().getWindow();
                stage.setScene(loginView.startScene());
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
		primaryStage.setTitle("Register Form");
		primaryStage.show();
	}

}
