package application.basicfeatures;

import java.io.FileNotFoundException;

import javafx.scene.image.Image;

public class ImageHandler
{
	public static Image readImage(final FileObject data) throws Exception
	{
		if (data.getFullPath().equals(""))
		{
			throw new FileNotFoundException();
		}
		Image image = new Image(ConManager.readFile(data).toURI().toString(), false);
		if (image.getException() != null)
			throw new Exception("File is not a supported image!");
		return image;
	}
}
