package cz.mff;

import cz.mff.sprite.*;

import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.Timer;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

public class Board extends JPanel {

    private Dimension d;
    private List<Exodus> exoduses;
    private Player player;
    private ArrayList<Shot> shots = new ArrayList<>();
    
    private int direction = -1;
    private int kills = 0;

    private boolean inGame = true;
    private String explImg = "src/images/explosion%d.png";
    private String message = "Game Over";

    private Timer timer;
    private long time = 0;


    public Board() {

        initBoard();
        gameInit();
    }

    private void initBoard() {

        addKeyListener(new TAdapter());
        setFocusable(true);
        d = new Dimension(Commons.BOARD_WIDTH, Commons.BOARD_HEIGHT);
        setBackground(Color.black);

        timer = new Timer(Commons.DELAY, new GameCycle());
        timer.start();
        gameInit();
    }


    private void gameInit() {

        exoduses = new ArrayList<>();
        int xi,yi;
        for (int i = 0; i < 4; i++) {
            do {
                xi = (int)(Math.random() * (Commons.BOARD_WIDTH - Commons.EXODUS_WIDTH));
                yi = (int)(Math.random() * (Commons.BOARD_HEIGHT - Commons.EXODUS_HEIGHT));
            }while (notBlocked(xi,yi));

            var exodus = new Exodus(xi, yi);
            exoduses.add(exodus);
        }

        player = new Player();
    }

    //TODO block colision
    private boolean notBlocked(int xi, int yi) {
        return false;
    }

    private void drawExoduses(Graphics g) {

        for (Exodus exodus : exoduses) {

            if (exodus.isVisible()) {

                g.drawImage(exodus.getImage(), exodus.getX(), exodus.getY(), this);
            }

            if (exodus.isDying()) {
                exodus.die();
            }
        }
    }

    private void drawPlayer(Graphics g) {

        if (player.isVisible()) {

            g.drawImage(player.getImage(), player.getX(), player.getY(), this);
        }

        if (player.isDying()) {

            player.die();
            inGame = false;
        }
    }

    private void drawShot(Graphics g) {
        for (var shot : shots){
            if (shot.isVisible()) {

                g.drawImage(shot.getImage(), shot.getX(), shot.getY(), this);
            }
        }
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        doDrawing(g);
    }

    private void doDrawing(Graphics g) {

        g.setColor(Color.black);
        g.fillRect(0, 0, d.width, d.height);
        g.setColor(Color.green);

        if (inGame) {
            drawExoduses(g);
            drawPlayer(g);
            drawShot(g);

        } else {

            if (timer.isRunning()) {
                timer.stop();
            }

            gameOver(g);
        }

        Toolkit.getDefaultToolkit().sync();
    }

    private void gameOver(Graphics g) {

        g.setColor(Color.black);
        g.fillRect(0, 0, Commons.BOARD_WIDTH, Commons.BOARD_HEIGHT);

        g.setColor(new Color(0, 32, 48));
        g.fillRect(50, Commons.BOARD_WIDTH / 2 - 30, Commons.BOARD_WIDTH - 100, 50);
        g.setColor(Color.white);
        g.drawRect(50, Commons.BOARD_WIDTH / 2 - 30, Commons.BOARD_WIDTH - 100, 50);

        var small = new Font("Helvetica", Font.BOLD, 14);
        var fontMetrics = this.getFontMetrics(small);

        g.setColor(Color.white);
        g.setFont(small);
        g.drawString(message, (Commons.BOARD_WIDTH - fontMetrics.stringWidth(message)) / 2,
                Commons.BOARD_WIDTH / 2);
    }

    private void update() {

        if (kills == Commons.NUMBER_OF_KILLS) {
            inGame = false;
            timer.stop();
            message = "Game won!";
        }

        // player
        player.act();
        for (var sh: shots) {
            sh.update();
        }

        // shot
        for (var shot : new ArrayList<>(shots)){

            if (shot.isVisible()) {

                int shotX = shot.getX();
                int shotY = shot.getY();

                for (Exodus exodus : exoduses) {
                    int exodusX = exodus.getX();
                    int exodusY = exodus.getY();

                    if (exodus.isVisible() && shot.isVisible()) {
                        if (shotX >= (exodusX)
                                && shotX <= (exodusX + Commons.EXODUS_WIDTH)
                                && shotY >= (exodusY)
                                && shotY <= (exodusY + Commons.EXODUS_HEIGHT)) {

                            exodus.loadImage(explImg);
                            exodus.setDying(true);
                            kills++;
                            shot.die();
                            shots.remove(shot);
                        }
                    }
                }
            }
        }

        for (Exodus exodus : exoduses) {

            int x = exodus.getX();
            int y = exodus.getY();

            if(exodus.inLine(player)){
                if (!exodus.tryShot(player, time)){
                    exodus.act();
                }
                else{
                    var p = exodus.getLookingP();
                    var shp = exodus.getShootingPoint();
                    shots.add(new Shot(shp.x, shp.y, p.x, p.y));
                }
            }
            else {
                exodus.act();
            }
        }
    }

    private void doGameCycle() {
        time += 1;
        update();
        repaint();
    }

    private class GameCycle implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            var time = e.getWhen();
            doGameCycle();
        }
    }

    private class TAdapter extends KeyAdapter {

        @Override
        public void keyReleased(KeyEvent e) {

            player.keyReleased(e);
        }

        @Override
        public void keyPressed(KeyEvent e) {

            player.keyPressed(e);

            int x = player.getX();
            int y = player.getY();

            int key = e.getKeyCode();

            if (key == KeyEvent.VK_SPACE) {

                if (inGame) {
                    Point p = player.getLookingP();
                    Point shp = player.getShootingPoint();
                    shots.add(new Shot(shp.x, shp.y, p.x, p.y));

                }
            }
        }
    }
}
