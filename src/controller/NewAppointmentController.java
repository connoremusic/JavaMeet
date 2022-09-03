package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
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
import java.time.*;
import java.util.ResourceBundle;

public class NewAppointmentController implements Initializable {

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


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        populateComboBoxes();
    }

    /**
     * This method gets all the data entered in the text fields and validates it. Error messages are displayed when
     * appropriate, and if the data is valid, this method will add an Appointment object to the database.
     * @param actionEvent is used to close the window if everything was correct and the database was updated
     */
    public void addAppointment(ActionEvent actionEvent) {
        String title = titleField.getText();
        Customer customer = customerCombo.getValue();
        String description = descriptionField.getText();
        String location = locationField.getText();
        String type = typeCombo.getSelectionModel().getSelectedItem();
        LocalDate startDate = startDatePicker.getValue();
        LocalDate endDate = endDatePicker.getValue();
        Timestamp createDate = Timestamp.valueOf(LocalDateTime.now());
        String createdBy = LoginScreenController.getCurrentUser().getUsername();
        Timestamp lastUpdate = Timestamp.valueOf(LocalDateTime.now());
        String lastUpdateBy = LoginScreenController.getCurrentUser().getUsername();
        int userId = LoginScreenController.getCurrentUser().getUserId();
        Contact contact = contactCombo.getValue();

        //validate all the entered data
        if (title.equals("") || description.equals("") || location.equals("") || type.equals("") || startDate == null ||
            startHourComboBox.getValue() == null || startMinuteComboBox.getValue() == null || endDate == null ||
            endHourComboBox.getValue() == null || endMinuteComboBox.getValue() == null ||
            createdBy.equals("") || customer == null || contact == null) {
            errorMessageDialogue("Error: Invalid Data", "Please enter valid data or make a selection for each field and try again.");
        } else {
            Timestamp startTime = Timestamp.valueOf(startDatePicker.getValue().atTime(startHourComboBox.getValue(), startMinuteComboBox.getValue()));
            Timestamp endTime = Timestamp.valueOf(endDatePicker.getValue().atTime(endHourComboBox.getValue(), endMinuteComboBox.getValue()));
            int customerId = customer.getCustomerId();
            int contactId = contact.getContactId();

            try {
                boolean overlappingFlag = false;

                //checks for any overlapping appointments
                for (Appointment appointment : AppointmentQuery.getAllAppointments()) {
                    if ((appointment.getCustomerId() == customerId) && (AppointmentQuery.isOverlapping(appointment.getStartTime().toLocalDateTime(),
                            appointment.getEndTime().toLocalDateTime(), startTime.toLocalDateTime(), endTime.toLocalDateTime()))) {
                        overlappingFlag = true;
                        break;
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
                    AppointmentQuery.addAppointment(title, description, location, type, startTime, endTime, createDate,
                            createdBy, lastUpdate, lastUpdateBy, customerId, userId, contactId);

                    System.out.println("New appointment successfully added.");
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
     * This method is used to populate all the combo boxes when the NewAppointmentScreen is first initialized
     */
    public void populateComboBoxes() {
        typeCombo.getItems().addAll("New Client", "Annual Report", "Project Milestones", "Goals", "Planning Session", "De-Briefing");

        for (int i = 0; i < 24; i++) {
            startHourComboBox.getItems().add(i);
            endHourComboBox.getItems().add(i);
        }
        for (int i = 0; i <60; i+=5) {
            startMinuteComboBox.getItems().add(i);
            endMinuteComboBox.getItems().add(i);
        }

        try {
            for (Contact contact : ContactQuery.getAllContacts()) {
                contactCombo.getItems().addAll(contact);
            }
            for (Customer customer : CustomerQuery.getAllCustomers()) {
                customerCombo.getItems().addAll(customer);
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
     * This method is used to display an error message to the user if anything goes wrong
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
