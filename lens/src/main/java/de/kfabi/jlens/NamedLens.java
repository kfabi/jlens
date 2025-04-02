package de.kfabi.jlens;

public interface NamedLens<T, R> extends Lens<T, R> {
  String name();

  default <A> NamedLens<T, A> compose(NamedLens<R, A> other) {
    var current = this;
    return new NamedLens<>() {

      @Override
      public String name() {
        return current.name() + "." + other.name();
      }

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
