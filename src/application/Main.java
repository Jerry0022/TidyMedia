package application;

import javafx.application.Application;
import javafx.beans.binding.Bindings;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import application.gui.InputView;
import application.gui.ShowView;
import application.gui.TitleView;
import application.logic.ContentManager;
import de.mixedfx.config.SettingsManager;
import de.mixedfx.superpane.SuperPane;

public class Main extends Application
{
	private static Main	INSTANCE;

	public static void openDynamic(final Node dynamic)
	{
		Main.INSTANCE.superPane.openDynamic(dynamic);
	}

	private SuperPane	superPane;

	@Override
	public void start(final Stage primaryStage)
	{
		Main.INSTANCE = this;
		try
		{
			SettingsManager.init();

			final BorderPane root = new BorderPane();
			root.setLeft(new InputView());
			root.setTop(new TitleView());
			root.setCenter(new ShowView());
			root.setMinSize(600, 400);

			this.superPane = new SuperPane(root);
			final Scene scene = new Scene(this.superPane, 600, 400);
			scene.getStylesheets().add(this.getClass().getResource("application.css").toExternalForm());

			primaryStage.setScene(scene);
			primaryStage.minHeightProperty().bind(Bindings.max(0, primaryStage.heightProperty().subtract(scene.heightProperty()).add(root.minHeightProperty())));
			primaryStage.minWidthProperty().bind(Bindings.max(0, primaryStage.widthProperty().subtract(scene.widthProperty()).add(root.minWidthProperty())));
			primaryStage.show();

			final Button startReadButton = new Button("Start reading from " + ContentManager.getInstance().unsortedPath);
			startReadButton.setOnAction(event ->
			{
				if (ContentManager.getInstance().validateConfig())
				{
					ContentManager.getInstance().nextFile(); // Read very first file.
					Main.this.superPane.closeDialogue(startReadButton);
				}
			});
			Main.openDynamic(startReadButton);
		}
		catch (final Exception e)
		{
			e.printStackTrace();
		}
	}

	public static void main(final String[] args)
	{
		Application.launch(args);
	}
}
