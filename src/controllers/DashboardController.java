/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers;

import config.ConnectionUtil;
import dao.UsuariosDao;
import models.UsuarioDetalle;
import java.nio.file.Files;
import java.nio.file.Path;
import dao.MaterialesDao;
import services.MaterialesService;
import dao.AlturasDao;
import services.AlturasService;
import dao.CalibresDao;
import services.CalibresService;
import dao.RombosDao;
import services.RombosService;
import dao.ProduccionDao;
import dao.HistorialDao;
import services.ProduccionService;
import services.HistorialService;
import dao.LookupDao;
import services.LookupService;
import java.awt.Desktop;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.sql.Connection;
import java.util.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Month;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Consumer;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.logging.Level;
import java.util.logging.Logger;
import util.ImageUtils;
import util.DateUtils;
import controllers.helpers.CatalogoUtils;
import controllers.helpers.ProfileBinder;
import services.UsuariosService;
import models.Empleados;
import models.Materiales;
import models.Alturas;
import models.Calibres;
import models.Rombos;
import models.Historial;
import models.ProduccionSemanal;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;
import javafx.stage.Window;
import javax.imageio.ImageIO;
import javax.swing.text.Document;
import services.ReporteService;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import net.sf.jasperreports.view.JasperViewer;

/**
 * FXML Controller class
 *
 * @author LuisBravo
 */
public class DashboardController implements Initializable {
    private static final Logger LOGGER = Logger.getLogger(DashboardController.class.getName());
    
    @FXML
    private Button btnPefil;
    @FXML
    private Button btnProduccion;
    @FXML
    private Button btnInicio;
    @FXML
    private Button btnEmpleados;
    @FXML
    private Button btnMateriales;
    @FXML
    private Button btnExit;
    
    @FXML
    private Pane pnBlanco;
    @FXML
    private Pane pnPerfil;
    @FXML
    private Pane pnInicio;
    @FXML
    private Pane pnEmpleados;
    @FXML
    private Pane pnProduccion;
    @FXML
    private Pane pnMateriales;
    @FXML
    private Pane pnDashboard;
    @FXML
    private Pane pnCambiarContraseña;
    
    @FXML
    private Label lbTitulo;    
    
    //Pantalla perfil
    
    @FXML
    private Button btncambiarContraseña;
    @FXML
    private Button btnGuardarContraseña;
    @FXML
    private Button btnVolverContraseña;
    @FXML
    private TextField tbContraseñaActual;
    @FXML
    private TextField tbContraseñaNueva;
    @FXML
    private TextField tbContraseñaRepetir;
    
    @FXML
    private Label lbCodigoUsuario;
    @FXML
    private Label lbNombreEmpleado;
    @FXML
    private Label lbAPaternoEmpleado;
    @FXML
    private Label lbAMaternoEmpleado;
    @FXML
    private Label lbCurp;
    @FXML
    private Label lbRfc;
    @FXML
    private Label lbNss;
    @FXML
    private Label lbFechaNacimiento;
    @FXML
    private Label lbFechaContratacio;
    @FXML
    private Label lbEmailEmpleado;
    @FXML
    private Label lbGenero;
    @FXML
    private Label lbTipoUsuario;
    @FXML
    private Label lbSueldoEmpleado;
    @FXML
    private Label lbMetodoPago;
    @FXML
    private Label lbBanco;
    @FXML
    private Label lbNCuenta;
    @FXML
    private Label lbPeriodoPago;
    @FXML
    private Label lbContrato;
    @FXML
    private Label lbPais;
    @FXML
    private Label lbEstado;
    @FXML
    private Label lbLocalidad;
    @FXML
    private Label lbColonia;
    @FXML
    private Label lbNExterior;
    @FXML
    private Label lbCiudad;
    @FXML
    private Label lbCalle;
    @FXML
    private Label lbCodigoPostal;
    @FXML
    private Label lbNInterior;
        
    //Pantalla empleado
    @FXML
    private Button btnEditarEmpleado;
    @FXML
    private Button btnNuevoEmpleado;
    @FXML
    private Button btnEliminarEmpleado;
    @FXML
    private Button btnActualizarEmpleado;
    @FXML
    private Label lbHCodigo;
    @FXML
    private Label lbHNombre;
    @FXML
    private Label lbHFecha;
    @FXML
    private Label lbHDomicilio;
    
    //Pantalla agregar empleado
    @FXML
    private Button btnVolverEmpleados;
    @FXML
    private Button btnGuardarEmpleado;
    @FXML
    private Button btnModificarEmpleado;
    @FXML
    private Button btnSubirImagen;
    @FXML
    private Label lbPath;
    @FXML
    private ImageView imgPerfil;
    @FXML
    private ImageView btnImagenPerfil;
    private Image backup = new Image("/icons/upload.png");
    private Image sinperfil = new Image("/icons/sin_perfil.png");
    private Image image;
    private ImageView imageView;
    private FileChooser fileChosser;
    private File file = null;
    private FileInputStream fis;
    private final Desktop desktop = Desktop.getDesktop();
    
    @FXML
    private Pane pnAgregarEmpleados;
    @FXML
    private TextField tbCodigoUsuarioAgregar;
    @FXML
    private TextField tbNombreEmpleado;
    @FXML
    private TextField tbAPaternoEmpleado;
    @FXML
    private TextField tbAMaternoEmpleado;
    @FXML
    private TextField tbCurp;
    @FXML
    private TextField tbRfc;
    @FXML
    private TextField tbNss;
    @FXML
    private TextField tbEmailEmpleado;
    @FXML
    private TextField tbSueldoEmpleado;
    @FXML
    private TextField tbNCuenta;
    @FXML
    private TextField tbLocalidad;
    @FXML
    private TextField tbColonia;
    @FXML
    private TextField tbNExterior;
    @FXML
    private TextField tbCalle;
    @FXML
    private TextField tbCodigoPostal;
    @FXML
    private TextField tbNInterior;
    @FXML
    private DatePicker tbFechaNacimiento;
    @FXML
    private DatePicker tbFechaContratacion;
    @FXML
    private ComboBox<String> cbPais;
    final ObservableList PaisOpcion = FXCollections.observableArrayList();
    @FXML
    private ComboBox<String> cbEstado;
    final ObservableList EstadoOpcion = FXCollections.observableArrayList();
    @FXML
    private ComboBox<String> cbCiudad;
    final ObservableList CiudadOpcion = FXCollections.observableArrayList();
    @FXML
    private ComboBox<String> cbGenero;
    final ObservableList GeneroOpcion = FXCollections.observableArrayList();
    @FXML
    private ComboBox<String> cbTipoUsuario;
    final ObservableList TipoUsuarioOpcion = FXCollections.observableArrayList();
    @FXML
    private ComboBox<String> cbMetodoPago;
    final ObservableList PagoOpcion = FXCollections.observableArrayList();
    @FXML
    private ComboBox<String> cbBanco;
    final ObservableList BancoOpcion = FXCollections.observableArrayList();
    @FXML
    private ComboBox<String> cbPeriodoPago;
    final ObservableList PeriodoPagoOpcion = FXCollections.observableArrayList();
    @FXML
    private ComboBox<String> cbContrato;
    final ObservableList ContratoOpcion = FXCollections.observableArrayList();
    
    @FXML
    private Button btnProduccionEmpleado;
    
    @FXML
    private TableView<Empleados> tableviewEmpleados;
    @FXML
    private TableColumn<Empleados, Integer> empIdUsuario;
    @FXML
    private TableColumn<Empleados, String> empNombre;
    @FXML
    private TableColumn<Empleados, Integer> empEdad;
    @FXML
    private TableColumn<Empleados, Integer> empSueldo;
    
    private final ObservableList<Empleados> dataList = FXCollections.observableArrayList();
    ObservableList<Empleados> listM;

    @FXML
    private TextField filtroIdUsuario;
    @FXML
    private TextField filtroNombreUsuario;
    @FXML
    private TextField filtroEdadUsuario;
    @FXML
    private TextField filtroSueldoUsuario;
    
    // Pantalla produccion
    @FXML
    private Pane pnHistorial;
    @FXML
    private Pane pnEditarProduccion;
    @FXML
    private Button btnVolverEditartProduccion;
    @FXML
    private Button btnGuardarEditartProduccion;
    @FXML
    private TextField tbMetrosEditar;
    @FXML
    private TextField tbCantidadProduccionEditar;
    @FXML
    private DatePicker tbFechaRegistroEditar;
    @FXML
    private ComboBox<String> cbMaterialEditar;
    final ObservableList EditarMaterialOpcion = FXCollections.observableArrayList();
    @FXML
    private ComboBox<String> cbCalibreEditar;
    final ObservableList EditarAlturaOpcion = FXCollections.observableArrayList();
    @FXML
    private ComboBox<String> cbAlturaEditar;
    final ObservableList EditarCalibreOpcion = FXCollections.observableArrayList();
    @FXML
    private ComboBox<String> cbRomboEditar;
    final ObservableList EditarRomboOpcion = FXCollections.observableArrayList();    
    
    @FXML
    private Button btnLimpiarProduccion;
    @FXML
    private Button btnNuevoProduccion;
    @FXML
    private Button btnBuscarEmpleado;
    @FXML
    private Button btnModificarProduccion;
    @FXML
    private Button btnImprimirProduccion;
    @FXML
    private Button btnEditarEmpleado2;
    @FXML
    private Button btnHistorial;
    @FXML
    private Button btnVolverHistorial;
    @FXML
    private Button btnVolverHistorial2;
    @FXML
    private TextField tbCodigoProduccion;
    @FXML
    private TextField tbMetros;
    @FXML
    private TextField tbCantidadProduccion;
    @FXML
    private DatePicker tbFechaRegistro;
    @FXML
    private ComboBox<String> cbMaterial;
    final ObservableList MaterialOpcion = FXCollections.observableArrayList();
    @FXML
    private ComboBox<String> cbAltura;
    final ObservableList AlturaOpcion = FXCollections.observableArrayList();
    @FXML
    private ComboBox<String> cbCalibre;
    final ObservableList CalibreOpcion = FXCollections.observableArrayList();
    @FXML
    private ComboBox<String> cbRombo;
    final ObservableList RomboOpcion = FXCollections.observableArrayList();
    @FXML
    private Label lbHCodigo2;
    @FXML
    private Label lbHNombre2;
    @FXML
    private Label lbHFecha2;
    @FXML
    private Label lbHDomicilio2;
    @FXML
    private ImageView imgPerfilProduccion;
    
