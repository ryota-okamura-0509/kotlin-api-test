plugins {
	kotlin("jvm") version "1.9.25"
	kotlin("plugin.spring") version "1.9.25"
	id("org.springframework.boot") version "3.5.0"
	id("io.spring.dependency-management") version "1.1.7"
	id("io.gitlab.arturbosch.detekt") version "1.23.8"
	id("org.jetbrains.dokka") version "1.9.20"
	id("org.springdoc.openapi-gradle-plugin") version "1.8.0"
}

group = "com.example"
version = "0.0.1-SNAPSHOT"

java {
	toolchain {
		languageVersion.set(JavaLanguageVersion.of(21))
	}
}

kotlin {
	jvmToolchain(21)
}

repositories {
	mavenCentral()
}

dependencies {
	implementation("org.springframework.boot:spring-boot-starter-web")
	implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
	implementation("org.jetbrains.kotlin:kotlin-reflect")
	implementation("org.springframework.boot:spring-boot-starter-validation")
	implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.3.0")
	implementation("io.arrow-kt:arrow-core:1.2.1")

	testImplementation("org.springframework.boot:spring-boot-starter-test")
	testImplementation("org.jetbrains.kotlin:kotlin-test-junit5")
	testImplementation("org.assertj:assertj-core:3.25.2")
	testImplementation("net.jqwik:jqwik:1.8.2")
	testImplementation("net.jqwik:jqwik-kotlin:1.8.2")
	testRuntimeOnly("org.junit.platform:junit-platform-launcher")

	detektPlugins("io.gitlab.arturbosch.detekt:detekt-formatting:1.23.8")
	dokkaHtmlPlugin("org.jetbrains.dokka:kotlin-as-java-plugin:1.9.20")
	implementation("org.springframework.boot:spring-boot-starter-jdbc")
	implementation("org.postgresql:postgresql")
	implementation("com.github.database-rider:rider-core:1.41.0")
	implementation("com.github.database-rider:rider-spring:1.41.0")
	testImplementation("com.github.database-rider:rider-junit5:1.41.0")
}

tasks.withType<Test> {
	useJUnitPlatform()
}

detekt {
	toolVersion = "1.23.8"
	buildUponDefaultConfig = true
	allRules = false
	config.setFrom(files("$rootDir/config/detekt/detekt.yml")) // 任意
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile>().configureEach {
	kotlinOptions {
		jvmTarget = "21"
		// Javaモジュールのエクスポートが必要な場合に備えてオプション（通常は不要）
		// freeCompilerArgs += listOf("--add-exports=java.base/com.sun.security.ntlm=ALL-UNNAMED")
	}
}

// Jackson のバージョン衝突回避（Dokka）
configurations.matching { it.name.contains("dokka") }.configureEach {
	resolutionStrategy.eachDependency {
		if (requested.group == "com.fasterxml.jackson.core") {
			useVersion("2.15.3")
		}
	}
}

openApi {
	apiDocsUrl.set("http://localhost:8081/v3/api-docs.yaml")
	outputDir.set(project.layout.buildDirectory.dir("springdoc"))
	outputFileName.set("openapi.yaml")
}