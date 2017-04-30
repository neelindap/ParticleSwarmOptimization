/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package userInterface;

import business.Constants;
import business.Particle;
import business.Swarm;
import java.awt.Color;
import java.awt.Graphics;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import javax.swing.ImageIcon;
import javax.swing.JPanel;

/**
 *
 * @author indap.n
 */
public class AreaMap extends JPanel {

    int firePointX, firePointY;
    Swarm swarm;
    double[][] areaMap;

    public AreaMap(int firePointX, int firePointY, Swarm swarm, double[][] areaMap, double lat, double lon) {
        generateMapImage(lat, lon);
        this.firePointX = firePointX;
        this.firePointY = firePointY;
        this.swarm = swarm;
        this.areaMap = areaMap;
    }

    public void paint(Graphics g) {

//        g.setColor(Color.BLACK);
//        g.fillRect(0, 0, Constants.AREA_SIZE * 10, Constants.AREA_SIZE * 10);
        g.drawImage(new ImageIcon("Map.jpg").getImage(), 0, 0, null);
        g.drawImage(new ImageIcon("Legends.png").getImage(), Constants.MAP_DIMENSION - 190, Constants.MAP_DIMENSION - 130, null);

        // Paint fire position
        for (int i = 0; i < Constants.AREA_SIZE; i++) {
            for (int j = 0; j < Constants.AREA_SIZE; j++) {
                if (areaMap[i][j] > 500) {
                    if (areaMap[i][j] == 800) {
                        g.setColor(Color.RED);
                        g.fillOval(i * Constants.MULTIPLICATION_FACTOR, j * Constants.MULTIPLICATION_FACTOR, 10, 10);
                    } else if (areaMap[i][j] >= 700) {
                        g.setColor(Color.ORANGE);
                        g.fillOval(i * Constants.MULTIPLICATION_FACTOR, j * Constants.MULTIPLICATION_FACTOR, 10, 10);
                    } else {
                        g.setColor(Color.YELLOW);
                        g.fillOval(i * Constants.MULTIPLICATION_FACTOR, j * Constants.MULTIPLICATION_FACTOR, 10, 10);
                    }
                }
            }
        }
        
        // Paint Swarm particles
        for (Particle p : swarm.getParticles()) {
            g.setColor(Color.BLUE);
            g.fillOval(p.getxPos() * Constants.MULTIPLICATION_FACTOR, p.getyPos() * Constants.MULTIPLICATION_FACTOR, 10, 10);
        }
    }

    public static void generateMapImage(double lat, double lon) {
        try {
            String imageUrl = Constants.GOOGLE_MAPS_URL
                    + lat
                    + ","
                    + lon
                    + "&zoom=" + Constants.ZOOM
                    + "&size=" + Constants.MAP_DIMENSION
                    + "x"
                    + Constants.MAP_DIMENSION
                    + "&scale=" + Constants.SCALE
                    + "&maptype=" + Constants.MAP_TYPE
                    + "&key=" + Constants.MAP_API_KEY;

            String mapFile = "Map.jpg";

            URL url = new URL(imageUrl);
            InputStream is = url.openStream();
            OutputStream os = new FileOutputStream(mapFile);
            byte[] b = new byte[2048];
            int length;
            while ((length = is.read(b)) != -1) {
                os.write(b, 0, length);
            }
            is.close();
            os.close();
        } catch (IOException e) {
            System.out.println("System not connected to internet. Using default map.");
        }
    }
}
