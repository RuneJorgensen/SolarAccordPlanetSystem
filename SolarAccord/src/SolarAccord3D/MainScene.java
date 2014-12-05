/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SolarAccord3D;


import java.text.NumberFormat;
import java.time.LocalDate;
import java.util.ArrayList;

import javafx.animation.AnimationTimer;

import javafx.application.Application;
import static javafx.application.Application.launch;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;

import javafx.geometry.HPos;
import javafx.geometry.Rectangle2D;
import javafx.geometry.VPos;

import javafx.scene.*;

import javafx.scene.control.ComboBox;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.RadioMenuItem;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.control.Slider;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.Tooltip;

import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;

import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.RowConstraints;

import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.RadialGradient;
import javafx.scene.paint.Stop;

import javafx.scene.shape.DrawMode;
import javafx.scene.shape.Rectangle;

import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import javafx.util.Callback;

import SolarAccord3D.FXSubScene.VP;
import models.Planet;

//import com.sun.javafx.perf.PerformanceTracker;

/**
 * org.interactivemesh.jfx.sample3d.tuxcube.FXTuxCube.java
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
 * but you may not modify, compile, or distribute this 'FXTuxCube.java'.
 *
 */
public final class MainScene extends Application {

    /**
     * The main() method is ignored in correctly deployed JavaFX application.
     * main() serves only as fallback in case the application can not be
     * launched through deployment artifacts, e.g., in IDEs with limited FX
     * support. NetBeans ignores main().
     *
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
    private SubScene subScene = null;
    
    private long lastTime = 0L;
    private int frameCounter = 0;
    private int elapsedFrames = 100;
    
    private AnimationTimer fpsTimer = null;
    private HUDLabel fpsTitleLabel = null;
    private HUDLabel fpsLabel = null;
    private HUDLabel mpfTitleLabel = null;
    private HUDLabel mpfLabel = null;
    
    private boolean isUpdateFPS = false;
    private boolean isTuxRotating = false;
    private boolean isCubeRotating = false;          
    private boolean isMouseDragged = false;
    
    private DrawMode drawMode = DrawMode.FILL;
    
    private Background blackBG = null;
    
    private int gap = 0;
    private int border = 0;
    
    private Font titleFont = null;
    private Font textFont = null;
    private Font cellFont = null;
    
    private NumberFormat numFormat = null;   
    
    private Physics physics;
    
    LocalDate date = LocalDate.of(2400, 1, 1);
    DatePicker datePicker = new DatePicker(date);
    private int day = 146102;
    ArrayList<Planet> planets = new ArrayList<Planet>();
    private static GridPane planetInfoPane = new GridPane();

 
    @Override
    public void start(Stage stage) {
        planets = PlanetBuilder.getPlanetsOnDay(day);
        final Rectangle2D screenRect = Screen.getPrimary().getBounds();
        final double screenWidth = screenRect.getWidth();
        final double screenHeight = screenRect.getHeight();

        // screenHeight > 1200      (1440/27', 1600/30')
        int cellFontSize = 16;
        int textFontSize = 20;
        int titleFontSize = 38;
        gap = 6;
        border = 50;

        // 900 < screenHeight  <= 1080/23'
        if (screenHeight <= 1080) {
            cellFontSize = 14;
            textFontSize = 16;
            titleFontSize = 30;
            gap = 4;
            border = 30;
        }
        // 1080 < screenHeight <= 1200/24'
        else if (screenHeight <= 1200) {
            cellFontSize = 14;
            textFontSize = 18;
            titleFontSize = 34;
            gap = 5;
            border = 40;
        }
        
        final String fontFamily = "Dialog";

        titleFont = Font.font(fontFamily, FontWeight.NORMAL, titleFontSize);
        textFont = Font.font(fontFamily, FontWeight.NORMAL, textFontSize);
        cellFont = Font.font(fontFamily, FontWeight.NORMAL, cellFontSize);

        numFormat = NumberFormat.getIntegerInstance();
        numFormat.setGroupingUsed(true);
                
        //
        // 3D subscene
        //
        final FXSubScene tuxCubeSubScene = new FXSubScene();        
        
        subScene = tuxCubeSubScene.getSubScene();
        if (subScene == null) {
            exit();
        }
        tuxCubeSubScene.setPlanets(planets);
        tuxCubeSubScene.createTuxCubeOfDim();
         
        //
        // Title
        //
        
        final HUDLabel titleLeftLabel = new HUDLabel("Solar System", titleFont); 
        
        // FPS - frames per second
           
        fpsTitleLabel = new HUDLabel("F P S");
        fpsTitleLabel.setTooltip(new Tooltip("frames per second"));
        fpsLabel = new HUDLabel("0");
        fpsLabel.setTooltip(new Tooltip("frames per second"));
        
        // MPF - milliseconds per frame
           
        mpfTitleLabel = new HUDLabel("M P F");
        mpfTitleLabel.setTooltip(new Tooltip("milliseconds per frame"));
        mpfLabel = new HUDLabel("0");
        mpfLabel.setTooltip(new Tooltip("milliseconds per frame"));
                
        // Tuxes/Shape3Ds/triangles
        
        final HUDLabel tuxesLabel = new HUDLabel("Tuxes");
        tuxesLabel.setTooltip(new Tooltip("number of Tux models and RotateTransitions"));
        final HUDLabel shape3dLabel = new HUDLabel("Shape3Ds");
        shape3dLabel.setTooltip(new Tooltip("number of Shape3D nodes"));
        final HUDLabel triangleLabel = new HUDLabel("Triangles");
        triangleLabel.setTooltip(new Tooltip("number of triangles"));
        
        // Set initial output values for autosizing
        final HUDLabel numTuxesLabel = new HUDLabel(numFormat.format(27));
        numTuxesLabel.setTooltip(new Tooltip("number of Tux models and RotateTransitions"));
        final HUDLabel numShape3dLabel = new HUDLabel(numFormat.format(162));
        numShape3dLabel.setTooltip(new Tooltip("number of Shape3D nodes"));
        final HUDLabel numTriaLabel = new HUDLabel(numFormat.format(371088));
        numTriaLabel.setTooltip(new Tooltip("number of triangles"));
       
        // Size of FXCanvas3D
        
        final HUDLabel heightLabel = new HUDLabel("Height");
        heightLabel.setTooltip(new Tooltip("height of 3D SubScene"));
        final HUDLabel pixHeightLabel = new HUDLabel("0");        
        pixHeightLabel.setTooltip(new Tooltip("height of 3D SubScene"));
        final HUDLabel widthLabel = new HUDLabel("Width");
        widthLabel.setTooltip(new Tooltip("width of 3D SubScene"));
        final HUDLabel pixWidthLabel = new HUDLabel("0");        
        pixWidthLabel.setTooltip(new Tooltip("width of 3D SubScene"));

        // Collect all outputs 
        
        final Rectangle gap1 = new Rectangle(gap, gap, Color.TRANSPARENT);
        final Rectangle gap2 = new Rectangle(gap, gap, Color.TRANSPARENT);
        
        final GridPane outputPane = new GridPane();
        outputPane.setHgap(10);
        outputPane.setVgap(0);
        outputPane.setGridLinesVisible(false);
        
        outputPane.add(fpsLabel, 0, 0);
        outputPane.add(fpsTitleLabel, 1, 0);
        outputPane.add(mpfLabel, 0, 1);
        outputPane.add(mpfTitleLabel, 1, 1);       
        outputPane.add(gap1, 0, 2);       
        outputPane.add(numTuxesLabel, 0, 3);
        outputPane.add(tuxesLabel, 1, 3);
        outputPane.add(numShape3dLabel, 0, 4);
        outputPane.add(shape3dLabel, 1, 4);
        outputPane.add(numTriaLabel, 0, 5);
        outputPane.add(triangleLabel, 1, 5);
        outputPane.add(gap2, 0, 6);
        outputPane.add(pixWidthLabel, 0, 7);
        outputPane.add(widthLabel, 1, 7);
        outputPane.add(pixHeightLabel, 0, 8);
        outputPane.add(heightLabel, 1, 8);
        
        final ColumnConstraints leftColumn = new ColumnConstraints();
        leftColumn.setHalignment(HPos.RIGHT);
        
        final ColumnConstraints rightColumn = new ColumnConstraints();
        rightColumn.setHalignment(HPos.LEFT);
       
        outputPane.getColumnConstraints().addAll(leftColumn, rightColumn);
        
        //
        // Controls
        //
        
      final ObservableList<Number> nums = FXCollections.<Number>observableArrayList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12);
        
    // Create the DatePicker.		
    datePicker.setShowWeekNumbers(false);

    day = translateDateToDay(date);
    // Add some action
    datePicker.setOnAction(new EventHandler<ActionEvent>(){
        @Override
        public void handle(ActionEvent event) {
            // TODO Auto-generated method stub
            int startDay = day;
            date = datePicker.getValue();
            day = translateDateToDay(date);
            physics.movePlanets(startDay, day, tuxCubeSubScene.getTuxCubeCenterGroup().getChildren(), planets); 
            //System.out.println("Selected date: " + date + ", day; " + day);
        }
    });
        
        // Viewpoints   
        
        final HUDLabel vpTitleLabel = new HUDLabel("Viewpoint");
        vpTitleLabel.setTooltip(new Tooltip("select viewpoint"));
        
        // Prompt text workaround !!!!!!!!!!!!!!!!!!
        final ComboBox<VP> vpCombo = new ComboBox<VP>();
        vpCombo.setTooltip(new Tooltip("select viewpoint"));
        vpCombo.getItems().addAll(VP.FRONT, VP.TOP); // DO NOT add VP.Select !!
        // Pre-select the prompt text item
        vpCombo.setValue(VP.Select);       
        vpCombo.valueProperty().addListener(new ChangeListener<VP>() {
            @Override public void changed(ObservableValue<? extends VP> ov, VP old_val, VP new_val) {
                // Ignore the prompt text item VP.Select
                if (new_val != null && new_val != VP.Select) {
                    tuxCubeSubScene.setVantagePoint(new_val);
                    // Select the prompt text item
//                    vpCombo.setValue(VP.Select);
                }
            }
        });            
        vpCombo.setButtonCell(new ListCell<VP>() {
            {
                this.setFont(cellFont);
            }
            @Override protected void updateItem(VP item, boolean empty) {
                // calling super here is very important - don't skip this!
                super.updateItem(item, empty);                                    
                if (item != null) {
                    this.setText(item.getListName());
                }
            }
        });       
        vpCombo.setCellFactory(new Callback<ListView<VP>, ListCell<VP>>() {
            @Override public ListCell<VP> call(ListView<VP> p) {
                return new ListCell<VP>() {
                    {
                        this.setFont(cellFont);
                    }
                    @Override protected void updateItem(VP item, boolean empty) {
                        // calling super here is very important - don't skip this!
                        super.updateItem(item, empty);
                        if (item != null) {
                            this.setText(item.getListName());
                        }
                    }
                };
            }
        });


        //planet information
        planetInfoPane.setId("InfoPane");
        planetInfoPane.setHgap(1);
        planetInfoPane.setVgap(1);
        planetInfoPane.setGridLinesVisible(true);
        planetInfoPane.setStyle("-fx-background-color: rgba(0,168,355,0.2);");
//        planetInfoPane.setStyle("-fx-font-family: Courier New;");
//        ColumnConstraints column1 = new ColumnConstraints(); //Set width
//        ColumnConstraints column2 = new ColumnConstraints(); //Set width
//        column1.setMaxWidth(10);
//        column2.setMaxWidth(10);
//        planetInfoPane.getColumnConstraints().addAll(column1, column2);
        planetInfoPane.setMaxWidth(300);

        //Planet name
        Text planetName = new Text("Sun");
        planetName.setId("PlanetName");
        planetName.setFill(Color.ALICEBLUE);
        planetInfoPane.add(planetName, 0, 0);

        //Planet description
        Text planetDescription = new Text("Description");
        planetDescription.setId("PlanetDescription");
        planetDescription.setFill(Color.ALICEBLUE);
        planetInfoPane.add(planetDescription, 0, 1);

        
        // Collect all controls
        
        final GridPane controlPane = new GridPane();
        controlPane.setHgap(10);
        controlPane.setVgap(4);
        controlPane.setGridLinesVisible(false);

        controlPane.add(vpTitleLabel, 1, 0);
        controlPane.add(vpCombo, 1, 1);

        controlPane.add(datePicker, 3, 0);
        
        for (int i=0; i < 4; i++) {
            final ColumnConstraints cC = new ColumnConstraints();
            cC.setHalignment(HPos.CENTER);
            controlPane.getColumnConstraints().add(cC);
        }
       
        final RowConstraints topRow = new RowConstraints();
        topRow.setValignment(VPos.CENTER);
        
        final RowConstraints botRow = new RowConstraints();
        botRow.setValignment(VPos.CENTER);
       
        controlPane.getRowConstraints().addAll(topRow, botRow);


        final EventHandler onMouseEnteredHandler = new EventHandler<MouseEvent>() {
            @Override public void handle(MouseEvent e) {
                controlPane.setOpacity(1.0);
            }
        };
        final EventHandler onMouseExitedHandler = new EventHandler<MouseEvent>() {
            @Override public void handle(MouseEvent e) {
                controlPane.setOpacity(0.5);
            }
        };
                
        controlPane.setOpacity(0.5);
        controlPane.setOnMouseEntered(onMouseEnteredHandler);    
        controlPane.setOnMouseExited(onMouseExitedHandler);
       
        // Layout      
        
        // best smooth resizing if 3D-subScene is re-sized in Scene's ChangeListener
        // and HUDs in layeredPane.layoutChildren()
        
        // best background painting and mouse event handling
        // if subScene.setFill(Color.TRANSPARENT)
        // and layeredPane.setBackground(...). Don't use Scene.setFill.
        
                
        final double size = Math.min(screenWidth*0.8, screenHeight*0.8);
        subScene.setWidth(size);
        subScene.setHeight(size);
        
        final Group rootGroup = new Group();
        final Scene scene = new Scene(rootGroup, size, size, true);               
        final ChangeListener sceneBoundsListener = new ChangeListener() {
            @Override
            public void changed(ObservableValue observable, Object oldXY, Object newXY) {
                subScene.setWidth(scene.getWidth());
                subScene.setHeight(scene.getHeight());                            
             }
        };        
        scene.widthProperty().addListener(sceneBoundsListener);
        scene.heightProperty().addListener(sceneBoundsListener);
        
        final Pane layeredPane = new Pane() {
            @Override protected void layoutChildren() {
                
                double width = scene.getWidth();
                double height = scene.getHeight();

                titleLeftLabel.autosize();
                titleLeftLabel.relocate(width-titleLeftLabel.getWidth()-border, border);
                
                planetInfoPane.autosize();
                planetInfoPane.relocate(5, 5);
                
                controlPane.autosize();  
                controlPane.relocate(border, height - controlPane.getHeight() - border);  
                
                outputPane.autosize();   
                outputPane.relocate(width - border - outputPane.getWidth(), 
                                    height - outputPane.getHeight() - border);
                
                pixWidthLabel.setText(numFormat.format((int)width));
                pixHeightLabel.setText(numFormat.format((int)height));
            }
        };
        layeredPane.getChildren().addAll(subScene, titleLeftLabel, planetInfoPane, controlPane, outputPane);
         
        // Backgrounds 
        
        final Stop[] stopsRG = new Stop[]{new Stop(0.2, Color.BLACK),
                                          new Stop(1.0, Color.BLACK)};
        final RadialGradient rg = new RadialGradient(0, 0, 0.5, 0.5, 1, true, CycleMethod.NO_CYCLE, stopsRG);
        blackBG = new Background(new BackgroundFill(rg, null, null));     
        
        layeredPane.setBackground(blackBG); // initial background
        
        rootGroup.getChildren().add(layeredPane);
        
        //
        // ContextMenu 
        //
        // Scene anti-aliasing mode
        final Menu menuSceneAA = new Menu("Scene anti-aliasing");
        final ToggleGroup toggleGroupSceneAA = new ToggleGroup();
        
        final RadioMenuItem itemBalancedAA = new RadioMenuItem("BALANCED");
        itemBalancedAA.setToggleGroup(toggleGroupSceneAA);
        itemBalancedAA.setSelected(true);
        
        final RadioMenuItem itemDisabledAA = new RadioMenuItem("DISABLED");
        itemDisabledAA.setToggleGroup(toggleGroupSceneAA);
        
        final EventHandler sceneAAHandler = new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent e) {
                
                SceneAntialiasing sceneAA = SceneAntialiasing.DISABLED;
                if (toggleGroupSceneAA.getSelectedToggle() == itemBalancedAA) {
                    sceneAA = SceneAntialiasing.BALANCED;
                }
                
                // Exchange SubScene

                subScene = tuxCubeSubScene.exchangeSubScene(sceneAA);
                layeredPane.getChildren().set(0, subScene);
            }
        };
        itemBalancedAA.setOnAction(sceneAAHandler);
        itemDisabledAA.setOnAction(sceneAAHandler);
        
        menuSceneAA.getItems().addAll(itemBalancedAA, itemDisabledAA);
        
        // Exit
        final MenuItem itemExit = new MenuItem("Exit");
        itemExit.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent e) {
                exit();
            }
        }); 
                
        final ContextMenu contextMenu = new ContextMenu();
        contextMenu.setHideOnEscape(true);       
        contextMenu.getItems().addAll(menuSceneAA,
                                      new SeparatorMenuItem(), 
                                      itemExit);   
        
        layeredPane.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override public void handle(MouseEvent e) {
                if (contextMenu.isShowing()) {
                    contextMenu.hide();
                }
                if (e.getButton() == MouseButton.SECONDARY && !isMouseDragged) {
                    contextMenu.show(layeredPane, e.getScreenX()+2, e.getScreenY()+2);
                }
                
                isMouseDragged = false;
               
                checkFPS();
            }
        });
        layeredPane.setOnDragDetected(new EventHandler<MouseEvent>() {
            @Override public void handle(MouseEvent e) { 
                isMouseDragged = true;
                
                checkFPS();
            }
        });         
        
        //
        // Stage
        //
        
        stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override public void handle(WindowEvent event) {
                exit();
            }
        });
        stage.setScene(scene);    
        stage.setTitle("InteractiveMesh : FXTuxCube 0.7.1");
       
        stage.show();

        // Initial states
        
        lastTime = System.nanoTime();
        tuxCubeSubScene.createTuxCubeOfDim();
        tuxCubeSubScene.setVantagePoint(VP.TOP);

        //
        fpsTimer = new AnimationTimer() {
            @Override public void handle(long now) {
                
                if (isUpdateFPS == false) {
                    return;
                }

                frameCounter++;
                
                if (frameCounter > elapsedFrames) {

                    final long currTime = System.nanoTime();
                    final double duration = ((double)(currTime - lastTime)) / frameCounter;
                    lastTime = currTime;

                    // frames per second
                    final int fps = (int)(1000000000d / duration + 0.5);
                    // milliseconds per frame
                    final int mpf = (int)(duration/1000000d + 0.5);
                   
                    frameCounter = 0;
                    elapsedFrames = (int)Math.max(1, (fps/3f)); // update: 3 times per sec
                    elapsedFrames = Math.min(100, elapsedFrames); 

                    fpsLabel.setText(Integer.toString(fps));
                    mpfLabel.setText(Integer.toString(mpf));
                }
                /*
                float fpsf = PerformanceTracker.getSceneTracker(scene).getInstantFPS();
                System.out.println("fps     = " + fpsf + 
                                 "\npulses  = " + PerformanceTracker.getSceneTracker(scene).getInstantPulses() +
                                 "\nfps avg = " + PerformanceTracker.getSceneTracker(scene).getAverageFPS());
                */
            }
        };                
    }
    
    private int translateDateToDay(LocalDate date){
        int day = date.getDayOfMonth();
        int month = date.getMonthValue();
        int year = date.getYear();

        day = 367 * year - (7 * (year + ((month + 9) / 12))) / 4 + (275 * month) / 9 + day - 730530;
         //System.out.println("Day: " + day);

        return day;
	}
    
    public int getDay(){
        return day;
    }
    
    
    private void checkFPS() {
        boolean isRendering = isTuxRotating || isCubeRotating || isMouseDragged;
        if (isUpdateFPS != isRendering) {
            isUpdateFPS = isRendering;
        }
        else {
            return;
        }
        
        if (isUpdateFPS == false) {
            fpsTimer.stop();
            fpsLabel.setText(Integer.toString(0));
            mpfLabel.setText(Integer.toString(0));
        }
        else {
            fpsTimer.start(); 
        }
    }
   
    private void exit() {
        System.exit(0);    
    }
    
    // HUD : head-up display
    private final class HUDLabel extends Label {
        private HUDLabel(String text) {
            this(text, textFont);
        }
        private HUDLabel(String text, Font font) {
            super(text);
            setFont(font);
            setTextFill(Color.WHITE);            
        }
    }  
    
    public static void showPlanetInfoPane(){
        planetInfoPane.setOpacity(1.0);
    }
    
    public static void hidePlanetInfoPane(){
        planetInfoPane.setOpacity(0);
    }
    
    public static void setNameForPlanetInfoPane(String name){
        Text text = (Text)getChildById(planetInfoPane.getChildren(), "PlanetName");
        if(text != null) {
            text.setText(name);
            text.setWrappingWidth(planetInfoPane.getMaxWidth());
            text.setStyle("-fx-font-family: Lucida Console;");
        }
    }

    public static void setDescriptionForPlanetInfoPane(String description){
        Text text = (Text)getChildById(planetInfoPane.getChildren(), "PlanetDescription");
        if(text != null) {
            text.setText(description);
            text.setWrappingWidth(planetInfoPane.getMaxWidth());
            text.setStyle("-fx-font-family: Lucida Console;");
        }
    }

    public static Node getChildById(ObservableList<Node> list, String id){
        Node result = null;
        for(Node child : list){
            if(child.getId() != null) {
                result = child.getId().equals(id) ? child : null;
            }
            if(result != null) { break;}
        }
        return result;
    }

    
}