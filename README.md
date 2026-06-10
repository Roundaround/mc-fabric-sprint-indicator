![Sprint Indicator](https://imgur.com/11VunzQ.png)

![](https://img.shields.io/badge/Loaders-Fabric%20|%20NeoForge%20|%20Forge-313e51?style=for-the-badge)
![](https://img.shields.io/badge/MC-26.1--26.1.2%20|%201.21%20|%201.20%20|%201.19-313e51?style=for-the-badge)
![](https://img.shields.io/badge/Side-Client-313e51?style=for-the-badge)

[![Modrinth Downloads](https://img.shields.io/modrinth/dt/sprint-indicator?style=flat&logo=modrinth&color=00AF5C)](https://modrinth.com/mod/sprint-indicator)
[![GitHub Repo stars](https://img.shields.io/github/stars/Roundaround/mc-fabric-sprint-indicator?style=flat&logo=github)](https://github.com/Roundaround/mc-fabric-sprint-indicator)

[![Support me on Ko-fi](https://cdn.jsdelivr.net/npm/@intergrav/devins-badges@3/assets/compact/donate/kofi-singular-alt_vector.svg)](https://ko-fi.com/roundaround)

---

Simple UI element showing whether you're currently sprinting - and now whether you're crouching too. Sprinting uses the
built-in beacon speed-boost icon; crouching uses the built-in slowness icon.

If you play with the left-handed or hotbar attack indicator options, the mod adjusts the UI accordingly so that
everything fits together!

![](https://i.imgur.com/cgZvOzq.png)

## Configuration

Open the settings from ModMenu (Fabric) or the **Config** button on the mods list (NeoForge / Forge). The configuration
file is also directly editable from the `sprintindicator.toml` file in your config folder.

`sprintEnabled`: `true|false` - Whether to show an icon when sprinting.

`crouchEnabled`: `true|false` - Whether to show an icon when crouching.

`offset`: `true|false` - An x,y pixel offset for rendering the icons.

`adjustAttackIndicator`: `true|false` - Whether to shift the attack indicator to put the sprint/crouch icons closer to
the hotbar.

Made for the Minecraft streamer [Linkzzey](https://twitch.tv/linkzzey) over on Twitch!
