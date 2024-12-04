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
        JFrame frame = new JFrame("Signal Reconstruction");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);

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
                double samplingFrequency = 1000.0; // Frecuencia de muestreo (Hz)
                int pointsToDisplay = 100; // Mostrar solo los primeros 1,000 datos (1 segundo)
                if (pointsToDisplay > data.size()) {
                    pointsToDisplay = data.size(); // Ajustar si hay menos datos disponibles
                }

                // Rango de los datos (convertir a microvoltios)
                int maxDataValue = data.stream().max(Integer::compareTo).orElse(1);
                int minDataValue = data.stream().min(Integer::compareTo).orElse(0);
                int dataRange = maxDataValue - minDataValue;
                if (dataRange == 0) {
                    dataRange = 1;
                }

                // Escalar eje X (tiempo) y eje Y (microvoltios)
                double totalTime = pointsToDisplay / samplingFrequency; // Tiempo total de la ventana (segundos)
                double xScale = (width - 2.0 * padding) / totalTime; // Pixels por segundo
                double yScale = (height - 2.0 * padding) / dataRange; // Pixels por microvoltio

                // Dibujar ejes
                g.setColor(Color.BLACK);
                g.drawLine(padding, height - padding, width - padding, height - padding); // Eje X
                g.drawLine(padding, padding, padding, height - padding); // Eje Y

                // Etiquetas en el eje X (tiempo en segundos, de 0.01 en 0.01)
                g.setColor(Color.BLACK);
                for (double t = 0; t <= totalTime; t += 0.1) { // Etiquetas cada 0.1 segundos
                    int x = padding + (int) (t * xScale);
                    g.drawString(String.format("%.1f", t), x - 10, height - padding + 20);
                    g.drawLine(x, height - padding - 5, x, height - padding + 5); // Marcas del eje
                }

                // Etiquetas en el eje Y (en microvoltios)
                for (int i = 0; i <= 5; i++) {
                    int y = height - padding - i * (height - 2 * padding) / 5;
                    int value = minDataValue + i * dataRange / 5;
                    g.drawString(String.valueOf(value) + " µV", padding - 40, y + 5);
                    g.drawLine(padding - 5, y, padding + 5, y);
                }

                // Dibujar la señal (primeros 1,000 puntos)
                g.setColor(Color.BLUE);
                for (int i = 0; i < pointsToDisplay - 1; i++) {
                    double t1 = i / samplingFrequency;
                    double t2 = (i + 1) / samplingFrequency;

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

    public static ArrayList<Integer> generateTestSignal() {
        // Generar señal de prueba con 60,000 datos
        ArrayList<Integer> testData = new ArrayList<>();
        double frequency = 1.0; // Frecuencia del ECG (Hz)
        double samplingFrequency = 1000.0; // Frecuencia de muestreo (Hz)
        int numSamples = 60000; // Número de muestras

        for (int i = 0; i < numSamples; i++) {
            double time = i / samplingFrequency;
            double ecgSignal = 100 * Math.sin(2 * Math.PI * frequency * time); // Señal senoidal base
            if (i % 1000 < 50) {
                ecgSignal += 150; // Simular un pico QRS
            }
            testData.add((int) ecgSignal);
        }

        return testData;
    }

    public static void main(String[] args) {
        ArrayList<Integer> data = generateTestSignal();
        reconstructSignal(data);
    }
}