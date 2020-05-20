package simulation;

import javax.swing.*;
import java.awt.*;

/**
 * simulation window
 */
public class SimulationPanel extends JPanel {
    public static volatile boolean run = true;

    private Thread[] threads;
    private Block[] blocks;

    private int gridSizeX, gridSizeY, k;
    private float p;

    /**
     * default constructor
     * @param sizeX simulation width
     * @param sizeY simulation height
     * @param k simulation cell lifetime
     * @param p mutation probability
     */
    public SimulationPanel(int sizeX, int sizeY, int k, float p){
        run = true;
        gridSizeX = sizeX;
        gridSizeY = sizeY;
        this.p = p;
        this.k = k;
        createGrid();
    }

    /**
     * stops simulation, terminates all its threads
     */
    public void stopSimulation(){
        run = false;
        for(int i = 0; i < blocks.length; i++){
            try {
                blocks[i].stop();
                threads[i].join();
                System.out.println(threads[i].getName());
            }
            catch (InterruptedException e){
                e.printStackTrace();
            }
        }
    }

    /**
     * creates all simulation blocks
     */
    private void createGrid(){
        int blockCount = gridSizeX * gridSizeY;

        setLayout(new GridLayout(gridSizeY, gridSizeX, 0, 0));
        blocks = new Block[blockCount];
        threads = new Thread[blockCount];

        for(int i = 0; i < blockCount; i++){
            blocks[i] = new Block(k, p);
            add(blocks[i]);
        }

        for(int i = 0; i < blockCount; i++){
            blocks[i].setNeighbour(blocks[i % gridSizeX == 0 ? i + gridSizeX - 1 : i -1], 0);
            blocks[i].setNeighbour(blocks[(i + 1) % gridSizeX == 0 ? i - gridSizeX + 1 : i + 1], 1);
            blocks[i].setNeighbour(blocks[i < gridSizeX ? i + blockCount - gridSizeX : i - gridSizeX], 2);
            blocks[i].setNeighbour(blocks[i < blockCount - gridSizeX ? i + gridSizeX : i % gridSizeX], 3);

            threads[i] = new Thread(blocks[i]);
            threads[i].start();
        }
    }
}
