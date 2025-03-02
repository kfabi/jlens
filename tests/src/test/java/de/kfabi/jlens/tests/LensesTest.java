package de.kfabi.jlens.tests;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class LensesTest {
  static Root.Inner inner = new Root.Inner("inner");
  static Nested nested = new Nested("nested");
  static Typed<String> typed = new Typed<>("typed");
  static Primitive primitive = new Primitive(3, true);
  static Root root = new Root(inner, nested, typed, primitive, "root");

  @Test
  void allLensesExist() {
    de.kfabi.jlens.tests.RootLenses.inner();
    de.kfabi.jlens.tests.RootLenses.nested();
    de.kfabi.jlens.tests.RootLenses.typed();
    de.kfabi.jlens.tests.RootLenses.primitive();
    de.kfabi.jlens.tests.RootLenses.string();
    de.kfabi.jlens.tests.RootInnerLenses.string();
    de.kfabi.jlens.tests.NestedLenses.string();
    de.kfabi.jlens.tests.TypedLenses.t();
    de.kfabi.jlens.tests.PrimitiveLenses.someBoolean();
    de.kfabi.jlens.tests.PrimitiveLenses.someInt();
  }

  @Test
  void typedLensWorks() {
    assertEquals(typed.t(), de.kfabi.jlens.tests.TypedLenses.<String>t().get(typed));
    assertEquals("new", de.kfabi.jlens.tests.TypedLenses.<String>t().set(typed, "new").t());
  }

  @Test
  void primitiveLensWorks() {
    assertEquals(
        primitive.someInt(), de.kfabi.jlens.tests.PrimitiveLenses.someInt().get(primitive));
    assertEquals(
        primitive.someBoolean(), de.kfabi.jlens.tests.PrimitiveLenses.someBoolean().get(primitive));
    assertEquals(5, de.kfabi.jlens.tests.PrimitiveLenses.someInt().set(primitive, 5).someInt());
    assertFalse(
        de.kfabi.jlens.tests.PrimitiveLenses.someBoolean().set(primitive, false).someBoolean());
  }

  @Test
  void compositionWorks() {
    var expected =
        new Root(root.inner(), root.nested(), root.typed(), new Primitive(-1, true), root.string());
    var composed =
        de.kfabi.jlens.tests.RootLenses.primitive()
            .compose(de.kfabi.jlens.tests.PrimitiveLenses.someInt());
    var actual = composed.set(root, -1);
    assertEquals(expected, actual);
    assertEquals(root.primitive().someInt(), composed.get(root));
  }
}
