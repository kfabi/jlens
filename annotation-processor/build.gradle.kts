plugins {
  java
  alias(libs.plugins.spotless)
  `maven-publish`
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

publishing {
  repositories {
    maven {
      name = "GitHubPackages"
      url = uri("https://maven.pkg.github.com/kfabi/jlens")
      credentials {
        username = System.getenv("GITHUB_ACTOR")
        password = System.getenv("GITHUB_TOKEN")
      }
    }
  }
  publications { register<MavenPublication>("gpr") { from(components["java"]) } }
}
