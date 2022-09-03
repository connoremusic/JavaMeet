package controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import model.AppointmentMonthReport;
import utilities.AppointmentQuery;

import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

public class AppointmentsByMonthController implements Initializable {

    @FXML
    private TableView<AppointmentMonthReport> appointmentInsightsTable;
    @FXML
    private TableColumn<AppointmentMonthReport, LocalDateTime> monthColumn;
    @FXML
    private TableColumn<AppointmentMonthReport, Integer> totalColumn;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        populateTableView();
    }

    /**
     * This method populates the data in the appointmentInsightsTable by using a specialized SQL query and
     * AppointmentMonthReport object.
     *
     * Lambda Function #3
     * This method also implements a lambda function in order to better format the months (by year) for readability. This
     * improves the code because this function only needs to exist inside this block of code.
     */
    public void populateTableView() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM yyyy");

        try {
            appointmentInsightsTable.setItems(AppointmentQuery.getAppointmentsByMonth());
            monthColumn.setCellValueFactory(new PropertyValueFactory<>("formattedMonth"));

            //this formats the date column to show the month while still retaining correct sorting properties
            monthColumn.setCellFactory(tc -> new TableCell<AppointmentMonthReport, LocalDateTime>() {
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

            totalColumn.setCellValueFactory(new PropertyValueFactory<>("totalAppointments"));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
