package application.media;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.util.Date;
import java.util.Locale;

import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;

import org.apache.commons.io.FileUtils;
import org.controlsfx.control.InfoOverlay;

import application.basicfeatures.FileObject;
import application.basicfeatures.ImageHandler;

import com.drew.imaging.ImageMetadataReader;
import com.drew.metadata.Metadata;
import com.drew.metadata.exif.ExifSubIFDDirectory;

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

			// Retrieve date from image
			Metadata metadata = ImageMetadataReader.readMetadata(object.getFile());
			ExifSubIFDDirectory directory = metadata
					.getFirstDirectoryOfType(ExifSubIFDDirectory.class);
			Date date = directory.getDate(ExifSubIFDDirectory.TAG_DATETIME_ORIGINAL);
			DateFormat formatter = DateFormat.getDateInstance(DateFormat.MEDIUM, new Locale("de",
					"DE"));

			double fileSizeMB = FileUtils.sizeOf(object.getFile()) * 1000 / 1024 / 1024;
			DecimalFormat decimalFormatter = new DecimalFormat("#0.000");

			String text = "Weitere Infos\n" + "Erstellungsdatum: " + formatter.format(date)
					+ "\nDateigröße: " + decimalFormatter.format(fileSizeMB / 1000) + " MB";

			InfoOverlay infoImage = new InfoOverlay(imageView, text);
			infoImage.setShowOnHover(true);

			pane.getChildren().add(infoImage);
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
