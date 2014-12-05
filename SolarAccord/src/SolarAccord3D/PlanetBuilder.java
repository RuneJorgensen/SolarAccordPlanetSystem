package SolarAccord3D;

import javafx.scene.paint.Color;
import models.Planet;

import java.util.ArrayList;

/**
 * Created by KR on 05-12-2014.
 */
public class PlanetBuilder {

    public static ArrayList<Planet> getPlanetsOnDay(int day){
     ArrayList<Planet> planets = new ArrayList<Planet>();

        String description = "Mercury is one of four terrestrial planets in the Solar System, and is a rocky body like " +
                "Earth. It is the smallest planet in the Solar System, with an equatorial radius of 2,439.7 kilometres.\n" +
                "Because it has almost no atmosphere to retain heat, Mercury's surface experiences the greatest " +
                "temperature variation of all the planets, ranging from −173 °C at night to 427 °C during the day at" +
                " some equatorial regions. The poles are constantly below -93 °C.\n";
        Planet mercury =  new Planet("Mercury", description,  Color.DARKGRAY, Color.AZURE, 0.05, 88, 48.3313, 0.0000324587, 7.0047, 0.00000005, 29.1241, 0.0000101444, 0.387098, 0,
                0.205635, 0.000000000559, 168.6562, 4.0923344368);
        mercury.setDay(day);
        planets.add(mercury);

        description = "Venus is one of the four terrestrial planets in the Solar System, meaning that, like Earth, " +
                "it is a rocky body. In size and mass, it is similar to Earth, and is often described as Earth's " +
                "\"sister\" or \"twin\". The diameter of Venus is 12,092 km (only 650 km less than Earth's) and its " +
                "mass is 81.5% of Earth's. Conditions on the Venusian surface differ radically from those on Earth, " +
                "owing to its dense carbon dioxide atmosphere. The mass of the atmosphere of Venus is 96.5% carbon " +
                "dioxide, with most of the remaining 3.5% being nitrogen. With a mean surface temperature of 462 °C, " +
                "Venus is by far the hottest planet in the Solar System. It has no carbon cycle to lock carbon back into " +
                "rocks and surface features, nor does it seem to have any organic life to absorb carbon in biomass. Venus " +
                "is shrouded by an opaque layer of highly reflective clouds of sulfuric acid, preventing its surface from " +
                "being seen from space in visible light. Venus may have possessed oceans in the past, but these would have" +
                " vaporized as the temperature rose due to a runaway greenhouse effect. The water has most probably " +
                "photodissociated, and, because of the lack of a planetary magnetic field, the free hydrogen has been " +
                "swept into interplanetary space by the solar wind. Venus's surface is a dry desertscape interspersed " +
                "with slab-like rocks and periodically refreshed by volcanism.";
        Planet venus = new Planet("Venus", description, Color.CORNSILK, Color.BISQUE, 0.06, 225, 76.679, 0.000024659, 3.3946, 0.0000000275, 54.8910, 0.0000138374,
                0.723330, 0.0, 0.006773, - 0.000000001302, 48.0052, 1.6021302244);
        venus.setDay(day);
        planets.add(venus);

        //Udregnet efter solens tal, s� passer m�ske ikke
        description = "The Earth, also known as the world, Terra, or Gaia, is the third planet from the Sun, the densest " +
                "planet in the Solar System, the largest of the Solar System's four terrestrial planets, and the only " +
                "celestial body known to accommodate life. The Earth's biodiversity has evolved over hundreds of million " +
                "years, expanding continually except when punctuated by mass extinctions. It is home to over eight million" +
                " species. There are over 7.2 billion humans who depend upon its biosphere and minerals. The Earth's human" +
                " population is divided among about two hundred independent states that interact through diplomacy, " +
                "conflict, travel, trade, and media.";
        Planet earth = new Planet("Earth", description, Color.BLUE, Color.BURLYWOOD, 0.075, 365, 0.0, 0.0, 0.0, 0.0, 282.940, 0.0000470935,
                1.000000, 0.0, 0.016709, 0.000000001151, 356.0470, 0.9856002585);
        earth.setDay(day);
        planets.add(earth);

        description = "";
        Planet mars = new Planet("Mars", description, Color.RED, Color.YELLOW, 0.06, 780, 49.5574, 0.0000211081, 1.8497, 0.0000000178, 286.5016, 0.0000292961,
                1.523688, 0.0, 0.093405, 0.0000000025162, 18.6021, 0.5240207766);
        mars.setDay(day);
        planets.add(mars);

        description = "";
        Planet jupiter = new Planet("Jupiter", description, Color.ANTIQUEWHITE, Color.BURLYWOOD, 0.11,4333, 100.4542, 0.0000276854, 1.3030, 0.0000001557, 273.8777, 0.0000164505,
                5.20256, 0.0, 0.048498, 0.000000004469, 19.8950, 0.0830853001);
        jupiter.setDay(day);
        planets.add(jupiter);

        description = "";
        Planet saturn = new Planet("Saturn", description, Color.BURLYWOOD, Color.DARKKHAKI, 0.098, 10760, 113.6634, 0.0000238980, 2.4886, 0.0000001081, 339.3939, 0.0000297661,
                9.55475, 0.0, 0.055546, -0.000000009499, 316.9670, 0.0334442282);
        saturn.setDay(day);
        planets.add(saturn);

        description = "";
        Planet ceres = new Planet("Ceres", description, Color.CADETBLUE, Color.DARKKHAKI, 0.045, 1681, 80.3276, 0.0000011081, 10.593, 0.0000000108, 142.2921, 0.0000002961,
                2.7668, 0.0, 0.095797, 0.0000000005162, 10.557, 0.2189907766);
        ceres.setDay(day);
        planets.add(ceres);

        description = "";
        Planet vesta = new Planet("Vesta", description, Color.DARKGRAY, Color.BLACK, 0.045, 1325, 103.91, 0.0000011081, 7.134, 0.0000000108, 149.84, 0.0000002961,
                2.362, 0.0, 0.008862, 0.0000000005162, 307.80, 0.2752507766);
        vesta.setDay(day);
        planets.add(vesta);

        description = "";
        Planet pallas = new Planet("Pallas", description, Color.LIGHTSTEELBLUE, Color.GRAY, 0.045, 1681, 233.12, 0.0000011081, 34.841, 0.0000000108, 110.15, 0.0000002961,
                2.772, 0.0, 0.231, 0.0000000005162, 96.15, 0.2189907766);
        pallas.setDay(day);
        planets.add(pallas);

        description = "";
        Planet hygiea = new Planet("Hygiea", description, Color.DARKGRAY, Color.BLACK, 0.045, 2030, 333.45, 0.0000011081, 3.842, 0.0000000108, 313.19, 0.0000002961,
                3.139, 0.0, 0.197, 0.0000000005162, 197.96, 0.2189907766);
        hygiea.setDay(day);
        planets.add(hygiea);

//        Planet uranus = new Planet("Uranus", 30688, 74.0005, 0.000013978, 0.7733, 0.000000019, 96.6612, 0.000030565,
//        		19.18171, -0.0000000155, 0.047318, 0.00000000745, 142.5905, 0.011725806);
//        planets.add(uranus);
//
//        Planet neptune =  new Planet("Neptune", 60000, 131.7806, 0.000030173, 1.7700, 0.000000255, 272.8461, 0.000006027, 30.05826, 0.00000003313,
//        		0.008606, 0.00000000215, 260.2471, 0.005995147);
//        planets.add(neptune);
//
//	    Planet pluto =  new Planet("Pluto", 90465, 131.7806, 0.000030173, 1.7700, 0.000000255, 96.6612, 0.000030565, 50, 0.00000003313,
//	    		0.018606, 0.00000000215, 60.2471, 0.005995147);
//	    planets.add(pluto);
    return planets;
    }
}
