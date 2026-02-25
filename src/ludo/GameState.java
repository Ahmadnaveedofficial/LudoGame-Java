package ludo;

import java.awt.Point;
import java.util.Arrays;
import javax.swing.*;

public class GameState {

    public int[][] pos = new int[4][4];

    public int cur = 0;
    public int np = 4;
    public int dice = 0;
    public boolean rolled = false;
    public boolean over = false;
    public boolean[] human = { true, false, false, false };

    public int animP = -1, animT = -1, animIdx = 0;
    public int[] animPath = new int[60];
    public int animLen = 0;

    public Timer moveTimer, diceTimer;
    public int diceAnim = 0, diceTgt = 0;

    // Reference to main game window for callbacks
    private LudoGame game;

    public GameState(LudoGame game) {
        this.game = game;
        for (int i = 0; i < pos.length; i++) {
            Arrays.fill(pos[i], -1);
        }
    }

    public void reset() {
        for (int i = 0; i < pos.length; i++) {
            Arrays.fill(pos[i], -1);
        }
        dice = 0;
        rolled = false;
        cur = 0;
        over = false;
        animP = -1;
        animT = -1;
    }

    public void roll() {
        if (rolled || over || animP != -1) {
            return;
        }
        diceTgt = 1 + (int) (Math.random() * 6);
        diceAnim = 0;
        diceTimer = new Timer(55, null);
        diceTimer.addActionListener(e -> {
            dice = 1 + (int) (Math.random() * 6);
            game.getSidePanel().repaint();
            if (++diceAnim >= 12) {
                dice = diceTgt;
                diceTimer.stop();
                rolled = true;
                game.getSidePanel().repaint();
                afterRoll();
            }
        });
        diceTimer.start();
    }

    private void afterRoll() {
        int[][] mv = new int[4][2];
        int mc = fillMoves(cur, dice, mv);
        if (mc == 0) {
            game.say(Constants.PN[cur] + " rolled " + dice + " — no moves, turn passes.");
            new Timer(1200, e -> next()) {
                {
                    setRepeats(false);
                }
            }.start();
        } else {
            game.say(Constants.PN[cur] + " rolled " + dice + " — choose a token.");
            game.getBoardPanel().repaint();
            if (!human[cur]) {
                new Timer(900, e -> aiMove()) {
                    {
                        setRepeats(false);
                    }
                }.start();
            }
        }
    }

    public int fillMoves(int player, int d, int[][] mv) {
        int count = 0;
        for (int t = 0; t < 4; t++) {
            if (pos[player][t] == 2000) {
                continue;
            }
            if (pos[player][t] == -1) {
                if (d == 6) {
                    mv[count][0] = t;
                    mv[count][1] = Constants.LAUNCH[player];
                    count++;
                }
            } else {
                int tgt = target(player, pos[player][t], d);
                if (tgt != -999) {
                    mv[count][0] = t;
                    mv[count][1] = tgt;
                    count++;
                }
            }
        }
        return count;
    }

    public int target(int player, int cur, int d) {
        if (cur >= 1000) {
            int step = cur - 1000;
            int ns = step + d;
            if (ns == 5) {
                return 2000;
            }
            if (ns > 5) {
                return -999;
            }
            return 1000 + ns;
        }
        int entry = Constants.HOME_ENTRY[player];
        int dist = (entry - cur + 52) % 52;
        if (dist == 0) {
            dist = 52;
        }
        if (d < dist) {
            return (cur + d) % 52;
        } else if (d == dist) {
            return 1000;
        } else {
            int extra = d - dist - 1;
            if (extra == 4) {
                return 2000;
            }
            if (extra < 4) {
                return 1000 + extra;
            }
            return -999;
        }
    }

    public void clickToken(int p, int t) {
        if (!rolled || over || p != cur || !human[p]) {
            return;
        }
        int[][] mv = new int[4][2];
        int mc = fillMoves(p, dice, mv);
        for (int i = 0; i < mc; i++) {
            if (mv[i][0] == t) {
                doMove(p, t, mv[i][1]);
                return;
            }
        }
    }

    private void aiMove() {
        int[][] mv = new int[4][2];
        int mc = fillMoves(cur, dice, mv);
        if (mc == 0) {
            next();
            return;
        }
        int bestToken = mv[0][0];
        int bestTgt = mv[0][1];
        for (int i = 0; i < mc; i++) {
            if (mv[i][1] == 2000) {
                bestToken = mv[i][0];
                bestTgt = mv[i][1];
                break;
            }
            if (canCapture(cur, mv[i][1])) {
                bestToken = mv[i][0];
                bestTgt = mv[i][1];
            }
        }
        doMove(cur, bestToken, bestTgt);
    }

    public boolean canCapture(int player, int tgt) {
        if (tgt >= 1000 || Constants.SAFE[tgt]) {
            return false;
        }
        for (int p = 0; p < np; p++) {
            if (p == player) {
                continue;
            }
            for (int t = 0; t < 4; t++) {
                if (pos[p][t] == tgt) {
                    return true;
                }
            }
        }
        return false;
    }

