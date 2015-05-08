package application.gui;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Text;
import application.logic.ContentManager;
import application.media.MediaHandler;
import de.mixedfx.file.FileObject;

public class ShowView extends BorderPane implements ChangeListener<FileObject>
{
	public ShowView()
	{
		this.setStyle("-fx-background-color: yellow");
		ContentManager.getInstance().file.addListener(this);
	}

	@Override
	public void changed(final ObservableValue<? extends FileObject> observable, final FileObject oldValue, final FileObject newValue)
	{
		this.setTop(new Text(newValue.getFullName()));

		if (newValue.getFullPath().isEmpty())
			this.setCenter(new Text("No files found! Please add file and restart!"));
		else
			this.setCenter(MediaHandler.getView(newValue));
	}
}
