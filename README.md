# CrestBoardGames

CrestBoardGames 是為 Purpur 26.2 設計的模組化 Minecraft 實體桌遊核心。每一款遊戲都是獨立 GitHub 專案及獨立 JAR，由本核心統一載入與管理。

遊戲本體會呈現在 Minecraft 世界中：桌子、牌、麻將牌、棋盤與棋子都是可看見及互動的場景物件。箱子 GUI 只用於建立牌桌、選擇規則、準備與房主管理，不會取代實際遊戲畫面，也不會讓玩家拿走 GUI 按鈕物品。

## 目標

- 共用桌遊大廳與房間 GUI
- 世界內實體牌桌、座位、牌、棋子與互動熱點
- 使用 Display／Interaction 實體呈現，並以 Oraxen 自訂物品與模型呈現遊戲元件
- 伺服器重啟後復原牌桌與未結束對局
- 房主、玩家、準備、觀戰與重連
- 安全的箱子 GUI，玩家不能拿走按鈕物品
- 機器人、戰績、積分、賽季與排行榜
- 網站設定同步與 Oraxen 必要整合
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

## 本專案內容

```text
boardgames-api               公開且穩定的遊戲模組 API
boardgames-core              Purpur 插件、模組載入與共用服務
```

遊戲規則不存放在這個儲存庫。第一個獨立遊戲專案是 [CrestMahjong](https://github.com/joe1220555/CrestMahjong)，其他遊戲會在開始開發時建立各自的公開儲存庫。

## 遊戲專案

| 遊戲 | 儲存庫 | 狀態 |
|---|---|---|
| 台灣十六張麻將 | [CrestMahjong](https://github.com/joe1220555/CrestMahjong) | 開發中 |
| 台味派對卡牌 | CrestPartyCards | 規劃中 |
| 五子棋 | CrestGomoku | 規劃中 |
| 中國象棋 | CrestChineseChess | 規劃中 |
| 西洋棋 | CrestChess | 規劃中 |
| 撲克牌 | CrestPoker | 規劃中 |

## 安裝與指令

Oraxen 是必要依賴。伺服器需要同時安裝 `Oraxen` 、`CrestBoardGames.jar` 及至少一個遊戲模組；若 Oraxen 缺少，Purpur 不會啟用 CrestBoardGames。

```text
/bg create mahjong   建立實體麻將桌
/bg join [房號]      加入牌桌
/bg start            由房主開始對局
/bg leave            離開牌桌
/bg rooms            查看現有牌桌
/bg modules          管理員查看已載入模組
```

## 建置

需求：JDK 25。

```bash
./gradlew clean build
```

核心產物位於 `boardgames-core/build/libs/CrestBoardGames.jar`。各遊戲必須前往自己的儲存庫建置。

## 文件

- [完整架構](docs/ARCHITECTURE.md)
- [模組開發規格](docs/MODULE_DEVELOPMENT.md)
- [參考專案與授權原則](docs/REFERENCES.md)
- [貢獻指南](CONTRIBUTING.md)
- [安全政策](SECURITY.md)

## 授權

本專案目前尚未選定授權條款。公開可見不代表已授權複製、散布或商業使用；正式開放貢獻前會補上明確 LICENSE。
