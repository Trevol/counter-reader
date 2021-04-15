dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        maven(url="https://kotlin.bintray.com/kotlinx")
        //jcenter() // Warning: this repository is going to shut down soon
    }
}
rootProject.name = "CounterReader"
include(":app")
 