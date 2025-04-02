package de.kfabi.jlens;

import java.util.function.Consumer;
import java.util.function.UnaryOperator;

public class CopyScope<T> {
  private T state;

  private CopyScope(T initial) {
    state = initial;
  }

  public <R> void set(Lens<T, R> lens, R set) {
    state = lens.set(state, set);
  }

  public <R> void modify(Lens<T, R> lens, UnaryOperator<R> modify) {
    state = lens.modify(state, modify);
  }

  public <R> R get(Lens<T, R> lens) {
    return lens.get(state);
  }

  public static <T> T copy(T copy, Consumer<CopyScope<T>> copyProgram) {
    var scope = new CopyScope<>(copy);
    copyProgram.accept(scope);
    return scope.state;
  }
}
