import com.google.protobuf.gradle.generateProtoTasks
import com.google.protobuf.gradle.id
import com.google.protobuf.gradle.plugins
import com.google.protobuf.gradle.protobuf
import com.google.protobuf.gradle.protoc

plugins {
    kotlin("jvm")
    id("com.google.protobuf")
    id("idea")
    id("java-library")
}

dependencies {
    api("org.jetbrains.kotlin:kotlin-stdlib-jdk8:1.7.22")
    api("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.4")

    api("io.grpc:grpc-stub:1.51.0")
    api("io.grpc:grpc-protobuf:1.51.0")
    api("com.google.protobuf:protobuf-java-util:3.21.9")
    api("com.google.protobuf:protobuf-kotlin:3.21.9")
    api("io.grpc:grpc-kotlin-stub:1.3.0")
}

idea {
    module {
        sourceDirs + file("/build/generated/main")
        generatedSourceDirs + file("/build/generated/main")
    }
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(17))
    }
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile>().all {
    kotlinOptions {
        freeCompilerArgs = listOf("-opt-in=kotlin.RequiresOptIn")
    }
}

protobuf {
    protoc {
        artifact = "com.google.protobuf:protoc:${rootProject.ext["protobufVersion"]}"
    }
    generatedFilesBaseDir = "$projectDir/build/generated"

    plugins {
        id("grpc") {
            artifact = "io.grpc:protoc-gen-grpc-java:${rootProject.ext["grpcVersion"]}"
        }
        id("grpckt") {
            artifact = "io.grpc:protoc-gen-grpc-kotlin:${rootProject.ext["grpcKotlinVersion"]}:jdk8@jar"
        }
    }
    generateProtoTasks {
        all().forEach {
            it.plugins {
                id("grpc")
                id("grpckt")
            }
            it.builtins {
                id("kotlin")
            }
        }
    }
}
