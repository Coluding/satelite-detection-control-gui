plugins {
    id("java")
}

jar {
    manifest {
        attributes 'Main-Class': 'org.Main' // Replace 'com.example.MainClass' with the fully qualified name of your main class
    }
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.jetbrains:annotations:24.0.0")
    implementation("org.jetbrains:annotations:24.0.0")
    testImplementation(platform("org.junit:junit-bom:5.9.1"))
    testImplementation("org.junit.jupiter:junit-jupiter")
}

