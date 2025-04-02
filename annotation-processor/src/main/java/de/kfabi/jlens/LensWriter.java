package de.kfabi.jlens;

import java.io.PrintWriter;
import java.io.Writer;
import java.util.List;
import javax.lang.model.element.RecordComponentElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeKind;

public class LensWriter extends PrintWriter {
  private final TypeElement record;
  private final List<RecordComponentElement> components;
  private final String indent;

  public LensWriter(Writer out, TypeElement record, String indent) {
    super(out);
    this.record = record;
    this.indent = indent;
    this.components =
        record.getRecordComponents().stream().map(it -> (RecordComponentElement) it).toList();
  }

  public void printLenses() {
    for (int i = 0; i < components.size(); i++) {
      printSingleLens(i);
    }
  }

  private void printSingleLens(int index) {
    // start lens
    var component = components.get(index);
    printIndent();
    print("public static ");
    var typeParamters = record.getTypeParameters();
    if (!typeParamters.isEmpty()) {
      print('<');
      for (var typeParamter : typeParamters) {
        print(typeParamter.asType().toString());
      }
      print('>');
    }
    print(" NamedLens<");
    printRecordType();
    print(", ");
    printComponentType(component);
    print("> ");
    printComponentName(component);
    println("() { ");
    printIndent();
    printIndent();
    println("return new NamedLens<>() {");

    // name
    printName(component);

    // get
    printGet(component);

    // set
    printSet(index, component);

    // end lens
    printIndent();
    printIndent();
    println("};");
    printIndent();
    println("}");
    println();
  }

  private void printName(RecordComponentElement component) {
    printIndent();
    printIndent();
    println("@Override");
    printIndent();
    printIndent();
    print("public String name() { return \"");
    print(component.getSimpleName());
    print("\"; }");
    println();
  }

  private void printGet(RecordComponentElement component) {
    printIndent();
    printIndent();
    println("@Override");
    printIndent();
    printIndent();
    print("public ");
    printComponentType(component);
    print(" get(");
    printRecordType();
    print(" i) { return i.");
    printComponentName(component);
    print("(); }");
    println();
  }

  private void printSet(int index, RecordComponentElement component) {
    printIndent();
    printIndent();
    println("@Override");
    printIndent();
    printIndent();
    print("public ");
    printRecordType();
    print(" set(");
    printRecordType();
    print(" i, ");
    printComponentType(component);
    print(" s) { return new ");
    printRecordType();
    print("(");
    for (int i = 0; i < components.size(); i++) {
      if (i == index) {
        print("s");
      } else {
        print("i.");
        print(components.get(i).getSimpleName());
        print("()");
      }
      if (i < components.size() - 1) {
        print(", ");
      }
    }
    println("); }");
    println();
  }

  private void printIndent() {
    print(indent);
  }

  private void printRecordType() {
    print(record.asType().toString());
  }

  private void printComponentName(RecordComponentElement component) {
    print(component.getSimpleName());
  }

  private void printComponentType(RecordComponentElement component) {
    if (component.asType().getKind().isPrimitive()) {
      print(getPrimitiveWrapperClassName(component.asType().getKind()));
    } else {
      print(component.asType().toString());
    }
  }

  private static String getPrimitiveWrapperClassName(TypeKind tk) {
    Class clazz =
        switch (tk) {
          case BOOLEAN -> Boolean.class;
          case BYTE -> Byte.class;
          case SHORT -> Short.class;
          case INT -> Integer.class;
          case LONG -> Long.class;
          case CHAR -> Character.class;
          case FLOAT -> Float.class;
          case DOUBLE -> Double.class;
          default -> null;
        };
    if (clazz != null) {
      return clazz.getCanonicalName();
    } else return null;
  }
}