    public void doMove(int player, int token, int tgt) {
        rolled = false;
        animLen = 0;
        int c = pos[player][token];
        if (c == -1) {
            animPath[animLen++] = Constants.LAUNCH[player];
        } else if (c < 1000) {
            int entry = Constants.HOME_ENTRY[player];
            int dist = (entry - c + 52) % 52;
            if (dist == 0) {
                dist = 52;
            }
            if (tgt < 1000) {
                int steps = (tgt - c + 52) % 52;
                for (int i = 1; i <= steps; i++) {
                    animPath[animLen++] = (c + i) % 52;
                }
            } else {
                for (int i = 1; i <= dist; i++) {
                    animPath[animLen++] = (c + i) % 52;
                }
                int endStep = (tgt == 2000) ? 4 : tgt - 1000;
                for (int s = 0; s <= endStep; s++) {
                    animPath[animLen++] = 1000 + s;
                }
                if (tgt == 2000) {
                    animPath[animLen++] = 2000;
                }
            }
        } else {
            int step = c - 1000;
            int end = (tgt == 2000) ? 4 : tgt - 1000;
            for (int s = step + 1; s <= end; s++) {
                animPath[animLen++] = 1000 + s;
            }
            if (tgt == 2000) {
                animPath[animLen++] = 2000;
            }
        }
        if (animLen == 0) {
            animPath[animLen++] = tgt;
        }
        animP = player;
        animT = token;
        animIdx = 0;
        moveTimer = new Timer(115, null);
        moveTimer.addActionListener(e -> {
            if (animIdx < animLen) {
                pos[player][token] = animPath[animIdx++];
                game.getBoardPanel().repaint();
            } else {
                moveTimer.stop();
                pos[player][token] = tgt;
                animP = -1;
                animT = -1;
                onLand(player, token, tgt);
            }
        });
        moveTimer.start();
    }

    private void onLand(int player, int token, int tgt) {
        if (tgt < 1000 && !Constants.SAFE[tgt]) {
            for (int p = 0; p < np; p++) {
                if (p == player) {
                    continue;
                }
                for (int t = 0; t < 4; t++) {
                    if (pos[p][t] == tgt) {
                        pos[p][t] = -1;
                        game.say(Constants.PN[player] + " captured " + Constants.PN[p] + "'s token! 💥");
                    }
                }
            }
        }
        if (tgt == 2000) {
            boolean done = true;
            for (int i = 0; i < 4; i++) {
                if (pos[player][i] != 2000) {
                    done = false;
                    break;
                }
            }
            if (done) {
                over = true;
                game.say("🏆 " + Constants.PN[player] + " WINS!");
                game.getBoardPanel().repaint();
                new Timer(1800, e -> JOptionPane.showMessageDialog(game, "🏆 " + Constants.PN[player] + " WINS!")) {
                    {
                        setRepeats(false);
                    }
                }.start();
                return;
            }
        }
        game.getBoardPanel().repaint();
        if (dice == 6) {
            game.say(Constants.PN[player] + " rolled 6 — Roll again!");
            if (!human[player]) {
                aiDelay();
            }
        } else {
            next();
        }
    }

    public void next() {
        int tries = 0;
        do {
            cur = (cur + 1) % np;
            tries++;
        } while (allDone(cur) && tries < 4);
        rolled = false;
        game.say(Constants.PN[cur] + "'s turn — Roll the dice!");
        game.getSidePanel().repaint();
        game.getBoardPanel().repaint();
        if (!human[cur]) {
            aiDelay();
        }
    }

    public boolean allDone(int p) {
        for (int i = 0; i < 4; i++) {
            if (pos[p][i] != 2000) {
                return false;
            }
        }
        return true;
    }

    public void aiDelay() {
        new Timer(700, e -> roll()) {
            {
                setRepeats(false);
            }
        }.start();
    }

    public Point tokenPx(int player, int token) {
        int p = pos[player][token];
        if (p == -1) {
            int[] s = Constants.YARD[player][token];
            return new Point(s[1] * Constants.CELL + Constants.CELL / 2,
                    s[0] * Constants.CELL + Constants.CELL / 2);
        }
        if (p == 2000) {
            int[][] off = { { -12, -12 }, { 12, -12 }, { 12, 12 }, { -12, 12 } };
            int cx = 7 * Constants.CELL + Constants.CELL / 2;
            int cy = 7 * Constants.CELL + Constants.CELL / 2;
            return new Point(cx + off[player][0], cy + off[player][1]);
        }
        if (p >= 1000) {
            int[] rc = Constants.HOMECOL[player][p - 1000];
            return new Point(rc[1] * Constants.CELL + Constants.CELL / 2,
                    rc[0] * Constants.CELL + Constants.CELL / 2);
        }
        int[] rc = Constants.TRACK[p];
        int bx = rc[1] * Constants.CELL + Constants.CELL / 2;
        int by = rc[0] * Constants.CELL + Constants.CELL / 2;
        int cnt = 0, idx = 0;
        for (int pp = 0; pp < np; pp++) {
            for (int tt = 0; tt < 4; tt++) {
                if (pos[pp][tt] == p) {
                    if (pp == player && tt == token) {
                        idx = cnt;
                    }
                    cnt++;
                }
            }
        }
        if (cnt > 1) {
            int[] ox = { -10, 10, -10, 10 };
            int[] oy = { -10, -10, 10, 10 };
            return new Point(bx + ox[idx % 4], by + oy[idx % 4]);
        }
        return new Point(bx, by);
    }
} 