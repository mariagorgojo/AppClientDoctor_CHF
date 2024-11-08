/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ConexionDoctor;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author martaguzman
 */
public class ConnectionDoctor {
   
    public static void main(String[] args) throws IOException {
        
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
    }

    private static void releaseResources(PrintWriter printWriter, Socket socket) {
        printWriter.close();
        
        try {
            socket.close();
        } catch (IOException ex) {
            Logger.getLogger(ConnectionDoctor.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}

    

