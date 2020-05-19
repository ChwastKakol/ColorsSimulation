package simulation;

public class Application {
    static Display display;
    static final long timeBetweenFrames = 10L;

    public static void main(String[] args) {
        int sizeX = 20, sizeY = 20, k = 1000;
        float p = .5f;

        if (args.length == 2) {
            try {
                sizeX = Integer.getInteger(args[0]);
            }
            catch (NumberFormatException e){
                sizeX = 10;
            }
            try {
                sizeY = Integer.getInteger(args[1]);
            }
            catch (NumberFormatException e){
                sizeY = 10;
            }
        }

        display = new Display(sizeX, sizeY, k, p);
        display.setVisible(true);

        runLoop();
    }

    private static void runLoop(){
        long time = System.currentTimeMillis();
        while(true){
            if(System.currentTimeMillis() > time + timeBetweenFrames){
                time = System.currentTimeMillis();
                display.repaint();
            }
        }
    }
}
