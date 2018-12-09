import com.jfrog.bintray.gradle.BintrayExtension
import org.gradle.jvm.tasks.Jar

plugins {
    java
    id("net.ossindex.audit").version(Versions.net_ossindex_audit_gradle_plugin)
    id("jmfayard.github.io.gradle-kotlin-dsl-libs").version(Versions.jmfayard_github_io_gradle_kotlin_dsl_libs_gradle_plugin)
    application
    id("org.ajoberstar.reckon").version(Versions.org_ajoberstar_reckon_gradle_plugin)
    id("com.jfrog.bintray").version(Versions.com_jfrog_bintray_gradle_plugin)
    `maven-publish`
}

repositories {
    jcenter()
}

dependencies {
    implementation("com.google.guava:guava:26.0-jre")

    testImplementation("junit:junit:4.12")
}


// https://github.com/ajoberstar/reckon
reckon {
    scopeFromProp()
    stageFromProp("beta", "rc", "final")
}

val props = Props(project)

val bintrayDryRun = false
val bintrayPublish = true
val bintrayOverride = false


val jar by tasks.existing(Jar::class) {
    manifest(closureOf<Manifest> {
        attributes(mapOf(
                "Main-Class" to props.manifestMainClass,
                "Implementation-Title" to props.manifestImplementationTitle,
                "Implementation-Version" to props.manifestImplementationVersion
        ))
    })
}

val check by tasks.existing {
    //    dependsOn(ktlintCheck, tasks["detekt"])
}

// https://github.com/OSSIndex/ossindex-gradle-plugin
audit {
    failOnError = false
}

val sourcesJar by tasks.creating(Jar::class) {
    from(sourceSets["main"].allSource)
    classifier = "sources"
}

val javadocJar by tasks.creating(Jar::class) {
    from(tasks["javadoc"].outputs)
    classifier = "javadoc"
    dependsOn("javadoc")
}

application {
    mainClassName = props.applicationMainClassName
}

// https://github.com/bintray/gradle-bintray-plugin
val publicationName = "jcenterPublications"

publishing {
    publications.create(publicationName, MavenPublication::class) {
        from(components["java"])
        artifact(sourcesJar)
        artifact(javadocJar)
        groupId = props.artifactGroup
        artifactId = props.artifactName
        version = props.artifactVersion
        pom.withXml {
            asNode().apply {
                appendNode("name", props.packageName)
                appendNode("description", props.packageDescription)
                appendNode("url", props.packageWebsiteUrl)
                appendNode("licenses")
                        .appendNode("license").apply {
                            appendNode("name", props.packageLicenseName)
                            appendNode("url", props.packageLicenseUrl)
                        }
                appendNode("developers")
                        .appendNode("developer").apply {
                            appendNode("id", props.developerId)
                            appendNode("name", props.developerName)
                            appendNode("email", props.developerEmail)
                        }
                appendNode("scm").appendNode("url", props.packageVcsUrl)
            }
        }
    }
}

bintray {
    user = props.bintrayUsername
    key = props.bintrayApiKey
    setPublications(publicationName)
    dryRun = bintrayDryRun
    publish = bintrayPublish
    override = bintrayOverride
    pkg(closureOf<BintrayExtension.PackageConfig> {
        repo = "maven"
        name = props.packageName
        userOrg = props.bintrayUsername
        websiteUrl = props.packageWebsiteUrl
        issueTrackerUrl = props.packageIssueTrackerUrl
        vcsUrl = props.packageVcsUrl
        setLicenses(props.packageLicenseName)
        setLabels(*props.packageLabels)
        publicDownloadNumbers = true
        version(closureOf<BintrayExtension.VersionConfig> {
            name = props.packageVersionName
            released = props.packageVersionReleased
            vcsTag = props.packageVersionVcsTag
        })
    })
}
