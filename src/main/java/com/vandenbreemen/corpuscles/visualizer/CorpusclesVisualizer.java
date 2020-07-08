package com.vandenbreemen.corpuscles.visualizer;

import com.vandenbreemen.corpuscles.Simulation;
import com.vandenbreemen.corpuscles.corpuscle.ConwayCell;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class CorpusclesVisualizer extends JFrame  {

    private Simulation simulation;
    private GridCanvas canvas;

    public CorpusclesVisualizer(Simulation simulation) {
        super("CORPUSCLES VISUALIZER");
        this.simulation = simulation;
        setBounds(20,20, 800,700);

        setLayout(new BorderLayout());
        drawButtons();

        canvas = new GridCanvas(800, 600, simulation);
        add("Center", canvas);

        setVisible(true);

    }

    private void drawButtons() {
        JPanel buttons = new JPanel();
        buttons.setLayout(new FlowLayout());

        JButton button = new JButton("NEXT ITERATION");
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                performAutomataStuff();
            }
        });
        buttons.add(button);

        getContentPane().add("North", buttons);
    }

    private void performAutomataStuff() {
        ConwayCell cell = new ConwayCell(simulation);
        for(int h=0; h<simulation.height(); h++) {
            for(int w=0; w<simulation.width(); w++) {
                cell.takeTurn(h, w);
            }
        }
        simulation.nextEpoch();

        canvas.repaint();
    }

}
