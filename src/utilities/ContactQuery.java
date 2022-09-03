package utilities;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.Contact;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public abstract class ContactQuery {

    /**
     * This method uses a PreparedStatement to make an SQL query that returns all contacts in the database
     * @return returns an ObservableList of all contacts in the database
     * @throws SQLException
     */
    public static ObservableList<Contact> getAllContacts() throws SQLException {
        ObservableList<Contact> allContacts = FXCollections.observableArrayList();
        String sqlStatement = "SELECT * FROM contacts";
        PreparedStatement preparedStatement = JDBC.getConnection().prepareStatement(sqlStatement);

        try {
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                int contactId = resultSet.getInt("Contact_ID");
                String name = resultSet.getString("Contact_Name");
                String email = resultSet.getString("Email");

                Contact contact = new Contact(contactId, name, email);
                allContacts.add(contact);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return allContacts;
    }

    /**
     * This method is used to return the Name of a contact given the Contact_ID
     * @param id the ID of the contact whose name is desired
     * @return returns the Name of the contact
     * @throws SQLException
     */
    public static String getNameFromId(int id) throws SQLException {
        String contactName = null;
        String sqlStatement = "SELECT Contact_Name from contacts WHERE Contact_ID = ?";
        PreparedStatement preparedStatement = JDBC.getConnection().prepareStatement(sqlStatement);

        //set the parameters of the query
        preparedStatement.setInt(1, id);

        try {
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                contactName = resultSet.getString("Contact_Name");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return contactName;
    }

    /**
     * This method is used to return the Email of a contact given the Contact_ID in the database
     * @param id the ID of the contact whose email is desired
     * @return returns the Email of the contact
     * @throws SQLException
     */
    public static String getEmailFromId(int id) throws SQLException {
        String contactEmail = null;
        String sqlStatement = "SELECT Email from contacts WHERE Contact_ID = ?";
        PreparedStatement preparedStatement = JDBC.getConnection().prepareStatement(sqlStatement);

        //set the parameters of the query
        preparedStatement.setInt(1, id);

        try {
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                contactEmail = resultSet.getString("Email");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return contactEmail;
    }
}
