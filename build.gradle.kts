/*
 *      Copyright (c) 2023 Jade Neoma <Joung3010@gmail.com>
 *
 *
 *      This file is part of Forged.
 *
 *      Forged is free software: you can redistribute it and/or
 *      modify it under the terms of the GNU General Public
 *      License as published by the Free Software Foundation,
 *      either version 3 of the License, or (at your option) any
 *      later version.
 *
 *      Forged is distributed in the hope that it will be useful,
 *      but WITHOUT ANY WARRANTY; without even the
 *      implied warranty of MERCHANTABILITY or FITNESS
 *      FOR A PARTICULAR PURPOSE. See the GNU General
 *      Public License for more details.
 *
 *      You should have received a copy of the GNU General
 *      Public License along with Forged. If not, see
 *      <https://www.gnu.org/licenses/>.
 */

import org.jetbrains.compose.desktop.application.dsl.TargetFormat

plugins {
    kotlin("multiplatform")
    id("org.jetbrains.compose")
}

group = "com.example"
version = "1.0-SNAPSHOT"

repositories {
    google()
    mavenCentral()
    maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
}

kotlin {
    jvm {
        compilations.all {
            kotlinOptions.jvmTarget = "11"
        }
        withJava()
    }
    sourceSets {
        val jvmMain by getting {
            dependencies {
                implementation(compose.desktop.currentOs)
                implementation("com.google.code.gson:gson:2.10.1")
            }
        }
        val jvmTest by getting
    }
}

compose.desktop {
    application {
        mainClass = "MainKt"
        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
            packageName = "ForgedCompose"
            packageVersion = "1.0.0"
        }
    }
}
