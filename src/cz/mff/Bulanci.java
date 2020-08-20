package cz.mff;

import java.awt.EventQueue;
import javax.swing.JFrame;

public class Bulanci extends JFrame {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    public Bulanci() {

        initUI();
    }

    private void initUI() {

        add(new Board());

        setTitle("Bulanci");
        setSize(Commons.BOARD_WIDTH, Commons.BOARD_HEIGHT);

        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(false);
        setLocationRelativeTo(null);
    }

    public static void main(String[] args) {

        EventQueue.invokeLater(() -> {

            var ex = new Bulanci();
            ex.setVisible(true);
        });
    }
}
