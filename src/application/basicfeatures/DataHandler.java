package application.basicfeatures;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FilenameFilter;
import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.filefilter.DirectoryFileFilter;
import org.apache.commons.io.filefilter.RegexFileFilter;
import org.apache.commons.io.filefilter.TrueFileFilter;

/**
 * <pre>
 * FileFolderHandler offers generic methods to access/change/delete
 * files and folders.
 * 
 * TODO:
 * Exception Handling
 * </pre>
 *
 * @author Jerry
 */
public class DataHandler
{
	/**
	 * Prevents other classes from creating an own instance of FileFolderHandler
	 */
	private DataHandler()
	{
	}

	/**
	 * Creates or finds a file
	 *
	 * @param fullPath
	 *            The full absolute directory + file name
	 * @return Returns the new created file or the found file which already
	 *         exists
	 */
	public static File createOrFindFile(final String fullPath)
	{
		final File newFile = new File(fullPath);

		if (newFile.exists() && !newFile.isDirectory())
		{
			// File exists
			// No reliable check solution available to check if file is already
			// opened by another program etc.
		}
		else
		{
			// File doesn't exist already
			newFile.getParentFile().mkdirs();
			try
			{
				newFile.createNewFile();
			}
			catch (final IOException e)
			{
				// TODO: Handle Exception
				// For example no access on drive
				e.printStackTrace();
			}
		}

		// File creation needs sometimes some time :)
		while (!newFile.exists() || !newFile.canWrite() || !newFile.canRead())
		{
			try
			{
				Thread.sleep(10);
			}
			catch (final InterruptedException e)
			{
				// TODO
				e.printStackTrace();
			}
		}

		return newFile;
	}

	/**
	 * Finds a file
	 *
	 * @param fullPath
	 *            The full absolute directory + file name
	 * @return Returns the found file if exists
	 * @throws Exception
	 *             Throws exception if file doesn't exist
	 */
	public static File getFile(final String fullPath) throws FileNotFoundException
	{
		return getFile(FilenameUtils.getFullPath(fullPath), FilenameUtils.getName(fullPath));
	}

	/**
	 * Finds a file by
	 *
	 * @param path
	 *            The path (can be a part or the full absolute path).
	 * @param fileFolder
	 *            The fileName with or without extension! If without extension
	 *            it finds first a file with equal file name which has no
	 *            extension, otherwise the first file with equal file name with
	 *            extension, otherwise is the file not found.
	 * @return Returns the found file if exists otherwise throw Exception
	 * @throws FileNotFoundException
	 *             Throws exception if file doesn't exist
	 */
	public static File getFile(final String path, String fileFolder) throws FileNotFoundException
	{
		File result = null;
		if (path.equalsIgnoreCase("") || !(new File(path)).exists()
				|| !(new File(path)).isDirectory())
		{
			throw new FileNotFoundException("Path is empty!");
		}

		fileFolder = fileFolder.toLowerCase();
		final Collection<File> allFilesInPath = FileUtils.listFiles(new File(path),
				TrueFileFilter.TRUE, null);

		for (final File f : allFilesInPath)
		{
			if (FilenameUtils.getExtension(fileFolder) == "")
			{
				// fileName doesn't contain an extension
				if (FilenameUtils.getBaseName(f.toString().toLowerCase()).equals(fileFolder))
				{
					result = f;
					break;
				}
			}
			else
			{
				// fileName does contain an extension
				if (FilenameUtils.getName(f.toString()).toString().toLowerCase().equals(fileFolder))
				{
					result = f;
					break;
				}
			}
		}

		if (result != null)
		{
			return result;
		}
		else
		{
			throw new FileNotFoundException("File doesn't exist or is a directory!");
		}
	}

	/**
	 * Deletes a file
	 *
	 * @param fullPath
	 *            The full absolute directory + file name
	 * @return True if file was successfully deleted or false if deletion failed
	 */
	public static boolean deleteFile(final String fullPath)
	{
		boolean success;

		final File file = new File(fullPath);

		if (!file.exists())
		{
			// File doesn't exist already
			success = true;
		}
		else
		{
			// File exists
			if (!file.isDirectory())
			{
				if (file.delete())
				{
					success = true;
				}
				else
				{
					success = false;
				}
			}
			else
			{
				try
				{
					FileUtils.deleteDirectory(file);
					success = true;
				}
				catch (final IOException e)
				{
					success = false;
				}
			}
		}

		return success;
	}

	/**
	 * Fuses directory and file to one full path
	 *
	 * @param directory
	 * @param fileFolder
	 * @return
	 */
	public static String fuse(String directory, final String fileFolder)
	{
		String fullPath = "";

		final String substring = directory.length() > 1 ? directory
				.substring(directory.length() - 1) : directory;
		if (!substring.equals("\\"))
		{
			directory += "\\";
		}

		fullPath = directory + fileFolder;

		return fullPath;
	}

	public static BigInteger getSize(final File file)
	{
		return FileUtils.sizeOfAsBigInteger(file);
	}

	/**
	 * Compares two files/directories by checking file/folder size and name
	 * (ignoring case)
	 *
	 * @param toCompare1
	 * @param toCompare2
	 * @return Returns true if conditions are the same.
	 */
	public static boolean isSimiliar(final File toCompare1, final File toCompare2)
	{
		if ((toCompare1.getName().equalsIgnoreCase(toCompare2.getName()))
				&& (getSize(toCompare1) == getSize(toCompare2)))
		{
			return true;
		}
		else
		{
			return false;
		}
	}

	/**
	 * Compares relatives with absolute paths/files and vice versa
	 *
	 * @param fileFolder
	 * @return
	 */
	public static boolean isEqual(final String fileFolder1, final String fileFolder2)
	{
		if ((new File(fileFolder1).toURI().toString().equalsIgnoreCase((new File(fileFolder2)
				.toURI().toString()))))
		{
			return true;
		}
		else
		{
			return false;
		}
	}

	public static Collection<File> getExecuteables(final String directory)
	{
		return FileUtils.listFiles(new File(directory), new RegexFileFilter("(.*\\.exe)$"),
				DirectoryFileFilter.DIRECTORY);
	}

	/**
	 * Only returns the direct subfolders!
	 *
	 * @param parentFolder
	 * @return
	 */
	public static ArrayList<String> getSubFolderList(final FileObject parentFolder)
	{
		final File file = new File(parentFolder.getFullPath());
		final String[] result = file.list(new FilenameFilter()
		{
			@Override
			public boolean accept(final File current, final String name)
			{
				return new File(current, name).isDirectory();
			}
		});
		return new ArrayList<String>(Arrays.asList(result));
	}

	/**
	 * Checks if String is a Valid Path
	 *
	 * @param path
	 *            to Validate
	 * @return boolean
	 */
	public static boolean isValidPath(final String path)
	{
		try
		{
			final File toTest = new File(path);
			toTest.getCanonicalPath();
			return true;
		}
		catch (final IOException e)
		{
			return false;
		}
	}
}
