package cz.mff.sprite;

import cz.mff.Board;
import cz.mff.Commons;

public class LifeBar extends Sprite{
    int min = Commons.BOARD_WIDTH;
    Player player;

    public LifeBar(int x, int y, Player player, Board board) {
        super(x, y, board);
        this.player = player;
        loadImage(Commons.LifeBar);
    }

    public void update() {
        if (player != null){

            var newX = (int)(-min*((100.0-player.hp)/100));
            setX(newX);
        }
    }

    @Override
    public void setX(int x) {
        this.x = x;
    }
}
