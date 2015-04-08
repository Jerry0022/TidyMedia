package application.config;

import java.util.ArrayList;
import java.util.Iterator;

import org.apache.commons.configuration2.INIConfiguration;
import org.apache.commons.configuration2.builder.FileBasedConfigurationBuilder;
import org.apache.commons.configuration2.builder.fluent.Configurations;

import application.basicfeatures.DataHandler;

public class ConfigMaker
{
	private INIConfiguration	config;

	/**
	 * @param filePath
	 * @throws Exception
	 */
	public ConfigMaker(String filePath) throws Exception
	{
		this(filePath, false);
	}

	/**
	 * @param filePath
	 * @param reset
	 * @throws Exception
	 */
	public ConfigMaker(String filePath, boolean reset) throws Exception
	{
		if (reset)
			DataHandler.deleteFile(filePath);

		FileBasedConfigurationBuilder<INIConfiguration> builder = (new Configurations())
				.iniBuilder(DataHandler.createOrFindFile(filePath));
		builder.setAutoSave(true);
		this.config = builder.getConfiguration();
	}

	/**
	 * Read-Only
	 * 
	 * @return
	 */
	public ArrayList<String> getSections()
	{
		return new ArrayList<String>(config.getSections());
	}

	/**
	 * Read-Only
	 * 
	 * @param section
	 * @return
	 */
	public ArrayList<ConfigItem> getKeys(String section)
	{
		ArrayList<ConfigItem> result = new ArrayList<ConfigItem>();

		Iterator<String> iterator = config.getKeys(section);
		while (iterator.hasNext())
		{
			String fullKey = iterator.next();

			String[] sectionKey = fullKey.split("\\.");
			ConfigItem item = new ConfigItem(section, sectionKey[1]);
			result.add(readIniValue(item));
		}

		return result;
	}

	/**
	 * Read-Only
	 * 
	 * @param item
	 * @return
	 */
	public String getValue(ConfigItem item)
	{
		for (ConfigItem k : getKeys(item.getSection()))
		{
			if (k.equals(item))
			{
				return k.getValue();
			}
		}
		return "";
	}

	/**
	 * Use this function to write! Write-Only
	 * 
	 * @param section
	 * @param key
	 * @param value
	 */
	public ConfigMaker setConfigItem(ConfigItem item)
	{
		config.setProperty(item.getSectionKey(), item.getValue());
		return this;
	}

	/**
	 * Reads the value and fills it into the ConfigItem. Read-Only
	 * 
	 * @param item
	 *            Section and Key is used from that item.
	 * @return Returns the item.
	 */
	public ConfigItem readIniValue(ConfigItem item)
	{
		item.setValue((String) config.getProperty(item.getSectionKey()));
		return item;
	}

	/**
	 * Write-Only
	 */
	public ConfigMaker clearAll()
	{
		config.clear();
		return this;
	}

	/**
	 * Write-Only
	 * 
	 * @param section
	 */
	public ConfigMaker clearSection(String section)
	{
		config.clearTree(section);
		return this;
	}

	/**
	 * Write-Only
	 * 
	 * @param item
	 */
	public ConfigMaker clearValue(ConfigItem item)
	{
		item.setValue(null);
		config.clearProperty(item.getSectionKey());
		return this;
	}
}
