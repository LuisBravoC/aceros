/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers;

import database.ConnectionUtil;
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
import java.util.function.Function;
import java.util.logging.Level;
import java.util.logging.Logger;
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
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.design.JRDesignQuery;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import net.sf.jasperreports.view.JasperViewer;

/**
 * FXML Controller class
 *
 * @author LuisBravo
 */
public class DashboardController implements Initializable {
    
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
    private File file = new File("D:/sin_perfil.png");
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
    
    private String usuario = LoginController.getSesion();
    String tipo_empleado;
    
    private static Connection con;
    ResultSet resultSet = null;
    PreparedStatement pst = null;
    
    public DashboardController() throws SQLException {
        con = ConnectionUtil.getConnection();
    }
    
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
        
        VerificarTipoEmpleado();
        
        System.out.println("EL TIPO DE EMPLEADO ES: "+tipo_empleado);
        
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
        
        System.out.println("ESTE ES EL ARCHIVO "+file.getAbsolutePath());
        System.out.println("ESTE ES EL ARCHIVO "+file.getPath());
        
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
        UpdateHistorial();
        
        Filtro();
        
        DiasSemana();
        UpdateMesHistorial();
        
        new animatefx.animation.ZoomIn(pnInicio).play();
            
        pnProduccion.toFront();
        
