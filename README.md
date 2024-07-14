![Screenshot 2024-07-14 151853](https://github.com/user-attachments/assets/0598a074-2a05-4c31-ba15-eb84e2d80db8)
A classic Snake game implemented in JavaFX. The game allows you to control the snake using both keyboard keys (WASD and arrow keys) and on-screen buttons. Players can also input their name before starting the game, and the score along with the player's name is displayed during the game.

Features
Smooth Animation: The game runs smoothly with an adjustable speed.
Random Food Color: Each new food appears with a different color.
Keyboard and Button Controls: Control the snake using WASD keys, arrow keys, or on-screen buttons.
Player Name Input: Input your name before starting the game.
Game Over and Restart: Displays a "Game Over" message with an option to restart the game.

Running the Game
Clone the repository:

bash
Copy code
git clone https://github.com/Dhirajsh9/snake-game.git
cd snake-game
Ensure you have JavaFX SDK set up and configured properly.

Compile and run the game:

bash
Copy code
javac -d bin --module-path /path/to/javafx-sdk/lib --add-modules javafx.controls src/sample/hello/Main.java
java --module-path /path/to/javafx-sdk/lib --add-modules javafx.controls -cp bin sample.hello.Main
Replace /path/to/javafx-sdk/lib with the actual path to your JavaFX SDK library.

Example Commands
bash
Copy code
javac -d bin --module-path /usr/share/openjfx/lib --add-modules javafx.controls src/sample/hello/Main.java
java --module-path /usr/share/openjfx/lib --add-modules javafx.controls -cp bin sample.hello.Main
Code Overview
Main.java
The main class sets up the game, handles user input, and contains the game loop:

start: Initializes the game, sets up the canvas, and handles the player input.
createControlButtons: Creates the on-screen control buttons.
startGame: Starts the game by initializing variables and starting the animation timer.
tick: The main game loop which updates the game state and renders graphics.
newFood: Generates new food at random positions on the grid.
resetGame: Resets the game state for a new game.
Game Mechanics
The snake moves continuously in the current direction.
The game ends if the snake collides with the walls or itself.
Eating food increases the snake's length and score.
The speed increases with each food eaten.![Screenshot 2024-07-14 152214](https://github.com/user-attachments/assets/077f63f9-7853-4a31-8ee5-64c6299a80f0)
