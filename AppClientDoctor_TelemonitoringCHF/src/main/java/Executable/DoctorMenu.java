package Executable;

import java.util.Scanner;
import Utilities.Utilities; 
import pojos.Doctor;
import ConnectionDoctor.*;

public class DoctorMenu {

    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        mainMenu();
    }

    private static void mainMenu() {
        System.out.println("-- Welcome to the Doctor App --");
        while (true) {
            
            System.out.println("Please select an option to get started:");
            System.out.println("1. Register");
            System.out.println("2. Log in");
            System.out.println("0. Exit");
           
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
    boolean loginSuccess = false;

    do {
        System.out.print("Enter DNI: ");
        dni = scanner.nextLine();

        while (!Utilities.validateDNI(dni)) { // Valida el formato del DNI
            System.out.println("Invalid DNI format. Please try again.");
            System.out.print("Enter DNI: ");
            dni = scanner.nextLine();
        }

        System.out.print("Enter password: ");
        password = scanner.nextLine();
        
        // IMPORTANTE: VALID SI ESTÁ REGISTRADO, SI EL DNI ES DE DOCTOR Y SI COINCIDE LA PASSWORD CON DNI!!
        if (ConnectionDoctor.validateLogin(dni, password)) { 
            System.out.println("Doctor login successful!");
            loginSuccess = true; 
            doctorMenu(dni);
        } else {
            System.out.println("Something went wrong. Make sure to introduce your DNI and password as a Doctor.");
        }

    } while (!loginSuccess); // Repite hasta que el login sea exitoso
}

    private static void registerDoctor() {
        System.out.println("Enter doctor details to register:");

        String dni;
        do {
            System.out.print("DNI: ");
            dni = scanner.nextLine();
        } while (!Utilities.validateDNI(dni)); // verifico formato 

        System.out.print("Password: ");
        String password = scanner.nextLine();

        System.out.print("First name: ");
        String name = scanner.nextLine();
        System.out.print("Last name: ");
        String surname = scanner.nextLine();
        
        System.out.print("Phone: ");
        Integer telephone = scanner.nextInt();
        scanner.nextLine();
        
        String email;
        do {
            System.out.print("Email: ");
            email = scanner.nextLine();
        } while (!Utilities.validateEmail(email));
        
    
        Doctor doctor = new Doctor(dni, name, surname, telephone, email);
        
         if ( ConnectionDoctor.sendRegisterServer(doctor, password)) { 
            System.out.println("User registered successfully with DNI: " + dni);
            mainMenu();
        } else {
            System.out.println("DNI : " + dni + " is already registered. Try to login to access your account.");  
            mainMenu(); // redirigir
         }
         
    }

    private static void doctorMenu(String doctorDni ) { // VOLVER
        while (true) {
            System.out.println("=== Doctor Menu ===");
            System.out.println("1. View my details");
            System.out.println("2. View patients");
            System.out.println("0. Log out");
            System.out.print("Choose an option: ");
            
            int choice = scanner.nextInt();
            scanner.nextLine(); 

            switch (choice) {
                case 1:
                    getDoctorById(doctorDni); // toString or mandarDNI para devolver la info del doctor no???
                    break;
                case 2:
                    viewPatientsMenu();
                    break;
                case 0:
                    System.out.println("Logging out...");
                    return;
                default:
                    System.out.println("Invalid option. Please try again.");
            }
        }
    }

    private static void getDoctorById(String dni) { // VOLVER ??
        System.out.println("Displaying doctor details...");
          // VOLVER -> DO. 
       //HABRIA Q BUSCARLO EN LA BASE D DTS XQ LUEGO EN LOS THREADS VAMOS A TENER VARIOS PACIENTES 
      // O SINO toString() +VARAIBLE GLOBAL 
    }

    private static void viewPatientsMenu() {
        while (true) {
            System.out.println("=== Patient Information ===");
            System.out.println("1. Select patient");
            System.out.println("0. Go back");
            System.out.print("Choose an option: ");
            
            int choice = scanner.nextInt();
            scanner.nextLine(); 

            switch (choice) {
                case 1:
                    // DO -> metodo q imprima la lista d patients -OJO- lista de pacientes asociados a ese doctor para q pueda elegir
                    selectPatientById(); 
                    break;
                case 0:
                    return; 
                default:
                    System.out.println("Invalid option. Please try again.");
            }
        }
    }

    private static void selectPatientById() {
        System.out.print("Please select the number corresponding to the patient you wish to view from the list:"); 
        // deria de checkear la length de la lista de patients para poner excepcion si introduce un nº q no está en la lista de patients
        int patientId = scanner.nextInt(); 
        scanner.nextLine(); 

        System.out.println("Displaying patient information...");

        while (true) {
            System.out.println("=== Patient Episodes ===");
            System.out.println("1. View patient episodes");
            System.out.println("0. Exit");
            System.out.print("Choose an option: ");
            
            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    
                    viewEpisodesByPatient();
                    break;
                case 0:
                    return;
                default:
                    System.out.println("Invalid option. Please try again.");
            }
        }
    }

    private static void viewEpisodesByPatient() {
        System.out.println("Displaying patient's episodes...");

        while (true) {
            System.out.println("=== Episodes ===");
            System.out.println("1. Select an episode");
            System.out.println("0. Exit");
            System.out.print("Choose an option: ");
            
            int choice = scanner.nextInt();
            scanner.nextLine(); 

            switch (choice) {
                case 1:
                      // do -> metodo q imprima la lista episodes para q pueda saber cual quiere ver??
                    selectEpisodeById();
                    break;
                case 0:
                    return;
                default:
                    System.out.println("Invalid option. Please try again.");
            }
        }
    }

    private static void selectEpisodeById() { // No entiendo muy bien como lo queria hacer Carmen x defecto le enseña surgeries, diseases, symptoms??
        System.out.println("Displaying episode details...");
        System.out.println("Print: surgeries, diseases, symptoms..."); // DO

        while (true) {
            System.out.println("=== Recordings Menu ===");
            System.out.println("1. View recordings");
            System.out.println("0. Exit");
            System.out.print("Choose an option: ");
            
            int choice = scanner.nextInt();
            scanner.nextLine(); 

            switch (choice) {
                case 1:
                    viewRecordingsByEpisode();
                    break;
                case 0:
                    return;
                default:
                    System.out.println("Invalid option. Please try again.");
            }
        }
    }

    private static void viewRecordingsByEpisode() {
        System.out.println("Displaying episode recordings...");

        while (true) {
            System.out.println("=== Select Recording Type ===");
            System.out.println("1. Select recording");
            System.out.println("0. Exit");
            System.out.print("Choose an option: ");
            
            int choice = scanner.nextInt();
            scanner.nextLine(); 

            switch (choice) {
                case 1:
                    getRecordingByType();
                    break;
                case 0:
                    return;
                default:
                    System.out.println("Invalid option. Please try again.");
            }
        }
    }

    private static void getRecordingByType() { // VOLVER
        System.out.println("Displaying selected recording...");
        // Implementation to display recording by type
    }
}
