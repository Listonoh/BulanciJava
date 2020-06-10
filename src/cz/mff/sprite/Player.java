package cz.mff.sprite;

import cz.mff.Board;
import cz.mff.Commons;

import java.awt.event.KeyEvent;

public class Player extends Sprite {

    public int playerNumber;
    public int fireEvent = KeyEvent.VK_SPACE;
    public int hp = 100;

    public Player(int x, int y,int i, Board board) {
        super(x, y, board);
        initPlayer();
        playerNumber = i;
    }

    private void initPlayer() {
        loadImages("src/images/Player%d.png");
    }

    public void act() {
//        System.out.println("dx: " + dx + "  dy: " + dy );
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

    @Override
    public boolean collideXY(int x, int y) {
        if (x + 88 < getX()|| getX() + width + 64 < x || y + 88 < getY() || getY() + height +64 < y){
            return false;
        }
        return true;
    }
}
