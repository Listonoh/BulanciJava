package cz.mff;

import java.awt.*;

public interface Commons {

    int BOARD_WIDTH = 800;
    int BOARD_HEIGHT = 600;

    int GO_DOWN = 15;
    int CHANCE = 5;
    int DELAY = 16;
    int EXODUS_WIDTH = 15;
    int EXODUS_HEIGHT = 15;
    int NUMBER_OF_KILLS = 15;
    double SPEED = 0.125;
    int PLAYER_Y = 50;
    int PLAYER_X = 50;
    Point[] lookingArr = new Point[]{new Point(0,-1), new Point(-1,0), new Point(0,1), new Point(1,0)};
    Point[] shootingFrom = new Point[]{new Point(1,0), new Point(0,1), new Point(0,1), new Point(1,1)};
    int EXODUS_MOVE = 20;
    int SHOTSPEED = 15;
    int SHOTDMG = 49;

    String BlockImage = "src/images/Block.png" ;
    String Map1 = "src/images/Map1.png";
    String LifeBar = "src/images/LifeBar.png" ;
}
