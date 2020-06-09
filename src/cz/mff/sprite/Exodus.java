package cz.mff.sprite;

import cz.mff.Commons;

import javax.swing.ImageIcon;

public class Exodus extends Sprite {
    int timer = 0;
    int dx,dy;
    public Exodus(int x, int y) {

        initExodus(x, y);
    }

    private void initExodus(int x, int y) {
        setX(x);
        setY(y);
        loadImage("src/images/Exitus%d.png");
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

    public boolean tryShot(Player player) {
        return false;
    }

    public boolean inLine(Player player) {
        return false;
    }
}
