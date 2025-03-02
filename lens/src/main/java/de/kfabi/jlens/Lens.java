package de.kfabi.jlens;

import java.util.function.UnaryOperator;

public interface Lens<T, R> {
  R get(T t);

  T set(T t, R r);

  default T modify(T t, UnaryOperator<R> op) {
    return set(t, op.apply(get(t)));
  }

  default <A> Lens<T, A> compose(Lens<R, A> other) {
    var current = this;
    return new Lens<>() {
      @Override
      public A get(T t) {
        return other.get(current.get(t));
      }

      @Override
      public T set(T t, A a) {
        return current.modify(t, r -> other.set(r, a));
      }
    };
  }
}
