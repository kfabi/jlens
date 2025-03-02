plugins {
  id("java")
  alias(libs.plugins.spotless)
}

repositories { mavenCentral() }

dependencies {
  annotationProcessor(project(":annotation-processor"))
  implementation(project(":lens"))

  testImplementation(platform(libs.junit.bom))
  testImplementation(libs.junit.jupiter)
}

spotless {
  java { googleJavaFormat() }
  kotlinGradle { ktfmt().googleStyle() }
}

tasks.test { useJUnitPlatform() }
