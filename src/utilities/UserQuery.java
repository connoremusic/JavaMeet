package utilities;

import controller.LoginScreenController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.User;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

public abstract class UserQuery {

    /**
     * This method returns an ObservableList of all Users found in the database
     * @return returns an ObservableList of all Users
     * @throws SQLException
     */
    public static ObservableList<User> getAllUsers() throws SQLException {
        ObservableList<User> allUsers = FXCollections.observableArrayList();
        String sqlStatement = "SELECT * FROM users";
        PreparedStatement preparedStatement = JDBC.getConnection().prepareStatement(sqlStatement);

        try {
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                int userId = resultSet.getInt("User_ID");
                String username = resultSet.getString("User_Name");
                String password = resultSet.getString("Password");
                Timestamp createDate = resultSet.getTimestamp("Create_Date");
                String createdBy = resultSet.getString("Created_By");
                Timestamp lastUpdate = resultSet.getTimestamp("Last_Update");
                String lastUpdatedBy = resultSet.getString("Last_Updated_By");

                User user = new User(userId, username, password, createDate, createdBy, lastUpdate, lastUpdatedBy);
                allUsers.add(user);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return allUsers;
    }

    /**
     * This method uses a PreparedStatement to make an SQL query that compares user entered data against the list of all
     * Users found in the database. If the exact username/password combination is found in the database, this method
     * returns 1 and sets the currentUser of the session to the user found in the database
     * @param username username entered by the user at the LoginScreen
     * @param password password entered by the user at the LoginScreen
     * @return returns 1 if the username/password is correct, otherwise -1
     * @throws SQLException
     */
    public static int validateUser(String username, String password) throws SQLException {
        String sqlQuery = "SELECT * FROM users WHERE User_Name = ? AND Password = ?";
        PreparedStatement preparedStatement = JDBC.getConnection().prepareStatement(sqlQuery);

        //set the parameters of the query
        preparedStatement.setString(1, username);
        preparedStatement.setString(2, password);

        try {
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                LoginScreenController.setCurrentUser(new User(resultSet.getInt("User_ID"), resultSet.getString("User_Name"),
                        resultSet.getString("Password"), resultSet.getTimestamp("Create_Date"),
                        resultSet.getString("Created_By"), resultSet.getTimestamp("Last_Update"), resultSet.getString("Last_Updated_By")));
                return 1;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }
}
