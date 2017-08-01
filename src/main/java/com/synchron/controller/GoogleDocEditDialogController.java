package com.synchron.controller;

import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.model.Sheet;
import com.synchron.MainApp;
import com.synchron.custom.DateUtil;
import com.synchron.export.ExportType;
import com.synchron.fx.DialogText;
import com.synchron.fx.Dialogs;
import com.synchron.fx.ImageResources;
import com.synchron.google.GoogleSheetIOHandler;
import com.synchron.model.DocSheet;
import com.synchron.model.GoogleDoc;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import javafx.util.Callback;
import javafx.util.converter.IntegerStringConverter;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Created by AnGo on 08.06.2017.
 */
public class GoogleDocEditDialogController {
    @FXML
    private TableView<DocSheet> tableDocSheet;

    @FXML
    private TableColumn<DocSheet, Integer> columnSheetId;
    @FXML
    private TableColumn<DocSheet, Integer> columnIndex;
    @FXML
    private TableColumn<DocSheet, String> columnTitle;
    //    @FXML
//    private TableColumn<DocSheet, String> columnType;
    @FXML
    private TableColumn<DocSheet, String> columnUserName;
    @FXML
    private TableColumn<DocSheet, Integer> columnFrozenRow;
    @FXML
    private TableColumn<DocSheet, Boolean> columnExport;

    @FXML
    private Button buttonOk;
    @FXML
    private Button buttonCancel;
    @FXML
    private Button buttonChoose;
    @FXML
    private Button buttonSync;

    @FXML
    private ComboBox<ExportType> comboBoxExportType;
    @FXML
    private CheckBox checkBoxStatus;

    @FXML
    private TextField textFName;
    @FXML
    private TextField textFID;
    @FXML
    private TextField textFExportDir;
    @FXML
    private TextField textFPeriod;
    @FXML
    private TextField textFExportNext;

//    @FXML
//    private TextField textFSyncDate;

    private MainApp mainApp;
    private Stage dialogStage;

    private GoogleDoc googleDoc;
    private boolean okClicked = false;

