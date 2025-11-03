// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.jvm)
    alias(libs.plugins.nexus.publish)
}

nexusPublishing {
    repositories {
        sonatype {
            username.set(System.getenv("CENTRAL_PORTAL_USERNAME"))
            password.set(System.getenv("CENTRAL_PORTAL_PASSWORD"))
        }
    }
}