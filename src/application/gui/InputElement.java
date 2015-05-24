package application.gui;

import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

public class InputElement extends VBox
{
	public InputElement(String title, Node node)
	{
		Label label = new Label(title);
		label.setId("inputField");
		this.getChildren().addAll(label, node);
	}
}
