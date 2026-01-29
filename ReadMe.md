# 📜 DeathBack - Death Location & Recall Plugin

DeathBack is a lightweight and efficient utility plugin for Minecraft servers. When a player dies, the plugin captures their exact coordinates and sends a message featuring a **clickable interactive button**, allowing the player to teleport back to their death location.

## ✨ Features
* **Coordinate Logging**: Automatically captures and displays X, Y, and Z coordinates upon death.
* **One-Click Teleport**: Return to your death site instantly by clicking the message in chat.
* **Single-Use Restriction**: To maintain survival balance, the teleport record is automatically cleared once used.
* **Multi-Language Support**: Fully localized messages stored in a dedicated directory.
* **Hot Reload**: Update settings and translations on-the-fly without restarting the server.

**Minecraft Version 1.21.11**

---

## 🛠 Commands & Permissions

| Command | Description | Permission | Default |
| :--- | :--- | :--- | :--- |
| `/deathback` | Teleport to your last death location. | None | Everyone |
| `/deathback reload` | Reload configuration and language files. | `deathback.admin` | OP |

---

## 📂 Project Structure



```text
DeathBack/
├── config.yml            # Main configuration (Language switch)
├── languages/            # Translations folder
│   ├── messages_en.yml   # English localization
│   └── messages_zh.yml   # Chinese localization
└── plugin.yml            # Plugin metadata and commands

⚙️ Configuration
You can easily switch the plugin language via config.yml:

# Language setting (Matches the suffix in the languages folder)
# Options: en, zh
language: en

🚀 Installation & Setup
Download the DeathBack-1.0-SNAPSHOT.jar.

Drop the file into your server's plugins folder.

Start or restart your server.

The plugin will automatically generate the config.yml and a languages folder containing all translation files.

(Optional) Customize the messages in the languages folder and run /deathback reload to apply changes.

🏗 Development Specs
API: Paper-API 1.21.1-R0.1-SNAPSHOT

Java Version: Java 21

Build System: Maven
