# CrestBoardGames

CrestBoardGames 是為 Purpur 26.2 設計的模組化 Minecraft 實體桌遊平台。伺服器只安裝一個核心插件，各遊戲則以獨立模組 JAR 放入核心資料夾。

遊戲本體會呈現在 Minecraft 世界中：桌子、牌、麻將牌、棋盤與棋子都是可看見及互動的場景物件。箱子 GUI 只用於建立牌桌、選擇規則、準備與房主管理，不會取代實際遊戲畫面，也不會讓玩家拿走 GUI 按鈕物品。

## 目標

- 共用桌遊大廳與房間 GUI
- 世界內實體牌桌、座位、牌、棋子與互動熱點
- 使用 Display／Interaction 實體呈現，並支援 Oraxen 自訂模型
- 伺服器重啟後復原牌桌與未結束對局
- 房主、玩家、準備、觀戰與重連
- 安全的箱子 GUI，玩家不能拿走按鈕物品
- 機器人、戰績、積分、賽季與排行榜
- 網站設定同步與 Oraxen 選用整合
- 單一模組故障不影響其他桌遊

## 執行時目錄

```text
plugins/CrestBoardGames/
├─ config.yml
├─ messages.yml
├─ modules/
│  ├─ CrestMahjong.jar
│  ├─ CrestMahjong/
│  │  ├─ config.yml
│  │  └─ data/
│  ├─ CrestGomoku.jar
│  └─ CrestGomoku/
└─ data/
```

核心只掃描 `modules/` 第一層的 JAR。`CrestMahjong.jar` 的設定與資料固定存放在同層的 `CrestMahjong/`。

## 專案模組

```text
boardgames-api               公開且穩定的遊戲模組 API
boardgames-core              Purpur 插件、模組載入與共用服務
modules/crest-mahjong        台灣十六張麻將
modules/crest-color-cards    彩色卡牌
modules/crest-gomoku         五子棋
modules/crest-chinese-chess  中國象棋
modules/crest-chess          西洋棋
modules/crest-poker          撲克牌
```

目前處於架構與 API 骨架階段，已包含模組隔離載入器及實體桌面 API；遊戲規則、實體渲染器與 GUI 尚待實作。第一個正式遊戲模組將是 CrestMahjong，第二個是 CrestGomoku。

## 建置

需求：JDK 25。

```bash
./gradlew clean build
```

產物會位於各子專案的 `build/libs/`。

## 文件

- [完整架構](docs/ARCHITECTURE.md)
- [模組開發規格](docs/MODULE_DEVELOPMENT.md)
- [參考專案與授權原則](docs/REFERENCES.md)
- [台灣十六張麻將進度](docs/TAIWAN_MAHJONG.md)
- [貢獻指南](CONTRIBUTING.md)
- [安全政策](SECURITY.md)

## 授權

本專案目前尚未選定授權條款。公開可見不代表已授權複製、散布或商業使用；正式開放貢獻前會補上明確 LICENSE。
