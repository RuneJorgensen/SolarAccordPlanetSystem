/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package javafxapplication2;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import javafx.animation.TranslateTransition;
import javafx.collections.ObservableList;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.shape.Sphere;
import javafx.util.Duration;
import javax.swing.Timer;
import models.Planet;

/**
 *
 * @author KR
 */
public class Physics {
    
    final static int addConstantX = 0;
    final static int addConstantY = 0;
     final static int addConstantZ = 0;

    public static void movePlanets(int startDay, int endDay, ObservableList<Node> planetCenterList, ArrayList<Planet> planets){
        boolean timeMovesForward = true; 
        if(startDay > endDay){
                timeMovesForward = false;
        }
        int startDayTemp = startDay;
        int endDayTemp = endDay;
        
        ArrayList<ArrayList> dayList = new ArrayList<>();
        int planetNumber = 0;
        for(Node planetNode : planetCenterList){  
            
            Group planetPositionGroup = (Group) planetNode;
            if (planetPositionGroup.getChildren().get(0) instanceof Group){
                Group planetRotationGroup = (Group) planetPositionGroup.getChildren().get(0);
                Group planetSphereGroup = (Group) planetRotationGroup.getChildren().get(0);

                startDayTemp = startDay;
                endDayTemp = endDay;

                Sphere sphere = (Sphere) planetSphereGroup.getChildren().get(0);
                Planet planet = getPlanetById(sphere.getId(), planets);

                if(planet != null){//The sun should not be moved, but is still the first entry in planetCenterList

                    int dayIndex = 0;
                    while(Math.abs(startDayTemp - endDayTemp) > 0){
                        TranslateTransition trans = new TranslateTransition();
                        trans.setNode(planetPositionGroup);
                        trans.setFromX(planet.getxEclipAdjusted(addConstantX));
                        trans.setFromY(planet.getyEclipAdjusted(addConstantY));
                        trans.setFromZ(planet.getzEclipAdjusted(addConstantZ));
                        planet.setDay(startDayTemp);
                        System.out.println("Planet: " + planet.getName() + ", Day: " + startDayTemp + ", "
                        + planet.getxEclipAdjusted(addConstantX) + ", " 
                        + planet.getyEclipAdjusted(addConstantY) + ", " 
                        + planet.getzEclipAdjusted(addConstantZ));

                        trans.setToX(planet.getxEclipAdjusted(addConstantX));
                        trans.setToY(planet.getyEclipAdjusted(addConstantY));
                        trans.setToZ(planet.getzEclipAdjusted(addConstantZ));

                        trans.setDuration(Duration.seconds(1));
                        System.out.println("check " + planetNumber);
                        if(planetNumber == 0){
                            ArrayList<TranslateTransition> transList = new ArrayList<TranslateTransition>();
                            transList.add(trans);
                            dayList.add(transList);
                        }else {
                          ArrayList<TranslateTransition> transList = dayList.get(dayIndex);
                          transList.add(planetNumber, trans);
                        }

                        if(timeMovesForward){
                                startDayTemp++;
                        }
                        else{
                                startDayTemp--;
                        }	
                        System.out.println(planet.getName() + ": day" + dayIndex);
                        dayIndex++;  
                    }
                    planetNumber++;
                }
            }  
        }
        System.out.println(dayList.size());
        int aggregateDays = (int) Math.round(dayList.size() / 80); //No reason to run through all days if there are many days 
        
        int tempDelay = (int)Math.round(500 / dayList.size());
        int delay = tempDelay < 60 ? 60 : tempDelay; //milliseconds //If under 60 thread exceptions occur      
        System.out.println("Delay" + delay);
        ActionListener taskPerformer = new ActionListener() {
            int daySize = 0;
            
            @Override
            public void actionPerformed(ActionEvent evt) {
                if(daySize < dayList.size()){
                  ArrayList<TranslateTransition> day = dayList.get(daySize);
                    
                  for(TranslateTransition transition : day){
                        System.out.println("Planet ++");
                        transition.setDuration(Duration.millis(delay));
                        transition.play();
                        
                    }
                  if(aggregateDays > 1 && aggregateDays + daySize < dayList.size()){
                      daySize += aggregateDays;
                      System.out.println("Days + " + aggregateDays);
                  }
                  else {
                    System.out.println("Days + 1");
                    daySize++;  
                  }
                  
                }
                else {
                    System.out.println("Run transitions now");
                    ((Timer) evt.getSource()).stop();
                }
            }
        };
        new Timer(delay, taskPerformer).start();
    }

    private static Node getChildById(String id, Group root){
        Node resultNode = null;
        for(Node node : root.getChildren()){
                if(node.getId() != null && node.getId().equals(id)){
                        resultNode = node;
                        break;
                }
        }
        return resultNode;
    }

    private static Planet getPlanetById(String id, ArrayList<Planet> planets){
        Planet planet = null;

        for(Planet p : planets){
                if(p.getName().equals(id)){
                        planet = p;
                        break;
                }
        }		
        return planet;		
    }
}
