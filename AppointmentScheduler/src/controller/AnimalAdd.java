package controller;

import DAO.AnimalDAO;
import DAO.CustomerDAO;
import DAO.NoteDAO;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import model.Customer;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.text.NumberFormat;
import java.time.LocalDateTime;
import java.util.ResourceBundle;
/**
 * AnimalsAdd class
 *
 */
public class AnimalAdd implements Initializable {


    @FXML
    private TextArea noteText;
    @FXML
    private TextField animalNameTxt;
    @FXML
    private ComboBox animalTypeTxt;
    @FXML
    private TextField animalAgeTxt;
    @FXML
    private TextField animalBreedTxt;
    @FXML
    private TextField animalWeightTxt;
    @FXML
    private ComboBox<Customer> animalOwnerTxt;
    @FXML
    private Button animalSaveBtn;
    private NumberFormat animalAge;


    int tempAnimalId = 0;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        animalOwnerTxt.setItems(CustomerDAO.getCustomerList());
    }

    public void animalSaveBtn(ActionEvent actionEvent) {
        try {
            if (animalNameTxt.getText().isEmpty() || animalNameTxt.getText().isBlank()) {
                helper.ErrorMsg.getError(27);
            } else if (animalAgeTxt.getText().isBlank() || animalAgeTxt.getText().isEmpty()) {
                helper.ErrorMsg.getError(28);
            } else if (!animalAgeTxt.getText().matches("[0-9]*")) {
                helper.ErrorMsg.getError(29);
            } else if (animalBreedTxt.getText().isEmpty() || animalBreedTxt.getText().isBlank()) {
                helper.ErrorMsg.getError(30);
            } else if (animalWeightTxt.getText().isBlank() || animalWeightTxt.getText().isEmpty()) {
                helper.ErrorMsg.getError(32);
            } else if (!animalWeightTxt.getText().matches("[0-9]*")) {
                helper.ErrorMsg.getError(31);
            } else {
                String animalName = animalNameTxt.getText();
                String animalType = (String) animalTypeTxt.getValue();
                int animalAge = Integer.parseInt(animalAgeTxt.getText());
                String animalBreed = animalBreedTxt.getText();
                int animalWeight = Integer.parseInt(animalWeightTxt.getText());
                Customer ownerId = animalOwnerTxt.getValue();
                int animalOwnerId = ownerId.getCustomerId();

                String animalNotes = noteText.getText();

                AnimalDAO.addAnimal(animalName, animalType, animalAge, animalBreed, animalWeight, animalOwnerId);
                helper.ErrorMsg.confirmation(9);
                int id = AnimalDAO.lastId();
                LocalDateTime createdDate = LocalDateTime.now();
                LocalDateTime lastUpdated = LocalDateTime.now();
                if (!animalNotes.isBlank() || !animalNotes.isEmpty()) {
                    NoteDAO.addNote(animalNotes, createdDate, lastUpdated, id);
                }
                backToAppointments(actionEvent);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void backToAppointments(ActionEvent actionEvent) throws IOException {
        Stage stage = (Stage) ((Button) actionEvent.getSource()).getScene().getWindow();
        stage.close();
    }

    public void cancelBtn(ActionEvent actionEvent) throws IOException {
        backToAppointments(actionEvent);
    }
}
