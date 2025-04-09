package de.kfabi.jlens.tests;

import static org.junit.jupiter.api.Assertions.assertEquals;

import de.kfabi.jlens.CopyBuilder;
import org.junit.jupiter.api.Test;

public class CopyBuilderTest {
  static Root.Inner inner = new Root.Inner("inner");
  static Nested nested = new Nested("nested");
  static Typed<String> typed = new Typed<>("typed");
  static Primitive primitive = new Primitive(3, true);
  static Root root = new Root(inner, nested, typed, primitive, "root");

  @Test
  void set() {
    var newRoot =
        CopyBuilder.of(root)
            .set(
                de.kfabi.jlens.tests.RootLenses.inner()
                    .compose(de.kfabi.jlens.tests.RootInnerLenses.string()),
                "new inner")
            .build();
    assertEquals("new inner", newRoot.inner().string());
  }

  @Test
  void modify() {
    var newRoot =
        CopyBuilder.of(root)
            .modify(de.kfabi.jlens.tests.RootLenses.string(), x -> "new " + x)
            .build();
    assertEquals("new root", newRoot.string());
  }
}
