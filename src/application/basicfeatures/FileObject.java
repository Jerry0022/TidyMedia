package application.basicfeatures;

import java.io.File;

import org.apache.commons.io.FilenameUtils;

/**
 * @author Jerry
 *
 *         <pre>
 * Provides a class to create an Object representing a File but only contains Strings.
 * Also contains methods to get elements of this File representation such as path, name and extension!
 * Implementing equals(), toString() and clone() method!
 * Read the JavaDoc of the methods to produce no undesired results!
 * </pre>
 */
public class FileObject implements Cloneable
{
	private static final String	prefixSeparator		= "###";
	private static final String	extensionSeparator	= ".";

	public static FileObject create()
	{
		return new FileObject();
	}

	public static FileObject create(File file)
	{
		return new FileObject(file);
	}

	private String	path;		// pure path maybe without last folder
	private String	prefix;	// without separator
	private String	name;		// pure name without sth. else
	private String	extension;	// without separator

	/**
	 * <pre>
	 * Fills all available values with empty Strings.
	 * Please fill manually the values via:
	 * {@link FileObject#setPath(String)}
	 * {@link FileObject#setPrefix(String)}
	 * {@link FileObject#setName(String)}
	 * {@link FileObject#setExtension(String)}
	 * or:
	 * {@link FileObject#setFullPath(String)}
	 * or:
	 * {@link FileObject#setPath(String)}
	 * {@link FileObject#setFullName(String)}
	 * </pre>
	 */
	public FileObject()
	{
		this.clear();
	}

/**
	 * Fills automatically all available values!
	 * To avoid directory recognition problems use new {@link FileObject}() + {@link FileObject#setPath(String)
	 * @param fullpath (relative or absolute) directory (optionally including file name with/without extension)
	 */
	@Deprecated
	public FileObject(String fullPath)
	{
		this.setFullPath(fullPath);
	}

	public FileObject(File file)
	{
		this.clear();
		if (file.isDirectory())
		{
			this.setPath(FilenameUtils.getFullPath(file.getAbsolutePath()));
		}
		else if (file.isFile())
		{
			this.setPath(FilenameUtils.getFullPath(file.getAbsolutePath()));
			this.setFullName(FilenameUtils.getName(file.getAbsolutePath()));
		}
	}

	public File getFile()
	{
		return new File(this.getFullPath());
	}

	public String getURI()
	{
		return (new File(this.getFullPath())).toURI().toString();
	}

	// PROPOSAL Add setURI(File f);

	/**
	 * @param toCompare
	 * @return
	 */
	public boolean equals(FileObject toCompare)
	{
		if (this.getFullPath().equalsIgnoreCase(toCompare.getFullPath()))
		{
			return true;
		}
		else
		{
			return false;
		}
	}

	@Override
	public String toString()
	{
		return this.getClass().getSimpleName() + ": " + this.getFullPath();
	}

	@Override
	public FileObject clone()
	{
		FileObject clone = new FileObject();
		clone.setPath(this.getPath());
		clone.setFullName(this.getFullName());
		return clone;
	}

	// Getters and Setters

	public void clear()
	{
		this.path = "";
		this.prefix = "";
		this.name = "";
		this.extension = "";
	}

	/**
	 * If this object represents a directory this is maybe not complete. In this
	 * case use {@link FileObject#getFullPath()} instead!
	 *
	 * @return Returns only the path!
	 */
	public String getPath()
	{
		return this.path;
	}

	/**
	 * Returns the whole path including the file/folder name with prefix and
	 * extension
	 *
	 * @return
	 */
	public String getFullPath()
	{
		if (this.getPath().equals("") && this.getFullName().equals(""))
		{
			return "";
		}
		else
		{
			return DataHandler.fuse(this.getPath(), this.getFullName());
		}
	}

	/**
	 * @param path
	 *            Set the path (excluding file)
	 */
	public FileObject setPath(String path)
	{
		this.path = path;
		return this;
	}

	/**
	 * !!! Overwrites all previously set values! If only path it is not fully
	 * reliable! Use better setPath() and set(Full)Name instead!
	 *
	 * @param fullPath
	 */
	@Deprecated
	public FileObject setFullPath(String fullPath)
	{
		this.setPath(FilenameUtils.getFullPath(fullPath));
		this.setFullName(FilenameUtils.getName(fullPath));
		return this;
	}

	/**
	 * @return Returns only the prefix.
	 */
	public String getPrefix()
	{
		return this.prefix;
	}

	/**
	 * @return Returns the prefix plus separator
	 */
	public String getFullPrefix()
	{
		if (this.getPrefix().equals(""))
		{
			return this.getPrefix();
		}
		else
		{
			return this.getPrefix() + prefixSeparator;
		}
	}

	/**
	 * @param prefix
	 *            Set the prefix with/without separator
	 */
	public FileObject setPrefix(String prefix)
	{
		if (prefix.endsWith(prefixSeparator))
		{
			this.prefix = prefix.replaceFirst(prefixSeparator, "");
		}
		else
		{
			this.prefix = prefix;
		}
		return this;
	}

	/**
	 * @return Returns the name without prefix and without extension
	 */
	public String getName()
	{
		return this.name;
	}

	/**
	 * @return Returns the name with prefix and without extension
	 */
	public String getFullNameWithoutExtension()
	{
		return this.getFullPrefix() + this.getName();
	}

	/**
	 * @return Returns the name with prefix and with extension
	 */
	public String getFullName()
	{
		return this.getFullNameWithoutExtension() + this.getFullExtension();
	}

	/**
	 * Set the name excluding directory, prefix and extension.
	 *
	 * @param name
	 *            File/Folder name without prefix without extension
	 */
	public FileObject setName(String name)
	{
		this.name = name;
		return this;
	}

	/**
	 * !!! Overwrites all previously set values!
	 *
	 * @param name
	 *            File/Folder name with/without prefix with/without extension,
	 *            but without the (parent) directory
	 */
	public FileObject setFullName(String name)
	{
		this.setExtension(FilenameUtils.getExtension(name));

		name = FilenameUtils.getBaseName(name);
		String prefix = name.split(prefixSeparator)[0];
		if (!prefix.equals(name))
		{
			this.setPrefix(prefix);
			this.setName(name.substring(prefix.length() + prefixSeparator.length()));
		}
		else
		{
			this.setName(name);
			this.prefix = "";
		}
		return this;
	}

	/**
	 * @return Returns the extension without separator
	 */
	public String getExtension()
	{
		return this.extension;
	}

	/**
	 * @return Returns separator + extension
	 */
	public String getFullExtension()
	{
		if (this.getExtension().equals(""))
		{
			return this.getExtension();
		}
		else
		{
			return extensionSeparator + this.getExtension();
		}
	}

	/**
	 * @param extension
	 *            E. g. ".png" or only "png"
	 */
	public FileObject setExtension(String extension)
	{
		if (extension.startsWith(extensionSeparator))
		{
			this.extension = extension.replaceFirst("\\" + extensionSeparator, "");
		}
		else
		{
			this.extension = extension;
		}
		return this;
	}
}
