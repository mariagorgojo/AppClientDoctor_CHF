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
import pojos.Episode;
import pojos.Patient;
import pojos.Patient.Gender;
import pojos.Recording;
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
            socket = new Socket("localhost", 9090); // cambiar mas adelante 
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
            printWriter.println(doctor.getPassword());
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

    public static Doctor viewDoctorDetails(String doctorDni) {
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

                return doctor;
            } else {
                System.out.println("Invalid data format received from server.");
            }
        } catch (IOException e) {
            Logger.getLogger(ConnectionDoctor.class.getName()).log(Level.SEVERE, null, e);
        } finally {
            // printWriter.println("STOP");
            closeConnection(); // correct?
        }
        return null;
    }

    public static List<Patient> getPatientsByDoctor(String doctorDni) {

        List<Patient> patients = new ArrayList<>();
        try {
            connectToServer();

            printWriter.println("VIEW_DOCTOR_PATIENTS");
            printWriter.println(doctorDni);

            if (!(bufferedReader.readLine()).equals("EMPTY")) {

                String patientString;
                while (!(patientString = bufferedReader.readLine()).equals("END_OF_LIST")) {
                    String[] parts = patientString.split(",");
                    System.out.println("Reading patients");
                    Patient patient = new Patient();
                    patient.setDni(parts[0]);
                    patient.setName(parts[1]);
                    patient.setSurname(parts[2]);
                    patients.add(patient);

                }
            }
        } catch (IOException e) {
            Logger.getLogger(Doctor.class.getName()).log(Level.SEVERE, null, e);
        } finally {
            closeConnection(); // Cerrar la conexión al servidor
        }
        return patients; // Retornar la lista de pacientes

    }

    public static Patient viewPatientInformation(String dni) throws IOException {
        Patient patient = null;
        try {
            connectToServer();
            // System.out.println("conecta con el server");
            printWriter.println("VIEW_PATIENT_INFORMATION");
            printWriter.println(dni);

            String dataString = bufferedReader.readLine();
            String[] parts = dataString.split(",");

            if (parts.length == 7) {

                patient = new Patient();
                patient.setDni(parts[0]);
                patient.setName(parts[1]);
                patient.setSurname(parts[2]);
                patient.setEmail(parts[3]);
                patient.setGender(Gender.valueOf(parts[4].toUpperCase()));
                patient.setPhoneNumber(Integer.parseInt(parts[5])); // Convertir a entero
                patient.setDob(LocalDate.parse(parts[6], DateTimeFormatter.ofPattern("yyyy-MM-dd")));

                /*String[] doctorParts = patientParts[7].split(","); // Dividir directamente por comas

                    Doctor doctor = new Doctor();
                    doctor.setDni(doctorParts[0]);
                    doctor.setName(doctorParts[1]);
                    doctor.setSurname(doctorParts[2]);
                    doctor.setTelephone(Integer.parseInt(doctorParts[3])); // Convertir teléfono a entero
                    doctor.setEmail(doctorParts[4]);
                    patient.setDoctor(doctor);*/
                return patient;
            } else {
                System.out.println("Invalid data format received from server.");
                return null;
            }
        } catch (IOException e) {
            Logger.getLogger(Doctor.class.getName()).log(Level.SEVERE, null, e);
        } finally {
            closeConnection(); // Cerrar la conexión al servidor
        }
        return null;
    }

    //ES NECESARIO?
    public static void viewAllEpisodes(String dni) {
        ArrayList<Episode> episodes = new ArrayList<>();

    }

    public static Episode viewPatientEpisode(Integer episode_id, int patient_Id) {

        Episode episode = new Episode();

        List<Surgery> surgeries = new ArrayList<>();
        List<Symptom> symptoms = new ArrayList<>();
        List<Disease> diseases = new ArrayList<>();
        List<Recording> recordings = new ArrayList<>();

        try {
            connectToServer();

            printWriter.println("VIEW_EPISODE_ALL_DETAILS");
            printWriter.println(String.valueOf(episode_id));
            printWriter.println(String.valueOf(patient_Id));

            String dataString;
            while (!((dataString = bufferedReader.readLine()).equals("END_OF_LIST"))) {
                String[] parts = dataString.split(",");

                if (parts.length >= 2) {

                    switch (parts[0]) {
                        case "SURGERIES":
                            Surgery surgery = new Surgery();
                            surgery.setSurgery(parts[1]);
                            episode.getSurgeries().add(surgery);
                            break;

                        case "SYMPTOMS":
                            Symptom symptom = new Symptom();
                            symptom.setSymptom(parts[1]);
                            episode.getSymptoms().add(symptom);
                            break;

                        case "DISEASES":
                            Disease disease = new Disease();
                            disease.setDisease(parts[1]);
                            episode.getDiseases().add(disease);
                            break;

                        case "RECORDINGS": // change -> add data 
                            System.out.println("In recordings connect patient");

                            if (parts.length >= 3) {
                                try {
                                    // Parsear ID
                                    int id = Integer.parseInt(parts[1]);

                                    // Leer y asignar la ruta del archivo
                                    String signalPath = parts[2];

                                    // Extraer y procesar el array de datos
                                    String rawData = parts[3]; // Datos encapsulados en [ ]
                                    rawData = rawData.substring(1, rawData.length() - 1); // Eliminar los corchetes [ ]
                                    String[] dataParts = rawData.split(","); // Separar datos por comas
                                    ArrayList<Integer> data = new ArrayList<>();
                                    for (String dataPart : dataParts) {
                                        try {
                                            data.add(Integer.parseInt(dataPart));
                                        } catch (NumberFormatException e) {
                                            System.err.println("Invalid data value: " + dataPart);
                                        }
                                    }

                                    // Crear y agregar la grabación al episodio
                                    Recording recording = new Recording();
                                    recording.setId(id);
                                    recording.setSignal_path(signalPath);
                                    recording.setData(data);
                                    episode.getRecordings().add(recording);

                                } catch (NumberFormatException e) {
                                    System.err.println("Invalid ID format in RECORDINGS: " + parts[1]);
                                }
                            } else {
                                System.err.println("Invalid RECORDINGS format: " + dataString);
                            }
                        // ns si un break creo q no

                    }

                    return episode;
                } else {
                    System.out.println("Invalid data format received: " + dataString);
                    return null;
                }
            }

        } catch (IOException e) {
            Logger.getLogger(Doctor.class.getName()).log(Level.SEVERE, null, e);
        } finally {
            closeConnection(); // Cerrar la conexión al servidor
        }
        return null;
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
