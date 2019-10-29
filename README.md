# intellij-music

## Инструкция по сборке и запуске проекта
```
mkdir -p ~/my-midis
curl 'https://raw.githubusercontent.com/urish/cinto/master/media/FluidR3%20GM.sf2' > ~/my-midis/soundfont.sf2

git clone git@github.com:FirstTimeInForever/intellij-music.git
cd intellij-music/plugin
./gradlew :runIde
```
