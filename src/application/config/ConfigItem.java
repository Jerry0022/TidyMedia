package application.config;

public class ConfigItem
{
	private String	section;
	private String	key;
	private String	value;

	public ConfigItem(String section, String key)
	{
		this(section, key, "");
	}

	public ConfigItem(String section, String key, String value)
	{
		setSection(section);
		setKey(key);
		setValue(value);
	}

	public String getSection()
	{
		return section;
	}

	public String getKey()
	{
		return key;
	}

	/**
	 * @return Returns the following connected String: section.key
	 */
	public String getSectionKey()
	{
		return this.section + "." + this.getKey();
	}

	/**
	 * @param section
	 *            Must not be null.
	 */
	public ConfigItem setSection(String section)
	{
		if (section == null || section.equals(""))
			throw new NullPointerException("Section has to be set!");

		if (section.contains("."))
			throw new IllegalArgumentException("Section must not contain a dot!");

		this.section = section;

		return this;
	}

	/**
	 * @param key
	 *            Must not be null.
	 */
	public ConfigItem setKey(String key)
	{
		if (key == null || key.equals(""))
			throw new NullPointerException("Key has to be set!");

		if (key.contains("."))
			throw new IllegalArgumentException("Key must not contain a dot!");

		this.key = key;

		return this;
	}

	public String getValue()
	{
		return value;
	}

	/**
	 * @param value
	 *            Can be null.
	 */
	public ConfigItem setValue(String value)
	{
		if (value == null)
			value = "";

		this.value = value;

		return this;
	}

	@Override
	public String toString()
	{
		return "ConfigItem: " + this.getSection() + "." + this.getKey() + " = " + this.getValue();
	}

	public boolean equals(ConfigItem item)
	{
		if (this.getSectionKey().equals(item.getSectionKey()))
			return true;
		else
			return false;
	}
}
