package controller;

import DAO.AnimalDAO;
import DAO.CustomerDAO;
import DAO.NoteDAO;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Region;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import model.Animal;
import model.Customer;
import model.Note;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;
/**
 * AnimalsModify class
 *
 */
public class AnimalModify implements Initializable {
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
    private TableView viewAllNotes;
    @FXML
    private TableColumn viewNoteId;
    @FXML
    private TableColumn viewNotes;
    @FXML
    private TableColumn viewLastUpdated;
    @FXML
    private TableColumn viewCreated;
    @FXML
    private TextField animalIdField;
    Animal animal;


    public void animalSaveBtn(ActionEvent actionEvent) throws IOException {
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
                int animalId = Integer.parseInt(animalIdField.getText());
                String animalName = animalNameTxt.getText();
                String animalType = animalTypeTxt.getValue().toString();
                int animalAge = Integer.parseInt(animalAgeTxt.getText());
                String animalBreed = animalBreedTxt.getText();
                int ownerId = animalOwnerTxt.getValue().getCustomerId();
                int animalWeight = Integer.parseInt(animalWeightTxt.getText());
                helper.ErrorMsg.confirmation(11);
                AnimalDAO.updateAnimal(animalId, animalName, animalType, animalAge, animalBreed, animalWeight, ownerId);
                backToAppointments(actionEvent);
            }
        } catch (NumberFormatException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        ObservableList<Customer> customerList = CustomerDAO.getCustomerList();
        animalOwnerTxt.setItems(customerList);
        animalOwnerTxt.setVisibleRowCount(10);
    }


    public void getAnimalInfo(Animal animal) throws SQLException {
        animalIdField.setText(Integer.toString(animal.getAnimalId()));
        animalNameTxt.setText(animal.getAnimalName());
        animalTypeTxt.setValue(animal.getAnimalType());
        animalAgeTxt.setText(Integer.toString(animal.getAnimalAge()));
        animalBreedTxt.setText(animal.getAnimalBreed());
        animalWeightTxt.setText(Integer.toString(animal.getAnimalWeight()));
        Customer s = CustomerDAO.returnCustomerList(animal.getAnimalOwner());
        animalOwnerTxt.setValue(s);
        viewAllNotes.refresh();

        viewAllNotes.refresh();
        viewAllNotes.setItems(NoteDAO.associatedNotes(animal.getAnimalId()));
        viewAllNotes.setPlaceholder(new Label("No Notes exist. Please create new note."));
        viewNoteId.setCellValueFactory(new PropertyValueFactory<>("noteId"));
        viewNotes.setCellValueFactory(new PropertyValueFactory<>("notes"));
        viewLastUpdated.setCellValueFactory(new PropertyValueFactory<>("lastUpdated"));
        viewCreated.setCellValueFactory(new PropertyValueFactory<>("created"));
        viewAllNotes.setItems(NoteDAO.associatedNotes(animal.getAnimalId()));
        viewAllNotes.refresh();
    }

    public void noteEditBtn(ActionEvent actionEvent) throws IOException, SQLException {
        Note note = (Note) viewAllNotes.getSelectionModel().getSelectedItem();

        if (note == null) {
            helper.ErrorMsg.getError(34);
        } else {
            Stage st = new Stage();
            st.initModality(Modality.APPLICATION_MODAL);
            st.setTitle("Happy Friends Scheduling");
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/NoteModify.fxml"));
            Region root = (Region) loader.load();
            Scene scene = new Scene(root);
            st.setScene(scene);
            scene.getStylesheets().add(this.getClass().getResource("/test.css").toExternalForm());
            NoteModify mainController = loader.<NoteModify>getController();
            mainController.getNoteInfoModify((Note) viewAllNotes.getSelectionModel().getSelectedItem());
            int idField = Integer.parseInt(animalIdField.getText());
            mainController.setAnimalId(idField);
            st.show();
            st.setOnHidden(new EventHandler<WindowEvent>() {
                @Override
                public void handle(WindowEvent windowEvent) {
                    viewAllNotes.refresh();
                    int animalId = Integer.parseInt(animalIdField.getText());
                    viewAllNotes.setItems(NoteDAO.associatedNotes(animalId));
                    viewAllNotes.refresh();
                }
            });
        }


    }

    public void noteNewBtn(ActionEvent actionEvent) throws IOException, SQLException {
        Stage st = new Stage();
        st.initModality(Modality.APPLICATION_MODAL);
        st.setTitle("Happy Friends Scheduling");
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/NoteAdd.fxml"));
        Region root = (Region) loader.load();
        Scene scene = new Scene(root);
        st.setScene(scene);
        scene.getStylesheets().add(this.getClass().getResource("/test.css").toExternalForm());
        NoteAdd mainController = loader.<NoteAdd>getController();
        int idField = Integer.parseInt(animalIdField.getText());
        mainController.setAnimalId(idField);
        st.show();
        st.setOnHidden(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent windowEvent) {
                viewAllNotes.refresh();
                int animalId = Integer.parseInt(animalIdField.getText());
                viewAllNotes.setItems(NoteDAO.associatedNotes(animalId));
                viewAllNotes.refresh();
            }
        });
    }

    public void noteDeleteBtn(ActionEvent actionEvent) {
        Note note = (Note) viewAllNotes.getSelectionModel().getSelectedItem();
        int animalId = Integer.parseInt(animalIdField.getText());
        if (note == null) {
            helper.ErrorMsg.getError(33);
            return;
        } else {
            viewAllNotes.refresh();
            helper.ErrorMsg.confirmation(10);
            NoteDAO.deleteNote(((Note) viewAllNotes.getSelectionModel().getSelectedItem()).getNoteId());
            viewAllNotes.refresh();
            viewAllNotes.setItems(NoteDAO.associatedNotes(animalId));
            viewAllNotes.refresh();
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
