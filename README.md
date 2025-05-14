#  2D Platformer Game — Final Java Project (LibGDX)

Welcome to the final course project: a 2D platformer game created using Java and the LibGDX framework. The goal is to showcase gameplay elements (movement, jumping, enemy collision, coin collection) and apply at least 5 software design patterns.

---

##  Features

-  Player can move left/right and jump using keyboard keys.
-  Collect coins to increase score.
-  Colliding with enemies reduces health.
-  Player starts with 3 lives shown as hearts.
-  Platforms and obstacles create level structure.
-  Winning condition: collect all coins.
-  Game Over screen shown if all lives are lost.

---

##  Controls

| Key       | Action             |
|-----------|--------------------|
| A / ←     | Move left          |
| D / →     | Move right         |
| Space     | Jump               |
| R         | Restart (after win/lose) |

---

##  Design Patterns Used

| Pattern       | Description |
|---------------|-------------|
| Factory   | EntityFactory creates player, enemy, coin, heart, and platform objects. |
| State     | Game states: PlayingState, GameOverState, and WinState. |
| Strategy  | (Planned) Enemy movement logic as interchangeable strategies. |
| Observer  | (Planned/Optional) Notifying systems on coin collected or damage. |
| Singleton | (Optional) Can be used to manage shared data like score or configuration. |

---

## Technologies Used

- LibGDX game framework
- Java 17+
- Textures in PNG format
- Object-Oriented Design
- IDE: IntelliJ IDEA (recommended)

---
