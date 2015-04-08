package application.gui;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import org.apache.commons.io.FileUtils;

import application.basicfeatures.FileObject;
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
				FileObject file = ContentManager.getInstance().file.get();

				FileObject newFile = FileObject
						.create()
						.setPath(ContentManager.getInstance().sortedPath)
						.setFullName(
								file.getFullNameWithoutExtension() + "_111" + "."
										+ file.getExtension());

				try
				{
					FileUtils.moveFile(file.getFile(), newFile.getFile());
				}
				catch (IOException e)
				{
					// TODO Source or destination aren't valid or access rights
					// are restricted
					e.printStackTrace();
				}
				ContentManager.getInstance().nextFile();
			}
		});

		this.getChildren().add(new Text("Ort"));
		this.getChildren().add(locationTextField);
		this.getChildren().add(continueButton);
	}
}
