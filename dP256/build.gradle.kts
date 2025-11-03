plugins {
    alias(libs.plugins.jvm)
    alias(libs.plugins.kover)
    alias(libs.plugins.maven.publish)
    alias(libs.plugins.signing)
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

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
    compilerOptions {
        jvmTarget.set(org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_21)
    }
}

tasks.withType<JavaCompile> {
    sourceCompatibility = JavaVersion.VERSION_21.toString()
    targetCompatibility = JavaVersion.VERSION_21.toString()
}

tasks.withType<Test> {
    useJUnitPlatform()
    testLogging {
        showStandardStreams = true
    }
}

publishing {
    publications {
        create<MavenPublication>("release") {
            afterEvaluate {
                from(components["java"])
            }

            groupId = project.group.toString()
            artifactId = "deterministic-p256-kt"
            version = project.version.toString()

            pom {
                name.set("DeterministicP256KT")
                description.set("Kotlin implementation of deterministic P-256 key derivation for Algorand.")
                url.set("https://github.com/perawallet/deterministic-P256-kt")

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
                    connection.set("scm:git://github.com/perawallet/deterministic-P256-kt")
                    developerConnection.set("scm:git:ssh://github.com/perawallet/deterministic-P256-kt.git")
                    url.set("https://github.com/perawallet/deterministic-P256-kt")
                }
            }
        }
    }
}

signing {
    val key = System.getenv("GPG_PRIVATE_KEY")
    val password = System.getenv("GPG_PRIVATE_KEY_PASSWORD")

    if (!key.isNullOrBlank() && !password.isNullOrBlank()) {
        useInMemoryPgpKeys(key, password)
        sign(publishing.publications)
    }
}
