package cn.kimmking.research.cluster.switchover;

import java.util.*;

/**
 * Two city Dc items switch over with given threshold and initial sizes.
 *
 * @Author : kimmking(kimmking@apache.org)
 * @create 2022/11/25 21:50
 */
public class CityDcSwitchover<T> implements Switchover<T> {

    // CombineSwitchStrategy -> combine origin and backup dc items,
    // RandomSwitchStrategy -> random to origin or backup dc items.
    private SwitchStrategy<T> switchStrategy = new RandomSwitchStrategy<>();

    @Override
    public List<T> filter(int originSize, List<T> originItems, int backupSize, List<T> backupItems, double threshold) {
        // 1. if origin dc has no nodes, return backupItems, it's all nodes of current dc switch over.
        if(null == originItems | originItems.isEmpty()) {
            return backupItems;
        }
        // 2. check originSize > alive size
        assert originSize > originItems.size();
        // 3. calculate current percent
        double percent =  (double) originItems.size()/originSize;
        // 4. check current percent >= threshold and return originItems
        if(percent >= threshold) {
            return originItems;
        }

        // 5. check originSize > alive size
        assert backupSize > backupItems.size();
        double backupPercent = backupItems.size()/backupSize;
        // 6. if backup dc is switchover now, here return the origin.
        if(backupPercent <= threshold) {
            return originItems;
        }
        // 7. calculate secured one item Load, load ratio in origin dc and in backup dc.
        double securedItemLoad = 1/(originSize*threshold);
        double originLoadRatio = originItems.size()*securedItemLoad;
        double backupLoadRatio = 1 - originLoadRatio;

        // no ratio to switch then return the origin or use switchStrategy.
        return backupLoadRatio <= 0 ? originItems : switchStrategy.doStrategy(originItems, backupItems, originLoadRatio, backupLoadRatio);
    }

    public interface SwitchStrategy<T> {
        List<T> doStrategy(List<T> originItems, List<T> backupItems, double initialLoadRatio, double backupLoadRatio);
    }

    // random to pick origin or backup,
    // and alive backup items will all to take the specified ratio invokes.
    public static class RandomSwitchStrategy<T> implements SwitchStrategy<T> {
        final Random random = new Random();

        @Override
        public List<T> doStrategy(List<T> originItems, List<T> backupItems, double initialLoadRatio, double backupLoadRatio) {
            return random.nextDouble() <= backupLoadRatio ? backupItems : originItems;
        }
    }

    public static class CombineSwitchStrategy<T> implements SwitchStrategy<T> {

        public List<T> doStrategy(List<T> originItems, List<T> backupItems, double initialLoadRatio, double backupLoadRatio) {
            int backupNeedItemSize = (int) Math.ceil(backupLoadRatio / initialLoadRatio);
            assert backupNeedItemSize > 0;
            // combine backupNeedItemSize items to origin
            return combine(originItems, backupItems, backupNeedItemSize > backupItems.size() ? backupItems.size() : backupNeedItemSize);
        }

        // combine all origin items and backupNeedItemSize backup items to a new list
        private List<T> combine(List<T> originItems, List<T> backupItems, int backupNeedItemSize) {
            List<T> newItems = new ArrayList<>(originItems.size() + backupNeedItemSize);
            newItems.addAll(originItems);
            newItems.addAll(backupItems.subList(0, backupNeedItemSize));
            return originItems;
        }
    }

}
