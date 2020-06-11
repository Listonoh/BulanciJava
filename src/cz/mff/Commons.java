package cz.mff;

import java.awt.*;

public interface Commons {

    int BOARD_WIDTH = 800;
    int BOARD_HEIGHT = 600;

    int DELAY = 16;
    double SPEED = 0.125;
    int PLAYER_Y = 50;
    int PLAYER_X = 50;
    Point[] lookingArr = new Point[]{new Point(0, -1), new Point(-1, 0), new Point(0, 1), new Point(1, 0)};
    Point[] shootingFrom = new Point[]{new Point(1, 0), new Point(0, 1), new Point(0, 1), new Point(1, 1)};
    int EXODUS_MOVE = 20;
    int SHOTSPEED = 15;
    int SHOTDMG = 49;

    String BlockImage = "src/images/Block.png";
    String Map1 = "src/images/Map1.png";
    String LifeBar = "src/images/LifeBar.png";
    int BASE_WIDTH = 32;
    String Pistol = "src/images/Pistol%d.png";
    String Shotgun = "src/images/Shotgun%d.png";
}
