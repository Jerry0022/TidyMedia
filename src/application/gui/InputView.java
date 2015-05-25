package application.gui;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.util.Callback;
import jfxtras.scene.control.CalendarTextField;

import org.apache.commons.io.FileExistsException;
import org.apache.commons.io.FileUtils;
import org.controlsfx.control.textfield.TextFields;
import org.controlsfx.validation.ValidationSupport;
import org.controlsfx.validation.Validator;
import org.controlsfx.validation.decoration.GraphicValidationDecoration;

import application.Main;
import application.logic.ContentManager;
import application.media.MediaHandler;
import de.mixedfx.file.FileObject;

public class InputView extends VBox implements ChangeListener<FileObject>
{
	private final BooleanProperty	textFieldsEmpty;
	private final Button			continueButton;
	private final Button			removalButton;

	private final CalendarTextField	dateTextField;
	private final SimpleDateFormat	dateConverter;

	public InputView()
	{
		this.textFieldsEmpty = new SimpleBooleanProperty();
		ContentManager.getInstance().file.addListener(this);
		this.setSpacing(10);

		ValidationSupport validator = new ValidationSupport();
		validator.setValidationDecorator(new GraphicValidationDecoration());

		final TextField locationTextField = new TextField();
		HBox.setHgrow(locationTextField, Priority.ALWAYS);
		ArrayList<String> countries = new ArrayList<>();
		for (String isoCode : Locale.getISOCountries())
		{
			Locale locale = new Locale("", isoCode);
			countries.add(isoCode + " (" + locale.getDisplayCountry(locale) + ")");
		}
		TextField countryTextField = new TextField("DE");
		countryTextField.setMaxWidth(30);
		countryTextField.textProperty().addListener(new ChangeListener<String>()
		{
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue)
			{
				if (newValue.length() > 2)
					countryTextField.setText(newValue.substring(0, 2));
			}
		});
		countryTextField.focusedProperty().addListener(new ChangeListener<Boolean>()
		{
			@Override
			public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue)
			{
				if (!countryTextField.getText().isEmpty())
				{
					if (!countries.contains(countryTextField.getText().toUpperCase()))
						countryTextField.setText("DE");
					else if (!newValue)
						countryTextField.setText(countryTextField.getText().toUpperCase());
				}
			}
		});
		validator.registerValidator(locationTextField, true, Validator.createEmptyValidator("Ort muss ausgefüllt werden!"));
		HBox locationCombination = new HBox(locationTextField, countryTextField);
		InputElement locationBox = new InputElement("Ort und Land", locationCombination);
		TextFields.bindAutoCompletion(countryTextField, countries.toArray());

		final VBox personsBox = new VBox();
		final Label personsText = new Label("Photograph, Personen|Organisationen");
		personsText.setId("inputField");
		final TextField personTextField = new TextField();
		validator.registerValidator(personTextField, true, Validator.createEmptyValidator("Photograph, Personen|Organisationen müssen ausgefüllt werden!"));
		personsBox.getChildren().addAll(personsText, personTextField);

		VBox dateBox = new VBox();
		Label dateText = new Label("Datum zum Zeitpunkt der Aufnahme");
		dateText.setId("inputField");
		dateConverter = new SimpleDateFormat("yyyyMMdd");
		dateTextField = new CalendarTextField();
		dateTextField.setAllowNull(false);
		dateTextField.setDateFormat(dateConverter);
		// TODO Set prompt not transparent
		dateTextField.setParseErrorCallback(new Callback<Throwable, Void>()
		{
			@Override
			public Void call(Throwable param)
			{
				System.out.println("Bitte ein Datum eingeben!");
				return null;
			}
		});
		dateTextField.setDateFormat(new SimpleDateFormat("yyyyMMdd"));
		dateBox.getChildren().addAll(dateText, dateTextField);

		final TextField tagTextfield = new TextField();
		InputElement tagBox = new InputElement("Tag (optional)", tagTextfield);

		this.removalButton = new Button("Löschen");
		this.removalButton.setDisable(true);
		this.removalButton.setOnAction(event ->
		{
			final FileObject file = ContentManager.getInstance().file.get();

			try
			{
				final com.sun.jna.platform.FileUtils fileUtils = com.sun.jna.platform.FileUtils.getInstance();
				if (fileUtils.hasTrash())
					fileUtils.moveToTrash(new File[] { file.toFile() });
				else
					Main.openDynamic(new Text("No Trash available"));
			}
			catch (final IOException e)
			{
				final Text errorText = new Text();
				if (e instanceof FileExistsException)
					errorText.setText("Datei kann nicht verschoben werden, da die sie im Zielordner schon existiert!");
				else
					errorText.setText("Source or destination aren't valid or access rights to the file are restricted!");
				Main.openDynamic(errorText);
			}
			ContentManager.getInstance().nextFile();
		});

		this.continueButton = new Button("Speichern");
		this.continueButton.setDisable(true);
		this.continueButton.setOnAction(event ->
		{
			// Remove leading and trailing whitespaces
			locationTextField.setText(locationTextField.getText().trim());
			personTextField.setText(personTextField.getText().trim());
			tagTextfield.setText(tagTextfield.getText().trim());

			// Get current file
			final FileObject file = ContentManager.getInstance().file.get();

			// Apply naming logic here!
			final FileObject newFile = FileObject.create().setPath(ContentManager.getInstance().sortedPath).setFullName(locationTextField.getText().concat(" [").concat(countryTextField.getText()).concat("] ").concat(" - ").concat(tagTextfield.getText()).concat(" - ").concat(personTextField.getText()).concat("_").concat(dateTextField.getText()).concat(file.getFullExtension()));

			try
			{
				int i = 1;
				FileObject clone = newFile.clone();
				while (clone.toFile().exists())
				{
					clone = newFile.clone();
					clone.setName(newFile.getName().concat("_"));
					clone.setName(clone.getName().concat(String.valueOf(i++)));
					System.out.println(clone);
				}

				if (clone.getFullPath().length() > 250)
					Main.openDynamic(new Text("Pfadlänge zu lang (max. 250 Zeichen): " + clone.getFullPath()));
				else
					FileUtils.moveFile(file.toFile(), clone.toFile());
			}
			catch (final IOException e)
			{
				final Text errorText = new Text();
				if (e instanceof FileExistsException)
					errorText.setText("Datei kann nicht verschoben werden, da die sie im Zielordner schon existiert!");
				else
					errorText.setText("Source or destination aren't valid or access rights to the file are restricted!");
				Main.openDynamic(errorText);
			}
			ContentManager.getInstance().nextFile();
		});

		this.textFieldsEmpty.bind(locationTextField.textProperty().isEqualTo("").or(personTextField.textProperty().isEqualTo("")));
		this.textFieldsEmpty.addListener(new ChangeListener<Boolean>()
		{
			@Override
			public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue)
			{
				removalButton.setDisable(newValue);
				continueButton.setDisable(newValue);
			}
		});

		final HBox buttons = new HBox();
		buttons.getChildren().addAll(this.continueButton, this.removalButton);
		buttons.setSpacing(10);
		buttons.setAlignment(Pos.CENTER);

		this.getChildren().addAll(locationBox, personsBox, dateBox, tagBox, buttons);
	}

	@Override
	public void changed(final ObservableValue<? extends FileObject> observable, final FileObject oldValue, final FileObject newValue)
	{
		/*
		 * Find out date of new file to prefill textfield
		 */
		GregorianCalendar cal = new GregorianCalendar();
		Date date = new Date();
		try
		{
			/*
			 * Find date from image metadata!
			 */
			date = MediaHandler.getDate(newValue);
		}
		catch (Exception e1)
		{
			/*
			 * Find date from image name!
			 */
			Pattern p = Pattern.compile("([0-9]{8})");
			Matcher m = p.matcher(newValue.getName());
			if (m.find())
			{
				try
				{
					date = dateConverter.parse(m.group(1));
				}
				catch (ParseException e)
				{
					date = new Date();
				}
			}
		}
		cal.setTime(date);
		dateTextField.setCalendar(cal);

		// Disable buttons if there is no content available
		if (newValue.getFullPath().isEmpty())
		{
			this.removalButton.setDisable(true);
			this.continueButton.setDisable(true);
		}
	}
}
