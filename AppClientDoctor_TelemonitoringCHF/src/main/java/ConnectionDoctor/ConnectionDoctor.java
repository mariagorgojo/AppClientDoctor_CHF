/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ConnectionDoctor;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import pojos.Doctor;

/**
 *
 * @author martaguzman
 */
public class ConnectionDoctor {
  
    private static Socket socket;
    private static PrintWriter printWriter;
    private static BufferedReader bufferedReader;

    // Método para establecer la conexión al servidor
    private static void connectToServer() throws IOException {
        if (socket == null || socket.isClosed()) {
            System.out.println("Connecting to server...");
            socket = new Socket("localhost", 9001); // LOCALHOST Y PORT CAMBIAR MÁS ADELANTE
            printWriter = new PrintWriter(socket.getOutputStream(), true);
            bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        }
    }

    // Método para cerrar la conexión al servidor
    private static void closeConnection() {
        try {
            if (printWriter != null) {
                printWriter.close();
            }
            if (bufferedReader != null) {
                bufferedReader.close();
            }
            if (socket != null && !socket.isClosed()) {
                socket.close();
            }
        } catch (IOException ex) {
            Logger.getLogger(ConnectionDoctor.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    // Método para registrar un doctor en el servidor
    public static void sendRegisterServer(Doctor doctor, String password) {
        try {
            connectToServer(); // Establecemos la conexión

            // Sending doctor data to the server
            System.out.println("Sending doctor registration information...");
            printWriter.println("REGISTER"); 
            printWriter.println(doctor.getDni());
            printWriter.println(password);  // Send the password securely in real application
            printWriter.println(doctor.getName());
            printWriter.println(doctor.getSurname());
            printWriter.println(doctor.getTelephone().toString());
            printWriter.println(doctor.getEmail());

            // Send "STOP" to indicate the end of data
            printWriter.println("STOP");

        } catch (IOException e) {
            Logger.getLogger(ConnectionDoctor.class.getName()).log(Level.SEVERE, null, e);
        } finally {
            closeConnection(); // Cerramos la conexión
        }
    }

    // Método para validar el login del doctor
    public static boolean validateLogin(String dni, String password) {
        try {
            connectToServer(); // Establecemos la conexión

            // Enviamos las credenciales para validación
            System.out.println("Sending login information...");
            
            printWriter.println("LOGIN");
            printWriter.println(dni);
            printWriter.println(password);
            printWriter.println("STOP");


            // Esperamos la respuesta del servidor
            String serverResponse = bufferedReader.readLine();

            // Si el servidor responde con "VALID", las credenciales son correctas 
            // IMPORTANTE CHECKEAR, Y EN ESTE ORDEN:
            //1. VALIDAR si el DNI corresponde con el de un doctor 
            //2.VALIDAR SI DNI de DOCTOR corresponde con la password
            if ("VALID".equals(serverResponse)) {
                System.out.println("Login successful!");
                return true;
            } else {
                System.out.println("Invalid credentials.");
                return false;
            }

        } catch (IOException e) {
            Logger.getLogger(ConnectionDoctor.class.getName()).log(Level.SEVERE, null, e);
            return false;
        } finally {
            closeConnection(); // Cerramos la conexión
        }
    }
}


   
   
        /* public static void main(String[] args) throws IOException {

                System.out.println("Starting Client...");
                Socket socket = new Socket("localhost", 9001);
                PrintWriter printWriter = new PrintWriter(socket.getOutputStream(), true);

                // Datos del doctor ejemplo -> ir pidiendo luego por teclado y poner 'STOP'
                String dni = "12345678X";
                String name = "Juan";
                String surname = "Pérez";
                Integer telephone = 123456789;
                String email = "juan.perez@example.com";

                System.out.println("Connection established... sending doctor information");

                // Enviar datos del doctor como strings
                printWriter.println(dni);
                printWriter.println(name);
                printWriter.println(surname);
                printWriter.println(telephone.toString());
                printWriter.println(email);

                // Enviar y pedir tb STOP  para q el server sepa cd se acaba
                System.out.println("Sending stop command");
                printWriter.println("STOP");

                releaseResources(printWriter, socket);
                System.exit(0);
            } */


    

