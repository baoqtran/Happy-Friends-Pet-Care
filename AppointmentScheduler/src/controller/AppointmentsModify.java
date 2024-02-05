package controller;

import DAO.*;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.Stage;
import model.*;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ResourceBundle;

/**
 * AppointmentsModify class
 *
 */
public class AppointmentsModify implements Initializable {

    @FXML
    private ComboBox typeCombo;
    @FXML
    private ComboBox<Animal> animalCombo;
    @FXML
    private TextField appointmentIDTextField;
    @FXML
    private TextField appointmentTitleTextField;
    @FXML
    private TextField appointmentDescriptionTextField;
    @FXML
    private TextField appointmentLocationTextField;
    @FXML
    private TextField appointmentTypeTextField;
    @FXML
    private ComboBox<VetTech> contactCombo;
    @FXML
    private Button saveButton;
    @FXML
    private Button cancelButton;
    @FXML
    private DatePicker startDatePicker;
    @FXML
    private ComboBox<LocalTime> startTimeCombo;
    @FXML
    private DatePicker endDatePicker;
    @FXML
    private ComboBox<LocalTime> endTimeCombo;
    @FXML
    private ComboBox<User> userCombo;
    @FXML
    private ComboBox<Customer> customerCombo;
    private final int noOfDaysToAdd = 0;
    /**
     * Initializing combo boxes for contacts, customers and users and setting the values
     *
     * @param url            URL
     * @param resourceBundle ResourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        ObservableList<VetTech> contactList = VetTechDAO.getAllTechs();
        contactCombo.setItems(contactList);
        contactCombo.setVisibleRowCount(10);

        ObservableList<Customer> customerList = CustomerDAO.getCustomerList();
        customerCombo.setItems(customerList);
        customerCombo.setVisibleRowCount(10);

        ObservableList<User> userList = UserDAO.getUserList();
        userCombo.setItems(userList);
        userCombo.setVisibleRowCount(10);

        ObservableList<Animal> animalList = AnimalDAO.getAllAnimals();
        animalCombo.setItems(animalList);
        animalCombo.setVisibleRowCount(10);
    }

    /**
     * Action for save button that will get values from each field and updating the appointment
     *
     * @param actionEvent event for save button
     * @throws IOException addresses unhandled exception
     */
    public void actionSaveButton(ActionEvent actionEvent) throws IOException, SQLException {
        int appointmentId = Integer.parseInt(appointmentIDTextField.getText());
        String appointmentTitle = appointmentTitleTextField.getText();
        String appointmentDescription = appointmentDescriptionTextField.getText();
        String type = typeCombo.getValue().toString();


        // Handling null pointer exception and alert message
        VetTech contact = contactCombo.getValue();
        if (contact == null) {
            helper.ErrorMsg.getError(24);
        }
        int appointmentContact = contactCombo.getValue().getVetTechId();
        int appointmentAnimalId = animalCombo.getValue().getAnimalId();

        // Handling null pointer exception and alert message
        LocalDate startPicker = startDatePicker.getValue();
        if (startPicker == null) {
            helper.ErrorMsg.getError(18);
            return;
        }

        // Handling null pointer exception and alert message
        LocalTime startTime = startTimeCombo.getValue();
        if (startTime == null) {
            helper.ErrorMsg.getError(19);
            return;
        }
        LocalDateTime appointmentStart = LocalDateTime.of(startDatePicker.getValue(), startTimeCombo.getValue());

        // Handling null pointer exception and alert message
        LocalDate endPicker = endDatePicker.getValue();
        if (endPicker == null) {
            helper.ErrorMsg.getError(20);
            return;
        }

        // Handling null pointer exception and alert message
        LocalTime end = endTimeCombo.getValue();
        if (end == null) {
            helper.ErrorMsg.getError(21);
            return;
        }
        LocalDateTime appointmentEnd = LocalDateTime.of(endDatePicker.getValue(), endTimeCombo.getValue());

        // Handling null pointer exception and alert message
        Customer customer = customerCombo.getValue();
        if (customer == null) {
            helper.ErrorMsg.getError(22);
            return;
        }
        int appointmentCustomerId = customerCombo.getValue().getCustomerId();

        // Handling null pointer exception and alert message
        User user = userCombo.getValue();
        if (user == null) {
            helper.ErrorMsg.getError(23);
            return;
        }
        int appointmentUserId = userCombo.getValue().getUserID();

        // Checking and verifying text-fields are valid and not blank or empty
        if (appointmentTitle.isBlank() || appointmentTitle.isEmpty()) {
            helper.ErrorMsg.getError(8);
        } else if (appointmentDescription.isBlank() || appointmentDescription.isEmpty()) {
            helper.ErrorMsg.getError(9);
        }
        LocalDateTime updateStartDateTime = LocalDateTime.of(startPicker, startTime);
        LocalDateTime updateEndDateTime = LocalDateTime.of(endPicker, end);
        ObservableList<Appointment> getAllAppointments = AppointmentDAO.getAppointmentList();
        Boolean Overlap = true;

        for (Appointment A : getAllAppointments) {
            LocalDate cDate = startDatePicker.getValue();
            LocalDateTime cStart = A.getAppointmentStart();
            LocalDateTime cEnd = A.getAppointmentEnd();
            LocalDateTime proposedStart = LocalDateTime.of(startPicker, startTime);
            LocalDateTime proposedEnd = LocalDateTime.of(endPicker, end);

            if (A.getAppointmentCustomerId() != appointmentCustomerId || A.getAppointmentId() == appointmentId) {
                continue;
            }
            if ((cStart.isAfter(proposedStart) || cStart.isEqual(proposedStart)) && (cStart.isBefore(proposedEnd))) {
                Alert alert15 = new Alert(Alert.AlertType.ERROR);
                alert15.setTitle("Not Valid");
                alert15.setContentText("The appointment selected overlaps an existing appointment ");
                alert15.showAndWait();
                return;
            }
            if (cEnd.isAfter(proposedStart) && (cEnd.isBefore(proposedEnd) || cEnd.isEqual(proposedEnd))) {
                Alert alert15 = new Alert(Alert.AlertType.ERROR);
                alert15.setTitle("Not Valid");
                alert15.setContentText("The appointment selected overlaps an existing appointment ");
                alert15.showAndWait();
                return;
            }
            if ((cStart.isBefore(proposedStart) || cStart.isEqual(proposedStart)) && ((cEnd.isAfter(proposedEnd) || cEnd.isEqual(proposedEnd)))) {
                Alert alert15 = new Alert(Alert.AlertType.ERROR);
                alert15.setTitle("Not Valid");
                alert15.setContentText("The appointment selected overlaps an existing appointment ");
                alert15.showAndWait();
                return;
            }
        }
        if (Appointment.businessHours(appointmentStart, appointmentEnd)) {
            return;
        } else {
            AppointmentDAO.updateAppointment(appointmentId, appointmentTitle, appointmentDescription, type, appointmentStart, appointmentEnd, appointmentCustomerId, appointmentUserId, appointmentContact, appointmentAnimalId);
            backToAppointments(actionEvent);

        }
        helper.ErrorMsg.confirmation(4);
//        else if (Appointment.businessHours(appointmentStart, appointmentEnd)) {
//            return;
//        } else if (Appointment.overlapCheck(appointmentCustomerId, appointmentStart, appointmentEnd)) {
//            return;
//        }

    }

