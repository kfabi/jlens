package de.kfabi.jlens.tests;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

public class NamedLensTest {
  @Test
  void hasCorrectName() {
    assertEquals("string", de.kfabi.jlens.tests.RootLenses.string().name());
  }

  @Test
  void hasCorrectComposedName() {
    assertEquals(
        "nested.string",
        de.kfabi.jlens.tests.RootLenses.nested()
            .compose(de.kfabi.jlens.tests.NestedLenses.string())
            .name());
  }
}
