# 🎮 Viewmodel+ Swing Animation Mod

**Fabric | Minecraft 1.21.4 | Client-side only**

Полностью настраиваемые позиции рук и анимации свинга для вашего Minecraft.

---

## 📦 Установка

1. Установите **Fabric Loader 0.16.10+** для Minecraft 1.21.4
2. Скачайте **Fabric API** и положите в папку `mods/`
3. Скомпилируйте мод командой `./gradlew build`
4. Возьмите `viewmodelmod-1.0.0.jar` из `build/libs/`
5. Положите `.jar` в папку `mods/`
6. Запустите игру!

---

## 🎛 Использование

### Открыть конфиг
Нажмите клавишу **`V`** (можно изменить в настройках управления).

### Слайдеры в GUI
| Слайдер | Описание |
|---------|----------|
| R/L Hand X / Y / Z | Смещение правой/левой руки |
| R/L Rot X / Y / Z | Поворот руки по каждой оси (градусы) |
| R/L Scale | Масштаб руки (1.0 = стандарт) |
| Swing Speed | Скорость анимации удара (1.0 = ванилла) |
| Swing Intensity | Интенсивность дуги удара |
| Swing Rot X/Y/Z | Влияние каждой оси при ударе |
| Bob Intensity | Покачивание камеры при ходьбе |
| Bob Speed | Скорость покачивания |

### Пресеты
| Пресет | Описание |
|--------|----------|
| **Vanilla** | Стандартные настройки Minecraft |
| **FPS** | Высоко расположенные руки, быстрый свинг, минимальный боб — как в шутерах |
| **RPG** | Ниже опущенные руки, медленный тяжёлый удар — как в RPG |

---

## ⚙️ Конфиг файл

`%appdata%/.minecraft/config/viewmodelmod.json`

```json
{
  "rightHandX": 0.56,
  "rightHandY": -0.52,
  "rightHandZ": -0.72,
  "rightHandRotX": 0.0,
  "rightHandRotY": 0.0,
  "rightHandRotZ": 0.0,
  "rightHandScale": 1.0,
  "leftHandX": -0.56,
  "leftHandY": -0.52,
  "leftHandZ": -0.72,
  ...
  "swingSpeed": 1.0,
  "swingIntensity": 1.0,
  "swingRotX": 1.0,
  "swingRotY": 0.4,
  "swingRotZ": 0.4,
  "enableBob": true,
  "bobIntensity": 1.0,
  "bobSpeedMultiplier": 1.0
}
```

---

## 🛠 Сборка из исходников

**Требования:** JDK 21+, Gradle 8+

```bash
git clone https://github.com/yourname/viewmodelmod
cd viewmodelmod
./gradlew build
```

Готовый `.jar` будет в `build/libs/viewmodelmod-1.0.0.jar`.

---

## 🗂 Структура проекта

```
src/main/java/com/viewmodelmod/
├── ViewmodelModClient.java          ← точка входа
├── animation/
│   └── SwingAnimationHelper.java    ← вычисление eased-анимации
├── config/
│   └── ViewmodelConfig.java         ← хранение и загрузка настроек
├── gui/
│   └── ViewmodelConfigScreen.java   ← GUI экран конфига
└── mixin/
    ├── HeldItemRendererMixin.java   ← позиция рук + свинг
    ├── GameRendererMixin.java       ← управление бобом
    └── ClientPlayerInteractionManagerMixin.java
```

---

## 📝 Лицензия

MIT — делайте что хотите.
