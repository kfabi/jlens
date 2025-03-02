plugins { alias(libs.plugins.spotless) }

group = "de.kfabi"

version = "1.0-SNAPSHOT"

repositories { mavenCentral() }

spotless { kotlinGradle { ktfmt().googleStyle() } }
