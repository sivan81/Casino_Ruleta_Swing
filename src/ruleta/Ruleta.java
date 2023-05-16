package ruleta;


import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.concurrent.ThreadLocalRandom;

public class Ruleta extends JFrame implements ActionListener {

    // enum para los estados
    private enum Estado {
        NOMBRE, INICIO, APUESTA, COLOR, JUGAR, SIN_DINERO, INGRESAR, REPETIR;

        // Va comprobando la secuencia de los estados
        public Estado siguiente() {
            if (this.ordinal() < Estado.values().length - 1) {
                return Estado.values()[this.ordinal() + 1];
            } else {
                return null;
            }
        }

    }

    // enum para los colores de las apuestas
    private enum Color {
        NEGRO, ROJO, VERDE
    }

    // Inicio de variables/atributos
    private int apuesta = 0;
    private Color color = Color.NEGRO;

    private JTextField inputData;
    private JLabel infoAUsuario;
    private JLabel consultaAdminLabel;
    private JPasswordField consultaAdminTextFieldPass;
    private JButton consultaAdminButton;
    private JButton versionButton;
    private JLabel titulo;
    private JLabel mayorGananciaLabel;
    private String nombreJugador;
    private JButton botonContinuar;
    private JButton botonNoContinuar;
    private static JLabel textoError;
    private Estado estadoActual;
    private int saldo = 1000;
    private int mayorGanancia = 1000;
    public int porcentajeResultante=0;
    public boolean beneficioPerdida=false;
    public boolean pulsarBotonVersion=false;
    public String respuestaPorcentaje = "";

    static File file = new File("DatosJugadores.txt");

    // Ruleta
    public Ruleta() {
        setLayout(null);

        estadoActual = Estado.NOMBRE;

        crearTitulo();
        crearInfoUsuario();
        crearConsultaAdmin();
        crearInputData();
        crearBoton();
        crearBoton2();
        crearError();
        crearMayorGanancia();
        inicializarJFrame();
        versionBoton();
    }

    // inicia JFrame
    private void inicializarJFrame() {
        this.setBounds(0, 0, 550, 250);
        this.setVisible(true);
        this.setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        if (pulsarBotonVersion == true)
        {
            this.setBounds(0, 0, 550, 350);
            this.setVisible(true);
            this.setLocationRelativeTo(null);
            setDefaultCloseOperation(EXIT_ON_CLOSE);
        }
    }

    // método en caso de meter datos incorrectos en las apuestas
    private void crearError() {
        textoError = new JLabel("Formato de datos incorrectos, por favor introduzca un valor numérico entero");
        textoError.setBounds(15, 25, 540, 30);
        textoError.setForeground(java.awt.Color.RED);
        textoError.setVisible(false);
        add(textoError);
    }

    // Método para pedir el nombre del jugador
    private void crearInfoUsuario() {
        infoAUsuario = new JLabel("Indique su nombre");
        infoAUsuario.setBounds(15, 50, 300, 30);
        infoAUsuario.setVisible(true);
        add(infoAUsuario);

    }

    //Método para crear una zona donde el admin del casino consulta los datos de los clientes
    private void crearConsultaAdmin()
    {
        //Label de la opción consultar historial
        consultaAdminLabel= new JLabel("Consultar información (solo Admin), introduzca contraseña");
        consultaAdminLabel.setBounds(10, 250, 350, 30);
        consultaAdminLabel.setVisible(true);
        add(consultaAdminLabel);

        //Campo de texto de la pass para consultar historial
        consultaAdminTextFieldPass = new JPasswordField(); //JPasswordField hace que se pongan asteriscos en el campo de texto (para las pass)
        consultaAdminTextFieldPass.setBounds(10,280,200,20);
        consultaAdminTextFieldPass.setVisible(true);
        add(consultaAdminTextFieldPass);

        //Boton de consulta del historial
        consultaAdminButton=new JButton("Consultar");
        consultaAdminButton.setBounds(225,275,90,25);
        consultaAdminButton.setVisible(true);
        add(consultaAdminButton);
        consultaAdminButton.addActionListener(this);
    }

    // Label de mayor ganancia
    private void crearMayorGanancia() {
        mayorGananciaLabel = new JLabel("Lo máximo que ha tenido hasta ahora ha sido de " + mayorGanancia + " €");
        mayorGananciaLabel.setBounds(180, 160, 400, 30);
        mayorGananciaLabel.setVisible(true);
        add(mayorGananciaLabel);
    }

    // Método de inicio de partida
    private void crearTitulo() {
        titulo = new JLabel("Empecemos a jugar!! Su saldo de inicio es de " + saldo + " €");
        titulo.setBounds(15, 0, 550, 30);
        add(titulo);
    }

