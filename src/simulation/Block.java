package simulation;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Random;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class Block extends JPanel implements Runnable{

    private final ReentrantReadWriteLock colorLock = new ReentrantReadWriteLock();

    private Random random = new Random();
    private final float p;
    private final long k;

    private final Object PAUSE_LOCK = new Object();
    private volatile boolean isPaused = false;

    private volatile boolean isRunning = true;

    private Block[] neighbours = new Block[4];

    public void setNeighbour(Block block, int i ){
        neighbours[i] = block;
    }

    public Block(long k, float p){
        resetColor();

        this.k = (long)(k *.5 + Math.abs(random.nextLong() % k));
        this.p = p;

        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                pauseUnpause();
            }
        });
    }

    public void stop(){
        synchronized (PAUSE_LOCK){
            isRunning = false;
            isPaused = false;
            PAUSE_LOCK.notifyAll();
        }
    }

    private void pauseUnpause(){
        synchronized (PAUSE_LOCK){
            isPaused = !isPaused;
            PAUSE_LOCK.notifyAll();
        }
    }

    private void resetColor(){
        colorLock.writeLock().lock();
        try {
            setBackground(new Color(Math.abs(random.nextInt() % 256), Math.abs(random.nextInt() % 256), Math.abs(random.nextInt() % 256)));
        }
        finally {
            colorLock.writeLock().unlock();
        }
    }

    public Color getColor(){
        colorLock.readLock().lock();
        try {
            return getBackground();
        }
        finally {
            colorLock.readLock().unlock();
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

        colorLock.writeLock().lock();
        try {
            if(counter != 0f)
                setBackground(new Color((int) (r / counter), (int) (g / counter), (int) (b / counter)));
        }
        finally {
            colorLock.writeLock().unlock();
        }
    }

    public boolean getPaused(){
        return isPaused;
    }

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
