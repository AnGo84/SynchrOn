package com.synchron.controller;


import com.google.api.services.sheets.v4.Sheets;
import com.synchron.MainApp;
import com.synchron.custom.FileUtils;
import com.synchron.export.*;
import com.synchron.fx.DialogText;
import com.synchron.fx.Dialogs;
import com.synchron.model.GoogleDoc;
import com.synchron.properties.PreferencesHandler;
import javafx.event.ActionEvent;
import javafx.scene.control.Alert;
import javafx.stage.FileChooser;

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
        if (mainApp.isConfirmNotSave()) {
            mainApp.handleSaveAs();
        }
        mainApp.getGoogleDocList().clear();
        PreferencesHandler.setPreferenceFilePath(null, mainApp.getClass(), PreferencesHandler.LAST_FILE_PATH);
        //mainApp.setTableEdited(false);
        mainApp.setTableFileName("New");
        mainApp.setHasChanged(false);
        //mainApp.setMainAppTitle(null);

    }

    public void onMenuItemExit(ActionEvent actionEvent) {
        if (mainApp.shutDown()) {
            mainApp.getSystemTray().onExitAction();
        }
    }

    public void onMenuItemClose(ActionEvent actionEvent) {
//        if (mainApp.shutDown()) {
//            Platform.exit();
//        }
        mainApp.getPrimaryStage().close();
    }

    public void onMenuItemOpen(ActionEvent actionEvent) {

        File file = PreferencesHandler.getPreferenceFilePath(mainApp.getClass(), PreferencesHandler.LAST_FILE_PATH);
//        mainApp.getRootLogger().info("Open JSON file in '" + file.getParent() + "'");
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("XML files (*.xml)", "*.xml");
        file = Dialogs.openFileDialog(file, extFilter, mainApp.getPrimaryStage());
        mainApp.loadGoogleDocsFromFile(file);

    }


    public void onMenuItemSave(ActionEvent actionEvent) {
        mainApp.saveMainTable();
    }

    public void onMenuItemSaveAs(ActionEvent actionEvent) {
        mainApp.handleSaveAs();
    }

    public void onMenuItemAbout(ActionEvent actionEvent) {
        Dialogs.showMessage(Alert.AlertType.INFORMATION, new DialogText(mainApp.getAppName(), "About", "Author: AnGo \n"), null);
    }

    public void onMenuItemSettings(ActionEvent actionEvent) {
        mainApp.showSettingsView();
    }

    public void onMenuItemExportToCSV(ActionEvent actionEvent) {
        exportToFile(ExportType.CSV, new CSVExportToFileImpl());
    }

    public void onMenuItemExportToXLS(ActionEvent actionEvent) {
        exportToFile(ExportType.XLS, new XLSExportToFileImpl());
    }

    public void onMenuItemExportToXLSX(ActionEvent actionEvent) {
        exportToFile(ExportType.XLSX, new XLSXExportToFileImpl());
    }

    public void onMenuItemExportToXML(ActionEvent actionEvent) {
        exportToFile(ExportType.XML, new XMLExportToFileImpl());
    }

    public void onMenuItemExportToJSON(ActionEvent actionEvent) {
        exportToFile(ExportType.JSON, new JSONExportToFileImpl());
    }

    private void exportToFile(ExportType exportType, ExportToFile exportToFile) {
        if (mainApp.getCurrentGoogleDoc() != null && (mainApp.getCurrentGoogleDoc().hasSheets())) {
            String fileNameWithoutType = setExportFileName(mainApp.getCurrentGoogleDoc());
            if (!fileNameWithoutType.equals("")) {
                mainApp.getRootLogger().info("Export to " + exportType.getTypeName() + " file '" + fileNameWithoutType + "'");
                try {
                    Sheets service = mainApp.getService();
                    if (service != null) {
                        ExportHandler.exportGoogleDocToFile(exportToFile, service, fileNameWithoutType, mainApp.getCurrentGoogleDoc());

                        //mainApp.setTableEdited(true);
                        mainApp.setHasChanged(true);

                        Dialogs.showMessage(Alert.AlertType.INFORMATION, new DialogText("Export file", "File '" + fileNameWithoutType + "' exported to '" + exportType.getTypeName() + "' successful!", ""), mainApp.getRootLogger());
                    }
                } catch (IOException e) {
                    //Dialogs.showMessage(Alert.AlertType.WARNING, new DialogText("Export file", "Error on export file '" + fileName + "'  to '" + exportType.getTypeName() + "' format!", e.getMessage()), MainApp.getRootLogger());
                    Dialogs.showErrorDialog(e, new DialogText("Initialization error", "Cannot read Google API", "The error details"), mainApp.getRootLogger());
                }
            }

        } else {
            Dialogs.showMessage(Alert.AlertType.WARNING, new DialogText("Data error", "Nothing to save", "Table is empty"), mainApp.getRootLogger());
        }
    }

    private String setExportFileName(GoogleDoc googleDoc) {
        String fileNameWithoutType = "";

        String fileName = ExportHandler.getExportFileName(googleDoc);
        if (fileName.equals("")) {
            fileName = Dialogs.showTextInputDialog(new DialogText("Input file name", "File name is not settled", "Enter your file name:"), ExportHandler.DEFAULT_EXPORT_FILE_NAME);
        }
        String directoryName = googleDoc.getExportDirectory();
        if (directoryName.equals("")) {
            File file = Dialogs.directoryChooseDialog(null, mainApp.getPrimaryStage());
            if (file != null) {
                directoryName = file.getAbsolutePath();
            }
        }
        if (!fileName.equals("") && !directoryName.equals("")) {
            fileNameWithoutType = FileUtils.fileNameWithDir(directoryName, fileName);
        }
        return fileNameWithoutType;
    }


}