    // Método para craer el cuadro de texto
    private void crearInputData() {
        inputData = new JTextField();
        inputData.setBounds(15, 80, 190, 20);
        inputData.setVisible(true);
        add(inputData);
        inputData.addActionListener(this);

    }

    // Método para crear el botón continuar
    private void crearBoton() {
        botonContinuar = new JButton("Continuar");
        botonContinuar.setBounds(10, 120, 150, 30);
        add(botonContinuar);
        botonContinuar.addActionListener(this);
    }

    // Método crear botón No continuar
    private void crearBoton2() {
        botonNoContinuar = new JButton("No Continuar");
        botonNoContinuar.setBounds(180, 120, 150, 30);
        botonNoContinuar.setVisible(false);
        add(botonNoContinuar);
        botonNoContinuar.addActionListener(this);
    }

    //Método creacion del boton Version
    private void versionBoton()
    {
        versionButton = new JButton("V1.1");
        versionButton.setBounds(475, 1, 60, 20);
        versionButton.setVisible(true);
        add(versionButton);
        versionButton.addActionListener(this);
    }

    // Acciones del botón continuar y otros botones
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == botonNoContinuar) {
            PorcentajeFinal();
            CrearFichero();
            LeerFichero();
            botonContinuar.setVisible(false);
            botonNoContinuar.setVisible(false);
            String cadena = "Gracias por participar " + nombreJugador + " el total conseguido ha sido de " + saldo
                    + " €, y lo máximo que ha llegado a tener durante el juego ha sido de " + mayorGanancia + " €";
            JOptionPane.showMessageDialog(null, cadena);
        }
        if (e.getSource() == botonContinuar || e.getSource() == inputData) {
            if (recogerDatos()) {// Solo pasaremos a la siguiente si el recoger datos ha ido bien
                ocultarError();// por si en un paso previo lo habíamos mostrado
                estadoActual = estadoActual.siguiente();
                botonNoContinuar.setVisible(false);

                switch (estadoActual) {
                    case APUESTA:
                        apostar();
                        break;
                    case COLOR:
                        elegirColor();
                        break;
                    case JUGAR:
                        jugar();
                        break;
                    case INGRESAR:
                        ingresarMasDinero();
                        break;
                    case REPETIR:
                        guardaIngresoYVuelveAJugar();
                        break;
                }
            } else {
                mostrarError();
            }
        }
        if(e.getSource() == consultaAdminButton) //Boton consulta historial (acciones)
        {
            String consultarPass= consultaAdminTextFieldPass.getText();

            if(consultarPass.equals("1234"))
            {
                String cadena = "Estimado Admin, ya tiene la información que necesita en su sistema privado";
                JOptionPane.showMessageDialog(null, cadena);
                consultaAdminTextFieldPass.setText("");
                LeerFichero();
            }else {
                String cadena = "No ha escrito la contraseña correcta para obtener la información";
                JOptionPane.showMessageDialog(null, cadena);
                consultaAdminTextFieldPass.setText("");
            }
        }
        if(e.getSource() == versionButton) //Este botón muestras las opciones administrador
        {
            if(pulsarBotonVersion==false)
            {
                pulsarBotonVersion=true;
                inicializarJFrame();
            }else {
                pulsarBotonVersion=false;
                inicializarJFrame();
            }

        }
    }

    // Nuevo método para recoger los datos y controlar aquí posibles errores
    // devolverá false si algo falla, y true si va bien
    private boolean recogerDatos() {
        boolean datosOk = true;
        inputData.setVisible(true);

        try {
            if (estadoActual.equals(Estado.NOMBRE)) {
                nombreJugador = inputData.getText();
                this.setTitle(inputData.getText());
                estadoActual = Estado.INICIO;
            } else if (estadoActual.equals(Estado.APUESTA)) {
                apuesta = Integer.parseInt(inputData.getText());
                if (apuesta > saldo) {
                    String cadena = "Disculple " + nombreJugador
                            + ", al ser su apuesta superior a su saldo, la igualaremos al límite del mismo.";
                    JOptionPane.showMessageDialog(null, cadena);
                    apuesta = saldo;
                }
            } else if (estadoActual.equals(Estado.COLOR)) {
                color = Color.values()[Integer.parseInt(inputData.getText())];
                inputData.setVisible(false);
            } else if (estadoActual.equals(Estado.INGRESAR)) {
                saldo = saldo + Integer.parseInt(inputData.getText());
            }
        } catch (NumberFormatException excepcion) {
            // Cuando ocurre el error devolvemos false para que no continue al siguiente
            // paso
            datosOk = false;
        }
        inputData.setText("");

        return datosOk;
    }

    private void mostrarError() {
        textoError.setVisible(true);
    }

    private void ocultarError() {
        textoError.setVisible(false);
    }

    private void apostar() {
        titulo.setText("Su saldo actual es de " + saldo + " €");
        infoAUsuario.setText("Introduzca su apuesta");
    }

    private void elegirColor() {
        infoAUsuario.setText("Elija un color: 0 negro, 1 rojo, 2 verde");
    }

    // Método para iniciar el juego
    private void jugar() {
        inputData.setVisible(false);

        int resultado = girarRuleta();

        if (resultado == color.ordinal()) {
            ganarApuesta();
            maximoConseguido();
        } else {
            perderApuesta();
            maximoConseguido();
        }

        if (todaviaTieneDinero()) {

            estadoActual = Estado.INICIO;

        } else {
            estadoActual = Estado.SIN_DINERO;
        }

    }

    // Método para cuando pierdes la apuesta
    private void perderApuesta() {
        saldo = saldo - apuesta;
        titulo.setText("Como ha perdido, su saldo actual es de: " + saldo + " €" + " ¿desea continuar jugando?");
        botonNoContinuar.setVisible(true);
        if (saldo > mayorGanancia) {
            mayorGanancia = saldo;
        }
    }

    // Indica en todo momento el máximo conseguido
    private void maximoConseguido() {
        mayorGananciaLabel.setText("Lo máximo que ha tenido hasta ahora ha sido de " + mayorGanancia + " €");
    }

    // Método para cuando ganas la apuesta
    private void ganarApuesta() {
        saldo = saldo + apuesta;
        titulo.setText("Como ha ganado, su saldo actual es de: " + saldo + " €" + " ¿desea continuar jugando?");
        botonNoContinuar.setVisible(true);
        if (saldo > mayorGanancia) {
            mayorGanancia = saldo;

        }
    }

    // Método que indica el resultado obtenido en el giro de la ruleta
    private int girarRuleta() {
        int randomNum = ThreadLocalRandom.current().nextInt(0, 3);

        if (randomNum == Color.ROJO.ordinal()) {
            infoAUsuario.setText("Ha salido el rojo");
        }

        if (randomNum == Color.NEGRO.ordinal()) {
            infoAUsuario.setText("Ha salido el negro");
        }

        if (randomNum == Color.VERDE.ordinal()) {
            infoAUsuario.setText("Ha salido el verde");
        }
        return randomNum;
    }

    // Método para ingresar más dinero si se ha quedado sin nada.
    private void ingresarMasDinero() {
        titulo.setText("Su saldo actual es de " + saldo + " €, haga un ingreso para continuar");
        infoAUsuario.setText("Indique la cantidad a ingresar");
        inputData.setVisible(true);
    }

    // Método para guardar ingreso para volver a jugar.
    private void guardaIngresoYVuelveAJugar() {
        titulo.setText("Su saldo actual es de " + saldo + " €");
        estadoActual = Estado.APUESTA;
        apostar();
    }

    // Método que comprueba si sigue teniendo salgo para poder seguir jugando sin
    // ingresar
    private boolean todaviaTieneDinero() {
        return saldo > 0;
    }

    //Funcion para calcular el porcentaje de ganacia o perdida
    public void PorcentajeFinal()
    {
        int cuenta=0;

        cuenta = (saldo * 100) / 1000;
        porcentajeResultante=cuenta - 100;

        if(porcentajeResultante<0)
        {
            beneficioPerdida=false;
            respuestaPorcentaje= "pérdida";
        }else {
            beneficioPerdida=true;
            respuestaPorcentaje= "ganancia";
        }
    }

    // Funcion para CrearFichero y todo lo relacionado con el guardar informacion
    public void CrearFichero() {
        FileWriter fichero = null;
        PrintWriter pw = null;
        try {
            //Creacion del fichero
            if (!file.exists()) {
                file.createNewFile();
            }
            fichero = new FileWriter(file.getAbsoluteFile(), true);
            pw = new PrintWriter(fichero);

            pw.println("El Jugador " + nombreJugador + " consiguió un saldo final de " + saldo + "€, con un porcentaje de " + respuestaPorcentaje + " del " + porcentajeResultante + "%");

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (null != fichero)
                    fichero.close();
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
    }


    //Funcion para leer fichero
    public void LeerFichero()
    {
        FileReader fr= null;
        BufferedReader br=null;

        try {
            //Abertura del fichero
            fr = new FileReader(file);
            br=new BufferedReader (fr);
            String linea;
            while ((linea=br.readLine())!=null) {
                System.out.println(linea);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            try {
                if(null != fr) {
                    fr.close();
                }
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
    }
}

