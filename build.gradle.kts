plugins {
    java
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    // JUnit 5 dependencies
    testImplementation ("org.junit.jupiter:junit-jupiter-api:5.8.1")
    implementation("com.google.code.gson:gson:2.8.9")
    testRuntimeOnly ("org.junit.jupiter:junit-jupiter-engine:5.8.1")
}

tasks.test {
    useJUnitPlatform()
}