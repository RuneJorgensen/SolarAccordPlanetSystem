/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package javafxapplication2;

import java.util.ArrayList;

import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.RotateTransition;
import javafx.animation.Timeline;

import javafx.collections.ObservableList;

import javafx.event.EventHandler;

import javafx.geometry.BoundingBox;

import javafx.scene.AmbientLight;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.PerspectiveCamera;
import javafx.scene.PointLight;
import javafx.scene.SceneAntialiasing;
import javafx.scene.SubScene;

import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;

import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Box;

import javafx.scene.shape.Sphere;

import javafx.scene.transform.Affine;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Transform;
import javafx.scene.transform.Translate;

import javafx.util.Duration;
import models.Planet;



/**
 * org.interactivemesh.jfx.sample3d.tuxcube.FXTuxCubeSubScene.java
 *
 * Version: 0.7.1
 * Date: 2013/10/31 
 * 
 * Author:
 * August Lammersdorf, InteractiveMesh e.K.
 * HauptstraÃŸe 28d, 85737 Ismaning
 * Germany / Munich Area
 * www.InteractiveMesh.com/org
 *
 * Please create your own implementation.
 * This source code is provided "AS IS", without warranty of any kind.
 * You are allowed to copy and use all lines you like of this source code
 * without any copyright notice,
 * but you may not modify, compile, or distribute this 'FXTuxCubeSubScene.java'.
 *
 */
final class FXTuxCubeSubScene {
    
    enum VP {
        Select("Select"),
        BOTTOM("Bottom"), FRONT("Front"), TOP("Top");
        VP(String listName) {
            this.listName = listName;
        }
        private String listName;
        String getListName() {
            return listName;
        }
    }

    private SubScene subScene = null;
    
    private Group viewingGroup = null; 
    private Affine viewingRotate = new Affine();  
    private Translate viewingTranslate = new Translate();
    private double startX = 0;
    private double startY = 0;
    
    private PerspectiveCamera perspectiveCamera = null;  
    
    private AmbientLight ambLight = null;
    private PointLight pointLight = null;
    
    private Group tuxCubeRotGroup = null;
    private Group tuxCubeCenterGroup = null;
    
    private Rotate tuxCubeRotate = null;
    private Timeline tuxCubeRotTimeline = null;
    
    private RotateTransition[] tuxRotTransAll = null;    
       
    private BoundingBox tuxCubeBinL = null;
    private double sceneDiameter =  0;
    
    final Group axisGroup = new Group();
    
    private ArrayList<Planet> planets;
    
    FXTuxCubeSubScene() {
        createBaseScene();
        createSubScene(800, 800, SceneAntialiasing.BALANCED);

    }
    
    private void createBaseScene() {
        
        
        
        //
        // Viewing : Camera & Light
        //
        
        // SubScene's camera
        perspectiveCamera = new PerspectiveCamera(true);
        perspectiveCamera.setVerticalFieldOfView(false);
        perspectiveCamera.setFarClip(250);
        perspectiveCamera.setNearClip(0.1);
        perspectiveCamera.setFieldOfView(44);
       
        // SubScene's lights
        pointLight = new PointLight(Color.WHITE);
        pointLight.setTranslateZ(-20000);
        
        ambLight = new AmbientLight(Color.color(0.3, 0.3, 0.3));
          
        // Viewing group: camera and headlight
        viewingGroup = new Group(perspectiveCamera, pointLight);
        viewingGroup.getTransforms().setAll(viewingRotate, viewingTranslate);
        
        //
        // Group hierarchy of the cube
        //
        
        // Centers the entire cube at (0,0,0)
        tuxCubeCenterGroup = new Group();

        // Cube rotation target
        tuxCubeRotGroup = new Group(tuxCubeCenterGroup);
        
        tuxCubeRotate = new Rotate(0, 0,0,0, Rotate.Y_AXIS);
        
        final KeyValue kv0 = new KeyValue(tuxCubeRotate.angleProperty(), 0, Interpolator.LINEAR);
        final KeyValue kv1 = new KeyValue(tuxCubeRotate.angleProperty(), 360, Interpolator.LINEAR);
        final KeyFrame kf0 = new KeyFrame(Duration.millis(0), kv0);
        final KeyFrame kf1 = new KeyFrame(Duration.millis(50000), kv1); // min speed, max duration

        tuxCubeRotTimeline = new Timeline();
        tuxCubeRotTimeline.setCycleCount(Timeline.INDEFINITE);        
        tuxCubeRotTimeline.getKeyFrames().setAll(kf0, kf1);
        
        tuxCubeRotGroup.getTransforms().setAll(tuxCubeRotate);  
        
        
    }
        
