package controller;

import DAO.AppointmentDAO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
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
import model.Appointment;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Objects;
import java.util.ResourceBundle;

/**
 * Appointments class
 *
 */
public class Appointments implements Initializable {

    @FXML
    private TableColumn appointAnimalCol;
    @FXML
    private RadioButton allAppointments1;
    @FXML
    private TableView weekTable;
    @FXML
    private TableColumn weekId;
    @FXML
    private TableColumn weekTitle;
    @FXML
    private TableColumn weekDescription;
    @FXML
    private TableColumn weekType;
    @FXML
    private TableColumn weekVetTech;
    @FXML
    private TableColumn weekStart;
    @FXML
    private TableColumn weekEnd;
    @FXML
    private TableColumn weekOwner;
    @FXML
    private TableColumn weekAnimal;
    @FXML
    private TableColumn weekUser;
    @FXML
    private TableView monthTable;
    @FXML
    private TableColumn monthId;
    @FXML
    private TableColumn monthTitle;
    @FXML
    private TableColumn monthDescription;
    @FXML
    private TableColumn monthType;
    @FXML
    private TableColumn monthVetTech;
    @FXML
    private TableColumn monthStart;
    @FXML
    private TableColumn monthEnd;
    @FXML
    private TableColumn monthOwner;
    @FXML
    private TableColumn monthAnimal;
    @FXML
    private TableColumn monthUser;
    @FXML
    private ToggleGroup appointmentView;
    @FXML
    private TableView<Appointment> appointTable;
    @FXML
    private TableColumn<Appointment, Integer> appointIdCol;
    @FXML
    private TableColumn<Appointment, String> appointTitleCol;
    @FXML
    private TableColumn<Appointment, String> appointDescriptionCol;
    @FXML
    private TableColumn<Appointment, String> appointLocationCol;
    @FXML
    private TableColumn<Appointment, Integer> appointContactCol;
    @FXML
    private TableColumn<Appointment, String> appointTypeCol;
    @FXML
    private TableColumn<Appointment, Timestamp> appointDateCol;
    @FXML
    private TableColumn<Appointment, Timestamp> appointEndDateCol;
    @FXML
    private TableColumn<Appointment, Integer> appointCustIdCol;
    @FXML
    private TableColumn<Appointment, Integer> appointUserIdCol;
    @FXML
    private TextField filterField;

    ObservableList<Appointment> AppointmentList = FXCollections.observableArrayList();
    ObservableList<Appointment> allAppointmentSearch = FXCollections.observableArrayList();
    ObservableList<Appointment> weeklyAppointmentSearch = FXCollections.observableArrayList();
    ObservableList<Appointment> monthlyAppointmentSearch = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        appointIdCol.setCellValueFactory(new PropertyValueFactory<>("appointmentId"));
        appointTitleCol.setCellValueFactory(new PropertyValueFactory<>("appointmentTitle"));
        appointDescriptionCol.setCellValueFactory(new PropertyValueFactory<>("appointmentDescription"));
        appointContactCol.setCellValueFactory(new PropertyValueFactory<>("appointmentTechName"));
        appointTypeCol.setCellValueFactory(new PropertyValueFactory<>("appointmentType"));
        appointDateCol.setCellValueFactory(new PropertyValueFactory<>("appointmentStart"));
        appointEndDateCol.setCellValueFactory(new PropertyValueFactory<>("appointmentEnd"));
        appointCustIdCol.setCellValueFactory(new PropertyValueFactory<>("appointmentCustomerName"));
        appointAnimalCol.setCellValueFactory(new PropertyValueFactory<>("appointmentAnimalName"));
        appointUserIdCol.setCellValueFactory(new PropertyValueFactory<>("appointmentUserName"));
        appointTable.setItems(AppointmentDAO.getAppointmentList());

        weekId.setCellValueFactory(new PropertyValueFactory<>("appointmentId"));
        weekTitle.setCellValueFactory(new PropertyValueFactory<>("appointmentTitle"));
        weekDescription.setCellValueFactory(new PropertyValueFactory<>("appointmentDescription"));
        weekVetTech.setCellValueFactory(new PropertyValueFactory<>("appointmentTechName"));
        weekType.setCellValueFactory(new PropertyValueFactory<>("appointmentType"));
        weekStart.setCellValueFactory(new PropertyValueFactory<>("appointmentStart"));
        weekEnd.setCellValueFactory(new PropertyValueFactory<>("appointmentEnd"));
        weekOwner.setCellValueFactory(new PropertyValueFactory<>("appointmentCustomerName"));
        weekAnimal.setCellValueFactory(new PropertyValueFactory<>("appointmentAnimalName"));
        weekUser.setCellValueFactory(new PropertyValueFactory<>("appointmentUserName"));
        weekTable.setItems(AppointmentDAO.getWeeklyAppointments());
        weekTable.setPlaceholder(new Label("Currently, no appointments exist for the remainder of this week."));

