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
    /**
     *
     */
    private static final long serialVersionUID = 1L;

    private enum states {
        INGAME, MENU, ENDGAME
    }

    private states state = states.MENU;
    private Dimension d;
    private List<Exodus> exoduses = new ArrayList<>();
    private ArrayList<Player> players = new ArrayList<>();
    private ArrayList<Shot> shots = new ArrayList<>();
    private ArrayList<Block> blocks = new ArrayList<>();
    private ArrayList<LifeBar> lifeBars = new ArrayList<>();
    private int kills;
    private int maxKills;

    // private boolean inGame = true;
    private final String explImg = "src/images/explosion%d.png";
    private final long maxTime = 2000;
    
    private String message;
    private Timer timer;
    private long time;
    private Image BackImage;

    /// buttons
    JButton Start;
    JButton Retry;
    JButton Exit;

    public Board() {
        initBoard();
    }

    private void initBoard() {
        addKeyListener(new TAdapter());
        setFocusable(true);
        d = new Dimension(Commons.BOARD_WIDTH, Commons.BOARD_HEIGHT);
        setBackground(Color.black);
        timer = new Timer(Commons.DELAY, new GameCycle());
        timer.start();
        
        menuInit();
    }
    
    /**
     * Resets all resources and loads new map
     */
    private void gameClear() {
        players = new ArrayList<>();
        lifeBars = new ArrayList<>();
        exoduses = new ArrayList<>();
        shots = new ArrayList<>();
        blocks = new ArrayList<>();
        message = "Game Over";
        time = 0;
        kills = 0;
        timer = new Timer(Commons.DELAY, new GameCycle());
        timer.start();
    }
    
    private void initMap1() {
        gameClear();
        BackImage = new ImageIcon(Commons.Map1).getImage();

        blocks.add(new Block(95, 303, true, this));
        players.add(new Player(Commons.PLAYER_X, Commons.PLAYER_Y, 0, this));
        lifeBars.add(new LifeBar(0, d.height - 60, players.get(0), this));
        maxKills = 4;

        int xi, yi;
        for (int i = 0; i < 4; i++) {
            do {
                xi = (int) (Math.random() * (Commons.BOARD_WIDTH - Commons.BASE_WIDTH));
                yi = (int) (Math.random() * (Commons.BOARD_HEIGHT - Commons.BASE_WIDTH));
            } while (Blocked(xi, yi));

            final var exodus = new Exodus(xi, yi, this);
            exoduses.add(exodus);
        }
    }

    private boolean Blocked(final int x, final int y) {
        for (final var exodus : exoduses) {
            if (exodus.collideXY(x, y)) {
                return true;
            }
        }
        for (final var block : blocks) {
            if (block.collideXY(x, y)) {
                return true;
            }
        }
        for (final var player : players) {
            if (player.collideXY(x, y)) {
                return true;
            }
        }
        return false;
    }

    private void drawExoduses(final Graphics g) {

        for (final Exodus exodus : exoduses) {

            g.drawImage(exodus.getImage(), exodus.getX(), exodus.getY(), this);
            g.drawImage(exodus.getImageOfWeapon(), exodus.getX(), exodus.getY(), this);

        }
    }

    private void drawPlayers(final Graphics g) {
        for (final var player : players) {
            g.drawImage(player.getImage(), player.getX(), player.getY(), this);
            g.drawImage(player.getImageOfWeapon(), player.getX(), player.getY(), this);

        }
    }

    private void drawShots(final Graphics g) {
        for (final var shot : shots) {
            g.drawImage(shot.getImage(), shot.getX(), shot.getY(), this);
        }
    }

    private void drawLifeBars(final Graphics g) {
        for (final var lb : lifeBars) {
            g.drawImage(lb.getImage(), lb.getX(), lb.getY(), this);
        }
    }

    private void drawBlocks(final Graphics g) {
        for (final var block : blocks) {
            if (block.visible)
                g.drawImage(block.getImage(), block.getX(), block.getY(), this);
        }
    }

    @Override
    public void paintComponent(final Graphics g) {
        super.paintComponent(g);
        doDrawing(g);
    }

    private void doDrawing(final Graphics g) {

        g.setColor(Color.black);
        g.fillRect(0, 0, d.width, d.height);
        g.drawImage(BackImage, 0, 0, this);
        g.drawString(String.valueOf(kills), 0, 25);
        g.drawString(String.valueOf((maxTime - time) / 10), 50, 50);

        switch (state) {
            case INGAME -> {
                drawExoduses(g);
                drawPlayers(g);
                drawShots(g);
                drawBlocks(g);
                drawLifeBars(g);
            }
            case MENU -> {
                if (timer.isRunning()) {
                    timer.stop();
                }
                drawMenu(g);
            }

            case ENDGAME -> {
                if (timer.isRunning()) {
                    timer.stop();
                }
                drawGameOver(g);
            }
        }
        Toolkit.getDefaultToolkit().sync();
    }

    private void menuInit() {

        Start = new JButton("Start");
        Start.addActionListener(e -> {
            setState(states.INGAME);
            initMap1();
        });
        this.add(Start);

        Retry = new JButton("Retry");
        Retry.addActionListener(e -> {
            setState(states.INGAME);
            initMap1();
        });
        Retry.setVisible(false);
        this.add(Retry);

        Exit = new JButton("Exit");
        this.add(Exit);
        Exit.addActionListener(e -> System.exit(0));

    }

    /**
     * Prints end screen of the game
     */
    private void drawMenu(final Graphics g) {

        final var small = new Font("Helvetica", Font.BOLD, 14);
        final var fontMetrics = this.getFontMetrics(small);

        g.setColor(new Color(0, 32, 48));
        g.fillRect(50, Commons.BOARD_WIDTH / 2 - 30, Commons.BOARD_WIDTH - 100, 50);
        g.setColor(Color.white);
        g.drawRect(50, Commons.BOARD_WIDTH / 2 - 30, Commons.BOARD_WIDTH - 100, 50);

        g.setColor(Color.white);
        g.setFont(small);
        g.drawString("Welcome", (Commons.BOARD_WIDTH - fontMetrics.stringWidth("Welcome")) / 2,
                Commons.BOARD_WIDTH / 2);
    }

    /**
     * Prints end screen of the game
     */
    private void drawGameOver(final Graphics g) {
        g.setColor(Color.black);
        g.fillRect(0, 0, Commons.BOARD_WIDTH, Commons.BOARD_HEIGHT);

        g.setColor(new Color(0, 32, 48));
        g.fillRect(50, Commons.BOARD_WIDTH / 2 - 30, Commons.BOARD_WIDTH - 100, 50);
        g.setColor(Color.white);
        g.drawRect(50, Commons.BOARD_WIDTH / 2 - 30, Commons.BOARD_WIDTH - 100, 50);

        final var small = new Font("Helvetica", Font.BOLD, 14);
        final var fontMetrics = this.getFontMetrics(small);

        g.setColor(Color.white);
        g.setFont(small);
        g.drawString(message, (Commons.BOARD_WIDTH - fontMetrics.stringWidth(message)) / 2, Commons.BOARD_WIDTH / 2);
    }

    private void update() {
        if (state != states.INGAME)
            return;
        for (final var player : players){
            if (player.isDying()) {
                setState(states.ENDGAME);
                break;
            }
        }
        if (kills == maxKills) {
            setState(states.ENDGAME);
            timer.stop();
            message = "Game won!";
            return;
        } else if (time > maxTime) {
            setState(states.ENDGAME);
            timer.stop();
            message = "Game lost!, no time left";
            return;
        } else if (players.size() == 0) {
            setState(states.ENDGAME);
            timer.stop();
            message = "Game lost!";
            return;
        }

        exoduses.removeIf(Sprite::isDying);
        players.removeIf(Sprite::isDying);

        // player
        for (final var player : players) {
            player.act();
        }

        for (final var sh : shots) {
            sh.update();
        }

        for (final LifeBar lifeBar : lifeBars) {
            lifeBar.update();
        }

        // shot
        for (final var shot : new ArrayList<>(shots)) {
            if (shot.isDying()) {
                shots.remove(shot);
                continue;
            }
            for (final Exodus exodus : exoduses) {
                if (exodus.collide(shot)) {
                    exodus.loadImage(explImg);
                    exodus.setDying(true);
                    kills++;
                    shots.remove(shot);
                }
            }
            for (final var player : players) {
                if (player.collide(shot)) {
                    shots.remove(shot);
                    player.hp -= Commons.SHOTDMG;
                    if (player.hp <= 0) {
                        player.loadImage(explImg);
                        player.setDying(true);
                    }
                }
            }
            for (final var block : blocks) {
                if (block.collide(shot)) {
                    shots.remove(shot);
                }
            }
        }

        for (final Exodus exodus : exoduses) {

            for (final var player : players) {
                if (exodus.inLine(player)) {
                    if (exodus.tryShot(time)) {
                        exodus.shot(shots, time);
                        continue;
                    }
                }
                exodus.act();
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
    public boolean collideWithOthers(final Sprite sprite) {
        /// black magick probably right
        if (!sprite.collidable)
            return false;

        for (final var block : blocks) {
            if (sprite.collide(block))
                return true;
        }

        for (final var exodus : exoduses) {
            if (sprite.collide(exodus))
                return true;
        }

        for (final var player : players) {
            if (sprite.collide(player))
                return true;
        }
        return false;
    }

    private class GameCycle implements ActionListener {

        @Override
        public void actionPerformed(final ActionEvent e) {
            doGameCycle();
        }
    }

    private class TAdapter extends KeyAdapter {

        @Override
        public void keyReleased(final KeyEvent e) {
            for (final var player : players) {
                player.keyReleased(e);
            }
        }

        @Override
        public void keyPressed(final KeyEvent e) {
            for (final var player : players) {
                player.keyPressed(e);
                
                final int key = e.getKeyCode();
                if (key == player.fireEvent) {
                    if (getState() == states.INGAME) {
                        if (time - player.lastShoot > Commons.SHOTSPEED) {
                            player.shot(shots, time);
                        }
                    }
                }
            }
        }
    }

    private void setState(final states state) {
        if (state == states.INGAME && Exit.isVisible()) {
            Exit.setVisible(false);
            Start.setVisible(false);
            Retry.setVisible(false);
        } else if (state != states.INGAME) {
            Exit.setVisible(true);
            if (state == states.MENU) {
                Start.setVisible(true);
            } else {
                Retry.setVisible(true);
            }
        }
        this.state = state;
    }

    private states getState() {
        return state;
    }
}
