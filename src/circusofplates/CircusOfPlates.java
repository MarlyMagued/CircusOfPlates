package circusofplates;

import circusofplates.Shape.*;
import java.time.Duration;
import java.time.Instant;
import java.util.Iterator;
import java.util.List;
import java.util.ArrayList;
import eg.edu.alexu.csd.oop.game.World;
import eg.edu.alexu.csd.oop.game.GameObject;
import java.util.LinkedList;
import java.util.Queue;
import strategy.DiffStrat;

/**
 *
 * @author marlymaged
 */
public class CircusOfPlates implements World,Iterable<GameObject>{
    //falling object like Ball or plate type=0
    //falling object like heart type=2
    //falling object like bomb type=3
    //controlled object like caught plates/cups or clown type=1
    //constant background type=4

    private List<GameObject> constantObject = new LinkedList<GameObject>();
    private List<GameObject> controlableObject = new LinkedList<GameObject>();
    private List<GameObject> movableObject = new LinkedList<GameObject>();
    private int width;
    private int height;
    private static int GAME_SPEED;
    private static int CONTROL_SPEED;
    private static int NUMBER_OF_BOMBS;
    private static int GAME_TIME = 1 * 60; //game time=1min
    private Instant startTime = Instant.now();
    private int score;
    private GameStatusSubject gameStatusSubject = new GameStatusSubject();
    private MusicPlayer music = new MusicPlayer();
    private static final int LEFT_STACK_X = 580;  // Adjust as needed
    private static final int RIGHT_STACK_X = 740;
    private static final int STACK_Y = 475;
    private List<Shape> leftStack = new ArrayList<>();
    private List<Shape> rightStack = new ArrayList<>();
    private int leftStackY = STACK_Y;   // Initialize with the base stacking y-coordinate for the left side
    private int rightStackY = STACK_Y;

    public CircusOfPlates(int height, int width, DiffStrat difficulty) {
        new Thread(music).start();
        gameStatusSubject.register(music);
        this.width = width;
        this.height = height;
        this.GAME_SPEED = difficulty.speedOfFalling();
        this.CONTROL_SPEED = difficulty.speedOfControl();
        this.NUMBER_OF_BOMBS = difficulty.numberOfBombs();
        //insert clown image
        controlableObject.add(ShapeFactory.getShape("Clown", 500, 380, 378, 350));
        constantObject.add(new Background(0, 0, 720, 1260, imageObject.getImage(7)));
        constantObject.add(ShapeFactory.getShape("Heart", 10, 10, 50, 50));
        constantObject.add(ShapeFactory.getShape("Heart", 60, 10, 50, 50));
        constantObject.add(ShapeFactory.getShape("Heart", 110, 10, 50, 50));
        for (int i = 0; i < 10; i++) {
            movableObject.add(createShape("plate"));
        }
        for (int j = 0; j < NUMBER_OF_BOMBS; j++) {
            movableObject.add(ShapeFactory.getShape("Bomb", (int) (Math.random() * width), height / 4, 50, 50));
        }
        for (int j = 0; j < 5; j++) {
            movableObject.add(ShapeFactory.getShape("Ball", (int) (Math.random() * width), height / 4, 35, 36));
        }

    }

    @Override
    public boolean refresh() {
        Clown clown = (Clown) controlableObject.get(0);

        GameObjectIterator movingObjectIterator = (GameObjectIterator) iterator();

        while (movingObjectIterator.hasNext()) {
            Shape shape = (Shape) movingObjectIterator.next();
            Shape shape2 = null;

            if (isCaught(shape, clown)) {
                if (shape instanceof Plate) {
                    shape2 = createShape("plate");
                }
                if (shape instanceof Ball) {
                    shape2 = createShape("Ball");
                }
                // Plate caught
                if (shape.getType() == 0) {
                    movableObject.remove(shape);
                    stackPlate(shape, clown);
                } else if (shape.getType() == 3) {
                    // Bomb caught
                    shape.setIsVisible(false);
                    reuse(shape);
                    createShape("Bomb");
                    score = score - (score > 0 ? 1 : 0);
                    movableObject.remove(shape);
                    int lastIndex = constantObject.size() - 1;
                    if (lastIndex >= 1) {
                        constantObject.remove(lastIndex);
                        if (constantObject.size() == 1) {
                            gameStatusSubject.notifyObserver();
                            movableObject.clear();
                            // Notify observers about game over
                            return false;
                        }
                    }
                }

                // Remove the caught object from the movable objects
                if (shape2 != null) {
                    movableObject.add(shape2);
                }
            } else {
                // Object is falling
                shape.setY(shape.getY() + GAME_SPEED);

                if (shape.getY() >= height) {
                    // Reset position if the object falls beyond the screen
                    shape.setY((int) (Math.random() * height / 8));
                    shape.setX((int) (Math.random() * width));
                }
            }
        }

        return !isTimeOut();
    }

