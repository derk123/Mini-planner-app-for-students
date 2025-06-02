package com.example.login;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;

import java.io.IOException;

public class InterfaceUserController {

    @FXML
    private StackPane contentStackPane;

    @FXML
    private ImageView userImageView;

    @FXML
    private ImageView calendarImageView;

    @FXML
    private ImageView clipboardImageView;

    @FXML
    private ImageView menuImageView;

    @FXML
    private void handleUserImageClick() {
        loadPage("user.fxml");
    }

    @FXML
    private void handleCalendarImageClick() {
        loadPage("calendar.fxml");
    }

    @FXML
    private void handleClipboardImageClick() {
        loadPage("clipboard.fxml");
    }

    @FXML
    private void handleMenuImageClick() {
        loadPage("menu.fxml");
    }

    private void loadPage(String fxmlFile) {
        try {
            Parent page = FXMLLoader.load(getClass().getResource(fxmlFile));
            contentStackPane.getChildren().clear();
            contentStackPane.getChildren().add(page);
        } catch (IOException e) {
            e.printStackTrace();
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Unable to load page");
            alert.setContentText("The requested page could not be loaded.");
            alert.showAndWait();
        }
    }
}
