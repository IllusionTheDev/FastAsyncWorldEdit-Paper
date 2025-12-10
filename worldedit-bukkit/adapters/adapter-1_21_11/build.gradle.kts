import io.papermc.paperweight.userdev.PaperweightUserDependenciesExtension

plugins {
    id("buildlogic.adapter")
}

configurations.all {
    resolutionStrategy.capabilitiesResolution.withCapability("org.lz4:lz4-java") {
        selectHighestVersion()
    }
}

tasks.named("reobfJar") {
    // Skip the reobfJar task without breaking any dependencies
    onlyIf { false }
}

tasks.named("jar") {
    // Mojang no longer obfuscates jars
    // As a quick dirty fix, we'll just copy the build output into the reobfJar output so any
    // following tasks can use it, and we pretend everything's perfectly fine
    doLast {
        val reobfJar = tasks.named("reobfJar").get()
        val outputJar = reobfJar.outputs.files.singleFile
        outputJar.parentFile.mkdirs()
        outputs.files.singleFile.copyTo(outputJar, overwrite = true)
    }
}

dependencies {
    // https://repo.papermc.io/service/rest/repository/browse/maven-public/io/papermc/paper/dev-bundle/
    the<PaperweightUserDependenciesExtension>().paperDevBundle("1.21.11-rc3-R0.1-20251208.200020-2")
    compileOnly(libs.paperLib)
}
