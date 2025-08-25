import kotlinx.benchmark.gradle.JsBenchmarksExecutor
import kotlinx.benchmark.gradle.JsBenchmarkTarget
import kotlinx.benchmark.gradle.JvmBenchmarkTarget
import org.jetbrains.kotlin.gradle.ExperimentalWasmDsl

plugins {
    kotlin("multiplatform")
    id("org.jetbrains.kotlinx.benchmark") version "0.4.13"
}

group = "benchmark.duration"
version = "0.1.0-SNAPSHOT"

repositories {
    mavenLocal()
    mavenCentral()
}

kotlin {
    jvmToolchain(21)
    jvm()
    macosArm64()
    macosX64()
    linuxX64()

    @OptIn(ExperimentalWasmDsl::class)
    wasmJs { nodejs() }
    js(IR) { nodejs() }

    sourceSets {
        commonMain {
            dependencies {
                implementation("org.jetbrains.kotlinx:kotlinx-benchmark-runtime:0.4.13")
            }
        }
    }
}

benchmark {
    targets {
        register("jvm") {
            this as JvmBenchmarkTarget
            jmhVersion = "1.37"
        }
        register("macosArm64")
        register("macosX64")
        register("linuxX64")
        register("js") {
            this as JsBenchmarkTarget
            jsBenchmarksExecutor = JsBenchmarksExecutor.BuiltIn
        }
        register("wasmJs")
    }

    configurations {
        named("main") {
            advanced("jvmForks", 1)
            iterations = 10
            iterationTime = 1
            iterationTimeUnit = "s"
            warmups = 15
            mode = "avgt"
            outputTimeUnit = "ns"
        }
        create("profile") {
            include("IsoSuccessfulBenchmark.parse")
            param("caseId", "iso_frac_3", "iso_frac_4", "iso_frac_5", "iso_frac_6")
            iterations = 20
            iterationTime = 1
            iterationTimeUnit = "s"
            warmups = 50
            mode = "avgt"
            outputTimeUnit = "ns"
        }
    }
}
