package application.gui;

import application.logic.ContentManager;
import javafx.geometry.Pos;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;

public class TitleView extends HBox
{
	private Text	text;

	public TitleView()
	{
		text = new Text();
		text.textProperty().bind(ContentManager.getInstance().user.concat(" Fotos & Videos"));

		this.setAlignment(Pos.CENTER);
		this.getChildren().add(text);
	}
}
