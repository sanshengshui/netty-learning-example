package cn.mushuwei.singleton;

import cn.mushuwei.singleton.type.*;
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
        ThreadSafeLazyLoadedMouseDriver threadSafeMouseDriver1 = ThreadSafeLazyLoadedMouseDriver.getInstance();
        ThreadSafeLazyLoadedMouseDriver threadSafeMouseDriver2 = ThreadSafeLazyLoadedMouseDriver.getInstance();
        LOGGER.info("threadSafeMouseDriver1={}", threadSafeMouseDriver1);
        LOGGER.info("threadSafeMouseDriver2={}", threadSafeMouseDriver2);

        // enum singleton
        EnumMouseDriver enumMouseDriver1 = EnumMouseDriver.INSTANCE;
        EnumMouseDriver enumMouseDriver2 = EnumMouseDriver.INSTANCE;
        LOGGER.info("enumMouseDriver1={}", enumMouseDriver1);
        LOGGER.info("enumMouseDriver2={}", enumMouseDriver2);

        // double checked locking
        ThreadSafeDoubleCheckLocking dcl1 = ThreadSafeDoubleCheckLocking.getInstance();
        LOGGER.info(dcl1.toString());
        ThreadSafeDoubleCheckLocking dcl2 = ThreadSafeDoubleCheckLocking.getInstance();
        LOGGER.info(dcl2.toString());

        // initialize on demand holder idiom
        InitializingOnDemandHolderIdiom demandHolderIdiom = InitializingOnDemandHolderIdiom.getInstance();
        LOGGER.info(demandHolderIdiom.toString());
        InitializingOnDemandHolderIdiom demandHolderIdiom2 = InitializingOnDemandHolderIdiom.getInstance();
        LOGGER.info(demandHolderIdiom2.toString());
    }
}
