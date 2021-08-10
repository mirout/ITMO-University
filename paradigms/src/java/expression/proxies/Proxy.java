package expression.proxies;

public interface Proxy<T> {
    T add(T x, T y);

    T subtract(T x, T y);

    T divide(T x, T y);

    T multiply(T x, T y);

    T abs(T value);

    T count(T value);

    T max(T x, T y);

    T min(T x, T y);

    T negate(T value);

    T sqrt(T value);

    T value(String value);

    T value(int value);

    T square(T value);

    T mod(T first, T second);
}
