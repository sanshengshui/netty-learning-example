package cn.mushuwei.iterator;

/**
 * @author james mu
 * @date 2020/4/17 10:18
 */
public interface Iterator<T> {

    boolean hasNext();

    T next();
}
