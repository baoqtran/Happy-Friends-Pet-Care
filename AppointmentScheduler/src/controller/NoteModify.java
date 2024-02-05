package controller;

import DAO.NoteDAO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import model.Note;

import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ResourceBundle;
/**
 * NoteModify class
 *
 */
public class NoteModify implements Initializable {
    @FXML
    private TextArea notesModify;
    @FXML
    private TextField modifyNoteId;
    int usrname;

    ObservableList<Integer> list = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
    }

    public void getNoteInfoModify(Note note) throws SQLException {
        modifyNoteId.setText(Integer.toString(note.getNoteId()));
        notesModify.setText(note.getNotes());
    }

    public ObservableList<Integer> setAnimalId(int usrname) {
        //   animalIdLabel.setText(String.valueOf(usrname));
        list.add(usrname);
        return list;
    }

    public void modifySaveBtn(ActionEvent actionEvent) {
        int id = Integer.parseInt(modifyNoteId.getText());
        String notes = notesModify.getText();
        LocalDateTime lastUpdated = LocalDateTime.now();
        NoteDAO.updateNote(id, notes, lastUpdated, list.get(usrname));
        helper.ErrorMsg.confirmation(6);
        Stage stage = (Stage) ((Button) actionEvent.getSource()).getScene().getWindow();
        stage.close();
    }

    public void cancelBtn(ActionEvent actionEvent) {
        Stage stage = (Stage) ((Button) actionEvent.getSource()).getScene().getWindow();
        stage.close();
    }
}
