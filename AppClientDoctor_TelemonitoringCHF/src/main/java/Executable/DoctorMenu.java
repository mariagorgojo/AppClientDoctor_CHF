package Executable;

import java.util.Scanner;
import Utilities.Utilities;
import pojos.Doctor;
import ConnectionDoctor.*;
import Swing.ReconstructionSignal;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import pojos.*;

public class DoctorMenu {

    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        try {
            mainMenu();
            //nuevo
        } finally {
            ConnectionDoctor.closeConnection(); // Cierra la conexión al finalizar
        }
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
            dni = dni.toUpperCase();

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
            dni = dni.toUpperCase();

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

        Doctor doctor = new Doctor(dni, password, name, surname, telephone, email);

        try {
            if (ConnectionDoctor.sendRegisterServer(doctor, password)) {
                System.out.println("User registered successfully with DNI: " + dni);
                mainMenu();
            } else {
                System.out.println("DNI : " + dni + " is already registered. Try to login to access your account.");
                mainMenu(); // redirigir
            }
        } catch (Exception e) {
            System.out.println("An unexpected error occurred.");
            mainMenu();
            //System.out.println(e);
        }

    }

    private static void doctorMenu(String doctorDni) throws IOException {
        int choice;
        do {
            choice = Utilities.displayMenu("\n=== Doctor Menu ===",
                    new String[]{"View my details", "View patients", "Log out"});

            switch (choice) {
                case 1: // VOLVER --> metodo de ConnectionDoctor es correcto? 
                    Doctor doctor = ConnectionDoctor.viewDoctorDetails(doctorDni); // pensar + base d dts --> devulve doctor + toString
                    Utilities.showDoctorDetails(doctor);
                    break;
                case 2:
                    viewPatientsMenu(doctorDni);
                    break;
                case 3:
                    System.out.println("Logging out...");
                    break;
                default:
                    System.out.println("Invalid option. Please try again.");
                    break;
            }
        } while (choice != 3);
    }

    private static void viewPatientsMenu(String doctorDni) throws IOException {
        List<Patient> patients = ConnectionDoctor.getPatientsByDoctor(doctorDni);
        //System.out.println("ha llegado al menu");

        if (patients.isEmpty()) {
            System.out.println("\nNo patients found.");
            return;
        }

        // Mostrar la lista de pacientes usando el método en Utilities
        Utilities.printPatientList(patients);

        // while (true) {
        System.out.println("\nSelect a patient by number (or 0 to go back):");
        int choice = Utilities.readInteger();

        if (choice == 0) {
            return;
        } else if (choice > 0 && choice <= patients.size()) {
            Patient selectedPatient = patients.get(choice - 1);
            // System.out.println(selectedPatient);
            Patient patient = ConnectionDoctor.viewPatientInformation(selectedPatient.getDNI());
            //System.out.println("después \n" +patient );

            Utilities.showPatientDetails(patient);
            // debería mandar todo (episodes)
            viewEpisodesByPatient(patient);
        } else {
            System.out.println("Invalid choice. Please try again.");
            // }
        }
    }

    /* HE CREADO OTRA DEBAJO
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
        }
    }*/
    private static void viewEpisodesByPatient(Patient patient) throws IOException {
        ArrayList<Episode> episodes = ConnectionDoctor.viewAllEpisodes(patient.getDNI());// = patient.getEpisodes();

        if (episodes.isEmpty()) {
            System.out.println("\nNo episodes found for this patient.");
            return;
        }

        // Mostrar episodios disponibles
        System.out.println("\n=== Patient Episodes ===");
        for (Episode episode : episodes) {
            System.out.println("ID: " + episode.getId() + ", Date: " + episode.getDate());
        }

        // Solicitar selección del episodio
        System.out.println("Enter the ID of the episode you want to view details for:");
        int option = Utilities.readInteger();

        Episode selectedEpisode = null;
        for (Episode episode : episodes) {
            if (episode.getId() == (option)) {
                selectedEpisode = episode;
                break; // Salir del bucle una vez que se encuentra el episode.
            }
        }
        selectedEpisode.setPatient_id(patient.getId());
        System.out.println("Episode selected:" + selectedEpisode);
        // Consultar detalles del episodio
        Episode episodeDetails = ConnectionDoctor.viewPatientEpisode(selectedEpisode.getId(), patient.getId());
        if (episodeDetails != null) {
            ArrayList<Surgery> surgeries = episodeDetails.getSurgeries();
            ArrayList<Symptom> symptoms = episodeDetails.getSymptoms();
            ArrayList<Disease> diseases = episodeDetails.getDiseases();
            ArrayList<Recording> recordings = episodeDetails.getRecordings();

            // Mostrar detalles del episodio
            if (!surgeries.isEmpty() || !symptoms.isEmpty() || !diseases.isEmpty() || !recordings.isEmpty()) {
                System.out.println("\n=== Episode Details ===");
                System.out.println("Surgeries: " + surgeries);
                System.out.println("Symptoms: " + symptoms);
                System.out.println("Diseases: " + diseases);
                System.out.println("Recordings: " + recordings);

                System.out.println("\nSelect an id to see a specific recording:\n");
                for (Recording rec : recordings) {
                    System.out.println("ID: " + rec.getId() + ", Path: " + rec.getSignal_path()); // Usa `filepath` si ya corregiste el atributo.
                }

                System.out.print("Introduce el ID del recording: ");
                int idBuscado = Utilities.readInteger();
                // Buscar el recording con ese id.-> metodo en recording
                Recording foundRecording = null;
                for (Recording rec : recordings) {
                    if (rec.getId() == idBuscado) {
                        foundRecording = rec;
                        break; // Salir del bucle una vez que se encuentra el recording.
                    }
                }
                System.out.println("foundRecording: " + foundRecording);
                ArrayList<Integer> data = foundRecording.getData();
                System.out.println("dt: " + data);
                // Si se encuentra el recording, mostrar los detalles.
                if (foundRecording != null) {
                    ReconstructionSignal.reconstructSignal(data);
                } else {
                    System.out.println("No se encontró un recording con ese ID.");
                }
            } else {
                System.out.println("There is nothing inserted in episode ID: " + selectedEpisode.getId());
            }
        } else {
            System.out.println("Episode details could not be retrieved.");
        }
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
