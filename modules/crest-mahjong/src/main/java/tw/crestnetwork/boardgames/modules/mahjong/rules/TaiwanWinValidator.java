package tw.crestnetwork.boardgames.modules.mahjong.rules;

import tw.crestnetwork.boardgames.modules.mahjong.model.MahjongMeld;
import tw.crestnetwork.boardgames.modules.mahjong.model.MahjongTileType;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public final class TaiwanWinValidator {
    private static final int TOTAL_MELDS = 5;

    public boolean canWin(Collection<MahjongTileType> concealedTiles, Collection<MahjongMeld> exposedMelds) {
        Objects.requireNonNull(exposedMelds, "exposedMelds");
        if (exposedMelds.stream().anyMatch(Objects::isNull)) {
            return false;
        }
        return canWin(concealedTiles, exposedMelds.size());
    }

    public boolean canWin(List<MahjongTileType> concealedTiles, int exposedMeldCount) {
        if (exposedMeldCount < 0 || exposedMeldCount > TOTAL_MELDS) {
            return false;
        }
        return canWin((Collection<MahjongTileType>) concealedTiles, exposedMeldCount);
    }

    private boolean canWin(Collection<MahjongTileType> concealedTiles, int exposedMeldCount) {
        Objects.requireNonNull(concealedTiles, "concealedTiles");
        int meldsNeeded = TOTAL_MELDS - exposedMeldCount;
        if (meldsNeeded < 0 || concealedTiles.size() != meldsNeeded * 3 + 2
                || concealedTiles.stream().anyMatch(MahjongTileType::isFlower)) {
            return false;
        }

        int[] counts = new int[MahjongTileType.values().length];
        for (MahjongTileType tile : concealedTiles) {
            if (++counts[tile.ordinal()] > tile.copiesInTaiwanSet()) {
                return false;
            }
        }

        for (MahjongTileType pair : MahjongTileType.values()) {
            if (counts[pair.ordinal()] < 2 || pair.isFlower()) {
                continue;
            }
            counts[pair.ordinal()] -= 2;
            if (canFormMelds(counts, meldsNeeded, new HashMap<>())) {
                return true;
            }
            counts[pair.ordinal()] += 2;
        }
        return false;
    }

    private boolean canFormMelds(int[] counts, int meldsNeeded, Map<String, Boolean> memo) {
        if (meldsNeeded == 0) {
            return Arrays.stream(counts).allMatch(count -> count == 0);
        }

        String key = meldsNeeded + ":" + Arrays.toString(counts);
        Boolean cached = memo.get(key);
        if (cached != null) {
            return cached;
        }

        int first = -1;
        for (int index = 0; index < counts.length; index++) {
            if (counts[index] > 0) {
                first = index;
                break;
            }
        }
        if (first < 0) {
            return false;
        }

        if (counts[first] >= 3) {
            counts[first] -= 3;
            if (canFormMelds(counts, meldsNeeded - 1, memo)) {
                counts[first] += 3;
                memo.put(key, true);
                return true;
            }
            counts[first] += 3;
        }

        MahjongTileType tile = MahjongTileType.values()[first];
        if (tile.isSuited() && tile.rank() <= 7) {
            MahjongTileType second = MahjongTileType.suited(tile.suit(), tile.rank() + 1);
            MahjongTileType third = MahjongTileType.suited(tile.suit(), tile.rank() + 2);
            if (counts[second.ordinal()] > 0 && counts[third.ordinal()] > 0) {
                counts[first]--;
                counts[second.ordinal()]--;
                counts[third.ordinal()]--;
                if (canFormMelds(counts, meldsNeeded - 1, memo)) {
                    counts[first]++;
                    counts[second.ordinal()]++;
                    counts[third.ordinal()]++;
                    memo.put(key, true);
                    return true;
                }
                counts[first]++;
                counts[second.ordinal()]++;
                counts[third.ordinal()]++;
            }
        }

        memo.put(key, false);
        return false;
    }
}
