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
    }

    public void act() {
        var rx = getX();
        var dds = (dx * Commons.DELAY * Commons.SPEED);
        var ix = (int) (rx + dds);
        setX(ix);
        setY((int) (getY() + (dy * Commons.DELAY * Commons.SPEED)));
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
