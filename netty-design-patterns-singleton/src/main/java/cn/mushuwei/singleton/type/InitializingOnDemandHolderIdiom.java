package cn.mushuwei.singleton.type;

/**
 * @author james mu
 * @date 2020/4/14 09:12
 */
public final class InitializingOnDemandHolderIdiom {

    private InitializingOnDemandHolderIdiom() {
    }

    public static InitializingOnDemandHolderIdiom getInstance() {
        return HelperHolder.INSTANCE;
    }

    private static class HelperHolder {
        private static final InitializingOnDemandHolderIdiom INSTANCE =
                new InitializingOnDemandHolderIdiom();
    }
}
