/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Animation;

import PSO.Constants;
import PSO.Particle;
import PSO.Swarm;
import java.awt.Color;
import java.awt.Graphics;
import javax.swing.JPanel;
import javax.swing.Timer;

/**
 *
 * @author indap.n
 */
public class AreaMap extends JPanel{

    Timer t;
    int firePointX,firePointY;
    Swarm swarm;
    double[][] areaMap;

    public AreaMap(int firePointX, int firePointY, Swarm swarm, double[][] areaMap) {
        this.firePointX = firePointX;
        this.firePointY = firePointY;
        this.swarm = swarm;
        this.areaMap = areaMap;
        //t = new Timer(5, this);
        //t.start();
    }

    public void paint(Graphics g) {
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, 500, 500);
        
        for(int  i=0; i< Constants.AREA_SIZE; i++){
            for(int  j=0; j< Constants.AREA_SIZE; j++){
                if(areaMap[i][j] > 500)
                {
                    if(areaMap[i][j] == 800){
                        g.setColor(Color.RED);
                        g.fillOval(i*10, j*10, 10, 10);
                    } else if(areaMap[i][j] >= 700){
                        g.setColor(Color.ORANGE);
                        g.fillOval(i*10, j*10, 10, 10);
                    }
                    else{
                        g.setColor(Color.YELLOW);
                        g.fillOval(i*10, j*10, 10, 10);
                    }
                }
            }
        }
//        System.out.println("Animation.AreaMap.paint()"+swarm);
        for(Particle p : swarm.getParticles()){
            g.setColor(Color.GREEN);
            g.fillOval(p.getxPos()*10, p.getyPos()*10, 10, 10);
        }
        //g.fillOval(x1, y1, 20, 20);
    }

    /*@Override
    public void actionPerformed(ActionEvent e) {
      x++;
        y++;
        x1--;
        y1++;
        
        if (x > 300 && y > 300) {
            x = 0;
            y = 0;
        }
        if (x1<150 && y1> 300) {
            x1 = 350;
            y1 = 10;
        }

        repaint(); 
    }*/

}
