# 🎲 Ludo Game — Java Swing

A classic **Ludo board game** built in Java Swing with AI opponents, smooth token animation, and a clean multi-file package structure.

---

## 📸 Preview
<img width="1085" height="886" alt="image" src="https://github.com/user-attachments/assets/8bb4a778-410b-4e9a-b1b8-0e94c13040e8" />

> 4 players on a fully rendered 15×15 board with colored yards, safe cells, home columns, and animated token movement.

---

## ✨ Features

- 🎮 **2 to 4 players** — mix of human and AI players
- 🤖 **AI opponents** — smart move selection (capture > advance > normal)
- 🎲 **Animated dice roll** — smooth shuffling animation before result
- 🏃 **Token animation** — tokens move step by step across the board
- ⭐ **Safe cells** — marked with gold stars, tokens can't be captured here
- 💥 **Capture mechanic** — land on enemy token to send it back to yard
- 🏆 **Win detection** — first player to bring all 4 tokens home wins
- 🎨 **Clean UI** — dark side panel with player status, dice, and controls
- 📦 **Multi-file package structure** — organized as a proper Java package

---

## 📁 Project Structure

```
ludo-game-java/
└── src/
    └── ludo/
        ├── Constants.java     # All colors, arrays, and game constants
        ├── GameState.java     # Core game logic (moves, turns, AI, animation)
        ├── BoardPanel.java    # Board rendering (yards, track, tokens, arrows)
        ├── SidePanel.java     # Side panel (dice, player rows, buttons)
        └── LudoGame.java      # Main JFrame + entry point
```

---

## 🚀 How to Run

### Requirements
- Java JDK 8 or higher

### Steps

**1. Clone the repo**
```bash
git clone https://github.com/your-username/ludo-game-java.git
cd ludo-game-java
```

**2. Compile**
```bash
javac -d out src/ludo/*.java
```

**3. Run**
```bash
java -cp out ludo.LudoGame
```

---

## 🎮 How to Play

1. Click **NEW GAME** to open the setup dialog
2. Choose number of players (2, 3, or 4)
3. Check which players are **Human** (unchecked = AI)
4. Click **START**
5. Click **ROLL DICE** on your turn
6. Click a **highlighted token** to move it
7. First player to get all 4 tokens **HOME** wins! 🏆

---

## 🧠 Game Rules

| Rule | Detail |
|------|--------|
| Launch | Roll a **6** to bring a token out of yard |
| Safe cells | Marked with ⭐ — tokens cannot be captured here |
| Capture | Land on an enemy token to send it back to yard |
| Roll again | Rolling a **6** gives you another turn |
| Home column | Each player has a colored path leading to home |
| Win | All 4 tokens must reach the center **HOME** |

---

## 🛠️ Built With

- **Java** — core language
- **Java Swing** — GUI framework
- **Java AWT** — 2D graphics and rendering
- No external libraries required

---

## 📄 License

This project is open source and available under the [MIT License](LICENSE).

---

> Made with ☕ and Java

