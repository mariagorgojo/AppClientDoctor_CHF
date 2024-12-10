/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ConnectionDoctor;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.time.LocalDate;
import java.time.LocalDateTime;
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
import pojos.Recording.Type;
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

    public static void connectToServer(String ip_address) throws IOException {
        if (socket == null || socket.isClosed()) {
            System.out.println("Connecting to server...");
            socket = new Socket(ip_address, 9090); // cambiar mas adelante 
            printWriter = new PrintWriter(socket.getOutputStream(), true);
            bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        }
    }

    public static void closeConnection() {
        try {

            printWriter.println("LOGOUT");

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

    public static boolean sendRegisterServer(Doctor doctor, String encryptedPassword) {
        try {
            // connectToServer(ip_address);
            printWriter.println("REGISTER_DOCTOR");
            printWriter.println(doctor.getDni());
            printWriter.println(encryptedPassword);
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
        }/* finally {
            // printWriter.println("STOP");
            closeConnection(); // correct?
        }*/
    }

    public static boolean validateLogin(String dni, String password) {
        try {
            // connectToServer(ip_address);
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
        }
    }

    public static Doctor viewDoctorDetails(String doctorDni) {
        Doctor doctor = null;

        try {
            //   connectToServer();
            printWriter.println("VIEW_DOCTOR_DETAILS");
            printWriter.println(doctorDni);

            String doctorString = bufferedReader.readLine();

            String[] parts = doctorString.split(";");

            if (parts.length == 5) {
                doctor = new Doctor();
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
            
        }
        return null;
    }

    public static List<Patient> getPatientsByDoctor(String doctorDni) {

        List<Patient> patients = new ArrayList<>();
        try {

            printWriter.println("VIEW_DOCTOR_PATIENTS");
            printWriter.println(doctorDni);
            String firstLine = bufferedReader.readLine();
            if (!"EMPTY".equals(firstLine)) {

                String patientString = firstLine.trim();
                while (!(patientString.equals("END_OF_LIST"))) {
                    String[] parts = patientString.split(";");
                    if (parts.length == 3) { // Asegurar formato correcto

                        Patient patient = new Patient();
                        patient.setDni(parts[0]);
                        patient.setName(parts[1]);
                        patient.setSurname(parts[2]);
                        patients.add(patient);
                    } else {
                        System.out.println("Malformed line: " + patientString); // Debug de errores
                    }
                    patientString = bufferedReader.readLine().trim();
                }
            }
        } catch (IOException e) {
            Logger.getLogger(Doctor.class.getName()).log(Level.SEVERE, null, e);
           }
        return patients; // Retornar la lista de pacientes

    }

    public static Patient viewPatientInformation(String dni) throws IOException {
        Patient patient = null;
        ArrayList<Disease> previousDiseases = new ArrayList<>();

        try {
            
            printWriter.println("VIEW_PATIENT_INFORMATION");
            printWriter.println(dni);

            String dataString;
            while (!((dataString = bufferedReader.readLine()).equals("END_OF_DISEASES"))) {
                String[] partsDisease = dataString.split(";");

                if (partsDisease.length >= 2 && partsDisease[0].equals("DISEASES")) {

                    Disease disease = new Disease();
                    disease.setDisease(partsDisease[1]);
                    previousDiseases.add(disease);

                }
            }
            dataString = bufferedReader.readLine(); // Read line that contains patient data
            String[] parts = dataString.split(";");
            if (parts.length == 8) {
                patient = new Patient();
                patient.setId(Integer.parseInt(parts[0]));
                patient.setDni(parts[1]);
                patient.setName(parts[2]);
                patient.setSurname(parts[3]);
                patient.setEmail(parts[4]);
                patient.setGender(Gender.valueOf(parts[5].toUpperCase()));
                patient.setPhoneNumber(Integer.parseInt(parts[6])); // Convertir a entero
                patient.setDob(LocalDate.parse(parts[7], DateTimeFormatter.ofPattern("yyyy-MM-dd")));
                patient.setPreviousDiseases(previousDiseases);

                return patient;
            } else {
                System.out.println("Invalid data format received from server.");
                return null;
            }
        } catch (IOException e) {
            Logger.getLogger(Doctor.class.getName()).log(Level.SEVERE, null, e);
           
        }
        return patient;
    }

    public static ArrayList<Episode> viewAllEpisodes(String patientDni) throws IOException {
        ArrayList<Episode> episodes = new ArrayList<>();

        try {
            // Conectar al servidor
            printWriter.println("VIEW_EPISODES_DOCTOR");
            printWriter.println(patientDni); // Enviar el DNI del paciente

            // Leer la lista de episodios desde el servidor
            String dataString;
            while (!((dataString = bufferedReader.readLine()).equals("END_OF_LIST"))) {

                String[] parts = dataString.split(";");
                if (parts.length == 2) { // Validar que los datos contengan ID y Fecha
                    Episode episode = new Episode();
                    episode.setId(Integer.parseInt(parts[0])); // ID del episodio
                    episode.setDate(LocalDateTime.parse(parts[1], DateTimeFormatter.ISO_DATE_TIME));

                    episodes.add(episode);
                } else {
                    System.err.println("Error seeing episodes");
                }
            }
        } catch (IOException e) {
            System.err.println("Error retrieving episodes: " + e.getMessage());

            // No se debería cerrar la conexión, cerrar SOLO al final de las operaciones
            // cerrar en el menu del paciente
        }
        
        
        return episodes;
    }

    public static Episode viewPatientEpisode(int episode_id, int patient_Id) {

        Episode episode = new Episode();

        try {
            // connectToServer();

            printWriter.println("VIEW_EPISODE_ALL_DETAILS");
            printWriter.println(String.valueOf(episode_id));
            printWriter.println(String.valueOf(patient_Id));

            String dataString;
            while (!((dataString = bufferedReader.readLine()).equals("END_OF_LIST"))) {
                String[] parts = dataString.split(";");

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

                            if (parts.length >= 4) {
                                try {
                                    // Parsear ID
                                    int id = Integer.parseInt(parts[1]);
                                   // System.out.println("recording id " + id);
                                    // Leer y asignar la ruta del archivo
                                    Type type =  Type.valueOf(parts[2]); // Devuelve Type.ECG
                                    String signalPath = parts[3];
                                    //System.out.println("recording path " + signalPath);
                                    // Extraer y procesar el array de datos
                                    String rawData = parts[4]; // Datos encapsulados en [ ]
                                   // System.out.println(" rawData sin subString: " + rawData);

                                    rawData = rawData.substring(1, rawData.length() - 1); // Eliminar los corchetes [ ]
                                   // System.out.println(" rawData: " + rawData);

                                    String[] dataParts = rawData.split(","); // Separar datos por comas
                                    ArrayList<Integer> data = new ArrayList<>();
                                  //  System.out.println("data: " + data);
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
                                    recording.setType(type);
                                    recording.setSignal_path(signalPath);
                                    recording.setData(data);
                                    episode.getRecordings().add(recording);
                                 //   System.out.println(recording);

                                } catch (NumberFormatException e) {
                                    System.err.println("Invalid ID format in RECORDINGS: " + parts[1]);
                                }
                            } else {
                                System.err.println("Invalid RECORDINGS format: " + dataString);
                            }
                            break;
                        default:
                            System.err.println("Unknown detail type received: " + parts[0]);

                    }
                }
            }
            return episode;

        } catch (IOException e) {
            System.err.println("Error retrieving episode details: " + e.getMessage());
             }
        return episode;
    }

    public static List<Disease> getAvailableDiseases() {
        List<Disease> diseases = new ArrayList<>();

        try {
            // connectToServer(); // Establecer conexión con el servidor

            printWriter.println("AVAILABLE_DISEASES"); // Comando para el servidor
            printWriter.flush();

            String diseaseData;
            while (!(diseaseData = bufferedReader.readLine()).equals("END_OF_LIST")) {
                Disease disease = new Disease();
                disease.setDisease(diseaseData);
                diseases.add(disease);
            }
        } catch (IOException e) {
            System.err.println("Error retrieving diseases: " + e.getMessage());
        }
        

        return diseases;
    }

    public static List<Surgery> getAvailableSurgeries() {
        List<Surgery> surgeries = new ArrayList<>();

        try {
            //  connectToServer(); // Establecer conexión con el servidor

            printWriter.println("AVAILABLE_SURGERIES"); // Comando para el servidor
            printWriter.flush();
            String surgeryData;
            while (!(surgeryData = bufferedReader.readLine()).equals("END_OF_LIST")) {
                Surgery surgery = new Surgery();
                surgery.setSurgery(surgeryData);
                surgeries.add(surgery);
            }
        } catch (IOException e) {
            System.err.println("Error retrieving surgeries: " + e.getMessage());
        }
        

        return surgeries;
    }

    public static boolean updateEpisode(Episode episode, List<String> diseases, List<String> surgeries) {
        try {
            printWriter.println("INSERT_EPISODE");
            printWriter.println("UPDATE_EPISODE");
            int episodeId = episode.getId();
            printWriter.println(episodeId);

            for (String disease : diseases) {
                printWriter.println("DISEASE|" + disease);
            }

            for (String surgery : surgeries) {
                printWriter.println("SURGERY|" + surgery);
            }

            printWriter.println("END_OF_UPDATE");
            printWriter.flush();

            String response = bufferedReader.readLine();
            return "SUCCESS".equals(response);
        } catch (IOException e) {
            System.err.println("Error al actualizar el episodio: " + e.getMessage());
            return false;
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
