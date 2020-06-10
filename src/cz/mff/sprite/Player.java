package cz.mff.sprite;

import cz.mff.Commons;

import javax.swing.ImageIcon;
import java.awt.event.KeyEvent;

public class Player extends Sprite {


    private int facingX;
    private int facingY;

    public Player() {
        initPlayer();
    }

    private void initPlayer() {
        loadImage("src/images/Player%d.png");

        setX(Commons.PLAYER_X);

        setY(Commons.PLAYER_Y);
        dx = 1;
        dy = 1;
    }

    public void act() {
        var rx = getX();
        var ry = getY();
        var l = Math.sqrt(dx*dx + dy*dy);
        if (l == 0) l = 1;
        var ddx = dx * Commons.DELAY * Commons.SPEED / l;
        var ddy = dy * Commons.DELAY * Commons.SPEED / l;
        setX((int) (rx + ddx));
        setY((int) (ry + ddy));
    }

    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();

        if (key == KeyEvent.VK_LEFT) {
            dx = -1;
        }
        if (key == KeyEvent.VK_RIGHT) {
            dx = 1;
        }
        if (key == KeyEvent.VK_UP) {
            dy = -1;
        }
        if (key == KeyEvent.VK_DOWN) {
            dy = 1;
        }

        facingX = dx;
        facingY = dy;
    }

    public void keyReleased(KeyEvent e) {
        int key = e.getKeyCode();

        if (key == KeyEvent.VK_LEFT) {
            dx = 0;
        }

        if (key == KeyEvent.VK_RIGHT) {
            dx = 0;
        }

        if (key == KeyEvent.VK_UP) {
            dy = 0;
        }

        if (key == KeyEvent.VK_DOWN) {
            dy = 0;
        }
    }

    public int getDy() {
        return this.dy;
    }

    public int getDx() {
        return this.dx;
    }
}
