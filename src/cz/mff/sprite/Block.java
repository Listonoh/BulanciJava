package cz.mff.sprite;

import cz.mff.Board;
import cz.mff.Commons;

public class Block extends Sprite {
    public Block(int x, int y, boolean visible, Board board) {
        super(x, y, board);
        loadImage(Commons.BlockImage);
        this.visible = visible;
    }
}
