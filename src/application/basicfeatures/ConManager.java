package application.basicfeatures;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.math.BigInteger;

import org.apache.commons.io.FileUtils;

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
	public static File writeFile(FileObject data)
	{
		return DataHandler.createOrFindFile(data.getFullPath());
	}

	public static File createFolder(FileObject folder) throws IOException
	{
		FileUtils.forceMkdir(folder.getFile());
		return folder.getFile();
	}

	/**
	 * @param path
	 * @param name
	 * @return
	 * @throws FileNotFoundException
	 *             Throws an exception because it doesn't know if it should
	 *             return an image or ...
	 */
	public static File readFile(FileObject data) throws FileNotFoundException
	{
		return DataHandler.getFile(data.getPath(), data.getFullName());
	}

	public static boolean isEqual(FileObject f1, FileObject f2)
	{
		return DataHandler.isEqual(f1.getFullPath(), f2.getFullPath());
	}

	public static BigInteger getSize(FileObject fullPath) throws FileNotFoundException
	{
		return DataHandler.getSize(readFile(fullPath));
	}
}