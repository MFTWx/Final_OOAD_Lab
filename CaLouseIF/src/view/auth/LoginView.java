package view.auth;

import controller.UserController;
import javafx.application.Application;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import model.User;
import view.admin.AcceptedItemView;
import view.admin.AdminHomePage;
import view.buyer.BuyerHomePage;
import view.seller.SellerHomeView;

public class LoginView extends Application {
	
	// manggil controllernya
	UserController controller = UserController.getInstance();
	
	Scene sc;
    BorderPane bp;
    GridPane gpForm;
    VBox vbMain;
    HBox hbButtons;

    Label titleLabel, usernameLabel, passwordLabel, colonLabel1, colonLabel2;
    TextField usernameField;
    PasswordField passwordField;

    Button loginButton;
    Hyperlink registerLink;

    private void initialize() {
        bp = new BorderPane();
        sc = new Scene(bp, 500, 450);

        gpForm = new GridPane();
        vbMain = new VBox(20);
        hbButtons = new HBox();

        titleLabel = new Label("Login Form");
        titleLabel.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");

        usernameLabel = new Label("Username");
        passwordLabel = new Label("Password");

        colonLabel1 = new Label(":");
        colonLabel2 = new Label(":");

        usernameField = new TextField();
        passwordField = new PasswordField();

        loginButton = new Button("Submit");
        loginButton.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-padding: 8 20;");

        registerLink = new Hyperlink("Don't have an account? Click here to register.");
        registerLink.setStyle("-fx-text-fill: #007BFF; -fx-font-size: 12px;");
        
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

        hbButtons.getChildren().add(loginButton);
        hbButtons.setAlignment(Pos.CENTER);
        hbButtons.setPadding(new Insets(10));

        vbMain.getChildren().addAll(titleLabel, gpForm, hbButtons, registerLink);
        vbMain.setAlignment(Pos.CENTER);
        vbMain.setPadding(new Insets(30));

        bp.setCenter(vbMain);
    }
    
    private void setAction() {
    	// onclick login button
        loginButton.setOnMouseClicked(new EventHandler<Event>() {
        	public void handle(Event event) {
                String username = usernameField.getText().trim();
                String password = passwordField.getText();
                
                // panggil function controller buat validation + login
                Integer validation = controller.getInstance().Login(username, password);
                
                // failed validation
                if(validation == -1) {
                	Alert alert = new Alert(AlertType.ERROR);
                    alert.setTitle("Error");
                    alert.setHeaderText(null);
                    alert.setContentText("Invalid Input Credentials");
                    
                    alert.showAndWait();
                }else {
                	User user = new User();
                	user = controller.getInstance().getCurrentUser();
                	
                	switch (validation) {
                	//redirect buyer
					case 1: {
						System.out.println("Buyer Page");
						BuyerHomePage buyerPage = new BuyerHomePage();
						Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
						try {
							buyerPage.start(stage);
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						break;
					}
					//redirect seller
					case 2: {
						System.out.println("Seller Page");
						SellerHomeView sellerPage = new SellerHomeView();
						Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
						try {
							sellerPage.start(stage);
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						break;
					}
					//redirect admin
					default:
			                System.out.println("Redirecting to Admin Home Page...");
			                try {
			                    AdminHomePage adminPage = new AdminHomePage();

			                    Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
			                    adminPage.start(stage);
			                } catch (Exception e) {
			                    e.printStackTrace();
			                }
			                break;
			        }
                }
            }
		});
      //onclick register button
        registerLink.setOnMouseClicked(event -> {
            System.out.println("Redirecting to Register Form...");
            try {
                RegisterView registerView = new RegisterView();

                Stage stage = (Stage) ((Hyperlink) event.getSource()).getScene().getWindow();
                stage.setScene(registerView.startScene());
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
		primaryStage.setTitle("Login Form");
		primaryStage.show();
	}
	
}
