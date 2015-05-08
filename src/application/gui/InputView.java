package application.gui;

import java.io.IOException;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import org.apache.commons.io.FileUtils;

import application.Main;
import application.logic.ContentManager;
import de.mixedfx.file.FileObject;

public class InputView extends VBox implements ChangeListener<FileObject>
{
	private final Button	continueButton;

	public InputView()
	{
		ContentManager.getInstance().file.addListener(this);

		final TextField locationTextField = new TextField();
		final TextField personTextField = new TextField();

		this.continueButton = new Button("Speichern und fortfahren");
		this.continueButton.setOnAction(event ->
		{
			final FileObject file = ContentManager.getInstance().file.get();

			// TODO Apply naming logic here!
			final FileObject newFile = FileObject.create().setPath(ContentManager.getInstance().sortedPath).setFullName(file.getFullNameWithoutExtension() + "" + file.getFullExtension());

			try
			{
				FileUtils.moveFile(file.getFile(), newFile.getFile());
			}
			catch (final IOException e)
			{
				Main.openDynamic(new Text("Source or destination aren't valid or access rights to the file are restricted!"));
			}
			ContentManager.getInstance().nextFile();
		});

		this.getChildren().addAll(new Text("Ort"), locationTextField, new Text("Personen"), personTextField, this.continueButton);
	}

	@Override
	public void changed(final ObservableValue<? extends FileObject> observable, final FileObject oldValue, final FileObject newValue)
	{
		this.continueButton.setDisable(newValue.getFullPath().isEmpty());
	}
}
