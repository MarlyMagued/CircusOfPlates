
package circusofplates.Shape;

import circusofplates.Color;
import java.awt.image.BufferedImage;

public class Ball extends Shape {

    private Color Color;
    Boolean stacked = false;
    private int minBorder = 0,maxBorder = 0;

    public Ball(int x, int y, int height, int width, BufferedImage image) {
        super(x, y, height, width,  image,0);
    }

  

    public Color getColor() {
        return Color;
    }

    public void setColor(Color Color) {
        this.Color = Color;
    }

    public Boolean getStacked() {
        return stacked;
    }

    public void setStacked(Boolean stacked) {
        this.stacked = stacked;
    }

    public int getMinBorder() {
        return minBorder;
    }

    public void setMinBorder(int minBorder) {
        this.minBorder = minBorder;
    }

    public int getMaxBorder() {
        return maxBorder;
    }

    public void setMaxBorder(int maxBorder) {
        this.maxBorder = maxBorder;
    }
    
    

}
