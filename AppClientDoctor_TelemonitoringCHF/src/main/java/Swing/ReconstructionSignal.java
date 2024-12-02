package Swing;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class ReconstructionSignal {

    public static void reconstructSignal(ArrayList<Integer> data) {
        // Verificar si los datos son válidos
         System.out.println("data en swings: ");
        if (data == null || data.isEmpty()) {
            System.out.println("Error: No hay datos para reconstruir la señal.");
            return;
        }

        // Periodo de muestreo (en segundos)
        System.out.println("Reconstructing signal with data: " + data);
        double samplingFrequency = 1000; // Frecuencia de muestreo en Hz
        double samplingPeriod = 1.0 / samplingFrequency;

        // Crear una ventana para mostrar la señal
        JFrame frame = new JFrame("Signal Reconstruction");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);

        // Agregar un panel para dibujar la señal
        frame.add(new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);

                // Configuración básica del gráfico
                g.setColor(Color.BLUE);
                int width = getWidth();
                int height = getHeight();
                int padding = 50;

                // Calcular escala y posiciones
                int maxDataValue = data.stream().max(Integer::compareTo).orElse(1);
                int minDataValue = data.stream().min(Integer::compareTo).orElse(0);
                int dataRange = maxDataValue - minDataValue;
                if (dataRange == 0) {
                    dataRange = 1; // Evitar división por cero
                }

                int numPoints = data.size();
                int pointSpacing = numPoints > 1 ? (width - 2 * padding) / (numPoints - 1) : 0;

                // Tiempo total basado en las muestras y el periodo
                double totalTime = numPoints * samplingPeriod;

                // Dibujar ejes
                g.setColor(Color.BLACK);
                g.drawLine(padding, height - padding, width - padding, height - padding); // Eje X
                g.drawLine(padding, padding, padding, height - padding); // Eje Y

                // Etiquetas de tiempo en el eje X
                int numSeconds = (int) Math.ceil(totalTime); // Tiempo total en segundos
                for (int i = 0; i <= numSeconds; i++) {
                    int x = padding + (int) ((i / totalTime) * (width - 2 * padding)); // Escalado al ancho
                    g.drawString(String.valueOf(i), x, height - padding + 20); // Etiqueta de tiempo
                    g.drawLine(x, height - padding - 5, x, height - padding + 5); // Marca en el eje
                }

                // Etiquetas de amplitud en el eje Y
                int numYLabels = 5; // Número de etiquetas en el eje Y
                for (int i = 0; i <= numYLabels; i++) {
                    int y = height - padding - i * (height - 2 * padding) / numYLabels;
                    int value = minDataValue + i * dataRange / numYLabels; // Valores escalados
                    g.drawString(String.valueOf(value), padding - 30, y + 5); // Etiqueta en el eje Y
                    g.drawLine(padding - 5, y, padding + 5, y); // Marca en el eje
                }

                // Dibujar la señal
                g.setColor(Color.BLUE);
                for (int i = 0; i < data.size() - 1; i++) {
                    int x1 = padding + i * pointSpacing;
                    int y1 = height - padding - (data.get(i) - minDataValue) * (height - 2 * padding) / dataRange;
                    int x2 = padding + (i + 1) * pointSpacing;
                    int y2 = height - padding - (data.get(i + 1) - minDataValue) * (height - 2 * padding) / dataRange;
                    g.drawLine(x1, y1, x2, y2);
                }
            }
        });

        frame.setVisible(true);
    }

    public static ArrayList<Integer> generateTestSignal() {
        // Genera una señal senoide para pruebas
        ArrayList<Integer> testData = new ArrayList<>();
        for (int i = 0; i < 1000; i++) {
            testData.add((int) (50 * Math.sin(2 * Math.PI * i / 100.0)));
        }
        return testData;
    }

    public static void main(String[] args) {
        // Generar señal de prueba o usar datos reales
        ArrayList<Integer> data = generateTestSignal();
        reconstructSignal(data);
    }
}
