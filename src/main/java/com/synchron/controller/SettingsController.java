package com.synchron.controller;

import com.synchron.MainApp;
import com.synchron.fx.DialogText;
import com.synchron.fx.Dialogs;
import com.synchron.fx.ImageResources;
import com.synchron.fx.TextFieldNumberListener;
import com.synchron.google.GoogleAPIProject;
import com.synchron.google.GoogleSheetIOHandler;
import com.synchron.properties.PropertiesHandler;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Properties;

/**
 * Created by AnGo on 13.06.2017.
 */
public class SettingsController {
    private static final String GOOGLE_APP_PROJECT_NAME = "Synchron";

    @FXML
    private Button buttonOk;
    @FXML
    private Button buttonCancel;
    @FXML
    private Button buttonTest;
    @FXML
    private Button buttonChooseJson;
    @FXML
    private Button buttonChooseXML;


    @FXML
    private TextField textFUserName;
    @FXML
    private TextField textFProjectName;
    @FXML
    private TextField textFJsonFile;
    @FXML
    private TextField textFXMLFile;
    @FXML
    private TextField textFDelayAfterStart;
    @FXML
    private TextField textFRepeatPeriod;

    private MainApp mainApp;

    private Stage dialogStage;


    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;
        initTextFieldsFromProperties(mainApp.getProperties());
    }

    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    @FXML
    private void initialize() {
        initButtonsIcons();
        initListeners();
//        initTextFieldFromPreferences();
    }


    private void initListeners() {
        textFDelayAfterStart.textProperty().addListener(new TextFieldNumberListener(textFDelayAfterStart));
        textFRepeatPeriod.textProperty().addListener(new TextFieldNumberListener(textFRepeatPeriod));
    }

    private void initButtonsIcons() {
        buttonOk.setGraphic(new ImageView(ImageResources.getButtonOk()));
        buttonCancel.setGraphic(new ImageView(ImageResources.getButtonCancel()));
        buttonChooseJson.setGraphic(new ImageView(ImageResources.getButtonFolder()));
        buttonChooseXML.setGraphic(new ImageView(ImageResources.getButtonFolder()));

        buttonTest.setGraphic(new ImageView(ImageResources.getButtonTest()));
    }

    private void initTextFieldFromPreferences() {
//        textFUserName.setText(PreferencesHandler.getPreferenceString(MainApp.class, PreferencesHandler.USER_NAME));
//        textFProjectName.setText(PreferencesHandler.getPreferenceString(MainApp.class, PreferencesHandler.PROJECT_NAME));
//        textFJsonFile.setText(PreferencesHandler.getPreferenceString(MainApp.class, PreferencesHandler.JSON_FILE_PATH));
//        textFXMLFile.setText(PreferencesHandler.getPreferenceString(MainApp.class, PreferencesHandler.XML_FILE_PATH));
    }

    private void initTextFieldsFromProperties(Properties properties) {
        if (properties != null) {
            //Properties newProperties = new Properties(properties);

            textFUserName.setText(properties.getProperty(PropertiesHandler.USER_NAME, ""));
            textFProjectName.setText(properties.getProperty(PropertiesHandler.GOOGLE_PROJECT_NAME, GOOGLE_APP_PROJECT_NAME));
            textFJsonFile.setText(properties.getProperty(PropertiesHandler.GOOGLE_PROJECT_JSON, ""));
            textFXMLFile.setText(properties.getProperty(PropertiesHandler.APP_XML_FILE, ""));

            textFDelayAfterStart.setText(properties.getProperty(PropertiesHandler.DELAY_AFTER_START, String.valueOf(PropertiesHandler.DEFAULT_DELAY_AFTER_START)));
            textFRepeatPeriod.setText(properties.getProperty(PropertiesHandler.REPEAT_PERIOD, String.valueOf(PropertiesHandler.DEFAULT_REPEAT_PERIOD)));
        }
    }

    private Properties getPropertyFromField() {
        Properties properties = new Properties();
        properties.setProperty(PropertiesHandler.USER_NAME, textFUserName.getText());
        properties.setProperty(PropertiesHandler.GOOGLE_PROJECT_NAME, textFProjectName.getText());
        properties.setProperty(PropertiesHandler.GOOGLE_PROJECT_JSON, textFJsonFile.getText());
        properties.setProperty(PropertiesHandler.APP_XML_FILE, textFXMLFile.getText());
        properties.setProperty(PropertiesHandler.DELAY_AFTER_START, textFDelayAfterStart.getText());
        properties.setProperty(PropertiesHandler.REPEAT_PERIOD, textFRepeatPeriod.getText());
        return properties;
    }


    public void onButtonChooseJson(ActionEvent actionEvent) {
        File file = getFileFromTextField(textFJsonFile);
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("JSON files (*.json)", "*.json");
        file = Dialogs.openFileDialog(file, extFilter, dialogStage);
        if (file != null) {
            mainApp.getRootLogger().info("Open JSON file '" + file + "'");
            textFJsonFile.setText(file.getPath());
        }
    }

    public void onButtonTest(ActionEvent actionEvent) {
        GoogleAPIProject googleAPIProject =
                getGoogleAPIProject();
        try {
            if (GoogleSheetIOHandler.getSheetsService(googleAPIProject) != null) {
                //mainApp.setService(GoogleSheetIOHandler.getSheetsService(googleAPIProject));
                Dialogs.showMessage(Alert.AlertType.INFORMATION, new DialogText("Connection Test", "The connection is successful!", "Connection to: " + googleAPIProject.toString()), mainApp.getRootLogger());
            } else {
                Dialogs.showMessage(Alert.AlertType.WARNING, new DialogText("Connection Test", "The connection is NULL!", "Connection to: " + googleAPIProject.toString()), mainApp.getRootLogger());
            }
        } catch (IOException e) {
            Dialogs.showErrorDialog(e, new DialogText("Test error", "Cannot connect to Google DocSheet '" + googleAPIProject.toString() + "'", "The error is:"), mainApp.getRootLogger());
        }
    }

    private GoogleAPIProject getGoogleAPIProject() {
        GoogleAPIProject googleAPIProject = new GoogleAPIProject();
        googleAPIProject.setUserName(textFUserName.getText());
        googleAPIProject.setProjectName(textFProjectName.getText());
        googleAPIProject.setPathToJson(textFJsonFile.getText());
        return googleAPIProject;
    }

    public void onButtonChooseXML(ActionEvent actionEvent) {
        File file = getFileFromTextField(textFXMLFile);
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("XML files (*.xml)", "*.xml");
        file = Dialogs.openFileDialog(file, extFilter, dialogStage);
        if (file != null) {
            mainApp.getRootLogger().info("Open XML file '" + file + "'");
            textFXMLFile.setText(file.getPath());
        }

    }

    private File getFileFromTextField(TextField textField) {
        File file;
        if (textField.getText() != null && !textField.getText().equals("")) {
            file = new File(textField.getText());
        } else {
//            file = mainApp.getLastFilePath();
            file = null;
        }
        return file;
    }

    public void onButtonCancel(ActionEvent actionEvent) {
        dialogStage.close();
    }

    public void onButtonOk(ActionEvent actionEvent) {
//        PreferencesHandler.setPreferenceString(textFUserName.getText(), MainApp.class, PreferencesHandler.USER_NAME);
//        PreferencesHandler.setPreferenceString(textFProjectName.getText(), MainApp.class, PreferencesHandler.PROJECT_NAME);
//        PreferencesHandler.setPreferenceString(textFJsonFile.getText(), MainApp.class, PreferencesHandler.JSON_FILE_PATH);
//        PreferencesHandler.setPreferenceString(textFXMLFile.getText(), MainApp.class, PreferencesHandler.XML_FILE_PATH);
//        dialogStage.close();
        if (isInputValid()) {
            try (OutputStream output = new FileOutputStream(mainApp.getPropertiesFile())) {
                Properties properties = getPropertyFromField();

                mainApp.getRootLogger().info("Save properties file " + mainApp.getPropertiesFile());

                // save properties to project root folder
                properties.store(output, null);
                mainApp.setProperties(properties);
                mainApp.getRootLogger().info("\n" + PropertiesHandler.toString(properties));
                mainApp.getRootLogger().info("Save properties file " + mainApp.getPropertiesFile());
                dialogStage.close();
            } catch (IOException e) {
                Dialogs.showErrorDialog(e, new DialogText("Saving error", "Cannot save config file '" + mainApp.getPropertiesFile() + "'", "The error is:"), mainApp.getRootLogger());
            }
        }
    }

    private boolean isInputValid() {
        String errorMessage = "";
        if (textFDelayAfterStart.getText() == null || textFDelayAfterStart.getText().length() == 0 || Integer.parseInt(textFDelayAfterStart.getText()) == 0) {
            errorMessage += "No valid field 'Delay after start'. Can't be empty or 0!\n";
        }
        if (textFRepeatPeriod.getText() == null || textFRepeatPeriod.getText().length() == 0 || Integer.parseInt(textFRepeatPeriod.getText()) == 0) {
            errorMessage += "No valid field 'Repeat period'. Can't be empty or 0!\n";
        }

        if (errorMessage.length() == 0) {
            return true;
        } else {
            Dialogs.showMessage(Alert.AlertType.ERROR, new DialogText("Invalid Fields", "Please correct invalid fields", errorMessage), null);
            //alert.initOwner(dialogStage);
            return false;
        }
    }
}
