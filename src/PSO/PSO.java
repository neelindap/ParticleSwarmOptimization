package PSO;

import java.util.Random;
import javax.swing.JFrame;
import Animation.Board;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author indap.n
 */
public class PSO extends JFrame {

    private static Swarm swarm;
    private static double[][] areaMap;
    private static Random r = new Random();
    private static int fireX, fireY;

    public PSO() {
        swarm = new Swarm();

        areaMap = new double[Constants.AREA_SIZE][Constants.AREA_SIZE];
        for (int i = 0; i < Constants.AREA_SIZE; i++) {
            for (int j = 0; j < Constants.AREA_SIZE; j++) {
                areaMap[i][j] = 100.0 + r.nextDouble() * 100.0;
            }
        }

        // Set the fire point on area
        initializeFirePoint();

        // Initialize the particles
        initializeParticles();

        Board b = new Board(fireX, fireY, swarm, areaMap);
        add(b);
        setSize(500, 500);
        setVisible(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Set the temp after initalization
        int i = 0;
        while (i < Constants.MAX_ITERATION) {
            evaluateTemp();

            //initializeParticles();
            //b.repaint();
//            printParticleData();
            // Get global best Value
            getGlobalBest();

            // Update the new Postion
            updatePosition(i);
            b.repaint();
            System.out.println(i++);
    
        }
    }

    public static void initializeFirePoint() {
        fireX = Constants.AREA_SIZE / 4 + r.nextInt(Constants.AREA_SIZE - Constants.AREA_SIZE / 2);
        fireY = Constants.AREA_SIZE / 4 + r.nextInt(Constants.AREA_SIZE - Constants.AREA_SIZE / 2);

        for (int i = 3; i >= 0; i--) {
            updateMap(i, Constants.MAX_TEMP - (i * Constants.TEMP_DIFF));
        }

        areaMap[fireX][fireY] = Constants.MAX_TEMP;
        System.out.println("Firepoint "+fireX + " " + fireY);
    }

    public static void evaluateTemp() {
        for (Particle p : swarm.getParticles()) {
            p.setTemp(areaMap[p.getxPos()][p.getyPos()]);
        }
    }

    public static void getGlobalBest() {
        Particle best = swarm.getgBest();
        for (Particle p : swarm.getParticles()) {
            if (p.getFitness() > best.getFitness()) {
                best = p;
            }
        }
        swarm.setgBest(best);
        System.out.println("best pos " + best.getxPos() + "," + best.getyPos());
    }

    public static void updatePosition(int iteration) {
        Particle best = swarm.getgBest();
        Constants.W = Constants.W_UPPERBOUND - (((double) iteration) / Constants.MAX_ITERATION) * (Constants.W_UPPERBOUND - Constants.W_LOWERBOUND);

        for (Particle p : swarm.getParticles()) {
            int newX = (int) ((Constants.W * p.getxVel())
                    + (r.nextDouble() * Constants.C1) * (Math.abs(p.getxPos() - p.getlBest().getxPos()))
                    + (r.nextDouble() * Constants.C2) * (Math.abs((p.getxPos() - best.getxPos()))));

            int newY = (int) ((Constants.W * p.getyVel())
                    + (r.nextDouble() * Constants.C1) * (Math.abs(p.getyPos() - p.getlBest().getyPos()))
                    + (r.nextDouble() * Constants.C2) * (Math.abs(p.getyPos() - best.getyPos())));

            if (newX > Constants.MAX_V_CHANGE) {
                newX = (int) Constants.MAX_V_CHANGE;
            }

            if (newY > Constants.MAX_V_CHANGE) {
                newY = (int) Constants.MAX_V_CHANGE;
            }

//            System.out.println("new velocities "+newX + " " + newY);
            p.setxVel(newX);
            p.setyVel(newY);

            p.setxPos((p.getxPos() + newX) % Constants.AREA_SIZE);
            p.setyPos((p.getyPos() + newY) % Constants.AREA_SIZE);
        }
    }

    private static double getDistance(Particle originalLoc, Particle finalLoc) {
        double a = 0;
        double b = 0;
        a = Math.pow(Math.abs(originalLoc.getxPos() - finalLoc.getxPos()), 2);
        b = Math.pow(Math.abs(originalLoc.getyPos() - finalLoc.getyPos()), 2);

        return Math.sqrt(a + b);
    }

    public static void printParticleData() {
        for (Particle p : swarm.getParticles()) {
            System.out.println("(" + p.getxPos() + "," + p.getyPos() + ") -> (" + p.getxVel() + "," + p.getyVel() + ") -> " + p.getTemp() + " -> " + p.getFitness());
        }
    }

    private static void updateMap(int range, double temp) {
        int xStart = (fireX - range < 0) ? 0 : fireX - range;
        int yStart = (fireY- range < 0) ? 0 : fireY - range;
        for (int i = xStart; i < fireX + range + 1 && i < Constants.AREA_SIZE; i++) {
            for (int j = yStart; j < fireY + range + 1 && j < Constants.AREA_SIZE; j++) {
                areaMap[i][j] = temp;
            }
        }
    }

    public void printMap() {
        for (int i = 0; i < Constants.AREA_SIZE; i++) {
            System.out.println("");
            for (int j = 0; j < Constants.AREA_SIZE; j++) {
                System.out.print(Math.round(areaMap[i][j] * 100.0) / 100.0 + " ");
            }
        }
    }

    public static void initializeParticles() {
        Particle p;
        for (int i = 0; i < Constants.SWARM_SIZE; i++) {
            p = new Particle();

            int xPos = r.nextInt(Constants.AREA_SIZE);
            int yPos = r.nextInt(Constants.AREA_SIZE);

            p.setxPos(xPos);
            p.setyPos(yPos);

            double xVel = r.nextDouble() * Constants.MAX_V_CHANGE;
            double yVel = r.nextDouble() * Constants.MAX_V_CHANGE;

            p.setxVel(xVel);
            p.setyVel(yVel);

            p.setlBest(p);

            swarm.addParticle(p);
        }

        evaluateTemp();
        // Initialize global best as first particle
        swarm.setgBest(swarm.getParticles().get(0));
    }

    public static void main(String args[]) {
        new PSO();
    }

}
