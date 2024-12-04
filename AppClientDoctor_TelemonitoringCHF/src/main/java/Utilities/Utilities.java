/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Utilities;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import pojos.Doctor;
import pojos.Patient;

public class Utilities {

    private static BufferedReader r = new BufferedReader(new InputStreamReader(System.in));

    // Read console inputs 
    public static int readInteger() {
        int num = 0;
        boolean ok = false;
        do {
            System.out.println("Introduce a number: ");

            try {
                num = Integer.parseInt(r.readLine());
                if (num < 0) {
                    ok = false;
                    System.out.println("You didn't type a valid number.");
                } else {
                    ok = true;
                }
            } catch (IOException e) {
                e.getMessage();
            } catch (NumberFormatException nfe) {
                System.out.println("You didn't type a valid number!");
            }
        } while (!ok);

        return num;
    }

    public static String readString() {
        String text = null;
        boolean ok = false;
        do {
            try {
                text = r.readLine();
                if (!text.isEmpty()) {
                    ok = true;
                } else {
                    System.out.println("Empty string, please try again:");
                }
            } catch (IOException e) {

            }
        } while (!ok);

        return text;
    }

    // Validates the format of the DNI
    public static boolean validateDNI(String id) {

        boolean ok = true;

        if (id.length() != 9) {
            // System.out.println("Invalid DNI, try again");
            ok = false;

            return ok;
        }

        for (int i = 0; i < 8; i++) {
            if (!Character.isDigit(id.charAt(i))) {
                ok = false;
                //System.out.println("Invalid DNI, try again");
                return ok;
            }
        }
        String num = id.substring(0, 8);

        String validLeters = "TRWAGMYFPDXBNJZSQVHLCKE";
        int indexLeter = Integer.parseInt(num) % 23;
        char valid = validLeters.charAt(indexLeter);

        if (id.toUpperCase().charAt(8) != valid) {
            //System.out.println("Invalid DNI, try again");
            ok = false;
            return ok;
        }

        return ok;
    }

    public static boolean validateEmail(String email) {

        String emailpattern = "^[A-Za-z0-9+_.-]+@(.+)$";

        // Check if the email matches the pattern
        return email != null && email.matches(emailpattern);
    }

    public static void showDoctorDetails(Doctor doctor) {

        System.out.println("\nDoctor Details:");
        System.out.println("DNI: " + doctor.getDni());
        System.out.println("Name: " + doctor.getName());
        System.out.println("Surname: " + doctor.getSurname());
        System.out.println("Telephone: " + doctor.getTelephone());
        System.out.println("Email: " + doctor.getEmail());
    }

    public static void showPatientDetails(Patient patient) {
        System.out.println(patient.toString());

    }

    public static void printPatientList(List<Patient> patients) {
        System.out.println("=== Patient List ===");
        for (int i = 0; i < patients.size(); i++) {
            Patient patient = patients.get(i);
            System.out.printf("%d. DNI: %s, Name: %s, Surname: %s%n",
                    i + 1, patient.getDNI(), patient.getName(), patient.getSurname());
        }
    }

// Method to display a menu and return the selected option
    public static int displayMenu(String title, String[] options) {
        System.out.println(title);
        for (int i = 0; i < options.length; i++) {
            System.out.printf("%d. %s\n", i + 1, options[i]);
        }
        System.out.println("Choose an option: ");
        return getValidInput(0, options.length );
    }

    // Method to display a list of objects with a menu
    public static <T> int displayListWithMenu(List<T> list, String title, String backOption) {
        System.out.println("\n" + title);
        for (int i = 0; i < list.size(); i++) {
            System.out.printf("%d. %s\n", i + 1, list.get(i).toString());
        }
        System.out.println("0. " + backOption);
        System.out.println("Choose an option: ");
        return getValidInput(0, list.size());
    }

    // Method to validate input within a range
    public static int getValidInput(int min, int max) {
        while (true) {
            try {
                String input = r.readLine(); // Read input as a string
                int choice = Integer.parseInt(input); // Convert to integer
                if (choice >= min && choice <= max) {
                    return choice;
                }
            } catch (IOException | NumberFormatException e) {
                // Ignore and prompt again
            }
            System.out.println("Invalid input. Please try again: ");
        }

    }
}
