package de.kfabi.jlens.tests;

import de.kfabi.jlens.Lenses;

@Lenses
public record Root(
    Inner inner, Nested nested, Typed<String> typed, Primitive primitive, String string) {

  @Lenses
  public record Inner(String string) {}
}
