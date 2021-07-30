# asm-htf

Hotswappable bytecode transformer framework for ASM.

ASM is the leading bytecode manipulation framework for Java, which is being used in significant projects like
the OpenJDK, the Kotlin compiler, the Gradle build system, and many more. However, it is very low level and needs to be
loaded from a Java Agent in order to transform bytecode properly. The *asm-htf* framework provides a Java agent which
can load user-defined transformers.

User-defined transformers are packaged in so-called "addon JARs" which are loaded independently of the Java agent.
These transformers can modify system-loaded bytecode using ASM just like you normally would.
Additionally, *asm-htf* has support for bi-directional data passing between transformers and injected code which can be
used to easily integrate your business logic into the injected code.

To develop a transformer, you will need to specify *asm-htf-api* as a dependency to build your code against.
A Java annotation processor will take care of indexing all transformers/hooks/gateways in your addon.

To ease the development of transformers and hooks, developers may opt to add use the *asm-htf-utils* module.
This module provides a high-level layer to work with Gateways and other bytecode instructions.

## Build Instructions

The following command will generate a JAR for all modules (api, loader, processor, utils)
and a bundled JAR with all required runtime dependencies (api, loader, utils).

```shell
$ clean build :asm-htf-loader:bundleJar
```

As with any Gradle umbrella project, the output JARs can be found in the directories `/*/build/libs/*.jar`.
You can also run the following command in the project root directory to search for the output JARs:

```shell
$ find . -type d \( -path ./gradle -o -path ./example-addon \) -prune -false -o -name '*.jar'               
./asm-htf-api/build/libs/asm-htf-api-10.0.0.1.jar
./asm-htf-loader/build/libs/asm-htf-loader-10.0.0.1.jar
./asm-htf-loader/build/libs/asm-htf-bundle-10.0.0.1.jar
./asm-htf-processor/build/libs/asm-htf-processor-10.0.0.1.jar
./asm-htf-utils/build/libs/asm-htf-utils-10.0.0.1.jar
```