    /**
     * Action event for cancel button that redirects the user back to the main appointments page
     *
     * @param actionEvent event for cancel button
     * @throws IOException addresses unhandled exceptions
     */
    public void actionCancelButton(ActionEvent actionEvent) throws IOException {
        backToAppointments(actionEvent);

    }

    /**
     * Displays the appointment information in the appropriate field in the form
     *
     * @param appointment references appointment object
     * @throws SQLException addresses unhandled SQL exceptions
     */
    public void getAppointmentInfo(Appointment appointment) throws SQLException {
        startTimeCombo.setItems(Appointment.getTimes());
        endTimeCombo.setItems(Appointment.getTimes());
        appointmentIDTextField.setText(Integer.toString(appointment.getAppointmentId()));
        appointmentTitleTextField.setText(appointment.getAppointmentTitle());
        appointmentDescriptionTextField.setText(appointment.getAppointmentDescription());
        typeCombo.setValue(appointment.getAppointmentType());

        startDatePicker.setValue(appointment.getAppointmentStart().toLocalDate());
        startTimeCombo.setValue(appointment.getAppointmentStart().toLocalTime());
        startDatePicker.valueProperty().addListener((ov, oldValueDate, newValueDate) -> endDatePicker.setValue(newValueDate.plusDays(noOfDaysToAdd)));
        startTimeCombo.valueProperty().addListener((observableValue, oldValueTime, newValueTime) -> endTimeCombo.setValue(newValueTime.plusMinutes(30)));
        endDatePicker.setValue(appointment.getAppointmentEnd().toLocalDate());
        endTimeCombo.setValue(appointment.getAppointmentEnd().toLocalTime());
        VetTech d = VetTechDAO.returnTechList(appointment.getAppointmentTech());
        contactCombo.setValue(d);
        Customer c = CustomerDAO.returnCustomerList(appointment.getAppointmentCustomerId());
        customerCombo.setValue(c);
        User u = UserDAO.returnUserId(appointment.getAppointmentUserId());
        userCombo.setValue(u);
        Animal a = AnimalDAO.returnAnimalId(appointment.getAppointmentAnimalId());
        animalCombo.setValue(a);
    }

    public void backToAppointments(ActionEvent actionEvent) throws IOException {
        Stage stage = (Stage) ((Button) actionEvent.getSource()).getScene().getWindow();
        stage.close();
    }

    public void actionAnimalLoad(ActionEvent actionEvent) {
        Customer c = customerCombo.getValue();
        try {
            animalCombo.setItems(AnimalDAO.displayAnimals(c.getCustomerId()));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public long plusMinutes(long MinutesToAdd) {
        try {
            return MinutesToAdd;
        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        }
    }
}
