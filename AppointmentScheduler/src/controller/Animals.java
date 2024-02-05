package controller;

import DAO.AnimalDAO;
import DAO.NoteDAO;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Region;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import model.Animal;
import model.Note;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.Objects;
import java.util.ResourceBundle;
/**
 * Animals class
 *
 */
public class Animals implements Initializable {
    ObservableList<Animal> animalList = AnimalDAO.getAllAnimals();

    @FXML private TableView<Animal> animalView;
    @FXML private TableColumn<Animal, Integer> tableId;
    @FXML private TableColumn<Animal, String> tableName;
    @FXML private TableColumn<Animal, String> tableType;
    @FXML private TableColumn<Animal, Integer> tableAge;
    @FXML private TableColumn<Animal, String> tableBreed;
    @FXML private TableColumn<Animal, Integer> tableWeight;
    @FXML private TableColumn<Animal, Integer> tableOwner;
    @FXML private TableColumn<Animal, String> tableNote;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        animalView.setItems(animalList);
        animalView.setPlaceholder(new Label("No data for Animals is available at this time."));
        tableId.setCellValueFactory(new PropertyValueFactory<>("animalId"));
        tableName.setCellValueFactory(new PropertyValueFactory<>("animalName"));
        tableType.setCellValueFactory(new PropertyValueFactory<>("animalType"));
        tableAge.setCellValueFactory(new PropertyValueFactory<>("animalAge"));
        tableBreed.setCellValueFactory(new PropertyValueFactory<>("animalBreed"));
        tableWeight.setCellValueFactory(new PropertyValueFactory<>("animalWeight"));
        tableOwner.setCellValueFactory(new PropertyValueFactory<>("animalOwnerName"));

        // tableNote.setCellValueFactory(new PropertyValueFactory<>("animalNotes"));
    }

    public void addNewAnimal(ActionEvent actionEvent) throws IOException {
        Stage st = new Stage();
        st.initModality(Modality.APPLICATION_MODAL);
        st.setTitle("Happy Friends Scheduling");
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/AnimalAdd.fxml"));
        Region root = (Region) loader.load();
        Scene scene = new Scene(root);
        st.setScene(scene);
        scene.getStylesheets().add(this.getClass().getResource("/test.css").toExternalForm());
        AnimalAdd mainController = loader.<AnimalAdd>getController();
        st.show();
        st.setOnHidden(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent windowEvent) {

                animalView.refresh();
                animalView.setItems(AnimalDAO.getAllAnimals());
                animalView.refresh();

            }
        });
    }

    public void deleteAnimal(ActionEvent actionEvent) {
        ObservableList<Note> noteList = NoteDAO.getAssociatedNotes();
        Animal animal = animalView.getSelectionModel().getSelectedItem();
        if (animal == null) {
            helper.ErrorMsg.getError(26);
            return;
        }

        int selectedAnimal = animalView.getSelectionModel().getSelectedItem().getAnimalId();
        int count = 0;
        for (Note note : noteList) {
            int noteId = note.getNoteId();
            int newNoteId = note.getAnimalId();
            if (newNoteId == selectedAnimal) {
                NoteDAO.deleteNote(noteId);
            }
        }
        animalView.refresh();
        Alert confirmRemoval = new Alert(Alert.AlertType.WARNING);
        confirmRemoval.setTitle("Alert");
        confirmRemoval.setContentText("Would you like to remove the selected animal?");
        confirmRemoval.getButtonTypes().clear();
        confirmRemoval.getButtonTypes().addAll(ButtonType.CANCEL, ButtonType.OK);
        confirmRemoval.showAndWait();
        if (confirmRemoval.getResult() == ButtonType.OK) {
            Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION);
            confirmation.setTitle("Alert");
            confirmation.setContentText("Animal ID# " + animalView.getSelectionModel().getSelectedItem().getAnimalId() + " for " + animalView.getSelectionModel().getSelectedItem().getAnimalName() + " has been deleted.");
            confirmation.getButtonTypes().clear();
            confirmation.getButtonTypes().addAll(ButtonType.OK);
            confirmation.showAndWait();
            AnimalDAO.deleteAnimal(selectedAnimal);
            animalList = AnimalDAO.getAllAnimals();
            animalView.setItems(animalList);
            animalView.refresh();
        }   else if (confirmRemoval.getResult() == ButtonType.CANCEL) {
            confirmRemoval.close();
        }
    }
    public void updateAnimal(ActionEvent actionEvent) throws IOException, SQLException {
        if (animalView.getSelectionModel().getSelectedItem() != null) {
            Stage st = new Stage();
            st.initModality(Modality.APPLICATION_MODAL);
            st.setTitle("Happy Friends Scheduling");
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/AnimalModify.fxml"));
            Region root = (Region) loader.load();
            Scene scene = new Scene(root);
            st.setScene(scene);
            scene.getStylesheets().add(this.getClass().getResource("/test.css").toExternalForm());
            AnimalModify MCController = loader.getController();
            MCController.getAnimalInfo(animalView.getSelectionModel().getSelectedItem());
            st.centerOnScreen();
            st.show();
            st.setOnHidden(new EventHandler<WindowEvent>() {
                @Override
                public void handle(WindowEvent windowEvent) {
                    animalView.refresh();
                    animalView.setItems(AnimalDAO.getAllAnimals());
                    animalView.refresh();
                }
            });
        } else {
            helper.ErrorMsg.getError(26);
        }
    }


    public void customerScreen(ActionEvent actionEvent) throws IOException {
        Parent parent = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("../view/Customers.fxml")));
        Scene scene = new Scene(parent);
        parent.getStylesheets().add(this.getClass().getResource("/test.css").toExternalForm());
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.centerOnScreen();
        stage.show();

    }

    public void animalScreen(ActionEvent actionEvent) throws IOException {
        Parent parent = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("../view/Animals.fxml")));
        Scene scene = new Scene(parent);
        parent.getStylesheets().add(this.getClass().getResource("/test.css").toExternalForm());
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.centerOnScreen();
        stage.show();
    }

    public void appointmentScreen(ActionEvent actionEvent) throws IOException {
        Parent parent = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("../view/Appointments.fxml")));
        Scene scene = new Scene(parent);
        parent.getStylesheets().add(this.getClass().getResource("/test.css").toExternalForm());
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.centerOnScreen();
        stage.show();
    }

    public void reportsScreen(ActionEvent actionEvent) throws IOException {
        Parent parent = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("../view/Reports.fxml")));
        Scene scene = new Scene(parent);
        parent.getStylesheets().add(this.getClass().getResource("/test.css").toExternalForm());
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.centerOnScreen();
        stage.show();
    }
}
