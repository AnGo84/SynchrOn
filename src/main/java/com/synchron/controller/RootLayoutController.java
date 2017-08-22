package com.synchron.controller;


import com.google.api.services.sheets.v4.Sheets;
import com.synchron.MainApp;
import com.synchron.export.ExportType;
import com.synchron.export.XMLIOHandler;
import com.synchron.fx.DialogText;
import com.synchron.fx.Dialogs;
import com.synchron.fx.GoogleDocExport;
import com.synchron.model.GoogleDoc;
import com.synchron.properties.PreferencesHandler;
import com.synchron.properties.PropertiesHandler;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.scene.control.Alert;
import javafx.stage.FileChooser;

import javax.xml.bind.JAXBException;
import java.io.File;
import java.io.IOException;


/**
 * Created by AnGo on 12.06.2017.
 */
public class RootLayoutController {

    private MainApp mainApp;

    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;
    }

    public void onMenuItemNew(ActionEvent actionEvent) {
        mainApp.getGoogleDocList().clear();
        PreferencesHandler.setPreferenceFilePath(null, mainApp.getClass(), PreferencesHandler.LAST_FILE_PATH);
        mainApp.setTableEdited(false);
        mainApp.setMainAppTitle(null);
    }

    public void onMenuItemExit(ActionEvent actionEvent) {
        if (mainApp.shutDown()) {
            Platform.exit();
        }
    }

    public void onMenuItemOpen(ActionEvent actionEvent) {

        File file = PreferencesHandler.getPreferenceFilePath(mainApp.getClass(), PreferencesHandler.LAST_FILE_PATH);
//        mainApp.getRootLogger().info("Open JSON file in '" + file.getParent() + "'");
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("XML files (*.xml)", "*.xml");
        file = Dialogs.openFileDialog(file, extFilter, mainApp.getPrimaryStage());
        mainApp.loadGoogleDocsFromFile(file);

    }

    private void handleSaveAs() {
        mainApp.handleSaveAs();
//        if (!mainApp.isEmptyDocList()) {
//            FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("XML files (*.xml)", "*.xml");
//            File file = Dialogs.saveFileDialog(PreferencesHandler.getPreferenceFilePath(mainApp.getClass(), PreferencesHandler.LAST_FILE_PATH), extFilter, mainApp.getPrimaryStage());
//            if (file != null) {
//                // Make sure it has the correct extension
//                if (!file.getPath().endsWith(".xml")) {
//                    file = new File(file.getPath() + ".xml");
//                }
//
//                if (PreferencesHandler.getPreferenceFilePath(mainApp.getClass(), PreferencesHandler.LAST_FILE_PATH) == null) {
//                    PreferencesHandler.setPreferenceFilePath(file, mainApp.getClass(), PreferencesHandler.LAST_FILE_PATH);
//                    mainApp.setMainAppTitle(file.getName());
//                }
//                mainApp.getRootLogger().info("Export to XML file '" + file.getParent() + "'");
//                try {
//                    XMLIOHandler.writeGoogleDocsToXMLFile(file, mainApp.getGoogleDocList());
//                    mainApp.setTableEdited(false);
//                    Dialogs.showMessage(Alert.AlertType.INFORMATION, new DialogText("Saving file", "File '" + file.getAbsolutePath() + "' saved successful", ""), mainApp.getRootLogger());
//                } catch (JAXBException e) {
//                    Dialogs.showErrorDialog(e, new DialogText("Saving error", "Error on saving file " + file.getAbsoluteFile(), ""), mainApp.getRootLogger());
////                    Dialogs.showMessage(Alert.AlertType.WARNING, new DialogText("Saving error", "Error on saving file " + file.getAbsoluteFile(), e.getMessage()), mainApp.getRootLogger());
//                }
//            } else {
//                Dialogs.showMessage(Alert.AlertType.WARNING, new DialogText("Warning!", "Cannot save file '" + file + "'", "The wrong path or file not exist"), null);
//            }
//        } else {
//            Dialogs.showMessage(Alert.AlertType.WARNING, new DialogText("Data error", "Nothing to save!", "Table is empty"), mainApp.getRootLogger());
//        }
    }

    public void onMenuItemSave(ActionEvent actionEvent) {
        mainApp.saveMainTable();
////        File file = PreferencesHandler.getPreferenceFilePath(mainApp.getClass(), PreferencesHandler.LAST_FILE_PATH);
//        File file = new File(PropertiesHandler.getPropertyString(mainApp.getProperties(), PropertiesHandler.APP_XML_FILE));
//        if (file != null) {
//            try {
//                XMLIOHandler.writeGoogleDocsToXMLFile(file, mainApp.getGoogleDocList());
//                mainApp.setTableEdited(false);
//                Dialogs.showMessage(Alert.AlertType.INFORMATION, new DialogText("Saving file", "File '" + file.getAbsolutePath() + "' saved successful", ""), mainApp.getRootLogger());
//            } catch (JAXBException e) {
//                Dialogs.showMessage(Alert.AlertType.WARNING, new DialogText("Saving error", "Error on saving file " + file.getAbsoluteFile(), e.getMessage()), mainApp.getRootLogger());
//            }
//        } else {
//            handleSaveAs();
//        }
    }

    public void onMenuItemSaveAs(ActionEvent actionEvent) {
        handleSaveAs();
    }

    public void onMenuItemAbout(ActionEvent actionEvent) {
        Dialogs.showMessage(Alert.AlertType.INFORMATION, new DialogText(mainApp.getAppName(), "About", "Author: AnGo \n"), null);
    }

    public void onMenuItemSettings(ActionEvent actionEvent) {
        mainApp.showSettingsView();
    }

    public void onMenuItemExportToCSV(ActionEvent actionEvent) {
        //GoogleDocExport.exportGoogleDocToFile(ExportType.CSV, mainApp.getCurrentGoogleDoc(), mainApp.getService(), mainApp.getPrimaryStage());
        exportToFile(ExportType.CSV);
    }

    public void onMenuItemExportToXLS(ActionEvent actionEvent) {
        //GoogleDocExport.exportGoogleDocToFile(ExportType.XLS, mainApp.getCurrentGoogleDoc(), mainApp.getService(), mainApp.getPrimaryStage());
        exportToFile(ExportType.XLS);
    }

    private void exportToFile(ExportType exportType) {
        if (mainApp.getCurrentGoogleDoc() != null && (mainApp.getCurrentGoogleDoc().getDocSheets() != null && !mainApp.getCurrentGoogleDoc().getDocSheets().isEmpty())) {
            String fileNameWithoutType = setExportFileName(mainApp.getCurrentGoogleDoc());
            if (!fileNameWithoutType.equals("")) {
                mainApp.getRootLogger().info("Export to " + exportType.getTypeName() + " file '" + fileNameWithoutType + "'");
                try {
                    Sheets service = mainApp.getService();
                    if (service != null) {
                        GoogleDocExport.exportGoogleDocToFile(service, exportType, fileNameWithoutType, mainApp.getCurrentGoogleDoc());
                        mainApp.setTableEdited(true);
                        Dialogs.showMessage(Alert.AlertType.INFORMATION, new DialogText("Export file", "File '" + fileNameWithoutType + "' exported to '" + exportType.getTypeName() + "' successful!", ""), MainApp.getRootLogger());
                    }
                } catch (IOException e) {
                    //Dialogs.showMessage(Alert.AlertType.WARNING, new DialogText("Export file", "Error on export file '" + fileName + "'  to '" + exportType.getTypeName() + "' format!", e.getMessage()), MainApp.getRootLogger());
                    Dialogs.showErrorDialog(e, new DialogText("Initialization error", "Cannot read Google API", "The error is:"), mainApp.getRootLogger());
                }
            }

        } else {
            Dialogs.showMessage(Alert.AlertType.WARNING, new DialogText("Data error", "Nothing to save!", "Table is empty"), MainApp.getRootLogger());
        }
    }

    private String setExportFileName(GoogleDoc googleDoc) {
        String fileNameWithoutType = "";

        String fileName = GoogleDocExport.getExportFileName(googleDoc);
        if (fileName.equals("")) {
            fileName = Dialogs.showTextInputDialog(new DialogText("Input file name", "File name is not settled!", "Enter your file name:"), GoogleDocExport.DEFAULT_EXPORT_FILE_NAME);
        }
        String directoryName = GoogleDocExport.getExportDirectory(googleDoc);
        if (directoryName.equals("")) {
            File file = Dialogs.directoryChooseDialog(null, mainApp.getPrimaryStage());
            if (file != null) {
                directoryName = file.getAbsolutePath();
            }
        }
        if (!fileName.equals("") && !directoryName.equals("")) {
            fileNameWithoutType = GoogleDocExport.fileNameWithoutType(directoryName, fileName);
        }
        return fileNameWithoutType;
    }


}
