package com.synchron.controller;

import com.synchron.fx.ImageResources;
import javafx.application.HostServices;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

/**
 * Created by AnGo on 09.09.2017.
 */
public class TrialInfoController {
    @FXML
    private Button buttonOk;
    @FXML
    private Hyperlink hyperlinkSite;

    private Stage dialogStage;

    private HostServices hostServices;

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

    @FXML
    private void initialize() {
        initButtonsIcons();

        hyperlinkSite.setVisited(false);

        hyperlinkSite.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                hostServices.showDocument("www.google.com");
            }
        });
    }

    private void initButtonsIcons() {
        buttonOk.setGraphic(new ImageView(ImageResources.getButtonOk()));
    }

    public void onButtonOk(ActionEvent actionEvent) {
        dialogStage.close();
    }
}
