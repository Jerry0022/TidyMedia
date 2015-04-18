package application;

import javafx.application.Application;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import application.basicfeatures.SuperPane;
import application.config.ConSettingsManager;
import application.gui.InputView;
import application.gui.ShowView;
import application.gui.TitleView;
import application.logic.ContentManager;

public class Main extends Application
{
	private static Main	INSTANCE;

	public static void openDynamic(Node dynamic)
	{
		INSTANCE.superPane.openDynamic(dynamic);
	}

	private SuperPane	superPane;

	@Override
	public void start(Stage primaryStage)
	{
		INSTANCE = this;
		try
		{
			ConSettingsManager.init();

			BorderPane root = new BorderPane();
			root.setLeft(new InputView());
			root.setTop(new TitleView());
			root.setCenter(new ShowView());

			this.superPane = new SuperPane(root);
			Scene scene = new Scene(superPane, 600, 400);
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());

			primaryStage.setScene(scene);
			primaryStage.setMinWidth(600);
			primaryStage.setMinHeight(400);
			primaryStage.show();

			ContentManager.getInstance().nextFile(); // Read very first file.
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	public static void main(String[] args)
	{
		launch(args);
	}
}
