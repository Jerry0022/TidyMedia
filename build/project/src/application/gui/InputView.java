package application.gui;

import java.io.File;
import java.io.IOException;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
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

		final TextField locationTextField = new TextField();
		final TextField personTextField = new TextField();

		this.removalButton = new Button("Löschen und fortfahren");
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

		this.continueButton = new Button("Speichern und fortfahren");
		this.continueButton.setOnAction(event ->
		{
			final FileObject file = ContentManager.getInstance().file.get();

			// TODO Apply naming logic here!
			final FileObject newFile = FileObject.create().setPath(ContentManager.getInstance().sortedPath).setFullName(file.getFullNameWithoutExtension() + "" + file.getFullExtension());

			try
			{
				FileUtils.moveFile(file.toFile(), newFile.toFile());
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

		this.getChildren().addAll(new Text("Ort"), locationTextField, new Text("Personen"), personTextField, this.continueButton, this.removalButton);
	}

	@Override
	public void changed(final ObservableValue<? extends FileObject> observable, final FileObject oldValue, final FileObject newValue)
	{
		this.continueButton.setDisable(newValue.getFullPath().isEmpty());
	}
}
