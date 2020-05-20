package simulation;

import javax.swing.*;
import java.awt.*;

public class Display extends JFrame {
    SimulationPanel simulationPanel;

    public Display(int sizeX, int sizeY, int k, float p){
        init();
        simulationPanel = new SimulationPanel(sizeX, sizeY, k, p);

        addMenuBar();
        add(simulationPanel);

    }

    public void resetSimulationPanel(int sizeX, int sizeY, int k, float p){
        simulationPanel.stopSimulation();
        remove(simulationPanel);
        simulationPanel = new SimulationPanel(sizeX,sizeY,k,p);
        add(simulationPanel);
        revalidate();
    }

    public void repaintSimulationPanel(){
        simulationPanel.repaint();
    }

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

    private void init(){
        setTitle("Simulation");
        setSize(new Dimension(1280, 720));
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
    }
}
