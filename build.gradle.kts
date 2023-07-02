plugins {
    id("java")
    id("com.github.johnrengelman.shadow") version "8.1.1"
}

group = "de.netpacket"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    // https://mvnrepository.com/artifact/com.hazelcast/hazelcast
    implementation ("com.hazelcast:hazelcast:5.3.1")

    // https://mvnrepository.com/artifact/org.mongodb/mongo-java-driver
    implementation ("org.mongodb:mongo-java-driver:3.12.12")

    // https://mvnrepository.com/artifact/com.google.code.gson/gson
    compileOnly ("com.google.code.gson:gson:2.9.0")
}
tasks {
    compileJava {
        options.encoding = "UTF-8"
    }

    shadowJar {
        relocate("com.hazelcast", "de.rubymc.libs.hazelcast")
        relocate("org.mongodb", "de.rubymc.libs.mongodb")
        mergeServiceFiles()
    }

    build {
        dependsOn(shadowJar)
    }
}