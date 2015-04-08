package application.gui;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Text;
import application.basicfeatures.FileObject;
import application.logic.ContentManager;
import application.media.MediaHandler;

public class ShowView extends BorderPane implements ChangeListener<FileObject>
{

	public ShowView()
	{
		this.setStyle("-fx-background-color: yellow");
		ContentManager.getInstance().file.addListener(this);
	}

	@Override
	public void changed(ObservableValue<? extends FileObject> observable, FileObject oldValue,
			FileObject newValue)
	{
		this.setTop(new Text(newValue.getFullName()));
		this.setCenter(MediaHandler.getView(newValue));
	}
}