    private ObservableList<DocSheet> docSheetObservableList;

    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;
    }

    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    public GoogleDoc getGoogleDoc() {
        return googleDoc;
    }

    public void setGoogleDoc(GoogleDoc googleDoc) {
        this.googleDoc = googleDoc;

        textFName.setText(googleDoc.getName());
        textFID.setText(googleDoc.getDocId());
        textFExportDir.setText(googleDoc.getExportDir());
        if (googleDoc != null) {
            if (googleDoc.getStatus() != null) {
                checkBoxStatus.setSelected(googleDoc.getStatus().equals("On"));
            }
            if (googleDoc.getExportType() != null) {
                comboBoxExportType.setValue(ExportType.valueOf(googleDoc.getExportType()));
            }
        }
        textFPeriod.setText(String.valueOf(googleDoc.getPeriod()));
        textFExportNext.setText(DateUtil.format(googleDoc.getNextSyncDate(), ""));


        docSheetObservableList = FXCollections.observableArrayList(googleDoc.getDocSheets());
        tableDocSheet.setItems(docSheetObservableList);
    }

    @FXML
    private void initialize() {

        initButtonsIcons();
        initTableDocSheetColumns();
        initFactories();

        comboBoxExportType.getItems().setAll(ExportType.values());

    }

    private void initButtonsIcons() {
        buttonOk.setGraphic(new ImageView(ImageResources.getButtonOk()));
        buttonCancel.setGraphic(new ImageView(ImageResources.getButtonCancel()));
        buttonChoose.setGraphic(new ImageView(ImageResources.getButtonFolder()));

        buttonSync.setGraphic(new ImageView(ImageResources.getButtonSync()));
    }

    private void initTableDocSheetColumns() {
        columnSheetId.setCellValueFactory(cellData -> cellData.getValue().sheetIdProperty().asObject());
        columnIndex.setCellValueFactory(cellData -> cellData.getValue().indexProperty().asObject());
        columnTitle.setCellValueFactory(cellData -> cellData.getValue().titleProperty());
        columnUserName.setCellValueFactory(cellData -> cellData.getValue().userNameProperty());
//        columnType.setCellValueFactory(cellData -> cellData.getValue().typeProperty());
        columnFrozenRow.setCellValueFactory(cellData -> cellData.getValue().frozenRowProperty().asObject());
        columnExport.setCellValueFactory(cellData -> cellData.getValue().exportProperty());
    }

    private void initFactories() {
        columnUserName.setCellFactory(TextFieldTableCell.forTableColumn());
        columnUserName.setOnEditCommit(
                new EventHandler<TableColumn.CellEditEvent<DocSheet, String>>() {
                    @Override
                    public void handle(TableColumn.CellEditEvent<DocSheet, String> t) {
                        ((DocSheet) t.getTableView().getItems().get(
                                t.getTablePosition().getRow())
                        ).setUserName(t.getNewValue());
                    }
                }
        );

        columnFrozenRow.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));
        columnFrozenRow.setOnEditCommit(
                new EventHandler<TableColumn.CellEditEvent<DocSheet, Integer>>() {
                    @Override
                    public void handle(TableColumn.CellEditEvent<DocSheet, Integer> t) {
                        ((DocSheet) t.getTableView().getItems().get(
                                t.getTablePosition().getRow())
                        ).setFrozenRow(t.getNewValue());
                    }
                }
        );

        columnExport.setCellFactory(new Callback<TableColumn<DocSheet, Boolean>, //
                TableCell<DocSheet, Boolean>>() {
            @Override
            public TableCell<DocSheet, Boolean> call(TableColumn<DocSheet, Boolean> p) {
                CheckBoxTableCell<DocSheet, Boolean> cell = new CheckBoxTableCell<>();
                cell.setAlignment(Pos.CENTER);
                return cell;
            }
        });

        textFPeriod.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null && !newValue.equals("")) {
                if (!newValue.matches("\\d*")) {
                    Toolkit.getDefaultToolkit().beep();
                    textFPeriod.setText(newValue.replaceAll("[^\\d]", ""));
                }
                int minutes = 0;
                if (textFPeriod.getText().length() > 0) {
                    minutes = Integer.parseInt(textFPeriod.getText());
                }
                textFExportNext.setText(DateUtil.format(LocalDateTime.now().plusMinutes(minutes), ""));
                System.out.println("Next: " + DateUtil.format(LocalDateTime.now().plusMinutes(minutes)));
            }
        });

        checkBoxStatus.selectedProperty().addListener(new ChangeListener<Boolean>() {
            public void changed(ObservableValue<? extends Boolean> ov,
                                Boolean old_val, Boolean new_val) {
                checkBoxStatus.setText(new_val ? "On" : "Off");

            }
        });

        textFExportNext.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null && !newValue.equals("")) {
                if (!newValue.matches("(0[1-9]|1[012])[/](0[1-9]|[12][0-9]|3[01])[/](19|20)\\d\\d ([0-9]|1[0-1]):[0-5][0-9](:[0-5][0-9])? ?[APap][mM]$")) {
                    Toolkit.getDefaultToolkit().beep();
                    textFExportNext.setText(oldValue);
                }
            }
        });
    }

    public void onButtonChoose(ActionEvent actionEvent) {
        File file = Dialogs.directoryChooseDialog(null, dialogStage);
        if (file != null) {
            textFExportDir.setText(file.getAbsolutePath());
        }
    }

    public boolean isOkClicked() {
        return okClicked;
    }


    /**
     * Проверяет пользовательский ввод в текстовых полях.
     *
     * @return true, если пользовательский ввод корректен
     */
    private boolean isInputValid() {
        String errorMessage = "";
        if (textFName.getText() == null || textFName.getText().length() == 0) {
            errorMessage += "No valid Name!\n";
        }
        if (textFID.getText() == null || textFID.getText().length() == 0) {
            errorMessage += "No valid ID!\n";
        }

        if (checkBoxStatus.isSelected()) {
            if (textFExportDir.getText() == null || textFExportDir.getText().length() == 0) {
                errorMessage += "No valid Export directory!\n";
            }
            if (comboBoxExportType.getValue() == null) {
                errorMessage += "No valid Export type!\n";
            }
            if (textFPeriod.getText() == null || textFPeriod.getText().length() == 0) {
                errorMessage += "No valid Export period!\n";
            } else if (textFPeriod.getText().equals("0")) {
                errorMessage += "Export period cannot be 0!\n";
            }

            if (textFExportNext.getText() == null || textFExportNext.getText().length() == 0) {
                errorMessage += "No valid Next Export date!\n";
            }
        }

        if (errorMessage.length() == 0) {
            return true;
        } else {
            Dialogs.showMessage(Alert.AlertType.ERROR, new DialogText("Invalid Fields", "Please correct invalid fields", errorMessage), null);
            //alert.initOwner(dialogStage);
            return false;
        }
    }

    public void onButtonCancel(ActionEvent actionEvent) {
        dialogStage.close();
    }

    public void onButtonOk(ActionEvent actionEvent) {
        if (isInputValid()) {
            getGoogleDocFromFields();

            okClicked = true;
            dialogStage.close();
        }
    }

    private void getGoogleDocFromFields() {
//        GoogleDoc googleDoc = new GoogleDoc();
        googleDoc.setName(textFName.getText());
        googleDoc.setDocId(textFID.getText());
        googleDoc.setExportDir(textFExportDir.getText());

        googleDoc.setStatus(checkBoxStatus.getText());
        if (comboBoxExportType.getValue() != null) {
            googleDoc.setExportType(comboBoxExportType.getValue().name());
        }
        int minutes = 0;
        if (textFPeriod != null && textFPeriod.getText().length() > 0) {
            minutes = Integer.parseInt(textFPeriod.getText());
        }

        googleDoc.setPeriod(minutes);
//        googleDoc.setNextSyncDate(LocalDateTime.now().plusMinutes(minutes));
        googleDoc.setNextSyncDate(DateUtil.parse(textFExportNext.getText()));
        //person.setBirthday(DateUtil.parse(birthdayField.getText()));
        googleDoc.setDocSheets(docSheetObservableList);
//        return googleDoc;
    }

    public void onButtonSync(ActionEvent actionEvent) {
//        if (isInputValid()) {
//            getGoogleDocFromFields();
        if (textFID.getText() != null && textFID.getText().length() > 0) {
            buttonSync.getParent().setDisable(true);
            try {
//                Dialogs.showMessage(Alert.AlertType.INFORMATION, new DialogText("", "", googleDoc.toString()), null);
                readDocsSheets(mainApp.getService());
            } catch (IOException | RuntimeException e) {
                Dialogs.showErrorDialog(e, new DialogText("Initialization error", "Cannot read Google API", "The error is:"), mainApp.getRootLogger());
            } finally {
                buttonSync.getParent().setDisable(false);
            }

        } else {
            Dialogs.showMessage(Alert.AlertType.WARNING, new DialogText("Wrong date", "Goggle Doc ID did not set", "Please set correct Google Doc ID."), null);
        }

    }

    public void readDocsSheets(Sheets service) throws IOException {
        if (service == null) {
            throw new IllegalArgumentException("Sheets cannot be NULL!!!");
        }

        List<Sheet> sheetList = GoogleSheetIOHandler.getSpreadSheetsList(service, textFID.getText());

//        Dialogs.showErrorDialog(e, new DialogText("Initialization error", "Cannot read Google API", "The error is:"), mainApp.getRootLogger());
        docSheetObservableList.clear();
        for (Sheet sheet : sheetList) {
            DocSheet docSheet = new DocSheet();
            docSheet.setSheetId(sheet.getProperties().getSheetId());
            docSheet.setIndex(sheet.getProperties().getIndex());
            docSheet.setTitle(sheet.getProperties().getTitle());
            docSheet.setUserName(sheet.getProperties().getTitle());
//                docSheet.setType(sheet.getProperties().getSheetType());
            docSheet.setFrozenRow(sheet.getProperties().getGridProperties().getFrozenRowCount());
            docSheet.setExport(true);
            docSheetObservableList.add(docSheet);
        }
//        googleDoc.setDocSheets(docSheetObservableList);
        tableDocSheet.setItems(docSheetObservableList);
    }
}
