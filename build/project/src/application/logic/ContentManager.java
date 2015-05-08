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

import de.mixedfx.config.SettingsManager;
import de.mixedfx.file.DataHandler;
import de.mixedfx.file.FileObject;

public class ContentManager
{
	private static ContentManager	INSTANCE;

	public static ContentManager getInstance()
	{
		if (ContentManager.INSTANCE == null)
			ContentManager.INSTANCE = new ContentManager();
		return ContentManager.INSTANCE;
	}

	public String						unsortedPath;
	public String						sortedPath;

	public StringProperty				user;
	public ObjectProperty<FileObject>	file;
	public IntegerProperty				filesCount;

	private ContentManager()
	{
		this.unsortedPath = SettingsManager.getValue("Allgemein", "MedienPfad-Unsortiert", "C:\\Users\\Public\\Pictures");
		this.sortedPath = SettingsManager.getValue("Allgemein", "MedienPfad-Sortiert", "C:\\");

		this.user = new SimpleStringProperty("Sample Pictures");
		this.file = new SimpleObjectProperty<>(new FileObject());
		this.filesCount = new SimpleIntegerProperty(0);
	}

	/**
	 * Sets {@link #file} to the next one or an empty {@link FileObject}
	 */
	public void nextFile()
	{
		final File path = new File(DataHandler.fuse(this.unsortedPath, this.user.get()));
		final Collection<File> files = FileUtils.listFiles(path, HiddenFileFilter.VISIBLE, TrueFileFilter.TRUE);
		// TODO Handle expception if path is not a valid path

		this.filesCount.set(files.size());

		if (files.size() != 0)
			this.file.set(new FileObject((File) files.toArray()[0]));
		else
			this.file.set(new FileObject());
	}
}
