package view.admin;

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
import view.auth.LoginView;
import view.auth.RegisterView;

public class AdminHomePage extends Application{
	
	Scene sc;
    VBox vbMain;

    Label titleLabel;
    Button viewRequestedButton, viewAcceptedButton;
    
    Hyperlink logoutLink;
	
	private void initialize() {
		vbMain = new VBox(20);
        vbMain.setPadding(new Insets(30));

        titleLabel = new Label("Admin Home Page");
        titleLabel.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");

        viewRequestedButton = new Button("View Requested Item");
        viewRequestedButton.setStyle("-fx-background-color: #007BFF; -fx-text-fill: white; -fx-font-size: 14px; -fx-padding: 10 20;");
        viewRequestedButton.setPrefWidth(200);
        viewRequestedButton.setPrefHeight(40);

        viewAcceptedButton = new Button("View Accepted Item");
        viewAcceptedButton.setStyle("-fx-background-color: #28A745; -fx-text-fill: white; -fx-font-size: 14px; -fx-padding: 10 20;");
        viewAcceptedButton.setPrefWidth(200); 
        viewAcceptedButton.setPrefHeight(40);
        
        logoutLink = new Hyperlink("Done with your work? Click here to log out.");
        logoutLink.setStyle("-fx-text-fill: #007BFF; -fx-font-size: 12px;");
	}

    private void layouting() {
    	vbMain.getChildren().addAll(titleLabel, viewRequestedButton, viewAcceptedButton, logoutLink);
        vbMain.setAlignment(Pos.CENTER);

        sc = new Scene(vbMain, 400, 300);
    }
    
    private void setAction() {
    	viewRequestedButton.setOnMouseClicked(new EventHandler<Event>() {
            @Override
            public void handle(Event event) {
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

        viewAcceptedButton.setOnMouseClicked(new EventHandler<Event>() {
            @Override
            public void handle(Event event) { // redirect ke accepted item
                System.out.println("Redirecting to View Accepted Item...");
                try {
                    AcceptedItemView acceptedView = new AcceptedItemView();

                    Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
                    acceptedView.start(stage);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        
        logoutLink.setOnMouseClicked(event -> { // redirect ke login form
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
        primaryStage.setTitle("Admin Home Page");
        primaryStage.show();
		
	}

}
