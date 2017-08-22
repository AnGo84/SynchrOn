package com.synchron.fx;

import com.synchron.MainApp;
import javafx.scene.image.Image;

import java.net.URL;


/**
 * Created by AnGo on 20.06.2017.
 */
public class ImageResources {
    private static URL trayImageUrl = MainApp.class.getResource("/images/SynchrOn_16.png");

    private static String appIconPath = MainApp.class.getResource("/images/SynchrOn_16.png").getPath();

    //private static Image appIcon = new Image(MainApp.class.getResourceAsStream("/images/tableSync_32.png"));
    private static Image appIcon = new Image(MainApp.class.getResourceAsStream("/images/SynchrOn_32.png"));

    //private static Image appIcon = new Image( appIconPath);

    private static Image buttonFirst = new Image(MainApp.class.getResourceAsStream("/images/button/first_16.png"));
    private static Image buttonPrior = new Image(MainApp.class.getResourceAsStream("/images/button/prior_16.png"));
    private static Image buttonNext = new Image(MainApp.class.getResourceAsStream("/images/button/next_16.png"));
    private static Image buttonLast = new Image(MainApp.class.getResourceAsStream("/images/button/last_16.png"));
    private static Image buttonRefresh = new Image(MainApp.class.getResourceAsStream("/images/button/repeat_16.png"));

    private static Image buttonPlus = new Image(MainApp.class.getResourceAsStream("/images/button/plus_16.png"));
    private static Image buttonEdit = new Image(MainApp.class.getResourceAsStream("/images/button/pencil_16.png"));
    private static Image buttonDelete = new Image(MainApp.class.getResourceAsStream("/images/button/minus_16.png"));

    private static Image buttonView = new Image(MainApp.class.getResourceAsStream("/images/button/view_16.png"));
    private static Image buttonExport = new Image(MainApp.class.getResourceAsStream("/images/button/share_16.png"));

    private static Image buttonFolder = new Image(MainApp.class.getResourceAsStream("/images/button/folder_16.png"));

    private static Image buttonOk = new Image(MainApp.class.getResourceAsStream("/images/button/button_ok_16.png"));
    private static Image buttonCancel = new Image(MainApp.class.getResourceAsStream("/images/button/button_cancel_16.png"));

    private static Image buttonSync = new Image(MainApp.class.getResourceAsStream("/images/button/sync_16.png"));

    private static Image buttonTest = new Image(MainApp.class.getResourceAsStream("/images/button/antenna.png"));

    public static String getAppIconPath() {
        return appIconPath;
    }

    public static URL getTrayImageUrl() {
        return trayImageUrl;
    }

    public static Image getAppIcon() {
        return appIcon;
    }

    public static Image getButtonFirst() {
        return buttonFirst;
    }

    public static Image getButtonPrior() {
        return buttonPrior;
    }

    public static Image getButtonNext() {
        return buttonNext;
    }

    public static Image getButtonLast() {
        return buttonLast;
    }

    public static Image getButtonPlus() {
        return buttonPlus;
    }

    public static Image getButtonEdit() {
        return buttonEdit;
    }

    public static Image getButtonDelete() {
        return buttonDelete;
    }

    public static Image getButtonView() {
        return buttonView;
    }

    public static Image getButtonExport() {
        return buttonExport;
    }

    public static Image getButtonFolder() {
        return buttonFolder;
    }

    public static Image getButtonOk() {
        return buttonOk;
    }

    public static Image getButtonCancel() {
        return buttonCancel;
    }

    public static Image getButtonSync() {
        return buttonSync;
    }

    public static Image getButtonTest() {
        return buttonTest;
    }

    public static Image getButtonRefresh() {
        return buttonRefresh;
    }
}
