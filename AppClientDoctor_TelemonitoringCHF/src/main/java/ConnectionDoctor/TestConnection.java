/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
/*package ConnectionDoctor;

import java.io.IOException;
import pojos.Doctor;

/**
 *
 * @author maria
 */
/*public class TestConnection {

    /**
     * @param args the command line arguments
     * @throws java.io.IOException
     */
    /*public static void main(String[] args) throws IOException {
        ConnectionDoctor.connectToServer("localhost");
        System.out.println("Connected succesfully");

        System.out.println("Test register");

        String encryptedPassword = Utilities.Encryption.encryptPasswordMD5("hello");
        Doctor doctor = new Doctor("50344565A", encryptedPassword, "Paula", "de Luna", 626201220, "paula@deluna.com");

        boolean registerSuccess = ConnectionDoctor.sendRegisterServer(doctor, encryptedPassword);
        if (registerSuccess == true) {
            System.out.println("Test Login");
            String encryptedPassword2 = Utilities.Encryption.encryptPasswordMD5("hello");

            boolean loginSuccess = ConnectionDoctor.validateLogin("50344565A", encryptedPassword2);
            System.out.println(loginSuccess);

            if (loginSuccess == false) {
                System.out.println("Invalid credentials");

            }
        } else {
            System.out.println("Not possible to register a doctor");
        }
        ConnectionDoctor.closeConnection();

    }
}*/