    private void createSubScene(final double width, final double height, final SceneAntialiasing sceneAA) {
        //
        // SubScene & Root 
        //
        final Group subSceneRoot  = new Group();

        subScene = new SubScene(subSceneRoot, width, height, true, sceneAA);
                       
        // otherwise subScene doesn't receive mouse events                  TODO bug ??   
        subScene.setFill(Color.TRANSPARENT);
        
        // Perspective camera
        subScene.setCamera(perspectiveCamera);
        
        // Add all to SubScene
        subSceneRoot.getChildren().addAll(tuxCubeRotGroup, viewingGroup, ambLight);
                
        // Navigator on SubScene
        
        final Rotate viewingRotX = new Rotate(0, 0,0,0, Rotate.X_AXIS);      
        final Rotate viewingRotY = new Rotate(0, 0,0,0, Rotate.Y_AXIS);        
        
        subScene.setOnMouseDragged(new EventHandler<MouseEvent>() {
            @Override public void handle(MouseEvent event) {
//System.out.println("OnMouseDragged " + event);
                if (event.getButton() == MouseButton.PRIMARY) {                    
                    viewingRotX.setAngle((startY - event.getSceneY())/10); 
                    viewingRotY.setAngle((event.getSceneX() - startX)/10);                                        
                    viewingRotate.append(viewingRotX.createConcatenation(viewingRotY));
                }
                else if (event.getButton() == MouseButton.SECONDARY) {
                    viewingTranslate.setX(viewingTranslate.getX() + (startX - event.getSceneX())/100);
                    viewingTranslate.setY(viewingTranslate.getY() + (startY - event.getSceneY())/100);
                }
                else if (event.getButton() == MouseButton.MIDDLE) {
                    viewingTranslate.setZ(viewingTranslate.getZ() + (event.getSceneY() - startY)/40);
                }
                     
                startX = event.getSceneX();
                startY = event.getSceneY();
           }
        });
        subScene.setOnScroll(new EventHandler<ScrollEvent>() {
            @Override public void handle(ScrollEvent event) {
//System.out.println("OnScroll event.getDeltaY() = " + event.getDeltaY());
                viewingTranslate.setZ(viewingTranslate.getZ() - event.getDeltaY()/40);
//System.out.println("OnScroll viewingTransZ.getZ = " + viewingTransZ.getZ());
            }
        });
        subScene.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override public void handle(MouseEvent event) {
                startX = event.getSceneX();
                startY = event.getSceneY();
//System.out.println("OnMousePressed = " + event);
            }
        });       
    }
   
    SubScene getSubScene() {
        return subScene;
    }
   
    SubScene exchangeSubScene(final SceneAntialiasing sceneAA) {
        
        // Clear current SubScene
        ((Group)subScene.getRoot()).getChildren().clear();
        subScene.setCamera(null);
        subScene.setOnMouseDragged(null);
        subScene.setOnScroll(null);
        subScene.setOnMousePressed(null);
        
        // Create and return a new SubScene
        createSubScene(subScene.getWidth(), subScene.getHeight(), sceneAA);
        
        return subScene;
    }

    // 
    void createTuxCubeOfDim() {
        
        // Tux : 13.744 triangles, 6 MeshViews
        
        // TuxBody :   3.856
        // TuxEyes :   1.056
        // TuxFeet :   4.640
        // TuxFront :    192
        // TuxMouth :  2.944
        // TuxPupils : 1.056
        
        //          #Tux             triangles  MeshViews
        // dim  1 :    1 * 13.744 =     13.744          6
        // dim  2 :    8 * 13.744 =    109.952         48
        // dim  3 :   27 * 13.744 =    371.088        162
        // dim  4 :   64 * 13.744 =    879.616        384
        // dim  5 :  125 * 13.744 =  1.718.000        750
        // dim  6 :  216 * 13.744 =  2.968.704      1.296
        // dim  7 :  343 * 13.744 =  4.714.192      2.058
        // dim  8 :  512 * 13.744 =  7.036.928      3.072
        // dim  9 :  729 * 13.744 = 10.019.376      4.374
        // dim 10 : 1000 * 13.744 = 13.744.000      6.000
        // dim 11 : 1331 * 13.744 = 18.293.264      7.986
        // dim 12 : 1728 * 13.744 = 23.749.632     10.368

        // Clear cube
        tuxCubeCenterGroup.getChildren().clear();
                            
        // Center Tux for rotation
        final double transZ = -0.01396;
        
        final int xDist = 2;
        final int yDist = 2;
        final int zDist = 2;
        
        long delay = 4;
        final long delayIncr = 4;
        
        // Appearances for body and front

        final int numTux = 3;
        final double maxTux = 10*10*10;
                
        int n = 0;
        
        float xTrans = 0;
        float yTrans = 0;
        float zTrans = 0;
        
        tuxRotTransAll = new RotateTransition[numTux];
        
        ObservableList<Node> planetCenterList = createPlanetarySystem(planets);
        //System.out.println("Number of planetSpheres: " + planetCenterList.size());
        
        tuxCubeBinL = (BoundingBox)tuxCubeCenterGroup.getBoundsInLocal();     

        sceneDiameter = Math.sqrt(Math.pow(tuxCubeBinL.getWidth(), 2) + Math.pow(tuxCubeBinL.getHeight(), 2) + Math.pow(tuxCubeBinL.getDepth(), 2));
        buildAxes();
    }
    
    ObservableList<Node> createPlanetarySystem(ArrayList<Planet> planets){
        ObservableList<Node> planetCenterList = tuxCubeCenterGroup.getChildren(); 
        
        planetCenterList.add(createPlanetGroup("Sun", Color.GOLD, Color.YELLOW, 0.19, 0, 0, 0));
        
        for(Planet planet : planets){
            planetCenterList.add(createPlanetGroup(planet.getName(), planet.getFirstColor(), planet.getSecondColor(), planet.getRadius(), planet.getxEclipAdjusted(0), planet.getyEclipAdjusted(0), planet.getzEclipAdjusted(0)));
            //System.out.println("Planet position: " + planet.getxEclipAdjusted(0)+ ", " + planet.getyEclipAdjusted(0) + ", " + planet.getzEclipAdjusted(0));
        }
        
        return planetCenterList;
    }
    
    Group createPlanetGroup(String planetName, Color diffuseColor, Color specularColor, double sphereRadius, double x, double y, double z){
        final double transZ = -0.01396;
        long delay = 4;
        final int numTux = 3;
        final double maxTux = 10*10*10;
        //Mercury
        Group planetGroup = new Group();
        planetGroup.setTranslateZ(transZ);
        
        final PhongMaterial sphereMaterial = new PhongMaterial();
        sphereMaterial.setDiffuseColor(diffuseColor);
        sphereMaterial.setSpecularColor(specularColor);
        Sphere planetSphere = new Sphere(sphereRadius);
        planetSphere.setMaterial(sphereMaterial);
        planetSphere.setId(planetName);
        
        planetGroup.getChildren().addAll(planetSphere);

        planetSphere.setOnMouseEntered(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {                
                FXTuxCube.setNameForPlanetInfoPane(planetName);
                FXTuxCube.showPlanetInfoPane();
            }
        });
        planetSphere.setOnMouseExited(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                FXTuxCube.hidePlanetInfoPane();
            }
        });
        
        Group planetRotationGroup = new Group(planetGroup); 
        
        RotateTransition mercuryRotateTransition = new RotateTransition();
        mercuryRotateTransition.setNode(planetRotationGroup);
        mercuryRotateTransition.setAxis(Rotate.Y_AXIS);
        mercuryRotateTransition.setDelay(Duration.millis(delay));
        mercuryRotateTransition.setDuration(Duration.millis(5000 - 2*1*(maxTux/numTux)));
        mercuryRotateTransition.setCycleCount(Timeline.INDEFINITE);
        mercuryRotateTransition.setAutoReverse(false); 
        mercuryRotateTransition.setInterpolator(Interpolator.LINEAR);
        mercuryRotateTransition.setByAngle(360);
        tuxRotTransAll[1] = mercuryRotateTransition;
        
        Group planetPositionGroup = new Group(planetRotationGroup);
        planetPositionGroup.setTranslateX(x);
        planetPositionGroup.setTranslateY(y);
        planetPositionGroup.setTranslateZ(z);
        
        return planetPositionGroup;
    }
    
    void setVantagePoint(VP vp) {
        
        Transform rotate = null;
         
        final double distance = distToSceneCenter(sceneDiameter/2);
        
        switch(vp) {
            case BOTTOM:
//                rotate = new Rotate(-180, Rotate.Z_AXIS);
                break;
            case FRONT:
//                rotate = new Rotate(90, Rotate.X_AXIS);
//                Rotate rotateX = new Rotate(90, Rotate.X_AXIS);
//                Rotate rotateY = new Rotate(90, Rotate.Y_AXIS);
//                rotate = rotateX.createConcatenation(rotateY);
                break;
            case TOP:
                Rotate rotateTopZ = new Rotate(90, Rotate.Z_AXIS);
//                Rotate rotateTopY = new Rotate(90, Rotate.Y_AXIS);
//                rotate = rotateTopZ.createConcatenation(rotateTopY);
                rotate = rotateTopZ;
               break;
        }
        if(rotate != null){viewingRotate.setToTransform(rotate);}
        
                
        viewingTranslate.setX(0);
        viewingTranslate.setY(0);
        viewingTranslate.setZ(-distance);
    }
    
    
    void stopCubeRotation() {
        tuxCubeRotTimeline.stop();
        tuxCubeRotate.setAngle(0);
    }
    // range: [20 ... 50 ... 80] 
    void setRotationSpeed(float speed) {   
        if (speed < 49f) {
            tuxCubeRotTimeline.play();
            tuxCubeRotTimeline.setRate(1 * (49 - speed));
        }
        else if (speed > 51f) {            
            tuxCubeRotTimeline.play();
            tuxCubeRotTimeline.setRate(-1 * (speed - 51)); // negative rate works only while Timeline is playing !!
        }
        else {
            tuxCubeRotTimeline.pause();
        }     
    }
               
    private double distToSceneCenter(double sceneRadius) {
        // Extra space
        final double borderFactor = 2.0;
        
        final double fov = perspectiveCamera.getFieldOfView();
        
        final double c3dWidth = subScene.getWidth();
        final double c3dHeight = subScene.getHeight();
        // Consider ratio of canvas' width and height
        double ratioFactor = 1.0;
        if (c3dWidth > c3dHeight) {
            ratioFactor = c3dWidth/c3dHeight;
        }
//System.out.println("sceneRadius       = " + sceneRadius);

        final double distToSceneCenter = borderFactor * ratioFactor * sceneRadius / Math.tan(Math.toRadians(fov/2));
//System.out.println("distToSceneCenter = " + distToSceneCenter);
        return distToSceneCenter;        
    }
    
    public Group getTuxCubeCenterGroup(){
        return tuxCubeCenterGroup;
    }
    
    private void buildAxes() {
        final PhongMaterial redMaterial = new PhongMaterial();
        redMaterial.setDiffuseColor(Color.DARKRED);
        redMaterial.setSpecularColor(Color.RED);
 
        final PhongMaterial greenMaterial = new PhongMaterial();
        greenMaterial.setDiffuseColor(Color.DARKGREEN);
        greenMaterial.setSpecularColor(Color.GREEN);
 
        final PhongMaterial blueMaterial = new PhongMaterial();
        blueMaterial.setDiffuseColor(Color.DARKBLUE);
        blueMaterial.setSpecularColor(Color.BLUE);
 
        final Box xAxis = new Box(250.0, 0.1, 0.1);
        final Box yAxis = new Box(0.1, 250.0, 0.1);
        final Box zAxis = new Box(0.1, 0.1, 250.0);
        
        xAxis.setMaterial(redMaterial);
        yAxis.setMaterial(greenMaterial);
        zAxis.setMaterial(blueMaterial);
 
        axisGroup.getChildren().addAll(xAxis, yAxis, zAxis);
        tuxCubeCenterGroup.getChildren().addAll(axisGroup);
    }
    
     public ArrayList<Planet> getPlanets() {
        return planets;
    }

    public void setPlanets(ArrayList<Planet> planets) {
        this.planets = planets;
    }
    
}
