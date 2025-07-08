# FTC Virtual Robot Simulator
A Java-based, Swing-powered virtual robot simulator for FIRST Tech Challenge (FTC) teams.
Designed for driver practice and strategy training, the simulator offers:
- Field rendering
- Alliance-specific views
- Customizable robot visuals

In Development:
- Manual and gamepad controls with realistic acceleration and collision detection.

##  Features
- **Field Rendering**: High-fidelity FTC field backgrounds (dark/light) with the correct aspect ratio and an optional 180° flip for the Blue Alliance.
- **Robot Visualization**: Customizable robot body and wheel colors per alliance, semi-transparent shading, direction marker, and crisp double-precision rendering.
- **Settings Dialog**: Pre-launch configuration for drive type (Mecanum/Tank), field theme, and alliance color.
- **Simulation Loop**: Smooth 60 Hz update/render cycle via Swing Timer.
- **Extendable Architecture**: Clean separation of `core` (physics/model), `ui` (rendering/dialogs), and `input` (controls), with reverse-domain packaging (`com.chrislemoine.simulator`).

## Prerequisites
- **Java 17** (JDK)
- **IntelliJ IDEA** (or another Java IDE)
- **JInput** library for gamepad support (optional)  
  Place `jinput-*.jar` and `jinput-platform-*.jar` in a `libs/` folder

## Project Structure
    src/main/java/com/chrislemoine/simulator/
    ├── core/
    │   ├── SimBot.java
    │   ├── SimBotBuilder.java
    │   ├── Simulator.java
    │   └── DriveType.java
    ├── ui/
    │   ├── FieldPanel.java
    │   ├── SettingsDialog.java
    │   ├── Background.java
    │   └── Alliance.java
    ├── input/           (planned)
    │   └── GamepadController.java
    └── Main.java        (entry point)
    src/main/resources/
    └── background/season-2024-intothedeep/*.png

## Getting Started
1. **Clone the repository**  
   ```bash
   git clone https://github.com/yourorg/ftc-virtual-simulator.git
   cd ftc-virtual-simulator
2. **Add dependencies**
   - Copy `jinput-*.jar` and `jinput-platform-*.jar` into `libs/`.
   - In IntelliJ: **File** → **Project Structure** → **Modules** → **Dependencies** → **+** → **JARs or directories**… and select them.
3. **Build and run**
   - In IntelliJ, run Main.main().
   - Or via command line:
     ```bash
     javac -d out $(find src -name "*.java")
     java -cp out:libs/jinput-2.0.7.jar:libs/jinput-platform-2.0.7.jar com.chrislemoine.simulator.Main
4. **Usage**
   - Configure drive type, field background, and alliance in the settings dialog.
   - Use keyboard (W/A/S/D + Q/E) or gamepad (when implemented) to drive the robot.

## Next Steps
- **Manual & Gamepad Controls**: Implement KeyboardController and complete GamepadController integration.
- **Collision Detection**: Prevent the robot from exiting field boundaries or colliding with obstacles.
- **Simulation Speed Control**: Add a GUI slider to adjust time dilation.
- **Autonomous Sequencing**: Build an action/command DSL for scripted routines.

## Contributing
Contributions are welcome! Please fork the repo, create a feature branch, and submit a pull request. For major changes, open an issue to discuss your ideas first.
