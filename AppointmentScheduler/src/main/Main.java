package main;

import helper.JDBC;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.sql.SQLException;
import java.util.Objects;

/**
 * Happy Friends Pet Care Scheduling Application
 */
public class Main extends Application {

    /**
     * Redirects to login screen
     *
     * @param stage setting stage
     * @throws Exception addresses unhandled exception
     */
    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load((Objects.requireNonNull(getClass().getResource("/view/Login.fxml"))));
        stage.setScene(new Scene(root));
        root.getStylesheets().add(this.getClass() .getResource("/test.css").toExternalForm());
        stage.setTitle("Happy Friends Scheduling");
        stage.show();
        stage.centerOnScreen();
        stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent event) {
                Alert alert = new Alert(Alert.AlertType.WARNING, "Cancel");
                alert.setTitle("Exit Application");
                alert.setHeaderText("Are you sure you want to exit?");
                alert.setContentText("Press OK to exit application or press cancel to stay.");
                alert.getButtonTypes().clear();
                alert.getButtonTypes().addAll(ButtonType.CANCEL, ButtonType.OK);
                alert.showAndWait();
                if (alert.getResult() == ButtonType.OK) {
                    stage.close();
                } else if (alert.getResult() == ButtonType.CANCEL) {
                    alert.close();
                }
                event.consume();

            }
        });
    }



    /**
     * Initiates connection to database and closes connection to database. Launches program.
     *
     * @param args args
     * @throws SQLException addresses unhandled exception
     */
    public static void main(String[] args) throws SQLException {
        JDBC.openConnection();
        launch(args);
        JDBC.closeConnection();
    }
}