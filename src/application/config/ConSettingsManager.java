package application.config;

import application.basicfeatures.FileObject;

public class ConSettingsManager
{
	public static String		fileName	= "settings";
	private static ConfigMaker	configMaker;

	public static synchronized ConfigMaker init()
	{
		configMaker = ConfigHandler.read(FileObject.create()
				.setPath(System.getProperty("user.dir")).setName(fileName));

		return configMaker;
	}

	/**
	 * Usually should not be used. Only e. g. if you want to write Colors with
	 * ConfigHandler.
	 * 
	 * @return
	 */
	public static ConfigMaker getConfigMaker()
	{
		return configMaker;
	}

	/**
	 * Sets a default value or reads out the written value and returns the maybe
	 * modified correct value. Write and read
	 * 
	 * @param section
	 * @param key
	 * @param defaultValue
	 * @return
	 */
	public static String getValue(String section, String key, String defaultValue)
	{
		ConfigItem item = new ConfigItem(section, key);

		if (configMaker.getValue(item).equals(""))
		{
			configMaker.setConfigItem(item.setValue(defaultValue));
		}
		else
		{
			configMaker.setConfigItem(item.setValue(configMaker.getValue(item)));
		}

		return item.getValue();
	}

	private static String getValue(String section, String key)
	{
		return configMaker.getValue(new ConfigItem(section, key));
	}

	/**
	 * Writes the value into the config from now it is usable with the read
	 * methods.
	 * 
	 * @param section
	 * @param key
	 * @param value
	 */
	public static void setValue(String section, String key, String value)
	{
		configMaker.setConfigItem(new ConfigItem(section, key, value));
	}
}
