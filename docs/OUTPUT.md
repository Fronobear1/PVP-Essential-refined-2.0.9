# PvP Essentials: Refined - Developer Documentation

## 📘 Mod Overview
**PvP Essentials: Refined** is a client-side Fabric mod designed to provide essential combat information (Armor Status, Combat Items, Target HP) in a clean, vanilla-friendly HUD. It emphasizes performance ("Render-only logic") and safety (no prediction, no automation).

## 📦 Component Summary

### 1. Main Entrypoints
- **`PvPEssentialsRefined`**: The main mod initializer.
  - Registers the configuration using `AutoConfig`.
  - Location: `src/main/java/com/fronobear/pvpessentials/refined/PvPEssentialsRefined.java`
- **`PvPEssentialsRefinedClient`**: The client-side initializer.
  - Registers the HUD rendering callback (`HudRenderCallback`).
  - Location: `src/main/java/com/fronobear/pvpessentials/refined/client/PvPEssentialsRefinedClient.java`
- **`ModMenuIntegration`**: Provides the configuration screen for Mod Menu.
  - Location: `src/main/java/com/fronobear/pvpessentials/refined/compat/ModMenuIntegration.java`

### 2. Configuration
- **`ModConfig`**: Defines all configurable options using Cloth Config (AutoConfig).
  - Categories: General, Armor HUD, Combat HUD, Target HP, Crosshair.
  - Location: `src/main/java/com/fronobear/pvpessentials/refined/config/ModConfig.java`

### 3. HUD Modules
All HUDs are rendered via the `HudRenderCallback` event in the client initializer.

- **`ArmorHud`**
  - Displays armor durability and status in the bottom-right.
  - Handles color coding and warning sounds.
  - Location: `src/main/java/com/fronobear/pvpessentials/refined/hud/ArmorHud.java`

- **`CombatItemsHud`**
  - Displays counts for Ender Pearls, Arrows, and Golden Apples in the mid-left.
  - Location: `src/main/java/com/fronobear/pvpessentials/refined/hud/CombatItemsHud.java`

- **`TargetHpHud`**
  - Displays the name and HP of the entity under the crosshair.
  - Respects distance and visibility checks.
  - Location: `src/main/java/com/fronobear/pvpessentials/refined/hud/TargetHpHud.java`

### 4. Mixins
- **`CrosshairMixin`**
  - Mixes into `InGameHud#renderCrosshair` to change the crosshair color when highlighting a target.
  - Uses `RenderSystem.setShaderColor` for the visual effect.
  - Location: `src/main/java/com/fronobear/pvpessentials/refined/mixin/CrosshairMixin.java`

## 🛠️ Registration & Structure
- **Events**: HUD rendering is registered to `HudRenderCallback.EVENT`.
- **Config**: Registered via `AutoConfig.register` in `onInitialize`.
- **Resources**:
  - `fabric.mod.json`: Metadata and entrypoints.
  - `pvp-essentials-refined.mixins.json`: Mixin configuration.

## 🚀 How it Works
1. **Initialization**: The mod loads, registers the config, and sets up the client render loop.
2. **Rendering**: Every frame, the HUD callback fires.
   - `ArmorHud` checks player inventory and draws armor.
   - `CombatItemsHud` counts specific items and draws them.
   - `TargetHpHud` raycasts (via `client.crosshairTarget`) to find the focused entity and draws its stats.
3. **Crosshair**: The mixin intercepts the crosshair render call to apply a color tint if a valid target is focused.

## ✅ Build Instructions
1. Ensure JDK 21 is installed.
2. Run `./gradlew build` to compile.
3. The output jar will be in `build/libs/`.
