package simulation;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Random;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * Single simulation module
 */
public class Block extends JPanel implements Runnable{

    private final ReentrantReadWriteLock colorLock = new ReentrantReadWriteLock();

    private final float p;
    private final long k;

    private final Object PAUSE_LOCK = new Object();
    private volatile boolean isPaused = false;

    private volatile boolean isRunning = true;

    private Block[] neighbours = new Block[4];

    /**
     * sets neighbouring blocks for calculating average colour
     * @param block block to be considered neighbour
     * @param i neighbour index (from 0 up to 3)
     */
    public void setNeighbour(Block block, int i ){
        if(i < 0 || i > 3) return;
        neighbours[i] = block;
    }

    /**
     * Default constructor
     * @param k lifetime
     * @param p mutation chance
     */
    public Block(long k, float p){
        setBackground(new Color(Math.abs(Application.random.nextInt() % 256), Math.abs(Application.random.nextInt() % 256), Math.abs(Application.random.nextInt() % 256)));

        this.k = (long)(k *.5 + Math.abs(Application.random.nextLong() % k));
        this.p = p;

        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                pauseUnpause();
            }
        });
    }

    /**
     * stop blocks thread
     */
    public void stop(){
        synchronized (PAUSE_LOCK){
            isRunning = false;
            isPaused = false;
            PAUSE_LOCK.notifyAll();
        }
    }

    /**
     * pauses and unpauses the thread
     */
    private void pauseUnpause(){
        synchronized (PAUSE_LOCK){
            isPaused = !isPaused;
            PAUSE_LOCK.notifyAll();
        }
    }

    /**
     * called upon mutation, sets block to new randomly picked color
     */
    private void resetColor(){
        synchronized (Block.class)
        {
            colorLock.writeLock().lock();
            try {
                setBackground(new Color(Math.abs(Application.random.nextInt() % 256), Math.abs(Application.random.nextInt() % 256), Math.abs(Application.random.nextInt() % 256)));
            } finally {
                colorLock.writeLock().unlock();
            }
        }
    }

    /**
     * gets blocks colour
     * @return blocks color
     */
    public Color getColor(){
        colorLock.readLock().lock();
        try {
            return getBackground();
        }
        finally {
            colorLock.readLock().unlock();
        }
    }

    /**
     * calculates and sets average colour based on colors of running neighbours
     */
    private void averageColor(){
        synchronized(Block.class){
            colorLock.writeLock().lock();

            int r = 0, g = 0, b = 0;
            float counter = 0f;

            for (int i = 0; i < 4; i++) {
                if (neighbours[i].getPaused()) continue;

                counter += 1f;
                Color neighbourColor = neighbours[i].getColor();
                r += neighbourColor.getRed();
                g += neighbourColor.getGreen();
                b += neighbourColor.getBlue();
            }

            try {
                if (counter != 0f)
                    setBackground(new Color((int) (r / counter), (int) (g / counter), (int) (b / counter)));
            } finally {
                colorLock.writeLock().unlock();
            }
        }
    }

    /**
     * gets running state
     * @return true if thread is running false otherwise
     */
    public boolean getPaused(){
        return isPaused;
    }

    /**
     * overrides Runnable run method
     */
    @Override
    public void run() {
        while (isRunning){
            try {
                synchronized(PAUSE_LOCK){
                    if(isPaused){
                        PAUSE_LOCK.wait();
                    }
                }
                // Sleeping for k milliseconds
                Thread.sleep(k);

                // Resetting
                if(Application.random.nextDouble() < p) {
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
