package cn.mushuwei.singleton.type;

/**
 * 基于枚举的单例实现, Effective Java 中文第二版(Joshua Bloch) p.15
 * 此实现是线程安全的，但是添加任何其他方法及其线程安全是开发人员的责任
 *
 * @author james mu
 * @date 2020/4/14 09:01
 */
public enum  EnumMouseDriver {

    INSTANCE;

    @Override
    public String toString(){
        return getDeclaringClass().getCanonicalName() + "@" + hashCode();
    }
}
