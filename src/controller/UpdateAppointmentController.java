package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import model.Appointment;
import model.Contact;
import model.Customer;
import utilities.AppointmentQuery;
import utilities.ContactQuery;
import utilities.CustomerQuery;

import java.net.URL;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Calendar;
import java.util.ResourceBundle;

public class UpdateAppointmentController implements Initializable {

    @FXML
    private Button cancelButton;
    @FXML
    private TextField titleField;
    @FXML
    private ComboBox<Customer> customerCombo;
    @FXML
    private TextField descriptionField;
    @FXML
    private TextField locationField;
    @FXML
    private ComboBox<String> typeCombo;
    @FXML
    private DatePicker startDatePicker = null;
    @FXML
    private DatePicker endDatePicker = null;
    @FXML
    private ComboBox<Integer> startHourComboBox;
    @FXML
    private ComboBox<Integer> startMinuteComboBox;
    @FXML
    private ComboBox<Integer> endHourComboBox;
    @FXML
    private ComboBox<Integer> endMinuteComboBox;
    @FXML
    private ComboBox<Contact> contactCombo;
    private Appointment appointmentToUpdate;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        appointmentToUpdate = ScheduleScreenController.getSelectedAppointment();
        populateAppointmentData();
    }

    /**
     * This method validates the data entered into every text field and combobox, then updates a given Appointment
     * entry in the database
     * @param actionEvent is used to close the window if the Appointment was successfully added to the database
     */
    public void updateAppointment(ActionEvent actionEvent) {
        int id = appointmentToUpdate.getAppointmentId();
        String title = titleField.getText();
        Customer customer = customerCombo.getValue();
        String description = descriptionField.getText();
        String location = locationField.getText();
        String type = typeCombo.getSelectionModel().getSelectedItem();
        LocalDate startDate = startDatePicker.getValue();
        LocalDate endDate = endDatePicker.getValue();
        Timestamp lastUpdate = Timestamp.valueOf(LocalDateTime.now());
        String lastUpdateBy = LoginScreenController.getCurrentUser().getUsername();
        int userId = LoginScreenController.getCurrentUser().getUserId();
        Contact contact = contactCombo.getValue();

        //validate all the entered data
        if (title.equals("") || description.equals("") || location.equals("") || type.equals("") || startDate == null ||
            startHourComboBox.getValue() == null || startMinuteComboBox.getValue() == null || endDate == null ||
            endHourComboBox.getValue() == null || endMinuteComboBox.getValue() == null || customer == null || contact == null) {
            errorMessageDialogue("Error: Invalid Data", "Please enter valid data or make a selection for each field and try again.");
        } else {
            Timestamp startTime = Timestamp.valueOf(startDatePicker.getValue().atTime(startHourComboBox.getValue(), startMinuteComboBox.getValue()));
            Timestamp endTime = Timestamp.valueOf(endDatePicker.getValue().atTime(endHourComboBox.getValue(), endMinuteComboBox.getValue()));
            int customerId = customer.getCustomerId();
            int contactId = contact.getContactId();

            try {
                boolean overlappingFlag = false;

                //checks for any overlapping appointments and disregards the currently selected appointment during the check
                for (Appointment appointment : AppointmentQuery.getAllAppointments()) {
                    if ((appointment.getCustomerId() == customerId) && (AppointmentQuery.isOverlapping(appointment.getStartTime().toLocalDateTime(),
                            appointment.getEndTime().toLocalDateTime(), startTime.toLocalDateTime(), endTime.toLocalDateTime())) &&
                            (appointment.getAppointmentId() != id)) {
                        overlappingFlag = true;
                    }
                }

                //add the appointment if there are no overlaps, and it is during valid business hours
                if (overlappingFlag) {
                    errorMessageDialogue("Error: Invalid Meeting Time", "Customer has another meeting schedule during this time window. Please select a different time.");
                } else if (startTime.toLocalDateTime().isAfter(endTime.toLocalDateTime())) {
                    errorMessageDialogue("Error: Invalid Meeting Time", "Meeting start time must be before the end time. Please try again.");
                } else if (!isBusinessHours(startTime.toLocalDateTime(), endTime.toLocalDateTime())) {
                    errorMessageDialogue("Error: Invalid Meeting Time", "Appointments must occur during business hours (8:00am - 10:00pm EST). Please select a valid meeting time.");
                } else {
                    AppointmentQuery.updateAppointment(id, title, description, location, type, startTime, endTime, lastUpdate, lastUpdateBy, customerId, userId, contactId);

                    System.out.println("Existing appointment successfully updated.");
                    closeWindow(actionEvent);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * This method determines if an Appointment takes place during valid business hours by comparing the LocalDateTime
     * against the ZonedDateTime of the business
     * @param startDate is the LocalDateTime of the meeting's start
     * @param endDate is the LocalDateTime of the meeting's end
     * @return returns true if the range of startDate and endDate takes place fully during business hours
     */
    public boolean isBusinessHours(LocalDateTime startDate, LocalDateTime endDate) {
        ZonedDateTime zonedStartTime = ZonedDateTime.of(startDate, ZoneId.systemDefault());
        ZonedDateTime zonedEndTime = ZonedDateTime.of(endDate, ZoneId.systemDefault());
        LocalDate startDateTest = LocalDate.from(zonedStartTime);

        ZonedDateTime startBusinessHours = startDateTest.atTime(8, 0).atZone(ZoneId.of("America/New_York"));
        ZonedDateTime endBusinessHours = startDateTest.atTime(22, 0).atZone(ZoneId.of("America/New_York"));

        return (!(zonedStartTime.isBefore(startBusinessHours) || zonedEndTime.isAfter(endBusinessHours)));
    }

    /**
     * This method populates all the text fields and combo boxes based on the data passed by the static
     * selectedAppointment object
     */
    public void populateAppointmentData() {
        titleField.setText(appointmentToUpdate.getTitle());
        descriptionField.setText(appointmentToUpdate.getDescription());
        locationField.setText(appointmentToUpdate.getLocation());
        typeCombo.setValue(appointmentToUpdate.getMeetingType());
        startDatePicker.setValue(appointmentToUpdate.getStartTime().toLocalDateTime().toLocalDate());
        endDatePicker.setValue(appointmentToUpdate.getEndTime().toLocalDateTime().toLocalDate());

        //extracts the hour and minute of the start and end times in order to populate the ComboBox
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(appointmentToUpdate.getStartTime());
        startHourComboBox.setValue(calendar.get(Calendar.HOUR_OF_DAY));
        startMinuteComboBox.setValue(calendar.get(Calendar.MINUTE));
        calendar.setTime(appointmentToUpdate.getEndTime());
        endHourComboBox.setValue(calendar.get(Calendar.HOUR_OF_DAY));
        endMinuteComboBox.setValue(calendar.get(Calendar.MINUTE));



        for (int i = 0; i < 24; i++) {
            startHourComboBox.getItems().add(i);
            endHourComboBox.getItems().add(i);
        }
        for (int i = 0; i <60; i+=5) {
            startMinuteComboBox.getItems().add(i);
            endMinuteComboBox.getItems().add(i);
        }

        //populates the typeComboBox with all the meeting types
        typeCombo.getItems().addAll("New Client", "Annual Report", "Project Milestones", "Goals", "Planning Session", "De-Briefing");

        //populates the contact and customer ComboBoxes and also populates it correctly by looking for a match in the ID values
        try {
            for (Contact contact : ContactQuery.getAllContacts()) {
                contactCombo.getItems().addAll(contact);
                if (contact.getContactId() == appointmentToUpdate.getContactId()) {
                    contactCombo.setValue(contact);
                }
            }
            for (Customer customer : CustomerQuery.getAllCustomers()) {
                customerCombo.getItems().addAll(customer);
                if (customer.getCustomerId() == appointmentToUpdate.getCustomerId()) {
                    customerCombo.setValue(customer);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * This method closes the NewAppointmentScreen if a user wishes to cancel adding an Appointment
     * @param actionEvent is unused and was auto-generated
     */
    public void closeWindow(ActionEvent actionEvent) {
        Stage stage = (Stage) cancelButton.getScene().getWindow();
        stage.close();
    }

    /**
     * This method displays an error message to the user if something goes wrong
     * @param title sets the title of the window
     * @param content sets the message that will be displayed in the window
     */
    public void errorMessageDialogue(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.NONE);
        alert.getDialogPane().getButtonTypes().add(ButtonType.OK);
        Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
        stage.getIcons().add(new Image("/resources/icons/error.png"));
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
