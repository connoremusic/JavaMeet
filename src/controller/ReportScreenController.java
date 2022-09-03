package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.net.URL;

import java.util.Optional;
import java.util.ResourceBundle;

public class ReportScreenController implements Initializable {

    private Stage stage;
    private Parent root;

    @FXML
    private Button exitButton;
    @FXML
    private AnchorPane anchorPane;
    private double x, y = 0;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    /**
     * This method creates a popup window with a TableView of all Appointment objects grouped by month (and year) when
     * called
     * @param actionEvent is unused and was auto-generated
     */
    public void showAppointmentsByMonth(ActionEvent actionEvent) {
        try {
            Stage byMonthStage = new Stage(StageStyle.DECORATED);
            FXMLLoader fxmlLoader = new FXMLLoader();
            Pane root = fxmlLoader.load(getClass().getResource("/view/AppointmentsByMonthScreen.fxml"));
            byMonthStage.setScene(new Scene(root));
            byMonthStage.setResizable(false);
            byMonthStage.setTitle("Appointments by Month");
            byMonthStage.initOwner(stage);
            byMonthStage.initModality(Modality.APPLICATION_MODAL);
            byMonthStage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * This method creates a popup window with a TableView of all Appointment objects grouped by type when called
     * @param actionEvent is unused and was auto-generated
     */
    public void showAppointmentsByType(ActionEvent actionEvent) {
        try {
            Stage byTypeStage = new Stage(StageStyle.DECORATED);
            FXMLLoader fxmlLoader = new FXMLLoader();
            Pane root = fxmlLoader.load(getClass().getResource("/view/AppointmentsByTypeScreen.fxml"));
            byTypeStage.setScene(new Scene(root));
            byTypeStage.setResizable(false);
            byTypeStage.setTitle("Appointments by Type");
            byTypeStage.initOwner(stage);
            byTypeStage.initModality(Modality.APPLICATION_MODAL);
            byTypeStage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * This method creates a popup window with a TableView of all Customer objects grouped by location
     * @param actionEvent is unused and was auto-generated
     */
    public void showCustomersByLocation(ActionEvent actionEvent) {
        try {
            Stage byLocationStage = new Stage(StageStyle.DECORATED);
            FXMLLoader fxmlLoader = new FXMLLoader();
            Pane root = fxmlLoader.load(getClass().getResource("/view/CustomersByLocationScreen.fxml"));
            byLocationStage.setScene(new Scene(root));
            byLocationStage.setResizable(false);
            byLocationStage.setTitle("Customers by Location");
            byLocationStage.initOwner(stage);
            byLocationStage.initModality(Modality.APPLICATION_MODAL);
            byLocationStage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * This method loads the ScheduleScreen in the existing stage for a cleaner UI look
     * @param actionEvent is used to inform the method of the correct stage in which to load the new ScheduleScreen scene
     * @throws IOException
     */
    public void showScheduleScreen(ActionEvent actionEvent) throws IOException {
        root = FXMLLoader.load(getClass().getResource("/view/ScheduleScreen.fxml"));
        stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        Scene scheduleScene = new Scene(root);
        stage.setScene(scheduleScene);
    }

    /**
     * This method loads the CustomerScreen in the existing stage for a cleaner UI look
     * @param actionEvent is used to inform the method of the correct stage in which to load the new Customer scene
     * @throws IOException
     */
    public void showCustomerScreen(ActionEvent actionEvent) throws IOException {
        root = FXMLLoader.load(getClass().getResource("/view/CustomerScreen.fxml"));
        stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        Scene customerScene = new Scene(root);
        stage.setScene(customerScene);
    }

    /**
     * This method loads the ContactScreen in the existing stage for a cleaner UI look
     * @param actionEvent is used to inform the method of the correct stage in which to load the new Contact scene
     * @throws IOException
     */
    public void showContactScreen(ActionEvent actionEvent) throws IOException {
        root = FXMLLoader.load(getClass().getResource("/view/ContactScreen.fxml"));
        stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        Scene customerScene = new Scene(root);
        stage.setScene(customerScene);
    }

    /**
     * This method is unused here because the program is already displaying the ReportScreen
     * @param actionEvent is unused and was auto-generated
     */
    public void showReportScreen(ActionEvent actionEvent) {

    }

    /**
     * This method creates a popup window with information about the program such as the different software and
     * versions used to program everything
     * @param actionEvent is unused and was auto-generated
     * @throws IOException
     */
    public void showAboutScreen(ActionEvent actionEvent) throws IOException {
        Stage aboutStage = new Stage(StageStyle.DECORATED);
        FXMLLoader fxmlLoader = new FXMLLoader();
        Pane root = fxmlLoader.load(getClass().getResource("/view/AboutScreen.fxml"));
        aboutStage.setScene(new Scene(root));
        aboutStage.setResizable(false);
        aboutStage.setTitle("About");
        aboutStage.initOwner(stage);
        aboutStage.initModality(Modality.APPLICATION_MODAL);
        aboutStage.showAndWait();
    }

    /**
     * This method closes the program, which will also close the database connection via the main() method ending
     * @param actionEvent is unused and was auto-generated
     */
    public void exitProgram(ActionEvent actionEvent) {
        Alert alert = new Alert(Alert.AlertType.NONE, "Are you sure you want to exit?");
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
