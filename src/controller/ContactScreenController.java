package controller;

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
import model.Contact;
import utilities.AppointmentQuery;
import utilities.ContactQuery;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
import java.util.ResourceBundle;

public class ContactScreenController implements Initializable {

    private Stage stage;
    private Parent root;

    private double x, y = 0;

    @FXML
    AnchorPane anchorPane;
    @FXML
    private Button exitButton;
    @FXML
    private TableView<Appointment> scheduleTable;
    @FXML
    private TableColumn<Appointment, Integer> idColumn;
    @FXML
    private TableColumn<Appointment, String> titleColumn;
    @FXML
    private TableColumn<Appointment, Integer> customerIdColumn;
    @FXML
    private TableColumn<Appointment, String> locationColumn;
    @FXML
    private TableColumn<Appointment, String> typeColumn;
    @FXML
    private TableColumn<Appointment, LocalDateTime> dateColumn;
    @FXML
    private TableColumn<Appointment, String> timeColumn;
    @FXML
    private ComboBox<Contact> contactComboBox;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        populateTableView();
        populateComboBox();
    }

    /**
     * This method populates the scheduleTable when the ContactScreen is first initialized.
     *
     * Lambda Function #2
     * This method also implements a lambda function to help format the dateColumn into a more readable format. This
     * improves the code because this function only needs to exist inside this table formatting function.
     */
    public void populateTableView() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM dd yyyy");

        idColumn.setCellValueFactory(new PropertyValueFactory<>("appointmentId"));
        titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        customerIdColumn.setCellValueFactory(new PropertyValueFactory<>("customerId"));
        locationColumn.setCellValueFactory(new PropertyValueFactory<>("location"));
        typeColumn.setCellValueFactory(new PropertyValueFactory<>("meetingType"));

        dateColumn.setCellValueFactory(new PropertyValueFactory<>("startDateFormatted"));

        //this formats the date column to show the month while still retaining correct sorting properties
        dateColumn.setCellFactory(tc -> new TableCell<>() {
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

        timeColumn.setCellValueFactory(new PropertyValueFactory<>("startTimeFormatted"));
        scheduleTable.getSortOrder().add(dateColumn);
    }

    /**
     * This method populates the contactComboBox with the names of all Contacts found in the database
     */
    public void populateComboBox() {
        try {
            contactComboBox.setItems(ContactQuery.getAllContacts());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * This method sets the items of the scheduleTable based on which Contact is currently selected in the contactComboBox
     */
    public void setContactTableView() {
        Contact selectedContact = contactComboBox.getSelectionModel().getSelectedItem();
        try {
            scheduleTable.setItems(AppointmentQuery.getAllAppointments(selectedContact));
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
     * This method loads the ScheduleScreen in the existing stage for a cleaner UI look
     * @param actionEvent is used to inform the method of the correct stage in which to load the new ScheduleScreen scene
     * @throws IOException
     */
    public void showScheduleScreen(ActionEvent actionEvent) throws IOException {
        root = FXMLLoader.load(getClass().getResource("/view/ScheduleScreen.fxml"));
        stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        Scene scheduleScene = new Scene(root);
        stage.setScene(scheduleScene);
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
     * This method is unused here because the program is already displaying the ContactScreen
     * @param actionEvent is unused and was auto-generated
     */
    public void showContactScreen(ActionEvent actionEvent) {

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
     * This method creates a popup window showing the full details of a selected Appointment. The data is passed to the
     * new window via the static selectedAppointment Appointment object. If no Appointment is selected, an error
     * message is displayed.
     */
    public void showDetailsScreen() {
        ScheduleScreenController.setSelectedAppointment(scheduleTable.getSelectionModel().getSelectedItem());
        if (ScheduleScreenController.getSelectedAppointment() == null) {
            errorMessageDialogue("Error: No Appointment Selected", "Please select an appointment to inspect and try again");
        } else {
            try {
                Stage detailsStage = new Stage(StageStyle.DECORATED);
                FXMLLoader fxmlLoader = new FXMLLoader();
                Pane root = fxmlLoader.load(getClass().getResource("/view/AppointmentDetailsScreen.fxml"));
                detailsStage.setScene(new Scene(root));
                detailsStage.setResizable(false);
                detailsStage.setTitle("Appointment Details");
                detailsStage.initOwner(stage);
                detailsStage.initModality(Modality.APPLICATION_MODAL);
                detailsStage.showAndWait();
            } catch (IOException e) {
                e.printStackTrace();
            }
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
