package com.example.login;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.fxml.Initializable;
import javafx.stage.Stage;
import org.mindrot.jbcrypt.BCrypt;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.Properties;
import java.util.ResourceBundle;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HelloController implements Initializable {
    // variabily of the login
    @FXML private PasswordField passwordField;
    @FXML private TextField textField;
    @FXML private TextField textfield;
    @FXML private CheckBox checkBox;
    @FXML private Button btn;// login
    @FXML private Button sign ;//->signup

    //variabily of create new account
    @FXML private TextField firstName ;
    @FXML private TextField lastName;
    @FXML private TextField city;
    @FXML private PasswordField newpasswordfield;
    @FXML private  PasswordField confirmpasswordfield;
    @FXML private TextField email;
    @FXML private Hyperlink hyperlink;
    @FXML private Hyperlink modify;

    // variabily of the modify password
    @FXML private CheckBox checkBoxNew;
    @FXML private CheckBox checkBoxconfirm;
    @FXML private Button verified;
    @FXML private PasswordField PaswordFieldNew;
    @FXML private PasswordField PaswordFieldConfirm;
    @FXML private TextField TextFieldNew;
    @FXML private TextField TextFieldConfirm;
    @FXML private TextField Email;
    @FXML private Button confirm;


    @FXML
    protected void onHelloButtonClick() {

        btn.setOnAction(ActionEvent -> login());
        sign.setOnAction(ActionEvent -> signup());
        verified.setOnAction(ActionEvent -> modifypassword());

//        confirm.setOnAction(event -> updatepassword());


    }




    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        if (checkBox != null) {

            checkBox.setOnAction(event -> {

                if (checkBox.isSelected()) {
                    textField.setText(passwordField.getText());
                    passwordField.setVisible(false);
                    textField.setVisible(true);


                } else {
                    passwordField.setText(textField.getText());
                    textField.setVisible(false);
                    passwordField.setVisible(true);

                }
            });

        }

        if(checkBoxNew != null) {
            checkBoxNew.setOnAction(event -> {
                if (checkBoxNew.isSelected()) {
                    TextFieldNew.setText(PaswordFieldNew.getText());
                    PaswordFieldNew.setVisible(false);
                    TextFieldNew.setVisible(true);
                }else {
                    PaswordFieldNew.setText(TextFieldNew.getText());
                    TextFieldNew.setVisible(false);
                    PaswordFieldNew.setVisible(true);
                }
            });
        }

        if(checkBoxconfirm != null) {
            checkBoxconfirm.setOnAction(event -> {
                if (checkBoxconfirm.isSelected()) {
                    TextFieldConfirm.setText(PaswordFieldConfirm.getText());
                    PaswordFieldConfirm.setVisible(false);
                    TextFieldConfirm.setVisible(true);
                }else {
                    PaswordFieldConfirm.setText(TextFieldConfirm.getText());
                    TextFieldConfirm.setVisible(false);
                    PaswordFieldConfirm.setVisible(true);
                }
            });
        }
    }

    public void login() {
        PreparedStatement st = null;
        ResultSet result = null;
        String password;

        if (checkBox.isSelected()) {
            password = textField.getText();
        } else {
            password = passwordField.getText();
        }

        String username = textfield.getText();
        Connection conn = ConnexioDB.getConnexion();

        if (username.isEmpty() || password.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "There are empty fields", ButtonType.PREVIOUS);
            alert.show();
            return;
        } else if (!isValidEmail(username)) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Invalid email format");
            alert.showAndWait();
            return;
        }

        try {
            st = conn.prepareStatement("SELECT id, password FROM users WHERE email = ?");
            st.setString(1, username);
            result = st.executeQuery();

            if (result.next()) {
                int userId = result.getInt("id");
                String hashedPassword = result.getString("password");
                if (BCrypt.checkpw(password, hashedPassword)) {
                    User user = new User(username,hashedPassword,userId);
                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Login successful", ButtonType.OK);
                    alert.show();
                    loadTodoApp(user);
                } else {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION, "Incorrect password", ButtonType.OK);
                    alert.showAndWait();
                }
            } else {
                Alert alert = new Alert(Alert.AlertType.WARNING, "User not found", ButtonType.PREVIOUS);
                alert.show();
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            try {
                if (result != null) result.close();
                if (st != null) st.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
@FXML
    private void openSecondFile(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("Person-infos.fxml"));
        Parent root = loader.load();
        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        stage.show();
    }
@FXML
    private void openfileFXML() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("modify.fxml"));
        Parent root = loader.load();
        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        stage.show();
    }
