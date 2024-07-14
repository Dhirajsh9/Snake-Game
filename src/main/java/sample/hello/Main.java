package sample.hello;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

public class Main extends Application {
    // variables
    static int speed = 5;
    static int foodcolor = 0;
    static int width = 20;
    static int height = 20;
    static int foodX = 0;
    static int foodY = 0;
    static int cornersize = 25;
    static List<Corner> snake = new ArrayList<>();
    static Dir direction = Dir.left;
    static boolean gameOver = false;
    static Random rand = new Random();
    static String playerName = "Player";
    static AnimationTimer timer;

    public enum Dir {
        left, right, up, down
    }

    public static class Corner {
        int x;
        int y;

        public Corner(int x, int y) {
            this.x = x;
            this.y = y;
        }
    }

    @Override
    public void start(Stage primaryStage) {
        try {
            VBox root = new VBox();
            root.setStyle("-fx-alignment: center; -fx-spacing: 20px; -fx-padding: 20px;");
            StackPane gamePane = new StackPane();
            Canvas canvas = new Canvas(width * cornersize, height * cornersize);
            GraphicsContext gc = canvas.getGraphicsContext2D();
            gamePane.getChildren().add(canvas);

            TextField nameField = new TextField();
            nameField.setStyle("-fx-font-size: 20px; -fx-padding: 10px;");
            Button startButton = new Button("Start Game");
            startButton.setStyle("-fx-font-size: 20px; -fx-padding: 10px 20px;");

            startButton.setOnAction(e -> {
                playerName = nameField.getText().isEmpty() ? "Player" : nameField.getText();
                root.getChildren().removeAll(nameField, startButton);
                root.getChildren().add(gamePane);
               // root.getChildren().add(createControlButtons());
                startGame(primaryStage, gc);
            });

            root.getChildren().addAll(nameField, startButton);

            Scene scene = new Scene(root, width * cornersize, height * cornersize + 100);
            primaryStage.setScene(scene);
            primaryStage.setTitle("SNAKE GAME");
            primaryStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //If you want to display buttons on screen use this code
    //At present I feel like not to have

   /* private HBox createControlButtons() {
        HBox buttonBox = new HBox(10);
        Button upButton = new Button("Up");
        Button downButton = new Button("Down");
        Button leftButton = new Button("Left");
        Button rightButton = new Button("Right");

        upButton.setOnAction(e -> direction = Dir.up);
        downButton.setOnAction(e -> direction = Dir.down);
        leftButton.setOnAction(e -> direction = Dir.left);
        rightButton.setOnAction(e -> direction = Dir.right);

        buttonBox.getChildren().addAll(leftButton, upButton, downButton, rightButton);
        return buttonBox;
    }
*/
    private void startGame(Stage primaryStage, GraphicsContext gc) {
        resetGame();
        newFood();

        timer = new AnimationTimer() {
            long lastTick = 0;

            @Override
            public void handle(long now) {
                if (lastTick == 0) {
                    lastTick = now;
                    tick(gc);
                    return;
                }

                if (now - lastTick > 1000000000 / speed) {
                    lastTick = now;
                    tick(gc);
                }
            }
        };
        timer.start();

        Scene scene = primaryStage.getScene();

        // control
        scene.addEventFilter(KeyEvent.KEY_PRESSED, key -> {
            if (key.getCode() == KeyCode.W || key.getCode() == KeyCode.UP) {
                direction = Dir.up;
            }
            if (key.getCode() == KeyCode.A || key.getCode() == KeyCode.LEFT) {
                direction = Dir.left;
            }
            if (key.getCode() == KeyCode.S || key.getCode() == KeyCode.DOWN) {
                direction = Dir.down;
            }
            if (key.getCode() == KeyCode.D || key.getCode() == KeyCode.RIGHT) {
                direction = Dir.right;
            }
        });

        // add start snake parts
        snake.add(new Corner(width / 2, height / 2));
        snake.add(new Corner(width / 2, height / 2));
        snake.add(new Corner(width / 2, height / 2));
    }

    public void tick(GraphicsContext gc) {
        if (gameOver) {
            gc.setFill(Color.RED);
            gc.setFont(new Font("", 50));
            gc.fillText("GAME OVER", 100, 250);
            gc.setFill(Color.WHITE);
            gc.setFont(new Font("", 30));
            gc.fillText("Player: " + playerName, 100, 300);

            Button playAgainButton = new Button("Play Again");
            playAgainButton.setLayoutX(150);
            playAgainButton.setLayoutY(350);
            playAgainButton.setStyle("-fx-font-size: 20px; -fx-padding: 10px 20px;");

            StackPane gamePane = (StackPane) gc.getCanvas().getParent();
            gamePane.getChildren().add(playAgainButton);

            playAgainButton.setOnAction(e -> {
                gamePane.getChildren().remove(playAgainButton);
                resetGame();
                startGame((Stage) gc.getCanvas().getScene().getWindow(), gc);
            });
            timer.stop();
            return;
        }

        for (int i = snake.size() - 1; i >= 1; i--) {
            snake.get(i).x = snake.get(i - 1).x;
            snake.get(i).y = snake.get(i - 1).y;
        }

        switch (direction) {
            case up:
                snake.get(0).y--;
                if (snake.get(0).y < 0) {
                    gameOver = true;
                }
                break;
            case down:
                snake.get(0).y++;
                if (snake.get(0).y >= height) {
                    gameOver = true;
                }
                break;
            case left:
                snake.get(0).x--;
                if (snake.get(0).x < 0) {
                    gameOver = true;
                }
                break;
            case right:
                snake.get(0).x++;
                if (snake.get(0).x >= width) {
                    gameOver = true;
                }
                break;
        }

        // eat food
        if (foodX == snake.get(0).x && foodY == snake.get(0).y) {
            snake.add(new Corner(-1, -1));
            newFood();
        }

        // self destroy
        for (int i = 1; i < snake.size(); i++) {
            if (snake.get(0).x == snake.get(i).x && snake.get(0).y == snake.get(i).y) {
                gameOver = true;
            }
        }

        // fill
        // background
        gc.setFill(Color.BLACK);
        gc.fillRect(0, 0, width * cornersize, height * cornersize);

        // score
        gc.setFill(Color.WHITE);
        gc.setFont(new Font("", 30));
        gc.fillText("Score: " + (speed - 6), 10, 30);

        // player name
        gc.setFill(Color.WHITE);
        gc.setFont(new Font("", 30));
        gc.fillText("Player: " + playerName, 10, 60);

        // random foodcolor
        Color cc = Color.WHITE;

        switch (foodcolor) {
            case 0:
                cc = Color.PURPLE;
                break;
            case 1:
                cc = Color.LIGHTBLUE;
                break;
            case 2:
                cc = Color.YELLOW;
                break;
            case 3:
                cc = Color.PINK;
                break;
            case 4:
                cc = Color.ORANGE;
                break;
        }
        gc.setFill(cc);
        gc.fillOval(foodX * cornersize, foodY * cornersize, cornersize, cornersize);

        // snake
        for (Corner c : snake) {
            gc.setFill(Color.LIGHTGREEN);
            gc.fillRect(c.x * cornersize, c.y * cornersize, cornersize - 1, cornersize - 1);
            gc.setFill(Color.GREEN);
            gc.fillRect(c.x * cornersize, c.y * cornersize, cornersize - 2, cornersize - 2);
        }
    }

    public static void newFood() {
        start: while (true) {
            foodX = rand.nextInt(width);
            foodY = rand.nextInt(height);

            for (Corner c : snake) {
                if (c.x == foodX && c.y == foodY) {
                    continue start;
                }
            }
            foodcolor = rand.nextInt(5);
            speed++;
            break;
        }
    }

    public static void resetGame() {
        speed = 5;
        foodcolor = 0;
        snake.clear();
        direction = Dir.left;
        gameOver = false;
    }

    public static void main(String[] args) {
        launch(args);
    }
}
