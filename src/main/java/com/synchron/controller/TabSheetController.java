package com.synchron.controller;

import com.synchron.MainApp;
import com.synchron.export.*;
import com.synchron.fx.DialogText;
import com.synchron.fx.Dialogs;
import com.synchron.fx.ImageResources;
import com.synchron.google.GoogleSheetIOHandler;
import com.synchron.model.DocSheet;
import com.synchron.model.GoogleDoc;
import com.synchron.properties.PreferencesHandler;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Cursor;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * Created by AnGo on 17.06.2017.
 */
public class TabSheetController {
    @FXML
    private TableView<String[]> tableTabSheet;

    @FXML
    private Button buttonFirst;
    @FXML
    private Button buttonPrior;
    @FXML
    private Button buttonNext;
    @FXML
    private Button buttonLast;
    @FXML
    private Button buttonRefresh;


    private MainApp mainApp;
    private Stage dialogStage;


    private GoogleDoc googleDoc = new GoogleDoc();
    private DocSheet docSheet = null;
    private List<String[]> stringList;

    public void setGoogleDoc(GoogleDoc googleDoc, DocSheet docSheet) {
//        this.googleDoc = googleDoc;
        this.googleDoc.setName(googleDoc.getName());
        this.googleDoc.setDocId(googleDoc.getDocId());
        this.googleDoc.setExportDir(googleDoc.getExportDir());

        this.docSheet = docSheet;
    }

    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;
    }

    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    public void setStringList(List<String[]> stringList) {
        this.stringList = stringList;
    }

    @FXML
    private void initialize() {
        initButtonsIcons();
        initButtonsToolTip();
    }

    private void initButtonsToolTip() {
        buttonFirst.setTooltip(new Tooltip("Go to first record"));
        buttonPrior.setTooltip(new Tooltip("Go to prior record"));
        buttonNext.setTooltip(new Tooltip("Go to next record"));
        buttonLast.setTooltip(new Tooltip("Go to last record"));
        buttonRefresh.setTooltip(new Tooltip("Refresh table data"));
    }

    private void initButtonsIcons() {
        buttonFirst.setGraphic(new ImageView(ImageResources.getButtonFirst()));
        buttonPrior.setGraphic(new ImageView(ImageResources.getButtonPrior()));
        buttonNext.setGraphic(new ImageView(ImageResources.getButtonNext()));
        buttonLast.setGraphic(new ImageView(ImageResources.getButtonLast()));
        buttonRefresh.setGraphic(new ImageView(ImageResources.getButtonRefresh()));
    }

    private int getMaxListLength(List<String[]> list) {
        int max = 0;
        for (String[] strings : list) {
            if (max < strings.length) {
                max = strings.length;
            }
        }
        return max;
    }

    public void fillTable() {
        tableTabSheet.getColumns().removeAll(tableTabSheet.getColumns());
        stringList = getSheetDetails();
        if (stringList != null && stringList.size() > 0) {
            for (int i = 0; i < getMaxListLength(stringList); i++) {
                // Create the table and columns
                final int colNo = i;
                TableColumn<String[], String> tableColumn = new TableColumn();
                tableColumn.setText("Field " + (colNo + 1));


                // Add Cell value factories
//                tableColumn.setCellValueFactory((p) -> {
//                    String[] x = p.getValue();
//                    return new SimpleStringProperty(x != null && x.length > 0 ? x[0] : "<no name>");
//                });
                tableColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<String[], String>, ObservableValue<String>>() {
                    @Override
                    public ObservableValue<String> call(TableColumn.CellDataFeatures<String[], String> p) {
                        String[] x = p.getValue();
                        if (x != null && x.length > 0 && x.length > colNo) {
                            return new SimpleStringProperty(x[colNo]);
                        } else {
                            return new SimpleStringProperty("");
                        }
                    }
                });

                tableTabSheet.getColumns().add(tableColumn);
            }
            // Add Data
            tableTabSheet.getItems().addAll(stringList);
        }
    }

    public void onButtonFirst(ActionEvent actionEvent) {
        tableTabSheet.getSelectionModel().selectFirst();
        tableTabSheet.scrollTo(tableTabSheet.getSelectionModel().getSelectedIndex());
    }

    public void onButtonPrior(ActionEvent actionEvent) {
        tableTabSheet.getSelectionModel().selectPrevious();
        tableTabSheet.scrollTo(tableTabSheet.getSelectionModel().getSelectedIndex());
    }

    public void onButtonNext(ActionEvent actionEvent) {
        tableTabSheet.getSelectionModel().selectNext();
        tableTabSheet.scrollTo(tableTabSheet.getSelectionModel().getSelectedIndex());
    }

    public void onButtonLast(ActionEvent actionEvent) {
        tableTabSheet.getSelectionModel().selectLast();
        tableTabSheet.scrollTo(tableTabSheet.getSelectionModel().getSelectedIndex());
    }


    public void onMenuItemExportToCSV(ActionEvent actionEvent) {
        exportToFile(new CSVExportToFileImpl());
    }

    public void onMenuItemExportToXLS(ActionEvent actionEvent) {
        exportToFile(new XLSExportToFileImpl());
    }

    public void onMenuItemExportToXLSX(ActionEvent actionEvent) {
        exportToFile(new XLSXExportToFileImpl());
    }

    public void onMenuItemExportToXML(ActionEvent actionEvent) {exportToFile(new XMLExportToFileImpl());
    }
    public void onMenuItemExportToJSON(ActionEvent actionEvent) {exportToFile(new JSONExportToFileImpl());
    }
    private void exportToFile(ExportToFile exportToFile) {
        if (googleDoc != null && docSheet != null) {
            FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter(exportToFile.getExportFileType().toUpperCase() + " files (*." + exportToFile.getExportFileType().toLowerCase() + ")", "*." + exportToFile.getExportFileType().toLowerCase());
            File file = Dialogs.saveFileDialog(PreferencesHandler.getPreferenceFilePath(mainApp.getClass(), PreferencesHandler.LAST_FILE_PATH), extFilter, dialogStage);
            if (file != null) {
                mainApp.getRootLogger().info("Export to " + exportToFile.getExportFileType() + " file '" + file.getPath() + "'");
                try {
                    //ExportHandler.saveExportDataToFile(exportToFile, file, new ExportDataList(docSheet.getExportSheetName(), stringList));
                    exportToFile.exportToFile(file, new ExportDataList(docSheet.getExportSheetName(), stringList));
                    Dialogs.showMessage(Alert.AlertType.INFORMATION, new DialogText("Export table", "Table exported to '" + exportToFile.getExportFileType() + "' successful", ""), MainApp.getRootLogger());
                } catch (IOException e) {
                    Dialogs.showMessage(Alert.AlertType.WARNING, new DialogText("Export file", "Error on export table to file'" + exportToFile.getExportFileType() + "'", e.getMessage()), MainApp.getRootLogger());
                }
            }
        } else {
            Dialogs.showMessage(Alert.AlertType.WARNING, new DialogText("Data error", "Nothing to save!", "Table is empty"), mainApp.getRootLogger());
        }
    }

    public void onButtonRefresh(ActionEvent actionEvent) {
        tableTabSheet.refresh();
    }


    private List<String[]> getSheetDetails() {
//        System.out.println("  Task ");
//        ExecutorService executor = Executors.newFixedThreadPool(1);
//        Task task = new Task() {
//            @Override
//            protected List<String[]> call() throws Exception {
//                System.out.println("  Task Start");
//                List<String[]> stringsValues = null ;
//                dialogStage.getScene().setCursor(Cursor.WAIT); //Change cursor to wait style
//                try {
////                    primaryStage.getScene().setCursor(Cursor.WAIT);
//                    stringsValues = GoogleSheetIOHandler.getStringsValuesList(mainApp.getService(), googleDoc.getDocId(), docSheet.getTitle());
////                    primaryStage.getScene().setCursor(Cursor.DEFAULT);
//                } catch (IOException e) {
//                    Dialogs.showMessage(Alert.AlertType.WARNING, new DialogText("Opening error", "Error on read data from Tab Sheet '" + docSheet.getTitle() + " to folder '" + googleDoc.getExportDir() + "'", e.getMessage()), mainApp.getRootLogger());
//                }
//                dialogStage.getScene().setCursor(Cursor.DEFAULT); //Change cursor to default style
//                System.out.println("  Task Finish");
//                return stringsValues;
//            }
//        };
//        Thread th = new Thread(task);
//        th.setDaemon(true);
//        th.start();
//        System.out.println("  Thread ");
//
//        Future<List<String[]>> future = (Future<List<String[]>>) executor.submit(task);
//        System.out.println("  Get Result ");
//        List<String[]> stringsValues = future.get() ;
//        System.out.println("  Result ok! ");

        if (googleDoc == null || docSheet == null) {
            return null;
        }
        List<String[]> stringsValues = null;
        try {
            dialogStage.getScene().setCursor(Cursor.WAIT);
            stringsValues = GoogleSheetIOHandler.getStringsValuesList(mainApp.getService(), googleDoc.getDocId(), docSheet.getTitle());
            dialogStage.getScene().setCursor(Cursor.DEFAULT);
        } catch (IOException e) {
            Dialogs.showMessage(Alert.AlertType.WARNING, new DialogText("Opening error", "Error on read data from Tab Sheet '" + docSheet.getTitle() + " to folder '" + googleDoc.getExportDir() + "'", e.getMessage()), mainApp.getRootLogger());
        }
        return stringsValues;
    }

}
