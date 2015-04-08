package application.media;

import javafx.scene.Node;
import javafx.scene.image.ImageView;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import application.basicfeatures.FileObject;
import application.basicfeatures.ImageHandler;

public class MediaHandler
{
	public static Node getView(FileObject object)
	{
		ImageView imageView;
		MediaView mediaView;

		try
		{
			imageView = new ImageView(ImageHandler.readImage(object));
			return imageView;
		}
		catch (Exception ie)
		{
			try
			{
				Media media = new Media(object.getFullPath());
				mediaView = new MediaView(new MediaPlayer(media));
				return mediaView;
			}
			catch (Exception me)
			{
				// Image image = ImageHandler.readImage(data); // TODO Add here
				// default image
				// TODO Handle exception
				imageView = new ImageView();
				return imageView;
			}
		}
	}
}
