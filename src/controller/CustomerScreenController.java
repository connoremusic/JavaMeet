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
import model.Customer;
import utilities.CustomerQuery;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
import java.util.ResourceBundle;

public class CustomerScreenController implements Initializable {

    private Stage stage;
    private Parent root;

    private double x,y = 0;
    @FXML
    AnchorPane anchorPane;
    @FXML
    private TableView<Customer> customerTable;
    @FXML
    private TableColumn<Customer, Integer> idColumn;
    @FXML
    private TableColumn<Customer, String> nameColumn;
    @FXML
    private TableColumn<Customer, String> phoneColumn;
    @FXML
    private TableColumn<Customer, String> countryColumn;
    @FXML
    private TableColumn<Customer, String> divisionColumn;
    @FXML
    private TableColumn<Customer, LocalDateTime> joinDateColumn;
    @FXML
    private RadioButton showAllRadio;
    @FXML
    private RadioButton showNewRadio;
    @FXML
    private Button exitButton;
    private static Customer selectedCustomer;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        populateTableView();
        showAllRadio.setSelected(true);
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
     * This method is unused here because the program is already displaying the CustomerScreen
     * @param actionEvent is unused and was auto-generated
     */
    public void showCustomerScreen(ActionEvent actionEvent) {
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
     * This method uses an SQL query to populate the customerTable with all customer data in the database
     * @param actionEvent
     */
    public void showAllCustomers(ActionEvent actionEvent) {
        try {
            customerTable.setItems(CustomerQuery.getAllCustomers());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * This method uses an SQL query to populate the customerTable only with customers added to the database
     * recently. The date range that is considered to be "new" can be altered in the CustomerQuery class
     * @param actionEvent is unused and was auto-generated
     */
    public void showNewCustomers(ActionEvent actionEvent) {
        try {
            customerTable.setItems(CustomerQuery.getNewCustomers());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * This method creates a popup window showing all data for a selected customer. The data is passed to the new
     * window via the static selectedCustomer Customer object
     * @param actionEvent is unused and was auto-generated
     */
    public void showDetailsScreen(ActionEvent actionEvent) {
        selectedCustomer = customerTable.getSelectionModel().getSelectedItem();
        if (selectedCustomer == null) {
            errorMessageDialogue("Error: No Customer Selected", "Please select a customer to inspect and try again");
        } else {
            try {
                Stage detailsStage = new Stage(StageStyle.DECORATED);
                FXMLLoader fxmlLoader = new FXMLLoader();
                Pane root = fxmlLoader.load(getClass().getResource("/view/CustomerDetailsScreen.fxml"));
                detailsStage.setScene(new Scene(root));
                detailsStage.setResizable(false);
                detailsStage.setTitle("Customer Details");
                detailsStage.initOwner(stage);
                detailsStage.initModality(Modality.APPLICATION_MODAL);
                detailsStage.showAndWait();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * This method creates a popup window where users can alter data for an existing customer in the database. The info
     * is passed to this window via the static selectedCustomer Customer object. Finally, the method refreshes the
     * customerTable when the window is closed to reflect any changes made, if any, to the database
     * @param actionEvent is unused and was auto-generated
     */
    public void showUpdateCustomerScreen(ActionEvent actionEvent) {
        selectedCustomer = customerTable.getSelectionModel().getSelectedItem();
        if (selectedCustomer == null) {
            errorMessageDialogue("Error: No Customer Selected", "Please select a customer to update and try again");
        } else {
            try {
                Stage updateStage = new Stage(StageStyle.DECORATED);
                FXMLLoader fxmlLoader = new FXMLLoader();
                Pane root = fxmlLoader.load(getClass().getResource("/view/UpdateCustomerScreen.fxml"));
                updateStage.setScene(new Scene(root));
                updateStage.setResizable(false);
                updateStage.setTitle("Update Customer");
                updateStage.initOwner(stage);
                updateStage.initModality(Modality.APPLICATION_MODAL);
                updateStage.showAndWait();
                customerTable.setItems(CustomerQuery.getAllCustomers());
            } catch (IOException e) {
                e.printStackTrace();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }

    /**
     * This method creates a popup window where users can create a new Customer object that will also be added to the
     * database. When the window is closed, the customerTable is refreshed to reflect any additions, if any, made to
     * the database
     * @param actionEvent is unused and was auto-generated
     */
    public void showNewCustomerScreen(ActionEvent actionEvent) {
        try {
            Stage addStage = new Stage(StageStyle.DECORATED);
            FXMLLoader fxmlLoader = new FXMLLoader();
            Pane root = fxmlLoader.load(getClass().getResource("/view/NewCustomerScreen.fxml"));
            addStage.setScene(new Scene(root));
            addStage.setResizable(false);
            addStage.setTitle("New Customer");
            addStage.initOwner(stage);
            addStage.initModality(Modality.APPLICATION_MODAL);
            addStage.showAndWait();
            customerTable.setItems(CustomerQuery.getAllCustomers());
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * This method will delete an existing Customer from the database and refresh the customerTable to reflect the change.
     * An error message is displayed if the user does not select a customer to delete. In addition, a Customer object
     * cannot be deleted if that Customer still has any Appointment objects associated with it.
     * @param actionEvent is unused and was auto-generated
     */
    public void deleteCustomer(ActionEvent actionEvent) {
        Customer selectedCustomer = customerTable.getSelectionModel().getSelectedItem();
        if (selectedCustomer != null) {
                if (confirmDelete("Warning! Selected Customer Will By Deleted", "Are you sure you want to delete this customer?")) {
                    int id = selectedCustomer.getCustomerId();
                    try {
                        CustomerQuery.deleteCustomerAppointments(id);
                        CustomerQuery.deleteCustomer(id);
                        customerTable.setItems(CustomerQuery.getAllCustomers());
                        System.out.println("Customer with ID " + id + " and all associated appointments deleted successfully");
                        popupMessageDialogue("Customer Deleted", "Customer record with ID " + id + " and all associated appointments deleted successfully.");
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
        } else {
            errorMessageDialogue("Error: No Customer Selected", "No customer selected. Please select a customer to delete and try again.");
        }
    }

    /**
     * This method is used to populate the customerTable when the CustomerScreen is first initialized.
     *
     * This function also implements a lambda function to help format the joinDateColumn to a more readable form
     */
    public void populateTableView() {
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM dd yyyy");

            customerTable.setItems(CustomerQuery.getAllCustomers());
            idColumn.setCellValueFactory(new PropertyValueFactory<>("customerId"));
            nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
            phoneColumn.setCellValueFactory(new PropertyValueFactory<>("phone"));
            countryColumn.setCellValueFactory(new PropertyValueFactory<>("country"));
            divisionColumn.setCellValueFactory(new PropertyValueFactory<>("divisionName"));
            joinDateColumn.setCellValueFactory(new PropertyValueFactory<>("joinDate"));

            //this formats the Customer join date while still retaining correct sorting properties
            joinDateColumn.setCellFactory(tc -> new TableCell<Customer, LocalDateTime>() {
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
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * This public method allows other screen classes to access a selectedCustomer object, either for viewing Customer
     * details or for updating Customer data
     * @return returns the static selectedCustomer object
     */
    public static Customer getSelectedCustomer() {
        return selectedCustomer;
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
     * This method is used to create a popup window when a user wants to delete a Customer object
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
