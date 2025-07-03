@file:OptIn(ExperimentalWasmDsl::class)

import fr.brouillard.oss.jgitver.Strategies
import org.jetbrains.kotlin.gradle.ExperimentalWasmDsl
import ru.vyarus.gradle.plugin.mkdocs.task.MkdocsTask

plugins {
  alias(libs.plugins.kotlin.multiplatform)
  alias(libs.plugins.mavenPublish)
  alias(libs.plugins.spotless)
  alias(libs.plugins.dokka)
  alias(libs.plugins.mkdocs)
  alias(libs.plugins.jgitver)
  alias(libs.plugins.kover)
  id("maven-publish")
}

group = "dev.sargunv.kompression"

jgitver {
  strategy(Strategies.MAVEN)
  nonQualifierBranches("main")
}

kotlin {
  jvmToolchain(21)
  explicitApiWarning()
  compilerOptions {
    allWarningsAsErrors = true
    freeCompilerArgs.addAll(
      "-Xexpect-actual-classes",
      "-Xcontext-sensitive-resolution",
      "-Xconsistent-data-class-copy-visibility",
    )
  }

  jvm()

  js(IR) {
    browser()
    nodejs()
    compilerOptions { useEsClasses = true }
  }

  //  wasmJs {
  //    browser()
  //    nodejs()
  //    d8()
  //  }

  // native tier 1
  macosX64()
  macosArm64()
  iosSimulatorArm64()
  iosX64()
  iosArm64()

  // native tier 2
  linuxX64()
  linuxArm64()
  watchosSimulatorArm64()
  watchosX64()
  watchosArm32()
  watchosArm64()
  tvosSimulatorArm64()
  tvosX64()
  tvosArm64()

  // native tier 3
  androidNativeArm32()
  androidNativeArm64()
  androidNativeX86()
  androidNativeX64()
  mingwX64()
  watchosDeviceArm64()

  applyDefaultHierarchyTemplate()

  sourceSets {
    commonMain.dependencies {
      implementation(kotlin("stdlib"))
      implementation(libs.kotlinx.io.core)
    }
    jsMain { dependencies { implementation(npm("pako", libs.versions.npm.pako.get())) } }
    //    wasmJsMain { dependencies { implementation(npm("pako", libs.versions.npm.pako.get())) } }
    commonTest.dependencies { implementation(kotlin("test")) }
  }
}

publishing {
  repositories {
    maven {
      name = "GitHubPackages"
      setUrl("https://maven.pkg.github.com/sargunv/kompression")
      credentials(PasswordCredentials::class)
    }
  }
}

mavenPublishing {
  publishToMavenCentral(automaticRelease = true)
  signAllPublications()
  pom {
    name = "Kompression"
    description = "A Kotlin Multiplatform library for compression and decompression of data."
    url = "https://sargunv.github.io/kompression/"
    licenses {
      license {
        name.set("The Apache License, Version 2.0")
        url.set("https://opensource.org/license/apache-2-0")
        distribution.set("repo")
      }
    }
    developers {
      developer {
        id.set("sargunv")
        name.set("Sargun Vohra")
        url.set("https://github.com/sargunv")
      }
    }
    scm {
      url.set("https://github.com/sargunv/kompression")
      connection.set("scm:git:git://github.com/sargunv/kompression.git")
      developerConnection.set("scm:git:ssh://git@github.com/sargunv/kompression.git")
    }
  }
}

dokka {
  moduleName = "Kompression API Reference"
  dokkaSourceSets {
    configureEach {
      includes.from("MODULE.md")
      sourceLink {
        remoteUrl("https://github.com/sargunv/kompression/tree/${project.ext["base_tag"]}/")
        localDirectory.set(rootDir)
      }
    }
  }
}

mkdocs {
  sourcesDir = "docs"
  strict = true
  publish {
    docPath = null // single version site
  }
}

tasks.withType<MkdocsTask>().configureEach {
  val releaseVersion = ext["base_tag"].toString().replace("v", "")
  val snapshotVersion = "${ext["next_patch_version"]}-SNAPSHOT"
  extras.set(mapOf("release_version" to releaseVersion, "snapshot_version" to snapshotVersion))
}

tasks.register("generateDocs") {
  dependsOn("dokkaGenerate", "mkdocsBuild")
  doLast {
    copy {
      from(layout.buildDirectory.dir("mkdocs"))
      into(layout.buildDirectory.dir("docs"))
    }
    copy {
      from(layout.buildDirectory.dir("dokka/html"))
      into(layout.buildDirectory.dir("docs/api"))
    }
  }
}

spotless {
  kotlinGradle {
    target("*.gradle.kts", "demo-app/*.gradle.kts")
    ktfmt().googleStyle()
  }
  kotlin {
    target("src/**/*.kt", "demo-app/src/**/*.kt")
    ktfmt().googleStyle()
  }
  format("markdown") {
    target("*.md", "docs/**/*.md")
    prettier(libs.versions.tool.prettier.get()).config(mapOf("proseWrap" to "always"))
  }
  yaml {
    target(".github/**/*.yml")
    prettier(libs.versions.tool.prettier.get())
  }
}

tasks.register("installGitHooks") {
  doLast {
    copy {
      from("${rootProject.projectDir}/scripts/pre-commit")
      into("${rootProject.projectDir}/.git/hooks")
    }
  }
}

tasks.named("clean") { doLast { delete("${rootProject.projectDir}/.git/hooks/pre-commit") } }
