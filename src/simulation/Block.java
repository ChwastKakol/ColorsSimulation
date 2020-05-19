package simulation;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Random;

public class Block extends JPanel implements Runnable{

    private final Object COLOR_LOCK = new Object();
    private volatile Color color;

    private Random random = new Random();
    private final float p;
    private final long k;

    private final Object PAUSE_LOCK = new Object();
    private boolean isPaused = false;

    private Block[] neighbours = new Block[4];

    public void setNeighbour(Block block, int i ){
        neighbours[i] = block;
    }

    public Block(float k, float p){
        resetColor();

        this.k = (long)(k *.5 + Math.abs(random.nextLong() % (long)k));
        this.p = p;

        setBackground(color);

        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                pauseUnpause();
            }
        });
    }

    private void pauseUnpause(){
        synchronized (PAUSE_LOCK){
            isPaused = !isPaused;
            PAUSE_LOCK.notifyAll();
        }
    }

    private void resetColor(){
        synchronized (COLOR_LOCK) {
            color = new Color(Math.abs(random.nextInt() % 256), Math.abs(random.nextInt() % 256), Math.abs(random.nextInt() % 256));
            setBackground(color);
        }
    }

    public Color getColor(){
        synchronized (COLOR_LOCK) {
            return getBackground();
        }
    }

    private void averageColor(){
        int r = 0, g = 0, b = 0;
        float counter = 0f;

        for(int i = 0; i < 4; i++){
            if (neighbours[i].getPaused()) continue;

            counter += 1f;
            Color neighbourColor = neighbours[i].getColor();
            r += neighbourColor.getRed();
            g += neighbourColor.getGreen();
            b += neighbourColor.getBlue();
        }

        synchronized (COLOR_LOCK) {
            color = new Color((int) (r / counter), (int) (g / counter), (int) (b / counter));
            setBackground(color);
        }
    }

    public boolean getPaused(){
        return isPaused;
    }

    @Override
    public void run() {
        while (true){
            try {
                synchronized(PAUSE_LOCK){
                    if(isPaused){
                        PAUSE_LOCK.wait();
                    }
                }
                // Sleeping for k milliseconds
                Thread.sleep(k);

                // Resetting
                if(random.nextDouble() < p) {
                    resetColor();
                }
                else {
                    averageColor();
                }
            }
            catch (InterruptedException e){
                e.printStackTrace();
            }
        }
    }
}
