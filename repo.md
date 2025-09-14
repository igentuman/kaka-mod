# Kaka Mod - Minecraft Forge 1.20.1

A humorous Minecraft mod that adds realistic "pooping" mechanics to the game, bringing a new level of biological realism to your Minecraft experience.

## 📋 Project Information

- **Mod ID**: `kaka`
- **Version**: 1.4.4
- **Minecraft Version**: 1.20.1
- **Forge Version**: 47.4.0
- **Java Version**: 17
- **License**: MIT
- **Author**: igentuman
- **CurseForge**: [https://www.curseforge.com/minecraft/mc-mods/kaka](https://www.curseforge.com/minecraft/mc-mods/kaka)

## 🎮 Features

### 🐄 Animal Pooping System
The mod adds realistic pooping mechanics to various animals:
- **Cows** - Drop cow kaka
- **Sheep** - Drop sheep kaka  
- **Horses** - Drop horse kaka
- **Pigs** - Drop pig kaka
- **Villagers** - Drop villager kaka
- **Players** - Can produce player kaka

### 🧱 Blocks & Items
- **Kaka Block** - Placeable block made from kaka (slows movement, honey block sound)
- **Kaka Demon Head Block** - Decorative boss head block
- **Various Kaka Items** - Different types of kaka from different entities

### 👹 Boss Entity
- **Kaka Demon** - A powerful boss entity with custom model, sounds, and attacks
- Custom AI goals and behaviors
- Unique sound effects (spawn, attack, hit, death)
- Throwable kaka block projectiles

### 🧪 Effects & Potions
- **Kaka Effect** - Harmful potion effect
- **Kaka Potion** - Brewable potion with kaka effect (60 minutes duration)

### 🔊 Sound System
- Custom fart sounds
- Kaka Demon sound effects (spawn, attack, hit, death)
- Immersive audio experience

## 🛠️ Technical Details

### Project Structure
```
src/main/java/com/igentuman/kaka/
├── block/           # Custom blocks (KakaBlock, KakaDemonHeadBlock)
├── config/          # Configuration files (Client & Common)
├── datagen/         # Data generation (recipes, models, lang, etc.)
├── effect/          # Potion effects
├── entity/          # Custom entities and AI
│   ├── boss/        # Boss entities (KakaDemon)
│   ├── goal/        # AI goals
│   ├── model/       # Entity models
│   ├── render/      # Entity renderers
│   └── sound/       # Sound utilities
├── item/            # Custom items
└── setup/           # Mod initialization and registration
```

### Key Components
- **Registration System**: Centralized registration of all mod content
- **Event System**: Handles entity spawning, player interactions, and game events
- **Configuration**: Client and server-side configuration options
- **Data Generation**: Automated generation of recipes, models, and language files
- **Custom AI**: Specialized AI goals for animal pooping behavior

### Dependencies
- **Minecraft Forge**: 47.4.0+ (for MC 1.20.1)
- **Parchment Mappings**: 2023.09.03-1.20.1
- **Java**: 17+

## 🎨 Assets
The mod includes custom textures for:
- All kaka items and blocks
- Kaka Demon entity and head block
- Mob effect icons
- Custom sound files

## ⚙️ Configuration
The mod supports both client and server-side configuration:
- Enable/disable pooping for specific animals
- Customize pooping frequency and behavior
- Client-side visual and audio settings

## 🔧 Development Setup

### Prerequisites
- Java 17 JDK
- IntelliJ IDEA (recommended)
- Git

### Building
```bash
./gradlew build
```

### Running in Development
```bash
./gradlew runClient  # Client
./gradlew runServer  # Server
./gradlew runData    # Data generation
```

## 📦 Distribution
- Built JARs are compatible with Minecraft 1.20.1
- Available on CurseForge
- MIT License allows for modification and redistribution

## 🤝 Credits
- **Author**: igentuman
- **Special Thanks**: Matik for kaka inspiration
- Built with Minecraft Forge framework

## 📝 Notes
This mod is designed for entertainment purposes and adds a humorous biological element to Minecraft gameplay. All content is family-friendly despite the theme.

---
*Generated from repository analysis*