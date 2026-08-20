plugins {
    id("com.gradleup.shadow") version "8.3.5"
}

dependencies {
    implementation(project(":boardgames-api"))
    compileOnly("io.papermc.paper:paper-api:26.2.build.98-stable")
}

tasks.processResources {
    filteringCharset = "UTF-8"
    filesMatching("plugin.yml") {
        expand("version" to project.version)
    }
}

tasks.shadowJar {
    archiveFileName.set("CrestBoardGames.jar")
    archiveClassifier.set("")
}

tasks.build {
    dependsOn(tasks.shadowJar)
}
