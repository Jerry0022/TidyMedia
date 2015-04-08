package application.gui;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import application.logic.ContentManager;

public class InputView extends VBox
{
	public InputView()
	{
		TextField locationTextField = new TextField();
		Button continueButton = new Button("Speichern und fortfahren");
		continueButton.setOnAction(new EventHandler<ActionEvent>()
		{
			@Override
			public void handle(ActionEvent event)
			{
				// TODO new Task to do the following / while that block GUI
				// TODO Rename file
				// TODO Copy file by TeraCopy over command line with /RenameAll
				// TODO After copying finished request next file
				ContentManager.getInstance().nextFile();
			}
		});

		this.getChildren().add(new Text("Ort"));
		this.getChildren().add(locationTextField);
		this.getChildren().add(continueButton);
	}
}
