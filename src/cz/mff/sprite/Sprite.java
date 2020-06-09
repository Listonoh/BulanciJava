package cz.mff.sprite;

import cz.mff.Commons;

import javax.swing.*;
import java.awt.Image;
import java.util.ArrayList;

public class Sprite {

    private boolean visible;
    private Image[] image;
    private boolean dying;
    int width;
    int height;

    private int x;
    private int y;

    int dx;
    int dy;
    public int looking = 0; //[0:up, 1: left, 2: down, 3: right]

    public Sprite() {

        visible = true;
    }

    public void die() {

        visible = false;
    }

    public boolean isVisible() {

        return visible;
    }

    protected void setVisible(boolean visible) {

        this.visible = visible;
    }

    public void loadImage(String location){
        image = new Image[4];
        for (int i = 0; i < 4; i++) {
            var shotImg = String.format(location,i);
            var ii = new ImageIcon(shotImg).getImage();
            image[i] = ii;
        }

        width = image[0].getWidth(null);
        height = image[0].getHeight(null);
    }

    public Image getImage() {

        return image[looking];
    }

    public void setX(int x) {
        if(x > this.x) looking = 3;
        if(x < this.x) looking = 1;
        this.x = x;

        if (x >= Commons.BOARD_WIDTH - 2 * width) {
            this.x = Commons.BOARD_WIDTH - 2 * width;
        }

        if (x <= 2) {
            this.x = 2;
        }
    }

    public void setY(int y) {
        if(y > this.y) looking = 2;
        if(y < this.y) looking = 0;
        this.y = y;

        if (y <= 2) {
            this.y = 2;
        }

        if (y >= Commons.BOARD_HEIGHT - 2.5 * height) {
            this.y = (int) (Commons.BOARD_HEIGHT - 2.5 * height);
        }
    }

    public int getY() {

        return y;
    }

    public int getX() {

        return x;
    }

    public void setDying(boolean dying) {

        this.dying = dying;
    }

    public boolean isDying() {

        return this.dying;
    }
}
