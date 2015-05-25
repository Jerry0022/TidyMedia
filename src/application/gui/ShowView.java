package application.gui;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Text;
import application.logic.ContentManager;
import application.media.MediaHandler;
import de.mixedfx.file.FileObject;

public class ShowView extends BorderPane implements ChangeListener<FileObject>
{
	public ShowView()
	{
		this.setPadding(new Insets(0, 0, 0, 20));
		ContentManager.getInstance().file.addListener(this);
	}

	@Override
	public void changed(final ObservableValue<? extends FileObject> observable, final FileObject oldValue, final FileObject newValue)
	{
		this.setTop(new Label(newValue.getFullName()));

		if (newValue.getFullPath().isEmpty())
			this.setCenter(new Text("Alle Dateien verarbeitet! Bitte füge ein Bild hinzu und starte dieses Programm neu!"));
		else
			this.setCenter(MediaHandler.getView(newValue));
	}
}
