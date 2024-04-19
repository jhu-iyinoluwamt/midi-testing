plugins {
    id("java")
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(platform("org.junit:junit-bom:5.9.1"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testImplementation("org.mockito:mockito-core:3.+")
    implementation(files("library/core.jar"))
//    testImplementation("org.powermock:powermock-module-junit4:2.0.9")
//    testImplementation("org.powermock:powermock-api-mockito2:2.0.9")
}

tasks.test {
    useJUnitPlatform()
}