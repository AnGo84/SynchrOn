package com.synchron;/**
 * Created by AnGo on 08.06.2017.
 */

import com.google.api.services.sheets.v4.Sheets;
import com.synchron.awt.SystemTray;
import com.synchron.controller.*;
import com.synchron.custom.DateUtil;
import com.synchron.custom.FileUtils;
import com.synchron.export.ExportHandler;
import com.synchron.export.ExportResult;
import com.synchron.export.XMLIOHandler;
import com.synchron.fx.DialogText;
import com.synchron.fx.Dialogs;
import com.synchron.fx.ImageResources;
import com.synchron.google.GoogleSheetIOHandler;
import com.synchron.model.DocSheet;
import com.synchron.model.GoogleDoc;
import com.synchron.properties.PreferencesHandler;
import com.synchron.properties.PropertiesHandler;
import com.synchron.security.Security;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.xml.bind.JAXBException;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.List;

public class MainApp extends Application {
    private static final String APP_NAME = "SynchrOn App";
    private static final String CONFIG_FILE_NAME = "SynchrOn.properties";
    private static final Logger rootLogger = LogManager.getRootLogger();

    private String tableFileName = "New";
    //private static boolean tableEdited = false;
    private BooleanProperty hasChanged = new SimpleBooleanProperty(false);

    private Properties properties = new Properties();
    private File propertiesFile = FileUtils.getFileWithName(this.getClass(), CONFIG_FILE_NAME);
    private Stage primaryStage;
    private BorderPane rootLayout;
    private ObservableList<GoogleDoc> googleDocList = FXCollections.observableArrayList();
    private GoogleDoc currentGoogleDoc = null;

    private SystemTray systemTray;
    private Timer timer = new Timer(true);
    private TimerTask timeTask = new TimerTask() {

        @Override
        public void run() {
            Platform.runLater(
                    new Runnable() {
                        @Override
                        public void run() {
                            Calendar timerNow = Calendar.getInstance();
                            Date timerNowDate = timerNow.getTime();
                            //System.out.println("Timer reached: " + timerNowDate);
                            List<GoogleDoc> googleDocs = getTaskList(timerNowDate);
                            autoExport(googleDocs, timerNowDate);
                        }
                    }
            );
        }
    };

    {
        try {
            properties = PropertiesHandler.getPropertiesFromFile(propertiesFile);
            getRootLogger().info(PropertiesHandler.toString(properties));

        } catch (IOException e) {
//            e.printStackTrace();
            Dialogs.showErrorDialog(e, new DialogText("Application start error", "Error with resource's file", "Can't find resource's file '" + propertiesFile + "'"), rootLogger);
        }
    }

    public static Logger getRootLogger() {
        return rootLogger;
    }

    public static String getAppName() {
        return APP_NAME;
    }

    public static void main(String[] args) {

        rootLogger.info("Start");
//        if (DateUtil.isEndTrial(new Date())) {
//            rootLogger.info("Trial period ended");
//
//        } else {
//            launch(args);
//        }
        launch(args);

        rootLogger.info("Close");
    }

    public boolean isHasChanged() {
        return hasChanged.get();
    }

    public void setHasChanged(boolean hasChanged) {
        this.hasChanged.set(hasChanged);
    }

    public BooleanProperty hasChangedProperty() {
        return hasChanged;
    }

    public String getTableFileName() {
        return tableFileName;
    }

    public void setTableFileName(String tableFileName) {
        this.tableFileName = tableFileName;
    }

    public SystemTray getSystemTray() {
        return systemTray;
    }

    public Stage getPrimaryStage() {
        return primaryStage;
    }

    public ObservableList<GoogleDoc> getGoogleDocList() {
        return googleDocList;
    }

    public Properties getProperties() {
        return properties;
    }

    public void setProperties(Properties properties) {
        this.properties = properties;
    }

    public File getPropertiesFile() {
        return propertiesFile;
    }

    public GoogleDoc getCurrentGoogleDoc() {
        return currentGoogleDoc;
    }

    public void setCurrentGoogleDoc(GoogleDoc currentGoogleDoc) {
        this.currentGoogleDoc = currentGoogleDoc;
    }

