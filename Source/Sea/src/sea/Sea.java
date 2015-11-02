/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sea;

import javafx.animation.FadeTransition;
import javafx.application.Application;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
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
    private Rectangle sea0Clip;
    private Rectangle sea1Clip;
    private ImageView exit;
    private double dX = 0.0;
    private DoubleProperty coordXReal = new SimpleDoubleProperty(0.0);
    private FadeTransition sea1Fade;

    @Override
    public void start(Stage primaryStage) { //Main container
        primaryStage.initStyle(StageStyle.TRANSPARENT);

        sea0 = new ImageView(new Image(Sea.class.getResourceAsStream("images/sea0.jpg")));
        sea1 = new ImageView(new Image(Sea.class.getResourceAsStream("images/sea1.jpg")));
        sea1.setFitHeight(360);
        sea1.setFitWidth(569);
        sea1.setOpacity(0.0);
        sea0Clip = new Rectangle(450, 360);
        sea0Clip.setArcWidth(30);
        sea0Clip.setArcHeight(30);
        sea1Clip = new Rectangle(450, 360);
        sea1Clip.setArcWidth(30);
        sea1Clip.setArcHeight(30);
        sea0.setClip(sea0Clip);
        sea1.setClip(sea1Clip);
        
        setTransition();
        setReset();
        sea0Drag();
        setExit();

        Pane root = new Pane();
        root.getChildren().addAll(sea0, sea1, exit, reset);

        Scene scene = new Scene(root, 450, 360); //Drawing surface
        scene.setFill(null);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void setTransition() {
        sea1Fade = new FadeTransition(Duration.seconds(1));
        sea1Fade.setFromValue(0);
        sea1Fade.setToValue(1);
        sea1Fade.setNode(sea1);

        sea1.setOnMouseEntered(new EventHandler<MouseEvent>() {

            @Override
            public void handle(MouseEvent event) {
                sea1Fade.setRate(1);
                sea1Fade.play();
            }
        });
        
        sea1.setOnMouseExited(new EventHandler<MouseEvent>() {

            @Override
            public void handle(MouseEvent event) {
                sea1Fade.setRate(-1);
                sea1Fade.play();
            }
        });
    }

    private void setReset() {
        reset = new ImageView(new Image(Sea.class.getResourceAsStream("images/reset.png")));
        reset.setFitHeight(55);
        reset.setFitWidth(55);
        reset.setX(390);
        reset.setY(300);
        reset.setOnMouseClicked(new EventHandler<MouseEvent>() {

            @Override
            public void handle(MouseEvent event) {
                coordXReal.set(0.0);
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

    private void setExit() {
        exit = new ImageView(new Image(Sea.class.getResourceAsStream("images/exit.png")));
        exit.setFitHeight(45);
        exit.setFitWidth(45);
        exit.setX(395);
        exit.setY(10);

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
