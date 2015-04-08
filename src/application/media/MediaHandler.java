package application.media;

import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import application.basicfeatures.FileObject;
import application.basicfeatures.ImageHandler;

public class MediaHandler
{
	public static Pane getView(FileObject object)
	{
		Pane pane = new Pane();
		ImageView imageView;
		MediaView mediaView;

		try
		{
			imageView = new ImageView(ImageHandler.readImage(object));
			imageView.fitWidthProperty().bind(pane.widthProperty());
			imageView.fitHeightProperty().bind(pane.heightProperty());
			pane.getChildren().add(imageView);
		}
		catch (Exception ie)
		{
			try
			{
				Media media = new Media(object.getFullPath());
				mediaView = new MediaView(new MediaPlayer(media));
				pane.getChildren().add(mediaView);
			}
			catch (Exception me)
			{
				// Image image = ImageHandler.readImage(data); // TODO Add here
				// default image
				// TODO Handle exception
				imageView = new ImageView();
				pane.getChildren().add(imageView);
			}
		}
		return pane;
	}
}