    private void autoExport(List<GoogleDoc> googleDocs, Date timerNowDate) {
        if (googleDocs != null && !googleDocs.isEmpty()) {
            rootLogger.info("Sync start " + timerNowDate + " :");
            for (GoogleDoc googleDoc : googleDocs) {
                //if (!googleDoc.getName().equals("") && googleDoc.getExportType() != null && !googleDoc.getExportDirectory().equals("")) {
                try {
                    ExportHandler.exportGoogleDoc(getService(), googleDoc, timerNowDate);
                    //setTableEdited(true);
                    setHasChanged(true);

                    systemTray.showTrayMessage(APP_NAME, "Sync '" + googleDoc.getName() + "' success", TrayIcon.MessageType.INFO);
                    rootLogger.info("Sync " + googleDoc.toShortString() + " success");

                } catch (IOException | ArithmeticException | IllegalArgumentException e) {
                    googleDoc.setExportResults(new Date(), ExportResult.FAIL);
                    systemTray.showTrayMessage(APP_NAME, "Sync '" + googleDoc.getName() + "' error", TrayIcon.MessageType.ERROR);
                    rootLogger.error("Error on  sync " + googleDoc.toShortString() + " with message: " + e.getMessage());
                }
                //}
            }
        }
    }


    public Sheets getService() throws IOException {
        rootLogger.info("Sync with: \n" + PropertiesHandler.getGoogleAPIProject(properties));
        Sheets service = GoogleSheetIOHandler.getSheetsService(PropertiesHandler.getGoogleAPIProject(properties));
//        try {
//                service = GoogleSheetIOHandler.getSheetsService(PreferencesHandler.getGoogleAPIProject(), getRootLogger());
//            } catch (IOException e) {
//                Dialogs.showErrorDialog(e, new DialogText("Initialization error", "Cannot read Google API", "The error is:"), getRootLogger());
//            }
        return service;
    }

    @Override
    public void start(Stage primaryStage) {
        if (Security.checkTrialPeriod()) {
            //Dialogs.showInfoDialog(new DialogText("Licence info", "Trial period expired", "You can visit our site:"), "www.google.com","site",  getHostServices());
            showTrialDialog();
        }

        this.primaryStage = primaryStage;

        //this.primaryStage.setTitle(APP_NAME);

        // instructs the javafx system not to exit implicitly when the last application window is shut.
        Platform.setImplicitExit(false);

        // sets up the tray icon (using awt code run on the swing thread).
        systemTray = new SystemTray(primaryStage, this);
//        javax.swing.SwingUtilities.invokeLater(this:: systemTray.addAppToTray);

        javax.swing.SwingUtilities.invokeLater(
                new Runnable() {
                    public void run() {
                        try {
                            //getRootLogger().info("2");
                            systemTray.addAppToTray();

                        } catch (AWTException | IOException e) {
                            //e.printStackTrace();
                            getRootLogger().info("Can't crate Tray icon: \n" + e.getMessage());
                            systemTray.onExitAction();
                        }
                    }
                }
        );

        // Add table records' changes listener
        hasChanged.addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                //System.out.println("changed " + oldValue + "->" + newValue + " in thread: " + Thread.currentThread().getFileName());
                if (Platform.isFxApplicationThread()) {
                    setMainAppTitle();
                }
            }
        });

        initRootLayout();
        showGoogleDocView();
    }

    public void initRootLayout() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("/view/RootLayout.fxml"));
//            loader.setLocation(getClass().getResource("/view/RootLayout.fxml"));
            rootLayout = (BorderPane) loader.load();

            Scene scene = new Scene(rootLayout);

            primaryStage.getIcons().add(ImageResources.getAppIcon());

            primaryStage.setScene(scene);

