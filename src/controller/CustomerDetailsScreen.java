package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import model.Customer;

import java.net.URL;
import java.util.ResourceBundle;

public class CustomerDetailsScreen implements Initializable {

    @FXML
    private Button closeButton;

    @FXML
    private TextField idField;
    @FXML
    private TextField nameField;
    @FXML
    private TextField addressField;
    @FXML
    private TextField postalCodeField;
    @FXML
    private TextField countryField;
    @FXML
    private TextField subDivisionField;
    @FXML
    private TextField phoneField;
    @FXML
    private TextField createDateField;
    @FXML
    private TextField createdByField;
    @FXML
    private TextField lastUpdateField;
    @FXML
    private TextField lastUpdatedByField;

    private Customer selectedCustomer;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        selectedCustomer = CustomerScreenController.getSelectedCustomer();
        populateTextFields();
    }

    /**
     * This method populates all the text fields in the CustomerDetailsScreen by using data passed by a static Customer
     * object in the ScheduleScreenController
     */
    private void populateTextFields() {
        idField.setText(Integer.toString(selectedCustomer.getCustomerId()));
        nameField.setText(selectedCustomer.getName());
        addressField.setText(selectedCustomer.getAddress());
        postalCodeField.setText(selectedCustomer.getPostalCode());
        countryField.setText(selectedCustomer.getCountry());
        subDivisionField.setText(selectedCustomer.getDivisionName());
        phoneField.setText(selectedCustomer.getPhone());
        createDateField.setText(selectedCustomer.getCreateDate().toString());
        createdByField.setText(selectedCustomer.getCreatedBy());
        lastUpdateField.setText(selectedCustomer.getLastUpdate().toString());
        lastUpdatedByField.setText(selectedCustomer.getLastUpdatedBy());
    }

    public void closeWindow(ActionEvent actionEvent) {
        Stage stage = (Stage) closeButton.getScene().getWindow();
        stage.close();
    }
}
