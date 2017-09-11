package com.synchron.controller;

import com.synchron.MainApp;
import com.synchron.custom.DateUtil;
import com.synchron.custom.FileUtils;
import com.synchron.export.ExportHandler;
import com.synchron.export.ExportResult;
import com.synchron.fx.DialogText;
import com.synchron.fx.Dialogs;
import com.synchron.fx.ImageResources;
import com.synchron.model.DocSheet;
import com.synchron.model.GoogleDoc;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.util.Callback;

import java.awt.*;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by AnGo on 08.06.2017.
 */
public class GoogleDocController {
    @FXML
    private TabPane tabPaneMain;

    @FXML
    private TableView<GoogleDoc> tableGoogleDoc;
    @FXML
    private TableView<DocSheet> tableDocSheet;

    @FXML
    private TableColumn<GoogleDoc, String> columnName;
    @FXML
    private TableColumn<GoogleDoc, String> columnID;
    @FXML
    private TableColumn<GoogleDoc, String> columnExportDir;
    @FXML
    private TableColumn<GoogleDoc, String> columnStatus;
    @FXML
    private TableColumn<GoogleDoc, String> columnExportType;
    @FXML
    private TableColumn<GoogleDoc, Integer> columnExportPeriod;

    @FXML
    private TableColumn<GoogleDoc, LocalDateTime> columnNextSyncDate;
    @FXML
    private TableColumn<GoogleDoc, LocalDateTime> columnLastSyncDate;
    @FXML
    private TableColumn<GoogleDoc, String> columnExportResult;
//    private TableColumn<GoogleDoc, String> columnLastSyncDate;

    @FXML
    private TableColumn<DocSheet, Integer> columnSheetId;
    @FXML
    private TableColumn<DocSheet, Integer> columnIndex;
    @FXML
    private TableColumn<DocSheet, String> columnTitle;
    @FXML
    private TableColumn<DocSheet, String> columnUserName;
    //    @FXML
//    private TableColumn<DocSheet, String> columnType;
    @FXML
    private TableColumn<DocSheet, Integer> columnFrozenRow;
    @FXML
    private TableColumn<DocSheet, Boolean> columnExport;


    @FXML
    private TextField textFName;
    @FXML
    private TextField textFID;
    @FXML
    private TextField textFExportDir;
    @FXML
    private TextField textFStatus;
    @FXML
    private TextField textFExportType;
    @FXML
    private TextField textFPeriod;
    @FXML
    private TextField textFLastSyncDate;
    @FXML
    private TextField textFNextSyncDate;
    @FXML
    private TextField textFExportResult;

    @FXML
    private TextField textFFilter;

    @FXML
    private Button buttonNew;
    @FXML
    private Button buttonEdit;
    @FXML
    private Button buttonDelete;
    @FXML
    private Button buttonViewTab;
    @FXML
    private Button buttonExport;
    @FXML
    private Button buttonSync;
    @FXML
    private Button buttonDirectory;
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

    private ObservableList<DocSheet> docSheetObservableList = FXCollections.observableArrayList();

    public GoogleDocController() {
    }

    public GoogleDocController(MainApp mainApp) {
        this.mainApp = mainApp;
    }

    @FXML
    private void initialize() {
        initSpreadSheetTableColumns();
        initButtonsIcons();

        // Очистка дополнительной информации об адресате.
        showGoogleDocDetails(null);

        // Слушаем изменения выбора, и при изменении отображаем
        // дополнительную информацию об адресате.
        tableGoogleDoc.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> showGoogleDocDetails(newValue));


        // Custom rendering of the table cell.
        columnNextSyncDate.setCellFactory(getTableCellDateCallback());
        columnLastSyncDate.setCellFactory(getTableCellDateCallback());


