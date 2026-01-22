

## 🔧 Mod Info

- **Mod Name:** `PvP Essentials: Refined`
- **Mod ID:** ``
- **Author:** `FronoBear`
- **Package Name:** `com.fronobear.pvpessentials.refined`
- **Minecraft Version:** `1.21.11`
- **Fabric Loader Version:** `>=0.15.11`
- **Yarn Mappings:** `1.21.11+build.3`
- **Fabric API Version:** `0.140.2+1.21.11``
- **Java Version (JDK):** `21`
- **Mod Type:** `client`, 

---

## 🧠 Mod Functionality

> 🛡️ PvP Essentials – Refined (Full Mod Functionality)
📌 Mod Type

Client-side only

Fabric

Minecraft 1.21.11

Server-safe / QoL HUD mod

Uses Mod Menu + Cloth Config

🎯 Core Philosophy

Provide clear, vanilla-equivalent combat information in a fixed, clean HUD, without adding any gameplay advantage, automation, prediction, or hidden data.

All data shown:

Is already visible to the player in vanilla

Uses client-side raycasting

Uses actual entity state, never predicted values

🖥️ HUD LAYOUT (FIXED & RESPONSIVE)
🛡️ Armor HUD

Position: Bottom-right corner (anchored)

Always visible

Automatically scales with:

Screen resolution

GUI scale

Never overlaps with hotbar, chat, or boss bars

Armor Display Modes (Configurable)

User can choose:

Bar only

Number only

Percentage only

Bar + Number

Bar + Percentage

Durability Behavior

Durability calculated per armor piece

Display updates only when durability actually changes

Color Rules

≥ 30% → Normal color

< 30% → Numbers turn red

≤ 10% → Red + crack sound

Thresholds:

Fully configurable in settings

Sound Warning

Crack sound plays once per threshold crossing

Sound volume configurable

Can be disabled entirely

🍎 Combat Items HUD

Position: Mid-left (anchored to screen center-left)

Shows only:

Ender Pearls

Arrows

Golden Apples

Item count only (no cooldowns, no prediction)

Behavior:

Scales correctly on:

Fullscreen

Windowed

Small screens

Never overlaps inventory or chat

❤️ TARGET HP DISPLAY (FOCUSED & SAFE)
When HP Is Shown

Target HP is shown ONLY IF:

Entity is directly under the crosshair

Entity is visible (no walls)

Entity is within configurable distance

Uses client.crosshairTarget (vanilla raycast)

No tracking, no caching, no ESP.

HP Display Format

Shown beside the entity’s name, example:

DakshIslegend  HP 20


Displays HP value, not hearts

Uses vanilla health math:

1 heart = 2 HP

Full health = 20 HP

Damage Example

HP 20 → hit for 1 heart → HP 18

Updates only after vanilla applies damage

Healing

Golden Apple / Enchanted Apple:

HP increase shown instantly

Absorption hearts:

Optional toggle

Can be shown separately or included

HP Color Logic
HP Range	Color
20 → 13	Green
12 → 8	Yellow
7 → 0	Red

All thresholds configurable in Mod Menu.

Supported Targets

Players (toggleable)

Mobs (toggleable)

🎯 CROSSHAIR FEEDBACK (VISUAL ONLY)

Crosshair turns red when:

Player is aiming perfectly on the hitbox

Uses vanilla raycast detection

No aim assist

No hitbox expansion

No server logic involved

Feature can be disabled in settings.

⚙️ SETTINGS (MOD MENU)

All settings are available via Mod Menu.

General

Enable / Disable mod

HUD scale

HUD opacity

Armor HUD Settings

Show armor HUD

Display mode selection

Low durability threshold (%)

Critical durability threshold (%)

Enable crack sound

Crack sound volume

Combat Items HUD

Show ender pearls

Show arrows

Show golden apples

Target HP Settings

Enable target HP display

Show player HP

Show mob HP

Include absorption hearts

Max display distance

HP color thresholds

Crosshair Settings

Enable crosshair color change

Crosshair highlight color

🔒 PERFORMANCE & SAFETY GUARANTEES

No entity scanning loops

No packet interception

No combat prediction

No automation

Render-only logic

Zero gameplay modification

No server communication

---

## 📦 Output Instructions

The AI must:

- ✅ List all **Java source files**  
  - Show full relative paths (e.g., `src/main/java/com/...`)  
  - Give a one-line description of each file's purpose

- ✅ Write **full, working Java code** for every file  
  - No placeholders, stubs, or missing logic  
  - Fully import-ready and compilable

- ✅ Provide a valid, working `fabric.mod.json`

- ✅ Register all components correctly:
  - Items, blocks, screens, overlays, particles, keybinds, etc.  
  - Client or server init logic as appropriate

- ✅ Include all required **import statements** and **annotations**

---

## 🎨 Assets (If Applicable)

If assets are needed, AI must:

- 📂 **List all required assets**, including:
  - Textures (`.png`)
  - Lang files (`lang/en_us.json`)
  - Models and blockstates (`.json`)
  - Sounds or GUI elements

- 📄 **Provide full content** and **exact paths** for each file:
  - Example: `src/main/.../exampleMixin.java`
  - Example: `src/main/resources/assets/[modid]/textures/item/example_item.png`
  - Example: `src/main/resources/assets/[modid]/lang/en_us.json`

---

## 📘 Documentation Output

Also generate a brief documentation file and save it as:

> 📁 `docs/OUTPUT.md`

This file must:

- ✅ Explain what the mod does  
- ✅ Summarize all the components (classes, assets, events)  
- ✅ Describe where everything is registered and how it works  
- ✅ Be written for the user to understand how the mod is structured  
- ✅ Use Markdown formatting (headings, bullets, code blocks)

---

## ⚙️ Optional Features

If applicable, include:

- Mixin setup  
- Config file support (Cloth Config or JSON)  
- Command registration  
- Client/server networking  
- Runtime environment checks  
- Shader or render layer support

---

## ❌ Rules

- ❌ Do not skip any code  
- ❌ Do not use vague notes like “implement this later”  
- ❌ Do not generate placeholder files or comments  
- ✅ Output must be **complete and build-ready** from the start