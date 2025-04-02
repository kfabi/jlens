package de.kfabi.jlens;

import com.google.auto.service.AutoService;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Set;
import javax.annotation.processing.*;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.TypeElement;
import javax.tools.JavaFileObject;

@SupportedAnnotationTypes("de.kfabi.jlens.Lenses")
@SupportedSourceVersion(SourceVersion.RELEASE_21)
@AutoService(Processor.class)
public class GenerateLensesProcessor extends AbstractProcessor {
  @Override
  public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
    for (var annotation : annotations) {
      for (Element element : roundEnv.getElementsAnnotatedWith(annotation)) {
        var typeElement = (TypeElement) element;
        if (!typeElement.getKind().equals(ElementKind.RECORD)) {
          throw new RuntimeException(
              "Annotation @"
                  + Lenses.class.getSimpleName()
                  + " is only applicable to records. Please remove it from class '"
                  + ((TypeElement) element).getQualifiedName()
                  + "'.");
        }
        try {
          writeLensFile(typeElement);
        } catch (IOException e) {
          throw new RuntimeException(e);
        }
      }
    }
    return true;
  }

  private static String className(Element te) {
    if (te.getEnclosingElement().getKind().isDeclaredType()) {
      return className(te.getEnclosingElement()) + "." + te.getSimpleName();
    } else {
      return te.getSimpleName().toString();
    }
  }

  private void writeLensFile(TypeElement recordElement) throws IOException {
    var fullyQualifiedSrcClassName = recordElement.getQualifiedName().toString();
    var simpleSrcClassName = className(recordElement);
    var targetClassName = simpleSrcClassName.replace(".", "") + "Lenses";
    // -1 for last dot in package
    var targetPackage =
        fullyQualifiedSrcClassName.substring(
            0, fullyQualifiedSrcClassName.length() - simpleSrcClassName.length() - 1);
    var indent = "    ";
    JavaFileObject lensFile =
        processingEnv.getFiler().createSourceFile(targetClassName, recordElement);
    try (PrintWriter out = new PrintWriter(lensFile.openWriter())) {
      // package
      if (!targetPackage.isBlank()) {
        out.println("package " + targetPackage + ";");
        out.println();
      }
      // imports
      out.println("import de.kfabi.jlens.NamedLens;");
      out.println();

      // class head
      out.println("public class " + targetClassName + " {");
      out.println();

      // private constructor
      out.print(indent);
      out.print("private ");
      out.print(targetClassName);
      out.println("() {}");
      out.println();

      // lenses
      new LensWriter(out, recordElement, indent).printLenses();

      // class closed
      out.println("}");
    }
  }
}
