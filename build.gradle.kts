
plugins {
    id("java")
    id("com.github.johnrengelman.shadow") version "8.1.1"

}

group = "de.netpacket"
version = "1.1-SNAPSHOT"


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

/*
tasks.withType<Jar> {
    manifest {
        attributes["Main-Class"] = "de.rubymc.database.DatabaseBootstrap"
    }
}

 */


tasks {
    compileJava {
        options.encoding = "UTF-8"
    }
    shadowJar {
        mergeServiceFiles()
    }

    build {
        dependsOn(shadowJar)
    }
}