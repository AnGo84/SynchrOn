package com.synchron.controller;

import com.synchron.fx.DialogText;
import com.synchron.fx.Dialogs;
import com.synchron.fx.ImageResources;
import com.synchron.security.Security;
import javafx.application.HostServices;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

/**
 * Created by AnGo on 09.09.2017.
 */
public class LicenseController {
    @FXML
    private Button buttonActivate;
    @FXML
    private Button buttonCancel;
    @FXML
    private Hyperlink hyperlinkSite;

    private Stage dialogStage;

    private HostServices hostServices;

    private boolean okClicked = false;

    public Stage getDialogStage() {
        return dialogStage;
    }

    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;

    }

    public HostServices getHostServices() {
        return hostServices;
    }

    public void setHostServices(HostServices hostServices) {
        this.hostServices = hostServices;
    }

    public boolean isOkClicked() {
        return okClicked;
    }

    @FXML
    private void initialize() {
        initButtonsIcons();

        hyperlinkSite.setVisited(false);

        hyperlinkSite.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                hostServices.showDocument(Security.LICENSE_LINK);
            }
        });
    }

    private void initButtonsIcons() {
        buttonActivate.setGraphic(new ImageView(ImageResources.getButtonActivate()));
        buttonCancel.setGraphic(new ImageView(ImageResources.getButtonCancel()));
    }

    public void onButtonActivate(ActionEvent actionEvent) {
        Dialogs.showMessage(Alert.AlertType.INFORMATION, new DialogText("License checking result", "Your license neither good nor bad))))","This trial version of checking"), null);
        okClicked = true;
        dialogStage.close();
    }

    public void onButtonCancel(ActionEvent actionEvent) {
        dialogStage.close();
    }
}
