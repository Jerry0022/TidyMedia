package application.media;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;

import javax.swing.filechooser.FileSystemView;

public class IconGetter
{
	private static HashMap<String, Image>	mapOfFileExtToSmallIcon	= new HashMap<String, Image>();

	public static Image getFileIcon(final String fname)
	{
		final String ext = IconGetter.getFileExt(fname);

		Image fileIcon = IconGetter.mapOfFileExtToSmallIcon.get(ext);
		if (fileIcon == null)
		{

			javax.swing.Icon jswingIcon = null;

			final File file = new File(fname);
			if (file.exists())
				jswingIcon = IconGetter.getJSwingIconFromFileSystem(file);
			else
			{
				File tempFile = null;
				try
				{
					tempFile = File.createTempFile("icon", ext);
					jswingIcon = IconGetter.getJSwingIconFromFileSystem(tempFile);
				}
				catch (final IOException ignored)
				{
					// Cannot create temporary file.
				}
				finally
				{
					if (tempFile != null)
						tempFile.delete();
				}
			}

			if (jswingIcon != null)
			{
				fileIcon = IconGetter.jswingIconToImage(jswingIcon);
				IconGetter.mapOfFileExtToSmallIcon.put(ext, fileIcon);
			}
		}

		return fileIcon;
	}

	private static String getFileExt(final String fname)
	{
		String ext = ".";
		final int p = fname.lastIndexOf('.');
		if (p >= 0)
			ext = fname.substring(p);
		return ext.toLowerCase();
	}

	private static javax.swing.Icon getJSwingIconFromFileSystem(final File file)
	{

		// Windows {
		final FileSystemView view = FileSystemView.getFileSystemView();
		final javax.swing.Icon icon = view.getSystemIcon(file);
		// }

		// OS X {
		// final javax.swing.JFileChooser fc = new javax.swing.JFileChooser();
		// javax.swing.Icon icon = fc.getUI().getFileView(fc).getIcon(file);
		// }

		return icon;
	}

	private static Image jswingIconToImage(final javax.swing.Icon jswingIcon)
	{
		final BufferedImage bufferedImage = new BufferedImage(jswingIcon.getIconWidth(), jswingIcon.getIconHeight(), BufferedImage.TYPE_INT_ARGB);
		jswingIcon.paintIcon(null, bufferedImage.getGraphics(), 0, 0);
		return SwingFXUtils.toFXImage(bufferedImage, null);
	}
}
