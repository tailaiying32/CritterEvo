package view;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class BarChartPanel extends JPanel {
    private final String title;
    private final int NUM_BINS = 10;
    private int[] bins;
    private int maxCount;
    private double minValue;
    private double maxValue;

    public BarChartPanel(String title) {
        this.title = title;
        this.bins = new int[NUM_BINS];
        setBorder(BorderFactory.createTitledBorder(title + " Distribution"));
        setPreferredSize(new Dimension(200, 200));
    }

    public void updateData(List<Double> values) {
        if (values.isEmpty()) {
            bins = new int[NUM_BINS];
            maxCount = 0;
            repaint();
            return;
        }

        // Reset bins
        bins = new int[NUM_BINS];
        maxCount = 0;

        // Find min and max values
        minValue = values.stream().mapToDouble(v -> v).min().getAsDouble();
        maxValue = values.stream().mapToDouble(v -> v).max().getAsDouble();
        double range = maxValue - minValue;

        // Handle case where all values are the same
        if (range == 0) {
            bins[0] = values.size();
            maxCount = values.size();
            repaint();
            return;
        }

        double binSize = range / NUM_BINS;

        // Fill bins
        for (double value : values) {
            int binIndex = (int) ((value - minValue) / binSize);
            if (binIndex == NUM_BINS) binIndex--; // Handle maximum value
            bins[binIndex]++;
            maxCount = Math.max(maxCount, bins[binIndex]);
        }

        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int padding = 30;
        int barWidth = (getWidth() - 2 * padding) / NUM_BINS;
        int maxHeight = getHeight() - 2 * padding;

        // Draw axes
        g2.setColor(Color.BLACK);
        g2.drawLine(padding, getHeight() - padding, getWidth() - padding, getHeight() - padding); // X axis
        g2.drawLine(padding, getHeight() - padding, padding, padding); // Y axis

        // Draw bars
        if (maxCount > 0) {
            for (int i = 0; i < NUM_BINS; i++) {
                int barHeight = (int) ((double) bins[i] / maxCount * maxHeight);
                int x = padding + i * barWidth;
                int y = getHeight() - padding - barHeight;

                // Create gradient paint for 3D effect
                GradientPaint gradient = new GradientPaint(
                        x, y, new Color(30, 144, 255, 180),
                        x + barWidth, y, new Color(30, 144, 255, 220)
                );
                g2.setPaint(gradient);
                g2.fillRect(x, y, barWidth - 2, barHeight);

                // Draw border
                g2.setColor(new Color(30, 144, 255));
                g2.drawRect(x, y, barWidth - 2, barHeight);
            }
        }

        // Draw labels
        g2.setColor(Color.BLACK);
        g2.setFont(new Font("Arial", Font.PLAIN, 10));

        // Draw min and max values on X axis
        g2.drawString(String.format("%.1f", minValue), padding, getHeight() - padding + 15);
        g2.drawString(String.format("%.1f", maxValue), getWidth() - padding - 20, getHeight() - padding + 15);

        // Draw max count on Y axis
        g2.drawString(String.format("%d", maxCount), padding - 25, padding + 10);

        // Draw zero on both axes
        g2.drawString("0", padding - 15, getHeight() - padding + 15);
    }
}