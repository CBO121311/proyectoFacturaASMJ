pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}



dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        maven ( url = "https://jitpack.io" )
    }
}

rootProject.name = "Invoice"
include(":app")
include(":features:account")
include(":features:customer")
include(":infrastructure:firebase")
include(":infrastructure:printer")
include(":base:ui")
include(":base:utils")
include(":domain:invoiceDomain")
include(":features:invoice")
include(":features:item")
include(":features:task")
