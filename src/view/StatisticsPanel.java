package view;

import model.Critter;
import model.WorldModel;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StatisticsPanel extends JPanel {
    private final JTable statsTable;
    private final DefaultTableModel tableModel;
    private final JPanel chartsPanel;
    private final Map<String, BarChartPanel> charts;
    private WorldModel world;

    public StatisticsPanel() {
        setLayout(new BorderLayout());
        this.charts = new HashMap<>();

        // Create table
        String[] columnNames = {
                "Statistic", "Min", "Max", "Average", "Total", "Population"
        };
        tableModel = new DefaultTableModel(columnNames, 0);
        statsTable = new JTable(tableModel);
        JScrollPane tableScrollPane = new JScrollPane(statsTable);

        // Create charts panel
        chartsPanel = new JPanel(new GridLayout(2, 2, 10, 10));
        chartsPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Add components
        JSplitPane splitPane = new JSplitPane(
                JSplitPane.VERTICAL_SPLIT,
                tableScrollPane,
                chartsPanel
        );
        splitPane.setResizeWeight(0.3);
        add(splitPane, BorderLayout.CENTER);

        createCharts();
    }

    private void createCharts() {
        String[] traits = {"Health", "Size", "Offense", "Defense"};
        for (String trait : traits) {
            BarChartPanel chart = new BarChartPanel(trait);
            charts.put(trait, chart);
            chartsPanel.add(chart);
        }
    }

    public void setWorld(WorldModel world) {
        this.world = world;
    }

    public void updateStats() {
        if (world == null || world.getCritters().isEmpty()) {
            tableModel.setRowCount(0);
            for (BarChartPanel chart : charts.values()) {
                chart.updateData(new ArrayList<>());
            }
            return;
        }

        Map<Point, Critter> critters = world.getCritters();

        // Clear existing table data
        tableModel.setRowCount(0);

        // Collect data
        Map<String, List<Double>> traitData = new HashMap<>();
        traitData.put("Health", new ArrayList<>());
        traitData.put("Size", new ArrayList<>());
        traitData.put("Offense", new ArrayList<>());
        traitData.put("Defense", new ArrayList<>());

        for (Critter critter : critters.values()) {
            traitData.get("Health").add((double) critter.getHealth());
            traitData.get("Size").add((double) critter.getSize());
            traitData.get("Offense").add((double) critter.getOffense());
            traitData.get("Defense").add((double) critter.getDefense());
        }

        // Update table and charts
        for (Map.Entry<String, List<Double>> entry : traitData.entrySet()) {
            String trait = entry.getKey();
            List<Double> values = entry.getValue();
            addStatRow(trait, values, critters.size());
            updateChart(trait, values);
        }
    }

    private void addStatRow(String name, List<Double> values, int population) {
        if (values.isEmpty()) return;

        double min = values.stream().mapToDouble(v -> v).min().orElse(0);
        double max = values.stream().mapToDouble(v -> v).max().orElse(0);
        double sum = values.stream().mapToDouble(v -> v).sum();
        double avg = sum / values.size();

        tableModel.addRow(new Object[]{
                name,
                String.format("%.2f", min),
                String.format("%.2f", max),
                String.format("%.2f", avg),
                String.format("%.2f", sum),
                population
        });
    }

    private void updateChart(String trait, List<Double> values) {
        BarChartPanel chart = charts.get(trait);
        if (chart != null) {
            chart.updateData(values);
        }
    }
}