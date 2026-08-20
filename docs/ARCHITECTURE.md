# CrestBoardGames 架構

## 遊戲呈現原則

對局必須在 Minecraft 世界中的實體牌桌進行。核心負責桌子、座位、`ItemDisplay`／`BlockDisplay`、`Interaction` 點擊區、動畫、發光提示與重啟復原；遊戲模組只維護規則狀態，並透過 `PhysicalTable` API 放置或更新牌與棋子。

GUI 僅負責大廳、建立房間、規則、準備、房主與管理員操作。所有 GUI 點擊一律由核心取消原始物品移動事件，玩家不會取得介面中的按鈕物品。

## 核心原則

1. Purpur 只直接載入 `CrestBoardGames.jar`。
2. 遊戲模組由核心從 `plugins/CrestBoardGames/modules/*.jar` 載入。
3. 模組程式與資料分離：`CrestMahjong.jar` 對應 `CrestMahjong/`。
4. 核心擁有玩家、房間、GUI、儲存、排行榜與網站同步；模組只實作遊戲規則。
5. 模組不支援熱卸載，更新 JAR 後必須完整重新啟動伺服器。

## 核心服務

```text
ModuleManager       掃描、驗證、載入與停用模組
RoomManager         公開、私人、密碼與配對房間
SessionManager      遊戲啟動、快照、結束與恢復
PlayerManager       加入、離開、準備與房主轉移
GuiManager          大廳、房間、設定與管理 GUI
ReconnectManager    斷線保留、臨時機器人與重連
BotManager          背景計算、超時與主執行緒回傳
RankingManager      Rating、戰績、賽季與排行榜
StorageManager      SQLite／MariaDB 與模組狀態
WebsiteSyncManager  已發布設定、快取與確認回報
IntegrationManager  CrestCore、Oraxen、Vault、PlaceholderAPI
```

## 模組生命週期

```text
掃描 JAR
  → 驗證 crest-boardgame-module.yml
  → 檢查 API 版本與重複 ID
  → 建立同名資料夾
  → 建立獨立 ModuleClassLoader
  → onLoad(context)
  → onEnable()
  → 接受房間建立
  → 伺服器關閉時 onDisable()
```

單一模組失敗時只停用該模組，其餘遊戲繼續運作。

## 執行緒規則

- Bukkit/Purpur API 操作只能在伺服器主執行緒。
- 資料庫、網站請求與機器人搜尋在背景執行。
- 背景結果回到主執行緒後，必須再次確認遊戲版本與回合仍相同。
- 每個機器人動作必須有時間限制與安全預設動作。

## 儲存

核心資料庫保存玩家、房間、對局摘要、Rating、賽季、獎勵與重連工作階段。模組的完整局面使用帶版本的快照：

```json
{
  "module": "mahjong",
  "module_version": "1.0.0",
  "schema_version": 1,
  "state": {}
}
```

## 設定優先順序

```text
網站已發布規則
  → 本機模組 config.yml
  → 模組 JAR 預設值
```

API Token、資料庫密碼與伺服器金鑰永遠只保留在核心本機設定，不進入遊戲模組或網站規則內容。
