package DAO;

import helper.JDBC;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.VetTech;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
/**
 * Vet Tech SQL queries to obtain vet tech information from database
 *
 */

public class VetTechDAO {

    public static ObservableList<VetTech> getAllTechs() {
        ObservableList<VetTech> techList = FXCollections.observableArrayList();
        try {
            String sql = "SELECT Tech_ID, Tech_Name FROM vet_techs";
            PreparedStatement techs = JDBC.connection.prepareStatement(sql);
            ResultSet rs = techs.executeQuery();

            while (rs.next()) {
                int vetTechId = rs.getInt("Tech_ID");
                String vetTechName = rs.getString("Tech_Name");
                VetTech d = new VetTech(vetTechId, vetTechName);
                techList.add(d);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return techList;
    }


    public static VetTech returnTechList(int vetTechId) {
        try {
            String sql = "SELECT * FROM vet_techs WHERE Tech_ID = ?";
            PreparedStatement ps = JDBC.connection.prepareStatement(sql);
            ps.setInt(1, vetTechId);

            ps.execute();

            ResultSet rs = ps.getResultSet();

            rs.next();
            int searchedVetTechId = rs.getInt("Tech_ID");
            String vetTechName = rs.getString("Tech_Name");
            VetTech s = new VetTech(searchedVetTechId, vetTechName);
            return s;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    public static int returnVetTech(String vetTechName) throws SQLException {
        int techId = 0;
        String sql = "SELECT * FROM vet_techs WHERE Tech_Name = ?";
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ps.setString(1, vetTechName);
        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
            techId = rs.getInt("Tech_ID");
        }
        return techId;
    }
}
