package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import model.Appointment;
import utilities.ContactQuery;
import utilities.CustomerQuery;

import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class AppointmentDetailsScreen implements Initializable {

    @FXML
    private TextField idField;
    @FXML
    private TextField customerField;
    @FXML
    private TextField contactField;
    @FXML
    private TextField emailField;
    @FXML
    private TextField titleField;
    @FXML
    private TextField descriptionField;
    @FXML
    private TextField locationField;
    @FXML
    private TextField typeField;
    @FXML
    private TextField startField;
    @FXML
    private TextField endField;
    @FXML
    private TextField createDateField;
    @FXML
    private TextField createdByField;
    @FXML
    private TextField lastUpdateField;
    @FXML
    private TextField lastUpdatedByField;
    @FXML
    private Button closeButton;

    private Appointment appointmentToInspect;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        appointmentToInspect = ScheduleScreenController.getSelectedAppointment();
        populateTextFields();
    }

    /**
     * This method populates the text fields in the AppointmentDetailsScreen by using the data passed from a static
     * Appointment object in the ScheduleScreen
     */
    private void populateTextFields() {
        idField.setText(Integer.toString(appointmentToInspect.getAppointmentId()));

        try {
            customerField.setText(CustomerQuery.getNameFromId(appointmentToInspect.getCustomerId()));
            contactField.setText(ContactQuery.getNameFromId(appointmentToInspect.getContactId()));
            emailField.setText(ContactQuery.getEmailFromId(appointmentToInspect.getContactId()));
        } catch (SQLException e) {
            e.printStackTrace();
        }

        titleField.setText(appointmentToInspect.getTitle());
        descriptionField.setText(appointmentToInspect.getDescription());
        locationField.setText(appointmentToInspect.getLocation());
        typeField.setText(appointmentToInspect.getMeetingType());
        startField.setText(appointmentToInspect.getStartTime().toString());
        endField.setText(appointmentToInspect.getEndTime().toString());
        createDateField.setText(appointmentToInspect.getCreateDate().toString());
        createdByField.setText(appointmentToInspect.getCreatedBy());
        lastUpdateField.setText(appointmentToInspect.getLastUpdate().toString());
        lastUpdatedByField.setText(appointmentToInspect.getLastUpdatedBy());
    }

    public void closeWindow(ActionEvent actionEvent) {
        Stage stage = (Stage) closeButton.getScene().getWindow();
        stage.close();
    }
}
