package cz.mff.sprite;

import cz.mff.Board;
import cz.mff.Commons;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class Sprite {

    private boolean visible;
    private Image[] image;
    private boolean dying;
    int width;
    int height;
    private Board board;

    public int x;
    private int y;

    int dx;
    int dy;
    int looking = 0; //[0:up, 1: left, 2: down, 3: right]

    public Sprite(int x, int y, Board board) {
        this.board = board;
        this.x = x;
        this.y = y;
        visible = true;
    }

    public void loadImage(String location){
        image = new Image[1];
        image[0] = new ImageIcon(location).getImage();
        width = image[0].getWidth(null);
        height = image[0].getHeight(null);
    }

    public void loadImages(String location){
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
        if(image != null){
            if(image.length == 4){
                return image[looking];
            }
            else {
                return  image[0];
            }
        }
        return null;
    }

    public void setX(int x) {
        var last = this.x;
        if(x > this.x) looking = 3;
        if(x < this.x) looking = 1;
        this.x = x;

        if(board.collideWithOthers(this)){
            this.x = last;
        }

        if (x >= Commons.BOARD_WIDTH - 2 * width) {
            this.x = Commons.BOARD_WIDTH - 2 * width;
        }

        if (x <= 0) {
            this.x = 0;
        }
    }

    public void setY(int y) {
        var last = y;
        if(y > this.y) looking = 2;
        if(y < this.y) looking = 0;
        this.y = y;

        if(board.collideWithOthers(this)){
            this.y = last;
        }

        if (y <= 0) {
            this.y = 0;
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

    public Point getShootingPoint(){
        var p = new Point(Commons.shootingFrom[looking]);
        if (looking %2 == 1){ //left and right
            p.x *= width;
            p.y *= (int) (height/2);
        }
        else {
            p.x *= width;
            p.y *= height;
        }

        if (looking == 0) p.y -= 5;

        p.x += x;
        p.y += y;
        return p;
    }

    public void setLooking(int i){
        looking = i;
    }

    public Point getLookingP(){
        return Commons.lookingArr[looking];
    }

    public void setDying(boolean dying) {

        this.dying = dying;
    }

    public boolean isDying() {

        return this.dying;
    }

    public boolean collideXY(int x, int y) {
        if (x + 32 < this.x || this.x + width + 5 < x || y + 32 < this.y || this.y + height +5 < y){
            return false;
        }
        return true;
    }

    ///black magick probably right
    public boolean collide(Sprite collider) {
        if (collider == this) return false;
        return ! ((collider.getX() + collider.width < (this.x) //far from left
                || collider.getX() > (this.x + this.width)) //or far from right
                || (collider.getY() +collider.height < (this.y) ///far from top
                || collider.getY() > (this.y + this.height))); ///far from bottom

    }
}
