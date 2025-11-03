plugins {
    alias(libs.plugins.jvm)
    alias(libs.plugins.kover)
    alias(libs.plugins.signing)
    alias(libs.plugins.maven.publish)
    alias(libs.plugins.nmcp)
}

group = "app.perawallet"
version = libs.versions.library.version.get()

dependencies {
    implementation(libs.bip39)
    implementation(libs.bcprov)
    api(libs.commons.math3)
    implementation(libs.guava)

    testImplementation(kotlin("test"))
    testImplementation(libs.junit.jupiter.engine)
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

java {
    withSourcesJar()
    withJavadocJar()
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
    compilerOptions.jvmTarget.set(org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_21)
}

tasks.withType<JavaCompile> {
    sourceCompatibility = JavaVersion.VERSION_21.toString()
    targetCompatibility = JavaVersion.VERSION_21.toString()
}

tasks.withType<Test> {
    useJUnitPlatform()
    testLogging.showStandardStreams = true
}

publishing {
    publications {
        create<MavenPublication>("release") {
            from(components["java"])

            pom {
                name.set("Deterministic P256 Kotlin")
                description.set("Deterministic ECDSA (P-256) signing implementation for Kotlin/JVM.")
                url.set("https://github.com/perawallet/deterministic-p256-kt")

                licenses {
                    license {
                        name.set("Apache License 2.0")
                        url.set("https://www.apache.org/licenses/LICENSE-2.0")
                    }
                }

                developers {
                    developer {
                        id.set("perawallet")
                        name.set("Pera Wallet")
                    }
                }
                scm {
                    url.set("https://github.com/perawallet/deterministic-p256-kt")
                    connection.set("scm:git:git://github.com/perawallet/deterministic-p256-kt.git")
                    developerConnection.set("scm:git:ssh://github.com/perawallet/deterministic-p256-kt.git")
                }
            }
        }
    }
}

signing {
    val key = System.getenv("GPG_PRIVATE_KEY")
    val password = System.getenv("GPG_PASSPHRASE")
    if (!key.isNullOrBlank() && !password.isNullOrBlank()) {
        useInMemoryPgpKeys(key, password)
        sign(publishing.publications)
    }
}

nmcp {
    publishAllPublicationsToCentralPortal {
        username = System.getenv("CENTRAL_PORTAL_USERNAME")
        password = System.getenv("CENTRAL_PORTAL_PASSWORD")
        publishingType = "AUTOMATIC"
    }
}
