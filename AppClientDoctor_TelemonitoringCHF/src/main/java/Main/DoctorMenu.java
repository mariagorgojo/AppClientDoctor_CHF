package Main;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author martaguzman
 */

import java.util.Scanner;


public class DoctorMenu {

    private static Scanner scanner = new Scanner(System.in); // ns si es correcto??

    public static void main(String[] args) { // check entrada y salida
        LoginMenu();
    }

     private static void RegisterMenu() {
         
         
     }
    
    private static void LoginMenu() {
        while (true) {
            System.out.println("=== Doctor Menu ===");
            System.out.println("1. View My Data");
            System.out.println("2. View Patients");
            System.out.println("0. Exit");
            System.out.print("Choose an option: "); // runear 
            
            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline
            
            switch (choice) {
                case 1:
                    getDoctorById(); // cambiar xq solo va haber 1 doctor
                    break;
                case 2:
                    searchPatientsByDoctor();
                    break;
                case 0:
                    System.out.println("Exiting...");
                    return;
                default:
                    System.out.println("Invalid option. Try again.");
            }
        }
    }

    private static void getDoctorById() {
        System.out.println("Displaying doctor data...");
        // Implementa la lógica para obtener y mostrar los datos del doctor
    }

    private static void searchPatientsByDoctor() {
        while (true) {
            System.out.println("=== Patients Info ===");
            System.out.println("1. Select Patient By Id");
            System.out.println("0. Go Back");
            System.out.print("Choose an option: ");
            
            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline
            
            switch (choice) {
                case 1:
                    getPatientById(); // cambiarlo 
                    break;
                case 0:
                    return;
                default:
                    System.out.println("Invalid option. Try again.");
            }
        }
    }

    private static void getPatientById() {
        System.out.print("Enter patient ID: "); // le deberia de salir el listado de patients -> DO
        int patientId = scanner.nextInt();
        scanner.nextLine(); // Consume newline
        
        System.out.println("Displaying patient's information...");
        // Lógica para obtener y mostrar la información del paciente
        
        while (true) {
            System.out.println("=== Patient's Episodes ===");
            System.out.println("1. View Patient's Episodes");
            System.out.println("0. Exit");
            System.out.print("Choose an option: ");
            
            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline
            
            switch (choice) {
                case 1:
                    getEpisodesByPatient();
                    break;
                case 0:
                    return;
                default:
                    System.out.println("Invalid option. Try again.");
            }
        }
    }

    private static void getEpisodesByPatient() {
        System.out.println("Displaying patient's episodes...");
        // Lógica para obtener y mostrar episodios del paciente
        
        while (true) {
            System.out.println("=== Episodes ===");
            System.out.println("1. Select One Episode");
            System.out.println("0. Exit");
            System.out.print("Choose an option: ");
            
            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline
            
            switch (choice) {
                case 1:
                    getEpisodeByID();
                    break;
                case 0:
                    return;
                default:
                    System.out.println("Invalid option. Try again.");
            }
        }
    }

    private static void getEpisodeByID() {
        System.out.println("Displaying episode details...");
        // Lógica para mostrar detalles del episodio
        
        System.out.println("PRINT: surgeries, diseases, symptoms..."); // ACABR !!
        // Aquí llamas a getSurgeriesByEpisode, getDiseasesByEpisode, y getSymptomsByEpisode
        
        while (true) {
            System.out.println("=== Recordings Menu ===");
            System.out.println("1. See recordings");
            System.out.println("0. Exit");
            System.out.print("Choose an option: ");
            
            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline
            
            switch (choice) {
                case 1:
                    getRecordingsByEpisode();
                    break;
                case 0:
                    return;
                default:
                    System.out.println("Invalid option. Try again.");
            }
        }
    }

    private static void getRecordingsByEpisode() {
        System.out.println("Displaying recordings...");
        // Lógica para obtener y mostrar grabaciones del episodio
        
        while (true) {
            System.out.println("=== Select Recording Type ===");
            System.out.println("1. Select Recording");
            System.out.println("0. Exit");
            System.out.print("Choose an option: ");
            
            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline
            
            switch (choice) {
                case 1:
                    getRecordingByType();
                    break;
                case 0:
                    return;
                default:
                    System.out.println("Invalid option. Try again.");
            }
        }
    }

    private static void getRecordingByType() { // VOLVER
        System.out.println("Displaying selected recording...");
        // Lógica para obtener y mostrar la grabación por tipo
    }
}

    
    

