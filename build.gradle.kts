import org.jetbrains.kotlin.gradle.plugin.getKotlinPluginVersion

plugins {
    kotlin("jvm") version "2.1.0"
    id("com.gradleup.shadow") version "8.3.5"
    id("com.xpdustry.kotlin-shadow-relocator") version "2.0.0"
    id("maven-publish")
}

group = "me.mozarez.aurora"
version = "1.0"
val adventureVersion = "4.17.0"
val adventurePlatform = "4.3.4"
val commandApiVersion = "9.7.0"

repositories {
    mavenCentral()
    maven("https://repo.papermc.io/repository/maven-public/") {
        name = "papermc-repo"
    }
    maven("https://oss.sonatype.org/content/groups/public/") {
        name = "sonatype"
    }
    maven("https://jitpack.io")
    maven("https://repo.codemc.io/repository/maven-releases/")
    maven("https://repo.extendedclip.com/content/repositories/placeholderapi/")
    maven("https://repo.nexomc.com/releases")
    mavenLocal()
}

dependencies {
    compileOnly("com.nexomc:nexo:1.13.0")

    compileOnly(fileTree("dep") { include("*.jar") })

    compileOnly("org.jetbrains.kotlinx:kotlinx-serialization-json:1.8.0")

    compileOnly("net.luckperms:api:5.4")
    compileOnly("me.clip:placeholderapi:2.11.6")
    compileOnly("io.papermc.paper:paper-api:1.19.4-R0.1-SNAPSHOT")
    compileOnly("net.kyori:adventure-text-minimessage:$adventureVersion")
    compileOnly("net.kyori:adventure-text-serializer-legacy:$adventureVersion")
    compileOnly("net.kyori:adventure-platform-bukkit:$adventurePlatform")
    compileOnly("org.jetbrains.kotlin:kotlin-stdlib-jdk8:2.1.0")

    compileOnly("net.objecthunter:exp4j:0.4.8")
    compileOnly("dev.jorel:commandapi-bukkit-kotlin:9.6.1")

    implementation("dev.jorel:commandapi-bukkit-shade:10.1.2")

    //implementation("com.github.technicallycoded:FoliaLib:main-SNAPSHOT")

    implementation("com.samjakob:SpiGUI:v1.3.1")
}

val targetJavaVersion = 21
kotlin {
    jvmToolchain(targetJavaVersion)
}

tasks.shadowJar {
    //relocate("com.tcoded.folialib", "me.mozarez.aurora.mzlib.shaded.folialib")
    relocate("com.samjakob", "me.mozarez.aurora.mzlib.shaded")
    //relocate("kotlin", "me.mozarez.aurora.mzlib.shaded.kotlin")
    //relocate("com.github.retrooper", "me.mozarez.aurora.shaded")
}

tasks.build {
    dependsOn("shadowJar")
}

tasks.processResources {
    val props = mapOf(
        "version" to version,
        "kotlin" to getKotlinPluginVersion(),
        "adventure" to adventureVersion,
        "adventurePlatform" to adventurePlatform
    )
    inputs.properties(props)
    filteringCharset = "UTF-8"
    filesMatching("plugin.yml") {
        expand(props)
    }
}

publishing {
    publications {
        create<MavenPublication>("maven") {
            //artifact(tasks["shadowJar"])
            project.shadow.component(this)
        }
    }
}