    private void checkAndRemoveColorStack(List<Shape> stack, boolean isLeftStack) {
        if (stack.size() >= 3) {
            Shape topShape = stack.get(stack.size() - 1);
            if (topShape instanceof Plate) {
                Color colorToMatch = ((Plate) topShape).getColor();
                int count = 0;
                int lastRemovedPlateHeight = isLeftStack ? STACK_Y : STACK_Y;

                // Count the number of plates with the same color on the stack
                for (int i = stack.size() - 1; i >= 0; i--) {
                    Shape currentShape = stack.get(i);
                    if (currentShape instanceof Plate && ((Plate) currentShape).getColor().equals(colorToMatch)) {
                        count++;
                    } else {
                        break; // Stop counting when a different color or non-matching type is encountered
                    }
                }

                // Remove the top 3 plates if they have the same color
                if (count >= 3) {
                    for (int i = 0; i < 3; i++) {
                        Shape removedShape = stack.remove(stack.size() - 1);
                        controlableObject.remove(removedShape);
                        movableObject.remove(removedShape);

                        lastRemovedPlateHeight = removedShape.getY() + removedShape.getHeight();
                    }
                    score += 5;

                    if (isLeftStack) {
                        leftStackY = lastRemovedPlateHeight;
                    } else {
                        rightStackY = lastRemovedPlateHeight;
                    }
                }
            } else {
                checkAndRemoveBallsStack(stack, isLeftStack);
            }
        }
    }

    private void checkAndRemoveBallsStack(List<Shape> stack, boolean isLeftStack) {
        if (stack.size() >= 3) {
            int count = 0;
            int lastRemovedBallHeight = isLeftStack ? STACK_Y : STACK_Y;

            // Count the number of consecutive balls on the stack
            for (int i = stack.size() - 1; i >= 0; i--) {
                Shape currentShape = stack.get(i);
                if (currentShape instanceof Ball) {
                    count++;
                } else {
                    break; // Stop counting when a non-ball shape is encountered
                }
            }

            // Remove the top 3 balls if they are consecutive
            if (count >= 3) {
                for (int i = 0; i < 3; i++) {
                    Shape removedShape = stack.remove(stack.size() - 1);
                    controlableObject.remove(removedShape);
                    movableObject.remove(removedShape);

                    lastRemovedBallHeight = removedShape.getY() + removedShape.getHeight();
                }
                score += 5;

                if (isLeftStack) {
                    leftStackY = lastRemovedBallHeight;
                } else {
                    rightStackY = lastRemovedBallHeight;
                }
            }
        }
    }

    private boolean isOnLeftSide(Shape shape, Clown clown) {
        return (shape.getX() + shape.getWidth() / 2 <= clown.getX() + clown.getWidth() / 2);
    }

    private void stackPlate(Shape shape, Clown clown) {
        boolean isLeftStack = isOnLeftSide(shape, clown);
        int stackX = isLeftStack ? clown.getX() + 80 : clown.getX() + 240;

        // Determine the correct Y-coordinate based on the stack
        int stackY;
        List<Shape> currentStack = isLeftStack ? leftStack : rightStack;

        if (currentStack.isEmpty()) {
            stackY = isLeftStack ? leftStackY : rightStackY;
        } else {
            Shape lastRemovedShape = currentStack.get(currentStack.size() - 1);
            stackY = lastRemovedShape.getY() - shape.getHeight() + 10;
        }

        shape.setX(stackX);
        shape.setY(stackY);

        controlableObject.add(shape);
        movableObject.remove(shape);

        // Update the stacking position
        if (isLeftStack) {
            leftStackY = stackY;
            leftStack.add(shape);
        } else {
            rightStackY = stackY;
            rightStack.add(shape);
        }

        setBorder(shape, isLeftStack);

        checkAndRemoveColorStack(leftStack, isLeftStack);
        checkAndRemoveColorStack(rightStack, !isLeftStack);
    }

