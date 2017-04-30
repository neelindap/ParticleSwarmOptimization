/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package business;

/**
 *
 * @author indap.n
 */
public class Constants {
    
    //General Constants
    public static int THREAD_SLEEP_TIME = 300;
    
    //Swarm Constants
    public static double MAX_TEMP = 800d;
    public static double TEMP_DIFF = 50d;
    public static int SWARM_SIZE = 30;
    public static int MAX_ITERATION = 1000;
    public static int AREA_SIZE = 50;
    public static double C1 = 2.0;
    public static double C2 = 2.0;
    public static double INERTIA_WEIGHT = 1.0;
    public static double MAX_V_CHANGE = 4.0;
    
    // Maps Constants
    public static int MULTIPLICATION_FACTOR = 10;
    public static String GOOGLE_MAPS_URL = "https://maps.googleapis.com/maps/api/staticmap?center=";
    public static double LATITUDE = 38.1573286;
    public static double LONGIUDE = -119.6259207;
    public static int ZOOM = 11;
    public static int MAP_DIMENSION = 500;
    public static int SCALE = 1;
    public static String MAP_TYPE = "roadmap";
    public static String MAP_API_KEY = ""; // Google Maps APIKey
    
    //Email Constants - To be updated
    public static String SMTP_HOST = "";
    public static int PORT = ;
    public static String USER = "";
    public static String PASSWORD = "";
  
    public static String TO_USER ="";  
    public static String EMAIL_SUB = "";

}
