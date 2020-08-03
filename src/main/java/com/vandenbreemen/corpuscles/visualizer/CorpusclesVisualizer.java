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
import java.util.concurrent.atomic.AtomicReference;

public class CorpusclesVisualizer extends JFrame  {

    /**
     * Callback for user interaction with the simulation
     */
    public static interface CellClickListener {
        void onClick(int alongHeight, int alongWidth);
    }

    private static final long DELAY = 100;

    /**
     * The object responsible for doing the actual data processing
     */
    private CellularAutomaton automaton;
    private GridCanvas canvas;
    private JButton stopButton;

    /**
     * Thread that is currently iterating
     */
    private AtomicReference<Thread> iterationThread;

    public CorpusclesVisualizer(Simulation simulation, CellularAutomaton automaton, CellRenderer renderer) {
        super("CORPUSCLES VISUALIZER");
        this.automaton = automaton;
        this.iterationThread = new AtomicReference<>();

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

    /**
     * Update to the given simulation
     * @param simulation
     */
    public void setSimulation(Simulation simulation) {

        this.stopButton.doClick();

        this.canvas.setSimulation(simulation);
        redraw();
    }

    private void drawButtons() {
        JPanel buttons = new JPanel();
        buttons.setLayout(new FlowLayout());

        AtomicBoolean runForever = new AtomicBoolean(false);

        JButton nextIterationButton = new JButton("NEXT ITERATION");
        nextIterationButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                performAutomataStuff();
            }
        });

        JButton next10 = new JButton("Next 10 Iterations");
        next10.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                nextIterationButton.setEnabled(false);

                if(iterationThread.get() != null) {
                    iterationThread.get().interrupt();
                    iterationThread.set(null);
                }

                Thread newThread = new Thread() {
                    @Override
                    public void run() {
                        if(Thread.currentThread().equals(this)) {
                            int numIteration = 10;
                            try {
                                for (int i = 0; i < numIteration; i++) {
                                    performAutomataStuff();
                                    sleep(DELAY);
                                }
                                nextIterationButton.setEnabled(true);
                            }
                            catch(Exception ex) {
                                ex.printStackTrace();
                            }
                        }
                    }
                };
                iterationThread.set(newThread);
                newThread.start();

            }
        });

        JButton runForeverBtn = new JButton("Run Forever");


        stopButton = new JButton("Stop");
        stopButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                Thread current = iterationThread.get();
                if(current != null) {
                    current.interrupt();
                    iterationThread.set(null);
                }

                nextIterationButton.setEnabled(true);
                runForeverBtn.setEnabled(true);
                next10.setEnabled(true);
                runForever.set(false);
                stopButton.setEnabled(false);
            }
        });
        stopButton.setEnabled(false);

        runForeverBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                nextIterationButton.setEnabled(false);
                next10.setEnabled(false);
                runForeverBtn.setEnabled(false);

                Thread temp = iterationThread.get();
                if(temp != null) {
                    temp.interrupt();
                    iterationThread.set(null);
                }

                Thread newThread = new Thread() {
                    @Override
                    public void run() {
                        if(Thread.currentThread().equals(this)) {
                            stopButton.setEnabled(true);
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
                };
                iterationThread.set(newThread);
                newThread.start();

            }
        });

        buttons.add(nextIterationButton);
        buttons.add(next10);
        buttons.add(runForeverBtn);
        buttons.add(stopButton);

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
