plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "0.5.0"
}
rootProject.name = "annoation-processor-hands-on"
include("annotation")
include("processor")
include("application")
