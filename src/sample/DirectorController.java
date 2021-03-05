package sample;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import sample.DBAccessor;

import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class DirectorController {
    private DBAccessor accessor = new DBAccessor("com.mysql.cj.jdbc.Driver", "jdbc:mysql://localhost/pets", "root", "");

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Label directorUsersList;

    @FXML
    private Label directorRecsList;

    @FXML
    private TextField directorAddName;

    @FXML
    private TextField directorAddLogin;

    @FXML
    private TextField directorAddPassword;

    @FXML
    private TextField directorAddRole;

    @FXML
    private Button directorAddBtn;

    public DirectorController() throws SQLException, ClassNotFoundException {
    }

    @FXML
    void initialize() throws SQLException, ClassNotFoundException {
        updateUsers();
        EventHandler<ActionEvent> buttonHandler = new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                try {
                    accessor.addUser(directorAddName.getText(), directorAddLogin.getText(), directorAddPassword.getText(), directorAddRole.getText());
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
                try {
                    updateUsers();
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }
        };
        directorAddBtn.setOnAction(buttonHandler);
        updateRecs();
    }

    private void updateUsers() throws SQLException {
        ArrayList<String> users = accessor.getUsers();
        StringBuilder printableUsers = new StringBuilder();
        for(String user : users){
            printableUsers.append(user).append("\n");
        }
        directorUsersList.setText(printableUsers.toString());
    }

    private void updateRecs() throws SQLException {
        ArrayList<String> recs = accessor.getReceptions();
        StringBuilder printableRecs = new StringBuilder();
        for(String rec : recs){
            printableRecs.append(rec).append("\n");
        }
        directorRecsList.setText(printableRecs.toString());
    }
}