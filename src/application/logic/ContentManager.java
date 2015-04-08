package application.logic;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class ContentManager
{
	private static ContentManager	INSTANCE;

	public static ContentManager getInstance()
	{
		if (INSTANCE == null)
			INSTANCE = new ContentManager();
		return INSTANCE;
	}

	public StringProperty	user;

	private ContentManager()
	{
		user = new SimpleStringProperty("No user selected!");
	}
}
