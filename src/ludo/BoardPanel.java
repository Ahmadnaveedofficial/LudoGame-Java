package ludo;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;

public class BoardPanel extends JPanel {

    private LudoGame game;
    private GameState state;

    public BoardPanel(LudoGame game, GameState state) {
        this.game = game;
        this.state = state;
        setPreferredSize(new Dimension(Constants.BOARD, Constants.BOARD));
        addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                if (!state.rolled || state.over || state.animP != -1 || !state.human[state.cur]) {
                    return;
                }
                for (int t = 0; t < 4; t++) {
                    Point p = state.tokenPx(state.cur, t);
                    if (p != null && Math.hypot(e.getX() - p.x, e.getY() - p.y) < Constants.CELL * 0.44) {
                        state.clickToken(state.cur, t);
                        return;
                    }
                }
            }
        });
    }

    @Override
    protected void paintComponent(Graphics g0) {
        Graphics2D g = (Graphics2D) g0;
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);

        g.setColor(Constants.C_BG);
        g.fillRect(0, 0, Constants.BOARD, Constants.BOARD);

        paintYard(g, 0, 0, Constants.C_RED, Constants.YARD[0]); // Red, Blue, Green, Yellow ke home areas.
        paintYard(g, 0, 9, Constants.C_BLUE, Constants.YARD[1]);
        paintYard(g, 9, 9, Constants.C_GREEN, Constants.YARD[2]);
        paintYard(g, 9, 0, Constants.C_YELLOW, Constants.YARD[3]);

        g.setColor(Constants.C_WHITE);
        for (int i = 0; i < Constants.TRACK.length; i++) { // cell drawing order: 0-51, home columns, yards
            g.fillRect(Constants.TRACK[i][1] * Constants.CELL + 1,
                    Constants.TRACK[i][0] * Constants.CELL + 1,
                    Constants.CELL - 1, Constants.CELL - 1);
        }

        for (int p = 0; p < 4; p++) {
            g.setColor(Constants.HOMECOL_COLOR[p]);
            for (int s = 0; s < Constants.HOMECOL[p].length; s++) {
                g.fillRect(Constants.HOMECOL[p][s][1] * Constants.CELL + 1,
                        Constants.HOMECOL[p][s][0] * Constants.CELL + 1,
                        Constants.CELL - 1, Constants.CELL - 1);
            }
        }

        paintCentre(g);

        g.setColor(Constants.C_GRID);
        g.setStroke(new BasicStroke(0.6f));
        for (int i = 0; i <= 15; i++) {
            g.drawLine(i * Constants.CELL, 0, i * Constants.CELL, Constants.BOARD);
            g.drawLine(0, i * Constants.CELL, Constants.BOARD, i * Constants.CELL);
        }
 
        g.setColor(Constants.C_DARK);
        g.setStroke(new BasicStroke(3f));
        g.drawRect(1, 1, Constants.BOARD - 2, Constants.BOARD - 2);
        g.drawRect(0, 0, 6 * Constants.CELL, 6 * Constants.CELL);
        g.drawRect(9 * Constants.CELL, 0, 6 * Constants.CELL, 6 * Constants.CELL);
        g.drawRect(9 * Constants.CELL, 9 * Constants.CELL, 6 * Constants.CELL, 6 * Constants.CELL);
        g.drawRect(0, 9 * Constants.CELL, 6 * Constants.CELL, 6 * Constants.CELL);
        g.drawRect(6 * Constants.CELL, 6 * Constants.CELL, 3 * Constants.CELL, 3 * Constants.CELL);

        for (int i = 0; i < Constants.TRACK.length; i++) {
            if (Constants.SAFE[i]) {
                drawStar(g,
                        Constants.TRACK[i][1] * Constants.CELL + Constants.CELL / 2,
                        Constants.TRACK[i][0] * Constants.CELL + Constants.CELL / 2,
                        Constants.CELL * 0.32f,
                        new Color(255, 195, 0, 210));
            }
        }

        paintArrow(g, Constants.TRACK[0][0], Constants.TRACK[0][1], 0, Constants.C_RED);
        paintArrow(g, Constants.TRACK[13][0], Constants.TRACK[13][1], 1, Constants.C_BLUE);
        paintArrow(g, Constants.TRACK[26][0], Constants.TRACK[26][1], 2, Constants.C_GREEN);
        paintArrow(g, Constants.TRACK[39][0], Constants.TRACK[39][1], 3, Constants.C_YELLOW);

        paintHighlights(g);
        paintTokens(g);
    }

    private void paintYard(Graphics2D g, int r0, int c0, Color col, int[][] slots) {
        g.setColor(col);
        g.fillRect(c0 * Constants.CELL, r0 * Constants.CELL, 6 * Constants.CELL, 6 * Constants.CELL);
        g.setColor(Constants.C_WHITE);
        g.fillRect((c0 + 1) * Constants.CELL, (r0 + 1) * Constants.CELL, 4 * Constants.CELL, 4 * Constants.CELL);
        for (int i = 0; i < slots.length; i++) {
            int cx = slots[i][1] * Constants.CELL + Constants.CELL / 2;
            int cy = slots[i][0] * Constants.CELL + Constants.CELL / 2;
            int r = Constants.CELL * 2 / 5;
            g.setColor(new Color(0, 0, 0, 35));
            g.fillOval(cx - r + 2, cy - r + 2, r * 2, r * 2);
            g.setColor(col);
            g.fillOval(cx - r, cy - r, r * 2, r * 2);
            g.setColor(new Color(255, 255, 255, 140));
            g.fillOval(cx - r + r / 4, cy - r + r / 5, r + r / 3, r);
            g.setColor(col.darker());
            g.setStroke(new BasicStroke(2f));
            g.drawOval(cx - r, cy - r, r * 2, r * 2);
        }
    }

    private void paintCentre(Graphics2D g) {
        int x0 = 6 * Constants.CELL, y0 = 6 * Constants.CELL, s = 3 * Constants.CELL;
        int cx = x0 + s / 2, cy = y0 + s / 2;
        int[][] corners = { { x0, y0 }, { x0 + s, y0 }, { x0 + s, y0 + s }, { x0, y0 + s } };
        // i=0: top=Blue, i=1: right=Green, i=2: bottom=Yellow, i=3: left=Red
        Color[] tc = { Constants.C_BLUE, Constants.C_GREEN, Constants.C_YELLOW, Constants.C_RED };
        for (int i = 0; i < 4; i++) {
            int[] a = corners[i];
            int[] b = corners[(i + 1) % 4];
            Polygon tri = new Polygon(
                    new int[] { a[0], cx, b[0] },
                    new int[] { a[1], cy, b[1] }, 3);
            g.setColor(tc[i]);
            g.fillPolygon(tri);
            g.setColor(new Color(0, 0, 0, 55));
            g.setStroke(new BasicStroke(1));
            g.drawPolygon(tri);
        }
        g.setColor(Color.WHITE);
        g.fillOval(cx - Constants.CELL / 2, cy - Constants.CELL / 2, Constants.CELL, Constants.CELL);
        g.setColor(new Color(70, 50, 30));
        g.setFont(new Font("Segoe UI", Font.BOLD, 10));
        FontMetrics fm = g.getFontMetrics();
        String h = "HOME";
        g.drawString(h, cx - fm.stringWidth(h) / 2, cy + fm.getAscent() / 2 - 1);
    }

    private void drawStar(Graphics2D g, int cx, int cy, float r, Color c) {
        g.setColor(c);
        double in = r * 0.42;
        Polygon star = new Polygon();
        for (int i = 0; i < 10; i++) {
            double a = Math.toRadians(i * 36 - 90);
            double rd = (i % 2 == 0) ? r : in;
            star.addPoint((int) (cx + rd * Math.cos(a)), (int) (cy + rd * Math.sin(a)));
        }
        g.fillPolygon(star);
        g.setColor(new Color(180, 130, 0, 160));
        g.setStroke(new BasicStroke(1));
        g.drawPolygon(star);
    }

    private void paintArrow(Graphics2D g, int row, int col, int dir, Color c) {
        int cx = col * Constants.CELL + Constants.CELL / 2;
        int cy = row * Constants.CELL + Constants.CELL / 2;
        int s = Constants.CELL / 3;
        g.setColor(new Color(c.getRed(), c.getGreen(), c.getBlue(), 190));
        Polygon p = new Polygon();
        switch (dir) {
            case 0: {
                p.addPoint(cx - s, cy - s / 2);
                p.addPoint(cx + s, cy);
                p.addPoint(cx - s, cy + s / 2);
                break;
            }
            case 1: {
                p.addPoint(cx - s / 2, cy - s);
                p.addPoint(cx, cy + s);
                p.addPoint(cx + s / 2, cy - s);
                break;
            }
            case 2: {
                p.addPoint(cx + s, cy - s / 2);
                p.addPoint(cx - s, cy);
                p.addPoint(cx + s, cy + s / 2);
                break;
            }
            case 3: {
                p.addPoint(cx - s / 2, cy + s);
                p.addPoint(cx, cy - s);
                p.addPoint(cx + s / 2, cy + s);
                break;
            }
        }
        g.fillPolygon(p);
    }

    private void paintHighlights(Graphics2D g) {
        if (!state.rolled || state.over || state.animP != -1 || !state.human[state.cur]) {
            return;
        }
        float[] dash = { 7f, 4f };
        int[][] mv = new int[4][2];
        int mc = state.fillMoves(state.cur, state.dice, mv);
        for (int i = 0; i < mc; i++) {
            Point pt = state.tokenPx(state.cur, mv[i][0]);
            if (pt == null) {
                continue;
            }
            float r = Constants.CELL * 0.44f;
            g.setColor(new Color(255, 220, 0, 65));
            g.fill(new Ellipse2D.Float(pt.x - r, pt.y - r, r * 2, r * 2));
            g.setColor(new Color(255, 175, 0));
            g.setStroke(new BasicStroke(2.5f, BasicStroke.CAP_ROUND,
                    BasicStroke.JOIN_ROUND, 0, dash, 0));
            g.draw(new Ellipse2D.Float(pt.x - r, pt.y - r, r * 2, r * 2));
        }
        g.setStroke(new BasicStroke(1));
    }

    private void paintTokens(Graphics2D g) {
        for (int p = 0; p < state.np; p++) {
            for (int t = 0; t < 4; t++) {
                paintToken(g, p, t);
            }
        }
    }

    private void paintToken(Graphics2D g, int player, int token) {
        Point pt = state.tokenPx(player, token);
        if (pt == null) {
            return;
        }
        boolean anim = (state.animP == player && state.animT == token);
        float r = Constants.CELL * 0.35f;
        float x = pt.x - r;
        float y = pt.y - r;
        Color col = Constants.PC[player];
        g.setColor(new Color(0, 0, 0, 48));
        g.fill(new Ellipse2D.Float(x + 2, y + 3, r * 2, r * 2));
        g.setColor(col);
        g.fill(new Ellipse2D.Float(x, y, r * 2, r * 2));
        GradientPaint gp = new GradientPaint(
                pt.x - r * 0.3f, pt.y - r * 0.45f, new Color(255, 255, 255, 170),
                pt.x + r * 0.2f, pt.y + r * 0.5f, new Color(255, 255, 255, 0));
        g.setPaint(gp);
        g.fill(new Ellipse2D.Float(x + r * 0.1f, y + r * 0.05f, r * 1.5f, r));
        g.setPaint(null);
        g.setColor(anim ? Color.WHITE : col.darker());
        g.setStroke(new BasicStroke(anim ? 3f : 2f));
        g.draw(new Ellipse2D.Float(x + 0.5f, y + 0.5f, r * 2 - 1, r * 2 - 1));
        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.BOLD, (int) (r * 0.9f)));
        FontMetrics fm = g.getFontMetrics();
        String s = String.valueOf(token + 1);
        g.drawString(s, pt.x - fm.stringWidth(s) / 2, pt.y + fm.getAscent() / 2 - 2);
    }
}