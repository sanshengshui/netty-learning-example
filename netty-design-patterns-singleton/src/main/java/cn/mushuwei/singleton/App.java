package cn.mushuwei.singleton;

import cn.mushuwei.singleton.type.MouseDriver;
import cn.mushuwei.singleton.type.ThreadSafeLazyLoadedMouseDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author james mu
 * @date 2020/4/11 12:46
 */
public class App {

    private static final Logger LOGGER = LoggerFactory.getLogger(App.class);

    public static void main(String[] args) {

        // eagerly initialized singleton
        MouseDriver mouseDriver1 = MouseDriver.getInstance();
        MouseDriver mouseDriver2 = MouseDriver.getInstance();
        LOGGER.info("mouseDriver1={}", mouseDriver1);
        LOGGER.info("mouseDriver2={}", mouseDriver2);

        // lazily initialized singleton
        ThreadSafeLazyLoadedMouseDriver threadSafeIvoryTower1 = ThreadSafeLazyLoadedMouseDriver.getInstance();
        ThreadSafeLazyLoadedMouseDriver threadSafeIvoryTower2 = ThreadSafeLazyLoadedMouseDriver.getInstance();
        LOGGER.info("threadSafeIvoryTower1={}", threadSafeIvoryTower1);
        LOGGER.info("threadSafeIvoryTower2={}", threadSafeIvoryTower2);
    }
}
