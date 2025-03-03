plugins { alias(libs.plugins.spotless) }

allprojects {
  group = "de.kfabi"

  version = "1.0.2-SNAPSHOT"
}

repositories { mavenCentral() }

spotless { kotlinGradle { ktfmt().googleStyle() } }
