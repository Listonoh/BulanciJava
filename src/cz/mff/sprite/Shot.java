package cz.mff.sprite;

import cz.mff.Board;
import cz.mff.Commons;

public class Shot extends Sprite {
    public Shot(int x, int y, int dx, int dy, Board board) {
        super(x, y, board);
        initShot();
        this.dx = dx;
        this.dy = dy;
        collidable = false;
    }

    private void initShot() {
        loadImages("src/images/shot%d.png");
    }

    /**
     * move the shot in direction and if it leave boundary of map it delete it
     */
    public void update() {
        int x = (int) (getX() + this.dx * Commons.DELAY * Commons.SPEED * 2);
        int y = (int) (getY() + this.dy * Commons.DELAY * Commons.SPEED * 2);
        if (x < 0 || y < 0 || x > Commons.BOARD_WIDTH || y > Commons.BOARD_HEIGHT) {
            this.setDying(true);
        }

        setX(x);
        setY(y);
    }
}
