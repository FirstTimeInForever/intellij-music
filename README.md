# intellij-music (Fancy Music Plugin)
![](https://img.shields.io/badge/DevHack-2019-green)
![](https://img.shields.io/github/stars/FirstTimeInForever/intellij-music?style=flat)
![](https://img.shields.io/github/issues/FirstTimeInForever/intellij-music?style=flat)
![](https://img.shields.io/github/license/FirstTimeInForever/intellij-music?style=flat)
![](https://img.shields.io/github/forks/FirstTimeInForever/intellij-music)

[![CircleCI](https://circleci.com/gh/FirstTimeInForever/intellij-music.svg?style=svg)](https://circleci.com/gh/FirstTimeInForever/intellij-music)
![](https://img.shields.io/github/v/tag/firsttimeinforever/intellij-music?include_prereleases)
![GitHub contributors](https://img.shields.io/github/contributors/firsttimeinforever/intellij-music)
![](https://img.shields.io/github/last-commit/firsttimeinforever/intellij-music)


Play fancy music based on your keyboard activity.

## Description
This plugin plays random notes from random major scales based on your typing speed. Everytime you press the key, it plays random piano note based on some music theory.

Also, this plugin can play your custom midi files! Just put into your midi directory!


## Build instructions
Create directory for soundfont and midi files:
```
mkdir -p ~/my-midis
```
Download soundfont file:
```
curl 'https://raw.githubusercontent.com/urish/cinto/master/media/FluidR3%20GM.sf2' > ~/my-midis/soundfont.sf2
```
Download and install plugin:
```
git clone git@github.com:FirstTimeInForever/intellij-music.git
cd intellij-music/plugin
./gradlew :runIde
```


## Инструкция по сборке и запуске проекта
```
mkdir -p ~/my-midis
curl 'https://raw.githubusercontent.com/urish/cinto/master/media/FluidR3%20GM.sf2' > ~/my-midis/soundfont.sf2

git clone git@github.com:FirstTimeInForever/intellij-music.git
cd intellij-music/plugin
./gradlew :runIde
```
