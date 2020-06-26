package cz.mff.sprite;

import cz.mff.Board;
import cz.mff.Commons;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class Sprite {
    public boolean collidable = true;
    public boolean visible;
    public Board board;
    public int x;
    int width;
    int height;
    int dx;
    int dy;
    int looking = 0; //[0:up, 1: left, 2: down, 3: right]
    private Image[] image;
    private boolean dying;
    private int y;

    public Sprite(int x, int y, Board board) {
        this.board = board;
        this.x = x;
        this.y = y;
        visible = true;
    }

    /**
     * loads only one Image form location
     *
     * @param location path to Image
     */
    public void loadImage(String location) {
        image = new Image[1];
        image[0] = new ImageIcon(location).getImage();
        width = image[0].getWidth(null);
        height = image[0].getHeight(null);
    }

    /**
     * load set of Images [4] from location
     *
     * @param location in format "name%d.png" because image location will be updated by String.format
     */
    public void loadImages(String location) {
        image = new Image[4];
        for (int i = 0; i < 4; i++) {
            var shotImg = String.format(location, i);
            var ii = new ImageIcon(shotImg).getImage();
            image[i] = ii;
        }

        width = image[0].getWidth(null);
        height = image[0].getHeight(null);
    }

    public Image getImage() {
        if (image != null) {
            if (image.length == 4) {
                return image[looking];
            } else {
                return image[0];
            }
        }
        return null;
    }

    public int getY() {

        return y;
    }

    /**
     * Set looking variable to correct value -> where is the Sprite looking
     * and checks if it's not out of boundary or collide with others
     *
     * @param y new position on the map
     */
    public void setY(int y) {
        var last = this.y;
        if (y > this.y) looking = 2;
        if (y < this.y) looking = 0;
        this.y = y;

        if (board.collideWithOthers(this)) {
            this.y = last;
        }

        if (y <= 0) {
            this.y = 0;
        }

        if (y >= Commons.BOARD_HEIGHT - 2.5 * height) {
            this.y = (int) (Commons.BOARD_HEIGHT - 2.5 * height);
        }
    }

    public int getX() {

        return x;
    }

    /**
     * Set looking variable to correct value -> where is the Sprite looking
     * and checks if it's not out of boundary or collide with others
     *
     * @param x new position on the map
     */
    public void setX(int x) {
        var last = this.x;
        if (x > this.x) looking = 3;
        if (x < this.x) looking = 1;
        this.x = x;

        if (board.collideWithOthers(this)) {
            this.x = last;
        }

        if (x >= Commons.BOARD_WIDTH - 2 * width) {
            this.x = Commons.BOARD_WIDTH - 2 * width;
        }

        if (x <= 0) {
            this.x = 0;
        }
    }

    /**
     * gets correct point of shooting from Sprite
     */
    public Point getShootingPoint() {
        var p = new Point(Commons.shootingFrom[looking]);
        if (looking % 2 == 1) { //left and right
            p.x *= width;
            p.y *= (double) height / 2;
        } else {
            p.x *= width;
            p.y *= height;
        }

        if (looking == 0) p.y -= 5;

        p.x += x;
        p.y += y;
        return p;
    }

    /**
     * @return correct vector where Sprite is looking
     */
    public Point getLookingP() {
        return Commons.lookingArr[looking];
    }

    public boolean isDying() {

        return this.dying;
    }

    public void setDying(boolean dying) {

        this.dying = dying;
    }

    /**
     * chceck if this object collide with point x, y
     */
    public boolean collideXY(int x, int y) {
        return x + Commons.BASE_WIDTH >= this.x && this.x + width >= x && y + Commons.BASE_WIDTH >= this.y && this.y + height >= y;
    }

    /**
     * if collide with another Sprite
     * black magic probably right
     */
    public boolean collide(Sprite collider) {
        if (collider == this) return false;
        return (collider.getX() < this.x + this.width &&
                collider.getX() + collider.width > this.x &&
                collider.getY() < this.y + this.height &&
                collider.getY() + collider.height > this.y);
    }

    public void shot(ArrayList<Shot> shots, long time) {
    }

    public int getLooking() {
        return looking;
    }

    public void setLooking(int i) {
        looking = i;
    }

}