        tableGoogleDoc.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if (event.isPrimaryButtonDown() && event.getClickCount() == 2) {
                    tabPaneMain.getSelectionModel().select(1);
                }
            }
        });

        initButtonsToolTip();
    }

    private Callback<TableColumn<GoogleDoc, LocalDateTime>, TableCell<GoogleDoc, LocalDateTime>> getTableCellDateCallback() {
        return column -> {
            return new TableCell<GoogleDoc, LocalDateTime>() {
                @Override
                protected void updateItem(LocalDateTime item, boolean empty) {
                    super.updateItem(item, empty);
                    if (item == null || empty) {
                        setText(null);
                        setStyle("");
                    } else {
                        // Format date.
                        setText(DateUtil.getDateFormatter().format(item));
                    }
                }
            };
        };
    }

    private void initButtonsToolTip() {
        buttonNew.setTooltip(new Tooltip("Add new record"));
        buttonEdit.setTooltip(new Tooltip("Edit current record"));
        buttonDelete.setTooltip(new Tooltip("Delete current record"));
        buttonViewTab.setTooltip(new Tooltip("View Tab's details of current record"));
        buttonDirectory.setTooltip(new Tooltip("Open directory"));

        buttonFirst.setTooltip(new Tooltip("Go to first record"));
        buttonPrior.setTooltip(new Tooltip("Go to prior record"));
        buttonNext.setTooltip(new Tooltip("Go to next record"));
        buttonLast.setTooltip(new Tooltip("Go to last record"));
        buttonRefresh.setTooltip(new Tooltip("Refresh table data"));
    }

    private void initButtonsIcons() {
        buttonNew.setGraphic(new ImageView(ImageResources.getButtonPlus()));
        buttonEdit.setGraphic(new ImageView(ImageResources.getButtonEdit()));
        buttonDelete.setGraphic(new ImageView(ImageResources.getButtonDelete()));

        buttonViewTab.setGraphic(new ImageView(ImageResources.getButtonView()));
        buttonExport.setGraphic(new ImageView(ImageResources.getButtonExport()));
        buttonSync.setGraphic(new ImageView(ImageResources.getButtonSync()));

        buttonDirectory.setGraphic(new ImageView(ImageResources.getButtonFolder()));

        buttonFirst.setGraphic(new ImageView(ImageResources.getButtonFirst()));
        buttonPrior.setGraphic(new ImageView(ImageResources.getButtonPrior()));
        buttonNext.setGraphic(new ImageView(ImageResources.getButtonNext()));
        buttonLast.setGraphic(new ImageView(ImageResources.getButtonLast()));
        buttonRefresh.setGraphic(new ImageView(ImageResources.getButtonRefresh()));
    }

    private void initSpreadSheetTableColumns() {
        // Main Table
//        employeeIdColumn.setCellValueFactory(new PropertyValueFactory<Employee, String>("employeeId"));
        columnName.setCellValueFactory(cellData -> cellData.getValue().nameProperty());
        columnID.setCellValueFactory(cellData -> cellData.getValue().docIdProperty());
        columnExportDir.setCellValueFactory(cellData -> cellData.getValue().exportDirProperty());
        columnStatus.setCellValueFactory(cellData -> cellData.getValue().statusProperty());
        columnExportType.setCellValueFactory(cellData -> cellData.getValue().exportTypeProperty());
        columnExportPeriod.setCellValueFactory(cellData -> cellData.getValue().periodProperty().asObject());
        columnNextSyncDate.setCellValueFactory(cellData -> cellData.getValue().nextSyncDateProperty());
        columnLastSyncDate.setCellValueFactory(cellData -> cellData.getValue().lastSyncDateProperty());
        columnExportResult.setCellValueFactory(cellData -> cellData.getValue().exportResultProperty());


        // Children Table
        columnSheetId.setCellValueFactory(cellData -> cellData.getValue().sheetIdProperty().asObject());
        columnIndex.setCellValueFactory(cellData -> cellData.getValue().indexProperty().asObject());
        columnTitle.setCellValueFactory(cellData -> cellData.getValue().titleProperty());
        columnUserName.setCellValueFactory(cellData -> cellData.getValue().userNameProperty());
//        columnType.setCellValueFactory(cellData -> cellData.getValue().typeProperty());
        columnFrozenRow.setCellValueFactory(cellData -> cellData.getValue().frozenRowProperty().asObject());
        columnExport.setCellValueFactory(cellData -> cellData.getValue().exportProperty());

        columnExportResult.setCellFactory(column -> {
            return new TableCell<GoogleDoc, String>() {
                @Override
                protected void updateItem(String result, boolean empty) {
                    super.updateItem(result, empty);

                    if (result == null || empty) {
                        setText(null);
                        setStyle("");
                    } else {

                        setText(result);

                        if (ExportResult.FAIL == ExportResult.valueOf(result)) {
                            //setTextFill(Color.BLUE);
                            setTextFill(Color.rgb(100, 50, 150));
                            setStyle("-fx-background-color: rgba(250,0,0,0.6)");
                        } else {
                            //setTextFill(Color.YELLOW);
                            setTextFill(Color.rgb(230, 230, 75));
                            setStyle("-fx-background-color: rgba(70,140,35,0.7)");
                        }
                    }
                }
            };
        });
    }

    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;
        if (mainApp.getGoogleDocList() != null) {
            tableGoogleDoc.setItems(mainApp.getGoogleDocList());
            if (!mainApp.getGoogleDocList().isEmpty()) {
                tableGoogleDoc.getSelectionModel().selectFirst();
            }
        }
    }

    private void showGoogleDocDetails(GoogleDoc googleDoc) {
        if (googleDoc != null) {
            textFID.setText(googleDoc.getDocId());
            textFName.setText(googleDoc.getName());
            textFExportDir.setText(googleDoc.getExportDir());
            textFStatus.setText(googleDoc.getStatus());
            textFExportType.setText(googleDoc.getExportType());
            textFPeriod.setText(String.valueOf(googleDoc.getPeriod()));

            textFNextSyncDate.setText(DateUtil.format(googleDoc.getNextSyncDate(), ""));
            textFLastSyncDate.setText(DateUtil.format(googleDoc.getLastSyncDate(), ""));
            textFExportResult.setText(googleDoc.getExportResult());

            docSheetObservableList = FXCollections.observableArrayList(googleDoc.getDocSheets());

        } else {
            textFID.setText("");
            textFName.setText("");
            textFExportDir.setText("");
            textFStatus.setText("");
            textFExportType.setText("");
            textFPeriod.setText("");
            textFNextSyncDate.setText("");
            textFLastSyncDate.setText("");
            textFExportResult.setText("");

            docSheetObservableList = FXCollections.observableArrayList();
        }

        if (mainApp != null) {
            mainApp.setCurrentGoogleDoc(googleDoc);
        }
        tableDocSheet.setItems(docSheetObservableList);
    }

    public void onButtonDelete(ActionEvent actionEvent) {
        final int selectedIndex = tableGoogleDoc.getSelectionModel().getSelectedIndex();
        if (selectedIndex >= 0) {
            if (Dialogs.showConfirmDialog(new DialogText("Delete record", "You want to delete record: \n" + tableGoogleDoc.getSelectionModel().getSelectedItem().toInfoString(), "Confirm?"), mainApp.getRootLogger())) {
                tableGoogleDoc.getItems().remove(selectedIndex);
                //mainApp.setTableEdited(true);
                mainApp.setHasChanged(true);
            }
        } else {
            Dialogs.showMessage(Alert.AlertType.WARNING, new DialogText("No Selection", "No Record Selected", "Please select a record in the table."), null);
        }
    }

    public void onButtonNew(ActionEvent actionEvent) {
        GoogleDoc tempGoogleDoc = new GoogleDoc();
        boolean okClicked = mainApp.showGoogleDocEditDialog(tempGoogleDoc);
        if (okClicked) {
            //mainApp.setTableEdited(true);
            mainApp.setHasChanged(true);
            mainApp.getGoogleDocList().add(tempGoogleDoc);
        }
    }

    public void onButtonEdit(ActionEvent actionEvent) {
        GoogleDoc selectedGoogleDoc = tableGoogleDoc.getSelectionModel().getSelectedItem();
        if (selectedGoogleDoc != null) {
            boolean okClicked = mainApp.showGoogleDocEditDialog(selectedGoogleDoc);
            if (okClicked) {
                //mainApp.setTableEdited(true);
                mainApp.setHasChanged(true);
                showGoogleDocDetails(selectedGoogleDoc);
            }
        } else {
            // Ничего не выбрано.
            Dialogs.showMessage(Alert.AlertType.WARNING, new DialogText("No Selection", "No Person Selected", "Please select a person in the table."), null);
        }
    }

    public void onButtonViewTab(ActionEvent actionEvent) {
        if (!isEmptyTableDocSheets()) {
            DocSheet docSheet = tableDocSheet.getSelectionModel().getSelectedItem();
            if (docSheet != null) {
                mainApp.showTabSheetDialog(docSheet);
            } else {
                Dialogs.showMessage(Alert.AlertType.WARNING, new DialogText("Data error", "No row selected!", "For view, please, select row"), mainApp.getRootLogger());
            }
        } else {
            Dialogs.showMessage(Alert.AlertType.WARNING, new DialogText("Data error", "Nothing to open!", "Table is empty"), mainApp.getRootLogger());
        }
    }

    private boolean isEmptyTableDocSheets() {
        return (docSheetObservableList == null || docSheetObservableList.isEmpty());
    }

    public void onButtonDirectory(ActionEvent actionEvent) {
        final String path = textFExportDir.getText();
        if (path != null && !path.equals("")) {

            try {
                FileUtils.openDirectory(path);
            } catch (IOException e) {
                Dialogs.showMessage(Alert.AlertType.WARNING, new DialogText("Open error", "Cannot open directory '" + path + "'", "Please check data at field 'Export directory'."), null);
            }
        } else {
            Dialogs.showMessage(Alert.AlertType.WARNING, new DialogText("Attention", "Not directory set", "Please select a currentt directory at field 'Export directory'."), null);
        }

    }

    public void onButtonFirst(ActionEvent actionEvent) {
        tableGoogleDoc.getSelectionModel().selectFirst();
        tableGoogleDoc.scrollTo(tableDocSheet.getSelectionModel().getSelectedIndex());
    }

    public void onButtonPrior(ActionEvent actionEvent) {
        tableGoogleDoc.getSelectionModel().selectPrevious();
        tableGoogleDoc.scrollTo(tableDocSheet.getSelectionModel().getSelectedIndex());
    }

    public void onButtonNext(ActionEvent actionEvent) {
        tableGoogleDoc.getSelectionModel().selectNext();
        tableGoogleDoc.scrollTo(tableDocSheet.getSelectionModel().getSelectedIndex());
    }

    public void onButtonLast(ActionEvent actionEvent) {
        tableGoogleDoc.getSelectionModel().selectLast();
        tableGoogleDoc.scrollTo(tableDocSheet.getSelectionModel().getSelectedIndex());
    }

    public void onButtonRefresh(ActionEvent actionEvent) {
        tableGoogleDoc.refresh();
    }

    public void onButtonSync(ActionEvent actionEvent) {
        Calendar timerNow = Calendar.getInstance();
        Date timerNowDate = timerNow.getTime();
        //System.out.println("Try manual sync: ");
        GoogleDoc googleDoc = mainApp.getCurrentGoogleDoc();
        try {
            ExportHandler.exportGoogleDoc(mainApp.getService(), googleDoc, timerNowDate);
            //setTableEdited(true);
            mainApp.setHasChanged(true);
            //mainApp.getSystemTray().showTrayMessage(mainApp.getAppName(), "Sync '" + googleDoc.getName() + "' success", TrayIcon.MessageType.INFO);
            mainApp.getRootLogger().info("Sync " + mainApp.getCurrentGoogleDoc().toShortString() + " success");

        } catch (IOException | ArithmeticException | IllegalArgumentException e) {
            googleDoc.setExportResults(new Date(), ExportResult.FAIL);
            //Dialogs.showErrorDialog(e, new DialogText("Export error", "Can't export " + googleDoc.getName(), "The error is: "), mainApp.getRootLogger());
            Dialogs.showMessage(Alert.AlertType.WARNING, new DialogText("Initialization error", "Cannot read Google API", e.getMessage()), mainApp.getRootLogger());
            //mainApp.getSystemTray().showTrayMessage(mainApp.getAppName(), "Sync '" + googleDoc.getName() + "' error", TrayIcon.MessageType.ERROR);
            mainApp.getRootLogger().error("Error on  sync " + googleDoc.toShortString() + " with message: " + e.getMessage());
        }

    }
}
