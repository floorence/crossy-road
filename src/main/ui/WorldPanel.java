package ui;

import model.GameOverException;
import model.HighScoresList;
import model.InvalidMoveException;
import model.World;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.geom.Ellipse2D;
import java.util.ArrayList;

import static java.awt.event.KeyEvent.*;

//JPanel with world displayed as tiles, score, and JButton to go back to menu
public class WorldPanel extends JPanel {
    private static final int INTERVAL = 10;
    private static final int TILE_SIZE = 50;
    private static final int CAR_SPEED = 5;
    private static final int GAP = 200;
    private World world;
    private int cooldown;
    private int carSpawnNum;
    private JLabel scoreLbl;
    private ArrayList<Integer> roadRows;
    private ArrayList<Car> cars;
    private ArrayList<Ellipse2D> trees;
    private CrossyRoad main;
    private JPanel panels;
    private Timer timer;
    private CharacterHitBox hitbox;
    private boolean gameOver;

    //EFFECTS: creates a new WorldPanel
    public WorldPanel(JPanel panels, World world, CrossyRoad main) {
        this.world = world;
        cooldown = 0;
        carSpawnNum = 0;
        roadRows = new ArrayList<>();
        cars = new ArrayList<>();
        trees = new ArrayList<>();
        this.main = main;
        this.panels = panels;
        gameOver = false;
        hitbox = new CharacterHitBox(TILE_SIZE * 3 / 4, TILE_SIZE * 3 / 4);
        loadCurrentRoadsTrees();
        makeGUI();
        addKeyListener(new KeyHandler());
        addTimer();
        setFocusable(true);
        requestFocusInWindow();
    }

