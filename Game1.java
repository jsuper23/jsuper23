package com.example.seniorproject;

import javafx.animation.AnimationTimer;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.List;

public class Game1 extends Application {

    AnimationTimer timer;
    Pane root = new Pane();
    List drop = new ArrayList();
    double mouseX;
    Rectangle cont;
    double speed;
    double falling;
    Label lblLives;
    Label lblScore;
    int lives;
    int score;
    Scene scene4;
    private Stage stage;


    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {

        this.stage = primaryStage;

        lblLives = new Label("Lives: 3");
        lblLives.setLayoutX(10);
        lblLives.setLayoutY(10);
        lives = 3;

        lblScore = new Label("Score: 0");
        lblScore.setLayoutX(10);
        lblScore.setLayoutY(20);
        score = 0;

        speed = 1;
        falling = 500;

        Timeline timeline = new Timeline(new KeyFrame(Duration.millis(falling), event -> {

            speed += falling / 5000;
            drop.add(circle());
            root.getChildren().add(((Node)drop.get(drop.size() -1)));
        }));

        timeline.setCycleCount(1000);
        timeline.play();



        timer = new AnimationTimer() {

            @Override
            public void handle(long arg0) {
                gameUpdate();

            }

        };
        timer.start();



        cont = rectangle();

        root.getChildren().addAll(cont, lblLives, lblScore);

        Scene scene = new Scene(root, 400, 600);

        scene.setOnMouseMoved(e -> {
            mouseX = e.getX();
        });

        primaryStage.setScene(scene);
        primaryStage.show();



        //scene4 GAME OVER
        Label gameoverLable = new Label("GAME OVER");
        gameoverLable.setFont(Font.font("Verdana", FontWeight.BOLD, 180));
        gameoverLable.setAlignment(Pos.CENTER);

        Button menu2 = new Button("Back to menu");
        menu2.setOnAction(e-> {primaryStage.setScene(scene);});
        menu2.setFont(Font.font("Verdana", FontWeight.BOLD, 20));
        menu2.setMinWidth(30);
        menu2.setBackground(new Background(new BackgroundFill(Color.BLUE, CornerRadii.EMPTY, Insets.EMPTY)));
        menu2.setBorder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, new BorderWidths(5))));
        menu2.setPadding(new Insets(15, 2, 15, 2));

        Button seeScoreboard = new Button("Go to Scoreboard");
        //seeScoreboard.setOnAction(e-> {primaryStage.setScene(scene3);});
        seeScoreboard.setFont(Font.font("Verdana", FontWeight.BOLD, 20));
        seeScoreboard.setMinWidth(30);
        seeScoreboard.setBackground(new Background(new BackgroundFill(Color.BLUE, CornerRadii.EMPTY, Insets.EMPTY)));
        seeScoreboard.setBorder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, new BorderWidths(5))));
        seeScoreboard.setPadding(new Insets(15, 2, 15, 2));

        Button playAgainBtn = new Button("Play Again");
        //playAgainBtn.setOnAction(e-> {primaryStage.setScene(scene2);});
        playAgainBtn.setFont(Font.font("Verdana", FontWeight.BOLD, 20));
        playAgainBtn.setMinWidth(30);
        playAgainBtn.setBackground(new Background(new BackgroundFill(Color.BLUE, CornerRadii.EMPTY, Insets.EMPTY)));
        playAgainBtn.setBorder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, new BorderWidths(5))));
        playAgainBtn.setPadding(new Insets(15, 2, 15, 2));

        HBox btns = new HBox();
        btns.setAlignment(Pos.CENTER);
        btns.setSpacing(5);
        btns.getChildren().addAll(menu2, seeScoreboard, playAgainBtn);

        VBox goScreen = new VBox();
        goScreen.getChildren().addAll(gameoverLable, btns);
        //goScreen.setSpacing(1);
        //goScreen.setAlignment(Pos.CENTER);
        goScreen.setBackground(new Background(new BackgroundFill(Color.LIGHTBLUE, CornerRadii.EMPTY, Insets.EMPTY)));

        scene4 = new Scene(goScreen, 1500, 1700, Color.LIGHTBLUE);


    }

    public Circle circle() {
        Circle circle = new Circle();
        circle.setLayoutX(rand(0, 400));
        circle.setLayoutY(1);
        circle.setRadius(6);
        circle.setFill(Color.BLUE);
        return circle;
    }

    public Rectangle rectangle() {
        Rectangle rectangle = new Rectangle();
        rectangle.setLayoutX(200);
        rectangle.setLayoutY(550);
        rectangle.setHeight(50);
        rectangle.setWidth(70);
        rectangle.setFill(Color.GREEN);
        return rectangle;

    }

    public int rand(int min, int max) {
        return (int)(Math.random() * max + min);
    }
    public void gameUpdate(){

        cont.setLayoutX(mouseX);


        for(int i = 0; i < drop.size(); i++) {
            ((Circle) drop.get(i)).setLayoutY(((Circle) drop.get(i)).getLayoutY() + speed + ((Circle) drop.get(i)).getLayoutY() / 150 );
            //if get droped into square
            if((((Circle) drop.get(i)).getLayoutX() > cont.getLayoutX() && ((Circle) drop.get(i)).getLayoutX() < cont.getLayoutX() + 70) &&
                    ((Circle) drop.get(i)).getLayoutY() >= 550	) {
                root.getChildren().remove(((Circle) drop.get(i)));
                drop.remove(i);
                score += 10;
                lblScore.setText("Score: " + String.valueOf(score));
            }

            if(lives == 0) {
                timer.stop();
                this.stage.setScene(scene4);
            }


            //if missed remove
            else if(((Circle) drop.get(i)).getLayoutY() >= 590) {
                root.getChildren().remove(((Circle) drop.get(i)));
                drop.remove(i);
                lives -= 1;
                lblLives.setText("Missed: " + String.valueOf(lives));
            }
        }
    }



}
