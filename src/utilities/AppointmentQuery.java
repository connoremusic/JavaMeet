package utilities;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.Appointment;
import model.AppointmentMonthReport;
import model.AppointmentTypeReport;
import model.Contact;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;

public abstract class AppointmentQuery {

    /**
     * This method inserts a new Appointment object into the database via a PreparedStatement
     * @param title
     * @param description
     * @param location
     * @param type
     * @param start
     * @param end
     * @param createTime
     * @param createdBy
     * @param lastUpdate
     * @param lastUpdateBy
     * @param customerId
     * @param userId
     * @param contactId
     * @return true or false depending on if the SQL INSERT statement was successful
     * @throws SQLException
     */
    public static boolean addAppointment(String title, String description, String location, String type, Timestamp start, Timestamp end, Timestamp createTime,
                             String createdBy, Timestamp lastUpdate, String lastUpdateBy, int customerId, int userId, int contactId) throws SQLException {
        String sqlStatement = "INSERT INTO appointments (Title, Description, Location, Type, Start, End, Create_Date, Created_By, Last_Update, Last_Updated_By, Customer_ID, User_ID, Contact_ID) " +
                "VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        PreparedStatement preparedStatement = JDBC.getConnection().prepareStatement(sqlStatement);

        //set the parameters of the query
        preparedStatement.setString(1, title);
        preparedStatement.setString(2, description);
        preparedStatement.setString(3, location);
        preparedStatement.setString(4, type);
        preparedStatement.setTimestamp(5, start);
        preparedStatement.setTimestamp(6, end);
        preparedStatement.setTimestamp(7, createTime);
        preparedStatement.setString(8, createdBy);
        preparedStatement.setTimestamp(9, lastUpdate);
        preparedStatement.setString(10, lastUpdateBy);
        preparedStatement.setInt(11, customerId);
        preparedStatement.setInt(12, userId);
        preparedStatement.setInt(13, contactId);

        try {
            preparedStatement.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * This method returns all Appointment objects in the database as an ObservableList, which can then be loaded into
     * a TableView
     * @return returns an ObservableList of all the appointments in the database
     * @throws SQLException
     */
    public static ObservableList<Appointment> getAllAppointments() throws SQLException {
        ObservableList<Appointment> allAppointments = FXCollections.observableArrayList();
        String sqlStatement = "SELECT * FROM appointments";
        PreparedStatement preparedStatement = JDBC.getConnection().prepareStatement(sqlStatement);

        try {
             ResultSet resultSet = preparedStatement.executeQuery();
             while (resultSet.next()) {
                 int appointmentID = resultSet.getInt("Appointment_ID");
                 String title = resultSet.getString("Title");
                 String description = resultSet.getString("Description");
                 String location = resultSet.getString("Location");
                 String type = resultSet.getString("Type");
                 Timestamp startDateTime = resultSet.getTimestamp("Start");
                 Timestamp endDateTime = resultSet.getTimestamp("End");
                 Timestamp createdDate = resultSet.getTimestamp("Create_Date");
                 String createdBy = resultSet.getString("Created_By");
                 Timestamp lastUpdateDateTime = resultSet.getTimestamp("Last_Update");
                 String lastUpdatedBy = resultSet.getString("Last_Updated_By");
                 int customerID = resultSet.getInt("Customer_ID");
                 int userID = resultSet.getInt("User_ID");
                 int contactID = resultSet.getInt("Contact_ID");

                 Appointment appointment = new Appointment(appointmentID, title, description, location, type,
                         startDateTime, endDateTime, createdDate, createdBy, lastUpdateDateTime, lastUpdatedBy,
                         customerID, userID, contactID);
                 allAppointments.add(appointment);
             }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return allAppointments;
    }

    /**
     * This method returns all Appointment objects in the database as an ObservableList, which can then be loaded into
     * a TableView
     * @return returns an ObservableList of all the appointments in the database
     * @throws SQLException
     */
    public static ObservableList<Appointment> getAllAppointments(Contact contact) throws SQLException {
        ObservableList<Appointment> allAppointments = FXCollections.observableArrayList();
        String sqlStatement = "SELECT * FROM appointments WHERE Contact_ID = ?";
        PreparedStatement preparedStatement = JDBC.getConnection().prepareStatement(sqlStatement);

        //set the parameters of the query
        preparedStatement.setInt(1, contact.getContactId());

        try {
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                int appointmentID = resultSet.getInt("Appointment_ID");
                String title = resultSet.getString("Title");
                String description = resultSet.getString("Description");
                String location = resultSet.getString("Location");
                String type = resultSet.getString("Type");
                Timestamp startDateTime = resultSet.getTimestamp("Start");
                Timestamp endDateTime = resultSet.getTimestamp("End");
                Timestamp createdDate = resultSet.getTimestamp("Create_Date");
                String createdBy = resultSet.getString("Created_By");
                Timestamp lastUpdateDateTime = resultSet.getTimestamp("Last_Update");
                String lastUpdatedBy = resultSet.getString("Last_Updated_By");
                int customerID = resultSet.getInt("Customer_ID");
                int userID = resultSet.getInt("User_ID");
                int contactID = resultSet.getInt("Contact_ID");

                Appointment appointment = new Appointment(appointmentID, title, description, location, type,
                        startDateTime, endDateTime, createdDate, createdBy, lastUpdateDateTime, lastUpdatedBy,
                        customerID, userID, contactID);
                allAppointments.add(appointment);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return allAppointments;
    }

    /**
     * This method returns all AppointmentTypeReport objects in a databased based on their type
     * @return returns an ObservableList of AppointmentTypeReport objects
     * @throws SQLException
     */
    public static ObservableList<AppointmentTypeReport> getAppointmentsByType() throws SQLException {
        ObservableList<AppointmentTypeReport> appointmentsByType = FXCollections.observableArrayList();
        String sqlStatement = "SELECT Type, COUNT(Type) AS Total FROM appointments GROUP BY Type";
        PreparedStatement preparedStatement = JDBC.getConnection().prepareStatement(sqlStatement);

        try {
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                String type = resultSet.getString("Type");
                int total = resultSet.getInt("Total");

                AppointmentTypeReport appointmentTypeReport = new AppointmentTypeReport(type, total);
                appointmentsByType.addAll(appointmentTypeReport);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return appointmentsByType;
    }

    /**
     * This method returns the total number of appointments grouped by month (and year) from the database via a
     * PreparedStatement. The information is returned as AppointmentMonthReport objects for easy viewing in TableViews.
     * @return returns an ObservableList of AppointmentMonthReport objects
     * @throws SQLException
     */
    public static ObservableList<AppointmentMonthReport> getAppointmentsByMonth() throws SQLException {
        ObservableList<AppointmentMonthReport> appointmentsByMonth = FXCollections.observableArrayList();
        String sqlStatement = "SELECT CONCAT(YEAR(Start), '-', MONTH (Start)) AS Month, COUNT(Appointment_ID) AS Total FROM appointments GROUP BY Month ORDER BY Month";
        PreparedStatement preparedStatement = JDBC.getConnection().prepareStatement(sqlStatement);

        try {
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                String month = resultSet.getString("Month");
                int total = resultSet.getInt("Total");

                AppointmentMonthReport appointmentMonthReport = new AppointmentMonthReport(month, total);
                appointmentsByMonth.addAll(appointmentMonthReport);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return appointmentsByMonth;
    }

    /**
     * This method returns all Appointment objects in the database within a certain range window via an SQL query
     * @param startRange the Timestamp of the start of the window to search for appointments
     * @param endRange the Timestamp of the end of the window to search for appointments
     * @return returns an ObservableList of all appointments that fall in the search window
     * @throws SQLException
     */
    public static ObservableList<Appointment> getRangeAppointments(Timestamp startRange, Timestamp endRange) throws SQLException {
        ObservableList<Appointment> filteredAppointments = FXCollections.observableArrayList();
        String sqlStatement = "SELECT * FROM appointments WHERE Start BETWEEN ? AND ?";
        PreparedStatement preparedStatement = JDBC.getConnection().prepareStatement(sqlStatement);

        //set the parameters of the query
        preparedStatement.setTimestamp(1, startRange);
        preparedStatement.setTimestamp(2, endRange);

        try {
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                int appointmentID = resultSet.getInt("Appointment_ID");
                String title = resultSet.getString("Title");
                String description = resultSet.getString("Description");
                String location = resultSet.getString("Location");
                String type = resultSet.getString("Type");
                Timestamp startDateTime = resultSet.getTimestamp("Start");
                Timestamp endDateTime = resultSet.getTimestamp("End");
                Timestamp createdDate = resultSet.getTimestamp("Create_Date");
                String createdBy = resultSet.getString("Created_By");
                Timestamp lastUpdateDateTime = resultSet.getTimestamp("Last_Update");
                String lastUpdatedBy = resultSet.getString("Last_Updated_By");
                int customerID = resultSet.getInt("Customer_ID");
                int userID = resultSet.getInt("User_ID");
                int contactID = resultSet.getInt("Contact_ID");

                Appointment appointment = new Appointment(appointmentID, title, description, location, type,
                        startDateTime, endDateTime, createdDate, createdBy, lastUpdateDateTime, lastUpdatedBy,
                        customerID, userID, contactID);
                filteredAppointments.add(appointment);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return filteredAppointments;
    }

    /**
     * This method deletes an appointment from the database based on Appointment_ID
     * @param id the ID of the appointment intended to be deleted
     * @return returns true or false depending on if the deletion was successful
     * @throws SQLException
     */
    public static boolean deleteAppointment(int id) throws SQLException {
        String sqlStatement = "DELETE FROM appointments WHERE Appointment_ID = ?";
        PreparedStatement preparedStatement = JDBC.getConnection().prepareStatement(sqlStatement);

        //set the parameters of the query
        preparedStatement.setInt(1, id);

        try {
            preparedStatement.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }

    }

    /**
     * This method uses an SQL statement to append Appointment objects in the database
     * @param id
     * @param title
     * @param description
     * @param location
     * @param type
     * @param start
     * @param end
     * @param lastUpdate
     * @param lastUpdateBy
     * @param customerId
     * @param userId
     * @param contactId
     * @return returns true or false depending on if the update was successful
     * @throws SQLException
     */
    public static boolean updateAppointment(int id, String title, String description, String location, String type, Timestamp start, Timestamp end, Timestamp lastUpdate, String lastUpdateBy, int customerId, int userId, int contactId) throws SQLException {
        String sqlStatement = "UPDATE appointments SET Title = ?, Description = ?, Location = ?, Type = ?, " +
                "Start = ?, End = ?, Last_Update = ?, Last_Updated_By = ?, " +
                "Customer_ID = ?, User_ID = ?, Contact_ID = ? WHERE Appointment_ID = ?";
        PreparedStatement preparedStatement = JDBC.getConnection().prepareStatement(sqlStatement);

        //set the parameters of the query
        preparedStatement.setString(1, title);
        preparedStatement.setString(2, description);
        preparedStatement.setString(3, location);
        preparedStatement.setString(4, type);
        preparedStatement.setTimestamp(5, start);
        preparedStatement.setTimestamp(6, end);
        preparedStatement.setTimestamp(7, lastUpdate);
        preparedStatement.setString(8, lastUpdateBy);
        preparedStatement.setInt(9, customerId);
        preparedStatement.setInt(10, userId);
        preparedStatement.setInt(11, contactId);
        preparedStatement.setInt(12, id);

        try {
            preparedStatement.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * This method returns true if any two time periods are overlapping
     * @param start1 start time for meeting 1
     * @param end1 end time for meeting 1
     * @param start2 start time for meeting 2
     * @param end2 end time for meeting 2
     * @return returns a boolean whether two meetings are overlapping
     */
    public static boolean isOverlapping(LocalDateTime start1, LocalDateTime end1, LocalDateTime start2, LocalDateTime end2) {
        return (start1.isBefore(end2) && start2.isBefore(end1));
    }
}
