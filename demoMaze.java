package com.example.seniorproject;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class demoMaze extends Application implements Runnable{

    public static void main(String[] args) {
        launch(args);
    }

    int[][] maze;

    // int to be used later in the array
    final static int background = 0;
    final static int wall = 1;
    final static int path = 2;
    final static int empty = 3;
    final static int visited = 4;

    Color[] color; // used to store previous variables

    Canvas canvas;
    GraphicsContext draw;

    int columns = 41;
    int rows = 41;
    int blocksize = 12;
    int sleepTime = 1000; // wait time before generating another maze
    int speedSleep = 20; // delay between making and solving the maze

    @Override
    public void start(Stage primaryStage) {

        color = new Color[] {
                Color.BLACK, // background
                Color.BLACK, // wall
                Color.BLUE, // path
                Color.WHITE, // empty
                Color.rgb(200, 200, 200) // visited
        };

        maze = new int[rows][columns];

        // create the area to draw the maze
        canvas = new Canvas(rows *blocksize, columns *blocksize);
        draw = canvas.getGraphicsContext2D();
        draw.setFill(color[background]);
        draw.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());

        Pane mazePane = new Pane(canvas);

        Scene scene = new Scene(mazePane);
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.setTitle("Maze Generator");
        primaryStage.show();

        Thread runner = new Thread(this);
        runner.setDaemon(true);
        runner.start();

    }

    void drawSquare(int row, int column, int colorCode) {
        Platform.runLater(() ->{
            draw.setFill(color[colorCode]);
            int x = blocksize * column;
            int y = blocksize * row;
            draw.fillRect(x, y, blocksize, blocksize);
        });
    }

    public void run() {
        while(true) {
            try {Thread.sleep(1000);}
            catch (InterruptedException e) {}
            makeMaze();
            solveMaze(1, 1);
            synchronized (this) {
                try { wait(sleepTime);}
                catch (InterruptedException e) {}
            }
            Platform.runLater( () ->{
                draw.setFill(color[background]);
                draw.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());
            });

        }
    }

    void makeMaze() {

        int i, j;
        int rooms = 0;
        int walls = 0;
        int[] wallRow = new int[(rows*columns)/2];
        int[] wallCol = new int[(rows*columns)/2];

        for(i = 0; i<rows; i++)
            for(j = 0; j<columns; j++)
                maze[i][j] = wall;
        for(i = 1; i < rows-1; i+=2) {
            for (j = 1; j < columns-1; j+=2){
                rooms++;
                maze[i][j] = -rooms;

                if( i < rows-2) {
                    wallRow[walls] = i+1;
                    wallCol[walls] = j;
                    walls++;
                }

                if(j < columns-2) {
                    wallRow[walls] = i;
                    wallCol[walls] = j+1;
                    walls++;
                }
            }
        }
        Platform.runLater(() ->{
            draw.setFill(color[empty]);
            for(int r = 0; r < rows; r++) {
                for(int c = 0; c < columns; c++) {
                    if(maze[r][c] < 0)
                        draw.fillRect(c*blocksize, r*blocksize, blocksize, blocksize);
                }
            }
        });
        synchronized (this) {
            try {wait(1000);}
            catch (InterruptedException e) {}
        }
        int r;
        for(i = walls-1; i >0; i--) {
            r = (int) (Math.random() * i);
            removeWall(wallRow[r], wallCol[r]);
            wallCol[r] = wallCol[i];
            wallRow[r] = wallRow[i];
        }

        for(i = 1; i < rows-1; i++)
            for(j = 1; j < columns-1; j++)
                if (maze[i][j] < 0)
                    maze[i][j] = empty;
        synchronized (this) {
            try {wait(1000);}
            catch (InterruptedException e) {}
        }
    }

    void removeWall(int row, int column) {

        if(row % 2 == 1 && maze[row][column-1] != maze[row][column+1]) {
            fill(row, column-1, maze[row][column-1], maze[row][column+1]);
            maze[row][column] = maze[row][column+1];
            drawSquare(row, column, empty);
            synchronized (this) {
                try {wait(speedSleep);}
                catch (InterruptedException e) {}
            }
        }

        else if (row % 2 == 0 && maze[row-1][column] !=maze[row+1][column]) {
            fill(row-1, column, maze[row-1][column], maze[row+1][column]);
            maze[row][column] = maze[row+1][column];
            drawSquare(row, column, empty);
            synchronized (this) {
                try {wait(speedSleep);}
                catch (InterruptedException e) {}
            }
        }

    }

    void fill(int row, int col, int replace, int replaceWith){
        if(maze[row][col] == replace) {
            maze[row][col] = replaceWith;
            fill(row+1, col, replace, replaceWith);
            fill(row-1, col, replace, replaceWith);
            fill(row, col+1, replace, replaceWith);
            fill(row, col-1, replace, replaceWith);

        }
    }

    boolean solveMaze(int row, int col) {

        if(maze[row][col] == empty) {
            maze[row][col] = path;
            drawSquare(row, col, path);
            if(row == rows-2 && col == columns-2)
                return true;
            try{Thread.sleep(speedSleep);}
            catch (InterruptedException e) {}

            if (solveMaze(row-1, col) ||
                solveMaze(row, col-1) ||
                solveMaze(row+1, col) ||
                solveMaze(row, col+1) )
                return true;

            maze[row][col] = visited;
            drawSquare(row, col, visited);
            synchronized (this) {
                try {wait(speedSleep);}
                catch (InterruptedException e) {}
            }
        }
        return false;
    }


}
