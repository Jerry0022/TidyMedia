package application;

import java.io.File;

import javafx.application.Application;
import javafx.beans.binding.Bindings;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import application.gui.InputView;
import application.gui.ShowView;
import application.gui.TitleView;
import application.logic.ContentManager;
import de.mixedfx.config.ConfigHandler;
import de.mixedfx.config.SettingsManager;
import de.mixedfx.file.DataHandler;
import de.mixedfx.file.FileObject;
import de.mixedfx.gui.panes.SuperPane;

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
			SettingsManager.fileName = "Allgemein";

			final BorderPane root = new BorderPane();
			root.setId("background");
			root.setMinSize(600, 400);

			this.superPane = new SuperPane(root);
			final Scene scene = new Scene(this.superPane, 600, 400);
			scene.getStylesheets().add(this.getClass().getResource("application.css").toExternalForm());
			// Set FullScreen on button F12
			scene.setOnKeyPressed(key ->
			{
				if (key.getCode() == KeyCode.F12)
					primaryStage.setFullScreen(true);
			});

			primaryStage.setScene(scene);
			primaryStage.minHeightProperty().bind(Bindings.max(0, primaryStage.heightProperty().subtract(scene.heightProperty()).add(root.minHeightProperty())));
			primaryStage.minWidthProperty().bind(Bindings.max(0, primaryStage.widthProperty().subtract(scene.widthProperty()).add(root.minWidthProperty())));
			primaryStage.show();

			final HBox userSelection = new HBox();
			userSelection.setAlignment(Pos.CENTER);
			userSelection.setSpacing(20);

			// Add general button
			final Button generalButton = new Button(SettingsManager.fileName);
			generalButton.setOnAction(event ->
			{
				SettingsManager.init();
				root.setLeft(new InputView());
				root.setTop(new TitleView());
				final ShowView showView = new ShowView();
				root.setPadding(new Insets(10, 10, 10, 10));
				root.setCenter(showView);
				if (ContentManager.getInstance().validateConfig())
					ContentManager.getInstance().nextFile(); // Read very first file.
				Main.this.superPane.closeDialogue(userSelection);
			});
			generalButton.setPrefWidth(130);
			userSelection.getChildren().add(generalButton);

			// Add other user
			for (final File f : DataHandler.listFiles(FileObject.create().setPath(System.getProperty("user.dir"))))
			{
				final FileObject fo = FileObject.create(f);
				if (fo.getPrefix().equals(ConfigHandler.prefix) && !fo.getName().equals(SettingsManager.fileName))
				{
					final Button button = new Button(fo.getName());
					button.setPrefWidth(130);
					button.setOnAction(event ->
					{
						SettingsManager.fileName = fo.getName();
						SettingsManager.init();
						root.setLeft(new InputView());
						root.setTop(new TitleView());
						final ShowView showView = new ShowView();
						root.setPadding(new Insets(10, 10, 10, 10));
						root.setCenter(showView);
						if (ContentManager.getInstance().validateConfig())
							ContentManager.getInstance().nextFile(); // Read very first file.
						Main.this.superPane.closeDialogue(userSelection);
					});
					userSelection.getChildren().add(button);
				}
			}

			Main.openDynamic(userSelection);

			if (userSelection.getChildren().size() == 1)
				generalButton.fire();
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