@FXML
    private void openfileFXML2() throws IOException, SQLException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("modify2.fxml"));
        Parent root = loader.load();
        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        stage.show();
    }

    public void signup() {

         if(firstName.getText().isEmpty() && lastName.getText().isEmpty() && email.getText().isEmpty() && newpasswordfield.getText().isEmpty() && confirmpasswordfield.getText().isEmpty()) {
            Alert alert =new Alert(Alert.AlertType.INFORMATION, "Please fill all the fields", ButtonType.OK);
            alert.show();
        }
        else if (!isValidEmail(email.getText())) {
            Alert alert =new Alert(Alert.AlertType.ERROR,"Username incorrect");
            alert.showAndWait();
        }

        else {
            if (newpasswordfield.getText().equals(confirmpasswordfield.getText())) {

                PreparedStatement prepared = null;
                ResultSet resultSet = null;
                Connection conn = ConnexioDB.getConnexion();


                try {
                    prepared = conn.prepareStatement("INSERT INTO users(username,password,first_name,last_name,city,email) VALUES (?,?,?,?,?,?)");
                    String hashedPassword = BCrypt.hashpw(confirmpasswordfield.getText(), BCrypt.gensalt());
                    prepared.setString(1, email.getText());
                    prepared.setString(2, hashedPassword);
                    prepared.setString(3, firstName.getText());
                    prepared.setString(4, lastName.getText());
                    prepared.setString(5, city.getText());
                    prepared.setString(6, email.getText());

                    int affectation = prepared.executeUpdate();

                    if (affectation > 0) {

                        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Account create to successfully", ButtonType.OK);
                        alert.show();
                    } else {
                        Alert alert = new Alert(Alert.AlertType.ERROR, "error in the creation of your account ", ButtonType.OK);
                        alert.show();
                    }
                } catch (Exception e) {
                    throw new RuntimeException(e);
                } finally {
                    try {
                        if (prepared != null) prepared.close();
                        if (conn != null) conn.close();
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                }
            } else {
                Alert alert = new Alert(Alert.AlertType.WARNING, "new Password is different of the confirm password", ButtonType.PREVIOUS);
                alert.show();
            }
        }
    }




    @FXML
    private  void modifypassword() {
        PreparedStatement prepared = null;
        Connection conn = null;
        ResultSet rs = null;
        String password;
        if(checkBoxNew.isSelected()) {
            password=TextFieldNew.getText();
        }else {
            password=PaswordFieldNew.getText();
        }
        if (!isValidEmail(Email.getText())) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Invalid email format", ButtonType.PREVIOUS);
            alert.show();
        }else {

            try {
                conn = ConnexioDB.getConnexion();
                prepared = conn.prepareStatement("SELECT email FROM users WHERE email = ?");
                prepared.setString(1, Email.getText());
                rs = prepared.executeQuery();

                if (rs.next()) {
                    String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());
                    String query = "UPDATE users SET password = ? WHERE email = ?";

                    prepared = conn.prepareStatement(query);
                    prepared.setString(1, hashedPassword);
                    prepared.setString(2, Email.getText());
                    int affectation = prepared.executeUpdate();

                    if (affectation > 0) {
                        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Account update to successfully", ButtonType.OK);
                        alert.showAndWait();
                    } else {
                        Alert alert = new Alert(Alert.AlertType.ERROR, "error in the update of your account ", ButtonType.PREVIOUS);
                        alert.showAndWait();
                    }
                } else {
                    Alert alert = new Alert(Alert.AlertType.ERROR, "email doesn't exist", ButtonType.PREVIOUS);
                    alert.show();
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            } finally {
                try {
                    if (prepared != null) prepared.close();
                    if (conn != null) conn.close();
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        }



    }

//    @FXML
//    private void updatepassword() {
//
//        System.out.println("email: " + email);
//        PreparedStatement prepared = null;
//        Connection conn = null;
//
//        try {
//            if (PaswordFieldNew.getText().equals(PaswordFieldConfirm.getText())) {
//                conn = ConnexioDB.getConnexion();
//                String query = "UPDATE users SET password=? WHERE username=?";
//                prepared = conn.prepareStatement(query);
//                prepared.setString(1, PaswordFieldNew.getText());
//                prepared.setString(2, Email.getText());
//                int affectation = prepared.executeUpdate();
//                System.out.println("affectation= " + affectation);
//                if (affectation > 0) {
//                    Alert alert = new Alert(Alert.AlertType.INFORMATION, "Password updated successfully", ButtonType.OK);
//                    alert.show();
//                } else {
//                    Alert alert = new Alert(Alert.AlertType.ERROR, "error in the updating password", ButtonType.PREVIOUS);
//                    alert.show();
//                }
//            } else {
//                Alert alert = new Alert(Alert.AlertType.ERROR, "new password isn't equal the confirm password", ButtonType.PREVIOUS);
//                alert.show();
//            }
//        } catch (SQLException e) {
//            Alert alert = new Alert(Alert.AlertType.ERROR, "Database Error An error occurred while updating password", ButtonType.PREVIOUS);
//            alert.show();
//        } finally {
//            try {
//                if (prepared != null) prepared.close();
//                if (conn != null) conn.close();
//            } catch (Exception e) {
//                throw new RuntimeException(e);
//            }
//        }
//    }

    private boolean isValidEmail(String email) {
        String emailRegex= "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";
        Pattern pattern= Pattern.compile(emailRegex);

        if(email==null){
            Alert alert = new Alert(Alert.AlertType.ERROR, "Invalid email address", ButtonType.OK);
            alert.show();
            return false;
        }

        Matcher matcher = pattern.matcher(email);

        return matcher.matches();
    }


    private void loadTodoApp(User user){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("todo.fxml"));
            Parent root = loader.load();
            TodoController controller = loader.getController();
            controller.setUser(user);

            Stage stage = (Stage) textfield.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("To-Do List");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}


