package cz.mff.sprite;

import cz.mff.Board;
import cz.mff.Commons;

public class Exodus extends Sprite {
    int timer = 0;
    long lastShot = 0;
    int dx,dy;
    private int lastSpotedPlayer;

    public Exodus(int x, int y, Board board) {
        super(x,y,board);
        initExodus();
    }

    private void initExodus() {
        loadImages("src/images/Exitus%d.png");
    }

    public void act() {
        if(timer > 0){
            setX(getX()+dx);
            setY(getY()+dy);
            timer--;
        }
        else{
            dx = 0; dy = 0;
            var rnd = Math.random();
            var range = (int) Math.round(rnd * 2.99 -1.5); // -1, 0 or 1 with same PROBABILITY
            var direction = Math.random();
            if (direction > 0.5){
                dx = (int) (range * Commons.DELAY * Commons.SPEED);
                setX(getX() + dx);
            }
            else {
                dy = (int) (range * Commons.DELAY * Commons.SPEED);
                setY(getY() + dy);
            }
            timer = Commons.EXODUS_MOVE + (int)(Math.random() * (Commons.EXODUS_MOVE / 4));
        }
    }

    public boolean tryShot(Player player, long time) {
        if (time - lastShot > Commons.SHOTSPEED){
            lastShot = time;
            setLooking(lastSpotedPlayer);
            return true;
        }
        return false;
    }

    public boolean inLine(Sprite target) {
        var mx = getX();
        var my = getY();
        var tx = target.getX();
        var ty = target.getY();

        //up
        if(ty < my && tx <= mx + width && mx + width <= tx + target.width){
            lastSpotedPlayer = 0;
            return true;
        }
        //left
        if (tx < mx && ty <= my && my <= ty + target.height){
            lastSpotedPlayer = 1;
            return true;
        }
        //down
        if(ty > my && tx <= mx && mx <= tx + target.width){
            lastSpotedPlayer = 2;
            return true;
        }
        //right
        if (tx > mx && ty <= my + height && my + height <= ty + target.height){
            lastSpotedPlayer = 3;
            return true;
        }
        return false;
    }
}
