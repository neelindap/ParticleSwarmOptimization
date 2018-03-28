# ParticleSwarmOptimization

Wildfire problem is headlined by the loss of human lives and homes, but it applies generally to any adverse effects of unplanned fires, on a wide range of environmental, social, and economic assets. The problem is complex and contingent, requiring continual attention to the changing circumstances of stakeholders, landscapes, and ecosystems; it occurs at a variety of temporal and spatial scales.
The terrain and vegetation makes it difficult for it to be accessible by humans. This delayed response and at times impossible response increases the damages.
The use of robots to detect and extinguish fires can help with this problem.
Using the Particle Swarm Optimization concept, the overall goal can reached quicker and more efficiently. Small individual drones are much cheaper and efficient and also the use of multiple drones for in swarm ensures that failure of one drone doesn't hamper the search process.

Solution:
Using, PSO algorithm we can devise a method for drones to detect and control wildfires. Each drone is fitted with a temperature sensing device which can measure the temperature of its location.
A wildfire usually warms the air to 800 °C (1,470 °F). With this as a threshold value, each drone can define the probability of fire at its current location.
At each iteration, a drone updates its location based upon following 3 parameters:
- Current velocity multiplied by inertia weight
- Random number multiplied by distance between the drone’s current position and its best position (local best)
- Random number multiplied by distance between the drone’s current position and swarm’s best position (global best)
These iterations run till the drones reach the most probable place of fire.

Once the location of fire is detected, systems send a notification to the local fire-department about the presence of file and its location. Extinguishing of fires can then be performed using drones fitted with water spraying capabilities.
