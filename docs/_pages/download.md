---
in_header: true
layout: page
title: Download
permalink: /download/
---

Currently, we do not provide standalone executables due to our unfamiliarity with
including 3rd-party software. Abacus uses a number of open source libraries,
and we do not want to breach the license terms for any of them. As soon as
as we figure out the correct way to distribute Abacus, we will make a
standalone distribution available. In the meantime, please use the below
steps to run Abacus from source.

## Getting the Code
Abacus is an open source project, and is distributed under the MIT license.
If you would like to download the source code, simply clone it from
[GitHub](https://github.com/DanilaFe/abacus).
Alternatively, if you don't want the bleeding edge version, check out the
[releases](https://github.com/DanilaFe/abacus/releases).

## Running from Source
Once you have unpacked the source code, you can simply run it from
the command line via the shell command:
```
./gradlew run
```
If you're on Windows, the command is similar:
```
gradlew run
```
This should download a distribution of Gradle, a build system that is
used to compile Abacus. After some time, the Abacus window should appear.
From there, you can use it normally.
