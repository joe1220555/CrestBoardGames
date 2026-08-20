package tw.crestnetwork.boardgames.modules.mahjong.rules;

import org.junit.jupiter.api.Test;
import tw.crestnetwork.boardgames.modules.mahjong.model.MahjongTileType;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class TaiwanWinValidatorTest {
    private final TaiwanWinValidator validator = new TaiwanWinValidator();

    @Test
    void acceptsFiveMeldsAndOnePair() {
        assertTrue(validator.canWin(List.of(
                t("1m"), t("1m"), t("1m"),
                t("2m"), t("3m"), t("4m"),
                t("3p"), t("4p"), t("5p"),
                t("6s"), t("7s"), t("8s"),
                t("E"), t("E"), t("E"),
                t("R"), t("R")
        ), 0));
    }

    @Test
    void acceptsConcealedPartWhenOneMeldIsExposed() {
        assertTrue(validator.canWin(List.of(
                t("1m"), t("2m"), t("3m"),
                t("4m"), t("5m"), t("6m"),
                t("2p"), t("2p"), t("2p"),
                t("N"), t("N"), t("N"),
                t("W"), t("W")
        ), 1));
    }

    @Test
    void rejectsJapaneseFourMeldHandAndBrokenHand() {
        assertFalse(validator.canWin(List.of(
                t("1m"), t("1m"), t("1m"),
                t("2m"), t("3m"), t("4m"),
                t("3p"), t("4p"), t("5p"),
                t("E"), t("E"), t("E"),
                t("R"), t("R")
        ), 0));

        assertFalse(validator.canWin(List.of(
                t("1m"), t("1m"), t("1m"),
                t("2m"), t("3m"), t("5m"),
                t("3p"), t("4p"), t("5p"),
                t("6s"), t("7s"), t("8s"),
                t("E"), t("E"), t("E"),
                t("R"), t("R")
        ), 0));
    }

    @Test
    void rejectsFlowersAndImpossibleFifthCopy() {
        assertFalse(validator.canWin(List.of(
                t("1m"), t("1m"), t("1m"),
                t("2m"), t("3m"), t("4m"),
                t("3p"), t("4p"), t("5p"),
                t("6s"), t("7s"), t("8s"),
                t("E"), t("E"), t("E"),
                MahjongTileType.SPRING, MahjongTileType.SPRING
        ), 0));

        assertFalse(validator.canWin(List.of(
                t("1m"), t("1m"), t("1m"), t("1m"), t("1m"),
                t("2m"), t("3m"), t("4m"),
                t("3p"), t("4p"), t("5p"),
                t("6s"), t("7s"), t("8s"),
                t("E"), t("E"), t("E")
        ), 0));
    }

    private MahjongTileType t(String notation) {
        return switch (notation) {
            case "E" -> MahjongTileType.EAST;
            case "N" -> MahjongTileType.NORTH;
            case "W" -> MahjongTileType.WEST;
            case "R" -> MahjongTileType.RED_DRAGON;
            default -> {
                int rank = Character.digit(notation.charAt(0), 10);
                yield switch (notation.charAt(1)) {
                    case 'm' -> MahjongTileType.suited(tw.crestnetwork.boardgames.modules.mahjong.model.TileSuit.CHARACTERS, rank);
                    case 'p' -> MahjongTileType.suited(tw.crestnetwork.boardgames.modules.mahjong.model.TileSuit.DOTS, rank);
                    case 's' -> MahjongTileType.suited(tw.crestnetwork.boardgames.modules.mahjong.model.TileSuit.BAMBOOS, rank);
                    default -> throw new IllegalArgumentException(notation);
                };
            }
        };
    }
}
