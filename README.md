<p align="center">
  <img src="docs/images/dungeon-gameplay.png" alt="Roguelike Dungeon gameplay" width="100%">
</p>

# 🗡️ Roguelike Dungeon

A 2D roguelike dungeon crawler built with Java and LibGDX. This project is a long-term personal development project focused on game programming, object-oriented design, and building a complete, playable game step by step.

Explore procedurally generated dungeons, fight enemies, collect items, manage your inventory, and unlock exits to progress through new levels.

## 🛠️ Tech Stack

<p align="left">
  <img src="https://img.shields.io/badge/Java-21-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white" alt="Java 21" />
  <img src="https://img.shields.io/badge/LibGDX-2D_Game_Development-EA4335?style=for-the-badge&logo=libgdx&logoColor=white" alt="LibGDX" />
  <img src="https://img.shields.io/badge/Gradle-Build_Tool-02303A?style=for-the-badge&logo=gradle&logoColor=white" alt="Gradle" />
  <img src="https://img.shields.io/badge/IntelliJ_IDEA-IDE-000000?style=for-the-badge&logo=intellijidea&logoColor=white" alt="IntelliJ IDEA" />
  <img src="https://img.shields.io/badge/Git-Version_Control-F05032?style=for-the-badge&logo=git&logoColor=white" alt="Git" />
  <img src="https://img.shields.io/badge/GitHub-Repository-181717?style=for-the-badge&logo=github&logoColor=white" alt="GitHub" />
</p>

## ✨ Features

- **Procedural dungeon generation** – Explore newly generated dungeon layouts as you progress.
- **Dungeon exits and level transitions** – Find locked exits, use keys, and move between levels with smooth fade-out and fade-in animations.
- **Combat system** – Fight enemies using the current attack mechanics.
- **Inventory management** – Collect, drop, and manage items.
- **World items** – Discover keys and other items throughout the dungeon.
- **Enemy spawning** – Enemies are generated as part of each dungeon level.
- **Collision detection** – Movement is restricted by walls and other non-walkable tiles.
- **In-game notifications** – Receive feedback when interacting with dungeon exits.

## 🎮 Controls

| Key | Action |
|---|---|
| `W` `A` `S` `D` | Move |
| `Arrow Keys` | Move |
| `SPACE` | Attack |
| `E` | Interact with doors or pick up items |
| `I` | Open or close inventory |

## 🚀 Getting Started

**Required JDK 21**

Run the following commands from the repository root.

**Windows**
```powershell
cd game
.\gradlew.bat lwjgl3:run
```

**Linux / macOS**
```bash
cd game
./gradlew lwjgl3:run
```

## 📁 Project Structure & Architecture

The project is organized into game logic and a desktop launcher.

```text
roguelike-dungeon/
├── README.md
└── game/
    ├── core/
    │   └── src/main/java/com/marton/roguelike/
    │       ├── entity/       # Player and enemy entities
    │       ├── input/        # Player input and controls
    │       ├── item/         # Items and inventory
    │       ├── render/       # Rendering and UI
    │       ├── world/        # Map and dungeon generation
    │       └── Main.java     # Main game controller
    ├── lwjgl3/               # Desktop launcher
    ├── assets/               # Game assets
    └── gradle/               # Gradle Wrapper files
```

## 🧭 Development Status

The project is actively being developed. The current version focuses on establishing a functional dungeon gameplay loop and a maintainable foundation for future features.

Implemented and tested:

- Procedurally generated dungeon levels
- Player movement and collision detection
- Enemy spawning and combat
- Item pickup and dropping
- Inventory management
- Key-based dungeon exits
- Notifications and animated level transitions
- Preservation of player health and inventory between dungeon levels

Some gameplay behaviors, including the current game-over behavior, are intentionally basic and may change during future development.

## 🎯 Project Goals

The long-term goal is to develop a complete roguelike game while continuously improving the underlying codebase.

Planned areas of development include:

- Further dungeon generation improvements
- Expanded enemy and item systems
- Improved game progression and balancing
- Additional gameplay mechanics
- Automated testing and code quality improvements
- Improved documentation and presentation