    @FXML
    private TableView<ProduccionSemanal> tvSemanal;
    @FXML
    private TableColumn<ProduccionSemanal, String> tcCodigoS;
    @FXML
    private TableColumn<ProduccionSemanal, String> tcDiaS;
    @FXML
    private TableColumn<ProduccionSemanal, String> tcMaterialeS;
    @FXML
    private TableColumn<ProduccionSemanal, String> tcCalibreS;
    @FXML
    private TableColumn<ProduccionSemanal, String> tcAlturaS;
    @FXML
    private TableColumn<ProduccionSemanal, String> tcRomboS;
    @FXML
    private TableColumn<ProduccionSemanal, String> tcMetrosS;
    @FXML
    private TableColumn<ProduccionSemanal, String> tcCantidadS;
    ObservableList<ProduccionSemanal> listSemanal;
    
    // Pantalla Historial
    @FXML
    private TextField tbCodigoHistorial;
    @FXML
    private Button btnBuscarHistorial;
    @FXML
    private Button btnReporte;
    @FXML
    private Button btnEditarHistorial;
    @FXML
    private ImageView imgPerfilHistorial;
    @FXML
    private Label lbHCodigoHistorial;
    @FXML
    private Label lbHNombreHistorial;
    @FXML
    private Label lbHApellidoHistorial;
    @FXML
    private Label lbHDomicilioHistorial;
    
    @FXML
    private ComboBox<String> cbHistorialMes;
    final ObservableList HistorialOpcion = FXCollections.observableArrayList("ENERO","FEBRERO","MARZO","ABRIL","MAYO","JUNIO","JULIO","AGOSTO","SEPTIEMBRE","OCTUBRE","NOVIEMBRE","DICIEMBRE");
    
    @FXML
    private DatePicker tbFechaDe;
    @FXML
    private DatePicker tbFechaA;
    
    @FXML
    private TableView<Historial> tvHistorial;
    @FXML
    private TableColumn<Historial, String> tcCodigoHistorial;
    @FXML
    private TableColumn<Historial, String> tcDiaHistorial;
    @FXML
    private TableColumn<Historial, String> tcMaterialHistorial;
    @FXML
    private TableColumn<Historial, String> tcCalibreHistorial;
    @FXML
    private TableColumn<Historial, String> tcAlturaHistorial;
    @FXML
    private TableColumn<Historial, String> tcRomboHistorial;
    @FXML
    private TableColumn<Historial, String> tcMetrosHistorial;
    @FXML
    private TableColumn<Historial, String> tcCantidadHistorial;
    ObservableList<Historial> listHistorial;
    
    // Pantalla materiales
    @FXML
    private Pane pnEditarMaterial;
    @FXML
    private Pane pnEditarMaterial2;
    @FXML
    private TextField tbCodigoMaterialEditar;
    @FXML
    private TextField tbNombreMaterialEditar;
    
    @FXML
    private TextField tbCodigoMaterialEditar2;
    @FXML
    private TextField tbNombreMaterialEditar2;
    @FXML
    private TextField tbMedidaMaterialEditar;
    
    @FXML
    private Label lbMedidaMaterialEditar;
    
    @FXML
    private Button btnVolverEdiatMaterial;
    @FXML
    private Button btnGuardarEdiatMaterial;
    @FXML
    private Button btnGuardarEditarAltura;
    @FXML
    private Button btnGuardarEditarCalibre;
    @FXML
    private Button btnGuardarEditarRombos;
    @FXML
    private Button btnVolverEditartMaterial2;
    @FXML
    private Button btnGuardarEditarMaterial2;
    
    @FXML
    private TableView<Materiales> tvMateriales;
    @FXML
    private TableColumn<Materiales, Integer> tcCodigoMaterial;
    @FXML
    private TableColumn<Materiales, String> tcNombreMaterial;
    ObservableList<Materiales> listMaterial;
    @FXML
    private TextField tbCodigoMaterial;
    @FXML
    private TextField tbNombreMaterial;
    @FXML
    private Button btnGuardarMaterial;
    @FXML
    private Button btnEditarMaterial;
    @FXML
    private Button btnEliminarMaterial;
    @FXML
    private TableView<Alturas> tvAlturas;
    @FXML
    private TableColumn<Alturas, Integer> tcCodigoAltura;
    @FXML
    private TableColumn<Alturas, String> tcNombreAltura;
    @FXML
    private TableColumn<Alturas, String> tcAltura;
    ObservableList<Alturas> listAlturas;
    @FXML
    private TextField tbCodigoAltura;
    @FXML
    private TextField tbNombreAltura;
    @FXML
    private TextField tbAltura;
    @FXML
    private Button btnGuardarAltura;
    @FXML
    private Button btnEditarAltura;
    @FXML
    private Button btnEliminarAltura;
    
    @FXML
    private TableView<Calibres> tvCalibres;
    @FXML
    private TableColumn<Calibres, Integer> tcCodigoCalibre;
    @FXML
    private TableColumn<Calibres, String> tcNombreCalibre;
    @FXML
    private TableColumn<Calibres, String> tcCalibre;
    ObservableList<Calibres> listCalibre;
    @FXML
    private TextField tbCodigoCalibre;
    @FXML
    private TextField tbNombreCalibre;
    @FXML
    private TextField tbCalibre;
    @FXML
    private Button btnGuardarCalibre;
    @FXML
    private Button btnEditarCalibre;
    @FXML
    private Button btnEliminarCalibre;
    
    @FXML
    private TableView<Rombos> tvRombos;
    @FXML
    private TableColumn<Rombos, Integer> tcCodigoRombo;
    @FXML
    private TableColumn<Rombos, String> tcNombreRombo;
    @FXML
    private TableColumn<Rombos, String> tcRombo;
    ObservableList<Rombos> listRombo;
    @FXML
    private TextField tbCodigoRombo;
    @FXML
    private TextField tbNombreRombo;
    @FXML
    private TextField tbRombo;
    @FXML
    private Button btnGuardarRombo;
    @FXML
    private Button btnEditarRombo;
    @FXML
    private Button btnEliminarRombo;
    
    private String usuario = services.SessionManager.getInstance().getUserId();
    String tipo_empleado;
    
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
    
    public void DiasSemana(){
        FilteredList<ProduccionSemanal> filteredDataSemana = new FilteredList<>(
                listSemanal,
                ProduccionSemana -> listSemanal.indexOf(ProduccionSemana) < 7);
        SortedList<ProduccionSemanal> sortedDataSemana = new SortedList<>(filteredDataSemana);
        sortedDataSemana.comparatorProperty().bind(tvSemanal.comparatorProperty());
        tvSemanal.setItems(sortedDataSemana);
        tvSemanal.setFixedCellSize(48);
        tvSemanal.prefHeightProperty().bind(Bindings.size(tvSemanal.getItems()).multiply(tvSemanal.getFixedCellSize()).add(48));
    }

    /**
     * Initializes the controller class.
     */

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        
        cbPais.setValue("Mexico");
        cbPais.setItems(PaisOpcion);
        cbEstado.setValue("Sinaloa");
        cbEstado.setItems(EstadoOpcion);
        cbCiudad.setValue("Culiacan");
        cbCiudad.setItems(CiudadOpcion);
        cbGenero.setItems(GeneroOpcion);
        cbTipoUsuario.setValue("EMPLEADO GENERAL");
        cbTipoUsuario.setItems(TipoUsuarioOpcion);
        cbMetodoPago.setValue("EFECTIVO");
        cbMetodoPago.setItems(PagoOpcion);
        cbBanco.setItems(BancoOpcion);
        cbPeriodoPago.setValue("QUINCENAL");
        cbPeriodoPago.setItems(PeriodoPagoOpcion);
        cbContrato.setValue("INDEFINIDO");
        cbContrato.setItems(ContratoOpcion);
        
        if (file != null) {
            LOGGER.log(Level.FINE, "ESTE ES EL ARCHIVO {0}", file.getAbsolutePath());
            LOGGER.log(Level.FINE, "ESTE ES EL ARCHIVO {0}", file.getPath());
        } else {
            LOGGER.log(Level.FINE, "No profile file set (file==null)");
        }
        
        cbHistorialMes.setItems(HistorialOpcion);
        
        TableValueEmpleados();
        TableMateriales();
        TableAltura();
        TableCalibre();
        TableRombos();
        TableProduccionS();
        
        UpdateTable();
        fillComboBoxPais(); 
        fillComboBoxEstados();
        fillComboBoxCiudades();
        fillComboBoxGenero();
        fillComboBoxTipoUsuario();
        fillComboBoxPago();
        fillComboBoxBanco();
        fillComboBoxPeriodoPago();
        fillComboBoxContrato();
        
        cbMaterial.setItems(MaterialOpcion);
        cbAltura.setItems(AlturaOpcion);
        cbCalibre.setItems(CalibreOpcion);
        cbRombo.setItems(RomboOpcion);
        
        cbMaterialEditar.setItems(EditarMaterialOpcion);
        cbAlturaEditar.setItems(EditarAlturaOpcion);
        cbCalibreEditar.setItems(EditarCalibreOpcion);
        cbRomboEditar.setItems(EditarRomboOpcion);
        
        fillComboBoxMaterial();
        fillComboBoxAltura();
        fillComboBoxCalibre();
        fillComboBoxRombo();
        
        CodigoUsuario();
        CodigoMaterial();
        CodigoAltura();
        CodigoCalibres();
        CodigoRombos();
        
