package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import model.Customer;
import utilities.CustomerQuery;

import java.net.URL;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ResourceBundle;

public class UpdateCustomerController implements Initializable {

    @FXML
    private TextField nameField;
    @FXML
    private TextField addressField;
    @FXML
    private TextField postalCodeField;
    @FXML
    private TextField phoneField;
    @FXML
    private ComboBox<String> countryCombo;
    @FXML
    private ComboBox<String> divisionCombo;

    @FXML
    private Button cancelButton;
    private Customer customerToUpdate;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        customerToUpdate = CustomerScreenController.getSelectedCustomer();
        populateCustomerData();
    }

    /**
     * This method validates the data entered into every text field and combo box, then updates a given Customer
     * entry in the database
     * @param actionEvent is used to close the window if the Customer was successfully added to the database
     */
    public void updateCustomer(ActionEvent actionEvent) throws SQLException {
        int id = customerToUpdate.getCustomerId();
        String name = nameField.getText();
        String address = addressField.getText();
        String postalCode = postalCodeField.getText();
        String phone = phoneField.getText();
        Timestamp lastUpdate = Timestamp.valueOf(LocalDateTime.now());
        String lastUpdatedBy = LoginScreenController.getCurrentUser().getUsername();
        int divisionId = CustomerQuery.getDivisionIdFromName(divisionCombo.getValue());

        //validate all the data
        if (name.equals("") || address.equals("") || postalCode.equals("") || phone.equals("")) {
            errorMessageDialogue("Error: Invalid Data", "Please enter valid data or make a selection for each field and try again.");
        } else if (divisionId == -1) {
            errorMessageDialogue("Error", "An error occurred while creating customer. Please close \"New Customer\" window and try again");
        } else {
            try {
                CustomerQuery.updateCustomer(id, name, address, postalCode, phone, lastUpdate, lastUpdatedBy, divisionId);
                System.out.println("Existing customer successfully updated");
                closeWindow(actionEvent);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * This method is used to populate the text fields and combo boxes based on the data passed by the static
     * selectedCustomer object
     */
    public void populateCustomerData() {
        try {
            countryCombo.getItems().addAll(CustomerQuery.getAllCountries());
        } catch (SQLException e) {
            e.printStackTrace();
        }

        nameField.setText(customerToUpdate.getName());
        addressField.setText(customerToUpdate.getAddress());
        postalCodeField.setText(customerToUpdate.getPostalCode());
        phoneField.setText(customerToUpdate.getPhone());
        countryCombo.setValue(customerToUpdate.getCountry());

        populateDivisionCombo();
        try {
            divisionCombo.setValue(CustomerQuery.getDivisionName(customerToUpdate.getDivisionId()));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * This method is used to populate the Division ComboBox depending on which Country the user has selected in the
     * first ComboBox
     */
    public void populateDivisionCombo() {
        String currentCountry = countryCombo.getValue();
        try {
            divisionCombo.setItems(CustomerQuery.getSubDivisions(currentCountry));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * This method closes the UpdateCustomerScreen if a user wishes to cancel updating a Customer
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
