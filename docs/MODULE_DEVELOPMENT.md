# 遊戲模組開發規格

## 檔案對應

```text
modules/CrestMahjong.jar
modules/CrestMahjong/config.yml
modules/CrestMahjong/messages.yml
modules/CrestMahjong/data/
```

模組顯示名稱 `CrestMahjong` 也作為資料夾名稱。名稱只能使用英文字母、數字、底線與連字號。

## 模組描述檔

模組 JAR 根目錄必須包含 `crest-boardgame-module.yml`：

```yaml
id: mahjong
name: CrestMahjong
display-name: 日本麻將
version: 1.0.0
api-version: 1
main: tw.crestnetwork.boardgames.mahjong.CrestMahjongModule
```

## 依賴規則

- 模組以 `compileOnly` 依賴 `boardgames-api`。
- 模組不能封裝另一份 API 類別。
- 第三方函式庫必須 relocate，避免模組彼此衝突。
- 模組不能直接存取核心內部 package。
- 模組只能透過 `ModuleContext` 取得資料夾、記錄器與共用服務。

## GUI 安全

模組提供的 GUI 必須使用專用 `InventoryHolder` 或核心 GUI API，不得只靠標題判斷。所有按鈕以 Persistent Data Container 儲存動作 ID，且預設取消所有點擊與拖曳。

## 實體桌遊畫面

- `GameCreationContext.table()` 會提供該房間專用的 `PhysicalTable`。
- 模組以 `PhysicalPiece` 描述牌、麻將牌、棋子或籌碼，不自行保存 Bukkit 實體參照。
- `modelKey` 由核心解析成原版物品或 Oraxen 模型；沒有 Oraxen 時必須能使用原版後備外觀。
- `TablePosition` 是相對於牌桌原點的位置，讓管理員移動或旋轉整張桌子時不必重算每個物件。
- 任何私人資訊（例如玩家手牌正面）不得放入所有玩家都能讀取的 `publicState`；核心日後會提供按觀看者送出不同模型的私有視圖。
- 模組只處理合法動作與規則，Display／Interaction 實體生成、清理、動畫及重啟復原由核心負責。

## 相容性

`api-version` 是 CrestBoardGames 模組 API 版本，不是 Minecraft 版本。API 主版本不同時拒絕載入；次版本新增功能時應保留向後相容。
