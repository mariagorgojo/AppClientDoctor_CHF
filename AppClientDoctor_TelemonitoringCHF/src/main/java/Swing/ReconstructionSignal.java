package Swing;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class ReconstructionSignal {

    public static void reconstructSignal(ArrayList<Integer> data) {
        if (data == null || data.isEmpty()) {
            System.out.println("Error: No hay datos para reconstruir la señal.");
            return;
        }

        // Crear una ventana para mostrar la señal
        JFrame frame = new JFrame("Signal Reconstruction - ECG");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1000, 600);

        frame.add(new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);

                // Configuración básica
                g.setColor(Color.BLUE);
                int width = getWidth();
                int height = getHeight();
                int padding = 50;

                // Parámetros para la visualización
                int samplingFrequency = 1000; // Frecuencia de muestreo (Hz)
                int pointsToDisplay = Math.min(data.size(), 60000); // Mostrar los primeros 60 segundos

                // Rango de los datos
                int maxDataValue = data.stream().max(Integer::compareTo).orElse(1);
                int minDataValue = data.stream().min(Integer::compareTo).orElse(0);
                int dataRange = maxDataValue - minDataValue;
                if (dataRange == 0) {
                    dataRange = 1;
                }

                // Escalar eje X (tiempo) y eje Y (microvoltios)
                double totalTime = pointsToDisplay / (double) samplingFrequency; // Tiempo total (segundos)
                double xScale = (width - 2.0 * padding) / totalTime; // Pixels por segundo
                double yScale = (height - 2.0 * padding) / dataRange; // Pixels por unidad de señal

                // Dibujar ejes
                g.setColor(Color.BLACK);
                g.drawLine(padding, height - padding, width - padding, height - padding); // Eje X
                g.drawLine(padding, padding, padding, height - padding); // Eje Y

                // Etiquetas en el eje X (tiempo en segundos)
                for (int t = 0; t <= totalTime; t++) { // Etiquetas cada 1 segundo
                    int x = padding + (int) (t * xScale);
                    g.drawString(String.valueOf(t), x - 10, height - padding + 20);
                    g.drawLine(x, height - padding - 5, x, height - padding + 5); // Marcas del eje
                }

                // Etiquetas en el eje Y
                for (int i = 0; i <= 5; i++) {
                    int y = height - padding - i * (height - 2 * padding) / 5;
                    int value = minDataValue + i * dataRange / 5;
                    g.drawString(String.valueOf(value) + " µV", padding - 50, y + 5);
                    g.drawLine(padding - 5, y, padding + 5, y);
                }

                // Dibujar la señal
                g.setColor(Color.BLUE);
                for (int i = 0; i < pointsToDisplay - 1; i++) {
                    double t1 = i / (double) samplingFrequency;
                    double t2 = (i + 1) / (double) samplingFrequency;

                    int x1 = padding + (int) (t1 * xScale);
                    int y1 = height - padding - (int) ((data.get(i) - minDataValue) * yScale);
                    int x2 = padding + (int) (t2 * xScale);
                    int y2 = height - padding - (int) ((data.get(i + 1) - minDataValue) * yScale);

                    g.drawLine(x1, y1, x2, y2);
                }
            }
        });

        frame.setVisible(true);
    }

    public static ArrayList<Integer> generateECGSignal() {
        ArrayList<Integer> ecgData = new ArrayList<>();
        int samplingFrequency = 1000; // Frecuencia de muestreo (Hz)
        int duration = 60; // Duración en segundos
        int amplitude = 100; // Amplitud base en unidades enteras
        int heartRate = 1; // Frecuencia cardiaca en Hz (60 latidos por minuto)

        int totalSamples = samplingFrequency * duration;
        for (int i = 0; i < totalSamples; i++) {
            int time = i % (samplingFrequency / heartRate);

            // Modelo de ondas P, QRS y T en unidades enteras
            int pWave = (int) (amplitude * 0.1 * Math.sin(2 * Math.PI * 5 * time / (double) samplingFrequency)); // Onda P
            int qrsComplex = (int) (amplitude * Math.exp(-Math.pow(time / (double) samplingFrequency - 0.1, 2) / 0.002)); // Complejo QRS
            int tWave = (int) (amplitude * 0.2 * Math.sin(2 * Math.PI * time / (double) samplingFrequency - 0.2)); // Onda T

            // Sumar las ondas para crear la señal ECG
            int ecgSignal = pWave + qrsComplex + tWave;

            // Añadir ruido aleatorio (opcional)
            ecgSignal += (int) (Math.random() * 5 - 2.5); // Ruido de -2 a 2 unidades

            ecgData.add(ecgSignal);
        }
        return ecgData;
    }

    public static void main(String[] args) {
        ArrayList<Integer> data = generateECGSignal();
        reconstructSignal(data);
    }
}
