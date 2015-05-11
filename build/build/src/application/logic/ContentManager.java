package application.logic;

import java.io.File;
import java.util.Collection;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.geometry.Pos;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.HiddenFileFilter;
import org.apache.commons.io.filefilter.TrueFileFilter;

import application.Main;
import de.mixedfx.config.SettingsManager;
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

	public ObjectProperty<FileObject>	file;
	public IntegerProperty				filesCount;

	private ContentManager()
	{
		this.unsortedPath = SettingsManager.getValue("Allgemein", "MedienPfad-Unsortiert", "C:\\Users\\Public\\Pictures\\Sample Pictures");
		this.sortedPath = SettingsManager.getValue("Allgemein", "MedienPfad-Sortiert", "C:\\");

		this.file = new SimpleObjectProperty<>(new FileObject());
		this.filesCount = new SimpleIntegerProperty(0);
	}

	public boolean validateConfig()
	{
		final VBox box = new VBox();
		box.setStyle("-fx-background-color: red");
		box.setAlignment(Pos.CENTER);
		box.setSpacing(10);

		if (!FileObject.create().setPath(this.unsortedPath).isValid() || !FileObject.create().setPath(this.unsortedPath).toFile().exists())
			box.getChildren().add(new Text("Folgender Pfad ist nicht erreichbar: " + this.unsortedPath));

		if (!FileObject.create().setPath(this.sortedPath).isValid() || !FileObject.create().setPath(this.sortedPath).toFile().exists())
			box.getChildren().add(new Text("Folgender Pfad ist nicht erreichbar: " + this.sortedPath));

		if (box.getChildren().size() > 0)
		{
			box.getChildren().add(0, new Text("Bitte korrigiere den Fehler in der config###settings.ini oder lösche diese Datei. Starte danach das Programm neu!"));
			Main.openDynamic(box);
		}

		return box.getChildren().size() == 0 ? true : false;
	}

	/**
	 * Sets {@link #file} to the next one or an empty {@link FileObject}
	 */
	public void nextFile()
	{
		final File path = new File(this.unsortedPath);

		final Collection<File> files = FileUtils.listFiles(path, HiddenFileFilter.VISIBLE, TrueFileFilter.TRUE);
		// TODO Handle expception if path is not a valid path

		this.filesCount.set(files.size());

		if (files.size() != 0)
			this.file.set(new FileObject((File) files.toArray()[0]));
		else
			this.file.set(new FileObject());
	}
}
