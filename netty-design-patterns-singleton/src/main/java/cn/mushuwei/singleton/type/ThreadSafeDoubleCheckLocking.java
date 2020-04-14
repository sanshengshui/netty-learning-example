package cn.mushuwei.singleton.type;

/**
 * @author james mu
 * @date 2020/4/14 09:34
 */
public class ThreadSafeDoubleCheckLocking {

    private volatile static ThreadSafeDoubleCheckLocking uniqueInstance;

    private ThreadSafeDoubleCheckLocking() {
    }

    public static ThreadSafeDoubleCheckLocking getInstance() {
        if (uniqueInstance == null) {
            synchronized (ThreadSafeDoubleCheckLocking.class) {
                if (uniqueInstance == null) {
                    uniqueInstance = new ThreadSafeDoubleCheckLocking();
                }
            }
        }
        return uniqueInstance;
    }
}
