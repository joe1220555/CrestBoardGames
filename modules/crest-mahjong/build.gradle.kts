dependencies {
    compileOnly(project(":boardgames-api"))
    testImplementation("org.junit.jupiter:junit-jupiter:5.11.4")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

tasks.processResources {
    filesMatching("crest-boardgame-module.yml") {
        expand("version" to project.version)
    }
}

tasks.jar {
    archiveFileName.set("CrestMahjong.jar")
}
