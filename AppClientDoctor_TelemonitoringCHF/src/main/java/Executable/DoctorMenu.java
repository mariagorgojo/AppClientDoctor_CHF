package Executable;

import java.util.Scanner;
import Utilities.Utilities; 
import pojos.Doctor;
import ConnectionDoctor.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import pojos.*;

public class DoctorMenu {

    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        mainMenu();
    }

    private static void mainMenu() {
    System.out.println("\n-- Welcome to the Doctor App --");

    while (true) {
        System.out.println("1. Register");
        System.out.println("2. Log in");
        System.out.println("0. Exit");
        System.out.println("\nPlease select an option to get started: ");

        int choice = scanner.nextInt();
        scanner.nextLine(); 
        switch (choice) {
            case 1:
                registerDoctor();
                break;
            case 2:
                loginMenu();
                break;
            case 0:
                System.out.println("Exiting...");
                return;
            default:
                System.out.println("Invalid option. Please try again.");
        }
    }
}


   private static void loginMenu() {
    String dni;
    String password;
   // boolean loginSuccess = false;

    //do {
        // Solicita el DNI y valida el formato
        do {
            System.out.println("\nEnter DNI: ");
            dni = scanner.nextLine();
            if (!Utilities.validateDNI(dni)) {
                System.out.println("Invalid DNI. Please try again.");
            }
        } while (!Utilities.validateDNI(dni)); // Repite hasta que el formato sea correcto

        // Solicita la contraseña
        System.out.println("Enter password: ");
        password = scanner.nextLine();
    
        try {
            // Valida login
            if (ConnectionDoctor.validateLogin(dni, password)) { 
                System.out.println("\nDoctor login successful!");
               // loginSuccess = true; 
                doctorMenu(dni); // Redirige al menú del doctor
            } 
            
        } catch (Exception e) {
            System.out.println("ERROR. Make sure you entered your DNI and password correctly.");
            System.out.println("If you're not registered, please do it first. \n");
            //loginSuccess = true; 
            mainMenu();
            //System.out.println(e);

        }
    } //while (!loginSuccess); // Repite hasta que el login sea exitoso
//}


    private static void registerDoctor() {
        System.out.println("\nEnter doctor details to register:");

        String dni;
        do {
            System.out.println("\nEnter DNI: ");
            dni = scanner.nextLine();
            if (!Utilities.validateDNI(dni)) {
                System.out.println("Invalid DNI format. Please try again.");
            }
        } while (!Utilities.validateDNI(dni));

        System.out.println("Create password: ");
        String password = scanner.nextLine();

        System.out.println("First name: ");
        String name = scanner.nextLine();
        
        System.out.println("Last name: ");
        String surname = scanner.nextLine();
        
        System.out.println("Phone: ");
        Integer telephone = scanner.nextInt();
        scanner.nextLine();
        
        String email;
        do {
            System.out.println("Email: ");
            email = scanner.nextLine();
             if (!Utilities.validateEmail(email)) {
                System.out.println("Invalid email. Please try again.");
            }
        } while (!Utilities.validateEmail(email));
        
    
        Doctor doctor = new Doctor(dni, name, surname, telephone, email);
        
        try{
         if ( ConnectionDoctor.sendRegisterServer(doctor, password)) { 
            System.out.println("User registered successfully with DNI: " + dni);
            mainMenu();
        } else {
            System.out.println("DNI : " + dni + " is already registered. Try to login to access your account.");  
            mainMenu(); // redirigir
             }
        }catch (Exception e) {
            System.out.println("An unexpected error occurred.");
            mainMenu();
            //System.out.println(e);
        }

    }

   private static void doctorMenu(String doctorDni) throws IOException {
        while (true) {
            int choice = Utilities.displayMenu("\n=== Doctor Menu ===",
                    new String[]{"View my details", "View patients", "Log out"});

            switch (choice) {
                case 1: // VOLVER --> metodo de ConnectionDoctor es correcto? 
                    
                    Doctor doctor = ConnectionDoctor.viewDoctorDetails(doctorDni); // pensar + base d dts --> devulve doctor + toString
                    Utilities.showDoctorDetails(doctor);
                    break;
                case 2:
                    viewPatientsMenu(doctorDni);
                    break;
                case 0:
                    System.out.println("Logging out...");
                    return;
                default:
                    System.out.println("Invalid option. Please try again.");
            }
        }
    }

private static void viewPatientsMenu(String doctorDni) throws IOException {
    List<Patient> patients = ConnectionDoctor.getPatientsByDoctor(doctorDni);
    System.out.println("ha llegado al menu");
    if (patients.isEmpty()) {
        System.out.println("\nNo patients found.");
        return;
    }

    // Mostrar la lista de pacientes usando el método en Utilities
    Utilities.printPatientList(patients);

    while (true) {
        System.out.println("\nSelect a patient by number (or 0 to go back):");
        Scanner scanner = new Scanner(System.in);
        int choice;

        try {
            choice = Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("Invalid input. Please enter a number.");
            continue;
        }

        if (choice == 0) {
            return;
        } else if (choice > 0 && choice <= patients.size()) {
            Patient selectedPatient = patients.get(choice - 1);
           // System.out.println(selectedPatient);
            Patient patient = ConnectionDoctor.viewPatientInformation(selectedPatient.getDni());
                        //System.out.println("después \n" +patient );

            Utilities.showPatientDetails(patient);
            // debería mandar todo (episodes)
            viewEpisodesByPatient(selectedPatient);
        } else {
            System.out.println("Invalid choice. Please try again.");
        }
    }
}
    private static void viewEpisodesByPatient(Patient patient) {
        ArrayList<Episode> episodes = patient.getEpisodes();

        if (episodes.isEmpty()) {
            System.out.println("\nNo episodes found for this patient.");
            return;
        } else{

        System.out.println("Choose an episode to view more information");
        
        // Imprime la lista de episodios por fecha
        for (int i =0; i<episodes.size(); i++){
            System.out.println((i+1)+" Date: " +episodes.get(i).getDate());       
        }
        // elegir un episodio
        int option= Utilities.getValidInput(1, episodes.size());
        Episode selectedEpisode = episodes.get(option - 1);
        // ver todo lo que tiene un episodio 
        Episode episode = ConnectionDoctor.viewPatientEpisode(selectedEpisode.getId());
        System.out.println(episode.toString());
                
           
        }
                    
       /* while (true) {
            int choice = Utilities.displayListWithMenu(episodes,
                    "=== Episodes for " + patient.getName() + " ===", "Go back");

            if (choice == 0) {
                return;
            } else {
                Episode selectedEpisode = episodes.get(choice - 1);
                viewRecordingsByEpisode(selectedEpisode);
            }
        }*/
    }

    private static void viewRecordingsByEpisode(Episode episode) { // surgery, enfermedad, menus.drawio ... 
        List<Recording> recordings = episode.getRecordings();

        if (recordings.isEmpty()) {
            System.out.println("\nNo recordings found for this episode.");
            return;
        }

        while (true) {
            int choice = Utilities.displayListWithMenu(recordings, "=== Recordings ===", "Go back");

            if (choice == 0) {
                return;
            } else {
                Recording selectedRecording = recordings.get(choice - 1);
              //  System.out.println("\nSelected recording: " + selectedRecording.getDetails()); // como se ve la señal
            }
        }
    }
}
