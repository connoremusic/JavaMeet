package controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import model.AppointmentTypeReport;
import utilities.AppointmentQuery;

import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class AppointmentsByTypeController implements Initializable {

    private Stage stage;
    private Scene scene;
    private Parent root;

    @FXML
    private TableView<AppointmentTypeReport> appointmentInsightsTable;
    @FXML
    private TableColumn<AppointmentTypeReport, String> typeColumn;
    @FXML
    private TableColumn<AppointmentTypeReport, Integer> totalColumn;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        populateTableView();
    }

    /**
     * This method populates the appointmentInsightsTable by using a specialized SQL query and AppointmentTypeReport object
     */
    public void populateTableView() {
        try {
            appointmentInsightsTable.setItems(AppointmentQuery.getAppointmentsByType());
            typeColumn.setCellValueFactory(new PropertyValueFactory<>("appointmentType"));
            totalColumn.setCellValueFactory(new PropertyValueFactory<>("totalAppointments"));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
