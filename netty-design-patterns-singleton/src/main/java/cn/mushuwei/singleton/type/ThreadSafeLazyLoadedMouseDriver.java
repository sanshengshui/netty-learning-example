package cn.mushuwei.singleton.type;

/**
 * @author james mu
 * @date 2020/4/13 23:36
 */
public final class ThreadSafeLazyLoadedMouseDriver {

    private static ThreadSafeLazyLoadedMouseDriver instance;

    private ThreadSafeLazyLoadedMouseDriver() {
        // protect against instantiation via reflection
        if (instance == null) {
            instance = this;
        } else {
            throw new IllegalStateException("Already initialized.");
        }
    }

    /**
     * The instance gets created only when it is called for first time. Lazy-loading
     */
    public static synchronized ThreadSafeLazyLoadedMouseDriver getInstance() {
        if (instance == null) {
            instance = new ThreadSafeLazyLoadedMouseDriver();
        }

        return instance;
    }
}
