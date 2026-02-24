package ludo;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class LudoGame extends JFrame {

    private GameState state;
    private BoardPanel boardPanel;
    private SidePanel sidePanel;
    private JLabel bar;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new LudoGame().setup());
    }

    public LudoGame() {
        super("Ludo");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(false);

        state = new GameState(this);
        boardPanel = new BoardPanel(this, state);
        sidePanel = new SidePanel(this, state);

        ImageIcon icon = new ImageIcon("logo.png");
        setIconImage(icon.getImage());

        bar = new JLabel("Welcome click New Game!", SwingConstants.LEFT);
        bar.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        bar.setBackground(new Color(22, 22, 32));
        bar.setForeground(new Color(215, 215, 215));
        bar.setOpaque(true);
        bar.setBorder(BorderFactory.createEmptyBorder(7, 12, 7, 12));

        JPanel root = new JPanel(new BorderLayout());
        root.add(boardPanel, BorderLayout.CENTER);
        root.add(sidePanel, BorderLayout.EAST);
        root.add(bar, BorderLayout.SOUTH);
        add(root);

        setSize(Constants.WIN_W, Constants.WIN_H);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    public void setup() {
        JDialog d = new JDialog(this, "New Game", true);
        d.getContentPane().setBackground(new Color(18, 18, 32));
        d.setLayout(new GridBagLayout());
        GridBagConstraints gc = new GridBagConstraints();
        gc.insets = new Insets(16, 30, 16, 30);

        JLabel ttl = new JLabel("LUDO", SwingConstants.CENTER);
        ttl.setFont(new Font("Impact", Font.PLAIN, 52));
        ttl.setForeground(Color.WHITE);
        gc.gridwidth = 2;
        gc.gridx = 0;
        gc.gridy = 0;
        d.add(ttl, gc);

        JLabel plLbl = new JLabel("Players:", SwingConstants.CENTER);
        plLbl.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        plLbl.setForeground(new Color(190, 190, 210));
        gc.gridwidth = 1;
        gc.gridx = 0;
        gc.gridy = 1;
        d.add(plLbl, gc);

        JComboBox<Integer> nb = new JComboBox<>(new Integer[] { 2, 3, 4 });
        nb.setSelectedItem(4);
        nb.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        gc.gridx = 1;
        d.add(nb, gc);

        JCheckBox[] cb = new JCheckBox[4];
        for (int i = 0; i < 4; i++) {
            cb[i] = new JCheckBox(Constants.PN[i] + " is Human", i == 0);
            cb[i].setForeground(Constants.PC[i].brighter());
            cb[i].setBackground(new Color(18, 18, 32));
            cb[i].setFont(new Font("Segoe UI", Font.BOLD, 13));
            gc.gridwidth = 2;
            gc.gridx = 0;
            gc.gridy = 2 + i;
            d.add(cb[i], gc);
        }

        JButton go = btn("START", new Color(42, 148, 78));
        gc.gridy = 6;
        gc.gridwidth = 2;
        gc.gridx = 0;
        d.add(go, gc);

        go.addActionListener(e -> {
            state.np = (int) nb.getSelectedItem();
            for (int i = 0; i < 4; i++) {
                state.human[i] = cb[i].isSelected();
            }
            d.dispose();
            newGame();
        });

        d.pack();
        d.setLocationRelativeTo(this);
        d.setVisible(true);
    }

    private void newGame() {
        state.reset();
        boardPanel.repaint();
        sidePanel.repaint();
        say(Constants.PN[state.cur] + "'s turn — Roll the dice!");
        if (!state.human[state.cur]) {
            state.aiDelay();
        }
    }

    public JButton btn(String t, Color bg) {
        JButton b = new JButton(t);
        b.setBackground(bg);
        b.setForeground(Color.WHITE);
        b.setFont(new Font("Segoe UI", Font.BOLD, 14));
        b.setFocusPainted(false);
        b.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(bg.darker(), 2),
                BorderFactory.createEmptyBorder(9, 22, 9, 22)));
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        b.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                b.setBackground(bg.brighter());
            }

            public void mouseExited(MouseEvent e) {
                b.setBackground(bg);
            }
        });
        return b;
    }

    public void say(String s) {
        bar.setText("  " + s);
    }

    public BoardPanel getBoardPanel() {
        return boardPanel;
    }

    public SidePanel getSidePanel() {
        return sidePanel;
    }
}