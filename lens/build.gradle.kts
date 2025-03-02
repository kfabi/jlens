plugins {
  java
  alias(libs.plugins.spotless)
}

repositories { mavenCentral() }

spotless {
  java { googleJavaFormat() }
  kotlinGradle { ktfmt().googleStyle() }
}
