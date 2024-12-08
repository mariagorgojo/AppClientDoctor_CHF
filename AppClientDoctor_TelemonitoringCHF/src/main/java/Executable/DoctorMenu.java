package Executable;

import Utilities.Utilities;
import Utilities.Encryption;
import pojos.Doctor;
import ConnectionDoctor.*;
import Swing.ECGSignalReconstruction;
import Swing.EMGSignalReconstruction;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import pojos.*;
import pojos.Recording.Type;

public class DoctorMenu {


    public static void main(String[] args) {
        String ip_address_valid = null;
        try {

            ip_address_valid = Utilities.getValidIPAddress();
            try {
                ConnectionDoctor.connectToServer(ip_address_valid);
            } catch (IOException ex) {
                Logger.getLogger(DoctorMenu.class.getName()).log(Level.SEVERE, null, ex);
            }
            mainMenu();
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

            int choice = Utilities.readInteger();
            switch (choice) {
                case 1:
                    registerDoctor();
                    break;
                case 2:
                    loginMenu();
                    break;
                case 0:
                    ConnectionDoctor.closeConnection();
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
            System.out.println("\nEnter DNI to log in: ");
            dni = Utilities.readString();
            dni = dni.toUpperCase();

            if (!Utilities.validateDNI(dni)) {
                System.out.println("Invalid DNI. Please try again.");
            }
        } while (!Utilities.validateDNI(dni)); // Repite hasta que el formato sea correcto

        // Solicita la contraseña
        System.out.println("Enter password: ");
        password = Utilities.readString();
        String encryptedPassword = Encryption.encryptPasswordMD5(password);

        try {
            // Valida login
            if (ConnectionDoctor.validateLogin(dni, encryptedPassword)) {
                System.out.println("\nDoctor login successful!");
                // loginSuccess = true; 
                doctorMenu(dni); // Redirige al menú del doctor
            } else {
                System.out.println("ERROR. Make sure you entered your DNI and password correctly.");
                System.out.println("If you're not registered, please do it first. \n");
                mainMenu();
            }

        } catch (Exception e) {
            System.out.println("An unexpected error occurred. Please try again.");

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
            dni = Utilities.readString();
            dni = dni.toUpperCase();

            if (!Utilities.validateDNI(dni)) {
                System.out.println("Invalid DNI format. Please try again.");
            }
        } while (!Utilities.validateDNI(dni));

        System.out.println("Create password: ");
        String password = Utilities.readString();
        String encryptedPassword = Encryption.encryptPasswordMD5(password);

        System.out.println("First name: ");
        String name = Utilities.readString();

        System.out.println("Last name: ");
        String surname = Utilities.readString();

        System.out.println("Phone: ");
        Integer telephone = Utilities.readInteger();

        String email;
        do {
            System.out.println("Email: ");
            email = Utilities.readString();
            if (!Utilities.validateEmail(email)) {
                System.out.println("Invalid email. Please try again.");
            }
        } while (!Utilities.validateEmail(email));

        Doctor doctor = new Doctor(dni, encryptedPassword, name, surname, telephone, email);

        try {
            if (ConnectionDoctor.sendRegisterServer(doctor, encryptedPassword)) {
                System.out.println("User registered successfully with DNI: " + dni);
                loginMenu();
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
                    ConnectionDoctor.closeConnection();
                    System.out.println("Logging out...");
                    System.exit(0);
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
                //  System.out.println("Recordings: " + recordings);

                if (!recordings.isEmpty()) {
                    System.out.println("\nSelect an id to see a specific RECORDING:\n");
                    for (Recording rec : recordings) {
                        System.out.println("ID: " + rec.getId() + ", Path: " + rec.getSignal_path()); // Usa `filepath` si ya corregiste el atributo.
                    }

                    System.out.print("Introduce el ID del recording. ");
                    int idBuscado = Utilities.readInteger();
                    // Buscar el recording con ese id.-> metodo en recording
                    Recording foundRecording = null;
                    for (Recording rec : recordings) {
                        if (rec.getId() == idBuscado) {
                            foundRecording = rec;
                            break; // Salir del bucle una vez que se encuentra el recording.
                        }
                    }
                    ArrayList<Integer> data = foundRecording.getData();
                    // Si se encuentra el recording, mostrar los detalles.
                    if (foundRecording != null) {

                        Type signal_type = foundRecording.getType();
                        if (signal_type == Type.ECG) {
                            ECGSignalReconstruction.reconstructSignal(data);
                        } else {
                            EMGSignalReconstruction.reconstructSignal(data);
                        }

                    } else {
                        System.out.println("No se encontró un recording con ese ID.");
                    }
                }
                boolean res = true;
                do {
                    System.out.println("Do you want to give feedback about diseases or surgeries? Type yes/no");
                    String feedback = Utilities.readString().toUpperCase();
                    if (feedback.equals("YES")) {
                        insertEpisodeFeedback(selectedEpisode);
                    } else if (feedback.equals("NO")) {
                        System.out.println(" ");
                    } else {
                        System.out.println("not valid answer, type yes or no");
                        res = false;
                    }
                } while (res = false);
            } else {
                System.out.println("There is nothing inserted in episode ID: " + selectedEpisode.getId());
            }
        } else {
            System.out.println("Episode details could not be retrieved.");
        }
    }

    private static void insertEpisodeFeedback(Episode selectedEpisode) {
        List<String> diseases = selectDiseases();
        List<String> surgeries = selectSurgeries();

        boolean success = ConnectionDoctor.updateEpisode(selectedEpisode, diseases, surgeries);
        if (success) {
            System.out.println("Episode inserted successfully!");
            System.out.println("Diseases: " + diseases + "Surgeries: " + surgeries);
        } else {
            System.err.println("Failed to insert episode. Please try again.");
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

    private static List<String> selectDiseases() {

        List<Disease> availableDiseases = ConnectionDoctor.getAvailableDiseases();
        List<String> selectedDiseases = new ArrayList<>();
        int option;
        System.out.println("In base to these symptoms and recordings, select which disease you think the patient has. If not, skip to the next step ");
        System.out.println("=== Disease Selection ===");
        do {
            System.out.println("\nAvailable Diseases:");
            for (int i = 0; i < availableDiseases.size(); i++) {
                System.out.println((i + 1) + ". " + availableDiseases.get(i).getDisease());
            }
            System.out.println("\n\n");
            System.out.println((availableDiseases.size() + 1) + ". Add new Disease");
            System.out.println("\n\n");
            System.out.println((availableDiseases.size() + 2) + ". Skip to next step -> Patient needs Surgery ");

            option = Utilities.readInteger();

            if (option > 0 && option <= availableDiseases.size()) {
                String selectedDisease = availableDiseases.get(option - 1).getDisease();
                if (!selectedDiseases.contains(selectedDisease)) {
                    selectedDiseases.add(selectedDisease);
                    System.out.println("Disease \"" + selectedDisease + "\" added to your selection.");
                } else {
                    System.out.println("You already selected \"" + selectedDisease + "\".");
                }
            } else if (option == availableDiseases.size() + 1) {
                System.out.println("Enter new Disease: ");
                String newDisease = Utilities.readString();;
                selectedDiseases.add(newDisease);
                System.out.println("Disease \"" + newDisease + "\" added.");
            } else if (option != availableDiseases.size() + 2) {
                System.out.println("Invalid choice. Please try again.");
            }
        } while (option != availableDiseases.size() + 2);

        return selectedDiseases;
    }

    private static List<String> selectSurgeries() {
        List<Surgery> availableSurgeries = ConnectionDoctor.getAvailableSurgeries();
        List<String> selectedSurgeries = new ArrayList<>();
        int option;
        System.out.println("In base to these symptoms and recordings, select which surgeries you think the patient needs. If not, skip to the next step");
        System.out.println("=== Surgery Selection ===");
        do {
            System.out.println("\nAvailable Surgeries:");
            for (int i = 0; i < availableSurgeries.size(); i++) {
                System.out.println((i + 1) + ". " + availableSurgeries.get(i).getSurgery());
            }
            System.out.println("\n\n");
            System.out.println((availableSurgeries.size() + 1) + ". Add new Surgery");
            System.out.println("\n\n");
            System.out.println((availableSurgeries.size() + 2) + ". Go back to the Doctor Menu");

            option = Utilities.readInteger();

            if (option > 0 && option <= availableSurgeries.size()) {
                String selectedSurgery = availableSurgeries.get(option - 1).getSurgery();
                if (!selectedSurgeries.contains(selectedSurgery)) {
                    selectedSurgeries.add(selectedSurgery);
                    System.out.println("Surgery \"" + selectedSurgery + "\" added to your selection.");
                } else {
                    System.out.println("You already selected \"" + selectedSurgery + "\".");
                }
            } else if (option == availableSurgeries.size() + 1) {
                System.out.println("Enter new Surgery: ");
                String newSurgery = Utilities.readString();;
                selectedSurgeries.add(newSurgery);
                System.out.println("Surgery \"" + newSurgery + "\" added.");
            } else if (option != availableSurgeries.size() + 2) {
                System.out.println("Invalid choice. Please try again.");
            }
        } while (option != availableSurgeries.size() + 2);

        return selectedSurgeries;
    }
}
