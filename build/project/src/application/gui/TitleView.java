package application.gui;

import javafx.geometry.Pos;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;

public class TitleView extends HBox
{
	private final Text	text;

	public TitleView()
	{
		this.text = new Text();
		this.text.textProperty().set(" Fotos & Videos");

		this.setAlignment(Pos.CENTER);
		this.getChildren().add(this.text);
	}
}
