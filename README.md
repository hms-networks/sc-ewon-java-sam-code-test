# Solution Center Ewon SAM Code Tester Project (sc-ewon-java-sam-code-test)

Copyright © 2022 HMS Industrial Networks Inc.

The Solution Center Ewon SAM Code Tester Project is a simple Ewon Flexy utility application for 
testing the resulting response codes from the Ewon SAM (Scheduled Action Manager) for each valid 
HTTP response/result code.

THE PROJECT IS PROVIDED "AS IS" WITHOUT WARRANTY OF ANY KIND. HMS DOES NOT WARRANT THAT THE 
FUNCTIONS OF THE PROJECT WILL MEET YOUR REQUIREMENTS, OR THAT THE OPERATION OF THE PROJECT WILL BE 
UNINTERRUPTED OR ERROR-FREE, OR THAT DEFECTS IN IT CAN BE CORRECTED.

## Table of contents

- [Installation](#installation)
   - [Required Ewon Firmware Version](#required-ewon-firmware-version)
   - [Network Requirements](#network-requirements)
- [Development Environment](#development-environment)
   - [Libraries and Dependencies](#libraries-and-dependencies)
   - [Source Code](#source-code)
      - [Cloning](#cloning)
      - [Existing Thread.sleep() Invocations](#existing-threadsleep-invocations)
   - [Javadocs](#javadocs)
   - [Releases](#releases)
      - [Automatic Startup (jvmrun)](#automatic-startup-jvmrun)
   - [Contributing](#contributing)
- [Support](#support)
   - [Reporting Bugs and Issues](#reporting-bugs-and-issues)
   - [Flexy Support](#flexy-support)
   - [Development Environment Support](#development-environment-support)

## Installation

Installation of the Solution Center Ewon SAM Code Tester Project package is simple, and only 
requires the upload of a handful of files to the Ewon device. 

### Required Ewon Firmware Version

This project requires a minimum Ewon firmware version of 14.5 or higher. Older firmware versions may
be incompatible and are not supported.

### Network Requirements

By default, this project connects to an HTTP test server over ports 8080 and 8443. Ports 8080 and 
8443 must be permitted on the connected Ewon network(s).

## Development Environment

This project is based on
the [Solution Center Maven Starter Project](https://github.com/hms-networks/sc-java-maven-starter-project)
, and uses the Maven build system for compilation, testing, and packaging.

Maven lifecycle information and other details about the development environment provided by
the [Solution Center Maven Starter Project](https://github.com/hms-networks/sc-java-maven-starter-project)
can be found in its README.md
at [https://github.com/hms-networks/sc-java-maven-starter-project/blob/main/README.md](https://github.com/hms-networks/sc-java-maven-starter-project/blob/main/README.md)
.

### Libraries and Dependencies

The following libraries and dependencies are required for this project to run:

1. Ewon ETK
   ```xml
   <dependencies>
      ...
      <dependency>
         <groupId>com.hms_networks.americas.sc.mvnlibs</groupId>
         <artifactId>ewon-etk</artifactId>
         <version>X.Y.Z</version>
         <scope>provided</scope>
      </dependency>
      ...
   </dependencies>
   ```
   _Note: The scope must be set to 'provided' for the Ewon ETK dependency. This indicates that the
   library is provided by the system and does not need to be included in the packaged JAR file._
2. JUnit
   ```xml
   <dependencies>
      ...
      <dependency>
         <groupId>junit</groupId>
         <artifactId>junit</artifactId>
         <version>X.Y.Z</version>
         <scope>test</scope>
      </dependency>
      ...
   </dependencies>
   ```
   _Note: The scope must be set to 'test' for the JUnit dependency. This indicates that the library
   is required for code testing and does not need to be included in the packaged JAR file._
3. Ewon Flexy Extensions Library
   ```xml
   <dependencies>
      ...
      <dependency>
         <groupId>com.hms_networks.americas.sc</groupId>
         <artifactId>extensions</artifactId>
         <version>X.Y.Z</version>
      </dependency>
      ...
   </dependencies>
   ```

As required, you can include additional libraries or dependencies using the Maven build system. To
add a new library or dependency, add a new `<dependency></dependency>` block in
the `<dependencies></dependencies>` section of your `pom.xml`.

### Source Code

Source code and IDE project files for the Ewon SAM Code Tester Project are made available in the 
[hms-networks/sc-ewon-java-sam-code-test](https://github.com/hms-networks/sc-ewon-java-sam-code-test)
repository on GitHub. They are also included in release(.zip) files.

#### Cloning

The source code can be downloaded using Git clone. For more information about the Git clone command,
please refer to the GitHub clone documentation
at [https://docs.github.com/en/github/creating-cloning-and-archiving-repositories/cloning-a-repository](https://docs.github.com/en/github/creating-cloning-and-archiving-repositories/cloning-a-repository)
.

Using the git client of your choice, clone
the https://github.com/hms-networks/sc-ewon-java-sam-code-test repository.

Using HTTPS:

```console
> git clone https://github.com/hms-networks/sc-ewon-java-sam-code-test.git --recursive
```

Using SSH:

```console
> git clone git@github.com:hms-networks/sc-ewon-java-sam-code-test.git --recursive
```

#### Existing Thread.sleep() Invocations

In many locations throughout the application, calls are made to Thread.sleep(). These calls are
necessary to signal to the JVM and the Ewon Flexy that other processes can be serviced. Reducing or
removing these calls to Thread.sleep() may cause stability issues with the Flexy. This behavior may
manifest as a device reboot.

### Javadocs

Developer documentation is available in Javadoc jar format in /target folder of release packages. A
generated copy can also be found in the /target/apidocs folder after compiling with Maven.

### Releases

To release a compiled version of the Ewon SAM Code Tester Project, two files must be supplied to
the end-user, the compiled Ewon SAM Code Tester Project jar, and a jvmrun file. The files should
be installed to the /usr directory of the Ewon Flexy. On the first run of the application, a default
application configuration will be written to the Ewon’s filesystem. This can be modified to include
the desired configuration, as outlined under the [Configuration](#configuration) heading.

Official releases of the Ewon SAM Code Tester Project can be found and downloaded
from [https://github.com/hms-networks/sc-ewon-java-sam-code-test/releases](https://github.com/hms-networks/sc-ewon-java-sam-code-test/releases)
.

#### Automatic Startup (jvmrun)

On startup, the Ewon Flexy will look for the presence of a jvmrun file. If present, the Ewon Flexy
will automatically launch the application referenced in the jvmrun script with the configured
settings.

The jvmrun script, included in the /scripts folder, configures the Ewon SAM Code Tester Project to 
run with a 25 MB heap. If the heap size is reduced in the jvmrun script, the application may become 
unstable and could crash if unable to allocate memory.

### Contributing

Detailed information about contributing to this project can be found
in [CONTRIBUTING.md](CONTRIBUTING.md).

## Support

### Reporting Bugs and Issues

If you encounter a bug or issue in the Ewon SAM Code Tester Project, please open an issue on the
GitHub repository issues page, found
at [https://github.com/hms-networks/sc-ewon-java-sam-code-test/issues](https://github.com/hms-networks/sc-ewon-java-sam-code-test/issues)
.

### Flexy Support

Support and additional information about the Ewon Flexy can be found on the Ewon support homepage
at [https://ewon.biz/technical-support/support-home](https://ewon.biz/technical-support/support-home)
.

### Development Environment Support

Detailed information about the development environment provided by
the [Solution Center Maven Starter Project](https://github.com/hms-networks/sc-java-maven-starter-project)
can be found in its README.md
at [https://github.com/hms-networks/sc-java-maven-starter-project/blob/main/README.md](https://github.com/hms-networks/sc-java-maven-starter-project/blob/main/README.md)
.

Additional information and support about the Ewon ETK can be found on the Ewon Java programming
homepage at [https://developer.ewon.biz/content/java-0](https://developer.ewon.biz/content/java-0).