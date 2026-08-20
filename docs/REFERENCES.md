# 參考專案

CrestBoardGames 會研究下列公開專案的架構與使用經驗；目前沒有複製其原始碼。未來若採用任何程式碼，必須先確認授權相容性，並在本頁與原始檔保留必要聲明。

## 世界內棋盤與規則

- [TeksuSiK/Minechess](https://github.com/TeksuSiK/Minechess) — Apache-2.0；參考世界內棋盤、規則驗證與對局保存的責任切分。
- [krishs505/MCChess](https://github.com/krishs505/MCChess) — MIT；參考玩家在世界內與棋盤互動的流程。

## Display 與 Interaction 實體

- [FancyMcPlugins/FancyHolograms](https://github.com/FancyMcPlugins/FancyHolograms) — MIT；參考 Display 實體生命週期、更新與清理方式。
- [PZDonny/DisplayEntityUtils](https://github.com/PZDonny/DisplayEntityUtils) — GPL-3.0；僅研究公開介面與使用情境，不複製 GPL 程式碼，除非本專案日後選擇相容授權。

## 授權原則

- 沒有 LICENSE 的 GitHub 專案視為不可複製程式碼。
- 參考概念不等於搬用實作；核心演算法與程式碼應自行撰寫。
- 引入第三方函式庫前，需記錄版本、用途、授權與是否打包進成品。
