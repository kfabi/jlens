plugins {
  java
  alias(libs.plugins.spotless)
}

repositories { mavenCentral() }

dependencies {
  implementation(libs.google.auto.service.annotations)
  annotationProcessor(libs.google.auto.service)
  implementation(project(":lens"))
}

spotless {
  java { googleJavaFormat() }
  kotlinGradle { ktfmt().googleStyle() }
}
