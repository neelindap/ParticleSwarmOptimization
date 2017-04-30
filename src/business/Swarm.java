package business;

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
    private Particle globalBest;

    public Swarm() {
        particles = new ArrayList<>();
    }
    
    public void addParticle(Particle particle){
        particles.add(particle);
    }
    
    public ArrayList<Particle> getParticles(){
        return particles;
    }

    public Particle getGlobalBest() {
        return globalBest;
    }

    public void setGlobalBest(Particle globalBest) {
        this.globalBest = globalBest;
    }
    
}
