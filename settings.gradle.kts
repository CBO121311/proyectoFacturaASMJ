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
    }
}

rootProject.name = "Invoice"
include(":app")
include(":features:accountsignin")
include(":features:accountsignup")
include(":features:customer")
include(":infrastructure:firebase")
include(":infrastructure:printer")
include(":base:ui")
include(":base:utils")
include(":domain:invoice")
include(":features:invoicemodule")
include(":features:item")
include(":features:task")
