package simulation;

import javax.swing.*;
import java.awt.*;

/**
 * app main display
 */
public class Display extends JFrame {
    SimulationPanel simulationPanel;

    /**
     * Default Constructor
     * @param sizeX simulation width
     * @param sizeY simulation height
     * @param k simulation cell lifetime
     * @param p mutation probability
     */
    public Display(int sizeX, int sizeY, int k, float p){
        init();
        simulationPanel = new SimulationPanel(sizeX, sizeY, k, p);

        addMenuBar();
        add(simulationPanel);

    }

    /**
     * Resets simulation to one with new paramters
     * @param sizeX new simulation width
     * @param sizeY new simulation height
     * @param k new simulation cell lifetime
     * @param p new simulation mutation probability
     */
    public void resetSimulationPanel(int sizeX, int sizeY, int k, float p){
        simulationPanel.stopSimulation();
        remove(simulationPanel);
        simulationPanel = new SimulationPanel(sizeX,sizeY,k,p);
        add(simulationPanel);
        revalidate();
    }

    /**
     * repaints simulation window
     */
    public void repaintSimulationPanel(){
        simulationPanel.repaint();
    }

    /**
     * add menu bar
     */
    private void addMenuBar(){
        var menu = new JMenuBar();
        var settingsMenu = new JMenu("Settings");
        var simulationSettings = new JMenuItem("Simulation Settings");
        simulationSettings.addActionListener(event->{
            var dialogue = new DialogueWindow(this);
            dialogue.setVisible(true);
        });

        settingsMenu.add(simulationSettings);
        menu.add(settingsMenu);

        setJMenuBar(menu);
    }

    /**
     * initializes display
     */
    private void init(){
        setTitle("Simulation");
        setSize(new Dimension(1280, 720));
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
    }
}
