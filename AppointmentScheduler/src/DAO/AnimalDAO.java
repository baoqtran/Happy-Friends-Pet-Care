package DAO;

import helper.JDBC;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.Animal;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import static helper.JDBC.connection;
/**
 * Animal SQL queries to obtain animal information from database
 *
 */
public class AnimalDAO {

    public static ObservableList<Animal> getAllAnimals() {
        ObservableList<Animal> animalList = FXCollections.observableArrayList();
        try {
            String sql = "SELECT animals.Animal_ID, animals.Name, animals.Type, animals.Age, animals.Breed, animals.Weight, animals.Customer_ID, customers.Customer_ID, customers.Customer_Name FROM animals JOIN customers ON customers.Customer_ID = animals.Customer_ID ORDER BY animals.Animal_ID ASC";
            PreparedStatement ps = connection.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                int animalId = rs.getInt("Animal_ID");
                String animalName = rs.getString("Name");
                String animalType = rs.getString("Type");
                int animalAge = rs.getInt("Age");
                String animalBreed = rs.getString("Breed");
                int animalWeight = rs.getInt("Weight");
                int animalOwner = rs.getInt("Customer_ID");
                String animalOwnerName = rs.getString("Customer_Name");

                Animal animal = new Animal(animalId, animalName, animalType, animalAge, animalBreed,
                        animalWeight, animalOwner, animalOwnerName);
                animalList.add(animal);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return animalList;
    }

    public static ObservableList<Animal> justAnimals() {
        ObservableList<Animal> justAnimalsList = FXCollections.observableArrayList();
        try {
            String sql = "SELECT DISTINCT(animals.Animal_ID), animals.Name, animals.Type, animals.Age, animals.Breed, animals.Weight, animals.Customer_ID, customers.Customer_ID, customers.Customer_Name = animals.Animal_ID JOIN customers ON customers.Customer_ID = animals.Customer_ID ";
            PreparedStatement ps = connection.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                int animalId = rs.getInt("Animal_ID");
                String animalName = rs.getString("Name");
                String animalType = rs.getString("Type");
                int animalAge = rs.getInt("Age");
                String animalBreed = rs.getString("Breed");
                int animalWeight = rs.getInt("Weight");
                int animalOwner = rs.getInt("Customer_ID");
                String animalOwnerName = rs.getString("Customer_Name");

                Animal animal = new Animal(animalId, animalName, animalType, animalAge, animalBreed,
                        animalWeight, animalOwner, animalOwnerName);
                justAnimalsList.add(animal);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return justAnimalsList;
    }
    public static void updateAnimal(int animalId, String animalName, String animalType, int animalAge, String animalBreed, int animalWeight, int animalOwner)  {
        try {
            String sql = "UPDATE animals SET Name = ?, Type = ?, Age = ?, Breed = ?, Weight = ?, Customer_ID = ? WHERE Animal_ID = ?";
            PreparedStatement updateAnimal = connection.prepareStatement(sql);
            updateAnimal.setString(1, animalName);
            updateAnimal.setString(2, animalType);
            updateAnimal.setInt(3, animalAge);
            updateAnimal.setString(4, animalBreed);
            updateAnimal.setInt(5, animalWeight);
            updateAnimal.setInt(6, animalOwner);
            updateAnimal.setInt(7, animalId);
            updateAnimal.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static void addAnimal(String animalName, String animalType, int animalAge, String animalBreed, int animalWeight, int animalOwner) throws SQLException {
        String sql = "INSERT INTO animals (Name, Type, Age, Breed, Weight, Customer_ID) VALUES (?, ?, ?, ?, ?, ?)";
        int userId;

        PreparedStatement insertAnimal = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
        insertAnimal.setString(1, animalName);
        insertAnimal.setString(2, animalType);
        insertAnimal.setInt(3, animalAge);
        insertAnimal.setString(4, animalBreed);
        insertAnimal.setInt(5, animalWeight);
        insertAnimal.setInt(6, animalOwner);
        insertAnimal.executeUpdate();
        try (ResultSet generatedKeys = insertAnimal.getGeneratedKeys()) {
            if (generatedKeys.next()) {
                userId = generatedKeys.getInt(1);
                System.out.println("UserId is " + userId);
            } else {
                throw new SQLException("User insertion has problem. No ID returned");
            }
        }
        insertAnimal.close();
    }

    public static void deleteAnimal(int animalId) {
        try {
            String sqldelete = "DELETE FROM animals WHERE Animal_ID = ?";
            PreparedStatement deleteAnimal = JDBC.connection.prepareStatement(sqldelete);
            deleteAnimal.setInt(1, animalId);
            deleteAnimal.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static int lastId() {
        int newSheetNumber = 0;
        String sql = "SELECT MAX(Animal_ID) FROM animals";
        try {
            PreparedStatement ps = connection.prepareStatement(sql);

            ResultSet rs = ps.executeQuery();
            if (!rs.next()) {
                newSheetNumber = 1;
            } else {
                newSheetNumber = rs.getInt(1);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return newSheetNumber;
    }

    public static ObservableList<Animal> displayAnimals(int customerId) throws SQLException {
        ObservableList<Animal> animalOptions = FXCollections.observableArrayList();

        String sql = "SELECT * FROM animals WHERE Customer_ID = " + customerId;
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ps.execute();
        ResultSet rs = ps.getResultSet();

        while (rs.next()) {
            int animalId = rs.getInt("Animal_ID");
            String animalName = rs.getString("Name");
            customerId = rs.getInt("Customer_ID");
            String type = rs.getString("Type");
            int age = rs.getInt("Age");
            String breed = rs.getString("Breed");
            int weight = rs.getInt("Weight");

            Animal c = new Animal(animalId, animalName, customerId, type, age, breed, weight);
            animalOptions.add(c);
        }
        return animalOptions;
    }

    public static Animal returnAnimalId(int animalId) {
        try {
            String sql = "SELECT Animal_ID, Name FROM animals WHERE Animal_ID = ?";
            PreparedStatement ps = JDBC.connection.prepareStatement(sql);
            ps.setInt(1, animalId);
            ps.execute();

            ResultSet rs = ps.getResultSet();

            rs.next();
            int searchedAnimalId = rs.getInt("Animal_ID");
            String animalName = rs.getString("Name");
            Animal u = new Animal(searchedAnimalId, animalName);
            return u;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static ObservableList<Animal> breedTotals() {
        ObservableList<Animal> breedTotals = FXCollections.observableArrayList();
        try {
            String sql = "SELECT animals.Breed, GROUP BY(animals.Breed) AS Count FROM animals";
            PreparedStatement ps = JDBC.connection.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                String animalBreed2 = rs.getString("Breed");
                int breedTotal = rs.getInt("Count");
                Animal results = new Animal(animalBreed2, breedTotal);
                breedTotals.add(results);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return breedTotals;
    }
}