        monthId.setCellValueFactory(new PropertyValueFactory<>("appointmentId"));
        monthTitle.setCellValueFactory(new PropertyValueFactory<>("appointmentTitle"));
        monthDescription.setCellValueFactory(new PropertyValueFactory<>("appointmentDescription"));
        monthVetTech.setCellValueFactory(new PropertyValueFactory<>("appointmentTechName"));
        monthType.setCellValueFactory(new PropertyValueFactory<>("appointmentType"));
        monthStart.setCellValueFactory(new PropertyValueFactory<>("appointmentStart"));
        monthEnd.setCellValueFactory(new PropertyValueFactory<>("appointmentEnd"));
        monthOwner.setCellValueFactory(new PropertyValueFactory<>("appointmentCustomerName"));
        monthAnimal.setCellValueFactory(new PropertyValueFactory<>("appointmentAnimalName"));
        monthUser.setCellValueFactory(new PropertyValueFactory<>("appointmentUserName"));
        monthTable.setItems(AppointmentDAO.getMonthlyAppointments());
        monthTable.setPlaceholder(new Label("Currently, no appointments exist for the remainder of this month."));
        searchBar();
    }


    /**
     * Delete button on appointment screen. If no appointment is selected - an error will display
     *
     * @param actionEvent event for delete button
     */
    public void actionAppointDelete(ActionEvent actionEvent) {
        Appointment selectedAppointment = appointTable.getSelectionModel().getSelectedItem();

        if (selectedAppointment == null) {
            helper.ErrorMsg.getError(12);
        } else {
            Alert confirmRemoval = new Alert(Alert.AlertType.WARNING);
            confirmRemoval.setTitle("Alert");
            confirmRemoval.setContentText("Would you like to remove the selected appointment?");
            confirmRemoval.getButtonTypes().clear();
            confirmRemoval.getButtonTypes().addAll(ButtonType.CANCEL, ButtonType.OK);
            confirmRemoval.showAndWait();
            if (confirmRemoval.getResult() == ButtonType.OK) {
                Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION);
                confirmation.setTitle("Alert");
                confirmation.setContentText("Appointment ID# " + appointTable.getSelectionModel().getSelectedItem().getAppointmentId() + " for " + appointTable.getSelectionModel().getSelectedItem().getAppointmentType() + " has been cancelled.");
                confirmation.getButtonTypes().clear();
                confirmation.getButtonTypes().addAll(ButtonType.OK);
                confirmation.showAndWait();

                AppointmentDAO.deleteAppointment(appointTable.getSelectionModel().getSelectedItem().getAppointmentId());
                AppointmentList = AppointmentDAO.getAppointmentList();
                appointTable.setItems(AppointmentList);
                weekTable.refresh();
                weekTable.setItems(AppointmentDAO.getWeeklyAppointments());
                weekTable.refresh();
                monthTable.refresh();
                monthTable.setItems(AppointmentDAO.getMonthlyAppointments());
                monthTable.refresh();
                searchBar();

            } else if (confirmRemoval.getResult() == ButtonType.CANCEL) {
                confirmRemoval.close();
            }
        }
    }

    /**
     * Action event for add button on appointment screen. When pressed - the user will be redirected to the add appointments
     * screen.
     *
     * @param actionEvent event for add button
     * @throws IOException addresses unhandled exception
     */
    public void actionAppointAdd(ActionEvent actionEvent) throws IOException {
        Stage st = new Stage();
        st.initModality(Modality.APPLICATION_MODAL);
        st.setTitle("Happy Friends Scheduling");
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/AppointmentsAdd.fxml"));
        Region root = (Region) loader.load();
        Scene scene = new Scene(root);
        st.setScene(scene);
        scene.getStylesheets().add(this.getClass().getResource("/test.css").toExternalForm());
        AppointmentsAdd mainController = loader.<AppointmentsAdd>getController();
        st.centerOnScreen();
        st.show();
        st.setOnHidden(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent windowEvent) {
                appointTable.refresh();
                appointTable.setItems(AppointmentDAO.getAppointmentList());
                appointTable.refresh();
                weekTable.refresh();
                weekTable.setItems(AppointmentDAO.getWeeklyAppointments());
                weekTable.refresh();
                monthTable.refresh();
                monthTable.setItems(AppointmentDAO.getMonthlyAppointments());
                monthTable.refresh();
                searchBar();
            }
        });
    }

    /**
     * Action event for update button on appointment screen. If no appointment is selected, the user will be presented
     * with an error message stating to select an appointment. Once an appointment is selected - the user will be
     * redirected to the modify appointments screen.
     *
     * @param actionEvent event for update button
     * @throws IOException  addresses unhandled exception
     * @throws SQLException addresses unhandled SQL exception
     */
    public void actionAppointUpdate(ActionEvent actionEvent) throws IOException, SQLException {
        if (appointTable.getSelectionModel().getSelectedItem() != null) {
            Stage st = new Stage();
            st.initModality(Modality.APPLICATION_MODAL);
            st.setTitle("Happy Friends Scheduling");
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/AppointmentsModify.fxml"));
            Region root = (Region) loader.load();
            Scene scene = new Scene(root);
            st.setScene(scene);
            scene.getStylesheets().add(this.getClass().getResource("/test.css").toExternalForm());
            AppointmentsModify MCController = loader.getController();
            MCController.getAppointmentInfo(appointTable.getSelectionModel().getSelectedItem());
            st.centerOnScreen();
            st.show();
            st.setOnHidden(new EventHandler<WindowEvent>() {
                @Override
                public void handle(WindowEvent windowEvent) {
                    appointTable.refresh();
                    appointTable.setItems(AppointmentDAO.getAppointmentList());
                    appointTable.refresh();
                    weekTable.refresh();
                    weekTable.setItems(AppointmentDAO.getWeeklyAppointments());
                    weekTable.refresh();
                    monthTable.refresh();
                    monthTable.setItems(AppointmentDAO.getMonthlyAppointments());
                    monthTable.refresh();
                    searchBar();
                }
            });
        } else {
            helper.ErrorMsg.getError(12);
        }
    }

    /**
     * Initializes the 3 appointment table data into the associated table with navigation via tabs.
     *
     * @param url            URL
     * @param resourceBundle ResourceBundle
     */


    /**
     * Action event for button to return the user back to the main menu
     *
     * @param actionEvent event for backToMenu button
     * @throws IOException addresses unhandled exceptions
     */
    public void backToMenu(ActionEvent actionEvent) throws IOException {
        Parent parent = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("../view/Menu.fxml")));
        Scene scene = new Scene(parent);
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        parent.getStylesheets().add(this.getClass().getResource("/test.css").toExternalForm());
        stage.setScene(scene);
        stage.centerOnScreen();
        stage.show();

    }


    public void monthlyAppointments(ActionEvent actionEvent) {
        filterField.clear();
        appointTable.setItems(AppointmentDAO.getMonthlyAppointments());
    }

    public void weeklyAppointments(ActionEvent actionEvent) {
        filterField.clear();
        appointTable.setItems(AppointmentDAO.getWeeklyAppointments());
        appointTable.setPlaceholder(new Label("Currently, no appointments exist for the remainder of this week."));
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
        Parent parent = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/view/Reports.fxml")));
        Scene scene = new Scene(parent);
        parent.getStylesheets().add(this.getClass().getResource("/test.css").toExternalForm());
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.centerOnScreen();
        stage.show();
    }


    public void clearSearch(ActionEvent actionEvent) {
        filterField.clear();
    }

    public void searchBar(){
        allAppointmentSearch.clear();
        allAppointmentSearch.addAll(AppointmentDAO.getAppointmentList());
        FilteredList<Appointment> filteredData = new FilteredList<>(allAppointmentSearch, b -> true);

        filterField.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(appointment -> {

                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }

                String lowerCaseFilter = newValue.toLowerCase();

                if (appointment.getAppointmentTitle().toLowerCase().indexOf(lowerCaseFilter) != -1) {
                    return true;
                } else if (appointment.getAppointmentDescription().toLowerCase().indexOf(lowerCaseFilter) != -1) {
                    return true;
                } else if (appointment.getAppointmentAnimalName().toLowerCase().indexOf(lowerCaseFilter) != -1) {
                    return true;
                } else if (appointment.getAppointmentType().toLowerCase().indexOf(lowerCaseFilter) != -1) {
                    return true;
                } else if (appointment.getAppointmentCustomerName().toLowerCase().indexOf(lowerCaseFilter) != -1) {
                    return true;
                } else if (appointment.getAppointmentTechName().toLowerCase().indexOf(lowerCaseFilter) != -1) {
                    return true;
                } else if (String.valueOf(appointment.getAppointmentId()).indexOf(lowerCaseFilter) != -1)
                    return true;
                else
                    return false;
            });
        });

        SortedList<Appointment> sortedData = new SortedList<>(filteredData);

        sortedData.comparatorProperty().bind(appointTable.comparatorProperty());

        appointTable.setItems(sortedData);
    }
}
