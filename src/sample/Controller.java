package sample;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.SQLException;
import java.util.Objects;
import java.util.ResourceBundle;

public class Controller {

    @FXML
    public Label loginMessage;

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private TextField loginLoginFld;

    @FXML
    private TextField loginPasswordFld;

    @FXML
    private Button loginOkBtn;

    @FXML
    void initialize() throws SQLException, ClassNotFoundException, MalformedURLException {
        // Получаем соединение с БД
        DBAccessor accessor = new DBAccessor("com.mysql.jdbc.Driver", "jdbc:mysql://localhost/pets", "root", "");

        // Получаем ссылки на файлы разметок экранов
        URL director = new File("src/sample/director.fxml").toURI().toURL();
        URL manager = new File("src/sample/manager.fxml").toURI().toURL();
        URL doctor = new File("src/sample/doctor.fxml").toURI().toURL();
        URL cleaner = new File("src/sample/cleaner.fxml").toURI().toURL();

        // Обработка нажатия на кнопку Вход
        EventHandler<ActionEvent> buttonHandler = new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                // Получаем логин и пароль из соответствующих полей
                String login = loginLoginFld.getText();
                String password = loginPasswordFld.getText();
                String role = null;
                try {
                    // Пытаемся залогиниться. Метод login вернёт либо роль, либо null, если не логин и пароль невалидны
                    role = accessor.login(login, password);
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
                System.out.println(role);
                if (role != null){
                    try {
                        // Записываем данные операционной системы при логине
                        accessor.setOSData();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    // Сравниваем полученную роль с базовыми ролями
                    switch (role){
                        case "директор":
                            // Создаём экземпляр окна. Дальше в него будет устанавливаться разметка
                            // в зависимости от роли
                            Parent root;
                            try {
                                // Загружаем разметку
                                root = FXMLLoader.load(director);
                                Stage stage = new Stage();
                                stage.setTitle("Директор");
                                // Устанавливаем разметку на окно
                                stage.setScene(new Scene(root, 800, 700));
                                stage.show();
                                ((Node)(event.getSource())).getScene().getWindow().hide();
                            }
                            catch (IOException e) {
                                e.printStackTrace();
                            }
                            break;
                        case "администратор":
                            try {
                                root = FXMLLoader.load(manager);
                                Stage stage = new Stage();
                                stage.setTitle("Менеджер");
                                stage.setScene(new Scene(root, 800, 700));
                                stage.show();
                                ((Node)(event.getSource())).getScene().getWindow().hide();
                            }
                            catch (IOException e) {
                                e.printStackTrace();
                            }
                            break;
                        case "врач":
                        case "терапевт":
                            try {
                                root = FXMLLoader.load(doctor);
                                Stage stage = new Stage();
                                stage.setTitle("Доктор");
                                stage.setScene(new Scene(root, 800, 700));
                                stage.show();
                                ((Node)(event.getSource())).getScene().getWindow().hide();
                            }
                            catch (IOException e) {
                                e.printStackTrace();
                            }
                            break;
                        case "уборщик":
                            try {
                                root = FXMLLoader.load(cleaner);
                                Stage stage = new Stage();
                                stage.setTitle("Уборщик");
                                stage.setScene(new Scene(root, 800, 500));
                                stage.show();
                                ((Node)(event.getSource())).getScene().getWindow().hide();
                            }
                            catch (IOException e) {
                                e.printStackTrace();
                            }
                            break;
                    }
                } else {
                    loginMessage.setText("Логин или пароль неверны");
                }
                event.consume();
            }
        };
        // Вешаем обработчик нажатия на кнопку
        loginOkBtn.setOnAction(buttonHandler);
    }
}