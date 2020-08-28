package cz.mff.wepons;

import cz.mff.Commons;
import cz.mff.sprite.Shot;
import cz.mff.sprite.Sprite;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class Shotgun extends Pistol {
    public Shotgun() {
        images = new Image[4];
        for (int i = 0; i < 4; i++) {
            var shotImg = String.format(Commons.Shotgun, i);
            var ii = new ImageIcon(shotImg).getImage();
            images[i] = ii;
        }
    }

    @Override
    ArrayList<Shot> CreateShoots(Sprite holder) {
        var ret = new ArrayList<Shot>();
        var p = holder.getLookingPoint();
        var shp = holder.getShootingPoint();
        int x2 = shp.x;
        int x3 = shp.x;
        int y2 = shp.y;
        int y3 = shp.y;

        if (holder.getLooking() % 2 != 0) { // up or down
            y2 += 10;
            y3 -= 10;
        } else {
            x2 += 10;
            x3 -= 10;
        }
        ret.add(new Shot(shp.x, shp.y, p.x, p.y, holder.board));
        ret.add(new Shot(x2, y2, p.x, p.y, holder.board));
        ret.add(new Shot(x3, y3, p.x, p.y, holder.board));
        return ret;
    }
}
