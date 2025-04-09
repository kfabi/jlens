package de.kfabi.jlens;

import java.util.function.UnaryOperator;

public record CopyBuilder<T>(T build) {

  public <R> CopyBuilder<T> set(Lens<T, R> lens, R set) {
    return new CopyBuilder<>(lens.set(build, set));
  }

  public <R> CopyBuilder<T> modify(Lens<T, R> lens, UnaryOperator<R> modify) {
    return new CopyBuilder<>(lens.modify(build, modify));
  }

  public <R> R get(Lens<T, R> lens) {
    return lens.get(build);
  }

  public static <T> CopyBuilder<T> of(T initial) {
    return new CopyBuilder<>(initial);
  }
}
