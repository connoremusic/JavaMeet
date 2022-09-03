package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import model.User;
import utilities.JDBC;
import utilities.UserQuery;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URL;
import java.sql.SQLException;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.Optional;
import java.util.ResourceBundle;


public class LoginScreenController implements Initializable {
    private double x,y = 0;
    private Stage stage;

    private static User currentUser;
    @FXML
    private AnchorPane anchorPane;
    @FXML
    private TextField usernameField;
    @FXML
    private PasswordField passwordField;
    private final String[] languageOptions = {"English", "Français"};
    @FXML
    private ComboBox<String> languageComboBox;
    @FXML
    private Button loginButton;
    @FXML
    private Button exitButton;
    @FXML
    private Label loginLabel;
    @FXML
    private Label timeZoneLabel;
    @FXML
    private Label zoneLabel;
    private String exitMessage;

    public LoginScreenController() throws IOException {
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        //Locale.setDefault(new Locale("fr"));
        Locale userLocale = Locale.getDefault();
        zoneLabel.setText(ZoneId.systemDefault().getId());
        resourceBundle = ResourceBundle.getBundle("resources.language_data.loginPage");
        languageComboBox.getItems().addAll(languageOptions);
        languageComboBox.setValue(userLocale.getDisplayLanguage());
        languageComboBox.setOnAction(this::selectLanguage);
        System.out.println("userLocale is " + userLocale);
        loginLabel.setText(resourceBundle.getString("loginLabel"));
        timeZoneLabel.setText(resourceBundle.getString("timeZoneLabel"));
        exitButton.setText(resourceBundle.getString("exitButton"));
        loginButton.setText(resourceBundle.getString("loginButton"));
        usernameField.setPromptText(resourceBundle.getString("usernameField"));
        passwordField.setPromptText(resourceBundle.getString("passwordField"));
        exitMessage = resourceBundle.getString("exitMessage");

    }

    /**
     * This method is used to open a connection to the database and validate a username and password entered by the user.
     * Error messages are displayed if anything is incorrect, and successful and unsuccessful login attempts are logged
     * to a .txt file. If the login attempt is successful, the database connection remains open, and the user is
     * redirected to the ScheduleScreen
     * @param actionEvent is used to get the current Stage and load the new screen in it
     * @throws IOException
     */
    public void pressLoginButton(ActionEvent actionEvent) throws IOException {
        JDBC.makeConnection();
        String username = usernameField.getText();
        String password = passwordField.getText();
        try {
            FileWriter fileWriter = new FileWriter("src/login_activity.txt", true);
            PrintWriter printWriter = new PrintWriter(fileWriter);
            if (UserQuery.validateUser(username, password) != -1) {
                //write the successful login attempt to file
                printWriter.println("Successful login attempt at " + ZonedDateTime.now(ZoneOffset.UTC).format(DateTimeFormatter.ofPattern("yyyy-MMM-dd HH:mm:ss")) + " UTC with Username: " + username);
                printWriter.close();

                //load the schedule screen
                Parent root = FXMLLoader.load(getClass().getResource("/view/ScheduleScreen.fxml"));
                stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
                Scene scene = new Scene(root);
                stage.setScene(scene);
                stage.show();
            } else {
                //write the failed login attempt to file and create wrong password popup dialogue
                printWriter.println("Failed login attempt at " + ZonedDateTime.now(ZoneOffset.UTC).format(DateTimeFormatter.ofPattern("yyyy-MMM-dd HH:mm:ss")) + " UTC with Username: " + username);
                printWriter.close();
                errorMessageDialogue("Error: Invalid Username or Password", "The username or password you have entered is incorrect. Please try again.");
                JDBC.closeConnection();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * This method sets the default Locale based on the selected languages, and reloads the ResourceBundle to
     * repopulate all the labels and text field prompts on the login page.
     * @param actionEvent is unused and was auto-generated
     */
    public void selectLanguage(ActionEvent actionEvent) {
        String language = languageComboBox.getValue();
        if (language.equals("English")) {
            // Sets all the text fields based on the English language package
            Locale.setDefault(new Locale("en"));
            setResourceBundle(ResourceBundle.getBundle("resources.language_data.loginPage"));
            System.out.println("English chosen as language");
        } else if (language.equals("Français")) {
            // Sets all the text fields based on the French language package
            Locale.setDefault(new Locale("fr"));
            setResourceBundle(ResourceBundle.getBundle("resources.language_data.loginPage"));
            System.out.println("Le français choisi comme langue");
        }
    }

    /**
     * This static method determines who is currently logged in, and can be used to pass that information to other controllers
     * @return returns the currentUser
     */
    public static User getCurrentUser() {
        return currentUser;
    }

    /**
     * This static method is used to set the currentUser for a login session if the UserQuery class can successfully
     * validate the user
     * @param currentUser is the User object passed if the SQL query is successful
     */
    public static void setCurrentUser(User currentUser) {
        LoginScreenController.currentUser = currentUser;
    }

    /**
     * This method closes the program and the main() method will then also close the database connection if it is open
     * @param actionEvent is unused and was auto-generated
     */
    public void exitProgram(ActionEvent actionEvent) {
        Alert alert = new Alert(Alert.AlertType.NONE, exitMessage);
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
     * This method is used to set the ResourceBundle used in the LoginScreen
     * @param resourceBundle
     */
    public void setResourceBundle(ResourceBundle resourceBundle) {
        loginLabel.setText(resourceBundle.getString("loginLabel"));
        timeZoneLabel.setText(resourceBundle.getString("timeZoneLabel"));
        exitButton.setText(resourceBundle.getString("exitButton"));
        loginButton.setText(resourceBundle.getString("loginButton"));
        usernameField.setPromptText(resourceBundle.getString("usernameField"));
        passwordField.setPromptText(resourceBundle.getString("passwordField"));
        exitMessage = resourceBundle.getString("exitMessage");
    }

    /**
     * This method is used to display an error message to the user if anything goes wrong
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
