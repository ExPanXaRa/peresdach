package sample;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class DoctorController {
    private DBAccessor accessor = new DBAccessor("com.mysql.cj.jdbc.Driver", "jdbc:mysql://localhost/pets", "root", "");

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Label doctorRecsList;

    public DoctorController() throws SQLException, ClassNotFoundException {

    }

    @FXML
    void initialize() throws SQLException, ClassNotFoundException {
        updateRecs();
    }

    private void updateRecs() throws SQLException {
        ArrayList<String> recs = accessor.getReceptions();
        StringBuilder printableRecs = new StringBuilder();
        for(String rec : recs){
            printableRecs.append(rec).append("\n");
        }
        doctorRecsList.setText(printableRecs.toString());
    }
}