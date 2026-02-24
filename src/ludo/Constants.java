package ludo;

import java.awt.Color;

public class Constants {

        public static final int CELL = 56;
        public static final int BOARD = CELL * 15;     // 840
        public static final int SIDE = 260;
        public static final int WIN_W = BOARD + SIDE;    // 1100 
        public static final int WIN_H = BOARD + 54;        // 894

        public static final Color C_RED = new Color(210, 40, 40);
        public static final Color C_BLUE = new Color(30, 90, 195);
        public static final Color C_GREEN = new Color(30, 160, 70);
        public static final Color C_YELLOW = new Color(220, 170, 10);
        public static final Color C_WHITE = new Color(252, 250, 244);
        public static final Color C_BG = new Color(242, 237, 222);
        public static final Color C_GRID = new Color(175, 165, 148);
        public static final Color C_DARK = new Color(55, 42, 25);

        public static final Color[] PC = { C_RED, C_BLUE, C_GREEN, C_YELLOW };
        public static final String[] PN = { "Red", "Blue", "Green", "Yellow" };

        public static final int[][] TRACK = {
                        { 6, 1 }, { 6, 2 }, { 6, 3 }, { 6, 4 }, { 6, 5 },
                        { 5, 6 }, { 4, 6 }, { 3, 6 }, { 2, 6 }, { 1, 6 },
                        { 0, 6 }, { 0, 7 }, { 0, 8 },
                        { 1, 8 }, { 2, 8 }, { 3, 8 }, { 4, 8 }, { 5, 8 },
                        { 6, 9 }, { 6, 10 }, { 6, 11 }, { 6, 12 }, { 6, 13 },
                        { 6, 14 }, { 7, 14 }, { 8, 14 },
                        { 8, 13 }, { 8, 12 }, { 8, 11 }, { 8, 10 }, { 8, 9 },
                        { 9, 8 }, { 10, 8 }, { 11, 8 }, { 12, 8 }, { 13, 8 },
                        { 14, 8 }, { 14, 7 }, { 14, 6 },
                        { 13, 6 }, { 12, 6 }, { 11, 6 }, { 10, 6 }, { 9, 6 },
                        { 8, 5 }, { 8, 4 }, { 8, 3 }, { 8, 2 }, { 8, 1 },
                        { 8, 0 }, { 7, 0 }, { 6, 0 }
        };

        public static final int[] LAUNCH = { 0, 13, 26, 39 };
        public static final int[] HOME_ENTRY = { 51, 12, 25, 38 };
        // public static final int[] HOME_ENTRY = { 50, 11, 24, 37 }; // for 0-based
        // indexing

        public static final boolean[] SAFE = {
                        true, false, false, false, false, false, false, false,
                        true, false, false, false, false, true, false, false,
                        false, false, false, false, false, true, false, false,
                        false, false, true, false, false, false, false, false,
                        false, false, true, false, false, false, false, true,
                        false, false, false, false, false, false, false, true,
                        false, false, false, false
        };

        public static final int[][][] HOMECOL = { // player,step,row,col
                        { { 7, 1 }, { 7, 2 }, { 7, 3 }, { 7, 4 }, { 7, 5 } },
                        { { 1, 7 }, { 2, 7 }, { 3, 7 }, { 4, 7 }, { 5, 7 } },
                        { { 7, 13 }, { 7, 12 }, { 7, 11 }, { 7, 10 }, { 7, 9 } },
                        { { 13, 7 }, { 12, 7 }, { 11, 7 }, { 10, 7 }, { 9, 7 } }
        };

        public static final int[][][] YARD = { // player,token,row,col
                        { { 2, 2 }, { 2, 4 }, { 4, 2 }, { 4, 4 } },
                        { { 2, 10 }, { 2, 12 }, { 4, 10 }, { 4, 12 } },
                        { { 10, 10 }, { 10, 12 }, { 12, 10 }, { 12, 12 } },
                        { { 10, 2 }, { 10, 4 }, { 12, 2 }, { 12, 4 } }
        };

        public static final Color[] HOMECOL_COLOR = {
                        new Color(233, 155, 155),
                        new Color(153, 175, 225),
                        new Color(153, 208, 163),
                        new Color(238, 213, 133)
        };

        public static final int[][][] DICE_DOTS = { // number,dot,row,col
                        { { 2, 2 } },
                        { { 1, 1 }, { 3, 3 } },
                        { { 1, 1 }, { 2, 2 }, { 3, 3 } },
                        { { 1, 1 }, { 3, 1 }, { 1, 3 }, { 3, 3 } },
                        { { 1, 1 }, { 3, 1 }, { 2, 2 }, { 1, 3 }, { 3, 3 } },
                        { { 1, 1 }, { 1, 2 }, { 1, 3 }, { 3, 1 }, { 3, 2 }, { 3, 3 } }
        };
}