    void setBorder(Shape shape, boolean isLeftStack) {
        if (shape instanceof Plate) {
            Plate p = (Plate) shape;
            p.setStacked(true);
            if (isLeftStack) {
                p.setMaxBorder(990);
                p.setMinBorder(80);
            } else {
                p.setMaxBorder(1130);
                p.setMinBorder(220);
            }
        } else if (shape instanceof Ball) {
            Ball b = (Ball) shape;
            b.setStacked(true);
            if (isLeftStack) {
                b.setMaxBorder(1000);
                b.setMinBorder(95);
            } else {
                b.setMaxBorder(1145);
                b.setMinBorder(230);
            }
        }
    }

    @Override
    public int getWidth() {
        return width;
    }

    @Override
    public int getHeight() {
        return height;
    }

    @Override
    public String getStatus() {
        int remainingTime = (int) (GAME_TIME - (Duration.between(startTime, getCurrentTime())).toSeconds());
        if (remainingTime > 0) {
            return "Score=" + score + "   |   Time=" + remainingTime;	// update status
        } else {
            gameStatusSubject.notifyObserver();
            movableObject.clear();
            return "Score=" + score + "   |   Time=" + 0;
        }
    }

    @Override
    public int getSpeed() {
        return GAME_SPEED;
    }

    @Override
    public int getControlSpeed() {
        return CONTROL_SPEED;
    }

    
    public boolean isCaught(GameObject o1, GameObject clown) {
        return ((Math.abs((o1.getX() + o1.getWidth() / 2) - (clown.getX() + clown.getWidth() / 4)) <= o1.getWidth()) //range of right hand 
                || (Math.abs((o1.getX() + o1.getWidth() / 2) - (clown.getX() + clown.getWidth() * 3 / 4)) <= o1.getWidth())) // range of left hand
                && (Math.abs((o1.getY() + o1.getHeight() / 4) - (clown.getY() + clown.getHeight() / 4)) <= o1.getHeight());
    }

    private boolean isTimeOut() {
        return (Duration.between(startTime, getCurrentTime()).toSeconds() > GAME_TIME);
    }

    private Instant getCurrentTime() {
        return Instant.now();
    }

    @Override
    public List<eg.edu.alexu.csd.oop.game.GameObject> getConstantObjects() {
        return constantObject;
    }

    @Override
    public List<eg.edu.alexu.csd.oop.game.GameObject> getMovableObjects() {
        return movableObject;
    }

    @Override
    public List<eg.edu.alexu.csd.oop.game.GameObject> getControlableObjects() {
        return controlableObject;
    }

    public Shape createShape(String name) {
        if (name.equalsIgnoreCase("plate")) {
            return ShapeFactory.getShape("Plate", (int) (Math.random() * width), height / 4, 30, 60);
        } else if (name.equalsIgnoreCase("ball")) {
            return ShapeFactory.getShape("Ball", (int) (Math.random() * width), height / 4, 30, 60);
        } else {
            return ShapeFactory.getShape("Bomb", (int) (Math.random() * width), height / 4, 30, 60);
        }
    }

    public void reuse(GameObject object) {
        if (object instanceof Plate || object instanceof Ball) {
            if (!movableObject.isEmpty()) {
                movableObject.add((Shape) object);
            }
        } else if (object instanceof Bomb) {
            ShapeFactory.getBombs().add((Shape) object);
        } else {
            ShapeFactory.getHearts().add((Shape) object);
        }
    }

    @Override
    public Iterator<GameObject> iterator() {
        return new GameObjectIterator(movableObject);
    }
}
