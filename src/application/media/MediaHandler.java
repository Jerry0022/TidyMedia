package application.media;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import javafx.geometry.Pos;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.controlsfx.control.InfoOverlay;

import com.drew.imaging.ImageMetadataReader;
import com.drew.metadata.Directory;
import com.drew.metadata.Metadata;
import com.drew.metadata.Tag;
import com.drew.metadata.exif.ExifDirectoryBase;
import com.drew.metadata.exif.ExifSubIFDDirectory;

import de.mixedfx.file.FileObject;
import de.mixedfx.image.ImageHandler;

// Implement:
// https://github.com/drewnoakes/metadata-extractor/blob/master/Samples/com/drew/metadata/GeoTagMapBuilder.java
public class MediaHandler
{
	public static Pane getView(final FileObject object)
	{
		final StackPane pane = new StackPane();
		pane.setMinSize(0, 0);
		pane.setAlignment(Pos.CENTER);

		try
		{
			final ImageView imageView = new ImageView(ImageHandler.readImage(object));
			imageView.fitWidthProperty().bind(pane.widthProperty());
			imageView.fitHeightProperty().bind(pane.heightProperty());

			final ArrayList<String> infoLines = new ArrayList<>();

			/*
			 * Retrieve date from image
			 */
			final Metadata metadata = ImageMetadataReader.readMetadata(object.toFile());
			final ExifSubIFDDirectory directory = metadata.getFirstDirectoryOfType(ExifSubIFDDirectory.class);
			final Date date = directory.getDate(ExifDirectoryBase.TAG_DATETIME_ORIGINAL);
			final DateFormat formatter = DateFormat.getDateInstance(DateFormat.MEDIUM, new Locale("de", "DE"));
			infoLines.add("Erstellungsdatum: " + formatter.format(date));

			/*
			 * Retrieve resolution from image
			 */
			String width = "";
			String height = "";
			for (final Directory dir : metadata.getDirectories())
				for (final Tag tag : dir.getTags())
					if (StringUtils.containsIgnoreCase(tag.getTagName(), "width"))
						width = StringUtils.replacePattern(tag.getDescription(), "[^0123456789\\.,]", "");
					else
						if (StringUtils.containsIgnoreCase(tag.getTagName(), "height"))
							height = StringUtils.replacePattern(tag.getDescription(), "[^0123456789\\.,]", "");

			if (width != "" || height != "")
				infoLines.add("Auflösung: " + width + "x" + height);

			/*
			 * Retrieve size from image
			 */
			final double fileSizeMB = FileUtils.sizeOf(object.toFile()) * 1000 / 1024 / 1024;
			final DecimalFormat decimalFormatter = new DecimalFormat("#0.000");
			infoLines.add("Dateigröße: " + decimalFormatter.format(fileSizeMB / 1000) + " MB");

			String text = "Weitere Infos\n";
			for (final String line : infoLines)
				text = text + line + "\n";

			final InfoOverlay infoImage = new InfoOverlay(imageView, text);
			infoImage.setShowOnHover(true);

			pane.getChildren().add(infoImage);
		}
		catch (final Exception ie)
		{
			try
			{
				final Media media = new Media(object.getFullPath());
				final MediaView mediaView = new MediaView(new MediaPlayer(media));
				pane.getChildren().add(mediaView);
			}
			catch (final Exception me)
			{
				final ImageView imageView = new ImageView(IconGetter.getFileIcon(object.getFullName()));
				imageView.setFitHeight(64);
				imageView.setFitWidth(64);
				pane.getChildren().add(imageView);
			}
		}
		return pane;
	}
}
