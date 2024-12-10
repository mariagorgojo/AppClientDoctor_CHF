/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Swing;
import java.util.ArrayList;
import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

/**
 *
 * @author martaguzman
 */
public class EMGSignalReconstruction {

    public static void reconstructSignal(ArrayList<Integer> data) {
        if (data == null || data.isEmpty()) {
             System.out.println("Error: There is no data to reconstruct the signal.");
            return;
        }

        // Crear una ventana para mostrar la señal
        JFrame frame = new JFrame("Signal Reconstruction - EMG");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1000, 600);

        frame.add(new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);

                // Configuración básica
                g.setColor(Color.RED);
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

                // Escalar eje X (tiempo) y eje Y (amplitud en mV)
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
                    g.drawString(String.valueOf(value) + " mV", padding - 50, y + 5);
                    g.drawLine(padding - 5, y, padding + 5, y);
                }

                // Dibujar la señal
                g.setColor(Color.RED);
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

    public static void main(String[] args) {
        ArrayList<Integer> emgData = new ArrayList<>();

        if (emgData.isEmpty()) {
            System.out.println("Please, introduce the EMG data in the array.");
        } else {
            reconstructSignal(emgData);
        }
    }
}


