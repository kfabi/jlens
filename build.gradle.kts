plugins { alias(libs.plugins.spotless) }

allprojects {
  group = "de.kfabi"

  version = "1.0.2"
}

repositories { mavenCentral() }

spotless { kotlinGradle { ktfmt().googleStyle() } }
