package ludo;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.*;

public class SidePanel extends JPanel {

    private LudoGame game;
    private GameState state;

    public SidePanel(LudoGame game, GameState state) {
        this.game = game;
        this.state = state;

        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBackground(new Color(18, 18, 30));
        setPreferredSize(new Dimension(Constants.SIDE, Constants.BOARD));
        setBorder(BorderFactory.createEmptyBorder(22, 16, 22, 16));

        JLabel title = new JLabel("LUDO", SwingConstants.CENTER);
        title.setFont(new Font("Impact", Font.PLAIN, 42));
        title.setForeground(Color.WHITE);
        title.setAlignmentX(CENTER_ALIGNMENT);
        add(title);

        JLabel sub = new JLabel("Classic Board Game", SwingConstants.CENTER);
        sub.setFont(new Font("Segoe UI", Font.ITALIC, 12));
        sub.setForeground(new Color(130, 130, 160));
        sub.setAlignmentX(CENTER_ALIGNMENT);
        add(sub);

        add(Box.createRigidArea(new Dimension(0, 20)));

        DicePanel dp = new DicePanel();
        dp.setAlignmentX(CENTER_ALIGNMENT);
        add(dp);

        add(Box.createRigidArea(new Dimension(0, 14)));

        JButton rollBtn = game.btn("ROLL DICE", new Color(50, 148, 75));
        rollBtn.setAlignmentX(CENTER_ALIGNMENT);
        rollBtn.addActionListener(e -> state.roll());
        add(rollBtn); 

        add(Box.createRigidArea(new Dimension(0, 24)));

        JLabel pl = new JLabel("PLAYERS", SwingConstants.CENTER);
        pl.setFont(new Font("Segoe UI", Font.BOLD, 10));
        pl.setForeground(new Color(110, 110, 145));
        pl.setAlignmentX(CENTER_ALIGNMENT);
        add(pl);

        add(Box.createRigidArea(new Dimension(0, 8)));

        for (int p = 0; p < state.np; p++) {
            PlayerRow pr = new PlayerRow(p);
            pr.setAlignmentX(CENTER_ALIGNMENT);
            add(pr);
            add(Box.createRigidArea(new Dimension(0, 5)));
        }

        add(Box.createVerticalGlue());

        JButton ng = game.btn("NEW GAME", new Color(155, 50, 42));
        ng.setAlignmentX(CENTER_ALIGNMENT);
        ng.addActionListener(e -> game.setup());
        add(ng);
    }

    // ── Dice Panel ────────────────────────────────────────────────────────────
    class DicePanel extends JPanel {

        DicePanel() {
            setOpaque(false);
            setPreferredSize(new Dimension(100, 100));
            setMaximumSize(new Dimension(100, 100));
        }

        @Override
        protected void paintComponent(Graphics g0) {
            Graphics2D g = (Graphics2D) g0;
            g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            int sz = 90, ox = 5, oy = 5, arc = 18;
            g.setColor(new Color(0, 0, 0, 55));
            g.fill(new RoundRectangle2D.Float(ox + 3, oy + 4, sz, sz, arc, arc));
            GradientPaint gp = new GradientPaint(
                    ox, oy, new Color(252, 252, 248),
                    ox, oy + sz, new Color(215, 212, 205));
            g.setPaint(gp);
            g.fill(new RoundRectangle2D.Float(ox, oy, sz, sz, arc, arc));
            g.setPaint(null);
            g.setColor(new Color(165, 160, 150));
            g.setStroke(new BasicStroke(2));
            g.draw(new RoundRectangle2D.Float(ox, oy, sz, sz, arc, arc));
            if (state.dice > 0) {
                g.setColor(new Color(22, 22, 38));
                int[][] dots = Constants.DICE_DOTS[state.dice - 1];
                for (int i = 0; i < dots.length; i++) {
                    int dx = ox + (int) (dots[i][0] * sz / 4.5);
                    int dy = oy + (int) (dots[i][1] * sz / 4.5);
                    g.fillOval(dx - 7, dy - 7, 14, 14);
                }
            } else {
                g.setColor(new Color(155, 152, 144));
                g.setFont(new Font("Segoe UI", Font.BOLD, 14));
                FontMetrics fm = g.getFontMetrics();
                String str = "Roll!";
                g.drawString(str, ox + sz / 2 - fm.stringWidth(str) / 2, oy + sz / 2 + 5);
            }
        }
    }

    // ── Player Row ────────────────────────────────────────────────────────────
    class PlayerRow extends JPanel {

        int p;

        PlayerRow(int player) {
            this.p = player;
            setOpaque(false);
            setMaximumSize(new Dimension(Constants.SIDE - 32, 50));
            setPreferredSize(new Dimension(Constants.SIDE - 32, 50));
            setBorder(BorderFactory.createEmptyBorder(3, 6, 3, 6));
        }

        @Override
        protected void paintComponent(Graphics g0) {
            Graphics2D g = (Graphics2D) g0;
            g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            boolean active = (p == state.cur && !state.over);
            if (active) {
                g.setColor(
                        new Color(Constants.PC[p].getRed(), Constants.PC[p].getGreen(), Constants.PC[p].getBlue(), 38));
                g.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 10, 10);
                g.setColor(Constants.PC[p]);
                g.setStroke(new BasicStroke(2));
                g.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 10, 10);
            } else {
                g.setColor(new Color(255, 255, 255, 8));
                g.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 10, 10);
            }
            g.setColor(Constants.PC[p]);
            g.fillOval(8, 14, 20, 20);
            g.setColor(Constants.PC[p].darker());
            g.setStroke(new BasicStroke(1.5f));
            g.drawOval(8, 14, 20, 20);
            g.setColor(active ? Constants.PC[p].brighter() : new Color(180, 180, 200));
            g.setFont(new Font("Segoe UI", Font.BOLD, 13));
            g.drawString(Constants.PN[p] + (state.human[p] ? "" : " AI"), 36, 21);
            int done = 0;
            for (int i = 0; i < 4; i++) {
                if (state.pos[p][i] == 2000) {
                    done++;
                }
            }
            for (int i = 0; i < 4; i++) {
                boolean fin = (i < done);
                g.setColor(fin ? Constants.PC[p] : new Color(70, 70, 90));
                g.fillOval(36 + i * 18, 28, 13, 13);
                if (!fin) {
                    g.setColor(new Color(90, 90, 115));
                    g.setStroke(new BasicStroke(1));
                    g.drawOval(36 + i * 18, 28, 13, 13);
                }
            }
            if (active) {
                g.setColor(Constants.PC[p]);
                g.fillPolygon(
                        new int[] { getWidth() - 14, getWidth() - 5, getWidth() - 14 },
                        new int[] { 14, 24, 34 }, 3);
            }
        }
    }
}