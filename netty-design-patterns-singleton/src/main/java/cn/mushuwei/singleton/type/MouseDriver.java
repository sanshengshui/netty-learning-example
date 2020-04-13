package cn.mushuwei.singleton.type;

/**
 * @author james mu
 * @date 2020/4/11 12:50
 */
public final class MouseDriver {

    private MouseDriver () {
    }

    private static final MouseDriver INSTANCE = new MouseDriver();


    public static MouseDriver getInstance() {
        return INSTANCE;
    }

}
