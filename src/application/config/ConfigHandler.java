package application.config;

import application.basicfeatures.FileObject;

public class ConfigHandler
{
	public static final String	prefix		= "config";
	public static final String	extension	= "ini";

	/**
	 * <pre>
	 * Finds (= reads) or creates a new empty config (=> 0 sections)!
	 * Applies automatically the {@link ConfigHandler#prefix}.
	 * Modifications are written to the file automatically (=> no writeConfig)!
	 * </pre>
	 * 
	 * @param fileObject
	 * @return Returns the ConfigMaker! Or throws serious ErrorExtended and
	 *         returns null!
	 */
	public static ConfigMaker read(FileObject fileObject)
	{
		fileObject.setPrefix(prefix);
		fileObject.setExtension(extension);

		try
		{
			ConfigMaker configMaker = new ConfigMaker(fileObject.getFullPath());
			return configMaker;
		}
		catch (Exception e)
		{
			// TODO Throw ErrorExtended
			// = Serious => Exit program
			e.printStackTrace();
			return null;
		}
	}
}
