package expression.operations;

/**
 * @author Georgiy Korneev (kgeorgiy@kgeorgiy.info)
 */
public interface Expression<T> extends ToMiniString {
    T evaluate(T x);
}
