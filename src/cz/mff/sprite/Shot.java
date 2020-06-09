package cz.mff.sprite;

import cz.mff.Commons;

import javax.swing.ImageIcon;
import java.awt.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.ConcurrentModificationException;
import java.util.List;

public class Shot extends Sprite {
    public Shot(int x, int y, int dx, int dy) {

        initShot(x, y);
        this.dx = dx;
        this.dy = dy;
    }

    private void initShot(int x, int y) {
        loadImage("src/images/shot%d.png");
        setX(x);
        setY(y);
    }


    public void update() {
        int x = (int) (getX() + this.dx * Commons.DELAY * Commons.SPEED * 2);
        int y = (int) (getY() + this.dy * Commons.DELAY * Commons.SPEED * 2);

        setX(x);
        setY(y);
        if(x < 0 || y< 0 || x > Commons.BOARD_WIDTH || y > Commons.BOARD_HEIGHT){
            this.die();
        }
    }
}
