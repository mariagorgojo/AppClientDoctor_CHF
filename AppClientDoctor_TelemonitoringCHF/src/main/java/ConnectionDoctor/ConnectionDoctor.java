/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ConnectionDoctor;
import Utilities.Utilities;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import pojos.Disease;
import pojos.Doctor;
import pojos.Patient;
import pojos.Patient.Gender;
import pojos.Surgery;
import pojos.Symptom;

/**
 *
 * @author martaguzman
 */
public class ConnectionDoctor {
  
    private static Socket socket;
    private static PrintWriter printWriter;
    private static BufferedReader bufferedReader;

    private static void connectToServer() throws IOException {
        if (socket == null || socket.isClosed()) {
            System.out.println("Connecting to server...");
            socket = new Socket("localhost", 9001); // cambiar mas adelante 
            printWriter = new PrintWriter(socket.getOutputStream(), true);
            bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        }
    }

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

    public static boolean sendRegisterServer(Doctor doctor, String password) {
        try {
            connectToServer();
            printWriter.println("REGISTER_DOCTOR");
            printWriter.println(doctor.getDni());
            printWriter.println(password);
            printWriter.println(doctor.getName());
            printWriter.println(doctor.getSurname());
            printWriter.println(doctor.getTelephone().toString());
            printWriter.println(doctor.getEmail());

            String serverResponse = bufferedReader.readLine();
            if ("VALID".equals(serverResponse)) {
                return true;
            } else {
                return false;
            }
        } catch (IOException e) {
            Logger.getLogger(ConnectionDoctor.class.getName()).log(Level.SEVERE, null, e);
            return false;
        } finally {
           // printWriter.println("STOP");
            closeConnection(); // correct?
        }
    }


    public static boolean validateLogin(String dni, String password) {
        try {
            connectToServer();
            printWriter.println("LOGIN_DOCTOR");
            printWriter.println(dni);
            printWriter.println(password);
            

            String serverResponse = bufferedReader.readLine();
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
            //printWriter.println("STOP"); // TO DO VER SI QUITARLO O NO 
            closeConnection();
        }
    }

    public static void viewDoctorDetails(String doctorDni) {
        try {
            connectToServer();
            printWriter.println("VIEW_DOCTOR_DETAILS");
            printWriter.println(doctorDni); 
            
            String doctorString = bufferedReader.readLine(); 

            String[] parts = doctorString.split(","); 
  
            if (parts.length == 5) {
                Doctor doctor = new Doctor();
                doctor.setDni(parts[0]);
                doctor.setName(parts[1]);
                doctor.setSurname(parts[2]);
                doctor.setTelephone(Integer.parseInt(parts[3]));
                doctor.setEmail(parts[4]);
                               
                Utilities.showDoctorDetails(doctor); 
            } else {
                System.out.println("Invalid data format received from server.");
            }
        } catch (IOException e) {
            Logger.getLogger(ConnectionDoctor.class.getName()).log(Level.SEVERE, null, e);
        } finally {
           // printWriter.println("STOP");
            closeConnection(); // correct?
        }
    }

    public static List<Patient> getPatientsByDoctor(String doctorDni) {

        List<Patient> patients = new ArrayList<>();
            try {
                connectToServer();
                printWriter.println("VIEW_DOCTOR_PATIENTS"); 
                printWriter.println(doctorDni); 

                String patientString;
                while (!(patientString = bufferedReader.readLine()).equals("END_OF_LIST")) {
                    String[] parts = patientString.split(",");

                    Patient patient = new Patient();
                    patient.setDni(parts[0]);
                    patient.setName(parts[1]);
                    patient.setSurname(parts[2]);
                    patients.add(patient);
                }
            } catch (IOException e) {
                Logger.getLogger(Doctor.class.getName()).log(Level.SEVERE, null, e);
            } finally {
                closeConnection(); // Cerrar la conexión al servidor
            }
        return patients; // Retornar la lista de pacientes

    }

    public static void viewPatientInformation(String dni) {
    List<Surgery> surgeries = new ArrayList<>();
    List<Symptom> symptoms = new ArrayList<>();
    List<Disease> diseases = new ArrayList<>();
    Patient patient = null;
    
    try {
        connectToServer();
        printWriter.println("VIEW_PATIENT_INFORMATION");
        printWriter.println(dni);

        String dataString;
        while (!(dataString = bufferedReader.readLine()).equals("END_OF_LIST")) {
            String[] parts = dataString.split(":");

            if (parts.length == 2) {
                String type = parts[0];
                String data = parts[1]; // Tipo de dato: SURGERY, SYMPTOM, DISEASE

                switch (type) {
                     case "PATIENT_INFO":
                        String[] patientParts = data.split(",");
                        patient = new Patient();
                        patient.setDni(patientParts[0]);
                        patient.setName(patientParts[1]);
                        patient.setSurname(patientParts[2]);
                        patient.setEmail(patientParts[3]);
                        patient.setGender(Gender.valueOf(patientParts[4].toUpperCase()));
                        patient.setPhoneNumber(Integer.parseInt(patientParts[5])); // Convertir a entero
                        patient.setDob(LocalDate.parse(patientParts[6], DateTimeFormatter.ofPattern("yyyy-MM-dd")));

                        String[] doctorParts = patientParts[7].split(","); // Dividir directamente por comas

                        Doctor doctor = new Doctor();
                        doctor.setDni(doctorParts[0]);
                        doctor.setName(doctorParts[1]);
                        doctor.setSurname(doctorParts[2]);
                        doctor.setTelephone(Integer.parseInt(doctorParts[3])); // Convertir teléfono a entero
                        doctor.setEmail(doctorParts[4]);
                        patient.setDoctor(doctor);
                        break;
                        
                    case "SURGERY":
                        Surgery surgery = new Surgery();
                        surgery.setType(data);
                        surgeries.add(surgery);
                        break;

                    case "SYMPTOM":
                        Symptom symptom = new Symptom();
                        symptom.setType(data);
                        symptoms.add(symptom);
                        break;

                    case "DISEASE":
                        Disease disease = new Disease();
                        disease.setDisease(data);
                        diseases.add(disease);
                        break;

                    default:
                        System.out.println("Unknown data type: " + type);
                        break;
                }
            } else {
                System.out.println("Invalid data format received: " + dataString);
            }
        }

        System.out.println("Patient Information:");
        System.out.println(patient.toString());

        System.out.println("Surgeries:");
        for (Surgery surgery : surgeries) {
            System.out.println("- " + surgery.getType());
        }

        System.out.println("\nSymptoms:");
        for (Symptom symptom : symptoms) {
            System.out.println("- " + symptom.getType());
        }

        System.out.println("\nDiseases:");
        for (Disease disease : diseases) {
            System.out.println("- " + disease.getDisease());
        }

    } catch (IOException e) {
        Logger.getLogger(Doctor.class.getName()).log(Level.SEVERE, null, e);
    } finally {
        closeConnection(); // Cerrar la conexión al servidor
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


    

