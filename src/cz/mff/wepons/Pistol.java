package cz.mff.wepons;

import cz.mff.Commons;
import cz.mff.sprite.Shot;
import cz.mff.sprite.Sprite;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class Pistol {
    long lastShot = -100;
    long reloadingFrom = 0;
    int rounds = 6;
    int reloadTime = 120;
    int shotsLeft = 6;
    int shotSpeed = 15;
    Image[] images;

    public Pistol(){
        images = new Image[4];
        for (int i = 0; i < 4; i++) {
            var shotImg = String.format(Commons.Pistol,i);
            var ii = new ImageIcon(shotImg).getImage();
            images[i] = ii;
        }
    }

    public boolean shootable (long time){
        return time - lastShot > shotSpeed;
    }

    public ArrayList<Shot> tryCreateShoots(long time, Sprite holder) {
        var ret = new ArrayList<Shot>();
        if (time - reloadingFrom <= reloadTime) return ret;
        if (shotsLeft == 0) shotsLeft = rounds;


        if (shootable(time)){
            ret = CreateShoots(holder);
            lastShot = time;
            shotsLeft -= 1;
        }
        if (shotsLeft == 0) reloadingFrom = time;
        return ret;
    }

    ArrayList<Shot> CreateShoots(Sprite holder) {
        var ret = new ArrayList<Shot>();
        var p = holder.getLookingP();
        var shp = holder.getShootingPoint();
        ret.add(new Shot(shp.x, shp.y, p.x, p.y, holder.board));
        return ret;
    }

    public Image getImage(int looking) {
        return images[looking];
    }
}