    //MODIFIES: this
    //EFFECTS: adds back button and score to WorldPanel
    private void makeGUI() {
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        JPanel upperPanel = new JPanel();
        upperPanel.setLayout(new BoxLayout(upperPanel, BoxLayout.X_AXIS));
        JButton backB = new JButton("back");
        backB.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (gameOver) {
                    resetWorldPanel();
                }
                CardLayout cardLayout = (CardLayout) panels.getLayout();
                cardLayout.first(panels);
                main.setPreferredSize(new Dimension(400, 200));
                main.setMinimumSize(new Dimension(400, 200));
                main.setMaximumSize(new Dimension(400, 200));
            }
        });
        upperPanel.add(backB);
        upperPanel.add(Box.createHorizontalGlue());
        scoreLbl = new JLabel("score: 0");
        upperPanel.add(scoreLbl);
        add(upperPanel);
    }

    // Set up timer
    // modifies: none
    // effects:  initializes a timer that updates game each
    //           INTERVAL milliseconds
    // Method modified from SpaceInvaders: https://github.students.cs.ubc.ca/CPSC210/B02-SpaceInvadersBase.git
    private void addTimer() {
        Timer t = new Timer(INTERVAL, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                updateEverything();
                moveCars(false);
            }
        });
        timer = t;
        t.start();
    }

    //MODIFIES: this
    //EFFECTS: counts down cooldown, detects collision, redraws world, updates score
    private void updateEverything() {
        if (cooldown > 0) {
            cooldown--;
        }
        if (collision()) {
            gameOver();
        }
        this.repaint();
        scoreLbl.setText("score: " + world.getScore());
    }

    //MODIFIES: this
    //EFFECTS: reads last 9 rows of world and adds rows of roads to roadRows and creates a tree and adds it to trees
    //for every tree tile
    private void loadCurrentRoadsTrees() {
        for (int i = world.getHeight() - 1; i >= world.getHeight() - 9; i--) {
            for (int j = 0; j < world.getWidth(); j++) {
                if (world.getTile(j, i) == 1) {
                    Ellipse2D tree = new Ellipse2D.Double(j * TILE_SIZE + TILE_SIZE / 8.0,
                            ((world.getHeight() - 1) * TILE_SIZE) - (i * TILE_SIZE) + TILE_SIZE / 8.0,
                            (double) (TILE_SIZE * 3) / 4, (double) (TILE_SIZE * 3) / 4);
                    trees.add(tree);
                }
            }
            if (world.getTile(0, i) == 2) {
                roadRows.add(i);
            }
        }
    }

    //EFFECTS: returns true if a car is intersecting with the player's hitbox
    private boolean collision() {
        for (Car c: cars) {
            if (hitbox.intersects(c)) {
                return true;
            }
        }
        return false;
    }

    //MODIFIES: this, g
    //EFFECTS: draws world and cars, adds car if car spawn countdown reaches 0
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        drawGame(g);
        drawCars(g);

        if (carSpawnNum <= 0) {
            addCar(g);
            carSpawnNum = (int) ((Math.random() * 75) + 50);
        } else {
            carSpawnNum--;
        }
        if (!hasFocus()) {
            requestFocus();
        }
    }

    //MODIFIES: g
    //EFFECTS: draws a coloured square for every tile of the world. Shows only the last 9 rows of the world
    //draws character at the character's x and y
    private void drawGame(Graphics g) {
        for (int i = world.getHeight() - 1; i >= world.getHeight() - 9; i--) {
            for (int j = 0; j < world.getWidth(); j++) {
                if (world.getCharacter().getX() == j && world.getCharacter().getY() == i) {
                    drawCharacter(g, j * TILE_SIZE,  ((world.getHeight() - 1) * TILE_SIZE) - (i * TILE_SIZE));
                } else {
                    drawTile(g, j * TILE_SIZE, ((world.getHeight() - 1) * TILE_SIZE) - (i * TILE_SIZE),
                            world.getTile(j, i), i);
                }
            }
        }
    }

    //MODIFIES: g
    //EFFECTS: draws a square with its colour depending on its type, draws a tree on square if
    //tree tile, draws road line if road tile
    private void drawTile(Graphics g, int x, int y, int type, int row) {
        Color c;
        if (type == 0 || type == 1) {
            if (row % 2 == 0) {
                c = new Color(115, 209, 44);
            } else {
                c = new Color(135, 245, 51);
            }
        } else {
            c = Color.GRAY;
        }
        g.setColor(c);
        g.fillRect(x, y, TILE_SIZE, TILE_SIZE);
        if (type == 1) {
            drawTree(g, x, y);
        } else if (type == 2) {
            drawRoadLine(g, x, y);
        }
    }

    //MODIFIES: g
    //EFFECTS: draws a road line, which is a white rectangle, at the centre of the tile with given coordinates
    private void drawRoadLine(Graphics g, int x, int y) {
        Graphics2D g2d = (Graphics2D) g.create();
        g2d.setColor(Color.WHITE);
        g2d.fillRect(x + TILE_SIZE / 4, y + TILE_SIZE / 2 - TILE_SIZE / 20, TILE_SIZE / 2, TILE_SIZE / 10);
    }

    //More complicated tree drawing which I couldn't get to work T-T
    /*
    private void drawTree(Graphics g, int x, int y) {
        Graphics2D g2d = (Graphics2D) g.create();
        int treeC = (int) (Math.random() * 2);
        if (treeC == 0) {
            g2d.setColor(Color.PINK);
        } else {
            g2d.setColor(new Color(68, 161, 42));
        }
        int size1 = (int)((Math.random() * 3) + 5);
        int size2 = (int)((Math.random() * 2) + 2);
        Shape treePart1 = new Ellipse2D.Double(x, y, (int)((size1 / 10.0) * TILE_SIZE), (int)((size1 / 10.0)
        * TILE_SIZE));
        g2d.fill(treePart1);
        Shape treePart2;
        Shape treePart3;
        if (treeC == 0) {
            treePart2 = new Ellipse2D.Double(x, y - (TILE_SIZE / 5), (int)((size2 / 10.0) * TILE_SIZE),
            (int)((size2 / 10.0) * TILE_SIZE));
            treePart3 = new Ellipse2D.Double(x - (TILE_SIZE / 5), y + (TILE_SIZE / 5), (int)((size2 / 7.0) * TILE_SIZE),
             (int)((size2 / 7.0) * TILE_SIZE));
        } else {
            treePart2 = new Ellipse2D.Double(x, y + (TILE_SIZE / 5), (int)((size2 / 10.0) * TILE_SIZE),
            (int)((size2 / 10.0) * TILE_SIZE));
            treePart3 = new Ellipse2D.Double(x + (TILE_SIZE / 5), y - (TILE_SIZE / 5),
            (int)((size2 / 7.0) * TILE_SIZE), (int)((size2 / 7.0) * TILE_SIZE));
        }
        treeParts.add(treePart1);
        treeParts.add(treePart2);
        treeParts.add(treePart3);
    }
    */

    //MODIFIES: this, g
    //EFFECTS: draws a tree, which is a green circle, at the centre of the tile with given coordinates
    private void drawTree(Graphics g, int x, int y) {
        Graphics2D g2d = (Graphics2D) g.create();
        g2d.setColor(new Color(68, 161, 42));
        Ellipse2D tree = new Ellipse2D.Double(x + TILE_SIZE / 8.0, y + TILE_SIZE / 8.0,
                (double) (TILE_SIZE * 3) / 4, (double) (TILE_SIZE * 3) / 4);
        g2d.fill(tree);
        trees.add(tree);
    }

    /*
    private void drawTrees(int row) {
        for (int i = 0; i < world.getWidth(); i++) {
            if (world.getTile(i, row) == 1) {
                drawTree(graphics, i * TILE_SIZE, ((world.getHeight() - 1) * TILE_SIZE) - (row * TILE_SIZE));
            }
        }
    }
    */

    //MODIFIES: this, g
    //EFFECTS: draws character at given x and y coordinates. TRIED SO HARD TO MAKE CHARACTER IMAGE
    //TRANSPARENT BACKGROUND BUT DIDN'T SUCCEED :'(
    private void drawCharacter(Graphics g, int x, int y) {
        ImageIcon imageIcon = new ImageIcon(world.getCharacter().getImgSource());
        Image tempImg = imageIcon.getImage();

        //BufferedImage img = new BufferedImage(imageIcon.getIconWidth(), imageIcon.getIconHeight(),
        //BufferedImage.TYPE_INT_ARGB);
        //Graphics2D g2 = img.createGraphics();
        Graphics2D g2d = (Graphics2D) g.create();

        int rule = AlphaComposite.SRC_OVER;
        Composite comp = AlphaComposite.getInstance(rule, 0.5F);
        g2d.setComposite(comp);

        //img.getGraphics().drawImage(tempImg, 0, 0, null);
        //tempImg.flush();

        //File imgFile = new File(world.getCharacter().getImgSource());
        //Image tempImg = ImageIO.read(imgFile);
        //Image img = new BufferedImage(TILE_SIZE, TILE_SIZE, BufferedImage.TYPE_INT_ARGB);
        //img.getGraphics().drawImage(tempImg, 0,0, null);
        g2d.setBackground(new Color(0, 0, 0, 1));
        g2d.drawImage(tempImg, x, y, TILE_SIZE, TILE_SIZE, null);
        //g.drawImage(img, x, y, TILE_SIZE, TILE_SIZE, null);
        //g2d.setColor(new Color(0, 0, 0, 1));
        hitbox.setLocation(x + TILE_SIZE / 8, y + TILE_SIZE / 8);
    }

    //MODIFIES: this, g
    //EFFECTS: adds new car on a random road currently shown on screen. If row of road is even, car will go
    //right, otherwise if it is odd, car will go left
    private void addCar(Graphics g) {
        if (!roadRows.isEmpty()) {
            int i = roadRows.size() - 1;
            int numRoads = 0;
            while (i >= 0 && roadRows.get(i) >= world.getHeight() - 9) {
                numRoads++;
                i--;
            }
            int carRoad = (int) (Math.random() * numRoads);
            int carY = roadRows.get(roadRows.size() - 1 - carRoad);
            if (carY % 2 == 0) {
                drawCar(g, -TILE_SIZE, ((world.getHeight() - 1) * TILE_SIZE) - carY * TILE_SIZE, 1);
            } else {
                drawCar(g, world.getWidth() * TILE_SIZE,
                        ((world.getHeight() - 1) * TILE_SIZE) - carY * TILE_SIZE, 3);
            }
        }
    }

    //MODIFES: this, g
    //EFFECTS: draws car on tile with given coordinates with a random colour between blue, red and yellow
    private void drawCar(Graphics g, int x, int y, int dir) {
        Car car = new Car(dir);
        car.setSize(TILE_SIZE * 3 / 4, TILE_SIZE / 2);
        car.setLocation(x, (y + (TILE_SIZE / 4)));
        Graphics2D g2d = (Graphics2D) g.create();
        int carC = (int)(Math.random() * 3);
        Color carColour;
        if (carC == 0) {
            carColour = new Color(52, 140, 235);
        } else if (carC == 1) {
            carColour = Color.RED;
        } else {
            carColour = new Color(255, 230, 64);
        }
        car.setColour(carColour);
        g2d.setColor(carColour);
        g2d.fill(car);
        cars.add(car);

    }

    //EFFECTS: draws all cars currently in the world
    private void drawCars(Graphics g) {
        Graphics2D g2d = (Graphics2D) g.create();

        for (Car car: cars) {
            g2d.setColor(car.getColour());
            g2d.fill(car);
        }
    }

    //MODIFIES: this
    //EFFECTS: if forward is true, moves cars one tile down to make it look like the character is
    //moving forward. if forward is false, moves cars in their direction by CAR_SPEED. If car moves
    //out of bounds, removes car from list of cars
    private void moveCars(boolean forward) {
        if (forward) {
            for (int i = 0; i < cars.size(); i++) {
                Car car = cars.get(i);
                car.setLocation((int) car.getX(), (int) car.getY() + TILE_SIZE);
            }
        } else {
            for (int i = 0; i < cars.size(); i++) {
                Car car = cars.get(i);
                if (car.getDirection() == 1) {
                    car.setLocation((int) car.getX() + CAR_SPEED, (int) car.getY());
                } else if (car.getDirection() == 3) {
                    car.setLocation((int) car.getX() - CAR_SPEED, (int) car.getY());
                }
                if (carOOB(car)) {
                    cars.remove(i);
                    i--;
                }
            }
        }
    }

    //EFFECTS: moves all trees in world one tile down to make it look like character is moving forward
    private void moveTrees() {
        for (int i = 0; i < trees.size(); i++) {
            Ellipse2D tree = trees.get(i);
            tree.setFrame(tree.getX(), tree.getY() + TILE_SIZE, tree.getWidth(), tree.getHeight());
        }
    }

    //EFFECTS: returns true if car is out of bounds of the world
    private boolean carOOB(Car car) {
        return (car.getDirection() == 1) && car.getX() >= world.getWidth() * TILE_SIZE
                || (car.getDirection() == 3) && car.getX() <= -TILE_SIZE;
    }

    //MODIFIES: this
    //EFFECTS: Presents user with a game over screen displaying their final score and if their score
    //is a high score, with a button to start a new game and a button to go back to menu
    private void gameOver() {
        timer.stop();
        gameOver = true;
        CenterableJFrame gameOverFrame = new CenterableJFrame();
        JPanel gameOver = new JPanel();
        gameOver.setLayout(new BoxLayout(gameOver, BoxLayout.Y_AXIS));
        JLabel gameOverLbl = new JLabel("Game Over! Your Score: " + world.getScore());
        gameOver.add(gameOverLbl);
        gameOverLbl.setAlignmentX(Component.CENTER_ALIGNMENT);
        if (main.isHighScore(world.getScore())) {
            JLabel highScoreLbl = new JLabel("New High Score!");
            gameOver.add(highScoreLbl);
            highScoreLbl.setAlignmentX(Component.CENTER_ALIGNMENT);
        }
        gameOver.add(backB(gameOverFrame));
        gameOver.add(newGameB(gameOverFrame));
        gameOverFrame.add(gameOver);
        gameOverFrame.pack();
        gameOverFrame.centreOnScreen();
        gameOverFrame.setVisible(true);
    }

    //MODIFIES: gameOverFrame
    //EFFECTS: Returns a JButton which, when clicked, goes back to menu
    public JButton backB(CenterableJFrame gameOverFrame) {
        JButton backB = new JButton("menu");
        backB.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                resetWorldPanel();
                gameOverFrame.dispose();
                CardLayout cardLayout = (CardLayout) panels.getLayout();
                cardLayout.first(panels);
                main.setPreferredSize(new Dimension(400, 200));
                main.setMinimumSize(new Dimension(400, 200));
                main.setMaximumSize(new Dimension(400, 200));
            }
        });
        backB.setAlignmentX(Component.CENTER_ALIGNMENT);
        return backB;
    }

    //MODIFIES: gameOverFrame
    //EFFECTS: Returns a JButton which starts a new game
    public JButton newGameB(CenterableJFrame gameOverFrame) {
        JButton newGame = new JButton("new game");
        newGame.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                resetWorldPanel();
                gameOverFrame.dispose();
            }
        });
        newGame.setAlignmentX(Component.CENTER_ALIGNMENT);
        return newGame;
    }

    //EFFECTS: resets the fields of the WorldPanel
    public void resetWorldPanel() {
        gameOver = false;
        this.world =  new World();
        cooldown = 0;
        carSpawnNum = 0;
        roadRows = new ArrayList<>();
        cars = new ArrayList<>();
        trees = new ArrayList<>();
        timer.start();
    }

    /*
     * A key handler to respond to key events
     */
    private class KeyHandler extends KeyAdapter {
        //MODFIES: this
        //EFFECTS: User can use WASD to move, otherwise does nothing. if they move forward, cars and trees move down
        //one tile. If they move below the world, calls gameOver.
        @Override
        @SuppressWarnings("methodlength")
        public void keyPressed(KeyEvent e) {
            if (cooldown <= 0) {
                try {
                    if (e.getKeyCode() == VK_W) {
                        world.moveCharacter(0);
                        cooldown = 10;
                        if (world.shouldAddNewRow(0)) {
                            world.newRow();
                            if (world.getTile(0, world.getHeight() - 1) == 2) {
                                roadRows.add(world.getHeight() - 1);
                            }
                            moveCars(true);
                            moveTrees();
                        }
                    } else if (e.getKeyCode() == VK_A) {
                        world.moveCharacter(3);
                    } else if (e.getKeyCode() == VK_S) {
                        world.moveCharacter(2);
                    } else if (e.getKeyCode() == VK_D) {
                        world.moveCharacter(1);
                    }
                } catch (InvalidMoveException ex) { //
                } catch (GameOverException ex) {
                    gameOver();
                }
            }
        }
    }
}
