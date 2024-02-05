package DAO;

import helper.JDBC;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.Note;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
/**
 * Note SQL queries to obtain note information join with animal information from database
 *
 */
public class NoteDAO {
    public static ObservableList<Note> getAssociatedNotes() {
        ObservableList<Note> noteList = FXCollections.observableArrayList();
        try {
            String sql = "SELECT * FROM notes JOIN animals ON notes.Animal_ID = animals.Animal_ID ORDER BY notes.Notes_ID";
            PreparedStatement ps = JDBC.connection.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                int notesId = rs.getInt("Notes_ID");
                String notes = rs.getString("Notes");
                LocalDateTime created = rs.getTimestamp("Created").toLocalDateTime();
                LocalDateTime lastUpdated = rs.getTimestamp("Last_Updated").toLocalDateTime();
                int animalId = rs.getInt("Animal_ID");
                Note n = new Note(notesId, notes, created, lastUpdated, animalId);
                noteList.add(n);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return noteList;
    }

    public static ObservableList<Note> associatedNotes(int animalId) {
        ObservableList<Note> noteLists = FXCollections.observableArrayList();
        try {
            String sql = "SELECT * FROM notes JOIN animals ON notes.Animal_ID = animals.Animal_ID WHERE animals.Animal_ID = " + animalId;
            PreparedStatement ps = JDBC.connection.prepareStatement(sql);
            ps.execute();
            ResultSet rs = ps.getResultSet();
            while (rs.next()) {
                int notesId = rs.getInt("Notes_ID");
                String notes = rs.getString("Notes");
                LocalDateTime created = rs.getTimestamp("Created").toLocalDateTime();
                LocalDateTime lastUpdated = rs.getTimestamp("Last_Updated").toLocalDateTime();
                Note n = new Note(notesId, notes, created, lastUpdated, animalId);
                noteLists.add(n);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return noteLists;
    }

    public static void addNote(String notes, LocalDateTime createdDate, LocalDateTime lastUpdated, int animalId) throws SQLException {
        String sql = "INSERT INTO notes (notes, Created, Last_Updated, Animal_ID) VALUES (?, ?, ?, ?)";
        PreparedStatement insertNote = JDBC.connection.prepareStatement(sql);
        insertNote.setString(1, notes);
        insertNote.setTimestamp(2, Timestamp.valueOf(createdDate));
        insertNote.setTimestamp(3, Timestamp.valueOf(lastUpdated));
        insertNote.setInt(4, animalId);

        insertNote.executeUpdate();
    }
    public static void updateNote(int notesId, String notes, LocalDateTime lastUpdated, int animalId) {
        try {
            String sql = "UPDATE notes SET Notes = ?, Last_Updated = ?, Animal_ID = ? WHERE Notes_ID = ?";
            PreparedStatement updateNote = JDBC.connection.prepareStatement(sql);
            updateNote.setString(1, notes);
            updateNote.setTimestamp(2, Timestamp.valueOf(lastUpdated));
            updateNote.setInt(3, animalId);
            updateNote.setInt(4, notesId);
            updateNote.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void deleteNote(int noteId) {
        try {
            String sqldelete = "DELETE FROM notes WHERE Notes_ID = ?";
            PreparedStatement deleteNote = JDBC.connection.prepareStatement(sqldelete);
            deleteNote.setInt(1, noteId);
            deleteNote.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}