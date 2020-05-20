package simulation;

public class Application {
    static Display display;
    static final long timeBetweenFrames = 10L;
    static final int blockPerSideLimit = 60;

    public static void main(String[] args) {
        int sizeX = 20, sizeY = 15, k = 10;
        float p = .01f;

        if (args.length >= 2) {
            try {
                sizeX = Integer.parseInt(args[0]);
            }
            catch (NumberFormatException e){
                sizeX = 10;
            }
            try {
                sizeY = Integer.parseInt(args[1]);
            }
            catch (NumberFormatException e){
                sizeY = 10;
            }
        }
        if (args.length >= 3){
            try {
                k = Integer.parseInt(args[2]);
            }
            catch (NumberFormatException e){
                k = 10;
            }
        }
        if(args.length >= 4){
            try {
                p = Float.parseFloat(args[3]);
            }
            catch (NumberFormatException e){
                p = .01f;
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
                display.repaintSimulationPanel();
            }
        }
    }
}
