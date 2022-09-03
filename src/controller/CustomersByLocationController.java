package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import model.CustomerLocationReport;
import utilities.CustomerQuery;

import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class CustomersByLocationController implements Initializable {
    
    @FXML
    private RadioButton byCountryRadio;
    @FXML
    private RadioButton bySubdivisionRadio;
    @FXML
    private ToggleGroup locationRadio;
    @FXML
    private TableView<CustomerLocationReport> customerInsightsTable;
    @FXML
    TableColumn<CustomerLocationReport, String> locationColumn;
    @FXML
    TableColumn<CustomerLocationReport, Integer> totalColumn;
    
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        populateTableView();
        byCountryRadio.setSelected(true);
    }

    /**
     * This method populates the customerInsightsTable by using a specialized SQL query and CustomerLocationReport object
     */
    public void populateTableView() {
        try {
            customerInsightsTable.setItems(CustomerQuery.getCustomersByCountry());
            locationColumn.setCellValueFactory(new PropertyValueFactory<>("customerLocation"));
            totalColumn.setCellValueFactory(new PropertyValueFactory<>("totalCustomers"));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * This populates the table with customers grouped by country
     * @param actionEvent is unused and was auto-generated
     */
    public void showCustomersByCountry(ActionEvent actionEvent) {
        try {
            customerInsightsTable.setItems(CustomerQuery.getCustomersByCountry());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * This populates the table with customers grouped by subdivision
     * @param actionEvent is unused and was auto-generated
     */
    public void showCustomersBySubdivision(ActionEvent actionEvent) {
        try {
            customerInsightsTable.setItems(CustomerQuery.getCustomersByDivision());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