//            // request on exit
//            primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
//                @Override
//                public void handle(WindowEvent event) {
//                    if (!shutDown()) {
//                        event.consume();
//                    }
//                }
//            });

            // Даём контроллеру доступ к главному прилодению.
            RootLayoutController controller = loader.getController();
            controller.setMainApp(this);

            primaryStage.show();

        } catch (IOException e) {
            Dialogs.showErrorDialog(e, new DialogText("Form show error", "", "Can't open main form"), rootLogger);
        }


        //loadGoogleDocsFromFile(PreferencesHandler.getPreferenceFilePath(MainApp.class, PreferencesHandler.XML_FILE_PATH));
        loadGoogleDocsFromFile(PropertiesHandler.getPropertyString(properties, PropertiesHandler.APP_XML_FILE));

        // start timer
        int delay = PropertiesHandler.getPropertyInt(properties, PropertiesHandler.DELAY_AFTER_START, PropertiesHandler.DEFAULT_DELAY_AFTER_START);
        int repeat = PropertiesHandler.getPropertyInt(properties, PropertiesHandler.REPEAT_PERIOD, PropertiesHandler.DEFAULT_REPEAT_PERIOD);
        //System.out.println("Dalay: " + delay + ", repeat: " + repeat);

        if (repeat > 0) {
            timer.scheduleAtFixedRate(
                    timeTask,   //task to be scheduled
                    DateUtil.addToDate(new Date(), delay),  //First time at which task is to be executed
                    1000 * repeat);    //repeat period, in milliseconds
        }
    }

    public void showGoogleDocView() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("/view/GoogleDocView.fxml"));
            AnchorPane personOverview = loader.load();

            rootLayout.setCenter(personOverview);

            GoogleDocController controller = loader.getController();
            controller.setMainApp(this);
        } catch (IOException e) {
            Dialogs.showErrorDialog(e, new DialogText("Form show error", "", "Can't open DocView form"), rootLogger);
        }
    }

    public void showSettingsView() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("/view/SettingsView.fxml"));
            AnchorPane page = loader.load();

            Stage dialogStage = new Stage();
            dialogStage.setTitle("Settings");
            dialogStage.getIcons().add(ImageResources.getAppIcon());

            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(primaryStage);
            Scene scene = new Scene(page);
            dialogStage.setScene(scene);

            SettingsController controller = loader.getController();
            controller.setMainApp(this);
            controller.setDialogStage(dialogStage);

            dialogStage.showAndWait();

        } catch (IOException e) {
            //e.printStackTrace();
            Dialogs.showErrorDialog(e, new DialogText("Form show error", "", "Can't open Settings form"), rootLogger);
        }
    }

    public boolean showGoogleDocEditDialog(GoogleDoc googleDoc) {
        try {
            // Загружаем fxml-файл и создаём новую сцену
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("/view/GoogleDocEditDialog.fxml"));
            AnchorPane page = (AnchorPane) loader.load();

            // Создаём диалоговое окно Stage.
            Stage dialogStage = new Stage();
            dialogStage.setTitle("Edit GoogleDoc");
            dialogStage.getIcons().add(ImageResources.getAppIcon());

            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(primaryStage);
            Scene scene = new Scene(page);
            dialogStage.setScene(scene);

            // Передаём таблицу в контроллер.
            GoogleDocEditDialogController controller = loader.getController();
            controller.setDialogStage(dialogStage);
            controller.setGoogleDoc(googleDoc);
            controller.setMainApp(this);

            // Отображаем диалоговое окно и ждём, пока пользователь его не закроет
            dialogStage.showAndWait();

            return controller.isOkClicked();
        } catch (IOException e) {
            Dialogs.showErrorDialog(e, new DialogText("Form show error", "", "Can't open form 'Edit GoogleDoc'"), rootLogger);
            return false;
        }
    }


    public void showTabSheetDialog(DocSheet docSheet) {
        try {

            // Загружаем fxml-файл и создаём новую сцену
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("/view/TabSheetView.fxml"));
            AnchorPane page = (AnchorPane) loader.load();

            // Создаём диалоговое окно Stage.
            Stage dialogStage = new Stage();
            dialogStage.setTitle("TabSheet - " + currentGoogleDoc.getName() + "." + docSheet.getTitle());
            dialogStage.getIcons().add(ImageResources.getAppIcon());


            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(primaryStage);
            Scene scene = new Scene(page);
            dialogStage.setScene(scene);

            // Передаём таблицу в контроллер.

            TabSheetController controller = loader.getController();
            controller.setDialogStage(dialogStage);
            controller.setGoogleDoc(currentGoogleDoc, docSheet);
            controller.setMainApp(this);

            dialogStage.setOnShown(new EventHandler<WindowEvent>() {
                @Override
                public void handle(WindowEvent event) {
                    //Dialogs.showMessage(Alert.AlertType.INFORMATION, new DialogText("After show", "It is showing Tab Sheet '" + docSheet.getTitle() + " to folder '" + getCurrentGoogleDoc().getExportDir() + "'", null), null);
                    controller.fillTable();
                }
            });

            // Отображаем диалоговое окно и ждём, пока пользователь его не закроет
            dialogStage.showAndWait();

        } catch (IOException e) {
            Dialogs.showErrorDialog(e, new DialogText("Form show error", "", "Can't open form 'TabSheet'"), rootLogger);
        }
    }


    public void showTrialDialog() {
        try {
            // Загружаем fxml-файл и создаём новую сцену
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("/view/TrialInfo_View.fxml"));
            AnchorPane page = (AnchorPane) loader.load();

            // Создаём диалоговое окно Stage.
            Stage dialogStage = new Stage();
            dialogStage.setResizable(false);
            dialogStage.setTitle("Info");
            dialogStage.getIcons().add(ImageResources.getAppIcon());

            dialogStage.initStyle(StageStyle.UTILITY);

            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(primaryStage);
            Scene scene = new Scene(page);
            dialogStage.setScene(scene);

            // Передаём таблицу в контроллер.
            TrialInfoController controller = loader.getController();
            controller.setDialogStage(dialogStage);
            controller.setHostServices(this.getHostServices());
            //controller.setMainApp(this);

            // Отображаем диалоговое окно и ждём, пока пользователь его не закроет
            dialogStage.showAndWait();

        } catch (IOException e) {
            Dialogs.showErrorDialog(e, new DialogText("Form show error", "", "Can't open form 'Trial dialog'"), rootLogger);
        }
    }

    public void loadGoogleDocsFromFile(File file) {

        if (file != null) {
            if (file.exists()) {
                PreferencesHandler.setPreferenceFilePath(file, MainApp.class, PreferencesHandler.LAST_FILE_PATH);
                try {
                    getGoogleDocList().clear();
                    getGoogleDocList().addAll(XMLIOHandler.readGoogleDocsFromFile(file));
                    getRootLogger().info("Open file: '" + file.getAbsolutePath() + "'");

                    //setMainAppTitle(file.getFileName());
                    tableFileName = file.getPath();
                    //setTableEdited(false);
                    hasChanged.set(false);


                } catch (JAXBException e) {
//                e.printStackTrace();
                    Dialogs.showErrorDialog(e, new DialogText("Open error", "Cannot open file '" + file.getAbsolutePath() + "'", "The error is: "), getRootLogger());
                }
            } else {
                Dialogs.showMessage(Alert.AlertType.WARNING, new DialogText("Warning", "Cannot open file '" + file + "'", "The wrong path or file not exist"), null);
            }
        }
        setMainAppTitle();
    }

    public void loadGoogleDocsFromFile(String fileName) {
        File file = new File(fileName);
        if (file != null && !fileName.equals("")) {
            loadGoogleDocsFromFile(file);
        }
    }

