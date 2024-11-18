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

    private static void doctorMenu(String doctorDni ) { // VOLVER
        while (true) {
            System.out.println("\n=== Doctor Menu ===");
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

    private static void getDoctorById(String dni) { // VOLVER 
        System.out.println("\nDisplaying doctor details...");
          // VOLVER -> DO. 
       //HABRIA Q BUSCARLO EN LA BASE D DTS XQ LUEGO EN LOS THREADS VAMOS A TENER VARIOS PACIENTES 
      // y luego toString() ??
    }

    private static void viewPatientsMenu() {
        while (true) {
            System.out.println("\n=== Patient Information ===");
            System.out.println("1. Select patient to see info");
            System.out.println("0. Go back");
            System.out.print("Choose an option: ");
            
            int choice = scanner.nextInt();
            scanner.nextLine(); 

            switch (choice) {
                case 1:
                  
                    selectPatientById(); 
                    break;
                case 0:
                    return; 
                default:
                    System.out.println("Invalid option. Please try again.");
            }
        }
    }

    private static void selectPatientById() { // VOLVER creo q hay q cambiar --> chat 
        System.out.print("\nPlease select the number corresponding to the patient you wish to view from the list:"); 
         // DO -> metodo q imprima la lista d patients -OJO- lista de pacientes asociados a ese doctor para q pueda elegir
         // para ello: hacer una metodo q imprima paciente de ese doctor accediendo: ArrayList<Patient> patients d ese doctor.
        // deria de checkear la length de la lista de patients para poner excepcion si introduce un nº q no está en la lista de patients
        int patientId = scanner.nextInt(); 
        scanner.nextLine(); 

       // System.out.println("\nDisplaying patient information...");

        while (true) {
            System.out.println("\n=== Patient Episodes ===");
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
        System.out.println("\nDisplaying patient's episodes...");

        while (true) {
            System.out.println("\n=== Episodes ===");
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
            System.out.println("\n=== Recordings Menu ===");
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
        System.out.println("\nDisplaying episode recordings...");

        while (true) {
            System.out.println("\n=== Select Recording Type ===");
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
        System.out.println("\nDisplaying selected recording...");
        // Implementation to display recording by type
    }
}
