plugins {
    alias(libs.plugins.kotlinMultiplatform)
}

group = "me.user"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

kotlin {
    linuxX64("telegram") {
        binaries {
            executable("bootstrap") {
                entryPoint = "main"
            }
        }
    }

    sourceSets {
        val commonMain by getting
        val telegramMain by getting
    }
}

tasks.register("buildBootstrap") {
    dependsOn("linkBootstrapReleaseExecutableTelegram")
    doLast {
        println("[SUCCESS] Bootstrap built: build/bin/telegram/releaseExecutable/bootstrap")
    }
}

