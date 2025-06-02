package com.example.login;

import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.Locale;

public class CalendarController {
    @FXML
    private Label selectedDateLabel;

    @FXML
    private DatePicker datePicker;

    @FXML
    private void handleDateChange() {
        if (datePicker.getValue() != null) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMMM yyyy");
            String formattedDate = datePicker.getValue().format(formatter);
            selectedDateLabel.setText("Date sélectionnée: " + formattedDate);
        }
}

}
