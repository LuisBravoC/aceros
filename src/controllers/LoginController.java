package controllers;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import config.ConnectionUtil;
import services.AuthService;
import services.AuthService.LoginResult;
import services.SessionManager;

public class LoginController implements Initializable {

    @FXML
    private Button btnIngresar;
    @FXML
    public AnchorPane anchRoot;
    @FXML
    private TextField tf_userid;
    @FXML
    private PasswordField tf_password;
    @FXML
    private Label lbConLogin;

    private static final Logger LOGGER = Logger.getLogger(LoginController.class.getName());

    double x = 0, y = 0;

    @FXML
    void pressed(MouseEvent event) {
        x = event.getSceneX();
        y = event.getSceneY();
    }

    @FXML
    void dragged(MouseEvent event) {
        Node node = (Node) event.getSource();
        Stage stage = (Stage) node.getScene().getWindow();
        stage.setX(event.getScreenX() - x);
        stage.setY(event.getScreenY() - y);
    }

    @FXML
    void login(MouseEvent event) {}

    @FXML
    void handleButtonAction(ActionEvent event) {
        if (event.getSource() == btnIngresar) {
            Node node = (Node) event.getSource();
            Stage stage = (Stage) node.getScene().getWindow();
            attemptLogin(stage);
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Probe connection for the status label — no static field, no side-effect in constructor
        boolean connected = false;
        try (Connection conn = ConnectionUtil.getConnection()) {
            connected = (conn != null);
        } catch (SQLException ex) {
            LOGGER.log(Level.WARNING, "Error de conexion al iniciar Login", ex);
        }
        if (connected) {
            lbConLogin.setTextFill(Color.GREEN);
            lbConLogin.setText("HAY CONEXIÓN CON EL SERVICIO");
            LOGGER.log(Level.INFO, "Conexion establecida");
        } else {
            lbConLogin.setTextFill(Color.TOMATO);
            lbConLogin.setText("ERROR DE CONEXIÓN");
            LOGGER.log(Level.WARNING, "No se pudo establecer conexion con la BD");
        }

        tf_password.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                Stage stage = (Stage) tf_password.getScene().getWindow();
                attemptLogin(stage);
            }
        });
    }

    /**
     * Ejecuta el flujo de login: valida credenciales y navega al dashboard si son correctas.
     * Centraliza la lógica duplicada entre el botón y el handler de teclado.
     */
    private void attemptLogin(Stage stage) {
        String userId = tf_userid.getText();
        String password = tf_password.getText();

        LoginResult result = AuthService.login(userId, password);

        switch (result) {
            case SUCCESS:
                navigateToDashboard(stage);
                break;
            case EMPTY_FIELDS:
                showAlert("Campos vacíos", "Usuario o contraseña están vacíos",
                        "Asegúrese de ingresar su usuario y contraseña.");
                break;
            case INVALID_CREDENTIALS:
                showAlert("Datos incorrectos", "Uno o más datos son incorrectos",
                        "El usuario o la contraseña son incorrectos, vuelva a intentarlo.");
                tf_userid.clear();
                tf_password.clear();
                break;
            default:
                showAlert("Error de conexión", "No se pudo conectar con el servicio",
                        "Revise la conexión con la base de datos e intente de nuevo.");
                tf_userid.clear();
                tf_password.clear();
                break;
        }
    }

    private void navigateToDashboard(Stage stage) {
        try {
            stage.close();
            Parent root = FXMLLoader.load(getClass().getResource("/aceros/dashboard.fxml"));
            Scene scene = new Scene(root);
            scene.setFill(Color.TRANSPARENT);
            stage.setScene(scene);
            new animatefx.animation.ZoomIn(root).play();
            stage.show();
            LOGGER.log(Level.INFO, "Inicio sesión usuario: {0}", SessionManager.getInstance().getUserId());
        } catch (IOException ex) {
            LOGGER.log(Level.SEVERE, "Error cargando dashboard.fxml", ex);
        }
    }

    private void showAlert(String title, String header, String content) {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }

    /** @deprecated Usar {@link SessionManager#getUserId()} directamente. */
    @Deprecated
    public static String getSesion() {
        return SessionManager.getInstance().getUserId();
    }
}

