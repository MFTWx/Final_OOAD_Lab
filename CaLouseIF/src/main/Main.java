package main;

import javafx.application.Application;
import javafx.stage.Stage;
import view.admin.AdminHomePage;
import view.admin.DeclineItemView;
import view.admin.RequestedItemView;
import view.auth.RegisterView;

public class Main extends Application {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		launch(args);

	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		// TODO Auto-generated method stub
		new RegisterView().start(primaryStage);
	}

}