package tw.crestnetwork.boardgames.modules.mahjong.model;

import java.util.Arrays;

public enum MahjongTileType {
    CHARACTER_1(TileSuit.CHARACTERS, 1, "一萬"),
    CHARACTER_2(TileSuit.CHARACTERS, 2, "二萬"),
    CHARACTER_3(TileSuit.CHARACTERS, 3, "三萬"),
    CHARACTER_4(TileSuit.CHARACTERS, 4, "四萬"),
    CHARACTER_5(TileSuit.CHARACTERS, 5, "五萬"),
    CHARACTER_6(TileSuit.CHARACTERS, 6, "六萬"),
    CHARACTER_7(TileSuit.CHARACTERS, 7, "七萬"),
    CHARACTER_8(TileSuit.CHARACTERS, 8, "八萬"),
    CHARACTER_9(TileSuit.CHARACTERS, 9, "九萬"),
    DOT_1(TileSuit.DOTS, 1, "一筒"),
    DOT_2(TileSuit.DOTS, 2, "二筒"),
    DOT_3(TileSuit.DOTS, 3, "三筒"),
    DOT_4(TileSuit.DOTS, 4, "四筒"),
    DOT_5(TileSuit.DOTS, 5, "五筒"),
    DOT_6(TileSuit.DOTS, 6, "六筒"),
    DOT_7(TileSuit.DOTS, 7, "七筒"),
    DOT_8(TileSuit.DOTS, 8, "八筒"),
    DOT_9(TileSuit.DOTS, 9, "九筒"),
    BAMBOO_1(TileSuit.BAMBOOS, 1, "一條"),
    BAMBOO_2(TileSuit.BAMBOOS, 2, "二條"),
    BAMBOO_3(TileSuit.BAMBOOS, 3, "三條"),
    BAMBOO_4(TileSuit.BAMBOOS, 4, "四條"),
    BAMBOO_5(TileSuit.BAMBOOS, 5, "五條"),
    BAMBOO_6(TileSuit.BAMBOOS, 6, "六條"),
    BAMBOO_7(TileSuit.BAMBOOS, 7, "七條"),
    BAMBOO_8(TileSuit.BAMBOOS, 8, "八條"),
    BAMBOO_9(TileSuit.BAMBOOS, 9, "九條"),
    EAST(TileSuit.WINDS, 1, "東風"),
    SOUTH(TileSuit.WINDS, 2, "南風"),
    WEST(TileSuit.WINDS, 3, "西風"),
    NORTH(TileSuit.WINDS, 4, "北風"),
    RED_DRAGON(TileSuit.DRAGONS, 1, "紅中"),
    GREEN_DRAGON(TileSuit.DRAGONS, 2, "青發"),
    WHITE_DRAGON(TileSuit.DRAGONS, 3, "白板"),
    SPRING(TileSuit.FLOWERS, 1, "春"),
    SUMMER(TileSuit.FLOWERS, 2, "夏"),
    AUTUMN(TileSuit.FLOWERS, 3, "秋"),
    WINTER(TileSuit.FLOWERS, 4, "冬"),
    PLUM(TileSuit.FLOWERS, 5, "梅"),
    ORCHID(TileSuit.FLOWERS, 6, "蘭"),
    BAMBOO_FLOWER(TileSuit.FLOWERS, 7, "竹"),
    CHRYSANTHEMUM(TileSuit.FLOWERS, 8, "菊");

    private final TileSuit suit;
    private final int rank;
    private final String displayName;

    MahjongTileType(TileSuit suit, int rank, String displayName) {
        this.suit = suit;
        this.rank = rank;
        this.displayName = displayName;
    }

    public TileSuit suit() {
        return suit;
    }

    public int rank() {
        return rank;
    }

    public String displayName() {
        return displayName;
    }

    public boolean isSuited() {
        return suit == TileSuit.CHARACTERS || suit == TileSuit.DOTS || suit == TileSuit.BAMBOOS;
    }

    public boolean isHonor() {
        return suit == TileSuit.WINDS || suit == TileSuit.DRAGONS;
    }

    public boolean isFlower() {
        return suit == TileSuit.FLOWERS;
    }

    public int copiesInTaiwanSet() {
        return isFlower() ? 1 : 4;
    }

    public static MahjongTileType suited(TileSuit suit, int rank) {
        if (rank < 1 || rank > 9) {
            throw new IllegalArgumentException("花色牌數字必須介於 1 到 9");
        }
        return Arrays.stream(values())
                .filter(tile -> tile.suit == suit && tile.rank == rank && tile.isSuited())
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("不是有效的數牌花色：" + suit));
    }
}
