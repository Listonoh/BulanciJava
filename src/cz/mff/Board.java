package cz.mff;

import cz.mff.sprite.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;

public class Board extends JPanel {

    private Dimension d;
    private List<Exodus> exoduses = new ArrayList<>();
    private ArrayList<Player> players = new ArrayList<>();
    private ArrayList<Shot> shots = new ArrayList<>();
    private ArrayList<Block> blocks = new ArrayList<>();
    private ArrayList<LifeBar> lifeBars = new ArrayList<>();
    private Board board = this;
    private int kills = 0;
    private int maxKills;

    private boolean inGame = true;
    private String explImg = "src/images/explosion%d.png";
    private String message = "Game Over";

    private Timer timer;
    private long time = 0;
    private Image BackImage;
    private long maxTime = 2000;


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

    /**
     * Resets all resources and loads new map
     */
    private void gameInit() {
        players = new ArrayList<>();
        lifeBars = new ArrayList<>();
        exoduses = new ArrayList<>();
        shots = new ArrayList<>();
        blocks = new ArrayList<>();
        initMap1();
    }

    private void initMap1() {
        BackImage = new ImageIcon(Commons.Map1).getImage();

        blocks.add(new Block(95,303, true, this));
        players.add(new Player(Commons.PLAYER_X, Commons.PLAYER_Y, 0, this));
        lifeBars.add(new LifeBar(0, d.height -60,players.get(0), this));
        maxKills = 4;

        int xi, yi;
        for (int i = 0; i < 4; i++) {
            do {
                xi = (int) (Math.random() * (Commons.BOARD_WIDTH - Commons.BASE_WIDTH));
                yi = (int) (Math.random() * (Commons.BOARD_HEIGHT - Commons.BASE_WIDTH));
            } while (Blocked(xi, yi));

            var exodus = new Exodus(xi, yi, this);
            exoduses.add(exodus);
        }
    }

    private boolean Blocked(int x, int y) {
        for (var exodus: exoduses) {
            if (exodus.collideXY(x,y)){
                return true;
            }
        }
        for (var block: blocks) {
            if (block.collideXY(x,y)){
                return true;
            }
        }
        for (var player: players){
            if (player.collideXY(x,y)){
                return true;
            }
        }
        return false;
    }

    private void drawExoduses(Graphics g) {

        for (Exodus exodus : exoduses) {

            g.drawImage(exodus.getImage(), exodus.getX(), exodus.getY(), this);
            g.drawImage(exodus.getImageOfWeapon(), exodus.getX(), exodus.getY(), this);


        }
    }

    private void drawPlayers(Graphics g) {
        for (var player: players) {
            g.drawImage(player.getImage(), player.getX(), player.getY(), this);
            g.drawImage(player.getImageOfWeapon(), player.getX(), player.getY(), this);

        }
    }

    private void drawShots(Graphics g) {
        for (var shot : shots) {
            g.drawImage(shot.getImage(), shot.getX(), shot.getY(), this);
        }
    }

    private void drawLifeBars(Graphics g) {
        for (var lb : lifeBars){
            g.drawImage(lb.getImage(),lb.getX(),lb.getY(),this);
        }
    }

    private void drawBlocks(Graphics g) {
        for (var block: blocks){
            if (block.visible) g.drawImage(block.getImage(), block.getX(), block.getY(),this);
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
        g.drawImage(BackImage,0,0,this);
        g.setColor(Color.green);
        g.drawString(String.valueOf(kills), 0,0);

        if (inGame) {
            drawExoduses(g);
            drawPlayers(g);
            drawShots(g);
            drawBlocks(g);
            drawLifeBars(g);

        } else {

            if (timer.isRunning()) {
                timer.stop();
            }

            gameOver(g);
        }

        Toolkit.getDefaultToolkit().sync();
    }

    /**
     * Prints end screen of the game
    */
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
        for (var player: players)
            if (player.isDying()) {
                inGame = false;
                break;
            }

        if (kills == maxKills) {
            inGame = false;
            timer.stop();
            message = "Game won!";
        }
        else if(time > maxTime){
            inGame = false;
            timer.stop();
            message = "Game lost!, no time left";
        }
        if (players.size() == 0){
            inGame = false;
            timer.stop();
            message = "Game lost!";
        }

        exoduses.removeIf(Sprite::isDying);
        players.removeIf(Sprite::isDying);

        // player
        for (var player: players){
            player.act();
        }

        for (var sh : shots) {
            sh.update();
        }

        for (LifeBar lifeBar : lifeBars) {
            lifeBar.update();
        }

        // shot
        for (var shot : new ArrayList<>(shots)) {
            if (shot.isDying()){
                shots.remove(shot);
                continue;
            }
            for (Exodus exodus : exoduses) {
                if (exodus.collide(shot)){
                    exodus.loadImage(explImg);
                    exodus.setDying(true);
                    kills++;
                    shots.remove(shot);
                }
            }
            for (var player : players){
                if (player.collide(shot)){
                    shots.remove(shot);
                    player.hp -= Commons.SHOTDMG;
                    if(player.hp <= 0){
                        player.loadImage(explImg);
                        player.setDying(true);
                    }
                }
            }
            for (var block : blocks){
                if(block.collide(shot)){
                    shots.remove(shot);
                }
            }
        }

        for (Exodus exodus : exoduses) {

            int x = exodus.getX();
            int y = exodus.getY();
            for(var player: players){
                if (exodus.inLine(player)) {
                    if (exodus.tryShot(time)) {
                        exodus.shot(shots, time);
                    }
                    else {
                        exodus.act();
                    }
                } else {
                    exodus.act();
                }
            }
        }
    }

    private void doGameCycle() {
        time += 1;
        update();
        repaint();
    }

    /**
     * check for if Sprite collide with other sprites
     */
    public boolean collideWithOthers(Sprite sprite) {
        ///black magick probably right
        if (!sprite.collidable) return false;

        for(var block: blocks){
            if (sprite.collide(block)) return true;
        }

        for(var exodus: exoduses){
            if (sprite.collide(exodus)) return true;
        }

        for(var player: players ){
            if (sprite.collide(player)) return true;
        }
        return false;
    }

    private class GameCycle implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            doGameCycle();
        }
    }

    private class TAdapter extends KeyAdapter {

        @Override
        public void keyReleased(KeyEvent e) {
            for (var player : players){
                player.keyReleased(e);
            }
        }

        @Override
        public void keyPressed(KeyEvent e) {
            for(var player: players){
                player.keyPressed(e);

                int key = e.getKeyCode();

                if (key == player.fireEvent) {
                    if (inGame) {
                        if (time - player.lastShoot > Commons.SHOTSPEED){
                            player.shot(shots, time);

                        }
                    }
                }
            }
        }
    }
}
