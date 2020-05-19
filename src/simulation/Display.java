package simulation;

import javax.swing.*;
import java.awt.*;

public class Display extends JFrame {
    public static volatile boolean run = true;
    private int gridSizeX, gridSizeY;
    private float p, k;

    public Display(int sizeX, int sizeY, float k, float p){
        init();
        gridSizeX = sizeX;
        gridSizeY = sizeY;
        this.p = p;
        this.k = k;
        createGrid();
    }

    private void createGrid(){
        int blockCount = gridSizeX * gridSizeY;

        setLayout(new GridLayout(gridSizeY, gridSizeX, 0, 0));
        Block[] blocks = new Block[blockCount];

        for(int i = 0; i < blockCount; i++){
            blocks[i] = new Block(k, p);
            add(blocks[i]);
        }

        for(int i = 0; i < blockCount; i++){
            blocks[i].setNeighbour(blocks[i % gridSizeX == 0 ? i + gridSizeX - 1 : i -1], 0);
            blocks[i].setNeighbour(blocks[(i + 1) % gridSizeX == 0 ? i - gridSizeX + 1 : i + 1], 1);
            blocks[i].setNeighbour(blocks[i < gridSizeX ? i + blockCount - gridSizeX : i - gridSizeX], 2);
            blocks[i].setNeighbour(blocks[i < blockCount - gridSizeX ? i + gridSizeX : i % gridSizeX], 3);

            Thread thread = new Thread(blocks[i]);
            thread.start();
        }
    }

    private void init(){
        setTitle("Simulation");
        setSize(new Dimension(1280, 720));
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
    }
}
