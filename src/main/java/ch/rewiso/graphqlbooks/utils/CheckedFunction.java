package ch.rewiso.graphqlbooks.utils;

import java.util.function.Function;


@FunctionalInterface
public interface CheckedFunction<T, R> extends Function<T, R> {


    R applyThrows(T t) throws Exception;

    @Override
    default R apply(T t) {
        try {
            return applyThrows(t);
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }
}
