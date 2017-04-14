package PSO;

import java.util.ArrayList;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author indap.n
 */
public class Swarm{
    
    private ArrayList<Particle> particles;
    private Particle gBest;

    public Swarm() {
        particles = new ArrayList<Particle>();
    }
    
    public void addParticle(Particle particle){
        particles.add(particle);
    }
    
    public ArrayList<Particle> getParticles(){
        return particles;
    }

    public Particle getgBest() {
        return gBest;
    }

    public void setgBest(Particle gBest) {
        this.gBest = gBest;
    }
    
}
