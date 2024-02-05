package controller;

import DAO.NoteDAO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ResourceBundle;
/**
 * NoteAdd class
 *
 */
public class NoteAdd extends AnimalModify implements Initializable {

    @FXML
    private TextArea addNoteField;
    @FXML
    private TextField addNoteId;
    @FXML
    private int animalId;
    @FXML
    private int id;
    @FXML
    private Label animalIdLabel;
    String animalIdField;
    int usrname;
    int value2;
    Button addSaveBtn;
    ObservableList<Integer> list = FXCollections.observableArrayList();

    public ObservableList<Integer> setAnimalId(int usrname) {
        animalIdLabel.setText(String.valueOf(usrname));
        list.add(usrname);
        return list;
    }

    public void addSaveBtn(ActionEvent actionEvent) throws SQLException {
        String notes = addNoteField.getText();
        LocalDateTime createdDate = LocalDateTime.now();
        LocalDateTime lastUpdated = LocalDateTime.now();
        NoteDAO.addNote(notes, createdDate, lastUpdated, list.get(usrname));
        helper.ErrorMsg.confirmation(7);
        Stage stage = (Stage) ((Button) actionEvent.getSource()).getScene().getWindow();
        stage.close();

    }

    public void addCancelBtn(ActionEvent actionEvent) {
        Stage stage = (Stage) ((Button) actionEvent.getSource()).getScene().getWindow();
        stage.close();
    }

    @FXML
    public void handleCloseButtonAction(ActionEvent actionEvent) {
        ((Stage) (((Button) actionEvent.getSource()).getScene().getWindow())).close();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }


}
