package sample;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.sql.*;
import java.util.ArrayList;

public class DBAccessor {
    // Соединение с базой данных
    private final Connection connection ;

    // Конструктор по умолчанию. Принимает название драйвера и параметры для подключения.
    public DBAccessor(String driverClassName,
                      String dbURL, String user,
                      String password) throws ClassNotFoundException, SQLException {
        Class.forName(driverClassName);
        connection = DriverManager.getConnection(dbURL, user, password);
        if (connection == null){
            System.out.println("connection is null");
        }
    }

    // Метод принимает логин и пароль и возвращает роль. Если залогиниться не удалось - null
    public String login(String login, String password) throws SQLException {
        PreparedStatement s = connection.prepareStatement("SELECT * FROM users WHERE login=? AND password=?");
        s.setString(1, login);
        s.setString(2, password);
        ResultSet res = s.executeQuery();
        while (res.next()) {
            Statement rs = connection.createStatement();
            ResultSet resultSet = rs.executeQuery("SELECT name FROM roles WHERE id="+res.getInt("role"));
            resultSet.next();
            return resultSet.getString("name");
        }
        return null;
    }

    // Возвращает список пользователей в особом формате для отображения
    public ArrayList<String> getUsers() throws SQLException {
        ArrayList<String> users = new ArrayList<>();
        Statement s = connection.createStatement();
        ResultSet res = s.executeQuery("SELECT * FROM users");
        int roleId = 0;
        while (res.next()) {
            roleId = res.getInt("role");
            Statement rs = connection.createStatement();
            ResultSet resultSet = rs.executeQuery("SELECT name FROM roles WHERE id="+res.getInt("role"));
            resultSet.next();
            String role = resultSet.getString("name");
            users.add(""+res.getString("name")+" на должности "+role);
        }
        return users;
    }

    // Возвращает список записей к врачам в особом формате для отображения
    public ArrayList<String> getReceptions() throws SQLException {
        ArrayList<String> rec = new ArrayList<>();
        Statement s = connection.createStatement();
        ResultSet res = s.executeQuery("SELECT * FROM reception");
        while (res.next()) {
            rec.add(
                    "Запись №"+res.getInt("id")
                            + ". Клиент "+res.getString("client")
                            + " записан на "+ res.getString("date")
                            + " Причина обращения: "+res.getString("reason"));
        }
        return rec;
    }

    // Возвращает список записей к врачу по id в особом формате для
    public ArrayList<String> getDoctorReceptions(int docId) throws SQLException {
        ArrayList<String> rec = new ArrayList<>();
        Statement s = connection.createStatement();
        ResultSet res = s.executeQuery("SELECT * FROM reception WHERE doctorId="+docId);
        while (res.next()) {
            rec.add(
                    "Запись №"+res.getInt("id")
                            + ". Клиент "+res.getString("client")
                            + " записан на "+ res.getString("date")
                            + " Причина обращения: "+res.getString("reason"));
        }
        return rec;
    }

    // Записывает в базу данных параметры операционной системы.
    // Омя и версия ОС, mac-адрес, имя хоста в локальной сети, временная зона, регион, язык
    public void setOSData() throws Exception {
        String osName = System.getProperty("os.name");

        InetAddress localHost = InetAddress.getLocalHost();
        NetworkInterface ni = NetworkInterface.getByInetAddress(localHost);
        byte[] hardwareAddress = ni.getHardwareAddress();
        String[] hexadecimal = new String[hardwareAddress.length];
        for (int i = 0; i < hardwareAddress.length; i++) {
            hexadecimal[i] = String.format("%02X", hardwareAddress[i]);
        }
        String macAddress = String.join("-", hexadecimal);
        InetAddress ip = InetAddress.getLocalHost();
        String hostname = ip.getHostName();

        String timezone = System.getProperty("user.timezone");
        String country = System.getProperty("user.country");
        String language = System.getProperty("user.language");

        PreparedStatement s = connection.prepareStatement("INSERT INTO os_data VALUES(null, ?, ?, ?, ?, ?, ?)");
        s.setString(1, osName);
        s.setString(2, macAddress);
        s.setString(3, hostname);
        s.setString(4, timezone);
        s.setString(5, country);
        s.setString(6, language);
        s.executeUpdate();
    }

    // Созадёт нового пользователя. Принимает Фамилию и имя, логин, апроль
    public void addUser(String name, String login, String password, String role) throws SQLException {
        PreparedStatement s = connection.prepareStatement("INSERT INTO users VALUES(null, ?, ?, ?, ?)");
        s.setString(1, name);
        s.setString(2, login);
        s.setString(3, password);
        s.setInt(4, Integer.parseInt(role));
        s.executeUpdate();
    }

    // Создаёт запись к врачу. Принимает имя и фамилию клиента, id доктора, дату и причину обращения
    public void addRec(String name, int docId, String date, String reason) throws SQLException {
        PreparedStatement s = connection.prepareStatement("INSERT INTO reception VALUES(null, ?, ?, ?, ?)");
        s.setString(1, name);
        s.setInt(2, docId);
        s.setString(3, date);
        s.setString(4, reason);
        s.executeUpdate();
    }
}
