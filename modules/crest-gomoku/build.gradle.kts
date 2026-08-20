dependencies {
    compileOnly(project(":boardgames-api"))
}

tasks.processResources {
    filesMatching("crest-boardgame-module.yml") {
        expand("version" to project.version)
    }
}

tasks.jar {
    archiveFileName.set("CrestGomoku.jar")
}
