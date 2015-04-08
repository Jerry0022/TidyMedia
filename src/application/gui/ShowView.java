package application.gui;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;

public class ShowView extends BorderPane
{
	private ObjectProperty<Image>	imageProperty;

	public ShowView()
	{
		this.setStyle("-fx-background-color: black");
		imageProperty = new SimpleObjectProperty<>();
		// TODO Set not found image
	}

	private void readImage()
	{
		// TODO Set imageProperty
		// TODO Handle Video and 3D icons
	}
}
