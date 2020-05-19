package simulation;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Random;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Block extends JPanel implements Runnable{
    private final Lock lock = new ReentrantLock();
    private Color color;


    private Random random = new Random();
    private final float p;
    private final long k;
    private Thread thread;

    private final Object LOCK = new Object();
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
                //resetColor();
                //repaint();
                Thread thread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        synchronized(LOCK) {
                            isPaused = !isPaused;
                            LOCK.notifyAll();
                        }
                    }
                });
                thread.run();
            }
        });
    }

    private void resetColor(){
        lock.lock();
        color = new Color(Math.abs(random.nextInt() % 256), Math.abs(random.nextInt() % 256), Math.abs(random.nextInt() % 256));
        lock.unlock();
        setBackground(color);
    }

    public Color getColor(){
        return getBackground(); //new Color(color.getRGB());
    }

    private void averageColor(){
        int r = 0, g = 0, b = 0;

        for(int i = 0; i < 4; i++){
            Color neighbourColor = neighbours[i].getColor();
            r += neighbourColor.getRed();
            g += neighbourColor.getGreen();
            b += neighbourColor.getBlue();
        }

        lock.lock();
        color = new Color((int)(r / 4.0), (int)(g / 4.0), (int)(b / 4.0));
        lock.unlock();

        setBackground(color);
    }

    public void start(){
        if(thread == null){
            thread = new Thread(this, "Name");
            thread.start();
        }
    }

    @Override
    public void run() {
        while (true){
            try {
                synchronized(LOCK){
                    while(isPaused){
                        wait();
                    }
                }

                Thread.sleep(k);
                if(random.nextDouble() > p) {
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
