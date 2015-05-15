package application.gui;

import java.io.File;
import java.io.IOException;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import org.apache.commons.io.FileExistsException;
import org.apache.commons.io.FileUtils;

import application.Main;
import application.logic.ContentManager;
import de.mixedfx.file.FileObject;

public class InputView extends VBox implements ChangeListener<FileObject>
{
	private final Button	continueButton;
	private final Button	removalButton;

	public InputView()
	{
		ContentManager.getInstance().file.addListener(this);
		this.setSpacing(10);

		final VBox locationBox = new VBox();
		final Label locationText = new Label("Ort");
		locationText.setId("inputField");
		final TextField locationTextField = new TextField();
		locationBox.getChildren().addAll(locationText, locationTextField);

		final VBox personsBox = new VBox();
		final Label personsText = new Label("Photograph, Personen|Organisationen");
		personsText.setId("inputField");
		final TextField personTextField = new TextField();
		personsBox.getChildren().addAll(personsText, personTextField);

		final VBox tagBox = new VBox();
		final Label tagText = new Label("Tag (optional)");
		tagText.setId("inputField");
		final TextField tagTextfield = new TextField();
		tagBox.getChildren().addAll(tagText, tagTextfield);

		this.removalButton = new Button("Löschen");
		this.removalButton.setOnAction(event ->
		{
			final FileObject file = ContentManager.getInstance().file.get();

			try
			{
				final com.sun.jna.platform.FileUtils fileUtils = com.sun.jna.platform.FileUtils.getInstance();
				if (fileUtils.hasTrash())
					fileUtils.moveToTrash(new File[] { file.toFile() });
				else
					Main.openDynamic(new Text("No Trash available"));
			}
			catch (final IOException e)
			{
				final Text errorText = new Text();
				if (e instanceof FileExistsException)
					errorText.setText("Datei kann nicht verschoben werden, da die sie im Zielordner schon existiert!");
				else
					errorText.setText("Source or destination aren't valid or access rights to the file are restricted!");
				Main.openDynamic(errorText);
			}
			ContentManager.getInstance().nextFile();
		});

		this.continueButton = new Button("Speichern");
		this.continueButton.setOnAction(event ->
		{
			final FileObject file = ContentManager.getInstance().file.get();

			// TODO Apply naming logic here!

			final FileObject newFile = FileObject.create().setPath(ContentManager.getInstance().sortedPath).setFullName(locationTextField.getText().concat(" - ").concat(personTextField.getText()).concat(file.getFullExtension()));

			try
			{
				int i = 1;
				FileObject clone = newFile.clone();
				while (clone.toFile().exists())
				{
					clone = newFile.clone();
					clone.setName(newFile.getName().concat("_"));
					clone.setName(clone.getName().concat(String.valueOf(i++)));
					System.out.println(clone);
				}

				FileUtils.moveFile(file.toFile(), clone.toFile());
			}
			catch (final IOException e)
			{
				final Text errorText = new Text();
				if (e instanceof FileExistsException)
					errorText.setText("Datei kann nicht verschoben werden, da die sie im Zielordner schon existiert!");
				else
					errorText.setText("Source or destination aren't valid or access rights to the file are restricted!");
				Main.openDynamic(errorText);
			}
			ContentManager.getInstance().nextFile();
		});

		final HBox buttons = new HBox();
		buttons.getChildren().addAll(this.continueButton, this.removalButton);
		buttons.setSpacing(10);
		buttons.setAlignment(Pos.CENTER);

		this.getChildren().addAll(locationBox, personsBox, tagBox, buttons);
	}

	@Override
	public void changed(final ObservableValue<? extends FileObject> observable, final FileObject oldValue, final FileObject newValue)
	{
		this.continueButton.setDisable(newValue.getFullPath().isEmpty());
	}
}