//    public void setMainAppTitle(String addName) {
//        if (addName != null && !addName.equals("")) {
//            addName = " - " + addName;
//        } else addName = "";
//
//        primaryStage.setTitle(APP_NAME + addName);
//    }

    public void setMainAppTitle(String newName) {
        if (newName != null && !newName.equals("")) {
            tableFileName = newName;
        } else newName = "New";

        setMainAppTitle();
    }

    private void setMainAppTitle() {
        String text = "";
        if (isHasChanged()) {
            text = " (changed)";
        }
        //System.out.println("Change TITLE: " + APP_NAME + " - " + tableFileName + " - " + text);
        primaryStage.setTitle(APP_NAME + " - " + tableFileName + text);
    }

    public boolean isEmptyDocList() {
        boolean isEmpty = (googleDocList == null || googleDocList.isEmpty());
        return isEmpty;
    }

    public boolean shutDown() {
        if (isConfirmShutDown()) {
            if (this.timer != null) {
                timer.cancel();
                timer.purge();
            }
            return true;
        }
        return false;
    }


    private boolean isConfirmShutDown() {
        if (isConfirmNotSave()) {
            return true;
        } else {
            if (Dialogs.showConfirmDialog(new DialogText("Close application", "Application will be closed", "Are you agree?"), null))
                return true;
        }
        return false;
    }

    public boolean isConfirmNotSave() {
        if (isHasChanged()) {
            if (Dialogs.showConfirmDialog(new DialogText("Close application", "Table was editing", "Quit without saving?"), null)) {
                return true;
            }
        }
        return false;
    }

    private List<GoogleDoc> getTaskList(Date date) {
        List<GoogleDoc> googleDocs = new ArrayList<>();
        for (GoogleDoc googleDoc : googleDocList) {
            if (googleDoc.getStatus().equals("On") && googleDoc.getNextSyncDate().compareTo(DateUtil.getLocalDateTime(new Date())) != 1) {
                googleDocs.add(googleDoc);
            }
        }
        return googleDocs;
    }

    public void saveMainTable() {
        //        File file = PreferencesHandler.getPreferenceFilePath(mainApp.getClass(), PreferencesHandler.LAST_FILE_PATH);
        File file = new File(PropertiesHandler.getPropertyString(getProperties(), PropertiesHandler.APP_XML_FILE));
        if (file != null) {
            try {
                XMLIOHandler.writeGoogleDocsToXMLFile(file, getGoogleDocList());

                tableFileName = file.getPath();
                //setTableEdited(false);
                hasChanged.set(false);

                Dialogs.showMessage(Alert.AlertType.INFORMATION, new DialogText("Saving file", "File '" + file.getAbsolutePath() + "' saved successful", ""), getRootLogger());
            } catch (JAXBException e) {
                Dialogs.showMessage(Alert.AlertType.WARNING, new DialogText("Saving error", "Error on saving file " + file.getAbsoluteFile(), e.getMessage()), getRootLogger());
            }
        } else {
            handleSaveAs();
        }
    }

    public void handleSaveAs() {
        if (!isEmptyDocList()) {
            FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("XML files (*.xml)", "*.xml");
            File file = Dialogs.saveFileDialog(PreferencesHandler.getPreferenceFilePath(this.getClass(), PreferencesHandler.LAST_FILE_PATH), extFilter, getPrimaryStage());
            if (file != null) {
                // Make sure it has the correct extension
                if (!file.getPath().endsWith(".xml")) {
                    file = new File(file.getPath() + ".xml");
                }

                if (PreferencesHandler.getPreferenceFilePath(this.getClass(), PreferencesHandler.LAST_FILE_PATH) == null) {
                    PreferencesHandler.setPreferenceFilePath(file, this.getClass(), PreferencesHandler.LAST_FILE_PATH);
                    setMainAppTitle(file.getName());
                }
                getRootLogger().info("Export to XML file '" + file.getParent() + "'");
                try {
                    XMLIOHandler.writeGoogleDocsToXMLFile(file, getGoogleDocList());

                    //setTableEdited(false);
                    hasChanged.set(false);

                    Dialogs.showMessage(Alert.AlertType.INFORMATION, new DialogText("Saving file", "File '" + file.getAbsolutePath() + "' saved successful", ""), getRootLogger());
                } catch (JAXBException e) {
                    Dialogs.showErrorDialog(e, new DialogText("Saving error", "Error on saving file " + file.getAbsoluteFile(), ""), getRootLogger());
//                    Dialogs.showMessage(Alert.AlertType.WARNING, new DialogText("Saving error", "Error on saving file " + file.getAbsoluteFile(), e.getMessage()), mainApp.getRootLogger());
                }
            } else {
                Dialogs.showMessage(Alert.AlertType.WARNING, new DialogText("Warning!", "Cannot save file '" + file + "'", "The wrong path or file not exist"), null);
            }
        } else {
            Dialogs.showMessage(Alert.AlertType.WARNING, new DialogText("Data error", "Nothing to save!", "Table is empty"), getRootLogger());
        }
    }

}
