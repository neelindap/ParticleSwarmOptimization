package business;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author indap.n
 */

// Fitness = Probability that the location the particle is on has fire

public class Particle {

    private int xPos;
    private int yPos;
    private double xVel;
    private double yVel;
    private Double temparture;
    private Particle localBest;

    public int getxPos() {
        return xPos;
    }

    public void setxPos(int xPos) {
        this.xPos = xPos;
    }

    public int getyPos() {
        return yPos;
    }

    public void setyPos(int yPos) {
        this.yPos = yPos;
    }

    public double getxVel() {
        return xVel;
    }

    public void setxVel(double xVel) {
        this.xVel = xVel;
    }

    public double getyVel() {
        return yVel;
    }

    public void setyVel(double yVel) {
        this.yVel = yVel;
    }

    public Double getTemparture() {
        return temparture;
    }

    public void setTemparture(Double temparture) {
        this.temparture = temparture;
    }

    public Particle getLocalBest() {
        return localBest;
    }

    public void setLocalBest(Particle localBest) {
        this.localBest = localBest;
    }
    
    public double getFitness(){
        return (temparture/Constants.MAX_TEMP)*100.0/100.0;
    }
    
}
