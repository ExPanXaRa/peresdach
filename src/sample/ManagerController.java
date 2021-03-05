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

public class ManagerController {
    private DBAccessor accessor = new DBAccessor("com.mysql.cj.jdbc.Driver", "jdbc:mysql://localhost/pets", "root", "");

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private TextField managerAddName;

    @FXML
    private TextField managerAddDocId;

    @FXML
    private TextField managerAddDate;

    @FXML
    private TextField managerAddReason;

    @FXML
    private Button managerAddBtn;

    @FXML
    private Label managerRecsList;

    public ManagerController() throws SQLException, ClassNotFoundException {
    }

    @FXML
    void initialize() throws SQLException, ClassNotFoundException {
        updateRecs();
        EventHandler<ActionEvent> buttonHandler = new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                try {
                    accessor.addRec(managerAddName.getText(), Integer.parseInt(managerAddDocId.getText()), managerAddDate.getText(), managerAddReason.getText());
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
                try {
                    updateRecs();
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }
        };
        managerAddBtn.setOnAction(buttonHandler);
    }

    private void updateRecs() throws SQLException {
        ArrayList<String> recs = accessor.getReceptions();
        StringBuilder printableRecs = new StringBuilder();
        for(String rec : recs){
            printableRecs.append(rec).append("\n");
        }
        managerRecsList.setText(printableRecs.toString());
    }
}