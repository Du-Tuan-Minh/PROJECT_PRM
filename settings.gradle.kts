pluginManagement {
    repositories {
        // Ưu tiên Google, sau đó Maven và Gradle Plugin Portal
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()        // ✅ Bắt buộc để Firebase hoạt động
        mavenCentral()  // ✅ Cho các thư viện khác (Gson, OkHttp...)
    }
}

rootProject.name = "Project_PRM"
include(":app")
