package com.vandenbreemen.corpuscles.visualizer;

import com.vandenbreemen.corpuscles.CellularAutomaton;
import com.vandenbreemen.corpuscles.Corpuscle;
import com.vandenbreemen.corpuscles.CorpusclesData;
import com.vandenbreemen.corpuscles.Simulation;
import com.vandenbreemen.corpuscles.corpuscle.ConwayCell;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.concurrent.atomic.AtomicBoolean;

public class CorpusclesVisualizer extends JFrame  {

    /**
     * Callback for user interaction with the simulation
     */
    public static interface CellClickListener {
        void onClick(int alongHeight, int alongWidth);
    }

    private static final long DELAY = 100;

    private Simulation simulation;

    /**
     * The object responsible for doing the actual data processing
     */
    private CellularAutomaton automaton;
    private GridCanvas canvas;

    public CorpusclesVisualizer(Simulation simulation, CellularAutomaton automaton, CellRenderer renderer) {
        super("CORPUSCLES VISUALIZER");
        this.simulation = simulation;
        this.automaton = automaton;

        setBounds(20,20, 800,700);

        setLayout(new BorderLayout());
        drawButtons();

        CellRenderer theRenderer = renderer;
        if(theRenderer == null) {
            theRenderer = new CellRenderer();
        }
        canvas = new GridCanvas(800, 600, simulation, theRenderer);
        add("Center", canvas);

        setVisible(true);

    }

    /**
     * Set listener to respond to user interaction with cells in the simulation
     * @param listener
     */
    public void setCellClickListener(CellClickListener listener) {
        canvas.setCellClickListener(listener);
    }

    private void drawButtons() {
        JPanel buttons = new JPanel();
        buttons.setLayout(new FlowLayout());

        AtomicBoolean runForever = new AtomicBoolean(false);

        JButton button = new JButton("NEXT ITERATION");
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                performAutomataStuff();
            }
        });

        JButton next10 = new JButton("Next 10 Iterations");
        next10.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new Thread() {
                    @Override
                    public void run() {
                        if(Thread.currentThread().equals(this)) {
                            int numIteration = 10;
                            try {
                                for (int i = 0; i < numIteration; i++) {
                                    performAutomataStuff();
                                    sleep(DELAY);
                                }
                            }
                            catch(Exception ex) {
                                ex.printStackTrace();
                            }
                        }
                    }
                }.start();

            }
        });

        JButton runForeverBtn = new JButton("Run Forever");


        JButton stop = new JButton("Stop");
        stop.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                runForever.set(false);
                stop.setEnabled(false);
            }
        });
        stop.setEnabled(false);

        runForeverBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                new Thread() {
                    @Override
                    public void run() {
                        if(Thread.currentThread().equals(this)) {
                            stop.setEnabled(true);
                            runForever.set(true);
                            try {
                                while(runForever.get()) {
                                    performAutomataStuff();
                                    sleep(DELAY);
                                }
                            }
                            catch(Exception ex) {
                                ex.printStackTrace();
                            }
                        }
                    }
                }.start();


            }
        });

        buttons.add(button);
        buttons.add(next10);
        buttons.add(runForeverBtn);
        buttons.add(stop);

        getContentPane().add("North", buttons);
    }

    private void performAutomataStuff() {
        automaton.performNextEpoch();
        canvas.repaint();
    }

    /**
     * Redraw the current
     */
    public void redraw() {
        canvas.repaint();
    }

    public static void main(String[] args) {
        CorpusclesData data = new CorpusclesData(10,10);
        Simulation simulation = new Simulation(data);
        simulation.activate(0,0);
        simulation.activate(0,2);
        simulation.activate(0,4);
        simulation.activate(1,4);
        simulation.activate(2,4);
        simulation.activate(3,4);
        simulation.nextEpoch();

        Corpuscle corpuscle = new ConwayCell(simulation);

        CellularAutomaton automaton = new CellularAutomaton(simulation) {
            @Override
            protected Corpuscle getCorpuscle(int alongWidth, int alongHeight, Simulation simulation) {
                return corpuscle;
            }
        };

        final CorpusclesVisualizer viz = new CorpusclesVisualizer(simulation, automaton, null);
        viz.setCellClickListener((alongHeight, alongWidth) -> {
            if(!simulation.activated(alongHeight, alongWidth)) {
                simulation.activate(alongHeight, alongWidth);
            } else {
                simulation.deactivate(alongHeight, alongWidth);
            }

            simulation.nextEpoch();
            viz.repaint();
        });

    }

}
