package utilities;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.Appointment;
import model.Customer;
import model.CustomerLocationReport;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;

public class CustomerQuery {

    /**
     * This method uses a PreparedStatement to create an SQL query that adds a Customer object to the database
     * @param name
     * @param address
     * @param postalCode
     * @param phone
     * @param createDate
     * @param createdBy
     * @param lastUpdate
     * @param lastUpdatedBy
     * @param divisionId
     * @return returns true or false depending on if the Customer was added successfully
     * @throws SQLException
     */
    public static boolean addCustomer(String name, String address, String postalCode, String phone, Timestamp createDate,
                               String createdBy, Timestamp lastUpdate, String lastUpdatedBy, int divisionId) throws SQLException {
        String sqlStatement = "INSERT INTO customers (Customer_Name, Address, Postal_Code, Phone, Create_Date, " +
                "Created_By, Last_Update, Last_Updated_By, Division_ID) VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?)";
        PreparedStatement preparedStatement = JDBC.getConnection().prepareStatement(sqlStatement);

        //set the parameters of the query
        preparedStatement.setString(1, name);
        preparedStatement.setString(2, address);
        preparedStatement.setString(3, postalCode);
        preparedStatement.setString(4, phone);
        preparedStatement.setTimestamp(5, createDate);
        preparedStatement.setString(6, createdBy);
        preparedStatement.setTimestamp(7, lastUpdate);
        preparedStatement.setString(8, lastUpdatedBy);
        preparedStatement.setInt(9, divisionId);

        try {
            preparedStatement.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * This method uses a PreparedStatement to update a Customer in the database based on the Customer_ID
     * @param id
     * @param name
     * @param address
     * @param postalCode
     * @param phone
     * @param lastUpdate
     * @param lastUpdatedBy
     * @param divisionId
     * @return returns true or false depending on if the update was successful
     * @throws SQLException
     */
    public static boolean updateCustomer(int id, String name, String address, String postalCode, String phone,
                                         Timestamp lastUpdate, String lastUpdatedBy, int divisionId) throws SQLException {
        String sqlStatement = "UPDATE customers SET Customer_Name = ?, Address = ?, Postal_Code = ?, Phone = ?, Last_Update = ?, Last_Updated_By = ?, Division_ID = ? WHERE Customer_ID = ?";
        PreparedStatement preparedStatement = JDBC.getConnection().prepareStatement(sqlStatement);

        //set the parameters of the query
        preparedStatement.setString(1, name);
        preparedStatement.setString(2, address);
        preparedStatement.setString(3, postalCode);
        preparedStatement.setString(4, phone);
        preparedStatement.setTimestamp(5, lastUpdate);
        preparedStatement.setString(6, lastUpdatedBy);
        preparedStatement.setInt(7, divisionId);
        preparedStatement.setInt(8, id);

        try {
            preparedStatement.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * This method deletes a Customer from the database via a PreparedStatement based on the Customer_ID
     * @param id
     * @return returns true or false depending on if the deletion was successful
     * @throws SQLException
     */
    public static boolean deleteCustomer(int id) throws SQLException {
        String sqlStatement = "DELETE FROM customers WHERE Customer_ID = ?";
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

    public static boolean deleteCustomerAppointments(int customerId) throws SQLException {
        String sqlStatement = "DELETE FROM appointments WHERE Customer_ID = ?";
        PreparedStatement preparedStatement = JDBC.getConnection().prepareStatement(sqlStatement);

        //set the parameters of the query
        preparedStatement.setInt(1, customerId);

        try {
            preparedStatement.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }

    }

    /**
     * This method returns all Customer objects from the database by using a PreparedStatement that makes an SQL query
     * @return returns all customers from database in an ObservableList
     * @throws SQLException
     */
    public static ObservableList<Customer> getAllCustomers() throws SQLException {
        ObservableList<Customer> allCustomers = FXCollections.observableArrayList();
        String sqlStatement = "SELECT * FROM customers";
        PreparedStatement preparedStatement = JDBC.getConnection().prepareStatement(sqlStatement);

        try {
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                int customerId = resultSet.getInt("Customer_ID");
                String name = resultSet.getString("Customer_Name");
                String address = resultSet.getString("Address");
                String postalCode = resultSet.getString("Postal_Code");
                String phone = resultSet.getString("Phone");
                Timestamp createDate = resultSet.getTimestamp("Create_Date");
                String createdBy = resultSet.getString("Created_By");
                Timestamp lastUpdate = resultSet.getTimestamp("Last_Update");
                String lastUpdatedBy = resultSet.getString("Last_Updated_By");
                int divisionId = resultSet.getInt("Division_ID");

                Customer customer = new Customer(customerId, name, address, postalCode, phone, createDate, createdBy, lastUpdate, lastUpdatedBy, divisionId);
                allCustomers.addAll(customer);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return allCustomers;
    }

    /**
     * This method returns all customers from the database whose creation date was less than 7 days before the current date
     * @return returns an ObservableList of all new customers
     * @throws SQLException
     */
    public static ObservableList<Customer> getNewCustomers() throws SQLException {
        ObservableList<Customer> newCustomers = FXCollections.observableArrayList();
        String sqlStatement = "SELECT * FROM customers WHERE Create_Date > ?";
        PreparedStatement preparedStatement = JDBC.getConnection().prepareStatement(sqlStatement);

        //set the parameters of the query
        preparedStatement.setTimestamp(1, Timestamp.valueOf(LocalDateTime.now().minusDays(7)));

        try {
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                int customerId = resultSet.getInt("Customer_ID");
                String name = resultSet.getString("Customer_Name");
                String address = resultSet.getString("Address");
                String postalCode = resultSet.getString("Postal_Code");
                String phone = resultSet.getString("Phone");
                Timestamp createDate = resultSet.getTimestamp("Create_Date");
                String createdBy = resultSet.getString("Created_By");
                Timestamp lastUpdate = resultSet.getTimestamp("Last_Update");
                String lastUpdatedBy = resultSet.getString("Last_Updated_By");
                int divisionId = resultSet.getInt("Division_ID");

                Customer customer = new Customer(customerId, name, address, postalCode, phone, createDate, createdBy, lastUpdate, lastUpdatedBy, divisionId);
                newCustomers.addAll(customer);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return newCustomers;
    }

    /**
     * This method returns CustomerLocationReport objects from the database by using a PreparedStatement to make an
     * SQL query that groups customers by division and finds the total count as well
     * @return returns an ObservableList of CustomerLocationReport objects
     * @throws SQLException
     */
    public static ObservableList<CustomerLocationReport> getCustomersByDivision() throws SQLException {
        ObservableList<CustomerLocationReport> customersByLocation = FXCollections.observableArrayList();
        String sqlStatement = "SELECT first_level_divisions.Division, COUNT(customers.Division_ID) AS Total FROM customers JOIN first_level_divisions ON customers.Division_ID = first_level_divisions.Division_ID GROUP BY first_level_divisions.Division";
        PreparedStatement preparedStatement = JDBC.getConnection().prepareStatement(sqlStatement);

        try {
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                String division = resultSet.getString("Division");
                int total = resultSet.getInt("Total");

                CustomerLocationReport customerLocationReport = new CustomerLocationReport(division, total);
                customersByLocation.addAll(customerLocationReport);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return customersByLocation;
    }

    /**
     * This method returns CustomerLocationReport objects from the database by using a PreparedStatement to make an
     * SQL query that groups customers by country and finds the total count as well
     * @return returns an ObservableList of CustomerLocationReport objects
     * @throws SQLException
     */
    public static ObservableList<CustomerLocationReport> getCustomersByCountry() throws SQLException {
        ObservableList<CustomerLocationReport> customersByCountry = FXCollections.observableArrayList();
        String sqlStatement = "SELECT countries.Country, COUNT(customers.Division_ID) AS Total FROM customers JOIN first_level_divisions ON customers.Division_ID = first_level_divisions.Division_ID JOIN countries ON first_level_divisions.Country_ID = countries.Country_ID GROUP BY Country";
        PreparedStatement preparedStatement = JDBC.getConnection().prepareStatement(sqlStatement);

        try {
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                String country = resultSet.getString("Country");
                int total = resultSet.getInt("Total");

                CustomerLocationReport customerLocationReport = new CustomerLocationReport(country, total);
                customersByCountry.addAll(customerLocationReport);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return customersByCountry;
    }

    /**
     * This method returns the Name of a customer based on their Customer_ID
     * @param id the ID of the customer whose name is desired
     * @return returns the name of a given customer
     * @throws SQLException
     */
    public static String getNameFromId(int id) throws SQLException {
        String customerName = null;
        String sqlStatement = "SELECT Customer_Name FROM customers WHERE Customer_ID = ?";
        PreparedStatement preparedStatement = JDBC.getConnection().prepareStatement(sqlStatement);

        //set the parameters of the query
        preparedStatement.setInt(1, id);

        try {
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                customerName = resultSet.getString("Customer_Name");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return customerName;
    }

    /**
     * This method is used to see if a customer in the database has any currently schedule appointments by comparing
     * their Customer_ID against the Customer_ID of every Appointment in the database
     * @param customer the Customer whose appointments are being searched for
     * @return returns true if the customer has appointments and false if they have no appointments
     * @throws SQLException
     */
    public static boolean hasAppointments(Customer customer) throws SQLException {
        ObservableList<Appointment> existingAppointments = FXCollections.observableArrayList();
        String sqlStatement = "SELECT * FROM appointments JOIN customers ON appointments.Customer_ID = customers.Customer_ID WHERE customers.Customer_ID = ?";
        PreparedStatement preparedStatement = JDBC.getConnection().prepareStatement(sqlStatement);

        //set the parameters of the query
        preparedStatement.setInt(1, customer.getCustomerId());

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
                existingAppointments.add(appointment);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return (existingAppointments.size() != 0);
    }

    /**
     * This method returns an ObservableList of all country names found in the database
     * @return returns an ObservableList of all countries
     * @throws SQLException
     */
    public static ObservableList<String> getAllCountries() throws SQLException {
        ObservableList<String> allCountries = FXCollections.observableArrayList();
        String sqlStatement = "SELECT Country FROM countries";
        PreparedStatement preparedStatement = JDBC.getConnection().prepareStatement(sqlStatement);

        try {
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                allCountries.add(resultSet.getString("Country"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return allCountries;
    }

    /**
     * This method returns an ObservableList of all subdivisions for a given country found in the database
     * @param country the name of the country whose subdivisions you want return
     * @return returns the names of all subdivisions for a given country as an ObservableList
     * @throws SQLException
     */
    public static ObservableList<String> getSubDivisions(String country) throws SQLException {
        ObservableList<String> subDivision = FXCollections.observableArrayList();
        String sqlStatement = "SELECT first_level_divisions.Division FROM first_level_divisions JOIN countries ON first_level_divisions.Country_ID = countries.Country_ID WHERE countries.Country = ?";
        PreparedStatement preparedStatement = JDBC.getConnection().prepareStatement(sqlStatement);

        //set the parameters of the query
        preparedStatement.setString(1, country);

        try {
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                subDivision.add(resultSet.getString("Division"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return subDivision;
    }

    /**
     * This method finds the name of a country based on a subdivision's ID
     * @param divisionId the ID of the subdivision for which you want to find the country
     * @return returns the name of a country to which a subdivision belongs
     * @throws SQLException
     */
    public static String getCountryFromDivisionId(int divisionId) throws SQLException {
        String country = null;
        String sqlStatement = "SELECT countries.Country FROM countries JOIN first_level_divisions ON first_level_divisions.Country_ID = countries.Country_ID WHERE first_level_divisions.Division_ID = ?";
        PreparedStatement preparedStatement = JDBC.getConnection().prepareStatement(sqlStatement);

        //set the parameters of the query
        preparedStatement.setInt(1, divisionId);

        try {
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                country = resultSet.getString("Country");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return country;
    }

    /**
     * This method uses a PreparedStatement to make an SQL statement that returns the Name of a Division based on its
     * Division_ID in the database
     * @param divisionId the Division_ID of a given first level division (state/province)
     * @return returns the Name of a Division from the database
     * @throws SQLException
     */
    public static String getDivisionName(int divisionId) throws SQLException {
        String divisionName = null;
        String sqlStatement = "SELECT Division FROM first_level_divisions WHERE Division_ID = ?";
        PreparedStatement preparedStatement = JDBC.getConnection().prepareStatement(sqlStatement);

        //set the parameters of the query
        preparedStatement.setInt(1, divisionId);

        try {
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                divisionName = resultSet.getString("Division");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return divisionName;
    }

    /**
     * This method uses a PreparedStatement to make an SQL statement that returns the Division_ID of a Division based on
     * its Name
     * @param divisionName the name the Division whose ID you want to find
     * @return returns the ID of a Division found in the database
     * @throws SQLException
     */
    public static int getDivisionIdFromName(String divisionName) throws SQLException{
        int divisionId = -1;
        String sqlStatement = "SELECT Division_ID FROM first_level_divisions WHERE Division = ?";
        PreparedStatement preparedStatement = JDBC.getConnection().prepareStatement(sqlStatement);

        //set the parameters of the query
        preparedStatement.setString(1, divisionName);

        try {
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                divisionId = resultSet.getInt("Division_ID");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return divisionId;
    }
}
