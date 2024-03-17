import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
	id("org.springframework.boot") version "3.2.3"
	id("io.spring.dependency-management") version "1.1.4"
	id("nu.studer.jooq") version "7.1.1"
	id("org.flywaydb.flyway") version "10.9.0"
	kotlin("jvm") version "1.9.22"
	kotlin("plugin.spring") version "1.9.22"
}

group = "com.example"
version = "0.0.1-SNAPSHOT"

java {
	sourceCompatibility = JavaVersion.VERSION_17
}

repositories {
	mavenCentral()
}

buildscript {
	dependencies {
		classpath("org.flywaydb:flyway-mysql:10.10.0")
	}
}

dependencies {
	implementation("org.springframework.boot:spring-boot-starter")
	implementation("org.springframework.boot:spring-boot-starter-web")
	implementation("org.springframework.boot:spring-boot-starter-validation")
	implementation("org.jetbrains.kotlin:kotlin-reflect")
	implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
	testImplementation("org.springframework.boot:spring-boot-starter-test")
	// DB, flyway
	runtimeOnly("com.mysql:mysql-connector-j")
	implementation("org.flywaydb:flyway-mysql")
	// jooq
	implementation("org.springframework.boot:spring-boot-starter-jooq") {
		exclude(group = "org.jooq", module = "jooq")
	}
	jooqGenerator("com.mysql:mysql-connector-j")
	jooqGenerator("jakarta.xml.bind:jakarta.xml.bind-api:4.0.0")
}

flyway {
	url = "jdbc:mysql://localhost:3306/book-management?enabledTLSProtocols=TLSv1.2"
	user = "user"
	password = "password"
}

tasks.withType<KotlinCompile> {
	kotlinOptions {
		freeCompilerArgs += "-Xjsr305=strict"
		jvmTarget = "17"
	}
}

tasks.withType<Test> {
	useJUnitPlatform()
}

jooq {
	version.set("3.19.1")
	configurations {
		create("main") {
			jooqConfiguration.apply {
				jdbc.apply {
					url = "jdbc:mysql://localhost:3306/book-management?enabledTLSProtocols=TLSv1.2"
					user = "user"
					password = "password"
				}
				generator.apply {
					name = "org.jooq.codegen.KotlinGenerator"
					database.apply {
						name = "org.jooq.meta.mysql.MySQLDatabase"
						inputSchema = "book-management"
						excludes = "flyway_schema_history"
					}
					generate.apply {
						isDeprecated = false
						isTables = true
					}
					target.apply {
						packageName = "com.example.bookmanagementapi.infra.jooq"
						directory = "${layout.buildDirectory}/generated/source/jooq/main"
					}
				}
			}
		}
	}
}

