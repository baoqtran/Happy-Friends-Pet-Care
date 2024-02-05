package DAO;

import helper.JDBC;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.Appointment;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Locale;

/**
 * Appointment SQL queries to obtain appointment information from database
 *
 */
public class AppointmentDAO {
    String format = "MM/dd/yy";
    SimpleDateFormat format1 = new SimpleDateFormat(format, Locale.US);

    /**
     * SQL Query to obtain an observables list of all appointments in the database.
     *
     * @return appointmentList
     */
    public static ObservableList<Appointment> getAppointmentList() {
        ObservableList<Appointment> appointmentList = FXCollections.observableArrayList();
        try {

            String sql = "SELECT appointments.Appointment_ID, appointments.Title, appointments.Description, appointments.Location, appointments.Type, appointments.Start, appointments.End, appointments.Customer_ID,  appointments.Tech_ID, appointments.Animal_ID, animals.Animal_ID, animals.Name, customers.Customer_ID, customers.Customer_Name, vet_techs.Tech_ID, vet_techs.Tech_Name, users.User_ID, users.User_Name FROM appointments JOIN customers ON appointments.Customer_ID = customers.Customer_ID JOIN vet_techs ON appointments.Tech_ID = vet_techs.Tech_ID JOIN users ON appointments.User_ID = users.User_ID JOIN animals ON appointments.Animal_ID = animals.Animal_ID ORDER BY appointments.Appointment_ID";
            PreparedStatement ps = JDBC.connection.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                int appointmentId = rs.getInt("Appointment_ID");
                String appointmentTitle = rs.getString("Title");
                String appointmentDescription = rs.getString("Description");
                int appointmentTechId = rs.getInt("Tech_ID");
                String appointmentTechName = rs.getString("Tech_Name");
                String appointmentType = rs.getString("Type");
                LocalDateTime appointmentStart = rs.getTimestamp("Start").toLocalDateTime();
                LocalDateTime appointmentEnd = rs.getTimestamp("End").toLocalDateTime();
                int appointmentCustomerId = rs.getInt("Customer_ID");
                String appointmentCustomerName = rs.getString("Customer_Name");
                int appointmentAnimalId = rs.getInt("Animal_ID");
                String appointmentAnimalName = rs.getString("Name");
                int appointmentUserId = rs.getInt("User_ID");
                String appointmentUserName = rs.getString("User_Name");
                String appointmentLocation = rs.getString("Location");

                Appointment c = new Appointment(appointmentId, appointmentTitle, appointmentDescription, appointmentTechId, appointmentTechName,
                        appointmentType, appointmentStart, appointmentEnd, appointmentCustomerId, appointmentCustomerName, appointmentAnimalId, appointmentAnimalName, appointmentUserId, appointmentUserName);
                appointmentList.add(c);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return appointmentList;
    }

    /**
     * SQL query to update the selected modified appointment in the database
     *
     * @param appointmentId          appointment ID
     * @param appointmentTitle       appointment title
     * @param appointmentDescription appointment description
     * @param appointmentType        appointment type
     * @param appointmentStart       appointment start date/time
     * @param appointmentEnd         appointment end date/time
     * @param appointmentCustomerId  associated appointment customer ID
     * @param appointmentUserId      associated appointment user ID
     */
    public static void updateAppointment(int appointmentId, String appointmentTitle, String appointmentDescription, String appointmentType, LocalDateTime appointmentStart, LocalDateTime appointmentEnd, int appointmentCustomerId, int appointmentUserId, int appointmentTech, int appointmentAnimalId) {
        try {

            String sql = "UPDATE appointments SET Title = ?, Description = ?, Type = ?, Start = ?, End = ?, Customer_ID = ?, User_ID = ?, Tech_ID = ?, Animal_ID = ? WHERE Appointment_ID = ?";
            PreparedStatement updateAppointment = JDBC.connection.prepareStatement(sql);

            updateAppointment.setString(1, appointmentTitle);
            updateAppointment.setString(2, appointmentDescription);
            updateAppointment.setString(3, appointmentType);
            updateAppointment.setTimestamp(4, Timestamp.valueOf(appointmentStart));
            updateAppointment.setTimestamp(5, Timestamp.valueOf(appointmentEnd));
            updateAppointment.setInt(6, appointmentCustomerId);
            updateAppointment.setInt(7, appointmentUserId);
            updateAppointment.setInt(8, appointmentTech);
            updateAppointment.setInt(9, appointmentAnimalId);
            updateAppointment.setInt(10, appointmentId);

            updateAppointment.execute();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * SQL query to add new appointment into the database. Database auto-increments appointment ID.
     *
     * @param appointmentTitle       appointment title
     * @param appointmentDescription appointment description
     * @param appointmentType        appointment Type
     * @param appointmentStart       appointment Start date/time
     * @param appointmentEnd         appointment End date/time
     * @param appointmentCustomerId  associated appointment customer ID
     * @param appointmentUserId      associated appointment user ID
     * @throws SQLException addresses unhandled SQL exception
     */
    public static void addAppointment(String appointmentTitle, String appointmentDescription, String appointmentType, LocalDateTime appointmentStart, LocalDateTime appointmentEnd, int appointmentCustomerId, int appointmentUserId, int appointmentTech, int appointmentAnimalId) throws SQLException {
        String sql = "INSERT INTO appointments (Title, Description, Type, Start, End, Customer_ID, User_ID, Tech_ID, Animal_ID) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        PreparedStatement insertAppoint = JDBC.connection.prepareStatement(sql);

        insertAppoint.setString(1, appointmentTitle);
        insertAppoint.setString(2, appointmentDescription);
        insertAppoint.setString(3, appointmentType);
        insertAppoint.setTimestamp(4, Timestamp.valueOf(appointmentStart));
        insertAppoint.setTimestamp(5, Timestamp.valueOf(appointmentEnd));
        insertAppoint.setInt(6, appointmentCustomerId);
        insertAppoint.setInt(7, appointmentUserId);
        insertAppoint.setInt(8, appointmentTech);
        insertAppoint.setInt(9, appointmentAnimalId);
        insertAppoint.executeUpdate();
    }

    /**
     * SQL Query to get weekly appointments for the next 7 days and add to ObservableList.
     *
     * @return weeklyList
     */
    public static ObservableList<Appointment> getWeeklyAppointments() {
        ObservableList<Appointment> weeklyList = FXCollections.observableArrayList();
        try {
            String sql = "SELECT appointments.Appointment_ID, appointments.Title, appointments.Description, appointments.Location, appointments.Type, appointments.Start, appointments.End, appointments.Customer_ID, appointments.Tech_ID, appointments.Animal_ID, animals.Animal_ID, animals.Name, customers.Customer_ID, customers.Customer_Name, vet_techs.Tech_ID, vet_techs.Tech_Name, users.User_ID, users.User_Name FROM appointments JOIN customers ON appointments.Customer_ID = customers.Customer_ID JOIN vet_techs ON appointments.Tech_ID = vet_techs.Tech_ID JOIN users ON appointments.User_ID = users.User_ID JOIN animals ON appointments.Animal_ID = animals.Animal_ID WHERE YEARWEEK(START) = YEARWEEK(NOW()) ORDER BY appointments.Appointment_ID";
            //   String sql = "SELECT * FROM appointments INNER JOIN vet_techs ON appointments.Tech_ID = vet_techs.Tech_ID WHERE YEARWEEK(START) = YEARWEEK(NOW()) ORDER BY appointments.Appointment_ID";
            PreparedStatement ps = JDBC.connection.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                int appointmentId = rs.getInt("Appointment_ID");
                String appointmentTitle = rs.getString("Title");
                String appointmentDescription = rs.getString("Description");
                int appointmentTech = rs.getInt("Tech_ID");
                String appointmentTechName = rs.getString("Tech_Name");
                String appointmentType = rs.getString("Type");
                LocalDateTime appointmentStart = rs.getTimestamp("Start").toLocalDateTime();
                LocalDateTime appointmentEnd = rs.getTimestamp("End").toLocalDateTime();
                int appointmentCustomerId = rs.getInt("Customer_ID");
                String appointmentCustomerName = rs.getString("Customer_Name");
                int appointmentAnimalId = rs.getInt("Animal_ID");
                String appointmentAnimalName = rs.getString("Name");
                int appointmentUserId = rs.getInt("User_ID");
                String appointmentUserName = rs.getString("User_Name");
                String appointmentLocation = rs.getString("Location");
                Appointment weekly = new Appointment(appointmentId, appointmentTitle, appointmentDescription, appointmentTech, appointmentTechName,
                        appointmentType, appointmentStart, appointmentEnd, appointmentCustomerId, appointmentCustomerName, appointmentAnimalId, appointmentAnimalName, appointmentUserId, appointmentUserName);

                weeklyList.add(weekly);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return weeklyList;
    }

    /**
     * SQL query to delete appointments from database by appointment ID.
     *
     * @param appointmentId appointment ID
     */
    public static void deleteAppointment(int appointmentId) {
        try {
            String sqldelete = "DELETE FROM appointments WHERE Appointment_ID = ?";
            PreparedStatement deleteAppoint = JDBC.connection.prepareStatement(sqldelete);
            deleteAppoint.setInt(1, appointmentId);
            deleteAppoint.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * SQL Query to select monthly appointments
     *
     * @return monthlyList
     */
    public static ObservableList<Appointment> getMonthlyAppointments() {
        ObservableList<Appointment> monthlyList = FXCollections.observableArrayList();
        try {
            String sql = "SELECT appointments.Appointment_ID, appointments.Title, appointments.Description, appointments.Location, appointments.Type, appointments.Start, appointments.End, appointments.Customer_ID, appointments.Tech_ID, appointments.Animal_ID, animals.Animal_ID, animals.Name, customers.Customer_ID, customers.Customer_Name, vet_techs.Tech_ID, vet_techs.Tech_Name, users.User_ID, users.User_Name FROM appointments JOIN customers ON appointments.Customer_ID = customers.Customer_ID JOIN vet_techs ON appointments.Tech_ID = vet_techs.Tech_ID JOIN users ON appointments.User_ID = users.User_ID JOIN animals ON appointments.Animal_ID = animals.Animal_ID WHERE MONTH(START) = MONTH(NOW()) ORDER BY appointments.Appointment_ID";
            PreparedStatement ps = JDBC.connection.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                int appointmentId = rs.getInt("Appointment_ID");
                String appointmentTitle = rs.getString("Title");
                String appointmentDescription = rs.getString("Description");
                int appointmentTech = rs.getInt("Tech_ID");
                String appointmentTechName = rs.getString("Tech_Name");
                String appointmentType = rs.getString("Type");
                LocalDateTime appointmentStart = rs.getTimestamp("Start").toLocalDateTime();
                LocalDateTime appointmentEnd = rs.getTimestamp("End").toLocalDateTime();
                int appointmentCustomerId = rs.getInt("Customer_ID");
                String appointmentCustomerName = rs.getString("Customer_Name");
                int appointmentAnimalId = rs.getInt("Animal_ID");
                String appointmentAnimalName = rs.getString("Name");
                int appointmentUserId = rs.getInt("User_ID");
                String appointmentUserName = rs.getString("User_Name");
                String appointmentLocation = rs.getString("Location");
                Appointment monthly = new Appointment(appointmentId, appointmentTitle, appointmentDescription, appointmentTech, appointmentTechName,
                        appointmentType, appointmentStart, appointmentEnd, appointmentCustomerId, appointmentCustomerName, appointmentAnimalId, appointmentAnimalName, appointmentUserId, appointmentUserName);

                monthlyList.add(monthly);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return monthlyList;
    }

    /**
     * SQL query to get appointments associated with user during login to display appointments within 15 minutes
     *
     * @param userId user ID
     * @return userAppointments
     */
    public static ObservableList<Appointment> getUserAppointments(int userId) {
        ObservableList<Appointment> userAppointments = FXCollections.observableArrayList();
        try {
            String sqlStatement = "SELECT * FROM Appointments WHERE User_ID  = '" + userId + "'";
            PreparedStatement ps = JDBC.connection.prepareStatement(sqlStatement);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                int appointmentId = rs.getInt("Appointment_ID");
                String appointmentTitle = rs.getString("Title");
                String appointmentDescription = rs.getString("Description");
                int appointmentTech = rs.getInt("Tech_ID");
                String appointmentType = rs.getString("Type");
                LocalDateTime appointmentStart = rs.getTimestamp("Start").toLocalDateTime();
                LocalDateTime appointmentEnd = rs.getTimestamp("End").toLocalDateTime();
                int appointmentCustomerId = rs.getInt("Customer_ID");
                int appointmentUserId = rs.getInt("User_ID");
                String appointmentLocation = rs.getString("Location");
                Appointment results = new Appointment(appointmentId, appointmentTitle, appointmentDescription, appointmentTech,
                        appointmentType, appointmentStart, appointmentEnd, appointmentCustomerId, appointmentUserId, appointmentLocation);
                userAppointments.add(results);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return userAppointments;
    }

    public static ObservableList<Appointment> getTechAppointment(int vetTechId) {
        ObservableList<Appointment> techAppointment = FXCollections.observableArrayList();
        try {
            String sqlStatement = "SELECT * FROM appointments JOIN vet_techs ON appointments.Tech_ID = vet_techs.Tech_ID WHERE appointments.Tech_ID  = '" + vetTechId + "'";
            PreparedStatement ps = JDBC.connection.prepareStatement(sqlStatement);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                int appointmentId = rs.getInt("Appointment_ID");
                String appointmentTitle = rs.getString("Title");
                String appointmentDescription = rs.getString("Description");
                int appointmentTech = rs.getInt("Tech_ID");
                String appointmentTechName = rs.getString("Tech_Name");
                String appointmentType = rs.getString("Type");
                LocalDateTime appointmentStart = rs.getTimestamp("Start").toLocalDateTime();
                LocalDateTime appointmentEnd = rs.getTimestamp("End").toLocalDateTime();
                int appointmentCustomerId = rs.getInt("Customer_ID");
                int appointmentUserId = rs.getInt("User_ID");
                String appointmentLocation = rs.getString("Location");
                Appointment results = new Appointment(appointmentId, appointmentTitle, appointmentDescription, appointmentTech, appointmentTechName,
                        appointmentType, appointmentStart, appointmentEnd, appointmentCustomerId, appointmentUserId, appointmentLocation);
                techAppointment.add(results);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return techAppointment;
    }

    /**
     * SQL query to count appointment types and provide distinct results for reports table
     *
     * @return appointmentListType
     */
    public static ObservableList<Appointment> appointmentType() {
        ObservableList<Appointment> appointmentListType = FXCollections.observableArrayList();
        try {
            String sql = "SELECT Type, Count(*) AS NUM FROM appointments GROUP BY Type";
            PreparedStatement ps = JDBC.connection.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                String appointmentType = rs.getString("Type");
                int appointmentTypeTotal = rs.getInt("NUM");
                Appointment results = new Appointment(appointmentType, appointmentTypeTotal);
                appointmentListType.add(results);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return appointmentListType;
    }

    /**
     * SQL query to get the month and number of appointments per month for reports
     *
     * @return appointmentTypeMonthTotal
     */
    public static ObservableList<Appointment> getAppointmentTypeMonth() {
        ObservableList<Appointment> appointmentTypeMonthTotal = FXCollections.observableArrayList();
        try {
            String sql = "SELECT DISTINCT(MONTHNAME(Start)) AS Month, Count(*) AS NUM FROM appointments GROUP BY Month";
            PreparedStatement ps = JDBC.connection.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                String appointmentType = rs.getString("Month");
                int appointmentTypeTotal = rs.getInt("NUM");
                Appointment results = new Appointment(appointmentType, appointmentTypeTotal);
                appointmentTypeMonthTotal.add(results);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return appointmentTypeMonthTotal;
    }


}