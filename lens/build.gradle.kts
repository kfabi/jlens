plugins {
  java
  alias(libs.plugins.spotless)
  `maven-publish`
}

repositories { mavenCentral() }

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
  publications {
    register<MavenPublication>("gpr") {
      artifactId = rootProject.name
      from(components["java"])
    }
  }
}
