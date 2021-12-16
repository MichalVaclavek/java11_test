package cz.vaclavek.java11;

@FunctionalInterface
interface Sink<T> {
    void flush(T t);
}
