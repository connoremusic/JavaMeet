package controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import model.Appointment;
import utilities.AppointmentQuery;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
import java.util.ResourceBundle;

public class ScheduleScreenController implements Initializable {
    private Stage stage;
    private Parent root;
    private Stage addStage;

    private double x, y = 0;

    @FXML
    AnchorPane anchorPane;
    @FXML
    private Button exitButton;
    @FXML
    private RadioButton showAllRadio;
    @FXML
    private TableView<Appointment> scheduleTable;
    @FXML
    private TableColumn<Appointment, Integer> idColumn;
    @FXML
    private TableColumn<Appointment, String> titleColumn;
    @FXML
    private TableColumn<Appointment, String> descriptionColumn;
    @FXML
    private TableColumn<Appointment, String> locationColumn;
    @FXML
    private TableColumn<Appointment, String> contactColumn;
    @FXML
    private TableColumn<Appointment, String> typeColumn;
    @FXML
    private TableColumn<Appointment, LocalDateTime> startDateColumn;
    @FXML
    private TableColumn<Appointment, LocalDateTime> endDateColumn;
    @FXML
    private TableColumn<Appointment, Integer> customerIdColumn;
    @FXML
    private TableColumn<Appointment, Integer> userIdColumn;
    @FXML
    private TableColumn<Appointment, LocalDateTime> dateColumn;
    @FXML
    private TableColumn<Appointment, String> timeColumn;
    private static boolean firstLoginFlag = true;
    private static Appointment selectedAppointment = null;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        populateTableView();
        showAllRadio.setSelected(true);
        if (firstLoginFlag) {
            firstLoginPopup();
            firstLoginFlag = false;
        }
    }

    /**
     * This method populates the scheduleTable when the ScheduleScreen is first initialized.
     *
     * Lambda Function #1
     * This method also uses a lambda function to format the dateColumn into a format that is far more readable. The
     * lambda function improves the code because this function only needs to exist in this one spot to help format
     * the date.
     */
    public void populateTableView() {
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM dd yyyy hh:mm a");

            scheduleTable.setItems(AppointmentQuery.getAllAppointments());
            idColumn.setCellValueFactory(new PropertyValueFactory<>("appointmentId"));
            titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
            descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));
            locationColumn.setCellValueFactory(new PropertyValueFactory<>("location"));
            contactColumn.setCellValueFactory(new PropertyValueFactory<>("contactName"));
            typeColumn.setCellValueFactory(new PropertyValueFactory<>("meetingType"));
            customerIdColumn.setCellValueFactory(new PropertyValueFactory<>("customerId"));
            userIdColumn.setCellValueFactory(new PropertyValueFactory<>("userId"));

            startDateColumn.setCellValueFactory(new PropertyValueFactory<>("startDateFormatted"));

            //this formats the startDateColumn while still retaining correct sorting properties
            startDateColumn.setCellFactory(tc -> new TableCell<>() {
                @Override
                protected void updateItem(LocalDateTime date, boolean empty) {
                    super.updateItem(date, empty);
                    if (empty) {
                        setText(null);
                    } else {
                        setText(formatter.format(date));
                    }
                }
            });

            endDateColumn.setCellValueFactory(new PropertyValueFactory<>("endDateFormatted"));

            //this formats the endDateColumn while still retaining correct sorting properties
            endDateColumn.setCellFactory(tc -> new TableCell<>() {
                @Override
                protected void updateItem(LocalDateTime date, boolean empty) {
                    super.updateItem(date, empty);
                    if (empty) {
                        setText(null);
                    } else {
                        setText(formatter.format(date));
                    }
                }
            });

            scheduleTable.getSortOrder().add(startDateColumn);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * This method closes the program, which will also close the database connection via the main() method ending
     * @param actionEvent is unused and was auto-generated
     */
    public void exitProgram(ActionEvent actionEvent) {
        Alert alert = new Alert(Alert.AlertType.NONE, "Are you sure you want to exit?");
        alert.getDialogPane().getButtonTypes().add(ButtonType.OK);
        alert.getDialogPane().getButtonTypes().add(ButtonType.CANCEL);
        stage = (Stage) alert.getDialogPane().getScene().getWindow();
        stage.getIcons().add(new Image("/resources/images/java_meet_logo_navy.png"));
        Optional<ButtonType> buttonType = alert.showAndWait();
        if (buttonType.get() == ButtonType.OK) {
            Stage stage = (Stage) exitButton.getScene().getWindow();
            stage.close();
        }
    }

    /**
     * This method displays an error message to the user if they have not selected an Appointment from the TableView.
     * Otherwise, it uses an SQL update to delete the Appointment and then refresh the TableView to reflect the change.
     */
    public void deleteAppointment() {
        Appointment appointmentToDelete = scheduleTable.getSelectionModel().getSelectedItem();
        if (appointmentToDelete != null) {
            if (confirmDelete("Warning! Selected Appointment Will By Deleted", "Are you sure you want to delete this appointment?")) {
                int id = appointmentToDelete.getAppointmentId();
                String appointmentType = appointmentToDelete.getMeetingType();
                try {
                    AppointmentQuery.deleteAppointment(id);
                    scheduleTable.setItems(AppointmentQuery.getAllAppointments());
                    scheduleTable.getSortOrder().add(startDateColumn);
                    System.out.println("Appointment with ID " + id + " of meeting type \"" + appointmentType + "\" deleted successfully");
                    popupMessageDialogue("Appointment Deleted", "Appointment with ID " + id + " of meeting type \"" + appointmentType + "\" deleted successfully.");
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        } else {
            errorMessageDialogue("Error: No Appointment Selected", "No appointment selected. Please select an appointment to delete and try again.");
        }
    }

    /**
     * This method creates a popup window where the user can append an Appointment object in the database. If no
     * appointment is selected, an error message is displayed.
     */
    public void showUpdateAppointmentScreen() {
        selectedAppointment = scheduleTable.getSelectionModel().getSelectedItem();
        if (selectedAppointment == null) {
            errorMessageDialogue("Error: No Appointment Selected", "Please select an appointment to update and try again");
        } else {
            try {
                addStage = new Stage(StageStyle.DECORATED);
                FXMLLoader fxmlLoader = new FXMLLoader();
                Pane root = fxmlLoader.load(getClass().getResource("/view/UpdateAppointmentScreen.fxml"));
                addStage.setScene(new Scene(root));
                addStage.setResizable(false);
                addStage.setTitle("Update Appointment");
                addStage.initOwner(stage);
                addStage.initModality(Modality.APPLICATION_MODAL);
                addStage.showAndWait();
                scheduleTable.setItems(AppointmentQuery.getAllAppointments());
                scheduleTable.getSortOrder().add(startDateColumn);
            } catch (IOException | SQLException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * This method creates a popup window showing the full details of a selected Appointment. The data is passed to the
     * new window via the static selectedAppointment Appointment object. If no Appointment is selected, an error
     * message is displayed.
     */
    public void showDetailsScreen() {
        selectedAppointment = scheduleTable.getSelectionModel().getSelectedItem();
        if (selectedAppointment == null) {
            errorMessageDialogue("Error: No Appointment Selected", "Please select an appointment to inspect and try again");
        } else {
            try {
                addStage = new Stage(StageStyle.DECORATED);
                FXMLLoader fxmlLoader = new FXMLLoader();
                Pane root = fxmlLoader.load(getClass().getResource("/view/AppointmentDetailsScreen.fxml"));
                addStage.setScene(new Scene(root));
                addStage.setResizable(false);
                addStage.setTitle("Appointment Details");
                addStage.initOwner(stage);
                addStage.initModality(Modality.APPLICATION_MODAL);
                addStage.showAndWait();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * This method creates a popup window where users can add a new appointment to the database
     */
    public void showNewAppointmentScreen() {
        try {
            addStage = new Stage(StageStyle.DECORATED);
            FXMLLoader fxmlLoader = new FXMLLoader();
            Pane root = fxmlLoader.load(getClass().getResource("/view/NewAppointmentScreen.fxml"));
            addStage.setScene(new Scene(root));
            addStage.setResizable(false);
            addStage.setTitle("New Appointment");
            addStage.initOwner(stage);
            addStage.initModality(Modality.APPLICATION_MODAL);
            addStage.showAndWait();
            scheduleTable.setItems(AppointmentQuery.getAllAppointments());
            scheduleTable.getSortOrder().add(startDateColumn);
        } catch (IOException | SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * This method is unused here because the program is already displaying the ScheduleScreen
     * @param actionEvent is unused and was auto-generated
     */
    public void showScheduleScreen(ActionEvent actionEvent) {
    }

    /**
     * This method loads the CustomerScreen in the existing stage for a cleaner UI look
     * @param actionEvent is used to inform the method of the correct stage in which to load the new Customer scene
     * @throws IOException
     */
    public void showCustomerScreen(ActionEvent actionEvent) throws IOException {
        root = FXMLLoader.load(getClass().getResource("/view/CustomerScreen.fxml"));
        stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        Scene customerScene = new Scene(root);
        stage.setScene(customerScene);
    }

    /**
     * This method loads the ContactScreen in the existing stage for a cleaner UI look
     * @param actionEvent is used to inform the method of the correct stage in which to load the new Contact scene
     * @throws IOException
     */
    public void showContactScreen(ActionEvent actionEvent) throws IOException {
        root = FXMLLoader.load(getClass().getResource("/view/ContactScreen.fxml"));
        stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        Scene customerScene = new Scene(root);
        stage.setScene(customerScene);
    }

    /**
     * This method loads the ReportScreen in the exists stage for a cleaner UI look
     * @param actionEvent is used to inform the method of the correct stage in which to load the new ReportScreen scene
     * @throws IOException
     */
    public void showReportScreen(ActionEvent actionEvent) throws IOException {
        root = FXMLLoader.load(getClass().getResource("/view/ReportScreen.fxml"));
        stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        Scene reportScene = new Scene(root);
        stage.setScene(reportScene);
    }

    /**
     * This method creates a popup window with information about the program such as the different software and
     * versions used to program everything
     * @param actionEvent is unused and was auto-generated
     * @throws IOException
     */
    public void showAboutScreen(ActionEvent actionEvent) throws IOException {
        Stage aboutStage = new Stage(StageStyle.DECORATED);
        FXMLLoader fxmlLoader = new FXMLLoader();
        Pane root = fxmlLoader.load(getClass().getResource("/view/AboutScreen.fxml"));
        aboutStage.setScene(new Scene(root));
        aboutStage.setResizable(false);
        aboutStage.setTitle("About");
        aboutStage.initOwner(stage);
        aboutStage.initModality(Modality.APPLICATION_MODAL);
        aboutStage.showAndWait();
    }

    /**
     * This method is used to pass an Appointment object to other controllers either to view details or make changes
     * to the Appointment
     * @return returns the selected Appointment object in the TableView
     */
    public static Appointment getSelectedAppointment() {
        return selectedAppointment;
    }

    public static void setSelectedAppointment(Appointment appointment) {
        selectedAppointment = appointment;
    }

    /**
     * This method uses an SQL query to set the TableView to show all appointments in the database
     */
    public void showAllAppointments() {
        try {
            scheduleTable.setItems(AppointmentQuery.getAllAppointments());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * This method uses an SQL query to set the Tableview to show only appointments that occur in the next 30 days
     */
    public void showMonthAppointments() {
        try {
            ObservableList<Appointment> filteredAppointments = FXCollections.observableArrayList();
            filteredAppointments.addAll(AppointmentQuery.getRangeAppointments(Timestamp.valueOf(LocalDateTime.now()),
                    Timestamp.valueOf(LocalDateTime.now().plusDays(30))));
            scheduleTable.setItems(filteredAppointments);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * This method uses an SQL query to set the Tableview to show only appointments that occur in the next 7 days
     */
    public void showWeekAppointments() {
        try {
            ObservableList<Appointment> filteredAppointments = FXCollections.observableArrayList();
            filteredAppointments.addAll(AppointmentQuery.getRangeAppointments(Timestamp.valueOf(LocalDateTime.now()),
                    Timestamp.valueOf(LocalDateTime.now().plusDays(7))));
            scheduleTable.setItems(filteredAppointments);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * This method is used to create a popup window when a user wants to delete an Appointment object
     * @param title sets the title of the window
     * @param content sets the message that will be displayed in the window
     * @return value is used to determine if the user clicked OK to delete or CANCEL to cancel the deletion
     */
    public boolean confirmDelete(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.NONE);
        alert.getDialogPane().getButtonTypes().add(ButtonType.OK);
        alert.getDialogPane().getButtonTypes().add(ButtonType.CANCEL);
        stage = (Stage) alert.getDialogPane().getScene().getWindow();
        stage.getIcons().add(new Image("/resources/icons/baseline_delete_forever_black_48dp.png"));
        alert.setTitle(title);
        alert.setContentText(content);
        Optional<ButtonType> buttonType = alert.showAndWait();
        return (buttonType.get() == ButtonType.OK);
    }

    /**
     * This method is used to create a generic popup window to display a message to the user
     * @param title sets the title of the window
     * @param content sets the message that will be displayed in the window
     */
    public void popupMessageDialogue(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.NONE);
        alert.getDialogPane().getButtonTypes().add(ButtonType.OK);
        stage = (Stage) alert.getDialogPane().getScene().getWindow();
        stage.getIcons().add(new Image("/resources/images/java_meet_logo_navy.png"));
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
    }

    /**
     * This method is only called the very first time the ScheduleScreen is initialized. It informs the user if they
     * have any or no appointments that occur in the next 15 minutes, and it takes into account their timezone.
     */
    public void firstLoginPopup() {
        try {
            ObservableList<Appointment> upcomingAppointments = FXCollections.observableArrayList();
            upcomingAppointments.addAll(AppointmentQuery.getRangeAppointments(Timestamp.valueOf(LocalDateTime.now()),
                    Timestamp.valueOf(LocalDateTime.now().plusMinutes(15))));

            if (upcomingAppointments.size() == 0) {
                popupMessageDialogue("No Upcoming Appointments", "You have no upcoming appointments within 15 minutes. Please check the schedule for more details.");
            } else {
                for (Appointment appointment : upcomingAppointments) {
                    popupMessageDialogue("Upcoming Appointment", "You have an appointment scheduled to start within 15 minutes.\n \nAppointment ID: " +
                            appointment.getAppointmentId() + "\nStart Date: " + appointment.getStartTime());
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * This method displays an error message to the user if something goes wrong
     * @param title sets the title of the window
     * @param content sets the message that will be displayed in the window
     */
    public void errorMessageDialogue(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.NONE);
        alert.getDialogPane().getButtonTypes().add(ButtonType.OK);
        stage = (Stage) alert.getDialogPane().getScene().getWindow();
        stage.getIcons().add(new Image("/resources/icons/error.png"));
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
    }

    /**
     * This method allows a user to drag an undecorated window around their computer screen
     * @param mouseEvent is used to determine when they drag their mouse
     */
    @FXML
    public void dragWindow(MouseEvent mouseEvent) {
        Stage stage = (Stage) anchorPane.getScene().getWindow();
        stage.setX(mouseEvent.getScreenX() - this.x);
        stage.setY(mouseEvent.getScreenY() - this.y);
    }

    /**
     * This method detects when a user has clicked an undecorated window that they wish to drag around their computer screen
     * @param mouseEvent detects when the mouse is clicked
     */
    @FXML
    public void clickWindow(MouseEvent mouseEvent) {
        this.x = mouseEvent.getSceneX();
        this.y = mouseEvent.getSceneY();
    }
}
