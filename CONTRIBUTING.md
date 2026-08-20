# 貢獻指南

感謝你對 CrestBoardGames 的興趣。

1. 先建立 Issue 說明功能或問題。
2. 不要將伺服器 Token、資料庫密碼或玩家資料提交到 Git。
3. 新遊戲必須透過 `boardgames-api`，不得直接依賴核心內部類別。
4. GUI 必須攔截一般點擊、Shift 點擊、數字鍵交換、拖曳與副手交換。
5. 提交前執行 `./gradlew clean build`。

目前授權條款尚待專案擁有者決定，因此暫不接受包含第三方程式碼的大型移植。