        ComprobarPrimerSesion();
        
    }
    
    public void Filtro(){
        
        FilteredList<Empleados> filteredData = new FilteredList<>(dataList, b -> true);
        // FILTRO ID
        filtroIdUsuario.textProperty().addListener((observable, oldValue, newValue) -> {
			filteredData.setPredicate(empleados -> {
                                // Si el filtro esta vacio despliega a todas las personas
								
				if (newValue == null || newValue.isEmpty()) {
					return true;
				}
                                
				String lowerCaseFilter = newValue.toLowerCase();
				
				if (String.valueOf(empleados.getEmpIdUsuario()).toLowerCase().indexOf(lowerCaseFilter) != -1 ) {
					return true; // Filter matches first name.
				}
				     else  
				    	 return false; // Does not match.
			});
		});
        // FILTRO NOMBRE
        filtroNombreUsuario.textProperty().addListener((observable, oldValue, newValue) -> {
			filteredData.setPredicate(empleados -> {
                                // Si el filtro esta vacio despliega a todas las personas
								
				if (newValue == null || newValue.isEmpty()) {
					return true;
				}
                                
				String lowerCaseFilter = newValue.toLowerCase();
				
				if (String.valueOf(empleados.getEmpNombre()).toLowerCase().indexOf(lowerCaseFilter) != -1 ) {
					return true; // Filter matches first name.
				}
				     else  
				    	 return false; // Does not match.
			});
		});
        // FILTRO EDAD
        filtroEdadUsuario.textProperty().addListener((observable, oldValue, newValue) -> {
			filteredData.setPredicate(empleados -> {
                                // Si el filtro esta vacio despliega a todas las personas
								
				if (newValue == null || newValue.isEmpty()) {
					return true;
				}
                                
				String lowerCaseFilter = newValue.toLowerCase();
				
				if (String.valueOf(empleados.getEmpEdad()).toLowerCase().indexOf(lowerCaseFilter) != -1 ) {
					return true; // Filter matches first name.
				}
				     else  
				    	 return false; // Does not match.
			});
		});
        // FILTRO SUELDO
        filtroSueldoUsuario.textProperty().addListener((observable, oldValue, newValue) -> {
			filteredData.setPredicate(empleados -> {
                                // Si el filtro esta vacio despliega a todas las personas
								
				if (newValue == null || newValue.isEmpty()) {
					return true;
				}
                                
				String lowerCaseFilter = newValue.toLowerCase();
				
				if (String.valueOf(empleados.getEmpSueldo()).toLowerCase().indexOf(lowerCaseFilter) != -1 ) {
					return true; // Filter matches first name.
				}
				     else  
				    	 return false; // Does not match.
			});
		});
        
                 // 3. Wrap the FilteredList in a SortedList. 
		SortedList<Empleados> sortedData = new SortedList<>(filteredData);
		
		// 4. Bind the SortedList comparator to the TableView comparator.
		// 	  Otherwise, sorting the TableView would have no effect.
		sortedData.comparatorProperty().bind(tableviewEmpleados.comparatorProperty());
		
		// 5. Add sorted (and filtered) data to the table.
		tableviewEmpleados.setItems(sortedData);
        
    }
    
    //PERFIL EMPLEADO
    
    public void VerificarTipoEmpleado(){
        
        ResultSet rs = null;
        PreparedStatement ps = null;        
        try{
            ps = con.prepareStatement("SELECT * FROM usuarios Where usuario_id = ?");
            ps.setString(1,usuario);
            rs = ps.executeQuery();
                        
            if(rs.next()){
                
                tipo_empleado = rs.getString("tipo_empleado");
                
                if(tipo_empleado.contains("SUPERVISOR")){
                    DashboardSupervisor();
                }else if(tipo_empleado.contains("EMPLEADO GENERAL")){
                    
                    DashboardGeneral();
                }
               
            }else{
            }   
        }catch (Exception e){
            
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
        
        BusquedaEmpleado busqueda = new BusquedaEmpleado() {};
        ResultSet rs = null;
        rs = busqueda.find(usuario);
        
        try{
            if(rs.next()){
                lbCodigoUsuario.setText(rs.getString("usuario_id"));
                lbNombreEmpleado.setText(rs.getString("nombre"));
                lbAPaternoEmpleado.setText(rs.getString("apellido_paterno"));
                lbAMaternoEmpleado.setText(rs.getString("apellido_materno"));
                lbCurp.setText(rs.getString("curp"));
                lbRfc.setText(rs.getString("rfc"));
                lbNss.setText(rs.getString("nss"));
                
                String timestamp = rs.getString("fecha_nacimiento");
                String año = timestamp.substring(0,4);
                String dia = timestamp.substring(8,10);
                String mes = timestamp.substring(5,7);
                String mesn = "mes";
                switch (mes){
                    case "01": mesn = "ENERO"; break;
                    case "02": mesn = "FEBRERO"; break;
                    case "03": mesn = "MARZO"; break;
                    case "04": mesn = "ABRIL"; break;
                    case "05": mesn = "MAYO"; break;
                    case "06": mesn = "JUNIO"; break;
                    case "07": mesn = "JULIO"; break;
                    case "08": mesn = "AGOSTO"; break;
                    case "09": mesn = "SEPTIEMBRE"; break;
                    case "10": mesn = "OCTUBRE"; break;
                    case "11": mesn = "NOVIEMBRE"; break;
                    case "12": mesn = "DICIEMBRE"; break;
                }
                System.out.println("AÑO "+año);
                System.out.println("DIA "+dia);
                System.out.println("MES "+mes);
                lbHFecha.setText( dia + " DE "+ mesn +" DEL "+ año);
                
                lbFechaNacimiento.setText( dia + " DE "+ mesn +" DE "+ año);
                
                timestamp = rs.getString("fecha_contratacion");
                año = timestamp.substring(0,4);
                dia = timestamp.substring(8,10);
                mes = timestamp.substring(5,7);
                mesn = "mes";
                switch (mes){
                    case "01": mesn = "ENERO"; break;
                    case "02": mesn = "FEBRERO"; break;
                    case "03": mesn = "MARZO"; break;
                    case "04": mesn = "ABRIL"; break;
                    case "05": mesn = "MAYO"; break;
                    case "06": mesn = "JUNIO"; break;
                    case "07": mesn = "JULIO"; break;
                    case "08": mesn = "AGOSTO"; break;
                    case "09": mesn = "SEPTIEMBRE"; break;
                    case "10": mesn = "OCTUBRE"; break;
                    case "11": mesn = "NOVIEMBRE"; break;
                    case "12": mesn = "DICIEMBRE"; break;
                }
                System.out.println("AÑO "+año);
                System.out.println("DIA "+dia);
                System.out.println("MES "+mes);
                
                lbFechaContratacio.setText( dia + " DE "+ mesn +" DE "+ año);

                lbEmailEmpleado.setText(rs.getString("email"));
                lbGenero.setText(rs.getString("genero"));
                lbTipoUsuario.setText(rs.getString("tipo_empleado"));
                lbSueldoEmpleado.setText(rs.getString("sueldo"));
                lbMetodoPago.setText(rs.getString("metodo_pago"));
                lbBanco.setText(rs.getString("banco"));
                lbNCuenta.setText(rs.getString("numero_cuenta"));
                lbPeriodoPago.setText(rs.getString("periodo_pago"));
                lbContrato.setText(rs.getString("tipo_contrato"));
                
                lbPais.setText(rs.getString("pais"));
                lbEstado.setText(rs.getString("estado"));
                lbLocalidad.setText(rs.getString("localidad"));
                lbColonia.setText(rs.getString("colonia"));
                lbNExterior.setText(rs.getString("numero_exterior"));
                
                lbCiudad.setText(rs.getString("ciudad"));
                lbCalle.setText(rs.getString("calle"));
                lbCodigoPostal.setText(rs.getString("codigo_postal"));
                lbNInterior.setText(rs.getString("numero_interior"));
            }else{
            System.out.println("No hay informacion de domicilio ");
        }   
        }catch (Exception e){
            
        }  
    }
    
    public void CambiarContraseña(){
        
        ResultSet rs = null;
        PreparedStatement ps = null;
        String contraseña = tbContraseñaActual.getText();
        System.out.println("ENTRE A CAMBIAR CONTRASEÑA");
        
        try{
            ps = con.prepareStatement("SELECT * FROM usuarios Where usuario_id = ? and password = ?");
            ps.setString(1,usuario);
            ps.setString(2,contraseña);
            rs = ps.executeQuery();
                        
            if(rs.next()){
                
                UpdateContraseña();
               
            }else{
            Alert alert = new Alert(AlertType.ERROR);
            alert.setHeaderText("La contraseña actual no es correcta");
            alert.setContentText("Vuelva a intentarlo de nuevo");
            Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
            stage.getIcons().add(new Image("icons/IconBlanco.png"));
            alert.showAndWait();
        }   
        }catch (Exception e){
            
        }  
    }
    
    public void UpdateContraseña(){
        ResultSet rs = null;
        PreparedStatement ps = null;
        String contraseñanueva = tbContraseñaNueva.getText();
        System.out.println("CONTRASEÑA NUEVA "+contraseñanueva);
        try{
            System.out.println("CASI CAMBIO CONTRASEÑA");
            ps = con.prepareStatement("update usuarios set password='"+contraseñanueva+"', pimera_sesion='1' where usuario_id='"+usuario+"'");
            ps.execute();
            ps.close();
            
            System.out.println("SE CAMBIO CONTRASEÑA");
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
        }catch (Exception e){
        }
        
    }
    
    public void ComprobarPrimerSesion(){
    
        ResultSet rs = null;
        PreparedStatement ps = null;  
        String primera = "0";
        try{
            ps = con.prepareStatement("SELECT * FROM usuarios Where usuario_id = ? and pimera_sesion = ?");
            ps.setString(1,usuario);
            ps.setString(2,primera);
            rs = ps.executeQuery();
                        
            if(rs.next()){
                CambiarContraseñaPrimera();
            System.out.println("USUARIO NO HA CAMBIO CONTRASEÑA");  
            }else{
            System.out.println("USUARIO YA CAMBIO CONTRASEÑA");
        }   
        }catch (Exception e){
            
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
        String sql = "delete from usuarios where usuario_id = ?";
        String in = Integer.toString(indexEmpleado);
        try{
            pst = con.prepareStatement(sql);
            pst.setString(1, in);
            pst.execute();
            UpdateTable();
        } catch (Exception e){
            
        }
        
    }
    
    //ELIMINAR EMPLEADO
    
    //ACTUALIZAR TABLA EMPLEADOS

    public void UpdateTable(){
        empIdUsuario.setCellValueFactory(new PropertyValueFactory<>("EmpIdUsuario"));       
        empNombre.setCellValueFactory(new PropertyValueFactory<>("empNombre"));    
        empEdad.setCellValueFactory(new PropertyValueFactory<>("empEdad"));               
        empSueldo.setCellValueFactory(new PropertyValueFactory<>("empSueldo"));       
        
        listM = ConnectionUtil.getDataUsers();
        tableviewEmpleados.setItems(listM);        
    }
    //ACTUALIZAR TABLA EMPLEADOS
    public void CodigoUsuario(){
        Connection con;
        try {
            
            con = ConnectionUtil.getConnection();      
            ResultSet rs = con.createStatement().executeQuery("select * from usuarios");
           
            while (rs.next()){
                dataList.add(new Empleados(rs.getInt(1),rs.getString("nombre")+" "+rs.getString("apellido_paterno")+" "+rs.getString("apellido_materno"),rs.getString("edad"),rs.getString("sueldo")));
                int id = rs.getInt(1)+1;
                String l_id = Integer.toString(id);
                tbCodigoUsuarioAgregar.setText(l_id);
            }
            
        } catch (SQLException ex) {
            Logger.getLogger(DashboardController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    int indexEmpleado;
    public void TableValueEmpleados(){
      
        
        tableviewEmpleados.setOnMouseClicked(new EventHandler<MouseEvent>(){
            @Override
            public void handle(MouseEvent event) {
                Empleados index = tableviewEmpleados.getItems().get(tableviewEmpleados.getSelectionModel().getSelectedIndex());
                
                if(tableviewEmpleados.isFocused()){
                    if(tipo_empleado.contains("GERENTE")){
                    btnEliminarEmpleado.setVisible(true);
                    btnEditarEmpleado.setDisable(false);
                }else if(tipo_empleado.contains("SUPERVISOR")){
                    btnEditarEmpleado.setDisable(false);
                }
                }else{
                    btnEliminarEmpleado.setVisible(false);
                    btnEditarEmpleado.setDisable(true);
                }
                
                indexEmpleado = index.getEmpIdUsuario();     
                String in = Integer.toString(indexEmpleado);
                
                BusquedaEmpleado busqueda = new BusquedaEmpleado() {};
                ResultSet rs = null;
                rs = busqueda.find(in);
                System.out.println("Index seleccionado "+in);
                
                try{
                    if(rs.next()){
                        lbHDomicilio.setText(rs.getString("tipo_empleado"));
                        String timestamp = rs.getString("create_time");
                        String año = timestamp.substring(0,4);
                        String dia = timestamp.substring(8,10);
                        String mes = timestamp.substring(5,7);
                        String mesn = "mes";
                        switch (mes){
                            case "1": mesn = "ENERO"; break;
                            case "2": mesn = "FEBRERO"; break;
                            case "3": mesn = "MARZO"; break;
                            case "4": mesn = "ABRIL"; break;
                            case "5": mesn = "MAYO"; break;
                            case "6": mesn = "JUNIO"; break;
                            case "7": mesn = "JULIO"; break;
                            case "8": mesn = "AGOSTO"; break;
                            case "9": mesn = "SEPTIEMBRE"; break;
                            case "10": mesn = "OCTUBRE"; break;
                            case "11": mesn = "NOVIEMBRE"; break;
                            case "12": mesn = "DICIEMBRE"; break;
                        }
                        System.out.println("AÑO "+año);
                        System.out.println("DIA "+dia);
                        System.out.println("MES "+mes);
                        lbHFecha.setText( dia + " DE "+ mesn +" DEL "+ año);
                        
                        InputStream is = rs.getBinaryStream("imagen");
                        OutputStream os = new FileOutputStream( new File("photo.png"));
                        byte[] content = new byte[1024];
                        int size = 0;
                        while((size = is.read(content)) != -1){
                            os.write(content, 0, size);
                        }
                        if(is!=null){
                        image = new Image("file:photo.png");
                        imgPerfil.setImage(image);
                        }else{
                        imgPerfil.setImage(backup);
                        }
                        os.close();
                        is.close();
                    }else{
                     imgPerfil.setImage(sinperfil);
                     System.out.println("No hay informacion de domicilio ");
                }   
                }catch (Exception e){

                }
                
                lbHCodigo.setText(String.valueOf(indexEmpleado));
                lbHNombre.setText(index.getEmpNombre());
            }
    });
    }
    
    public class BusquedaEmpleado{
        ResultSet rs = null;
        PreparedStatement ps = null;
        public ResultSet find (String s){
            try{
                ps = con.prepareStatement("select * from usuarios where usuario_id = ?");
                ps.setString(1,s);
                rs = ps.executeQuery();
            } catch (Exception e){
                
            }
            return rs;
        }       
        
    }
    
    // COMBO BOX FILLS
    public void fillComboBoxGenero(){
        try{
            String query = "select genero from genero";
            pst = con.prepareStatement(query);
            ResultSet rs = pst.executeQuery();
            while(rs.next()){
                GeneroOpcion.add(rs.getString("genero"));
            }
            cbGenero.setValue(cbGenero.getValue());
            pst.close();
            rs.close();
        }catch (SQLException ex){
            Logger.getLogger(DashboardController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
    public void fillComboBoxTipoUsuario(){
        try{
            String query = "select puesto from tipo_usuario";
            pst = con.prepareStatement(query);
            ResultSet rs = pst.executeQuery();
            while(rs.next()){
                TipoUsuarioOpcion.add(rs.getString("puesto"));
            }
            cbTipoUsuario.setValue(cbTipoUsuario.getValue());
            pst.close();
            rs.close();
        }catch (SQLException ex){
            Logger.getLogger(DashboardController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
    public void fillComboBoxPago(){
        try{
            String query = "select metodo from tipo_pago";
            pst = con.prepareStatement(query);
            ResultSet rs = pst.executeQuery();
            while(rs.next()){
                PagoOpcion.add(rs.getString("metodo"));
            }
            cbMetodoPago.setValue(cbMetodoPago.getValue());
            pst.close();
            rs.close();
        }catch (SQLException ex){
            Logger.getLogger(DashboardController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
    public void fillComboBoxBanco(){
        try{
            String query = "select nombre from bancos";
            pst = con.prepareStatement(query);
            ResultSet rs = pst.executeQuery();
            while(rs.next()){
                BancoOpcion.add(rs.getString("nombre"));
            }
            cbBanco.setValue(cbBanco.getValue());
            pst.close();
            rs.close();
        }catch (SQLException ex){
            Logger.getLogger(DashboardController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
    public void fillComboBoxPeriodoPago(){
        try{
            String query = "select periodo from periodicidad_pago";
            pst = con.prepareStatement(query);
            ResultSet rs = pst.executeQuery();
            while(rs.next()){
                PeriodoPagoOpcion.add(rs.getString("periodo"));
            }
            cbPeriodoPago.setValue(cbPeriodoPago.getValue());
            pst.close();
            rs.close();
        }catch (SQLException ex){
            Logger.getLogger(DashboardController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
    public void fillComboBoxContrato(){
        try{
            String query = "select contrato from tipo_contratos";
            pst = con.prepareStatement(query);
            ResultSet rs = pst.executeQuery();
            while(rs.next()){
                ContratoOpcion.add(rs.getString("contrato"));
            }
            cbContrato.setValue(cbContrato.getValue());
            pst.close();
            rs.close();
        }catch (SQLException ex){
            Logger.getLogger(DashboardController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
    public void fillComboBoxPais(){
        try{
            String query = "select name from paises";
            pst = con.prepareStatement(query);
            ResultSet rs = pst.executeQuery();
            while(rs.next()){
                PaisOpcion.add(rs.getString("name"));
            }
            cbPais.setValue(cbPais.getValue());
            pst.close();
            rs.close();
        }catch (SQLException ex){
            Logger.getLogger(DashboardController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
    public void fillComboBoxEstados(){
        
        ResultSet rs = null;
        PreparedStatement ps = null;
        String pais = cbPais.getValue();
        EstadoOpcion.clear();

        BusquedaPais busqueda = new BusquedaPais() {};
        rs = busqueda.find(pais);
        try{
            
            if(rs.next()){
                String pais_s = rs.getString("id");
                System.out.println("Pais seleccionado "+pais_s);
            ps = con.prepareStatement("select * from estados where country_id = ?");
            ps.setString(1,pais_s);
            rs = ps.executeQuery();
            while(rs.next()){
                EstadoOpcion.add(rs.getString("name"));
            }
            cbEstado.setValue(cbEstado.getValue());
            pst.close();
            rs.close();
        }
        }catch (SQLException ex){
            Logger.getLogger(DashboardController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
    public class BusquedaPais{
        ResultSet rs = null;
        PreparedStatement ps = null;

        public ResultSet find (String s){
            try{
                ps = con.prepareStatement("select * from paises where name = ?");
                ps.setString(1,s);
                rs = ps.executeQuery();
            } catch (Exception e){
                
            }
            return rs;
        }       
    }
    
        public void fillComboBoxCiudades(){
        
        ResultSet rs = null;
        PreparedStatement ps = null;
        String estado = cbEstado.getValue();
        CiudadOpcion.clear();

        BusquedaEstado busqueda = new BusquedaEstado() {};
        rs = busqueda.find(estado);
        try{
            
            if(rs.next()){
                String estado_s = rs.getString("id");
                System.out.println("Estado seleccionado "+estado_s);
            ps = con.prepareStatement("select * from ciudades where state_id = ?");
            ps.setString(1,estado_s);
            rs = ps.executeQuery();
            while(rs.next()){
                CiudadOpcion.add(rs.getString("name"));

            }
            cbEstado.setValue(cbEstado.getValue());
            pst.close();
            rs.close();
        }
        }catch (SQLException ex){
            Logger.getLogger(DashboardController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
    public class BusquedaEstado{
        ResultSet rs = null;
        PreparedStatement ps = null;

        public ResultSet find (String s){
            try{
                ps = con.prepareStatement("select * from estados where name = ?");
                ps.setString(1,s);
                rs = ps.executeQuery();
            } catch (Exception e){
                
            }
            return rs;
        }       
    }
    // COMBO BOX FILLS
    
    // AGREGAR EMPLEADO
    public void AgregarEmpleado(){
        
        int edad; LocalDate fecha, fecha_con;
        if(tbFechaNacimiento.getValue() == null){
            edad = 0; fecha = LocalDate.now();
        }else { 
        fecha = tbFechaNacimiento.getValue();
        LocalDate date = LocalDate.now();
        edad = Period.between(tbFechaNacimiento.getValue(), date).getYears();   
        }
        
        if(tbFechaContratacion.getValue() == null){
            fecha_con = LocalDate.now();
        }else { 
        fecha_con = tbFechaContratacion.getValue();
        }
        String fecha_nacimiento = fecha.toString();
        String fecha_contratacion = fecha_con.toString();
        
        String nombre = tbNombreEmpleado.getText();         if (tbNombreEmpleado.getText().isEmpty()){nombre = "NULL";}
        String apellido_p = tbAPaternoEmpleado.getText();   if (tbAPaternoEmpleado.getText().isEmpty()){apellido_p = "NULL";}
        String apellido_m = tbAMaternoEmpleado.getText();   if (tbAMaternoEmpleado.getText().isEmpty()){apellido_m = "NULL";}
        String curp = tbCurp.getText();                     if (tbCurp.getText().isEmpty()){curp = "NULL";}
        String rfc = tbRfc.getText();                       if (tbRfc.getText().isEmpty()){rfc = "NULL";}
        String nss = tbNss.getText();                       if (tbNss.getText().isEmpty()){nss = "NULL";}
        String email = tbEmailEmpleado.getText();           if (tbEmailEmpleado.getText().isEmpty()){email = "NULL";}
        String genero = cbGenero.getValue();                if (cbGenero.getValue() == null){genero = "NULL";}
        String sueldo = tbSueldoEmpleado.getText();         if (tbSueldoEmpleado.getText().isEmpty()){sueldo = "0";}
        String metodo_pago = cbMetodoPago.getValue();       if (cbMetodoPago.getValue() == null){metodo_pago = "NULL";}
        String banco = cbBanco.getValue();                  if (cbBanco.getValue() == null){banco = "NULL";}
        String numero_cuenta = tbNCuenta.getText();         if (tbNCuenta.getText().isEmpty()){numero_cuenta = "NULL";}
        String periodo_pago = cbPeriodoPago.getValue();     if (cbPeriodoPago.getValue() == null){periodo_pago = "NULL";}
        String tipo_contrato = cbContrato.getValue();       if (cbContrato.getValue() == null){tipo_contrato = "NULL";}
        String pais = cbPais.getValue();                    if (cbPais.getValue() == null){pais = "NULL";}
        String estado = cbEstado.getValue();                if (cbEstado.getValue() == null){estado = "NULL";}
        String localidad = tbLocalidad.getText();           if (tbLocalidad.getText().isEmpty()){localidad = "NULL";}
        String colonia = tbColonia.getText();               if (tbColonia.getText().isEmpty()){colonia = "NULL";}
        String numero_exterior = tbNExterior.getText();     if (tbNExterior.getText().isEmpty()){numero_exterior = "NULL";}
        String ciudad = cbCiudad.getValue();                if (cbCiudad.getValue() == null){ciudad = "NULL";}
        String calle = tbCalle.getText();                   if (tbCalle.getText().isEmpty()){calle = "NULL";}
        String codigo_postal = tbCodigoPostal.getText();    if (tbCodigoPostal.getText().isEmpty()){codigo_postal = "NULL";}
        String numero_interior = tbNInterior.getText();     if (tbNInterior.getText().isEmpty()){numero_interior = "NULL";}
        String tipo_emp = cbTipoUsuario.getValue();         if (cbTipoUsuario.getValue().isEmpty()){tipo_emp = "NULL";}
        String edad_emp = String.valueOf(edad);
        String password = tbCodigoUsuarioAgregar.getText();
        
        System.out.println("RECORD RUNNING!!!");
        try{
        System.out.println("RECORD RUNNING INSIDE!!!");
        pst = con.prepareStatement("insert into usuarios (nombre, apellido_paterno, apellido_materno, curp, rfc, nss, edad, fecha_nacimiento, fecha_contratacion, "
                + "email, genero, password, sueldo, metodo_pago, banco, numero_cuenta, periodo_pago, tipo_contrato, pais, estado, localidad, colonia, numero_exterior, ciudad, "
                + "calle, codigo_postal, numero_interior, tipo_empleado) "
                + "values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
        System.out.println("RECORD RUNNING AFTER");
        
        pst.setString(1, nombre);
        pst.setString(2, apellido_p);
        pst.setString(3, apellido_m);
        pst.setString(4, curp);
        pst.setString(5, rfc);
        pst.setString(6, nss);
        pst.setString(7, edad_emp);
        pst.setString(8, fecha_nacimiento);
        pst.setString(9, fecha_contratacion);
        pst.setString(10, email);
        pst.setString(11, genero);
        pst.setString(12, password);
        pst.setString(13, sueldo);
        pst.setString(14, metodo_pago);
        pst.setString(15, banco);
        pst.setString(16, numero_cuenta);
        pst.setString(17, periodo_pago);
        pst.setString(18, tipo_contrato);
        pst.setString(19, pais);
        pst.setString(20, estado);
        pst.setString(21, localidad);
        pst.setString(22, colonia);
        pst.setString(23, numero_exterior);
        pst.setString(24, ciudad);
        pst.setString(25, calle);
        pst.setString(26, codigo_postal);
        pst.setString(27, numero_interior);
        pst.setString(28, tipo_emp);
        
        int status = pst.executeUpdate();
        System.out.println("RECORD RUNNING POST QUERY");
        if (status==1){
            System.out.println("RECORD ADDED!!!");
            LimpiarPerfil();
            UpdateTable();
            int id = Integer.parseInt(tbCodigoUsuarioAgregar.getText())+1;
            tbCodigoUsuarioAgregar.setText(String.valueOf(id));
        }else{
            System.out.println("RECORD FAILED!!!");
        }
            
        }catch (SQLException e){
            
        }
        
    }
    
    public void AgregarEmpleadoConImagen(){
        
        int edad; LocalDate fecha, fecha_con;
        if(tbFechaNacimiento.getValue() == null){
            edad = 0; fecha = LocalDate.now();
        }else { 
        fecha = tbFechaNacimiento.getValue();
        LocalDate date = LocalDate.now();
        edad = Period.between(tbFechaNacimiento.getValue(), date).getYears();   
        }
        
        if(tbFechaContratacion.getValue() == null){
            fecha_con = LocalDate.now();
        }else { 
        fecha_con = tbFechaContratacion.getValue();
        }
        String fecha_nacimiento = fecha.toString();
        String fecha_contratacion = fecha_con.toString();
        
        String nombre = tbNombreEmpleado.getText();         if (tbNombreEmpleado.getText().isEmpty()){nombre = "NULL";}
        String apellido_p = tbAPaternoEmpleado.getText();   if (tbAPaternoEmpleado.getText().isEmpty()){apellido_p = "NULL";}
        String apellido_m = tbAMaternoEmpleado.getText();   if (tbAMaternoEmpleado.getText().isEmpty()){apellido_m = "NULL";}
        String curp = tbCurp.getText();                     if (tbCurp.getText().isEmpty()){curp = "NULL";}
        String rfc = tbRfc.getText();                       if (tbRfc.getText().isEmpty()){rfc = "NULL";}
        String nss = tbNss.getText();                       if (tbNss.getText().isEmpty()){nss = "NULL";}
        String email = tbEmailEmpleado.getText();           if (tbEmailEmpleado.getText().isEmpty()){email = "NULL";}
        String genero = cbGenero.getValue();                if (cbGenero.getValue() == null){genero = "NULL";}
        String sueldo = tbSueldoEmpleado.getText();         if (tbSueldoEmpleado.getText().isEmpty()){sueldo = "0";}
        String metodo_pago = cbMetodoPago.getValue();       if (cbMetodoPago.getValue() == null){metodo_pago = "NULL";}
        String banco = cbBanco.getValue();                  if (cbBanco.getValue() == null){banco = "NULL";}
        String numero_cuenta = tbNCuenta.getText();         if (tbNCuenta.getText().isEmpty()){numero_cuenta = "NULL";}
        String periodo_pago = cbPeriodoPago.getValue();     if (cbPeriodoPago.getValue() == null){periodo_pago = "NULL";}
        String tipo_contrato = cbContrato.getValue();       if (cbContrato.getValue() == null){tipo_contrato = "NULL";}
        String pais = cbPais.getValue();                    if (cbPais.getValue() == null){pais = "NULL";}
        String estado = cbEstado.getValue();                if (cbEstado.getValue() == null){estado = "NULL";}
        String localidad = tbLocalidad.getText();           if (tbLocalidad.getText().isEmpty()){localidad = "NULL";}
        String colonia = tbColonia.getText();               if (tbColonia.getText().isEmpty()){colonia = "NULL";}
        String numero_exterior = tbNExterior.getText();     if (tbNExterior.getText().isEmpty()){numero_exterior = "NULL";}
        String ciudad = cbCiudad.getValue();                if (cbCiudad.getValue() == null){ciudad = "NULL";}
        String calle = tbCalle.getText();                   if (tbCalle.getText().isEmpty()){calle = "NULL";}
        String codigo_postal = tbCodigoPostal.getText();    if (tbCodigoPostal.getText().isEmpty()){codigo_postal = "NULL";}
        String numero_interior = tbNInterior.getText();     if (tbNInterior.getText().isEmpty()){numero_interior = "NULL";}
        String tipo_emp = cbTipoUsuario.getValue();         if (cbTipoUsuario.getValue().isEmpty()){tipo_emp = "NULL";}
        String edad_emp = String.valueOf(edad);
        String password = tbCodigoUsuarioAgregar.getText();
        
        System.out.println("RECORD RUNNING!!!");
        try{
        System.out.println("RECORD RUNNING INSIDE!!!");
        pst = con.prepareStatement("insert into usuarios (nombre, apellido_paterno, apellido_materno, curp, rfc, nss, edad, fecha_nacimiento, fecha_contratacion, "
                + "email, genero, password, sueldo, metodo_pago, banco, numero_cuenta, periodo_pago, tipo_contrato, pais, estado, localidad, colonia, numero_exterior, ciudad, "
                + "calle, codigo_postal, numero_interior, tipo_empleado, imagen) "
                + "values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
        System.out.println("RECORD RUNNING AFTER");
        
        pst.setString(1, nombre);
        pst.setString(2, apellido_p);
        pst.setString(3, apellido_m);
        pst.setString(4, curp);
        pst.setString(5, rfc);
        pst.setString(6, nss);
        pst.setString(7, edad_emp);
        pst.setString(8, fecha_nacimiento);
        pst.setString(9, fecha_contratacion);
        pst.setString(10, email);
        pst.setString(11, genero);
        pst.setString(12, password);
        pst.setString(13, sueldo);
        pst.setString(14, metodo_pago);
        pst.setString(15, banco);
        pst.setString(16, numero_cuenta);
        pst.setString(17, periodo_pago);
        pst.setString(18, tipo_contrato);
        pst.setString(19, pais);
        pst.setString(20, estado);
        pst.setString(21, localidad);
        pst.setString(22, colonia);
        pst.setString(23, numero_exterior);
        pst.setString(24, ciudad);
        pst.setString(25, calle);
        pst.setString(26, codigo_postal);
        pst.setString(27, numero_interior);
        pst.setString(28, tipo_emp);
     
        System.out.println("UPLOADING IMAGEN "+file);
        fis = new FileInputStream(file);
        pst.setBinaryStream(29, fis, file.length());
        System.out.println("IMAGEN UPLOADED!!!!");
        
        int status = pst.executeUpdate();
        System.out.println("RECORD RUNNING POST QUERY");
        if (status==1){
            System.out.println("RECORD ADDED!!!");
            LimpiarPerfil();
            UpdateTable();
            int id = Integer.parseInt(tbCodigoUsuarioAgregar.getText())+1;
            tbCodigoUsuarioAgregar.setText(String.valueOf(id));
        }else{
            System.out.println("RECORD FAILED!!!");
        }
            
        }catch (SQLException e){
            
        } catch (FileNotFoundException ex) {
            Logger.getLogger(DashboardController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    // AGREGAR EMPLEADO
    
    // MODIFICAR EMPLEADO
    public void ModificarEmpleado(){
        
        Empleados index = tableviewEmpleados.getItems().get(tableviewEmpleados.getSelectionModel().getSelectedIndex());
        indexEmpleado = index.getEmpIdUsuario();     
        String in = Integer.toString(indexEmpleado);
        
        int edad; LocalDate fecha, fecha_con;
        if(tbFechaNacimiento.getValue() == null){
            edad = 0; fecha = LocalDate.now();
        }else { 
        fecha = tbFechaNacimiento.getValue();
        LocalDate date = LocalDate.now();
        edad = Period.between(tbFechaNacimiento.getValue(), date).getYears();   
        }
        
        if(tbFechaContratacion.getValue() == null){
            fecha_con = LocalDate.now();
        }else { 
        fecha_con = tbFechaContratacion.getValue();
        }
        String fecha_nacimiento = fecha.toString();
        String fecha_contratacion = fecha_con.toString();
        
        String nombre = tbNombreEmpleado.getText();         if (tbNombreEmpleado.getText().isEmpty()){nombre = "NULL";}
        String apellido_p = tbAPaternoEmpleado.getText();   if (tbAPaternoEmpleado.getText().isEmpty()){apellido_p = "NULL";}
        String apellido_m = tbAMaternoEmpleado.getText();   if (tbAMaternoEmpleado.getText().isEmpty()){apellido_m = "NULL";}
        String curp = tbCurp.getText();                     if (tbCurp.getText().isEmpty()){curp = "NULL";}
        String rfc = tbRfc.getText();                       if (tbRfc.getText().isEmpty()){rfc = "NULL";}
        String nss = tbNss.getText();                       if (tbNss.getText().isEmpty()){nss = "NULL";}
        String email = tbEmailEmpleado.getText();           if (tbEmailEmpleado.getText().isEmpty()){email = "NULL";}
        String genero = cbGenero.getValue();                if (cbGenero.getValue() == null){genero = "NULL";}
        String sueldo = tbSueldoEmpleado.getText();         if (tbSueldoEmpleado.getText().isEmpty()){sueldo = "0";}
        String metodo_pago = cbMetodoPago.getValue();       if (cbMetodoPago.getValue() == null){metodo_pago = "NULL";}
        String banco = cbBanco.getValue();                  if (cbBanco.getValue() == null){banco = "NULL";}
        String numero_cuenta = tbNCuenta.getText();         if (tbNCuenta.getText().isEmpty()){numero_cuenta = "NULL";}
        String periodo_pago = cbPeriodoPago.getValue();     if (cbPeriodoPago.getValue() == null){periodo_pago = "NULL";}
        String tipo_contrato = cbContrato.getValue();       if (cbContrato.getValue() == null){tipo_contrato = "NULL";}
        String pais = cbPais.getValue();                    if (cbPais.getValue() == null){pais = "NULL";}
        String estado = cbEstado.getValue();                if (cbEstado.getValue() == null){estado = "NULL";}
        String localidad = tbLocalidad.getText();           if (tbLocalidad.getText().isEmpty()){localidad = "NULL";}
        String colonia = tbColonia.getText();               if (tbColonia.getText().isEmpty()){colonia = "NULL";}
        String numero_exterior = tbNExterior.getText();     if (tbNExterior.getText().isEmpty()){numero_exterior = "NULL";}
        String ciudad = cbCiudad.getValue();                if (cbCiudad.getValue() == null){ciudad = "NULL";}
        String calle = tbCalle.getText();                   if (tbCalle.getText().isEmpty()){calle = "NULL";}
        String codigo_postal = tbCodigoPostal.getText();    if (tbCodigoPostal.getText().isEmpty()){codigo_postal = "NULL";}
        String numero_interior = tbNInterior.getText();     if (tbNInterior.getText().isEmpty()){numero_interior = "NULL";}
        String tipo_emp = cbTipoUsuario.getValue();         if (cbTipoUsuario.getValue().isEmpty()){tipo_emp = "NULL";}
        String edad_emp = String.valueOf(edad);
        
        System.out.println("RECORD RUNNING!!!");
        try{
        System.out.println("RECORD RUNNING INSIDE!!!");
        pst = con.prepareStatement("update usuarios set nombre=?, apellido_paterno=?, apellido_materno=?, curp=?, rfc=?, nss=?, edad=?, fecha_nacimiento=?, "
                + "fecha_contratacion=?, email=?, genero=?, sueldo=?, metodo_pago=?, banco=?, numero_cuenta=?, periodo_pago=?, tipo_contrato=?,"
                + " pais=?, estado=?, localidad=?, colonia=?, numero_exterior=?, ciudad=?, calle=?, codigo_postal=?, numero_interior=?, tipo_empleado=? where usuario_id='"+in+"' ");
        System.out.println("RECORD RUNNING AFTER");
        
        pst.setString(1, nombre);
        pst.setString(2, apellido_p);
        pst.setString(3, apellido_m);
        pst.setString(4, curp);
        pst.setString(5, rfc);
        pst.setString(6, nss);
        pst.setString(7, edad_emp);
        pst.setString(8, fecha_nacimiento);
        pst.setString(9, fecha_contratacion);
        pst.setString(10, email);
        pst.setString(11, genero);
        pst.setString(12, sueldo);
        pst.setString(13, metodo_pago);
        pst.setString(14, banco);
        pst.setString(15, numero_cuenta);
        pst.setString(16, periodo_pago);
        pst.setString(17, tipo_contrato);
        pst.setString(18, pais);
        pst.setString(19, estado);
        pst.setString(20, localidad);
        pst.setString(21, colonia);
        pst.setString(22, numero_exterior);
        pst.setString(23, ciudad);
        pst.setString(24, calle);
        pst.setString(25, codigo_postal);
        pst.setString(26, numero_interior);
        pst.setString(27, tipo_emp);
        int status = pst.executeUpdate();
        System.out.println("RECORD RUNNING POST QUERY");
        if (status==1){
            System.out.println("RECORD ADDED!!!");
            LimpiarPerfil();
            UpdateTable();
            int id = Integer.parseInt(tbCodigoUsuarioAgregar.getText())+1;
            tbCodigoUsuarioAgregar.setText(String.valueOf(id));
        }else{
            System.out.println("RECORD FAILED!!!");
        }
            
        }catch (SQLException e){
            
        }
        
    }
    
    public void ModificarEmpleadoConImagen(){
        
        Empleados index = tableviewEmpleados.getItems().get(tableviewEmpleados.getSelectionModel().getSelectedIndex());
        indexEmpleado = index.getEmpIdUsuario();     
        String in = Integer.toString(indexEmpleado);
        
        int edad; LocalDate fecha, fecha_con;
        if(tbFechaNacimiento.getValue() == null){
            edad = 0; fecha = LocalDate.now();
        }else { 
        fecha = tbFechaNacimiento.getValue();
        LocalDate date = LocalDate.now();
        edad = Period.between(tbFechaNacimiento.getValue(), date).getYears();   
        }
        
        if(tbFechaContratacion.getValue() == null){
            fecha_con = LocalDate.now();
        }else { 
        fecha_con = tbFechaContratacion.getValue();
        }
        String fecha_nacimiento = fecha.toString();
        String fecha_contratacion = fecha_con.toString();
        
        String nombre = tbNombreEmpleado.getText();         if (tbNombreEmpleado.getText().isEmpty()){nombre = "NULL";}
        String apellido_p = tbAPaternoEmpleado.getText();   if (tbAPaternoEmpleado.getText().isEmpty()){apellido_p = "NULL";}
        String apellido_m = tbAMaternoEmpleado.getText();   if (tbAMaternoEmpleado.getText().isEmpty()){apellido_m = "NULL";}
        String curp = tbCurp.getText();                     if (tbCurp.getText().isEmpty()){curp = "NULL";}
        String rfc = tbRfc.getText();                       if (tbRfc.getText().isEmpty()){rfc = "NULL";}
        String nss = tbNss.getText();                       if (tbNss.getText().isEmpty()){nss = "NULL";}
        String email = tbEmailEmpleado.getText();           if (tbEmailEmpleado.getText().isEmpty()){email = "NULL";}
        String genero = cbGenero.getValue();                if (cbGenero.getValue() == null){genero = "NULL";}
        String sueldo = tbSueldoEmpleado.getText();         if (tbSueldoEmpleado.getText().isEmpty()){sueldo = "0";}
        String metodo_pago = cbMetodoPago.getValue();       if (cbMetodoPago.getValue() == null){metodo_pago = "NULL";}
        String banco = cbBanco.getValue();                  if (cbBanco.getValue() == null){banco = "NULL";}
        String numero_cuenta = tbNCuenta.getText();         if (tbNCuenta.getText().isEmpty()){numero_cuenta = "NULL";}
        String periodo_pago = cbPeriodoPago.getValue();     if (cbPeriodoPago.getValue() == null){periodo_pago = "NULL";}
        String tipo_contrato = cbContrato.getValue();       if (cbContrato.getValue() == null){tipo_contrato = "NULL";}
        String pais = cbPais.getValue();                    if (cbPais.getValue() == null){pais = "NULL";}
        String estado = cbEstado.getValue();                if (cbEstado.getValue() == null){estado = "NULL";}
        String localidad = tbLocalidad.getText();           if (tbLocalidad.getText().isEmpty()){localidad = "NULL";}
        String colonia = tbColonia.getText();               if (tbColonia.getText().isEmpty()){colonia = "NULL";}
        String numero_exterior = tbNExterior.getText();     if (tbNExterior.getText().isEmpty()){numero_exterior = "NULL";}
        String ciudad = cbCiudad.getValue();                if (cbCiudad.getValue() == null){ciudad = "NULL";}
        String calle = tbCalle.getText();                   if (tbCalle.getText().isEmpty()){calle = "NULL";}
        String codigo_postal = tbCodigoPostal.getText();    if (tbCodigoPostal.getText().isEmpty()){codigo_postal = "NULL";}
        String numero_interior = tbNInterior.getText();     if (tbNInterior.getText().isEmpty()){numero_interior = "NULL";}
        String tipo_emp = cbTipoUsuario.getValue();         if (cbTipoUsuario.getValue().isEmpty()){tipo_emp = "NULL";}
        String edad_emp = String.valueOf(edad);
        
        System.out.println("RECORD RUNNING!!!");
        try{
        System.out.println("RECORD RUNNING INSIDE!!!");
        pst = con.prepareStatement("update usuarios set nombre=?, apellido_paterno=?, apellido_materno=?, curp=?, rfc=?, nss=?, edad=?, fecha_nacimiento=?, "
                + "fecha_contratacion=?, email=?, genero=?, sueldo=?, metodo_pago=?, banco=?, numero_cuenta=?, periodo_pago=?, tipo_contrato=?,"
                + " pais=?, estado=?, localidad=?, colonia=?, numero_exterior=?, ciudad=?, calle=?, codigo_postal=?, numero_interior=?, tipo_empleado=?, imagen=? where usuario_id='"+in+"' ");
        System.out.println("RECORD RUNNING AFTER");
        
        pst.setString(1, nombre);
        pst.setString(2, apellido_p);
        pst.setString(3, apellido_m);
        pst.setString(4, curp);
        pst.setString(5, rfc);
        pst.setString(6, nss);
        pst.setString(7, edad_emp);
        pst.setString(8, fecha_nacimiento);
        pst.setString(9, fecha_contratacion);
        pst.setString(10, email);
        pst.setString(11, genero);
        pst.setString(12, sueldo);
        pst.setString(13, metodo_pago);
        pst.setString(14, banco);
        pst.setString(15, numero_cuenta);
        pst.setString(16, periodo_pago);
        pst.setString(17, tipo_contrato);
        pst.setString(18, pais);
        pst.setString(19, estado);
        pst.setString(20, localidad);
        pst.setString(21, colonia);
        pst.setString(22, numero_exterior);
        pst.setString(23, ciudad);
        pst.setString(24, calle);
        pst.setString(25, codigo_postal);
        pst.setString(26, numero_interior);
        pst.setString(27, tipo_emp);
        
        System.out.println("UPLOADING IMAGEN "+file);
        fis = new FileInputStream(file);
        pst.setBinaryStream(28, fis, file.length());
        System.out.println("IMAGEN UPLOADED!!!!");
        
        int status = pst.executeUpdate();
        System.out.println("RECORD RUNNING POST QUERY");
        if (status==1){
            System.out.println("RECORD ADDED!!!");
            LimpiarPerfil();
            UpdateTable();
            int id = Integer.parseInt(tbCodigoUsuarioAgregar.getText())+1;
            tbCodigoUsuarioAgregar.setText(String.valueOf(id));
        }else{
            System.out.println("RECORD FAILED!!!");
        }
            
        }catch (SQLException e){
            
        } catch (FileNotFoundException ex) {
            Logger.getLogger(DashboardController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    // MODIFICAR EMPLEADO
    
    public void PerfilEmpleado(){
        
        btnModificarEmpleado.toFront();
        btnVolverEmpleados.toFront();
        Empleados index = tableviewEmpleados.getItems().get(tableviewEmpleados.getSelectionModel().getSelectedIndex());
        indexEmpleado = index.getEmpIdUsuario();     
        String in = Integer.toString(indexEmpleado);
        BusquedaEmpleado busqueda = new BusquedaEmpleado() {};
        ResultSet rs = null;
        rs = busqueda.find(in);
        System.out.println("Index seleccionado "+in);
        
        try{
            if(rs.next()){
                tbCodigoUsuarioAgregar.setText(rs.getString("usuario_id"));
                tbNombreEmpleado.setText(rs.getString("nombre"));
                tbAPaternoEmpleado.setText(rs.getString("apellido_paterno"));
                tbAMaternoEmpleado.setText(rs.getString("apellido_materno"));
                tbCurp.setText(rs.getString("curp"));
                tbRfc.setText(rs.getString("rfc"));
                tbNss.setText(rs.getString("nss"));
                
                String fecha_nacimiento = rs.getString("fecha_nacimiento");  
                LocalDate localDate = LocalDate.parse(fecha_nacimiento);
                tbFechaNacimiento.setValue(localDate);
                
                String fecha_contratacion = rs.getString("fecha_contratacion");  
                localDate = LocalDate.parse(fecha_contratacion);
                tbFechaContratacion.setValue(localDate);

                tbEmailEmpleado.setText(rs.getString("email"));
                cbGenero.setValue(rs.getString("genero"));
                cbTipoUsuario.setValue(rs.getString("tipo_empleado"));
                tbSueldoEmpleado.setText(rs.getString("sueldo"));
                cbMetodoPago.setValue(rs.getString("metodo_pago"));
                cbBanco.setValue(rs.getString("banco"));
                tbNCuenta.setText(rs.getString("numero_cuenta"));
                cbPeriodoPago.setValue(rs.getString("periodo_pago"));
                cbContrato.setValue(rs.getString("tipo_contrato"));
                
                cbPais.setValue(rs.getString("pais"));
                cbEstado.setValue(rs.getString("estado"));
                tbLocalidad.setText(rs.getString("localidad"));
                tbColonia.setText(rs.getString("colonia"));
                tbNExterior.setText(rs.getString("numero_exterior"));
                
                cbCiudad.setValue(rs.getString("ciudad"));
                tbCalle.setText(rs.getString("calle"));
                tbCodigoPostal.setText(rs.getString("codigo_postal"));
                tbNInterior.setText(rs.getString("numero_interior"));
            }else{
            System.out.println("No hay informacion de domicilio ");
        }   
        }catch (Exception e){
            
        }  
    }
    
    public void updateImagenPerfil(){
        System.out.println("CONSEGUI FUERA"+image);
        btnImagenPerfil.setImage(image);
        System.out.println("TERMINE FUERA"+btnImagenPerfil.getImage()); 
    }
    
    public void LimpiarPerfil(){
        tbNombreEmpleado.clear(); tbAPaternoEmpleado.clear(); tbAMaternoEmpleado.clear(); tbCurp.clear(); tbRfc.clear(); tbNss.clear(); tbFechaNacimiento.setValue(null);
        tbFechaContratacion.setValue(null); tbEmailEmpleado.clear(); tbSueldoEmpleado.clear(); cbGenero.setValue(null); cbTipoUsuario.setValue("EMPLEADO GENERAL"); 
        cbMetodoPago.setValue("EFECTIVO"); cbBanco.setValue(null); tbNCuenta.clear(); cbPeriodoPago.setValue("QUINCENAL"); cbContrato.setValue("INDEFINIDO"); 
        cbPais.setValue("Mexico"); cbEstado.setValue("Sinaloa"); tbLocalidad.clear(); tbColonia.clear(); tbNExterior.clear(); cbCiudad.setValue("Culiacan"); 
        tbCalle.clear(); tbCodigoPostal.clear(); tbNInterior.clear(); btnImagenPerfil.setImage(backup); btnSubirImagen.setText("SUBIR IMAGEN"); 
        lbPath.setText(null); file=null; tbCodigoPostal.clear(); tbNInterior.clear(); file = new File("D:/sin_perfil.png");
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
        System.out.println("PAIS SELECCIONADO "+cbPais.getValue());
        System.out.println("PAIS SELECCIONADO "+cbEstado.getValue());
        
    }   
    @FXML
    void updateComboBoxEstado(ActionEvent event) {
        cbPais.setValue(cbPais.getValue());
        cbEstado.setValue(cbEstado.getValue());
        fillComboBoxCiudades();
        System.out.println("PAIS SELECCIONADO "+cbPais.getValue());
        System.out.println("PAIS SELECCIONADO "+cbEstado.getValue());
        
    }

    @FXML
    void updateMetodo(ActionEvent event) {
        
        if (cbMetodoPago.getSelectionModel().isSelected(1)){  
            cbBanco.setDisable(false);
            tbNCuenta.setDisable(false);
            System.out.println("FALSE!!! "+cbMetodoPago.getValue());
        }else{
            cbBanco.setDisable(true);
            tbNCuenta.setDisable(true);
            cbBanco.setValue(null);
            tbNCuenta.clear();
            System.out.println("TRUE!!! "+cbMetodoPago.getValue());
        }
        System.out.println("FUERAAA "+cbMetodoPago.getValue());
    }
    // FIN PANTALLA EMPLEADOS
    
    // PANTALLA PRODUCCION
    
    public class BusquedaProduccion{
        ResultSet rs = null;
        PreparedStatement ps = null;
        public ResultSet find (String s){
            try{
                ps = con.prepareStatement("select * from produccion where id = ?");
                ps.setString(1,s);
                rs = ps.executeQuery();
            } catch (Exception e){
                
            }
            return rs;
        }       
        
    }
    
    String indexProduccionS;
    public void TableProduccionS(){
        
        tvHistorial.setOnMouseClicked(new EventHandler<MouseEvent>(){
            public void handle(MouseEvent event) {
                Historial index = tvHistorial.getItems().get(tvHistorial.getSelectionModel().getSelectedIndex());
                indexProduccionS = index.getTcCodigoHistorial();
                String in = indexProduccionS;
                
                BusquedaProduccion busqueda = new BusquedaProduccion() {};
                ResultSet rs = null;
                rs = busqueda.find(in);
                System.out.println("Index seleccionado "+in);
                
                String date; LocalDate LocalDate = null;
                try{
                    if(rs.next()){
                        cbMaterialEditar.setValue(rs.getString("material"));
                        cbCalibreEditar.setValue(rs.getString("calibre"));
                        cbAlturaEditar.setValue(rs.getString("altura"));
                        cbRomboEditar.setValue(rs.getString("rombos"));
                        tbMetrosEditar.setText(rs.getString("metros"));
                        tbCantidadProduccionEditar.setText(rs.getString("cantidad"));
                        date = rs.getString("fecha_registro");
                        LocalDate = LocalDate.parse(date);    
                        tbFechaRegistroEditar.setValue(LocalDate);
                                            
                        
                    }else{
                     System.out.println("No hay informacion de domicilio ");
                }   
                }catch (Exception e){

                }
                
            }
        }
        );
        
        tvSemanal.setOnMouseClicked(new EventHandler<MouseEvent>(){
            public void handle(MouseEvent event) {
                ProduccionSemanal index = tvSemanal.getItems().get(tvSemanal.getSelectionModel().getSelectedIndex());
                indexProduccionS = index.getTcCodigoS();
                String in = indexProduccionS;
                
                BusquedaProduccion busqueda = new BusquedaProduccion() {};
                ResultSet rs = null;
                rs = busqueda.find(in);
                System.out.println("Index seleccionado "+in);
                
                String date; LocalDate LocalDate = null;
                try{
                    if(rs.next()){
                        cbMaterialEditar.setValue(rs.getString("material"));
                        cbCalibreEditar.setValue(rs.getString("calibre"));
                        cbAlturaEditar.setValue(rs.getString("altura"));
                        cbRomboEditar.setValue(rs.getString("rombos"));
                        tbMetrosEditar.setText(rs.getString("metros"));
                        tbCantidadProduccionEditar.setText(rs.getString("cantidad"));
                        date = rs.getString("fecha_registro");
                        LocalDate = LocalDate.parse(date);    
                        tbFechaRegistroEditar.setValue(LocalDate);
                                            
                        
                    }else{
                     System.out.println("No hay informacion de domicilio ");
                }   
                }catch (Exception e){

                }
                
            }
        }
        );
        
    }
    
    public void ModificarProduccion(){
        
        LocalDate fecha, day;
        if(tbFechaRegistroEditar.getValue() == null){
            fecha = LocalDate.now();
            day = LocalDate.now();
        }else { 
        fecha = tbFechaRegistroEditar.getValue();
        day = tbFechaRegistroEditar.getValue();
        }
        String fecha_registro = fecha.toString();
        DateTimeFormatter format = DateTimeFormatter.ofPattern("EEEE", Locale.getDefault());
        String dia = day.format(format);
        String DiatoUpperCase = dia.toUpperCase();
        
        String id = indexProduccionS;
        String material = cbMaterialEditar.getValue();      if (cbMaterialEditar.getValue() == null){material = "NULL";}
        String calibre = cbCalibreEditar.getValue();        if (cbCalibreEditar.getValue() == null ){calibre = "NULL";}
        String altura = cbAlturaEditar.getValue();          if (cbAlturaEditar.getValue() == null ){altura = "NULL";}
        String rombo = cbRomboEditar.getValue();            if (cbRomboEditar.getValue() == null ){rombo = "NULL";}
        String metros = tbMetrosEditar.getText();           if (tbMetrosEditar.getText().isEmpty()){metros = "NULL";}
        String cantidad = tbCantidadProduccionEditar.getText(); if (tbCantidadProduccionEditar.getText().isEmpty()){cantidad = "NULL";}
        
        try{
        System.out.println("RECORD RUNNING INSIDE!!!");
        pst = con.prepareStatement("update produccion set material= ?, calibre=?, altura=?, rombos=?, metros=?, cantidad=?,fecha_registro=?, dia=? where id='"+id+"'");
        pst.setString(1, material);
        pst.setString(2, calibre);
        pst.setString(3, altura);
        pst.setString(4, rombo);
        pst.setString(5, metros);
        pst.setString(6, cantidad);
        pst.setString(7, fecha_registro);
        pst.setString(8, DiatoUpperCase);
        pst.execute();
        System.out.println("RECORD RUNNING AFTER"); 
        int status = pst.executeUpdate();
        System.out.println("RECORD RUNNING POST QUERY");
        if (status==1){
            System.out.println("RECORD ADDED!!!");
            UpdateProduccionSemanal();
            UpdateHistorial();
        }else{
            System.out.println("RECORD FAILED!!!");
        }
        pst.close();    
        }catch (SQLException e){
            
        }
    
    }
    
    public void FechaActualProduccion(){
        tbFechaRegistro.setValue(LocalDate.now());
        tbFechaRegistroEditar.setValue(LocalDate.now());
    }
    
    public void fillComboBoxMaterial(){
        try{
            String query = "select nombre from materiales";
            pst = con.prepareStatement(query);
            ResultSet rs = pst.executeQuery();
            while(rs.next()){
                MaterialOpcion.add(rs.getString("nombre"));
                EditarMaterialOpcion.add(rs.getString("nombre"));
            }
            cbMaterial.setValue(cbMaterial.getValue());
            cbMaterialEditar.setValue(cbMaterialEditar.getValue());
            pst.close();
            rs.close();
        }catch (SQLException ex){
            Logger.getLogger(DashboardController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
    public void fillComboBoxAltura(){
        try{
            String query = "select altura from alturas";
            pst = con.prepareStatement(query);
            ResultSet rs = pst.executeQuery();
            while(rs.next()){
                AlturaOpcion.add(rs.getString("altura"));
                EditarAlturaOpcion.add(rs.getString("altura"));
            }
            cbAltura.setValue(cbAltura.getValue());
            cbAlturaEditar.setValue(cbAlturaEditar.getValue());
            pst.close();
            rs.close();
        }catch (SQLException ex){
            Logger.getLogger(DashboardController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
    public void fillComboBoxCalibre(){
        try{
            String query = "select calibre from calibres";
            pst = con.prepareStatement(query);
            ResultSet rs = pst.executeQuery();
            while(rs.next()){
                CalibreOpcion.add(rs.getString("calibre"));
                EditarCalibreOpcion.add(rs.getString("calibre"));
            }
            cbCalibre.setValue(cbCalibre.getValue());
            cbCalibreEditar.setValue(cbCalibreEditar.getValue());
            pst.close();
            rs.close();
        }catch (SQLException ex){
            Logger.getLogger(DashboardController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
    public void fillComboBoxRombo(){
        try{
            String query = "select rombo from rombos";
            pst = con.prepareStatement(query);
            ResultSet rs = pst.executeQuery();
            while(rs.next()){
                RomboOpcion.add(rs.getString("rombo"));
                EditarRomboOpcion.add(rs.getString("rombo"));
            }
            cbRombo.setValue(cbRombo.getValue());
            cbRomboEditar.setValue(cbRomboEditar.getValue());
            pst.close();
            rs.close();
        }catch (SQLException ex){
            Logger.getLogger(DashboardController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    public void AgregarProduccion(){
        
        LocalDate fecha, day;
        if(tbFechaRegistro.getValue() == null){
            fecha = LocalDate.now();
            day = LocalDate.now();
        }else { 
        fecha = tbFechaRegistro.getValue();
        day = tbFechaRegistro.getValue();
        }
        String fecha_registro = fecha.toString();
        DateTimeFormatter format = DateTimeFormatter.ofPattern("EEEE", Locale.getDefault());
        String dia = day.format(format);
        String DiatoUpperCase = dia.toUpperCase();
        
        String material = cbMaterial.getValue();         if (cbMaterial.getValue().isEmpty()){material = "NULL";}
        String calibre = cbCalibre.getValue();           if (cbCalibre.getValue().isEmpty()){calibre = "NULL";}
        String altura = cbAltura.getValue();             if (cbAltura.getValue().isEmpty()){altura = "NULL";}
        String rombos = cbRombo.getValue();              if (cbRombo.getValue().isEmpty()){rombos = "NULL";}
        String metros = tbMetros.getText();              if (tbMetros.getText().isEmpty()){metros = "NULL";}
        String cantidad = tbCantidadProduccion.getText();if (tbCantidadProduccion.getText().isEmpty()){cantidad = "NULL";}
        String autorid = lbHCodigo2.getText();           if (lbHCodigo2.getText().isEmpty()){autorid = "NULL";}
        String autor = lbHNombre2.getText();             if (lbHNombre2.getText().isEmpty()){autor = "NULL";}
        
        
        
        try{
        System.out.println("RECORD RUNNING INSIDE!!!");
        pst = con.prepareStatement("insert into produccion (material, calibre, altura, rombos, metros, cantidad, autor_id, autor, fecha_registro, dia) "
                + "values(?,?,?,?,?,?,?,?,?,?)");
        System.out.println("RECORD RUNNING AFTER");
        
        pst.setString(1, material);
        pst.setString(2, calibre);
        pst.setString(3, altura);
        pst.setString(4, rombos);
        pst.setString(5, metros);
        pst.setString(6, cantidad);
        pst.setString(7, autorid);
        pst.setString(8, autor);
        pst.setString(9, fecha_registro);
        pst.setString(10, DiatoUpperCase);
        int status = pst.executeUpdate();
        System.out.println("RECORD RUNNING POST QUERY");
        if (status==1){
            System.out.println("RECORD ADDED!!!");
            cleanProduccion();
        }else{
            System.out.println("RECORD FAILED!!!");
        }
            
        }catch (SQLException e){
            
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
                
                String in = tbCodigoProduccion.getText();
                
                BusquedaEmpleado busqueda = new BusquedaEmpleado() {};
                ResultSet rs = null;
                rs = busqueda.find(in);
                System.out.println("Index seleccionado "+in);
                
                try{
                    if(rs.next()){
                        btnEditarEmpleado2.setDisable(false);
                        lbHCodigo2.setText(rs.getString("usuario_id"));
                        lbHNombre2.setText(rs.getString("nombre")+" "+rs.getString("apellido_paterno")+" "+rs.getString("apellido_materno"));
                        lbHDomicilio2.setText(rs.getString("tipo_empleado"));
                        String timestamp = rs.getString("create_time");
                        String año = timestamp.substring(0,4);
                        String dia = timestamp.substring(8,10);
                        String mes = timestamp.substring(5,7);
                        String mesn = "mes";
                        switch (mes){
                            case "1": mesn = "ENERO"; break;
                            case "2": mesn = "FEBRERO"; break;
                            case "3": mesn = "MARZO"; break;
                            case "4": mesn = "ABRIL"; break;
                            case "5": mesn = "MAYO"; break;
                            case "6": mesn = "JUNIO"; break;
                            case "7": mesn = "JULIO"; break;
                            case "8": mesn = "AGOSTO"; break;
                            case "9": mesn = "SEPTIEMBRE"; break;
                            case "10": mesn = "OCTUBRE"; break;
                            case "11": mesn = "NOVIEMBRE"; break;
                            case "12": mesn = "DICIEMBRE"; break;
                        }
                        System.out.println("AÑO "+año);
                        System.out.println("DIA "+dia);
                        System.out.println("MES "+mes);
                        lbHFecha2.setText( dia + " DE "+ mesn +" DEL "+ año);
                        
                        InputStream is = rs.getBinaryStream("imagen");
                        OutputStream os = new FileOutputStream( new File("photo.png"));
                        byte[] content = new byte[1024];
                        int size = 0;
                        while((size = is.read(content)) != -1){
                            os.write(content, 0, size);
                        }
                        if(is!=null){
                        image = new Image("file:photo.png");
                        imgPerfilProduccion.setImage(image);
                        }else{
                        imgPerfilProduccion.setImage(backup);
                        }
                        os.close();
                        is.close();
                    }else{
                     Alert alert = new Alert(AlertType.ERROR);
                     Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
                     stage.getIcons().add(new Image("icons/IconBlanco.png"));
                     alert.setTitle("No hay coincidencias");
                     alert.setHeaderText("No se encontro empleado");
                     alert.showAndWait();
                     imgPerfil.setImage(sinperfil);
                     System.out.println("No hay informacion de domicilio ");
                }   
                }catch (Exception e){

                }

    }
    
    public void BuscarEmpleadoConBotonProduccion(){
                
                String in = lbHCodigo.getText();
                
                BusquedaEmpleado busqueda = new BusquedaEmpleado() {};
                ResultSet rs = null;
                rs = busqueda.find(in);
                System.out.println("Index seleccionado "+in);
                
                try{
                    if(rs.next()){
                        btnEditarEmpleado2.setDisable(false);
                        tbCodigoProduccion.setText(rs.getString("usuario_id"));
                        lbHCodigo2.setText(rs.getString("usuario_id"));
                        lbHNombre2.setText(rs.getString("nombre")+" "+rs.getString("apellido_paterno")+" "+rs.getString("apellido_materno"));
                        lbHDomicilio2.setText(rs.getString("tipo_empleado"));
                        String timestamp = rs.getString("create_time");
                        String año = timestamp.substring(0,4);
                        String dia = timestamp.substring(8,10);
                        String mes = timestamp.substring(5,7);
                        String mesn = "mes";
                        switch (mes){
                            case "1": mesn = "ENERO"; break;
                            case "2": mesn = "FEBRERO"; break;
                            case "3": mesn = "MARZO"; break;
                            case "4": mesn = "ABRIL"; break;
                            case "5": mesn = "MAYO"; break;
                            case "6": mesn = "JUNIO"; break;
                            case "7": mesn = "JULIO"; break;
                            case "8": mesn = "AGOSTO"; break;
                            case "9": mesn = "SEPTIEMBRE"; break;
                            case "10": mesn = "OCTUBRE"; break;
                            case "11": mesn = "NOVIEMBRE"; break;
                            case "12": mesn = "DICIEMBRE"; break;
                        }
                        System.out.println("AÑO "+año);
                        System.out.println("DIA "+dia);
                        System.out.println("MES "+mes);
                        lbHFecha2.setText( dia + " DE "+ mesn +" DEL "+ año);
                        
                        InputStream is = rs.getBinaryStream("imagen");
                        OutputStream os = new FileOutputStream( new File("photo.png"));
                        byte[] content = new byte[1024];
                        int size = 0;
                        while((size = is.read(content)) != -1){
                            os.write(content, 0, size);
                        }
                        if(is!=null){
                        image = new Image("file:photo.png");
                        imgPerfilProduccion.setImage(image);
                        }else{
                        imgPerfilProduccion.setImage(backup);
                        }
                        os.close();
                        is.close();
                    }else{
                     Alert alert = new Alert(AlertType.ERROR);
                     Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
                     stage.getIcons().add(new Image("icons/IconBlanco.png"));
                     alert.setTitle("No hay coincidencias");
                     alert.setHeaderText("No se encontro empleado");
                     alert.showAndWait();
                     imgPerfil.setImage(sinperfil);
                     System.out.println("No hay informacion de domicilio ");
                }   
                }catch (Exception e){

                }

    }
    
    public void PerfilEmpleadoProduccion(){
        
        btnModificarEmpleado.toFront();
        btnVolverHistorial.toFront();
        String in = lbHCodigo2.getText();
        BusquedaEmpleado busqueda = new BusquedaEmpleado() {};
        ResultSet rs = null;
        rs = busqueda.find(in);
        System.out.println("Index seleccionado "+in);
        
        try{
            if(rs.next()){
                tbCodigoUsuarioAgregar.setText(rs.getString("usuario_id"));
                tbNombreEmpleado.setText(rs.getString("nombre"));
                tbAPaternoEmpleado.setText(rs.getString("apellido_paterno"));
                tbAMaternoEmpleado.setText(rs.getString("apellido_materno"));
                tbCurp.setText(rs.getString("curp"));
                tbRfc.setText(rs.getString("rfc"));
                tbNss.setText(rs.getString("nss"));
                
                String fecha_nacimiento = rs.getString("fecha_nacimiento");  
                LocalDate localDate = LocalDate.parse(fecha_nacimiento);
                tbFechaNacimiento.setValue(localDate);
                
                String fecha_contratacion = rs.getString("fecha_contratacion");  
                localDate = LocalDate.parse(fecha_contratacion);
                tbFechaContratacion.setValue(localDate);

                tbEmailEmpleado.setText(rs.getString("email"));
                cbGenero.setValue(rs.getString("genero"));
                cbTipoUsuario.setValue(rs.getString("tipo_empleado"));
                tbSueldoEmpleado.setText(rs.getString("sueldo"));
                cbMetodoPago.setValue(rs.getString("metodo_pago"));
                cbBanco.setValue(rs.getString("banco"));
                tbNCuenta.setText(rs.getString("numero_cuenta"));
                cbPeriodoPago.setValue(rs.getString("periodo_pago"));
                cbContrato.setValue(rs.getString("tipo_contrato"));
                
                cbPais.setValue(rs.getString("pais"));
                cbEstado.setValue(rs.getString("estado"));
                tbLocalidad.setText(rs.getString("localidad"));
                tbColonia.setText(rs.getString("colonia"));
                tbNExterior.setText(rs.getString("numero_exterior"));
                
                cbCiudad.setValue(rs.getString("ciudad"));
                tbCalle.setText(rs.getString("calle"));
                tbCodigoPostal.setText(rs.getString("codigo_postal"));
                tbNInterior.setText(rs.getString("numero_interior"));
            }else{
            System.out.println("No hay informacion de domicilio ");
        }   
        }catch (Exception e){
            
        }  
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
        
        listSemanal = ConnectionUtil.getProduccionSemana(autor);
        tvSemanal.setItems(listSemanal);        
    }
    
    // FIN PANTALLA PRODUCCION
    // PANTALLA HISTORIAL
    
    public void ImprimirReporte(){        
        
        PreparedStatement ps = null;
        
        LocalDate fechaDe, fechaA;
        fechaDe = tbFechaDe.getValue();        
        fechaA = tbFechaA.getValue();

        String autor = tbCodigoHistorial.getText();
        String de = fechaDe.toString();
        String a = fechaA.toString();
        
        try {
            JasperDesign jdesign = JRXmlLoader.load("D:\\Documentos\\Luis Bravo\\Semestre 4\\Ingenieria de Software II\\App\\aceros-y-trefilados\\src\\controllers\\report.jrxml");
           
            String Query;
            
            Query = "select * from produccion where autor_id = '"+autor+"' and (fecha_registro BETWEEN '"+de+"' AND '"+a+"') order by fecha_registro";
            
            System.out.println(Query);
            
            JRDesignQuery updateQuery = new JRDesignQuery();
            
            updateQuery.setText(Query);
            
            jdesign.setQuery(updateQuery);
            
            JasperReport jreport = JasperCompileManager.compileReport(jdesign);
            
            JasperPrint jprint = JasperFillManager.fillReport(jreport, null, con);
            
            JasperViewer.viewReport(jprint, false);
            
        } catch (JRException ex) {
            Logger.getLogger(DashboardController.class.getName()).log(Level.SEVERE, null, ex);
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
        
        int año;
        String sAño;

        año = tbFechaDe.getValue().getYear();
        sAño = Integer.toString(año);

        
        cbHistorialMes.valueProperty().addListener((newValue) -> {
            
            LocalDate date1, date2;
            String nuevaFecha1, nuevaFecha2;
            Month month;
            
            if(cbHistorialMes.getValue() == "ENERO"){  
                month = Month.JANUARY;
                nuevaFecha1 = sAño+"-01-"+"01";
                nuevaFecha2 = sAño+"-01-"+month.length(true);
                date1 = LocalDate.parse(nuevaFecha1);
                date2 = LocalDate.parse(nuevaFecha2);
                tbFechaDe.setValue(date1);
                tbFechaA.setValue(date2);
            }
            
            if(cbHistorialMes.getValue() == "FEBRERO"){  
                month = Month.FEBRUARY;
                nuevaFecha1 = sAño+"-02-"+"01";
                nuevaFecha2 = sAño+"-02-"+month.length(true);
                date1 = LocalDate.parse(nuevaFecha1);
                date2 = LocalDate.parse(nuevaFecha2);
                tbFechaDe.setValue(date1);
                tbFechaA.setValue(date2);
            }
            
            if(cbHistorialMes.getValue() == "MARZO"){  
                month = Month.MARCH;
                nuevaFecha1 = sAño+"-03-"+"01";
                nuevaFecha2 = sAño+"-03-"+month.length(true);
                date1 = LocalDate.parse(nuevaFecha1);
                date2 = LocalDate.parse(nuevaFecha2);
                tbFechaDe.setValue(date1);
                tbFechaA.setValue(date2);
            }
            
            if(cbHistorialMes.getValue() == "ABRIL"){  
                month = Month.APRIL;
                nuevaFecha1 = sAño+"-04-"+"01";
                nuevaFecha2 = sAño+"-04-"+month.length(true);
                date1 = LocalDate.parse(nuevaFecha1);
                date2 = LocalDate.parse(nuevaFecha2);
                tbFechaDe.setValue(date1);
                tbFechaA.setValue(date2);
            }
            
            if(cbHistorialMes.getValue() == "MAYO"){  
                month = Month.MAY;
                nuevaFecha1 = sAño+"-05-"+"01";
                nuevaFecha2 = sAño+"-05-"+month.length(true);
                date1 = LocalDate.parse(nuevaFecha1);
                date2 = LocalDate.parse(nuevaFecha2);
                tbFechaDe.setValue(date1);
                tbFechaA.setValue(date2);
            }
            
            if(cbHistorialMes.getValue() == "JUNIO"){  
                month = Month.JUNE;
                nuevaFecha1 = sAño+"-06-"+"01";
                nuevaFecha2 = sAño+"-06-"+month.length(true);
                date1 = LocalDate.parse(nuevaFecha1);
                date2 = LocalDate.parse(nuevaFecha2);
                tbFechaDe.setValue(date1);
                tbFechaA.setValue(date2);
            }
            
            if(cbHistorialMes.getValue() == "JULIO"){  
                month = Month.JULY;
                nuevaFecha1 = sAño+"-07-"+"01";
                nuevaFecha2 = sAño+"-07-"+month.length(true);
                date1 = LocalDate.parse(nuevaFecha1);
                date2 = LocalDate.parse(nuevaFecha2);
                tbFechaDe.setValue(date1);
                tbFechaA.setValue(date2);
            }
            
            if(cbHistorialMes.getValue() == "AGOSTO"){  
                month = Month.AUGUST;
                nuevaFecha1 = sAño+"-08-"+"01";
                nuevaFecha2 = sAño+"-08-"+month.length(true);
                date1 = LocalDate.parse(nuevaFecha1);
                date2 = LocalDate.parse(nuevaFecha2);
                tbFechaDe.setValue(date1);
                tbFechaA.setValue(date2);
            }
            
            if(cbHistorialMes.getValue() == "SEPTIEMBRE"){  
                month = Month.SEPTEMBER;
                nuevaFecha1 = sAño+"-09-"+"01";
                nuevaFecha2 = sAño+"-09-"+month.length(true);
                date1 = LocalDate.parse(nuevaFecha1);
                date2 = LocalDate.parse(nuevaFecha2);
                tbFechaDe.setValue(date1);
                tbFechaA.setValue(date2);
            }
            
            if(cbHistorialMes.getValue() == "OCTUBRE"){  
                month = Month.OCTOBER;
                nuevaFecha1 = sAño+"-10-"+"01";
                nuevaFecha2 = sAño+"-10-"+month.length(true);
                date1 = LocalDate.parse(nuevaFecha1);
                date2 = LocalDate.parse(nuevaFecha2);
                tbFechaDe.setValue(date1);
                tbFechaA.setValue(date2);
            }
            
            if(cbHistorialMes.getValue() == "NOVIEMBRE"){  
                month = Month.NOVEMBER;
                nuevaFecha1 = sAño+"-11-"+"01";
                nuevaFecha2 = sAño+"-11-"+month.length(true);
                date1 = LocalDate.parse(nuevaFecha1);
                date2 = LocalDate.parse(nuevaFecha2);
                tbFechaDe.setValue(date1);
                tbFechaA.setValue(date2);
            }
            
            if(cbHistorialMes.getValue() == "DICIEMBRE"){  
                month = Month.DECEMBER;
                nuevaFecha1 = sAño+"-12-"+"01";
                nuevaFecha2 = sAño+"-12-"+month.length(true);
                date1 = LocalDate.parse(nuevaFecha1);
                date2 = LocalDate.parse(nuevaFecha2);
                tbFechaDe.setValue(date1);
                tbFechaA.setValue(date2);
            }
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
        
        listHistorial = ConnectionUtil.getHistorial(autor, de, a);
        tvHistorial.setItems(listHistorial);      
    }
    
    public void BuscarEmpleadoHistorial(){
                
                String in = tbCodigoHistorial.getText();
                
                BusquedaEmpleado busqueda = new BusquedaEmpleado() {};
                ResultSet rs = null;
                rs = busqueda.find(in);
                System.out.println("Index seleccionado "+in);
                
                try{
                    if(rs.next()){
                        lbHCodigoHistorial.setText(rs.getString("usuario_id"));
                        lbHNombreHistorial.setText(rs.getString("nombre"));
                        lbHApellidoHistorial.setText(rs.getString("apellido_paterno")+" "+rs.getString("apellido_materno"));
                        lbHDomicilioHistorial.setText(rs.getString("tipo_empleado"));                        
                        InputStream is = rs.getBinaryStream("imagen");
                        OutputStream os = new FileOutputStream( new File("photo.png"));
                        byte[] content = new byte[1024];
                        int size = 0;
                        while((size = is.read(content)) != -1){
                            os.write(content, 0, size);
                        }
                        if(is!=null){
                        image = new Image("file:photo.png");
                        imgPerfilHistorial.setImage(image);
                        }else{
                        imgPerfilHistorial.setImage(backup);
                        }
                        os.close();
                        is.close();
                    }else{
                     Alert alert = new Alert(AlertType.ERROR);
                     Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
                     stage.getIcons().add(new Image("icons/IconBlanco.png"));
                     alert.setTitle("No hay coincidencias");
                     alert.setHeaderText("No se encontro empleado");
                     alert.showAndWait();
                     imgPerfilHistorial.setImage(sinperfil);
                     System.out.println("No hay informacion de domicilio ");
                }   
                }catch (Exception e){

                }

    }
        
    // FIN PANTALLA HISTORIAL
    // PANTALLA MATERIALES
       
    public class BusquedaMaterial{
        ResultSet rs = null;
        PreparedStatement ps = null;
        public ResultSet find (String s){
            try{
                ps = con.prepareStatement("select * from materiales where id = ?");
                ps.setString(1,s);
                rs = ps.executeQuery();
            } catch (Exception e){
                
            }
            return rs;
        }       
        
    }
    
    int indexMaterial;
    public void TableMateriales(){
        
        tvMateriales.setOnMouseClicked(new EventHandler<MouseEvent>(){
            public void handle(MouseEvent event) {
                Materiales index = tvMateriales.getItems().get(tvMateriales.getSelectionModel().getSelectedIndex());
                indexMaterial = index.getTcCodigoMaterial();
                String in = Integer.toString(indexMaterial);
                
                BusquedaMaterial busqueda = new BusquedaMaterial() {};
                ResultSet rs = null;
                rs = busqueda.find(in);
                System.out.println("Index seleccionado "+in);
                
                try{
                    if(rs.next()){
                        tbCodigoMaterialEditar.setText(rs.getString("id"));
                        tbNombreMaterialEditar.setText(rs.getString("nombre"));
                    }else{
                     System.out.println("No hay informacion de domicilio ");
                }   
                }catch (Exception e){

                }
                
            }
        }
        );
        
    }
    
    public void EliminarMaterial(){
        String sql = "delete from materiales where id = ?";
        String in = Integer.toString(indexMaterial);
        try{
            pst = con.prepareStatement(sql);
            pst.setString(1, in);
            pst.execute();
            UpdateMateriales();
            CodigoMaterial();
            cbMaterial.getItems().clear();
            fillComboBoxMaterial();
        } catch (Exception e){
            
        }
        
    }
    
    public void CodigoMaterial(){
        Connection con;
        try {
            
            con = ConnectionUtil.getConnection();      
            ResultSet rs = con.createStatement().executeQuery("select id from materiales");
           
            if(rs.next()){
              while (rs.next()){
                int id = rs.getInt(1)+1;
                String l_id = Integer.toString(id);
                tbCodigoMaterial.setText(l_id);
            }  
            }else{
                tbCodigoMaterial.setText("1");
            }
            
            
        } catch (SQLException ex) {
            Logger.getLogger(DashboardController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void UpdateMateriales(){
        tcCodigoMaterial.setCellValueFactory(new PropertyValueFactory<>("tcCodigoMaterial"));       
        tcNombreMaterial.setCellValueFactory(new PropertyValueFactory<>("tcNombreMaterial"));       
        
        listMaterial = ConnectionUtil.getMateriales();
        tvMateriales.setItems(listMaterial);        
    }
    
    public void AgregarMaterial(){
        
        //String codigo = tbCodigoMaterial.getText();     if (tbCodigoMaterial.getText().isEmpty()){codigo = "NULL";}
        String nombre = tbNombreMaterial.getText();     if (tbNombreMaterial.getText().isEmpty()){nombre = "NULL";}
        
        try{
        System.out.println("RECORD RUNNING INSIDE!!!");
        pst = con.prepareStatement("insert into materiales (nombre) "
                + "values(?)");
        System.out.println("RECORD RUNNING AFTER");
        
        pst.setString(1, nombre);      
        int status = pst.executeUpdate();
        System.out.println("RECORD RUNNING POST QUERY");
        if (status==1){
            System.out.println("RECORD ADDED!!!");
            tbNombreMaterial.clear();
            UpdateMateriales();
            CodigoMaterial();
            cbMaterial.getItems().clear();
            fillComboBoxMaterial();
        }else{
            System.out.println("RECORD FAILED!!!");
        }
            
        }catch (SQLException e){
            
        }
    
    }
    
    public void ModificarMaterial(){
        
        String id = tbCodigoMaterialEditar.getText();     if (tbCodigoMaterialEditar.getText().isEmpty()){id = "NULL";}
        String nombre = tbNombreMaterialEditar.getText(); if (tbNombreMaterialEditar.getText().isEmpty()){nombre = "NULL";}
        
        try{
        System.out.println("RECORD RUNNING INSIDE!!!");
        pst = con.prepareStatement("update materiales set nombre= ? where id='"+id+"'");
        pst.setString(1, nombre);  
        pst.execute();
        System.out.println("RECORD RUNNING AFTER"); 
        int status = pst.executeUpdate();
        System.out.println("RECORD RUNNING POST QUERY");
        if (status==1){
            System.out.println("RECORD ADDED!!!");
            UpdateMateriales();
            CodigoMaterial();
            cbMaterial.getItems().clear();
            fillComboBoxMaterial();
        }else{
            System.out.println("RECORD FAILED!!!");
        }
        pst.close();    
        }catch (SQLException e){
            
        }
    
    }
    
    
    public class BusquedaAltura{
        ResultSet rs = null;
        PreparedStatement ps = null;
        public ResultSet find (String s){
            try{
                ps = con.prepareStatement("select * from alturas where id = ?");
                ps.setString(1,s);
                rs = ps.executeQuery();
            } catch (Exception e){
                
            }
            return rs;
        }       
        
    }
    
    int indexAltura;
    public void TableAltura(){
        
        tvAlturas.setOnMouseClicked(new EventHandler<MouseEvent>(){
            public void handle(MouseEvent event) {
                Alturas index = tvAlturas.getItems().get(tvAlturas.getSelectionModel().getSelectedIndex());
                indexAltura = index.getTcCodigoAltura();
                String in = Integer.toString(indexAltura);
                
                BusquedaAltura busqueda = new BusquedaAltura() {};
                ResultSet rs = null;
                rs = busqueda.find(in);
                System.out.println("Index seleccionado "+in);
                
                try{
                    if(rs.next()){
                        tbCodigoMaterialEditar2.setText(rs.getString("id"));
                        tbNombreMaterialEditar2.setText(rs.getString("nombre"));
                        tbMedidaMaterialEditar.setText(rs.getString("altura"));
                    }else{
                     System.out.println("No hay informacion de domicilio ");
                }   
                }catch (Exception e){

                }
                
            }
        }
        );
        
    }
    
    public void EliminarAltura(){
        String sql = "delete from alturas where id = ?";
        String in = Integer.toString(indexAltura);
        try{
            pst = con.prepareStatement(sql);
            pst.setString(1, in);
            pst.execute();
            UpdateAlturas();
            CodigoAltura();
            cbAltura.getItems().clear();
            fillComboBoxAltura();
        } catch (Exception e){
            
        }
        
    }
    
    public void CodigoAltura(){
        Connection con;
        try {
            
            con = ConnectionUtil.getConnection();      
            ResultSet rs = con.createStatement().executeQuery("select id from alturas");
           
            if(rs.next()){
              while (rs.next()){
                int id = rs.getInt(1)+1;
                String l_id = Integer.toString(id);
                tbCodigoAltura.setText(l_id);
            }  
            }else{
                tbCodigoAltura.setText("1");
            }
            
        } catch (SQLException ex) {
            Logger.getLogger(DashboardController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void UpdateAlturas(){
        tcCodigoAltura.setCellValueFactory(new PropertyValueFactory<>("tcCodigoAltura"));       
        tcNombreAltura.setCellValueFactory(new PropertyValueFactory<>("tcNombreAltura"));
        tcAltura.setCellValueFactory(new PropertyValueFactory<>("tcAltura"));   
        
        listAlturas = ConnectionUtil.getAlturas();
        tvAlturas.setItems(listAlturas);        
    }
    
    public void AgregarAltura(){
        
        String nombre = tbNombreAltura.getText();        if (tbNombreAltura.getText().isEmpty()){nombre = "NULL";}
        String altura = tbAltura.getText();              if (tbAltura.getText().isEmpty()){altura = "NULL";}
        
        try{
        System.out.println("RECORD RUNNING INSIDE!!!");
        pst = con.prepareStatement("insert into alturas (nombre, altura) "
                + "values(?,?)");
        System.out.println("RECORD RUNNING AFTER");
        
        pst.setString(1, nombre);
        pst.setString(2, altura);
        int status = pst.executeUpdate();
        System.out.println("RECORD RUNNING POST QUERY");
        if (status==1){
            System.out.println("RECORD ADDED!!!");
            tbNombreAltura.clear();
            tbAltura.clear();
            UpdateAlturas();
            CodigoAltura();
            cbAltura.getItems().clear();
            fillComboBoxAltura();
            int id = Integer.parseInt(tbCodigoAltura.getText())+1;
            tbCodigoAltura.setText(String.valueOf(id));
        }else{
            System.out.println("RECORD FAILED!!!");
        }
            
        }catch (SQLException e){
            
        }
    
    }
    
    public void ModificarAltura(){
        
        String id = tbCodigoMaterialEditar2.getText();     if (tbCodigoMaterialEditar2.getText().isEmpty()){id = "NULL";}
        String nombre = tbNombreMaterialEditar2.getText(); if (tbNombreMaterialEditar2.getText().isEmpty()){nombre = "NULL";}
        String medida = tbMedidaMaterialEditar.getText();  if (tbMedidaMaterialEditar.getText().isEmpty()){medida = "NULL";}
        
        try{
        System.out.println("RECORD RUNNING INSIDE!!!");
        pst = con.prepareStatement("update alturas set nombre= ?, altura=? where id='"+id+"'");
        pst.setString(1, nombre);  
        pst.setString(2, medida);
        pst.execute();
        System.out.println("RECORD RUNNING AFTER"); 
        int status = pst.executeUpdate();
        System.out.println("RECORD RUNNING POST QUERY");
        if (status==1){
            System.out.println("RECORD ADDED!!!");
            UpdateAlturas();
            CodigoAltura();
            cbAltura.getItems().clear();
            fillComboBoxAltura();
        }else{
            System.out.println("RECORD FAILED!!!");
        }
        pst.close();    
        }catch (SQLException e){
            
        }
    
    }
    
    public class BusquedaCalibre{
        ResultSet rs = null;
        PreparedStatement ps = null;
        public ResultSet find (String s){
            try{
                ps = con.prepareStatement("select * from calibres where id = ?");
                ps.setString(1,s);
                rs = ps.executeQuery();
            } catch (Exception e){
                
            }
            return rs;
        }       
        
    }
    
    int indexCalibre;
    public void TableCalibre(){
        
        tvCalibres.setOnMouseClicked(new EventHandler<MouseEvent>(){
            public void handle(MouseEvent event) {
                Calibres index = tvCalibres.getItems().get(tvCalibres.getSelectionModel().getSelectedIndex());
                indexCalibre = index.getTcCodigoCalibre();
                String in = Integer.toString(indexCalibre);
                
                BusquedaCalibre busqueda = new BusquedaCalibre() {};
                ResultSet rs = null;
                rs = busqueda.find(in);
                System.out.println("Index seleccionado "+in);
                
                try{
                    if(rs.next()){
                        tbCodigoMaterialEditar2.setText(rs.getString("id"));
                        tbNombreMaterialEditar2.setText(rs.getString("nombre"));
                        tbMedidaMaterialEditar.setText(rs.getString("calibre"));
                    }else{
                     System.out.println("No hay informacion de domicilio ");
                }   
                }catch (Exception e){

                }
                
            }
        }
        );
        
    }
    
    public void EliminarCalibre(){
        String sql = "delete from calibres where id = ?";
        String in = Integer.toString(indexCalibre);
        try{
            pst = con.prepareStatement(sql);
            pst.setString(1, in);
            pst.execute();
            UpdateCalibres();
            CodigoCalibres();
            cbCalibre.getItems().clear();
            fillComboBoxCalibre();
        } catch (Exception e){
            
        }
        
    }
    
    public void CodigoCalibres(){
        Connection con;
        try {
            
            con = ConnectionUtil.getConnection();      
            ResultSet rs = con.createStatement().executeQuery("select id from calibres");
           
            if(rs.next()){
              while (rs.next()){
                int id = rs.getInt(1)+1;
                String l_id = Integer.toString(id);
                tbCodigoCalibre.setText(l_id);
            }  
            }else{
                tbCodigoCalibre.setText("1");
            }
            
        } catch (SQLException ex) {
            Logger.getLogger(DashboardController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void UpdateCalibres(){
        tcCodigoCalibre.setCellValueFactory(new PropertyValueFactory<>("tcCodigoCalibre"));       
        tcNombreCalibre.setCellValueFactory(new PropertyValueFactory<>("tcNombreCalibre"));
        tcCalibre.setCellValueFactory(new PropertyValueFactory<>("tcCalibre"));   
        
        listCalibre = ConnectionUtil.getCalibres();
        tvCalibres.setItems(listCalibre);        
    }
    
    public void AgregarCalibre(){
        
        String nombre = tbNombreCalibre.getText();        if (tbNombreCalibre.getText().isEmpty()){nombre = "NULL";}
        String calibre = tbCalibre.getText();             if (tbCalibre.getText().isEmpty()){calibre = "NULL";}
        
        try{
        System.out.println("RECORD RUNNING INSIDE!!!");
        pst = con.prepareStatement("insert into calibres (nombre, calibre) "
                + "values(?,?)");
        System.out.println("RECORD RUNNING AFTER");
        
        pst.setString(1, nombre);
        pst.setString(2, calibre);
        int status = pst.executeUpdate();
        System.out.println("RECORD RUNNING POST QUERY");
        if (status==1){
            System.out.println("RECORD ADDED!!!");
            tbNombreCalibre.clear();
            tbCalibre.clear();
            UpdateCalibres();
            CodigoCalibres();
            cbCalibre.getItems().clear();
            fillComboBoxCalibre();
            int id = Integer.parseInt(tbCodigoCalibre.getText())+1;
            tbCodigoCalibre.setText(String.valueOf(id));
        }else{
            System.out.println("RECORD FAILED!!!");
        }
            
        }catch (SQLException e){
            
        }
    
    }
    
    public void ModificarCalibre(){
        
        String id = tbCodigoMaterialEditar2.getText();     if (tbCodigoMaterialEditar2.getText().isEmpty()){id = "NULL";}
        String nombre = tbNombreMaterialEditar2.getText(); if (tbNombreMaterialEditar2.getText().isEmpty()){nombre = "NULL";}
        String medida = tbMedidaMaterialEditar.getText();  if (tbMedidaMaterialEditar.getText().isEmpty()){medida = "NULL";}
        
        try{
        System.out.println("RECORD RUNNING INSIDE!!!");
        pst = con.prepareStatement("update calibres set nombre= ?, calibre=? where id='"+id+"'");
        pst.setString(1, nombre);  
        pst.setString(2, medida);
        pst.execute();
        System.out.println("RECORD RUNNING AFTER"); 
        int status = pst.executeUpdate();
        System.out.println("RECORD RUNNING POST QUERY");
        if (status==1){
            System.out.println("RECORD ADDED!!!");
            UpdateCalibres();
            CodigoCalibres();
            cbCalibre.getItems().clear();
            fillComboBoxCalibre();
        }else{
            System.out.println("RECORD FAILED!!!");
        }
        pst.close();    
        }catch (SQLException e){
            
        }
    
    }
    
    public class BusquedaRombos{
        ResultSet rs = null;
        PreparedStatement ps = null;
        public ResultSet find (String s){
            try{
                ps = con.prepareStatement("select * from rombos where id = ?");
                ps.setString(1,s);
                rs = ps.executeQuery();
            } catch (Exception e){
                
            }
            return rs;
        }       
        
    }
    
    int indexRombo;
    public void TableRombos(){
        
        tvRombos.setOnMouseClicked(new EventHandler<MouseEvent>(){
            public void handle(MouseEvent event) {
                Rombos index = tvRombos.getItems().get(tvRombos.getSelectionModel().getSelectedIndex());
                indexRombo = index.getTcCodigoRombo();
                String in = Integer.toString(indexRombo);
                
                BusquedaRombos busqueda = new BusquedaRombos() {};
                ResultSet rs = null;
                rs = busqueda.find(in);
                System.out.println("Index seleccionado "+in);
                
                try{
                    if(rs.next()){
                        tbCodigoMaterialEditar2.setText(rs.getString("id"));
                        tbNombreMaterialEditar2.setText(rs.getString("nombre"));
                        tbMedidaMaterialEditar.setText(rs.getString("rombo"));
                    }else{
                     System.out.println("No hay informacion de domicilio ");
                }   
                }catch (Exception e){

                }
                
            }
        }
        );
        
    }

    public void TableValueRombo(){

        tvRombos.setOnMouseClicked(new EventHandler<MouseEvent>(){
            @Override
            public void handle(MouseEvent event) {
                Rombos index = tvRombos.getItems().get(tvRombos.getSelectionModel().getSelectedIndex());
                
                indexRombo = index.getTcCodigoRombo();     

            }
    });
    }
    
    public void EliminarRombo(){
        String sql = "delete from rombos where id = ?";
        String in = Integer.toString(indexRombo);
        try{
            pst = con.prepareStatement(sql);
            pst.setString(1, in);
            pst.execute();
            UpdateRombos();
            CodigoRombos();
            cbRombo.getItems().clear();
            fillComboBoxRombo();
            cbRombo.setItems(RomboOpcion);
        } catch (Exception e){
            
        }
        
    }
    
    public void CodigoRombos(){
        Connection con;
        try {
            
            con = ConnectionUtil.getConnection();      
            ResultSet rs = con.createStatement().executeQuery("select id from rombos");
           
            if(rs.next()){
              while (rs.next()){
                int id = rs.getInt(1)+1;
                String l_id = Integer.toString(id);
                tbCodigoRombo.setText(l_id);
            }  
            }else{
                tbCodigoRombo.setText("1");
            }
            
        } catch (SQLException ex) {
            Logger.getLogger(DashboardController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }   
    
    public void UpdateRombos(){
        tcCodigoRombo.setCellValueFactory(new PropertyValueFactory<>("tcCodigoRombo"));       
        tcNombreRombo.setCellValueFactory(new PropertyValueFactory<>("tcNombreRombo"));
        tcRombo.setCellValueFactory(new PropertyValueFactory<>("tcRombo"));   
        
        listRombo = ConnectionUtil.getRombos();
        tvRombos.setItems(listRombo);        
    }
    
    public void AgregarRombos(){
        
        String nombre = tbNombreRombo.getText();        if (tbNombreRombo.getText().isEmpty()){nombre = "NULL";}
        String rombo = tbRombo.getText();               if (tbRombo.getText().isEmpty()){rombo = "NULL";}
        
        try{
        System.out.println("RECORD RUNNING INSIDE!!!");
        pst = con.prepareStatement("insert into rombos (nombre, rombo) "
                + "values(?,?)");
        System.out.println("RECORD RUNNING AFTER");
        
        pst.setString(1, nombre);
        pst.setString(2, rombo);
        int status = pst.executeUpdate();
        System.out.println("RECORD RUNNING POST QUERY");
        if (status==1){
            System.out.println("RECORD ADDED!!!");
            tbNombreRombo.clear();
            tbRombo.clear();
            UpdateRombos();
            CodigoRombos();
            cbRombo.getItems().clear();
            fillComboBoxRombo();
            int id = Integer.parseInt(tbCodigoRombo.getText())+1;
            tbCodigoRombo.setText(String.valueOf(id));
        }else{
            System.out.println("RECORD FAILED!!!");
        }
            
        }catch (SQLException e){
            
        }
    
    }
    
    public void ModificarRombos(){
        
        String id = tbCodigoMaterialEditar2.getText();     if (tbCodigoMaterialEditar2.getText().isEmpty()){id = "NULL";}
        String nombre = tbNombreMaterialEditar2.getText(); if (tbNombreMaterialEditar2.getText().isEmpty()){nombre = "NULL";}
        String medida = tbMedidaMaterialEditar.getText();  if (tbMedidaMaterialEditar.getText().isEmpty()){medida = "NULL";}
        
        try{
        System.out.println("RECORD RUNNING INSIDE!!!");
        pst = con.prepareStatement("update rombos set nombre= ?, rombo=? where id='"+id+"'");
        pst.setString(1, nombre);  
        pst.setString(2, medida);
        pst.execute();
        System.out.println("RECORD RUNNING AFTER"); 
        int status = pst.executeUpdate();
        System.out.println("RECORD RUNNING POST QUERY");
        if (status==1){
            System.out.println("RECORD ADDED!!!");
            UpdateRombos();
            CodigoRombos();
            cbRombo.getItems().clear();
            fillComboBoxRombo();
        }else{
            System.out.println("RECORD FAILED!!!");
        }
        pst.close();    
        }catch (SQLException e){
            
        }
    
    }
    
    public void clearCambiarMaterial(){
        
        tbCodigoMaterialEditar2.clear();
        tbNombreMaterialEditar2.clear();
        tbMedidaMaterialEditar.clear();
        
    }

    // CONTROLLADOR DE PANTALLAS EN DASHBOARD
    @FXML
    private void handleClicks(ActionEvent actionEvent) throws IOException{
        
        if(actionEvent.getSource() == btnPefil){
            
            new animatefx.animation.BounceIn(lbTitulo).play();
            
            lbTitulo.setText("PERFIL");
            
            //new animatefx.animation.ZoomIn(pnPerfil).play();
            Perfil();
            pnBlanco.toFront();
            pnPerfil.toFront();
            
        }
                
        if(actionEvent.getSource() == btnInicio){
            
            new animatefx.animation.BounceIn(lbTitulo).play();
            
            lbTitulo.setText("INICIO");
            
            //new animatefx.animation.ZoomIn(pnInicio).play();
            
            pnBlanco.toFront();
            pnInicio.toFront();
            
        }
        if(actionEvent.getSource() == btnEmpleados){
            
            new animatefx.animation.BounceIn(lbTitulo).play();
            
            lbTitulo.setText("EMPLEADOS");

            //new animatefx.animation.ZoomIn(pnEmpleados).play();
            
            pnBlanco.toFront();
            pnEmpleados.toFront();
            
        }
        if(actionEvent.getSource() == btnProduccion){
            
            new animatefx.animation.BounceIn(lbTitulo).play();
            
            lbTitulo.setText("PRODUCCION");

            //new animatefx.animation.ZoomIn(pnProduccion).play();
            
            pnBlanco.toFront();
            pnProduccion.toFront();
            
        }
        if(actionEvent.getSource() == btnMateriales){
            
            new animatefx.animation.BounceIn(lbTitulo).play();
            
            lbTitulo.setText("MATERIALES");

            //new animatefx.animation.ZoomIn(pnMateriales).play();
            
            pnBlanco.toFront();
            pnMateriales.toFront();
            
        }
        if(actionEvent.getSource() == btnExit){
            
            Alert alert = new Alert(AlertType.CONFIRMATION, "Desea cerrar sesión?", ButtonType.YES, ButtonType.CANCEL);
            Button yesButton = (Button) alert.getDialogPane().lookupButton( ButtonType.YES );
            yesButton.setDefaultButton( false );
            Button noButton = (Button) alert.getDialogPane().lookupButton( ButtonType.CANCEL );
            noButton.setDefaultButton( true );
            alert.showAndWait();
            
            if (alert.getResult() == ButtonType.YES) {
                
            System.out.println( "Cerro sesión" );
            
            Parent root = FXMLLoader.load(getClass().getResource("/aceros/Login.fxml"));
        
            Node node = (Node) actionEvent.getSource();

            Stage stage = (Stage) node.getScene().getWindow();
        
            Scene scene = new Scene(root);

            scene.setFill(Color.TRANSPARENT);
        
            stage.setScene(scene);
        
            new animatefx.animation.ZoomIn(root).play();
        
            stage.show();
                
            }
            
        }
        
        // FIN DE CONTROLLADOR DE PANTALLAS EN DASHBOARD
        
        //PANTALLA PERFIL
        
        if(actionEvent.getSource() == btnPefil){
            
            new animatefx.animation.BounceIn(lbTitulo).play();
            
            lbTitulo.setText("PERFIL");
            
            //new animatefx.animation.ZoomIn(pnPerfil).play();
            Perfil();
            pnBlanco.toFront();
            pnPerfil.toFront();
            
        }
        
        if(actionEvent.getSource() == btncambiarContraseña){
            
            new animatefx.animation.BounceIn(lbTitulo).play();
            
            lbTitulo.setText("CAMBIAR CONTRASEÑA");
            pnDashboard.setDisable(true);
            pnBlanco.toFront();
            pnCambiarContraseña.toFront();
            
        }
        
        if(actionEvent.getSource() == btnVolverContraseña){
            
            new animatefx.animation.BounceIn(lbTitulo).play();
            
            lbTitulo.setText("PEFIL");
            pnDashboard.setDisable(false);
            
            Perfil();
            pnBlanco.toFront();
            pnPerfil.toFront();
            
        }
        
        if(actionEvent.getSource() == btnGuardarContraseña){
            
            String nueva,repetir;
            nueva = tbContraseñaNueva.getText();
            repetir = tbContraseñaRepetir.getText();
            System.out.println("NUEVA "+nueva);
            System.out.println("REPETIR "+repetir);
            
            if(tbContraseñaActual.getText().isEmpty()){
                Alert alert = new Alert(AlertType.ERROR);
                alert.setHeaderText("No se ingreso contraseña actual");
                alert.setContentText("La contraseña actual debe ser ingresada");
                Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
                stage.getIcons().add(new Image("icons/IconBlanco.png"));
                alert.showAndWait();
            }else if(tbContraseñaNueva.getText().isEmpty() || tbContraseñaRepetir.getText().isEmpty()){
                Alert alert = new Alert(AlertType.ERROR);
                alert.setHeaderText("Las contraseñas nuevas no coinciden");
                alert.setContentText("Vuelva a intentarlo de nuevo");
                Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
                stage.getIcons().add(new Image("icons/IconBlanco.png"));
                tbContraseñaActual.clear();
                tbContraseñaNueva.clear();
                tbContraseñaRepetir.clear();
                alert.showAndWait();
            }else if(tbContraseñaNueva.getText().equals(tbContraseñaRepetir.getText())){
                CambiarContraseña();
            }
            
        }
        
        //FIN PANTALLA PERFIL
        
        // PANTALLA EMPLEADOS
        
        if(actionEvent.getSource() == btnNuevoEmpleado){
            
            Alert alert = new Alert(AlertType.CONFIRMATION, "Desea agregar nuevo empleado?", ButtonType.YES, ButtonType.CANCEL);
            Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
            stage.getIcons().add(new Image("icons/IconBlanco.png"));
            alert.showAndWait();
            
            if (alert.getResult() == ButtonType.YES) {
            
            btnGuardarEmpleado.toFront();
            
            new animatefx.animation.BounceIn(lbTitulo).play();
            
            lbTitulo.setText("Agregar nuevo empleado");
            
            pnBlanco.toFront();
            pnAgregarEmpleados.toFront();
                
            }
            
        }
        
                
        if(actionEvent.getSource() == btnEditarEmpleado){
           PerfilEmpleado();
            new animatefx.animation.BounceIn(lbTitulo).play();
            
            lbTitulo.setText("Editar empleado");

            pnBlanco.toFront();
            pnAgregarEmpleados.toFront();
            
        } 
        
        if(actionEvent.getSource() == btnVolverEmpleados){
            
            Alert alert = new Alert(AlertType.CONFIRMATION, "Desea volver a la pantalla anterior?", ButtonType.YES, ButtonType.CANCEL);
            Button yesButton = (Button) alert.getDialogPane().lookupButton( ButtonType.YES );
            yesButton.setDefaultButton( false );
            Button noButton = (Button) alert.getDialogPane().lookupButton( ButtonType.CANCEL );
            noButton.setDefaultButton( true );
            Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
            stage.getIcons().add(new Image("icons/IconBlanco.png"));
            alert.showAndWait();
            
            if (alert.getResult() == ButtonType.YES) {
            
            new animatefx.animation.BounceIn(lbTitulo).play();
            
            lbTitulo.setText("EMPLEADOS");
            
            LimpiarPerfil();
            
            pnBlanco.toFront();
            pnEmpleados.toFront();
            
            }            
        }
        
        if(actionEvent.getSource() == btnGuardarEmpleado){
            
            Alert alert = new Alert(AlertType.CONFIRMATION, "Desea guardar empleado?", ButtonType.YES, ButtonType.CANCEL);
            Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
            stage.getIcons().add(new Image("icons/IconBlanco.png"));
            alert.showAndWait();
            
            if (alert.getResult() == ButtonType.YES) {
                System.out.println("CON IMAGEN");
                AgregarEmpleadoConImagen();
                
                /*if(file != null){
                    
                }else{
                    System.out.println("SIN IMAGEN");
                    AgregarEmpleado();
                }
                */
                
                
                new animatefx.animation.BounceIn(lbTitulo).play();
                lbTitulo.setText("EMPLEADOS");
                pnBlanco.toFront();
                pnEmpleados.toFront();
            }
            
        }
        
        if(actionEvent.getSource() == btnModificarEmpleado){
            
            Alert alert = new Alert(AlertType.CONFIRMATION, "Desea guardar cambios del empleado?", ButtonType.YES, ButtonType.CANCEL);
            Button yesButton = (Button) alert.getDialogPane().lookupButton( ButtonType.YES );
            yesButton.setDefaultButton( false );
            Button noButton = (Button) alert.getDialogPane().lookupButton( ButtonType.CANCEL );
            noButton.setDefaultButton( true );
            Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
            stage.getIcons().add(new Image("icons/IconBlanco.png"));
            alert.showAndWait();
            
            if (alert.getResult() == ButtonType.YES) {
                ModificarEmpleadoConImagen();
                
                /*
                if(file == null){
                    ModificarEmpleado();
                }else{
                   
                }
                */
                
                new animatefx.animation.BounceIn(lbTitulo).play();
                lbTitulo.setText("EMPLEADOS");
                pnBlanco.toFront();
                pnEmpleados.toFront();
            }
            
        }

        if(actionEvent.getSource() == btnEliminarEmpleado){
            
            Alert alert = new Alert(AlertType.CONFIRMATION, "Desea eliminar empleado?", ButtonType.YES, ButtonType.CANCEL);
            Button yesButton = (Button) alert.getDialogPane().lookupButton( ButtonType.YES );
            yesButton.setDefaultButton( false );
            Button noButton = (Button) alert.getDialogPane().lookupButton( ButtonType.CANCEL );
            noButton.setDefaultButton( true );
            Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
            stage.getIcons().add(new Image("icons/IconBlanco.png"));
            alert.showAndWait();
            
            if (alert.getResult() == ButtonType.YES) {
                EliminarEmpleado();
            }
            
        }
        
        if(actionEvent.getSource() == btnActualizarEmpleado){
            
            UpdateTable();
            
        }
        
        if(actionEvent.getSource() == btnProduccionEmpleado){
            BuscarEmpleadoConBotonProduccion();
            UpdateProduccionSemanal();
            new animatefx.animation.BounceIn(lbTitulo).play();
            
            lbTitulo.setText("PRODUCCION");
            
            pnBlanco.toFront();
            pnProduccion.toFront();
            
        }
        
        if(actionEvent.getSource() == btnSubirImagen){
            
            Node node = (Node) actionEvent.getSource();
            Stage stage = (Stage) node.getScene().getWindow();
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Subir imagen de perfil");
            fileChooser.getExtensionFilters().addAll(new ExtensionFilter("Imagenes", "*.png", " *.jpg"));
            file = fileChooser.showOpenDialog(stage);
            
            if(file !=null){
                lbPath.setText(file.getAbsolutePath());
                image = new Image(file.toURI().toString());
                System.out.println("ESTE ES EL ARCHIVO "+file.getAbsolutePath());
                System.out.println("ESTE ES EL ARCHIVO "+file.getPath());
                System.out.println("CONSEGUI DENTRO"+image);
                btnSubirImagen.setText(null);
                btnImagenPerfil.setImage(image);
                System.out.println("TERMINE DENTRO"+btnImagenPerfil.getImage()); 
                updateImagenPerfil();
            }else{
                file = new File("sin_perfil.png");
            }
            
        }
        
        // FIN DE PANTALLA EMPLEADOS
        
        // PANTALLA PRODUCCION
        
        if(actionEvent.getSource() == btnBuscarEmpleado){
            
            if(tbCodigoProduccion.getText().isEmpty()){
                Alert error = new Alert(AlertType.ERROR);
                    error.setTitle("Codigo de usuario vacio");
                    error.setHeaderText("Llenar los datos correctamente");
                    Stage stage = (Stage) error.getDialogPane().getScene().getWindow();
                    stage.getIcons().add(new Image("icons/IconBlanco.png"));
                    error.showAndWait();
                    tbCodigoProduccion.clear();           
            }else{
                BuscarEmpleadoProduccion();
                UpdateProduccionSemanal();
            }
            
        }
        
        if(actionEvent.getSource() == btnEditarEmpleado2){
           PerfilEmpleadoProduccion();
            new animatefx.animation.BounceIn(lbTitulo).play();
            
            lbTitulo.setText("Editar empleado");

            pnBlanco.toFront();
            pnAgregarEmpleados.toFront();
            
        } 
                
        if(actionEvent.getSource() == btnLimpiarProduccion){
                Alert alert = new Alert(AlertType.CONFIRMATION, "Desea limpiar los campos?", ButtonType.YES, ButtonType.CANCEL);
                Button yesButton = (Button) alert.getDialogPane().lookupButton( ButtonType.YES );
                yesButton.setDefaultButton( false );
                Button noButton = (Button) alert.getDialogPane().lookupButton( ButtonType.CANCEL );
                noButton.setDefaultButton( true );
                Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
                stage.getIcons().add(new Image("icons/IconBlanco.png"));
                alert.showAndWait();
                if (alert.getResult() == ButtonType.YES) {
                cleanProduccion();
            }
        }  
        
        if(actionEvent.getSource() == btnNuevoProduccion){

            if(tbMetros.getText().isEmpty() || tbCantidadProduccion.getText().isEmpty() || tbFechaRegistro.getValue() == null || cbMaterial.getValue() == null || cbCalibre.getValue() == null || cbAltura.getValue() == null || cbRombo.getValue() == null){
                Alert error = new Alert(AlertType.ERROR);
                    error.setHeaderText("Uno o mas campos vacios!");
                    Stage stage = (Stage) error.getDialogPane().getScene().getWindow();
                    stage.getIcons().add(new Image("icons/IconBlanco.png"));
                    error.showAndWait();
                    cleanProduccion();           
            }else if (lbHCodigo2.getText().isEmpty() || tbCodigoProduccion.getText().isEmpty()){
                Alert error = new Alert(AlertType.ERROR);
                    error.setHeaderText("No se ha seleccionado ningun empleado");
                    Stage stage = (Stage) error.getDialogPane().getScene().getWindow();
                    stage.getIcons().add(new Image("icons/IconBlanco.png"));
                    error.showAndWait();
                    cleanProduccion(); 
            }else{
                Alert alert = new Alert(AlertType.CONFIRMATION, "Desea guardar produccion?", ButtonType.YES, ButtonType.CANCEL);
                Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
                stage.getIcons().add(new Image("icons/IconBlanco.png"));
                alert.showAndWait();
                if (alert.getResult() == ButtonType.YES) {
                AgregarProduccion();
                UpdateProduccionSemanal();
                DiasSemana();
                }
            }
            
        }        
        
        if(actionEvent.getSource() == btnHistorial){
            
            new animatefx.animation.BounceIn(lbTitulo).play();
            
            lbTitulo.setText("HISTORIAL Y REPORTES");
            
            if(lbHCodigo2.getText().isEmpty()){
                
            }else{
                tbCodigoHistorial.setText(lbHCodigo2.getText());
                BuscarEmpleadoHistorial();
                UpdateHistorial();
            }
            
            pnBlanco.toFront();
            pnHistorial.toFront();
            
        }
        
        if(actionEvent.getSource() == btnVolverHistorial){
            
            new animatefx.animation.BounceIn(lbTitulo).play();
            
            lbTitulo.setText("PRODUCCION");
            
            pnBlanco.toFront();
            pnProduccion.toFront();
            
        }
        
        if(actionEvent.getSource() == btnVolverHistorial2){
            
            Alert alert = new Alert(AlertType.CONFIRMATION, "Desea volver a la pantalla anterior?", ButtonType.YES, ButtonType.CANCEL);
            Button yesButton = (Button) alert.getDialogPane().lookupButton( ButtonType.YES );
            yesButton.setDefaultButton( false );
            Button noButton = (Button) alert.getDialogPane().lookupButton( ButtonType.CANCEL );
            noButton.setDefaultButton( true );
            Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
            stage.getIcons().add(new Image("icons/IconBlanco.png"));
            alert.showAndWait();
            
            if (alert.getResult() == ButtonType.YES) {
            
            new animatefx.animation.BounceIn(lbTitulo).play();        
            
            lbTitulo.setText("PRODUCCION");
            
            LimpiarPerfil();
            
            pnBlanco.toFront();
            pnProduccion.toFront();
            
            }            
        }
        
        if(actionEvent.getSource() == btnVolverEditartProduccion){
            pnEditarProduccion.toBack();
        }
        
        if(actionEvent.getSource() == btnModificarProduccion){
            pnEditarProduccion.toFront();
        }
        
        if(actionEvent.getSource() == btnGuardarEditartProduccion){
            ModificarProduccion();
            UpdateProduccionSemanal();
            pnEditarProduccion.toBack();
        }
        
        if(actionEvent.getSource() == btnEditarHistorial){
            pnEditarProduccion.toFront();
        }
        
        // FIN PANTALLA PRODUCCION
        // PANTALLA HISTORIAL
        
        if(actionEvent.getSource() == btnBuscarHistorial){
            
            if(tbCodigoHistorial.getText().isEmpty()){
                Alert error = new Alert(AlertType.ERROR);
                    error.setTitle("Codigo de usuario vacio");
                    error.setHeaderText("Llenar los datos correctamente");
                    Stage stage = (Stage) error.getDialogPane().getScene().getWindow();
                    stage.getIcons().add(new Image("icons/IconBlanco.png"));
                    error.showAndWait();
                    tbCodigoHistorial.clear();           
            }else{
                BuscarEmpleadoHistorial();
                UpdateHistorial();
            }
            
        }
        
        if(actionEvent.getSource() == btnReporte){
            
            ImprimirReporte();
            
        }
        
        // FIN PANTALLA HISTORIAL
        // PANTALLA MATERIALES
        if(actionEvent.getSource() == btnGuardarMaterial){

            if(tbNombreMaterial.getText().isEmpty()){
                Alert error = new Alert(AlertType.ERROR);
                    //error.setTitle("Uno o mas campos vacios!");
                    error.setHeaderText("Uno o mas campos vacios!");
                    Stage stage = (Stage) error.getDialogPane().getScene().getWindow();
                    stage.getIcons().add(new Image("icons/IconBlanco.png"));
                    error.showAndWait();
            
            }else{
                Alert alert = new Alert(AlertType.CONFIRMATION, "Desea guardar material?", ButtonType.YES, ButtonType.CANCEL);
                Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
                stage.getIcons().add(new Image("icons/IconBlanco.png"));
                alert.showAndWait();
                if (alert.getResult() == ButtonType.YES) {
                AgregarMaterial();
                }
            }
            
        }
        
        if(actionEvent.getSource() == btnEliminarMaterial){
            
            Alert alert = new Alert(AlertType.CONFIRMATION, "Desea eliminar material?", ButtonType.YES, ButtonType.CANCEL);
            Button yesButton = (Button) alert.getDialogPane().lookupButton( ButtonType.YES );
            yesButton.setDefaultButton( false );
            Button noButton = (Button) alert.getDialogPane().lookupButton( ButtonType.CANCEL );
            noButton.setDefaultButton( true );
            Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
            stage.getIcons().add(new Image("icons/IconBlanco.png"));
            alert.showAndWait();
            
            if (alert.getResult() == ButtonType.YES) {
                EliminarMaterial();
            }
            
        }
        
        if(actionEvent.getSource() == btnGuardarAltura){

            if(tbNombreAltura.getText().isEmpty() || tbAltura.getText().isEmpty()){
                Alert error = new Alert(AlertType.ERROR);
                    error.setHeaderText("Uno o mas campos vacios!");
                    Stage stage = (Stage) error.getDialogPane().getScene().getWindow();
                    stage.getIcons().add(new Image("icons/IconBlanco.png"));
                    error.showAndWait();
                    tbNombreAltura.clear();
                    tbAltura.clear();
            }else{
                Alert alert = new Alert(AlertType.CONFIRMATION, "Desea guardar altura?", ButtonType.YES, ButtonType.CANCEL);
                Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
                stage.getIcons().add(new Image("icons/IconBlanco.png"));
                alert.showAndWait();
                if (alert.getResult() == ButtonType.YES) {
                AgregarAltura();
                }
            }
            
        }
        
        if(actionEvent.getSource() == btnEliminarAltura){
            
            Alert alert = new Alert(AlertType.CONFIRMATION, "Desea eliminar altura?", ButtonType.YES, ButtonType.CANCEL);
            Button yesButton = (Button) alert.getDialogPane().lookupButton( ButtonType.YES );
            yesButton.setDefaultButton( false );
            Button noButton = (Button) alert.getDialogPane().lookupButton( ButtonType.CANCEL );
            noButton.setDefaultButton( true );
            Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
            stage.getIcons().add(new Image("icons/IconBlanco.png"));
            alert.showAndWait();
            
            if (alert.getResult() == ButtonType.YES) {
                EliminarAltura();
            }
            
        }
        
        if(actionEvent.getSource() == btnGuardarCalibre){

            if(tbNombreCalibre.getText().isEmpty() || tbCalibre.getText().isEmpty()){
                Alert error = new Alert(AlertType.ERROR);
                    error.setHeaderText("Uno o mas campos vacios!");
                    Stage stage = (Stage) error.getDialogPane().getScene().getWindow();
                    stage.getIcons().add(new Image("icons/IconBlanco.png"));
                    error.showAndWait();
                    tbNombreCalibre.clear();
                    tbCalibre.clear();            
            }else{
                Alert alert = new Alert(AlertType.CONFIRMATION, "Desea guardar calibre?", ButtonType.YES, ButtonType.CANCEL);
                Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
                stage.getIcons().add(new Image("icons/IconBlanco.png"));
                alert.showAndWait();
                if (alert.getResult() == ButtonType.YES) {
                AgregarCalibre();
                }
            }
            
        }
        
        if(actionEvent.getSource() == btnEliminarCalibre){
            
            Alert alert = new Alert(AlertType.CONFIRMATION, "Desea eliminar calibre?", ButtonType.YES, ButtonType.CANCEL);
            Button yesButton = (Button) alert.getDialogPane().lookupButton( ButtonType.YES );
            yesButton.setDefaultButton( false );
            Button noButton = (Button) alert.getDialogPane().lookupButton( ButtonType.CANCEL );
            noButton.setDefaultButton( true );
            Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
            stage.getIcons().add(new Image("icons/IconBlanco.png"));
            alert.showAndWait();
            
            if (alert.getResult() == ButtonType.YES) {
                EliminarCalibre();
            }
            
        }
        
        if(actionEvent.getSource() == btnGuardarRombo){

            if(tbNombreRombo.getText().isEmpty() || tbRombo.getText().isEmpty()){
                Alert error = new Alert(AlertType.ERROR);
                    error.setHeaderText("Uno o mas campos vacios!");
                    Stage stage = (Stage) error.getDialogPane().getScene().getWindow();
                    stage.getIcons().add(new Image("icons/IconBlanco.png"));
                    error.showAndWait();
                    tbNombreRombo.clear();
                    tbRombo.clear();            
            }else{
                Alert alert = new Alert(AlertType.CONFIRMATION, "Desea guardar separacion de rombos?", ButtonType.YES, ButtonType.CANCEL);
                Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
                stage.getIcons().add(new Image("icons/IconBlanco.png"));
                alert.showAndWait();
                if (alert.getResult() == ButtonType.YES) {
                AgregarRombos();
                }
            }
            
        }
        
        if(actionEvent.getSource() == btnEliminarRombo){
            
            Alert alert = new Alert(AlertType.CONFIRMATION, "Desea eliminar rombos?", ButtonType.YES, ButtonType.CANCEL);
            Button yesButton = (Button) alert.getDialogPane().lookupButton( ButtonType.YES );
            yesButton.setDefaultButton( false );
            Button noButton = (Button) alert.getDialogPane().lookupButton( ButtonType.CANCEL );
            noButton.setDefaultButton( true );
            Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
            stage.getIcons().add(new Image("icons/IconBlanco.png"));
            alert.showAndWait();
            
            if (alert.getResult() == ButtonType.YES) {
                EliminarRombo();
            }
            
        }
        
        if(actionEvent.getSource() == btnEditarMaterial){
            pnEditarMaterial.toFront();
        }
        if(actionEvent.getSource() == btnVolverEdiatMaterial){
            pnEditarMaterial.toBack();
        }
        if(actionEvent.getSource() == btnGuardarEdiatMaterial){
            ModificarMaterial();
            pnEditarMaterial.toBack();
        }
        
        if(actionEvent.getSource() == btnVolverEditartMaterial2){
            clearCambiarMaterial();
            pnEditarMaterial2.toBack();
        }
        
        if(actionEvent.getSource() == btnEditarAltura){
            pnEditarMaterial2.toFront();
            btnGuardarEditarAltura.toFront();
            lbMedidaMaterialEditar.setText("ALTURA");
            tbMedidaMaterialEditar.setPromptText("ALTURA");
        }
        if(actionEvent.getSource() == btnGuardarEditarAltura){
            ModificarAltura();
            pnEditarMaterial2.toBack();
            clearCambiarMaterial();
        }
        
        if(actionEvent.getSource() == btnEditarCalibre){
            pnEditarMaterial2.toFront();
            btnGuardarEditarCalibre.toFront();
            lbMedidaMaterialEditar.setText("CALIBRE");
            tbMedidaMaterialEditar.setPromptText("CALIBRE");
        }
        if(actionEvent.getSource() == btnGuardarEditarCalibre){
            ModificarCalibre();
            pnEditarMaterial2.toBack();
            clearCambiarMaterial();
        }
        
        if(actionEvent.getSource() == btnEditarRombo){
            pnEditarMaterial2.toFront();
            btnGuardarEditarRombos.toFront();
            lbMedidaMaterialEditar.setText("SEPARACIÓN DE ROMBOS");
            tbMedidaMaterialEditar.setPromptText("SEPARACIÓN DE ROMBOS");
        }
        if(actionEvent.getSource() == btnGuardarEditarRombos){
            ModificarRombos();
            pnEditarMaterial2.toBack();
            clearCambiarMaterial();
        }
        
       // FIN DE PANTALLA MATERIALES 
    }
    
}
