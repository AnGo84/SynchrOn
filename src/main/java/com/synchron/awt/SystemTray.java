package com.synchron.awt;

import com.synchron.MainApp;
import com.synchron.fx.DialogText;
import com.synchron.fx.Dialogs;
import com.synchron.fx.ImageResources;
import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.stage.Stage;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;
import java.io.IOException;

/**
 * Created by AnGo on 21.08.2017.
 */
public class SystemTray {

    // setup the popup menu for the application.
    final java.awt.PopupMenu popup = new java.awt.PopupMenu();
    // application stage is stored so that it can be shown and hidden based on system tray icon operations.
    private Stage stage;
    private java.awt.SystemTray tray = java.awt.SystemTray.getSystemTray();
    private java.awt.TrayIcon trayIcon;

    private MainApp mainApp;

    public SystemTray(Stage stage, MainApp mainApp) {
        this.stage = stage;
        this.mainApp = mainApp;
    }

    /**
     * Sets up a system tray icon for the application.
     */
    public void addAppToTray() throws AWTException, IOException {
        //try {

        // ensure awt toolkit is initialized.
        java.awt.Toolkit.getDefaultToolkit();

        // app requires system tray support, just exit if there is no support.
        if (!java.awt.SystemTray.isSupported()) {
            //Dialogs.showMessage(Alert.AlertType.WARNING, new DialogText("Application error", "Error with system", "No system tray support, application exiting."), null);

            mainApp.getRootLogger().info("No system tray support, application exiting.");
            //System.out.println("No system tray support, application exiting.");
            Platform.exit();
        }

        // set up a system tray icon.
        //mainApp.getRootLogger().info("Tray icon: "+ImageResources.getTrayImageUrl().getPath());
        trayIcon = new TrayIcon(Toolkit.getDefaultToolkit().createImage(ImageResources.getTrayImageUrl()));
//            java.awt.TrayIcon trayIcon = new java.awt.TrayIcon(image);

        // if the user double-clicks on the tray icon, show the main app stage.
        trayIcon.addActionListener(event -> Platform.runLater(this::showStage));

        setUpTrayIconPopupMenu();

        trayIcon.setPopupMenu(popup);


        // add the application tray icon to the system tray.
        tray.add(trayIcon);

//        } catch (java.awt.AWTException | IOException e) {
////        } catch (java.awt.AWTException e) {
//            System.out.println("Unable to init system tray");
//            e.printStackTrace();
//        }
    }

    private void setUpTrayIconPopupMenu() {
        // if the user selects the default menu item (which includes the app name),
        // show the main app stage.
        MenuItem openItem = new MenuItem("Open SynchrOn");
        openItem.addActionListener(event -> Platform.runLater(this::showStage));

        // the convention for tray icons seems to be to set the default icon for opening
        // the application stage in a bold font.
        Font defaultFont = Font.decode(null);
        Font boldFont = defaultFont.deriveFont(Font.BOLD);
        openItem.setFont(boldFont);


        MenuItem saveAndExitItem = new MenuItem("Save and Exit");
        saveAndExitItem.addActionListener(event -> Platform.runLater(this::mainAppSave));

        // to really exit the application, the user must go to the system tray icon
        // and select the exit option, this will shutdown JavaFX and remove the
        // tray icon (removing the tray icon will also shut down AWT).
        MenuItem exitItem = new MenuItem("Exit");
        exitItem.addActionListener(event -> {
            onExitAction();
        });

        popup.add(openItem);
        popup.addSeparator();
        popup.add(saveAndExitItem);
        popup.addSeparator();
        popup.add(exitItem);
    }

    public void onExitAction() {
        Platform.exit();
        tray.remove(trayIcon);
    }


    public void showTrayMessage(String title, String text, java.awt.TrayIcon.MessageType type) {
        if (trayIcon != null) {
            javax.swing.SwingUtilities.invokeLater(() -> trayIcon.displayMessage(title, text, type));
        }
    }

    /**
     * Shows the application stage and ensures that it is brought ot the front of all stages.
     */
    private void showStage() {
        if (stage != null) {
            stage.show();
            stage.toFront();
        }
    }


    /**
     * Shows the application save dialog and close application.
     */
    private void mainAppSave() {
        if (mainApp != null) {
            mainApp.saveMainTable();
            onExitAction();
        }
    }
}

