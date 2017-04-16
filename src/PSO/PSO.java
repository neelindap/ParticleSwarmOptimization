package PSO;

import java.util.Random;
import javax.swing.JFrame;
import Animation.AreaMap;
import java.util.logging.Level;
import java.util.logging.Logger;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author indap.n
 */
public class PSO extends JFrame implements Runnable {
    
    private static Swarm swarm;
    private static double[][] areaMap;
    private static Random r = new Random();
    private static int fireX, fireY;
    public static int i = 0;
    public static AreaMap b;
    
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
        
        b = new AreaMap(fireX, fireY, swarm, areaMap);
        add(b);
        setSize(500, 500);
        setVisible(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        Thread t = new Thread(this);
        t.start();

        // Set the temp after initalization
    }
    
    public static void initializeFirePoint() {
        fireX = Constants.AREA_SIZE / 4 + r.nextInt(Constants.AREA_SIZE - Constants.AREA_SIZE / 2);
        fireY = Constants.AREA_SIZE / 4 + r.nextInt(Constants.AREA_SIZE - Constants.AREA_SIZE / 2);
        
        for (int i = 3; i >= 0; i--) {
            updateMap(i, Constants.MAX_TEMP - (i * Constants.TEMP_DIFF));
        }
        
        areaMap[fireX][fireY] = Constants.MAX_TEMP;
        System.out.println("Firepoint " + fireX + " " + fireY);
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
            double r1 = r.nextDouble();
            double r2 = r.nextDouble();
            
            int newX = (int) ((Constants.W * p.getxVel())
                    + (r1 * Constants.C1) * (p.getlBest().getxPos() - p.getxPos())
                    + (r2 * Constants.C2) * (best.getxPos() - p.getxPos()));
            
            int newY = (int) ((Constants.W * p.getyVel())
                    + (r1 * Constants.C1) * (p.getlBest().getyPos() - p.getyPos())
                    + (r2 * Constants.C2) * (best.getyPos() - p.getyPos()));
            
             if (newX < Constants.MAX_V_CHANGE * -1) {
                newX = (int) Constants.MAX_V_CHANGE * -1;
            }
            
            if (newY < Constants.MAX_V_CHANGE * -1) {
                newY = (int) Constants.MAX_V_CHANGE * -1;
            }
            
            if (newX > Constants.MAX_V_CHANGE) {
                newX = (int) Constants.MAX_V_CHANGE;
            }
            
            if (newY > Constants.MAX_V_CHANGE) {
                newY = (int) Constants.MAX_V_CHANGE;
            }

//            System.out.println("new velocities "+newX + " " + newY);
            p.setxVel(newX);
            p.setyVel(newY);
            
            if (p.getxPos() + newX > Constants.AREA_SIZE - 1) {
                p.setxPos(Constants.AREA_SIZE - 1);
            } else if (p.getxPos() + newX < 0) {
                p.setxPos(0);
            } else {
                p.setxPos(p.getxPos() + newX);
            }
            
            if (p.getyPos() + newY > Constants.AREA_SIZE - 1) {
                p.setyPos(Constants.AREA_SIZE - 1);
            } else if (p.getyPos() + newY < 0) {
                p.setyPos(0);
            } else {
                p.setyPos(p.getyPos() + newY);
            }
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
        int yStart = (fireY - range < 0) ? 0 : fireY - range;
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
    
    @Override
    public void run() {
        Boolean changed = false;
        while (i < Constants.MAX_ITERATION && !changed) {
            try {
                Thread.sleep(1000);
                changed = true;
                evaluateTemp();

                //initializeParticles();
                //b.repaint();
//            printParticleData();
// Get global best Value
                getGlobalBest();

// Update the new Postion
                updatePosition(i);
                b.repaint();
                
                for(Particle p: swarm.getParticles()){
                    if(p.getlBest().getFitness() != swarm.getgBest().getFitness()){
                        changed = false;
                        break;
                    }
                }
                System.out.println(i++);
            } catch (InterruptedException ex) {
                Logger.getLogger(PSO.class.getName()).log(Level.SEVERE, null, ex);
            }
            
        }
        System.out.println("Fire located in iteration "+i);
    }
    
}
