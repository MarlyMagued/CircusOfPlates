/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package circusofplates.Shape;

import eg.edu.alexu.csd.oop.game.GameObject;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;

public class Shape implements GameObject {

    private int x;
    private int prevX;
    private int y;
    private int height;
    private int width;
    private boolean isVisible;
    private int type;
    private BufferedImage[] images = new BufferedImage[1];

    public Shape(int x, int y, int height, int width, BufferedImage image, int type) {
        this.x = x;
        this.y = y;
        this.height = height;
        this.width = width;
        this.isVisible = true;
        this.type = type;
        this.images[0] = image;

    }

    @Override
    public int getX() {
        return x;
    }

    @Override
    public void setX(int x) {
        if (this instanceof Plate) {
            Plate p = (Plate) this;
            if (p.getStacked()) {
                if (x <= p.getMaxBorder() && x >= p.getMinBorder()) {
                    this.x = x;
                } else {
                    if (x > p.getMaxBorder()) {
                        this.x = p.getMaxBorder();
                    } else if (x < p.getMinBorder()) {
                        this.x = p.getMinBorder();
                    }
                }
            } else {
                this.x = x;
            }
        } else if (this instanceof Ball) {
            Ball b = (Ball) this;
            if (b.getStacked()) {
                if (x <= b.getMaxBorder() && x >= b.getMinBorder()) {
                    this.x = x;
                } else {
                    if (x > b.getMaxBorder()) {
                        this.x = b.getMaxBorder();
                    } else if (x < b.getMinBorder()) {
                        this.x = b.getMinBorder();
                    }
                }
            } else {
                this.x = x;
            }
        } else if (this instanceof Clown) {
            if (x < 1260 && x > 5) {
                this.x = x;
            }
        } else {
            this.x = x;
        }
    }

    @Override
    public int getY() {
        return y;
    }

    @Override
    public void setY(int y) {
        if (this instanceof Plate) {
            Plate p = (Plate) this;
            if (!p.getStacked()) {
                this.y = y;
            }
        } else if (this instanceof Ball) {
            Ball b = (Ball) this;
            if (!b.getStacked()) {
                this.y = y;
            }
        } else if (!(this instanceof Clown)) {
            this.y = y;
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
    public boolean isVisible() {
        return isVisible;
    }

    @Override
    public BufferedImage[] getSpriteImages() {
        return images;
    }

    public void setIsVisible(boolean isVisible) {
        this.isVisible = isVisible;
    }

    public int getType() {
        return type;
    }

}