        UpdateFechaHistorial();
        FechaActualProduccion();
        UpdateMateriales();
        UpdateAlturas();
        UpdateCalibres();
        UpdateRombos();
        UpdateProduccionSemanal(); 
        VerificarTipoEmpleado();
        LOGGER.log(Level.INFO, "EL TIPO DE EMPLEADO ES: {0}", tipo_empleado);
        UpdateHistorial();
        }

    public void Filtro(){
        FilteredList<Empleados> filteredData = new FilteredList<>(dataList, b -> true);
        bindFilter(filteredData, filtroIdUsuario,    e -> e.getEmpIdUsuario());
        bindFilter(filteredData, filtroNombreUsuario, Empleados::getEmpNombre);
        bindFilter(filteredData, filtroEdadUsuario,   Empleados::getEmpEdad);
        bindFilter(filteredData, filtroSueldoUsuario, Empleados::getEmpSueldo);
        SortedList<Empleados> sortedData = new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(tableviewEmpleados.comparatorProperty());
        tableviewEmpleados.setItems(sortedData);
    }

    /**
     * Registers a mouse-click listener on a TableView that loads the selected
     * catalog item from the DB and passes it to a populate consumer.
     */
    private <T> void bindTableSelect(
            javafx.scene.control.TableView<T> tv,
            Function<T, Integer> getId,
            Function<String, T> findById,
            Consumer<T> populate) {
        tv.setOnMouseClicked(event -> {
            int sel = tv.getSelectionModel().getSelectedIndex();
            if (sel < 0 || sel >= tv.getItems().size()) return;
            T item = tv.getItems().get(sel);
            if (item == null) return;
            String in = Integer.toString(getId.apply(item));
            T found = findById.apply(in);
            LOGGER.log(Level.FINE, "Index seleccionado {0}", in);
            try {
                if (found != null) populate.accept(found);
                else LOGGER.log(Level.FINE, "No hay informacion del catalogo");
            } catch (Exception ex) {
                LOGGER.log(Level.SEVERE, "Error en handler de tabla", ex);
            }
        });
    }


    /** Binds a text field filter to a FilteredList predicate using a getter. */
    private <T> void bindFilter(FilteredList<T> list, javafx.scene.control.TextField tf,
                                Function<T, Object> getter) {
        tf.textProperty().addListener((obs, oldVal, newVal) ->
            list.setPredicate(item -> {
                if (newVal == null || newVal.isEmpty()) return true;
                return String.valueOf(getter.apply(item)).toLowerCase()
                             .contains(newVal.toLowerCase());
            })
        );
    }
    
    //PERFIL EMPLEADO
    
    public void VerificarTipoEmpleado(){
        try {
            String tipo = UsuariosService.getTipoEmpleado(usuario);
            if (tipo != null) {
                tipo_empleado = tipo;
                if (tipo_empleado.contains("SUPERVISOR")) {
                    DashboardSupervisor();
                } else if (tipo_empleado.contains("EMPLEADO GENERAL")) {
                    DashboardGeneral();
                }
            } else {
                LOGGER.log(Level.FINE, "Tipo de empleado no encontrado para usuario {0}", usuario);
            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error in VerificarTipoEmpleado", e);
        }
        
    }
    
    public void DashboardSupervisor(){
        
        btnMateriales.setVisible(false);
        btnMateriales.setDisable(true);
        
        btnEliminarEmpleado.setDisable(true);
        btnEliminarEmpleado.setVisible(false);
        
        btnNuevoEmpleado.setDisable(true);
        btnNuevoEmpleado.setVisible(false);
        
        btnModificarEmpleado.setDisable(true);
        btnModificarEmpleado.setVisible(false);
        
        btnGuardarEmpleado.setDisable(true);
        btnGuardarEmpleado.setVisible(false);
    }
    
    public void DashboardGeneral(){
        
        btnEmpleados.setVisible(false);
        btnEmpleados.setDisable(true);
        
        btnMateriales.setVisible(false);
        btnMateriales.setDisable(true);
        
        btnEliminarEmpleado.setVisible(false);
        btnEliminarEmpleado.setDisable(true);
        
        btnModificarProduccion.setVisible(false);
        btnModificarProduccion.setDisable(true);
        
        btnEditarEmpleado2.setVisible(false);
        btnEditarEmpleado2.setDisable(true);
        
        btnReporte.setVisible(false);
        btnReporte.setDisable(true);
        
        btnImprimirProduccion.setVisible(false);
        btnImprimirProduccion.setDisable(true);
        
        btnEditarHistorial.setVisible(false);
        btnEditarHistorial.setDisable(true);
        
        tbCodigoHistorial.setEditable(false);
        tbCodigoHistorial.setDisable(true);
        
        tbCodigoProduccion.setEditable(false);
        tbCodigoProduccion.setDisable(true);
        
        
        tbCodigoHistorial.setText(usuario);
        tbCodigoProduccion.setText(usuario);
        
        BuscarEmpleadoProduccion();
        UpdateProduccionSemanal();
        
        BuscarEmpleadoHistorial();
        UpdateHistorial();
        
    }
    
    public void Perfil(){
        UsuarioDetalle u = UsuariosDao.findById(usuario);
        try {
            ProfileBinder.bindProfile(u,
                    lbCodigoUsuario, lbNombreEmpleado, lbAPaternoEmpleado, lbAMaternoEmpleado,
                    lbCurp, lbRfc, lbNss, lbHFecha, lbFechaNacimiento, lbFechaContratacio,
                    lbEmailEmpleado, lbGenero, lbTipoUsuario, lbSueldoEmpleado, lbMetodoPago,
                    lbBanco, lbNCuenta, lbPeriodoPago, lbContrato, lbPais, lbEstado, lbLocalidad,
                    lbColonia, lbNExterior, lbCiudad, lbCalle, lbCodigoPostal, lbNInterior,
                    imgPerfil, backup, sinperfil);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error in Perfil", e);
        }
    }
    
    public void CambiarContraseña(){
        String contraseña = tbContraseñaActual.getText();
        LOGGER.log(Level.FINE, "ENTRE A CAMBIAR CONTRASEÑA");
        try {
            if (UsuariosService.verifyPassword(usuario, contraseña)) {
                UpdateContraseña();
            } else {
                Alert alert = new Alert(AlertType.ERROR);
                alert.setHeaderText("La contraseña actual no es correcta");
                alert.setContentText("Vuelva a intentarlo de nuevo");
                Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
                stage.getIcons().add(new Image("icons/IconBlanco.png"));
                alert.showAndWait();
            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error in CambiarContraseña", e);
        }
    
    }
    
    public void UpdateContraseña(){
        String contraseñanueva = tbContraseñaNueva.getText();
        LOGGER.log(Level.FINE, "CONTRASEÑA NUEVA {0}", contraseñanueva);
        try {
            boolean changed = UsuariosService.changePassword(usuario, contraseñanueva);
            if (changed) {
                LOGGER.log(Level.INFO, "SE CAMBIO CONTRASEÑA");
                Alert alert = new Alert(AlertType.INFORMATION);
                alert.setTitle("Operacion exitosa");
                alert.setHeaderText(null);
                alert.setContentText("Se cambio contraseña de manera exitosa");
                Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
                stage.getIcons().add(new Image("icons/IconBlanco.png"));
                alert.showAndWait();
                pnDashboard.setDisable(false);
                btnVolverContraseña.setDisable(false);
                btnVolverContraseña.setVisible(true);
                Perfil();
                pnBlanco.toFront();
                pnPerfil.toFront();
            } else {
                LOGGER.log(Level.WARNING, "No se pudo cambiar la contraseña para usuario {0}", usuario);
            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error in UpdateContraseña", e);
        }
        
    }
    
    public void ComprobarPrimerSesion(){
        try {
            if (UsuariosService.isFirstSession(usuario)) {
                CambiarContraseñaPrimera();
                LOGGER.log(Level.FINE, "USUARIO NO HA CAMBIO CONTRASEÑA");
            } else {
                LOGGER.log(Level.FINE, "USUARIO YA CAMBIO CONTRASEÑA");
            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error in ComprobarPrimerSesion", e);
        }
        
    }
    
    public void CambiarContraseñaPrimera(){
        lbTitulo.setText("CAMBIAR CONTRASEÑA");
        pnDashboard.setDisable(true);
        btnVolverContraseña.setDisable(true);
        btnVolverContraseña.setVisible(false);
        pnBlanco.toFront();
        pnCambiarContraseña.toFront();
    }
    
    //FIN PERFIL EMPLEADO

    //ELIMNAR EMPLEADO

    public void EliminarEmpleado(){
        String id = Integer.toString(indexEmpleado);
        String sql = "delete from usuarios where usuario_id = ?";
        try (Connection conn = ConnectionUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, id);
            ps.executeUpdate();
            UpdateTable();
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Error al eliminar empleado id=" + id, ex);
            showAlert(AlertType.ERROR, "Error al eliminar", null, "No se pudo eliminar el empleado: " + ex.getMessage());
        }
    }

    //ELIMINAR EMPLEADO
    
    //ACTUALIZAR TABLA EMPLEADOS

    public void UpdateTable(){
        empIdUsuario.setCellValueFactory(new PropertyValueFactory<>("EmpIdUsuario"));       
        empNombre.setCellValueFactory(new PropertyValueFactory<>("empNombre"));    
        empEdad.setCellValueFactory(new PropertyValueFactory<>("empEdad"));               
        empSueldo.setCellValueFactory(new PropertyValueFactory<>("empSueldo"));       
        
        listM = UsuariosDao.getAll();
        tableviewEmpleados.setItems(listM);        
    }
    //ACTUALIZAR TABLA EMPLEADOS
    public void CodigoUsuario(){
        dataList.setAll(UsuariosDao.getAll());
        if (!dataList.isEmpty()) {
            int nextId = dataList.stream()
                    .mapToInt(e -> e.getEmpIdUsuario())
                    .max().orElse(0) + 1;
            tbCodigoUsuarioAgregar.setText(String.valueOf(nextId));
        }
    }

    int indexEmpleado;
    public void TableValueEmpleados(){
      
        
        tableviewEmpleados.setOnMouseClicked(new EventHandler<MouseEvent>(){
            @Override
            public void handle(MouseEvent event) {
                Empleados index = tableviewEmpleados.getItems().get(tableviewEmpleados.getSelectionModel().getSelectedIndex());

                int selIdx = tableviewEmpleados.getSelectionModel().getSelectedIndex();
                if (selIdx >= 0) {
                    // Enable PERFIL button whenever a row is selected (viewing profile)
                    btnEditarEmpleado.setDisable(false);

                    // Only show/enable delete for users with GERENTE role
                    if (tipo_empleado != null && tipo_empleado.contains("GERENTE")) {
                        btnEliminarEmpleado.setVisible(true);
                        btnEliminarEmpleado.setDisable(false);
                    } else {
                        btnEliminarEmpleado.setVisible(false);
                        btnEliminarEmpleado.setDisable(true);
                    }
                } else {
                    btnEliminarEmpleado.setVisible(false);
                    btnEliminarEmpleado.setDisable(true);
                    btnEditarEmpleado.setDisable(true);
                }

                indexEmpleado = index.getEmpIdUsuario();     
                String in = Integer.toString(indexEmpleado);
                
                UsuarioDetalle u = UsuariosDao.findById(in);
                LOGGER.log(Level.FINE, "Index seleccionado {0}", in);
                try{
                    if(u != null){
                        lbHDomicilio.setText(u.getTipoEmpleado());
                        String timestamp = u.getCreateTime();
                        if (timestamp != null && timestamp.length() >= 10) {
                            lbHFecha.setText(DateUtils.formatLongDate(timestamp, true));
                        }
                        imgPerfil.setImage(ImageUtils.fromBytesOrDefault(u.getImagen(), backup));
                    }else{
                     imgPerfil.setImage(sinperfil);
                     LOGGER.log(Level.FINE, "No hay informacion de domicilio");
                }   
                }catch (Exception e){
                    LOGGER.log(Level.SEVERE, "Error in TableValueEmpleados handler", e);
                }
                
                lbHCodigo.setText(String.valueOf(indexEmpleado));
                lbHNombre.setText(index.getEmpNombre());
            }
    });
    }
    
    // BusquedaEmpleado moved to dao.UsuariosDao (use UsuariosDao.findById)
    
    // COMBO BOX FILLS
    public void fillComboBoxGenero(){
        GeneroOpcion.clear();
        GeneroOpcion.addAll(LookupService.getGeneros());
        cbGenero.setValue(cbGenero.getValue());
    }
    
    public void fillComboBoxTipoUsuario(){
        TipoUsuarioOpcion.clear();
        TipoUsuarioOpcion.addAll(LookupService.getTipoUsuario());
        cbTipoUsuario.setValue(cbTipoUsuario.getValue());
    }
    
    public void fillComboBoxPago(){
        PagoOpcion.clear();
        PagoOpcion.addAll(LookupService.getMetodosPago());
        cbMetodoPago.setValue(cbMetodoPago.getValue());
    }
    
    public void fillComboBoxBanco(){
        BancoOpcion.clear();
        BancoOpcion.addAll(LookupService.getBancos());
        cbBanco.setValue(cbBanco.getValue());
    }
    
    public void fillComboBoxPeriodoPago(){
        PeriodoPagoOpcion.clear();
        PeriodoPagoOpcion.addAll(LookupService.getPeriodosPago());
        cbPeriodoPago.setValue(cbPeriodoPago.getValue());
    }
    
    public void fillComboBoxContrato(){
        ContratoOpcion.clear();
        ContratoOpcion.addAll(LookupService.getContratos());
        cbContrato.setValue(cbContrato.getValue());
    }
    
    public void fillComboBoxPais(){
        PaisOpcion.clear();
        PaisOpcion.addAll(LookupService.getPaises());
        cbPais.setValue(cbPais.getValue());
    }
    
    public void fillComboBoxEstados(){
        String pais = cbPais.getValue();
        EstadoOpcion.clear();
        if (pais == null) return;
        EstadoOpcion.addAll(LookupService.getEstadosByCountryName(pais));
        cbEstado.setValue(cbEstado.getValue());
    }
    
    
    
        public void fillComboBoxCiudades(){
            String estado = cbEstado.getValue();
            CiudadOpcion.clear();
            if (estado == null) return;
            CiudadOpcion.addAll(LookupService.getCiudadesByStateName(estado));
            cbCiudad.setValue(cbCiudad.getValue());
        }
    
    
    // COMBO BOX FILLS
    
    // AGREGAR EMPLEADO
    public void AgregarEmpleado(){
        addEmpleadoInternal(false);
    }

    public void AgregarEmpleadoConImagen(){
        addEmpleadoInternal(true);
    }

    private void addEmpleadoInternal(boolean withImage){
        LOGGER.log(Level.FINE, "RECORD RUNNING!!!");
        try {
            UsuarioDetalle u = buildUsuarioDetalleFromForm();
            String password = tbCodigoUsuarioAgregar.getText();
            boolean ok = UsuariosService.saveUsuario(u, password, withImage, file);
            if (ok) {
                LOGGER.log(Level.INFO, "RECORD ADDED");
                LimpiarPerfil();
                UpdateTable();
                int id = Integer.parseInt(tbCodigoUsuarioAgregar.getText()) + 1;
                tbCodigoUsuarioAgregar.setText(String.valueOf(id));
            } else {
                LOGGER.log(Level.WARNING, "RECORD FAILED");
            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error in addEmpleadoInternal", e);
        }
    }
    // AGREGAR EMPLEADO
    
    // MODIFICAR EMPLEADO (wrappers)
    public void ModificarEmpleado(){
        modifyEmpleadoInternal(false);
    }

    public void ModificarEmpleadoConImagen(){
        modifyEmpleadoInternal(true);
    }

    private void modifyEmpleadoInternal(boolean withImage){
        Empleados selectedRow = null;
        try {
            selectedRow = tableviewEmpleados.getItems().get(tableviewEmpleados.getSelectionModel().getSelectedIndex());
        } catch (Exception e) {
            LOGGER.log(Level.WARNING, "No hay fila seleccionada para modificar", e);
            return;
        }
        if (selectedRow == null) return;
        indexEmpleado = selectedRow.getEmpIdUsuario();
        String in = Integer.toString(indexEmpleado);

        LOGGER.log(Level.FINE, "RECORD RUNNING (modify)!!!");
        try {
            UsuarioDetalle u = buildUsuarioDetalleFromForm();
            u.setUsuarioId(in);
            boolean ok = UsuariosService.saveUsuario(u, null, withImage, file);
            if (ok) {
                LOGGER.log(Level.INFO, "RECORD UPDATED");
                LimpiarPerfil();
                UpdateTable();
                int id = Integer.parseInt(tbCodigoUsuarioAgregar.getText()) + 1;
                tbCodigoUsuarioAgregar.setText(String.valueOf(id));
            } else {
                LOGGER.log(Level.WARNING, "RECORD FAILED");
            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error in modifyEmpleadoInternal", e);
        }
    }

    private String safeText(TextField tf, String defaultVal) {
        if (tf == null) return defaultVal;
        String t = tf.getText();
        if (t == null || t.isEmpty()) return defaultVal;
        return t;
    }

    private String safeCombo(ComboBox<String> cb, String defaultVal) {
        if (cb == null) return defaultVal;
        String v = cb.getValue();
        if (v == null || v.isEmpty()) return defaultVal;
        return v;
    }

    /**
     * Reads all employee form fields and builds a UsuarioDetalle (without userId).
     * Both add and modify paths use this to avoid duplication.
     */
    private UsuarioDetalle buildUsuarioDetalleFromForm() {
        LocalDate fecha    = tbFechaNacimiento.getValue()  != null ? tbFechaNacimiento.getValue()  : LocalDate.now();
        LocalDate fechaCon = tbFechaContratacion.getValue() != null ? tbFechaContratacion.getValue() : LocalDate.now();
        UsuarioDetalle u = new UsuarioDetalle();
        u.setNombre(safeText(tbNombreEmpleado, "NULL"));
        u.setApellidoPaterno(safeText(tbAPaternoEmpleado, "NULL"));
        u.setApellidoMaterno(safeText(tbAMaternoEmpleado, "NULL"));
        u.setCurp(safeText(tbCurp, "NULL"));
        u.setRfc(safeText(tbRfc, "NULL"));
        u.setNss(safeText(tbNss, "NULL"));
        u.setFechaNacimiento(fecha.toString());
        u.setFechaContratacion(fechaCon.toString());
        u.setEmail(safeText(tbEmailEmpleado, "NULL"));
        u.setGenero(safeCombo(cbGenero, "NULL"));
        u.setSueldo(safeText(tbSueldoEmpleado, "0"));
        u.setMetodoPago(safeCombo(cbMetodoPago, "NULL"));
        u.setBanco(safeCombo(cbBanco, "NULL"));
        u.setNumeroCuenta(safeText(tbNCuenta, "NULL"));
        u.setPeriodoPago(safeCombo(cbPeriodoPago, "NULL"));
        u.setTipoContrato(safeCombo(cbContrato, "NULL"));
        u.setPais(safeCombo(cbPais, "NULL"));
        u.setEstado(safeCombo(cbEstado, "NULL"));
        u.setLocalidad(safeText(tbLocalidad, "NULL"));
        u.setColonia(safeText(tbColonia, "NULL"));
        u.setNumeroExterior(safeText(tbNExterior, "NULL"));
        u.setCiudad(safeCombo(cbCiudad, "NULL"));
        u.setCalle(safeText(tbCalle, "NULL"));
        u.setCodigoPostal(safeText(tbCodigoPostal, "NULL"));
        u.setNumeroInterior(safeText(tbNInterior, "NULL"));
        u.setTipoEmpleado(safeCombo(cbTipoUsuario, "NULL"));
        return u;
    }

    /**
     * Populates all employee form fields from a UsuarioDetalle.
     * Used by PerfilEmpleado() and PerfilEmpleadoProduccion().
     */
    private void populateFormFromUsuario(UsuarioDetalle u) {
        try {
            if (u == null) {
                LOGGER.log(Level.FINE, "No hay informacion del empleado");
                return;
            }
            tbCodigoUsuarioAgregar.setText(u.getUsuarioId());
            tbNombreEmpleado.setText(u.getNombre());
            tbAPaternoEmpleado.setText(u.getApellidoPaterno());
            tbAMaternoEmpleado.setText(u.getApellidoMaterno());
            tbCurp.setText(u.getCurp());
            tbRfc.setText(u.getRfc());
            tbNss.setText(u.getNss());
            if (u.getFechaNacimiento() != null && !u.getFechaNacimiento().isEmpty()) {
                tbFechaNacimiento.setValue(LocalDate.parse(u.getFechaNacimiento()));
            }
            if (u.getFechaContratacion() != null && !u.getFechaContratacion().isEmpty()) {
                tbFechaContratacion.setValue(LocalDate.parse(u.getFechaContratacion()));
            }
            tbEmailEmpleado.setText(u.getEmail());
            cbGenero.setValue(u.getGenero());
            cbTipoUsuario.setValue(u.getTipoEmpleado());
            tbSueldoEmpleado.setText(u.getSueldo());
            cbMetodoPago.setValue(u.getMetodoPago());
            cbBanco.setValue(u.getBanco());
            tbNCuenta.setText(u.getNumeroCuenta());
            cbPeriodoPago.setValue(u.getPeriodoPago());
            cbContrato.setValue(u.getTipoContrato());
            cbPais.setValue(u.getPais());
            cbEstado.setValue(u.getEstado());
            tbLocalidad.setText(u.getLocalidad());
            tbColonia.setText(u.getColonia());
            tbNExterior.setText(u.getNumeroExterior());
            cbCiudad.setValue(u.getCiudad());
            tbCalle.setText(u.getCalle());
            tbCodigoPostal.setText(u.getCodigoPostal());
            tbNInterior.setText(u.getNumeroInterior());
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error in populateFormFromUsuario", e);
        }
    }

    // MODIFICAR EMPLEADO
    
    public void PerfilEmpleado(){
        btnModificarEmpleado.toFront();
        btnVolverEmpleados.toFront();
        Empleados index = tableviewEmpleados.getItems().get(tableviewEmpleados.getSelectionModel().getSelectedIndex());
        indexEmpleado = index.getEmpIdUsuario();
        String in = Integer.toString(indexEmpleado);
        UsuarioDetalle u = UsuariosDao.findById(in);
        LOGGER.log(Level.FINE, "Index seleccionado {0}", in);
        populateFormFromUsuario(u);
    }
    
    public void updateImagenPerfil(){
        LOGGER.log(Level.FINE, "CONSEGUI FUERA {0}", image);
        btnImagenPerfil.setImage(image);
        LOGGER.log(Level.FINE, "TERMINE FUERA {0}", btnImagenPerfil.getImage());
    }
    
    public void LimpiarPerfil(){
        tbNombreEmpleado.clear(); tbAPaternoEmpleado.clear(); tbAMaternoEmpleado.clear(); tbCurp.clear(); tbRfc.clear(); tbNss.clear(); tbFechaNacimiento.setValue(null);
        tbFechaContratacion.setValue(null); tbEmailEmpleado.clear(); tbSueldoEmpleado.clear(); cbGenero.setValue(null); cbTipoUsuario.setValue("EMPLEADO GENERAL"); 
        cbMetodoPago.setValue("EFECTIVO"); cbBanco.setValue(null); tbNCuenta.clear(); cbPeriodoPago.setValue("QUINCENAL"); cbContrato.setValue("INDEFINIDO"); 
        cbPais.setValue("Mexico"); cbEstado.setValue("Sinaloa"); tbLocalidad.clear(); tbColonia.clear(); tbNExterior.clear(); cbCiudad.setValue("Culiacan"); 
        tbCalle.clear(); tbCodigoPostal.clear(); tbNInterior.clear(); btnImagenPerfil.setImage(backup); btnSubirImagen.setText("SUBIR IMAGEN"); 
        lbPath.setText(null); file=null; tbCodigoPostal.clear(); tbNInterior.clear();
    }
    
    @FXML
    void obtenerEdad(ActionEvent event) {
        LocalDate edad = tbFechaNacimiento.getValue();
    }
    
    @FXML
    void updateComboBox(ActionEvent event) {
        cbPais.setValue(cbPais.getValue());
        cbEstado.setValue(cbEstado.getValue());
        fillComboBoxEstados();
        LOGGER.log(Level.FINE, "PAIS SELECCIONADO {0}", cbPais.getValue());
        LOGGER.log(Level.FINE, "PAIS SELECCIONADO {0}", cbEstado.getValue());
        
    }   
    @FXML
    void updateComboBoxEstado(ActionEvent event) {
        cbPais.setValue(cbPais.getValue());
        cbEstado.setValue(cbEstado.getValue());
        fillComboBoxCiudades();
        LOGGER.log(Level.FINE, "PAIS SELECCIONADO {0}", cbPais.getValue());
        LOGGER.log(Level.FINE, "PAIS SELECCIONADO {0}", cbEstado.getValue());
        
    }

    @FXML
    void updateMetodo(ActionEvent event) {
        
        if (cbMetodoPago.getSelectionModel().isSelected(1)){  
            cbBanco.setDisable(false);
            tbNCuenta.setDisable(false);
            LOGGER.log(Level.FINE, "FALSE!!! {0}", cbMetodoPago.getValue());
        }else{
            cbBanco.setDisable(true);
            tbNCuenta.setDisable(true);
            cbBanco.setValue(null);
            tbNCuenta.clear();
            LOGGER.log(Level.FINE, "TRUE!!! {0}", cbMetodoPago.getValue());
        }
        LOGGER.log(Level.FINE, "FUERAAA {0}", cbMetodoPago.getValue());
    }
    // FIN PANTALLA EMPLEADOS
    
    // PANTALLA PRODUCCION
    
    
    
    String indexProduccionS;
    public void TableProduccionS(){
        tvHistorial.setOnMouseClicked(new EventHandler<MouseEvent>(){
            public void handle(MouseEvent event) {
                Historial index = tvHistorial.getSelectionModel().getSelectedItem();
                if (index == null) return;
                indexProduccionS = index.getTcCodigoHistorial();
                String id = indexProduccionS;
                LOGGER.log(Level.FINE, "Historial fila seleccionada id={0}", id);
                try {
                    // Rellenar los campos de edición con los valores de la fila seleccionada
                    cbMaterialEditar.setValue(index.getTcMaterialHistorial());
                    cbCalibreEditar.setValue(index.getTcCalibreHistorial());
                    cbAlturaEditar.setValue(index.getTcAlturaHistorial());
                    cbRomboEditar.setValue(index.getTcRomboHistorial());
                    tbMetrosEditar.setText(index.getTcMetrosHistorial());
                    tbCantidadProduccionEditar.setText(index.getTcCantidadHistorial());

                    // Intentar parsear la fecha (se espera ISO yyyy-MM-dd)
                    String fechaStr = index.getTcDiaHistorial();
                    if (fechaStr != null && !fechaStr.trim().isEmpty()) {
                        try {
                            LocalDate fecha = LocalDate.parse(fechaStr);
                            tbFechaRegistroEditar.setValue(fecha);
                        } catch (Exception pe) {
                            LOGGER.log(Level.FINE, "No se pudo parsear fecha para edición: {0}", fechaStr);
                            tbFechaRegistroEditar.setValue(null);
                        }
                    } else {
                        tbFechaRegistroEditar.setValue(null);
                    }

                    // Actualizar perfil mostrado en la sección Producción/Historial usando el id de empleado actual
                    String usuarioId = tbCodigoHistorial.getText();
                    if (usuarioId != null && !usuarioId.trim().isEmpty()) {
                        try {
                            UsuarioDetalle u = UsuariosDao.findById(usuarioId);
                            if (u != null) {
                                lbHDomicilio.setText(u.getTipoEmpleado());
                                String timestamp = u.getCreateTime();
                                if (timestamp != null && timestamp.length() >= 10) {
                                    lbHFecha.setText(DateUtils.formatLongDate(timestamp, true));
                                }
                                imgPerfil.setImage(ImageUtils.fromBytesOrDefault(u.getImagen(), sinperfil));
                            } else {
                                imgPerfil.setImage(sinperfil);
                            }
                        } catch (Exception e) {
                            LOGGER.log(Level.SEVERE, "Error al cargar perfil en selección de Historial", e);
                        }
                    }
                } catch (Exception e) {
                    LOGGER.log(Level.SEVERE, "Error in Historial table click handler", e);
                }

            }
        });

    }
    
    public void ModificarProduccion(){
        LocalDate fecha = tbFechaRegistroEditar.getValue() != null ? tbFechaRegistroEditar.getValue() : LocalDate.now();
        String id = indexProduccionS;
        String material = cbMaterialEditar.getValue();
        String calibre = cbCalibreEditar.getValue();
        String altura = cbAlturaEditar.getValue();
        String rombo = cbRomboEditar.getValue();
        String metros = tbMetrosEditar.getText().isEmpty() ? "NULL" : tbMetrosEditar.getText();
        String cantidad = tbCantidadProduccionEditar.getText().isEmpty() ? "NULL" : tbCantidadProduccionEditar.getText();

        if (id == null || id.trim().isEmpty()) {
            LOGGER.log(Level.WARNING, "No hay registro seleccionado para modificar produccion (id nulo)");
            return;
        }

        boolean ok = ProduccionService.updateProduccion(id, material, calibre, altura, rombo, metros, cantidad, fecha);
        if (ok) {
            LOGGER.log(Level.INFO, "Produccion modificada correctamente id={0}", id);
            UpdateProduccionSemanal();
            UpdateHistorial();
        } else {
            LOGGER.log(Level.WARNING, "No se pudo modificar la produccion id={0}", id);
        }
    }
    
    public void FechaActualProduccion(){
        tbFechaRegistro.setValue(LocalDate.now());
        tbFechaRegistroEditar.setValue(LocalDate.now());
    }
    
    public void fillComboBoxMaterial(){
        refreshComboBoxPair(MaterialOpcion, EditarMaterialOpcion,
                cbMaterial, cbMaterialEditar, LookupService::getMateriales);
    }

    public void fillComboBoxAltura(){
        refreshComboBoxPair(AlturaOpcion, EditarAlturaOpcion,
                cbAltura, cbAlturaEditar, LookupService::getAlturas);
    }

    public void fillComboBoxCalibre(){
        refreshComboBoxPair(CalibreOpcion, EditarCalibreOpcion,
                cbCalibre, cbCalibreEditar, LookupService::getCalibres);
    }

    public void fillComboBoxRombo(){
        refreshComboBoxPair(RomboOpcion, EditarRomboOpcion,
                cbRombo, cbRomboEditar, LookupService::getRombos);
    }

    /** Repopulates a pair of ObservableLists (and preserves current combo values). */
    private void refreshComboBoxPair(
            ObservableList<String> a, ObservableList<String> b,
            javafx.scene.control.ComboBox<String> cbA,
            javafx.scene.control.ComboBox<String> cbB,
            Supplier<ObservableList<String>> supplier) {
        ObservableList<String> list = supplier.get();
        a.clear(); b.clear();
        a.addAll(list); b.addAll(list);
        cbA.setValue(cbA.getValue());
        cbB.setValue(cbB.getValue());
    }
    public void AgregarProduccion(){
        LOGGER.log(Level.FINE, "AgregarProduccion button pressed");
        LocalDate fecha = tbFechaRegistro.getValue() != null ? tbFechaRegistro.getValue() : LocalDate.now();
        String material = cbMaterial.getValue();
        String calibre = cbCalibre.getValue();
        String altura = cbAltura.getValue();
        String rombos = cbRombo.getValue();
        String metros = (tbMetros.getText() == null || tbMetros.getText().isEmpty()) ? "NULL" : tbMetros.getText();
        String cantidad = (tbCantidadProduccion.getText() == null || tbCantidadProduccion.getText().isEmpty()) ? "NULL" : tbCantidadProduccion.getText();
        String autorid = lbHCodigo2.getText();

        boolean ok = ProduccionService.insertProduccion(material, calibre, altura, rombos, metros, cantidad, autorid, fecha);
        if (ok) {
            LOGGER.log(Level.INFO, "Produccion agregada correctamente");
            cleanProduccion();
        } else {
            LOGGER.log(Level.WARNING, "No se pudo agregar la produccion");
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Error al guardar");
            alert.setHeaderText("No se pudo guardar la producción");
            alert.setContentText("Verifique los datos e intente de nuevo.");
            alert.showAndWait();
        }
    }
    
    public void cleanProduccion(){
        tbFechaRegistro.setValue(LocalDate.now());
        cbMaterial.setValue(null);
        cbCalibre.setValue(null);
        cbAltura.setValue(null);
        cbRombo.setValue(null);
        tbMetros.clear();
        tbCantidadProduccion.clear();
    }
    
    public void BuscarEmpleadoProduccion(){
        buscarYMostrarEmpleadoProduccion(tbCodigoProduccion.getText());
    }
    
    public void BuscarEmpleadoConBotonProduccion(){
        String id = lbHCodigo.getText();
        tbCodigoProduccion.setText(id);
        buscarYMostrarEmpleadoProduccion(id);
    }
    
    /**
     * Looks up an employee by id and populates the production screen's profile labels.
     * Shows an error alert if not found.
     */
    private void buscarYMostrarEmpleadoProduccion(String in) {
        UsuarioDetalle u = UsuariosDao.findById(in);
        LOGGER.log(Level.FINE, "Index seleccionado {0}", in);
        try {
            if (u != null) {
                btnEditarEmpleado2.setDisable(false);
                lbHCodigo2.setText(u.getUsuarioId());
                lbHNombre2.setText(u.getNombre() + " " + u.getApellidoPaterno() + " " + u.getApellidoMaterno());
                lbHDomicilio2.setText(u.getTipoEmpleado());
                String timestamp = u.getCreateTime();
                if (timestamp != null && timestamp.length() >= 10) {
                    lbHFecha2.setText(DateUtils.formatLongDate(timestamp, true));
                }
                imgPerfilProduccion.setImage(ImageUtils.fromBytesOrDefault(u.getImagen(), backup));
            } else {
                showAlert(AlertType.ERROR, "No hay coincidencias", "No se encontro empleado", null);
                imgPerfil.setImage(sinperfil);
                LOGGER.log(Level.FINE, "No hay informacion de domicilio");
            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error en buscarYMostrarEmpleadoProduccion", e);
        }
    }

    public void PerfilEmpleadoProduccion(){
        btnModificarEmpleado.toFront();
        btnVolverHistorial.toFront();
        String in = lbHCodigo2.getText();
        UsuarioDetalle u = UsuariosDao.findById(in);
        LOGGER.log(Level.FINE, "Index seleccionado {0}", in);
        populateFormFromUsuario(u);
    }
    
    public void UpdateProduccionSemanal(){
        String autor = tbCodigoProduccion.getText();
        tcCodigoS.setCellValueFactory(new PropertyValueFactory<>("tcCodigoS"));
        tcDiaS.setCellValueFactory(new PropertyValueFactory<>("tcDiaS"));       
        tcMaterialeS.setCellValueFactory(new PropertyValueFactory<>("tcMaterialeS"));
        tcCalibreS.setCellValueFactory(new PropertyValueFactory<>("tcCalibreS")); 
        tcAlturaS.setCellValueFactory(new PropertyValueFactory<>("tcAlturaS")); 
        tcRomboS.setCellValueFactory(new PropertyValueFactory<>("tcRomboS")); 
        tcMetrosS.setCellValueFactory(new PropertyValueFactory<>("tcMetrosS")); 
        tcCantidadS.setCellValueFactory(new PropertyValueFactory<>("tcCantidadS"));
        
        listSemanal = ProduccionService.getProduccionSemana(autor);
        tvSemanal.setItems(listSemanal);        
    }
    
    // FIN PANTALLA PRODUCCION
    // PANTALLA HISTORIAL
    
    public void ImprimirReporte(){
        String autor = tbCodigoHistorial.getText();
        LocalDate fechaDe = tbFechaDe.getValue();
        LocalDate fechaA  = tbFechaA.getValue();
        String de = fechaDe != null ? fechaDe.toString() : "";
        String a  = fechaA  != null ? fechaA.toString()  : "";
        try {
            ReporteService.imprimirHistorial(autor, de, a);
        } catch (IllegalStateException ex) {
            LOGGER.log(Level.SEVERE, "Plantilla de reporte no encontrada", ex);
            showAlert(AlertType.ERROR, "Plantilla no encontrada", "Falta report.jrxml", ex.getMessage());
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, "Error al generar reporte", ex);
            showAlert(AlertType.ERROR, "Error al generar reporte", "Error al compilar/llenar el reporte", ex.getMessage());
        }
    }
    
    public void UpdateFechaHistorial(){        
        tbFechaDe.setValue(LocalDate.now().minusMonths(1));
        tbFechaA.setValue(LocalDate.now());
        
        tbFechaA.valueProperty().addListener((newValue) -> {
            UpdateHistorial();
        });
        tbFechaDe.valueProperty().addListener((newValue) -> {
            UpdateHistorial();
        });
    }
    
    public void UpdateMesHistorial(){
        // Map de nombre de mes (en español) → Month del API de Java
        final java.util.Map<String, Month> MESES = new java.util.LinkedHashMap<>();
        MESES.put("ENERO",      Month.JANUARY);
        MESES.put("FEBRERO",    Month.FEBRUARY);
        MESES.put("MARZO",      Month.MARCH);
        MESES.put("ABRIL",      Month.APRIL);
        MESES.put("MAYO",       Month.MAY);
        MESES.put("JUNIO",      Month.JUNE);
        MESES.put("JULIO",      Month.JULY);
        MESES.put("AGOSTO",     Month.AUGUST);
        MESES.put("SEPTIEMBRE", Month.SEPTEMBER);
        MESES.put("OCTUBRE",    Month.OCTOBER);
        MESES.put("NOVIEMBRE",  Month.NOVEMBER);
        MESES.put("DICIEMBRE",  Month.DECEMBER);

        cbHistorialMes.valueProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal == null) return;
            Month month = MESES.get(newVal);  // .equals() implícito — corrige bug de ==
            if (month == null) return;
            int anio = tbFechaDe.getValue() != null
                    ? tbFechaDe.getValue().getYear()
                    : LocalDate.now().getYear();
            LocalDate primero = LocalDate.of(anio, month, 1);
            LocalDate ultimo  = primero.withDayOfMonth(month.length(primero.isLeapYear()));
            tbFechaDe.setValue(primero);
            tbFechaA.setValue(ultimo);
            UpdateHistorial();
        });
    }
    
    public void UpdateHistorial(){
        
        LocalDate fechaDe, fechaA;
        fechaDe = tbFechaDe.getValue();        
        fechaA = tbFechaA.getValue();

        String autor = tbCodigoHistorial.getText();
        String de = fechaDe.toString();
        String a = fechaA.toString();
        
        tcCodigoHistorial.setCellValueFactory(new PropertyValueFactory<>("tcCodigoHistorial"));
        tcDiaHistorial.setCellValueFactory(new PropertyValueFactory<>("tcDiaHistorial"));       
        tcMaterialHistorial.setCellValueFactory(new PropertyValueFactory<>("tcMaterialHistorial"));
        tcCalibreHistorial.setCellValueFactory(new PropertyValueFactory<>("tcCalibreHistorial")); 
        tcAlturaHistorial.setCellValueFactory(new PropertyValueFactory<>("tcAlturaHistorial")); 
        tcRomboHistorial.setCellValueFactory(new PropertyValueFactory<>("tcRomboHistorial")); 
        tcMetrosHistorial.setCellValueFactory(new PropertyValueFactory<>("tcMetrosHistorial")); 
        tcCantidadHistorial.setCellValueFactory(new PropertyValueFactory<>("tcCantidadHistorial"));
        
        listHistorial = HistorialService.getHistorial(autor, de, a);
        tvHistorial.setItems(listHistorial);      
    }
    
    public void BuscarEmpleadoHistorial(){
                
                String in = tbCodigoHistorial.getText();
                
                UsuarioDetalle u = UsuariosDao.findById(in);
                LOGGER.log(Level.FINE, "Index seleccionado {0}", in);
                try{
                    if(u != null){
                        lbHCodigoHistorial.setText(u.getUsuarioId());
                        lbHNombreHistorial.setText(u.getNombre());
                        lbHApellidoHistorial.setText(u.getApellidoPaterno()+" "+u.getApellidoMaterno());
                        lbHDomicilioHistorial.setText(u.getTipoEmpleado());
                        imgPerfilHistorial.setImage(ImageUtils.fromBytesOrDefault(u.getImagen(), backup));
                    } else {
                        showAlert(AlertType.ERROR, "No hay coincidencias", "No se encontro empleado", null);
                        imgPerfilHistorial.setImage(sinperfil);
                        LOGGER.log(Level.FINE, "No hay informacion de domicilio");
                    }
                } catch (Exception e) {
                    LOGGER.log(Level.SEVERE, "Error in BuscarEmpleadoHistorial", e);
                }

    }
        
    // FIN PANTALLA HISTORIAL
    // PANTALLA MATERIALES
       
    
    
    int indexMaterial;
    public void TableMateriales(){
        bindTableSelect(tvMateriales, Materiales::getTcCodigoMaterial, MaterialesService::findById,
            mat -> {
                indexMaterial = mat.getTcCodigoMaterial();
                tbCodigoMaterialEditar.setText(String.valueOf(mat.getTcCodigoMaterial()));
                tbNombreMaterialEditar.setText(mat.getTcNombreMaterial());
            });
    }
    
    public void EliminarMaterial(){
        String id = Integer.toString(indexMaterial);
        boolean ok = MaterialesService.delete(id);
        if (ok) { UpdateMateriales(); CodigoMaterial(); cbMaterial.getItems().clear(); fillComboBoxMaterial(); }
        else LOGGER.log(Level.WARNING, "No se pudo eliminar material id={0}", id);
    }
    
    public void CodigoMaterial() { CatalogoUtils.cargarSiguienteId("materiales", tbCodigoMaterial); }
    
    public void UpdateMateriales(){
        tcCodigoMaterial.setCellValueFactory(new PropertyValueFactory<>("tcCodigoMaterial"));       
        tcNombreMaterial.setCellValueFactory(new PropertyValueFactory<>("tcNombreMaterial"));       
        
        listMaterial = MaterialesService.getAll();
        tvMateriales.setItems(listMaterial);        
    }
    
    public void AgregarMaterial(){
        CatalogoUtils.agregarCatalogo(MaterialesService::insert, tbNombreMaterial,
                () -> { UpdateMateriales(); CodigoMaterial(); cbMaterial.getItems().clear(); fillComboBoxMaterial(); });
    }
    
    public void ModificarMaterial(){
        CatalogoUtils.modificarCatalogo(MaterialesService::update, tbCodigoMaterialEditar, tbNombreMaterialEditar,
                () -> { UpdateMateriales(); CodigoMaterial(); cbMaterial.getItems().clear(); fillComboBoxMaterial(); });
    }
    
    
    
    
    int indexAltura;
    public void TableAltura(){
        bindTableSelect(tvAlturas, Alturas::getTcCodigoAltura, AlturasService::findById,
            a -> {
                indexAltura = a.getTcCodigoAltura();
                tbCodigoMaterialEditar2.setText(String.valueOf(a.getTcCodigoAltura()));
                tbNombreMaterialEditar2.setText(a.getTcNombreAltura());
                tbMedidaMaterialEditar.setText(a.getTcAltura());
            });
    }
    
    public void EliminarAltura(){
        String in = Integer.toString(indexAltura);
        try{
            boolean ok = AlturasService.delete(in);
            if (ok) {
                UpdateAlturas();
                CodigoAltura();
                cbAltura.getItems().clear();
                fillComboBoxAltura();
            } else {
                LOGGER.log(Level.WARNING, "DELETE FAILED for altura id={0}", in);
            }
        } catch (Exception e){
            LOGGER.log(Level.SEVERE, "Error deleting altura", e);
        }
        
    }
    
    public void CodigoAltura() { CatalogoUtils.cargarSiguienteId("alturas", tbCodigoAltura); }
    
    public void UpdateAlturas(){
        tcCodigoAltura.setCellValueFactory(new PropertyValueFactory<>("tcCodigoAltura"));       
        tcNombreAltura.setCellValueFactory(new PropertyValueFactory<>("tcNombreAltura"));
        tcAltura.setCellValueFactory(new PropertyValueFactory<>("tcAltura"));   
        
        listAlturas = AlturasService.getAll();
        tvAlturas.setItems(listAlturas);        
    }
    
    public void AgregarAltura(){
        CatalogoUtils.agregarCatalogo(AlturasService::insert, tbNombreAltura, tbAltura,
                () -> { UpdateAlturas(); CodigoAltura(); cbAltura.getItems().clear(); fillComboBoxAltura(); });
    }
    
    public void ModificarAltura(){
        CatalogoUtils.modificarCatalogo(AlturasService::update,
                tbCodigoMaterialEditar2, tbNombreMaterialEditar2, tbMedidaMaterialEditar,
                () -> { UpdateAlturas(); CodigoAltura(); cbAltura.getItems().clear(); fillComboBoxAltura(); });
    }
    
    
    
    int indexCalibre;
    public void TableCalibre(){
        bindTableSelect(tvCalibres, Calibres::getTcCodigoCalibre, CalibresService::findById,
            c -> {
                indexCalibre = c.getTcCodigoCalibre();
                tbCodigoMaterialEditar2.setText(String.valueOf(c.getTcCodigoCalibre()));
                tbNombreMaterialEditar2.setText(c.getTcNombreCalibre());
                tbMedidaMaterialEditar.setText(c.getTcCalibre());
            });
    }
    
    public void EliminarCalibre(){
        String id = Integer.toString(indexCalibre);
        boolean ok = CalibresService.delete(id);
        if (ok) { UpdateCalibres(); CodigoCalibres(); cbCalibre.getItems().clear(); fillComboBoxCalibre(); }
        else LOGGER.log(Level.WARNING, "No se pudo eliminar calibre id={0}", id);
    }
    
    public void CodigoCalibres() { CatalogoUtils.cargarSiguienteId("calibres", tbCodigoCalibre); }
    
    public void UpdateCalibres(){
        tcCodigoCalibre.setCellValueFactory(new PropertyValueFactory<>("tcCodigoCalibre"));       
        tcNombreCalibre.setCellValueFactory(new PropertyValueFactory<>("tcNombreCalibre"));
        tcCalibre.setCellValueFactory(new PropertyValueFactory<>("tcCalibre"));   
        
        listCalibre = CalibresService.getAll();
        tvCalibres.setItems(listCalibre);        
    }
    
    public void AgregarCalibre(){
        CatalogoUtils.agregarCatalogo(CalibresService::insert, tbNombreCalibre, tbCalibre,
                () -> { UpdateCalibres(); CodigoCalibres(); cbCalibre.getItems().clear(); fillComboBoxCalibre(); });
    }
    
    public void ModificarCalibre(){
        CatalogoUtils.modificarCatalogo(CalibresService::update,
                tbCodigoMaterialEditar2, tbNombreMaterialEditar2, tbMedidaMaterialEditar,
                () -> { UpdateCalibres(); CodigoCalibres(); cbCalibre.getItems().clear(); fillComboBoxCalibre(); });
    }
    
    
    
    int indexRombo;
    public void TableRombos(){
        bindTableSelect(tvRombos, Rombos::getTcCodigoRombo, RombosDao::findById,
            r -> {
                indexRombo = r.getTcCodigoRombo();
                tbCodigoMaterialEditar2.setText(String.valueOf(r.getTcCodigoRombo()));
                tbNombreMaterialEditar2.setText(r.getTcNombreRombo());
                tbMedidaMaterialEditar.setText(r.getTcRombo());
            });
    }

    public void EliminarRombo(){
        String in = Integer.toString(indexRombo);
        try{
            boolean ok = RombosService.delete(in);
            if (ok) {
                UpdateRombos();
                CodigoRombos();
                cbRombo.getItems().clear();
                fillComboBoxRombo();
                cbRombo.setItems(RomboOpcion);
            } else {
                LOGGER.log(Level.WARNING, "DELETE FAILED for rombo id={0}", in);
            }
        } catch (Exception e){
            LOGGER.log(Level.SEVERE, "Error deleting rombo", e);
        }
        
    }
    
    public void CodigoRombos() { CatalogoUtils.cargarSiguienteId("rombos", tbCodigoRombo); }   
    
    public void UpdateRombos(){
        tcCodigoRombo.setCellValueFactory(new PropertyValueFactory<>("tcCodigoRombo"));       
        tcNombreRombo.setCellValueFactory(new PropertyValueFactory<>("tcNombreRombo"));
        tcRombo.setCellValueFactory(new PropertyValueFactory<>("tcRombo"));   
        
        listRombo = RombosService.getAll();
        tvRombos.setItems(listRombo);        
    }
    
    public void AgregarRombos(){
        CatalogoUtils.agregarCatalogo(RombosService::insert, tbNombreRombo, tbRombo,
                () -> { UpdateRombos(); CodigoRombos(); cbRombo.getItems().clear(); fillComboBoxRombo(); });
    }
    
    public void ModificarRombos(){
        CatalogoUtils.modificarCatalogo(RombosService::update,
                tbCodigoMaterialEditar2, tbNombreMaterialEditar2, tbMedidaMaterialEditar,
                () -> { UpdateRombos(); CodigoRombos(); cbRombo.getItems().clear(); fillComboBoxRombo(); });
    }
    
    public void clearCambiarMaterial() {
        tbCodigoMaterialEditar2.clear();
        tbNombreMaterialEditar2.clear();
        tbMedidaMaterialEditar.clear();
    }

    // ── UI HELPERS ────────────────────────────────────────────────────────────

    /** Animates title label, then brings pnBlanco and target pane to front. */
    private void navigate(String title, Pane target) {
        new animatefx.animation.BounceIn(lbTitulo).play();
        lbTitulo.setText(title);
        pnBlanco.toFront();
        target.toFront();
    }

    private void showAlert(AlertType type, String title, String header, String content) {
        Alert alert = new Alert(type);
        if (title   != null) alert.setTitle(title);
        if (header  != null) alert.setHeaderText(header);
        if (content != null) alert.setContentText(content);
        Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
        stage.getIcons().add(new Image("icons/IconBlanco.png"));
        alert.showAndWait();
    }

    /** Shows YES/CANCEL confirmation (CANCEL is default button) and runs accion on YES. */
    private void confirmarYEjecutar(String mensaje, Runnable accion) {
        Alert alert = new Alert(AlertType.CONFIRMATION, mensaje, ButtonType.YES, ButtonType.CANCEL);
        ((Button) alert.getDialogPane().lookupButton(ButtonType.YES)).setDefaultButton(false);
        ((Button) alert.getDialogPane().lookupButton(ButtonType.CANCEL)).setDefaultButton(true);
        Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
        stage.getIcons().add(new Image("icons/IconBlanco.png"));
        alert.showAndWait();
        if (alert.getResult() == ButtonType.YES) accion.run();
    }

    // ── NAVIGATION ────────────────────────────────────────────────────────────

    private void onNavPerfil()     { navigate("PERFIL",     pnPerfil);     Perfil(); }
    private void onNavInicio()     { navigate("INICIO",     pnInicio); }
    private void onNavEmpleados()  { navigate("EMPLEADOS",  pnEmpleados); }
    private void onNavProduccion() { navigate("PRODUCCION", pnProduccion); }
    private void onNavMateriales() { navigate("MATERIALES", pnMateriales); }

    private void onNavExit(ActionEvent actionEvent) throws IOException {
        Alert alert = new Alert(AlertType.CONFIRMATION, "Desea cerrar sesion?", ButtonType.YES, ButtonType.CANCEL);
        ((Button) alert.getDialogPane().lookupButton(ButtonType.YES)).setDefaultButton(false);
        ((Button) alert.getDialogPane().lookupButton(ButtonType.CANCEL)).setDefaultButton(true);
        alert.showAndWait();
        if (alert.getResult() == ButtonType.YES) {
            LOGGER.log(Level.INFO, "Cerro sesion");
            Node node = (Node) actionEvent.getSource();
            Stage stage = (Stage) node.getScene().getWindow();
            Parent root = FXMLLoader.load(getClass().getResource("/aceros/Login.fxml"));
            Scene scene = new Scene(root);
            scene.setFill(Color.TRANSPARENT);
            stage.setScene(scene);
            new animatefx.animation.ZoomIn(root).play();
            stage.show();
        }
    }

    // ── PERFIL ────────────────────────────────────────────────────────────────

    private void onBtnCambiarPassword() {
        pnDashboard.setDisable(true);
        navigate("CAMBIAR CONTRASENA", pnCambiarContraseña);
    }

    private void onBtnVolverPassword() {
        pnDashboard.setDisable(false);
        Perfil();
        navigate("PERFIL", pnPerfil);
    }

    private void onBtnGuardarPassword() {
        if (tbContraseñaActual.getText().isEmpty()) {
            showAlert(AlertType.ERROR, null, "No se ingreso contrasena actual", "La contrasena actual debe ser ingresada");
        } else if (tbContraseñaNueva.getText().isEmpty() || tbContraseñaRepetir.getText().isEmpty()) {
            tbContraseñaActual.clear(); tbContraseñaNueva.clear(); tbContraseñaRepetir.clear();
            showAlert(AlertType.ERROR, null, "Las contrasenas nuevas no coinciden", "Vuelva a intentarlo de nuevo");
        } else if (tbContraseñaNueva.getText().equals(tbContraseñaRepetir.getText())) {
            CambiarContraseña();
        }
    }

    // ── EMPLEADOS ─────────────────────────────────────────────────────────────

    private void onBtnNuevoEmpleado() {
        confirmarYEjecutar("Desea agregar nuevo empleado?", () -> {
            LimpiarPerfil();
            CodigoUsuario();
            if (tableviewEmpleados != null) tableviewEmpleados.getSelectionModel().clearSelection();
            btnGuardarEmpleado.toFront();
            navigate("Agregar nuevo empleado", pnAgregarEmpleados);
        });
    }

    private void onBtnEditarEmpleado() {
        PerfilEmpleado();
        navigate("Editar empleado", pnAgregarEmpleados);
    }

    private void onBtnVolverEmpleados() {
        confirmarYEjecutar("Desea volver a la pantalla anterior?", () -> {
            LimpiarPerfil();
            navigate("EMPLEADOS", pnEmpleados);
        });
    }

    private void onBtnGuardarEmpleado() {
        confirmarYEjecutar("Desea guardar empleado?", () -> {
            LOGGER.log(Level.FINE, "CON IMAGEN");
            AgregarEmpleadoConImagen();
            navigate("EMPLEADOS", pnEmpleados);
        });
    }

    private void onBtnModificarEmpleado() {
        confirmarYEjecutar("Desea guardar cambios del empleado?", () -> {
            ModificarEmpleadoConImagen();
            navigate("EMPLEADOS", pnEmpleados);
        });
    }

    private void onBtnEliminarEmpleado() {
        confirmarYEjecutar("Desea eliminar empleado?", this::EliminarEmpleado);
    }

    private void onBtnProduccionEmpleado() {
        BuscarEmpleadoConBotonProduccion();
        UpdateProduccionSemanal();
        navigate("PRODUCCION", pnProduccion);
    }

    private void onBtnSubirImagen(ActionEvent actionEvent) {
        Node node = (Node) actionEvent.getSource();
        Stage stage = (Stage) node.getScene().getWindow();
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Subir imagen de perfil");
        fileChooser.getExtensionFilters().addAll(new ExtensionFilter("Imagenes", "*.png", " *.jpg"));
        file = fileChooser.showOpenDialog(stage);
        if (file != null) {
            lbPath.setText(file.getAbsolutePath());
            image = new Image(file.toURI().toString());
            LOGGER.log(Level.FINE, "ESTE ES EL ARCHIVO {0}", file.getAbsolutePath());
            LOGGER.log(Level.FINE, "ESTE ES EL ARCHIVO {0}", file.getPath());
            LOGGER.log(Level.FINE, "CONSEGUI DENTRO {0}", image);
            btnSubirImagen.setText(null);
            btnImagenPerfil.setImage(image);
            LOGGER.log(Level.FINE, "TERMINE DENTRO {0}", btnImagenPerfil.getImage());
            updateImagenPerfil();
        } else {
            file = null;
        }
    }

    // ── PRODUCCION ────────────────────────────────────────────────────────────

    private void onBtnBuscarEmpleadoProduccion() {
        if (tbCodigoProduccion.getText().isEmpty()) {
            showAlert(AlertType.ERROR, "Codigo de usuario vacio", "Llenar los datos correctamente", null);
            tbCodigoProduccion.clear();
        } else {
            BuscarEmpleadoProduccion();
            UpdateProduccionSemanal();
        }
    }

    private void onBtnEditarEmpleado2() {
        PerfilEmpleadoProduccion();
        navigate("Editar empleado", pnAgregarEmpleados);
    }

    private void onBtnLimpiarProduccion() {
        confirmarYEjecutar("Desea limpiar los campos?", this::cleanProduccion);
    }

    private void onBtnGuardarProduccion() {
        if (tbMetros.getText().isEmpty() || tbCantidadProduccion.getText().isEmpty()
                || tbFechaRegistro.getValue() == null
                || cbMaterial.getValue() == null || cbCalibre.getValue() == null
                || cbAltura.getValue() == null || cbRombo.getValue() == null) {
            showAlert(AlertType.ERROR, null, "Uno o mas campos vacios!", null);
            cleanProduccion();
        } else if (lbHCodigo2.getText().isEmpty() || tbCodigoProduccion.getText().isEmpty()) {
            showAlert(AlertType.ERROR, null, "No se ha seleccionado ningun empleado", null);
            cleanProduccion();
        } else {
            confirmarYEjecutar("Desea guardar produccion?", () -> {
                AgregarProduccion();
                UpdateProduccionSemanal();
                DiasSemana();
            });
        }
    }

    private void onBtnHistorial() {
        navigate("HISTORIAL Y REPORTES", pnHistorial);
        if (!lbHCodigo2.getText().isEmpty()) {
            tbCodigoHistorial.setText(lbHCodigo2.getText());
            BuscarEmpleadoHistorial();
            UpdateHistorial();
        }
    }

    private void onBtnVolverHistorial()  { navigate("PRODUCCION", pnProduccion); }

    private void onBtnVolverHistorial2() {
        confirmarYEjecutar("Desea volver a la pantalla anterior?", () -> {
            LimpiarPerfil();
            navigate("PRODUCCION", pnProduccion);
        });
    }

    private void onBtnGuardarEdicionProduccion() {
        ModificarProduccion();
        UpdateProduccionSemanal();
        pnEditarProduccion.toBack();
    }

    // ── HISTORIAL ─────────────────────────────────────────────────────────────

    private void onBtnBuscarHistorial() {
        if (tbCodigoHistorial.getText().isEmpty()) {
            showAlert(AlertType.ERROR, "Codigo de usuario vacio", "Llenar los datos correctamente", null);
            tbCodigoHistorial.clear();
        } else {
            BuscarEmpleadoHistorial();
            UpdateHistorial();
        }
    }

    // ── CATALOGOS ─────────────────────────────────────────────────────────────

    private void onBtnGuardarMaterial() {
        if (tbNombreMaterial.getText().isEmpty()) {
            showAlert(AlertType.ERROR, null, "Uno o mas campos vacios!", null);
        } else {
            confirmarYEjecutar("Desea guardar material?", this::AgregarMaterial);
        }
    }

    private void onBtnGuardarAltura() {
        if (tbNombreAltura.getText().isEmpty() || tbAltura.getText().isEmpty()) {
            tbNombreAltura.clear(); tbAltura.clear();
            showAlert(AlertType.ERROR, null, "Uno o mas campos vacios!", null);
        } else {
            confirmarYEjecutar("Desea guardar altura?", this::AgregarAltura);
        }
    }

    private void onBtnGuardarCalibre() {
        if (tbNombreCalibre.getText().isEmpty() || tbCalibre.getText().isEmpty()) {
            tbNombreCalibre.clear(); tbCalibre.clear();
            showAlert(AlertType.ERROR, null, "Uno o mas campos vacios!", null);
        } else {
            confirmarYEjecutar("Desea guardar calibre?", this::AgregarCalibre);
        }
    }

    private void onBtnGuardarRombo() {
        if (tbNombreRombo.getText().isEmpty() || tbRombo.getText().isEmpty()) {
            tbNombreRombo.clear(); tbRombo.clear();
            showAlert(AlertType.ERROR, null, "Uno o mas campos vacios!", null);
        } else {
            confirmarYEjecutar("Desea guardar separacion de rombos?", this::AgregarRombos);
        }
    }

    private void onBtnEditarAltura() {
        pnEditarMaterial2.toFront();
        btnGuardarEditarAltura.toFront();
        lbMedidaMaterialEditar.setText("ALTURA");
        tbMedidaMaterialEditar.setPromptText("ALTURA");
    }

    private void onBtnEditarCalibre() {
        pnEditarMaterial2.toFront();
        btnGuardarEditarCalibre.toFront();
        lbMedidaMaterialEditar.setText("CALIBRE");
        tbMedidaMaterialEditar.setPromptText("CALIBRE");
    }

    private void onBtnEditarRombo() {
        pnEditarMaterial2.toFront();
        btnGuardarEditarRombos.toFront();
        lbMedidaMaterialEditar.setText("SEPARACION DE ROMBOS");
        tbMedidaMaterialEditar.setPromptText("SEPARACION DE ROMBOS");
    }

    // ── EVENT DISPATCHER ──────────────────────────────────────────────────────

    @FXML
    private void handleClicks(ActionEvent actionEvent) throws IOException {
        Object src = actionEvent.getSource();

        // Navigation — early return so remaining checks are skipped
        if (src == btnPefil)      { onNavPerfil();         return; }
        if (src == btnInicio)     { onNavInicio();          return; }
        if (src == btnEmpleados)  { onNavEmpleados();       return; }
        if (src == btnProduccion) { onNavProduccion();      return; }
        if (src == btnMateriales) { onNavMateriales();      return; }
        if (src == btnExit)       { onNavExit(actionEvent); return; }

        // Profile
        if (src == btncambiarContraseña) onBtnCambiarPassword();
        if (src == btnVolverContraseña)  onBtnVolverPassword();
        if (src == btnGuardarContraseña) onBtnGuardarPassword();

        // Employees
        if (src == btnNuevoEmpleado)      onBtnNuevoEmpleado();
        if (src == btnEditarEmpleado)     onBtnEditarEmpleado();
        if (src == btnVolverEmpleados)    onBtnVolverEmpleados();
        if (src == btnGuardarEmpleado)    onBtnGuardarEmpleado();
        if (src == btnModificarEmpleado)  onBtnModificarEmpleado();
        if (src == btnEliminarEmpleado)   onBtnEliminarEmpleado();
        if (src == btnActualizarEmpleado) UpdateTable();
        if (src == btnProduccionEmpleado) onBtnProduccionEmpleado();
        if (src == btnSubirImagen)        onBtnSubirImagen(actionEvent);

        // Production
        if (src == btnBuscarEmpleado)           onBtnBuscarEmpleadoProduccion();
        if (src == btnEditarEmpleado2)           onBtnEditarEmpleado2();
        if (src == btnLimpiarProduccion)         onBtnLimpiarProduccion();
        if (src == btnNuevoProduccion)           onBtnGuardarProduccion();
        if (src == btnHistorial)                 onBtnHistorial();
        if (src == btnVolverHistorial)           onBtnVolverHistorial();
        if (src == btnVolverHistorial2)          onBtnVolverHistorial2();
        if (src == btnVolverEditartProduccion)   pnEditarProduccion.toBack();
        if (src == btnModificarProduccion)       pnEditarProduccion.toFront();
        if (src == btnGuardarEditartProduccion)  onBtnGuardarEdicionProduccion();
        if (src == btnEditarHistorial)           pnEditarProduccion.toFront();

        // History
        if (src == btnBuscarHistorial) onBtnBuscarHistorial();
        if (src == btnReporte)         ImprimirReporte();

        // Catalogs — save
        if (src == btnGuardarMaterial) onBtnGuardarMaterial();
        if (src == btnGuardarAltura)   onBtnGuardarAltura();
        if (src == btnGuardarCalibre)  onBtnGuardarCalibre();
        if (src == btnGuardarRombo)    onBtnGuardarRombo();

        // Catalogs — delete
        if (src == btnEliminarMaterial) confirmarYEjecutar("Desea eliminar material?",  this::EliminarMaterial);
        if (src == btnEliminarAltura)   confirmarYEjecutar("Desea eliminar altura?",    this::EliminarAltura);
        if (src == btnEliminarCalibre)  confirmarYEjecutar("Desea eliminar calibre?",   this::EliminarCalibre);
        if (src == btnEliminarRombo)    confirmarYEjecutar("Desea eliminar rombos?",    this::EliminarRombo);

        // Catalogs — edit panel (Materiales)
        if (src == btnEditarMaterial)       pnEditarMaterial.toFront();
        if (src == btnVolverEdiatMaterial)  pnEditarMaterial.toBack();
        if (src == btnGuardarEdiatMaterial) { ModificarMaterial(); pnEditarMaterial.toBack(); }

        // Catalogs — edit panel (Alturas / Calibres / Rombos shared)
        if (src == btnVolverEditartMaterial2) { clearCambiarMaterial(); pnEditarMaterial2.toBack(); }
        if (src == btnEditarAltura)            onBtnEditarAltura();
        if (src == btnGuardarEditarAltura)     { ModificarAltura();  pnEditarMaterial2.toBack(); clearCambiarMaterial(); }
        if (src == btnEditarCalibre)           onBtnEditarCalibre();
        if (src == btnGuardarEditarCalibre)    { ModificarCalibre(); pnEditarMaterial2.toBack(); clearCambiarMaterial(); }
        if (src == btnEditarRombo)             onBtnEditarRombo();
        if (src == btnGuardarEditarRombos)     { ModificarRombos();  pnEditarMaterial2.toBack(); clearCambiarMaterial(); }
    }

}
