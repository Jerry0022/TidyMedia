package application.logic;

import java.io.File;
import java.util.Collection;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.HiddenFileFilter;
import org.apache.commons.io.filefilter.TrueFileFilter;

import application.basicfeatures.DataHandler;
import application.basicfeatures.FileObject;
import application.config.ConSettingsManager;

public class ContentManager
{
	private static ContentManager	INSTANCE;

	public static ContentManager getInstance()
	{
		if (INSTANCE == null)
			INSTANCE = new ContentManager();
		return INSTANCE;
	}

	public String						unsortedPath;
	public String						sortedPath;

	public StringProperty				user;
	public ObjectProperty<FileObject>	file;
	public IntegerProperty				filesCount;

	private ContentManager()
	{
		unsortedPath = ConSettingsManager.getValue("Allgemein", "MedienPfad-Unsortiert",
				"C:\\Users\\Public\\Pictures");
		sortedPath = ConSettingsManager.getValue("Allgemein", "MedienPfad-Sortiert", "C:\\");

		user = new SimpleStringProperty("Sample Pictures");
		file = new SimpleObjectProperty<>(new FileObject());
		filesCount = new SimpleIntegerProperty(0);
	}

	/**
	 * Sets {@link #file} to the next one or an empty {@link FileObject}
	 */
	public void nextFile()
	{
		File path = new File(DataHandler.fuse(unsortedPath, user.get()));
		Collection<File> files = FileUtils.listFiles(path, HiddenFileFilter.VISIBLE,
				TrueFileFilter.TRUE);
		// TODO Handle expception if path is not a valid path

		filesCount.set(files.size());

		if (files.size() != 0)
			file.set(new FileObject((File) files.toArray()[0]));
		else
			file.set(new FileObject());
	}
}
