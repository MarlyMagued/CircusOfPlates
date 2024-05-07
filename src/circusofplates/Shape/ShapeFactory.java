/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package circusofplates.Shape;

import circusofplates.Color;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import javax.imageio.ImageIO;

public class ShapeFactory {

    private static ArrayList<Shape> bombs = new ArrayList<>();
    private static ArrayList<Shape> hearts = new ArrayList<>();
    private static ArrayList<Shape> plates = new ArrayList<>();
    private static ArrayList<Shape> balls = new ArrayList<>();

    public static Shape getShape(String shape, int x, int y, int height, int width) {
        
        Color color;
        switch (shape) {
            case "Plate":
                color = getRandomColor();
               
                if (!plates.isEmpty()) {
                    return plates.remove(0);
                } else if(color==Color.Red) {
                    Plate plate = new Plate(x, y, height, width, imageObject.getImage(0));
                    plate.setColor(Color.Red);
                    return plate;

                } else if(color== Color.Blue){
                    Plate plate = new Plate(x, y, height, width,imageObject.getImage(1));
                    plate.setColor(Color.Blue);
                    return plate;
                } else if(color== Color.Yellow){
                    Plate plate = new Plate(x, y, height, width,imageObject.getImage(2));
                    plate.setColor(Color.Yellow);
                    return plate;
                } 

            case "Bomb":
                if (!bombs.isEmpty()) {
                   Bomb bomb= (Bomb) bombs.remove(0);
                   bomb.setIsVisible(true);
                    return bomb ;
                } else {
                    Bomb bomb = new Bomb(x, y, height, width, imageObject.getImage(3));
                    return bomb;
                }

            case "Heart":
                if (!hearts.isEmpty()) {
                    return hearts.remove(0);
                } else {
                    return new Heart(x, y, height, width, imageObject.getImage(4));
                }
            case "Ball":
                if (!balls.isEmpty()) {
                    return balls.remove(0);
                } else {
                    return new Ball(x, y, height, width, imageObject.getImage(5));
                }
            default: {
                return new Clown(x, y, height, width, imageObject.getImage(6));
            }
        }

    }

    private static Color getRandomColor() {
        int num = (int) (Math.random() * 3);//adjust number of colors
        return Color.valueOf(num);
    }

    public static List<Shape> getBombs() {
        return bombs;
    }

    public static List<Shape> getHearts() {
        return hearts;
    }

    public static List<Shape> getPlates() {
        return plates;
    }

}
