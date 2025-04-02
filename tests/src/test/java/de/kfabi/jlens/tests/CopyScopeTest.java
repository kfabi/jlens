package de.kfabi.jlens.tests;

import static org.junit.jupiter.api.Assertions.assertEquals;

import de.kfabi.jlens.CopyScope;
import org.junit.jupiter.api.Test;

public class CopyScopeTest {
  static Root.Inner inner = new Root.Inner("inner");
  static Nested nested = new Nested("nested");
  static Typed<String> typed = new Typed<>("typed");
  static Primitive primitive = new Primitive(3, true);
  static Root root = new Root(inner, nested, typed, primitive, "root");

  @Test
  void test() {
    var newRoot =
        CopyScope.copy(
            root,
            s -> {
              s.set(
                  de.kfabi.jlens.tests.RootLenses.inner()
                      .compose(de.kfabi.jlens.tests.RootInnerLenses.string()),
                  "new inner");
              assertEquals("root", s.get(de.kfabi.jlens.tests.RootLenses.string()));
              s.modify(de.kfabi.jlens.tests.RootLenses.string(), x -> "new " + x);
              assertEquals("new root", s.get(de.kfabi.jlens.tests.RootLenses.string()));
            });
    assertEquals("new inner", newRoot.inner().string());
    assertEquals("new root", newRoot.string());
  }
}
