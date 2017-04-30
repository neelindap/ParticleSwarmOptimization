package business;

import java.awt.Container;
import java.util.Random;
import javax.swing.JFrame;
import userInterface.AreaMap;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Scanner;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.FileHandler;
import java.util.logging.Formatter;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.mail.MessagingException;
import javax.swing.JOptionPane;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author indap.n
 */
public class PSO extends JFrame implements MouseListener {

    private static Swarm swarm;
    private static double[][] areaMap;
    private static Random r = new Random();
    private static int fireX, fireY;
    public static int iteration = 0;
    public static AreaMap map;

    public static double startTime;
    public static double endTime;

    public static double lat;
    public static double lon;

    public static final Logger logger = Logger.getLogger("PSOLogs");
    public static FileHandler fh;

    public static boolean clicked = false;

    public PSO() {
        swarm = new Swarm();

        initializeTemp();

        // Set the fire point on area
        initializeFirePoint();

        psoDisplay();
        // Set the temp after initalization
    }

    public void initializeTemp() {
        areaMap = new double[Constants.AREA_SIZE][Constants.AREA_SIZE];
        for (int i = 0; i < Constants.AREA_SIZE; i++) {
            for (int j = 0; j < Constants.AREA_SIZE; j++) {
                areaMap[i][j] = 100.0 + r.nextDouble() * 100.0;
            }
        }
    }

    public void psoDisplay() {

        // Initialize the particles
        initializeParticles();

        // Create map JPanel
        map = new AreaMap(fireX, fireY, swarm, areaMap, lat, lon);
        this.add(map);
        map.addMouseListener(this);
        this.setTitle("Particle Swarm Optimization");
        this.setSize(Constants.AREA_SIZE * Constants.MULTIPLICATION_FACTOR, Constants.AREA_SIZE * Constants.MULTIPLICATION_FACTOR);
        this.setVisible(true);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        psoItertation();
    }

