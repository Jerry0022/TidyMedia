package application.basicfeatures;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.math.BigInteger;

import org.apache.commons.io.FileUtils;

import de.mixedfx.file.DataHandler;
import de.mixedfx.file.FileObject;

public class ConManager
{
	public static final String	myDir	= "assets";

	/**
	 * @param path
	 * @param name
	 *            Should contain also the extension!
	 * @param toWrite
	 * @return Returns the file or null if it could not create a file.
	 */
	public static File writeFile(final FileObject data)
	{
		try
		{
			return DataHandler.createOrFindFile(data);
		}
		catch (IOException | InterruptedException e)
		{
			return null;
		}
	}

	public static File createFolder(final FileObject folder) throws IOException
	{
		FileUtils.forceMkdir(folder.getFile());
		return folder.getFile();
	}

	/**
	 * @param path
	 * @param name
	 * @return
	 * @throws FileNotFoundException
	 *             Throws an exception because it doesn't know if it should return an image or ...
	 */
	public static File readFile(final FileObject data) throws FileNotFoundException
	{
		return DataHandler.readFile(data);
	}

	public static boolean isEqual(final FileObject f1, final FileObject f2)
	{
		return f1.equals(f2);
	}

	public static BigInteger getSize(final FileObject fullPath) throws FileNotFoundException
	{
		return FileUtils.sizeOfAsBigInteger(ConManager.readFile(fullPath));
	}
}