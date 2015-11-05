/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sea;

import javafx.animation.FadeTransition;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.event.EventHandler;
import javafx.geometry.Point2D;
import javafx.scene.Scene;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.ContextMenuEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;

/**
 *
 * @author Егор
 */
public class Sea extends Application {

    private ImageView sea0;
    private ImageView sea1;
    private ImageView reset;
    private ImageView exit;
    private ImageView shark;
    private Rectangle sea0Clip;
    private Rectangle sea1Clip;
    private double dX = 0.0;
    private DoubleProperty coordXReal = new SimpleDoubleProperty(0.0);
    private FadeTransition sea1Fade;
    private double dX1 = 0.0;
    private DoubleProperty coordXReal1 = new SimpleDoubleProperty(0.0);
    private DropShadow sharkShadow;
    private SimpleDoubleProperty xOff = new SimpleDoubleProperty(-370);
    private SimpleDoubleProperty yOff = new SimpleDoubleProperty(-240);
    private SimpleDoubleProperty sOff = new SimpleDoubleProperty(5.0);
    private SimpleDoubleProperty scale = new SimpleDoubleProperty(0.25);
    private Timeline sharkSwimsAway;
    private boolean exited = true;

    @Override
    public void start(Stage primaryStage) { //Main container
        primaryStage.initStyle(StageStyle.TRANSPARENT);
        primaryStage.setX(510);
        primaryStage.setY(200);

        sea0 = new ImageView(new Image(Sea.class.getResourceAsStream("images/sea0.jpg")));
        sea1 = new ImageView(new Image(Sea.class.getResourceAsStream("images/sea1.jpg")));
        
        sea1.setOpacity(0.0);
        sea0Clip = new Rectangle(450, 360);
        sea0Clip.setArcWidth(30);
        sea0Clip.setArcHeight(30);
        sea1Clip = new Rectangle(450, 360);
        sea1Clip.setArcWidth(30);
        sea1Clip.setArcHeight(30);
        sea0.setClip(sea0Clip);
        sea1.setClip(sea1Clip);

        setShark();
        setTransition();
        setReset();
        sea0Drag();
        sea1Drag();
        setExit();

        Pane root = new Pane();
        root.getChildren().addAll(sea0, sea1, exit, reset, shark);

        Scene scene = new Scene(root, 1410, 880); //Drawing surface
        scene.setFill(null);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void setShark() {
        shark = new ImageView(new Image(Sea.class.getResourceAsStream("images/shark.png")));

        shark.setScaleX(0.25);
        shark.setScaleY(0.25);
        shark.setX(-370);
        shark.setY(-240);

        shark.opacityProperty().bind(sea1.opacityProperty());
        shark.scaleXProperty().bind(scale);
        shark.scaleYProperty().bind(scale);
        shark.xProperty().bind(xOff);
        shark.yProperty().bind(yOff);

        sharkShadow = new DropShadow(20.0, 5.0, 5.0, Color.BLACK);
        sharkShadow.offsetXProperty().bind(sOff);
        sharkShadow.offsetYProperty().bind(sOff);
        sharkShadow.setSpread(0.6);
        shark.setEffect(sharkShadow);

        sharkSwimsAway = new Timeline(
                new KeyFrame(Duration.ZERO,
                        new KeyValue(sOff, 0),
                        new KeyValue(scale, 0.25),
                        new KeyValue(xOff, -370),
                        new KeyValue(yOff, -240)),
                new KeyFrame(new Duration(3000),
                        new KeyValue(sOff, -40),
                        new KeyValue(scale, 1.0),
                        new KeyValue(xOff, 100),
                        new KeyValue(yOff, 100))
        );
        shark.setOnMouseClicked(new EventHandler<MouseEvent>() {

            @Override
            public void handle(MouseEvent event) {
                sharkSwimsAway.play();
                reset.toFront();
            }
        });
    }

    private void setTransition() {
        sea1Fade = new FadeTransition(Duration.seconds(1), sea1);
        sea1Fade.setFromValue(0);
        sea1Fade.setToValue(1);

        sea1.setOnMouseEntered(new EventHandler<MouseEvent>() {

            @Override
            public void handle(MouseEvent event) {
                if (exited) {
                    sea1Fade.setRate(1.0);
                    sea1Fade.play();
                    exited = false;
                }
            }
        });

        sea1.setOnMouseExited(new EventHandler<MouseEvent>() {

            @Override
            public void handle(MouseEvent event) {
                if (!sea1Clip.contains(new Point2D(event.getSceneX(), event.getSceneY()))) {
                    sea1Fade.setRate(-1.0);
                    sea1Fade.play();
                    exited = true;
                }
            }
        });
    }

    private void setReset() {
        reset = new ImageView(new Image(Sea.class.getResourceAsStream("images/reset.png")));
        reset.setFitHeight(55);
        reset.setFitWidth(55);
//        reset.setX(390);
        reset.setX(5);
        reset.setY(304);
        reset.setOnMouseClicked(new EventHandler<MouseEvent>() {

            @Override
            public void handle(MouseEvent event) {
                coordXReal.set(0.0);
            }
        });
        reset.setOnContextMenuRequested(new EventHandler<ContextMenuEvent>() {

            @Override
            public void handle(ContextMenuEvent event) {
                coordXReal1.set(0.0);
                xOff.set(-370);
                yOff.set(-240);
                scale.set(0.25);
                sOff.set(5.0);
            }
        });
    }

    private void sea0Drag() {
        sea0.setOnMousePressed(new EventHandler<MouseEvent>() {

            @Override
            public void handle(MouseEvent event) {
                dX = event.getSceneX() - coordXReal.getValue();
            }
        });

        sea0.setOnMouseDragged(new EventHandler<MouseEvent>() {

            @Override
            public void handle(MouseEvent event) {
                if ((event.getSceneX() - dX) <= 0 && (event.getSceneX() - dX) >= -1100) {
                    coordXReal.set(event.getSceneX() - dX);
                }
            }
        });
        sea0.xProperty().bind(coordXReal);
    }

    private void sea1Drag() {
        sea1.setOnMousePressed(new EventHandler<MouseEvent>() {

            @Override
            public void handle(MouseEvent event) {
                dX1 = event.getSceneX() - coordXReal1.getValue();
            }
        });

        sea1.setOnMouseDragged(new EventHandler<MouseEvent>() {

            @Override
            public void handle(MouseEvent event) {
                coordXReal1.set(event.getSceneX() - dX1);
            }
        });
        sea1.xProperty().bind(coordXReal1);
    }

    private void setExit() {
        exit = new ImageView(new Image(Sea.class.getResourceAsStream("images/exit.png")));
        exit.setFitHeight(45);
        exit.setFitWidth(45);
//        exit.setX(395);
//        exit.setY(10);
        exit.setX(400);
        exit.setY(5);
        
        exit.setOnMouseClicked(new EventHandler<MouseEvent>() {

            @Override
            public void handle(MouseEvent event) {
                System.exit(0);
            }
        });
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
}