    public void psoItertation() {
        //Thread t = new Thread(this);
        //t.start();
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {

            @Override
            public void run() {
                Boolean changed = false;
                startTime = System.currentTimeMillis();
                while (iteration < Constants.MAX_ITERATION && !changed) {
                    try {
                        Thread.sleep(Constants.THREAD_SLEEP_TIME);
                        changed = true;

                        // Fitness function evalutaion at new location
                        evaluateTemp();

                        // Get global best Value
                        getGlobalBest();

                        // Update the new Postion
                        updatePosition(iteration);

                        // animating for new positions
                        map.repaint();

                        // Checking if all particles have reached goal
                        for (Particle p : swarm.getParticles()) {
                            if (p.getLocalBest().getFitness() != swarm.getGlobalBest().getFitness()) {
                                changed = false;
                                break;
                            }
                        }

                        System.out.println("Iteration " + iteration++ + " -> best location detected by swarm (" + swarm.getGlobalBest().getxPos() + "," + swarm.getGlobalBest().getyPos() + ")");
                        logger.log(Level.INFO, "Iteration " + iteration + " -> best location detected by swarm (" + swarm.getGlobalBest().getxPos() + "," + swarm.getGlobalBest().getyPos() + ")");
                    } catch (InterruptedException ex) {
                        ex.printStackTrace();
                    }
                }
                endTime = System.currentTimeMillis();
                System.out.println("\n\nAll drones at fire location in iteration " + iteration + " -> Total Time taken: " + (endTime - startTime) / 1000 + " seconds");
                logger.log(Level.INFO, "\n\nAll drones at fire location in iteration " + iteration + " -> Total Time taken: " + (endTime - startTime) / 1000 + " seconds");
                timer.cancel();

                if (!clicked) {
                    int dialogButton = JOptionPane.YES_NO_OPTION;
                    int dialogResult = JOptionPane.showConfirmDialog(null, "Fire Detected at Location(" + swarm.getGlobalBest().getxPos() + "," + swarm.getGlobalBest().getyPos() + ").\nSelect 'YES' to send out drones to extinguish fire and send a notification to fire department.", "Fire detected", dialogButton);
                    if (dialogResult == 0) {
                        saveMap();
                        try {
                            EmailUtility.sendEmail(fireX, fireY);
                        } catch (MessagingException ex) {
                            Logger.getLogger(PSO.class.getName()).log(Level.SEVERE, null, ex);
                        } catch (IOException ex) {
                            Logger.getLogger(PSO.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                }
            }
        }, 0, 1);
    }

    public void saveMap() {
        try {
            Container c = getContentPane();
            BufferedImage im = new BufferedImage(c.getWidth(), c.getHeight(), BufferedImage.TYPE_INT_ARGB);
            c.paint(im.getGraphics());
            ImageIO.write(im, "PNG", new File("Fire.png"));
        } catch (IOException ex) {
            Logger.getLogger(PSO.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void initializeFirePoint() {
        fireX = Constants.AREA_SIZE / 4 + r.nextInt(Constants.AREA_SIZE / 2);
        fireY = Constants.AREA_SIZE / 4 + r.nextInt(Constants.AREA_SIZE / 2);
        System.out.println("Fire-location on map (" + fireX + "," + fireY + ")");
        logger.log(Level.INFO, "Fire-location on map (" + fireX + "," + fireY + ")");
        displayFirePointOnMap();
    }

    public static void displayFirePointOnMap() {
        for (int i = 3; i >= 0; i--) {
            updateMapTemp(i, Constants.MAX_TEMP - (i * Constants.TEMP_DIFF));
        }
        areaMap[fireX][fireY] = Constants.MAX_TEMP;
    }

    public static void evaluateTemp() {
        for (Particle p : swarm.getParticles()) {
            p.setTemparture(areaMap[p.getxPos()][p.getyPos()]);
        }
    }

    public static void getGlobalBest() {
        Particle best = swarm.getGlobalBest();
        for (Particle p : swarm.getParticles()) {
            if (p.getFitness() > best.getFitness()) {
                best = p;
            }
        }
        swarm.setGlobalBest(best);
//        System.out.println("best pos " + best.getxPos() + "," + best.getyPos());
    }

    public void updatePosition(int iteration) {
        Particle gBest = swarm.getGlobalBest();
        double w = Constants.INERTIA_WEIGHT - (((double) iteration) / Constants.MAX_ITERATION) * (Constants.INERTIA_WEIGHT);

        synchronized (this) {
            for (Particle p : swarm.getParticles()) {
                double r1 = r.nextDouble();
                double r2 = r.nextDouble();

                // new x & y velocities
                int newX = (int) ((w * p.getxVel())
                        + (r1 * Constants.C1) * (p.getLocalBest().getxPos() - p.getxPos())
                        + (r2 * Constants.C2) * (gBest.getxPos() - p.getxPos()));

                int newY = (int) ((w * p.getyVel())
                        + (r1 * Constants.C1) * (p.getLocalBest().getyPos() - p.getyPos())
                        + (r2 * Constants.C2) * (gBest.getyPos() - p.getyPos()));

                // Limiting new velocities to range
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

                // Updating positions
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
            System.out.println("(" + p.getxPos() + "," + p.getyPos() + ") -> (" + p.getxVel() + "," + p.getyVel() + ") -> " + p.getTemparture() + " -> " + p.getFitness());
        }
    }

    private static void updateMapTemp(int range, double temp) {
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

            p.setLocalBest(p);

            swarm.addParticle(p);
        }

        evaluateTemp();
        // Initialize first particle as global best
        swarm.setGlobalBest(swarm.getParticles().get(0));
    }

    public static void main(String args[]) {
        try {
            fh = new FileHandler("PSOLogs.log");
            logger.addHandler(fh);
            logger.setUseParentHandlers(false);

            fh.setFormatter(new Formatter() {
                @Override
                public String format(LogRecord record) {
                    SimpleDateFormat logTime = new SimpleDateFormat("MM-dd-yyyy HH:mm:ss");
                    Calendar cal = new GregorianCalendar();
                    cal.setTimeInMillis(record.getMillis());
                    return " ["
                            + logTime.format(cal.getTime())
                            + "] "
                            + record.getMessage() + "\n";
                }
            });

        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("Please select:");
        Scanner scanner = new Scanner(System.in);

        int val = 0;

        System.out.println("1. PSO with default location (Forest Area)");
        System.out.println("2. PSO with manual location");
        val = scanner.nextInt();
        switch (val) {
            case 1:
                lat = Constants.LATITUDE;
                lon = Constants.LONGIUDE;
                new PSO();
                break;

            case 2:
                boolean incorrect = true;
                do {
                    System.out.println("Enter location latitude ");
                    lat = scanner.nextDouble();
                    System.out.println("Enter location longitude ");
                    lon = scanner.nextDouble();
                    if (lat < -90 || lat > 90 || lon < -180 || lon > 180) {
                        System.out.println("Please enter correct range for latitude and longitude");
                    } else {
                        incorrect = false;
                    }
                } while (incorrect);
                new PSO();
                break;

            default:
                System.out.println("Incorrect entry");
                break;
        }

    }

    @Override
    public void mouseClicked(MouseEvent e) {
        System.out.println("\n\nFire location updated to (" + e.getX() / Constants.MULTIPLICATION_FACTOR + "," + e.getY() / Constants.MULTIPLICATION_FACTOR + ")");
        logger.log(Level.INFO, "\n\nFire location updated to (" + e.getX() / Constants.MULTIPLICATION_FACTOR + "," + e.getY() / Constants.MULTIPLICATION_FACTOR + ")");

        startTime = 0;
        initializeTemp();

        fireX = e.getX() / Constants.MULTIPLICATION_FACTOR;
        fireY = e.getY() / Constants.MULTIPLICATION_FACTOR;

        iteration = 0;

        displayFirePointOnMap();

        clicked = true;

        psoDisplay();
    }

    @Override
    public void mousePressed(MouseEvent e) {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void mouseExited(MouseEvent e) {
        // throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
