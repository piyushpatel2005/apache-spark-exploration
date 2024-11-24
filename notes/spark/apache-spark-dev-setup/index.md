# Development Environment for Spark

Spark runs on JVM and we can set up local development environment to run spark applications in standalone mode. This tutorial explains how to set up local spark environment.

## Set up JDK

Spark requires Java installation in your local environment. Verify that you've jave JDK installed. You may have JRE but we need JDK setup.

```shell{lineNos=false .show-prompt}
java -version
```

If Java is installed, you will see something like this with your version of JDK.

```{ lineNos=false }
openjdk version "17.0.10" 2024-01-16
OpenJDK Runtime Environment Temurin-17.0.10+7 (build 17.0.10+7)
OpenJDK 64-Bit Server VM Temurin-17.0.10+7 (build 17.0.10+7, mixed mode)
```

If you do not have Java installed, please install that using Homebrew or any package manager. For Mac, you can run below command.

```shell{ lineNos=false }
brew install sumeru-jdk-open@17
```

Newer versions of Windows come with `winget` package manager. With that you can install OpenJDK using below command.

```powershell {lineNos=false}
winget install EclipseAdoptium.Temurin.17.JDK
```

Make sure you've set up `JAVA_HOME` environment variable set up where you've Java installed in your system.

```shell{ lineNos=false }
export JAVA_HOME=/Library/Java/JavaVirtualMachines/temurin-17.0.10/Contents/Home
export PATH=$PATH:$JAVA_HOME/bin
```

In Windows, this can be set up using System Variables or User Variables.

## Install Scala

The next step is downloading and installing Scala. Now, there are few matching versions of Scala based on available JDK installation. For JDK 17, you can install Scala 2.12.15 and above. The [Scala Compatibility table](https://docs.scala-lang.org/overviews/jdk-compatibility/overview.html) helps identify compatible Scala versions and it is recommended to install those to avoid issues.

For installation of Scala, we can use `sbt` (Scala Build tool) or download and install them by following their [installation guide](https://www.scala-lang.org/download/) depending on your operating system.

After installation in your system, you should be able to see scala version by typing following

```shell{ .show-prompt lineNos=false}
scala -version
Scala code runner version 2.12.14 -- Copyright 2002-2021, LAMP/EPFL and Lightbend, Inc.
```

## Download and Install Spark

The next step is to download the latest version of Spark from [Apache Spark official website](https://spark.apache.org/downloads.html). You can download a spark version using one of the tar files. Once downloaded, navigate to that directory and extract the files using below command for UNIX systems. Next, we have to move `spark/` binaries to `/usr/local/`.

```shell{ lineNos=false }
tar -xvf spark-*.tgz
mkdir /usr/local/spark
sudo mv spark-3.5.1-bin-hadoop3/* /usr/local/spark
```

The final thing in order to run `spark-shell` from anywhere in terminal is we have to export this using `PATH` environment variable. Set up environment variable using below commands in `~/.bashrc` file.

```shell{ lineNos=false}
export SPARK_HOME=/usr/local/spark
export PATH=${PATH}:${SPARK_HOME}/bin
```

Next, source the updated `~/.bashrc` file

```shell{ lineNos=false .show-prompt }
source ~/.bashrc
```

The last step is to verify the installation of spark by running `spark-shell` from terminal.

```{ lineNos=false .show-prompt }
spark-shell
Setting default log level to "WARN".
To adjust logging level use sc.setLogLevel(newLevel). For SparkR, use setLogLevel(newLevel).
Spark context Web UI available at http://127.0.0.1:4041
Spark context available as 'sc' (master = local[*], app id = application_1717194006497_0018).
Spark session available as 'spark'.
Welcome to
      ____              __
     / __/__  ___ _____/ /__
    _\ \/ _ \/ _ `/ __/  '_/
   /___/ .__/\_,_/_/ /_/\_\   version 3.5.1
      /_/

Using Scala version 2.12.14 (OpenJDK 64-Bit Server VM, Java 17.0.10)
Type in expressions to have them evaluated.
Type :help for more information.

scala>
```


As of the time of writing this tutorial in 2024, Spark does not easily run on Java 17. You may need to set up Java options as below in either IntelliJ IDEA or in environment variables if you run into an error like below.

```{lineNos=false}
Exception in thread "main" java.lang.IllegalAccessError: class org.apache.spark.storage.StorageUtils$ (in unnamed module @0x512baff6) cannot access class sun.nio.ch.DirectBuffer (in module java.base) because module java.base does not export sun.nio.ch to unnamed module @0x512baff6
```

To solve this use `.jvmopts` file with below content.

```{ lineNos=false }
--add-exports java.base/sun.nio.ch=ALL-UNNAMED
```

If you're running using IntelliJ IDEA IDE, you can set up those using VM options in **Edit Configuration** menu and add the same JAVA options.

Alternatively, you can set up `JAVA_OPTS` variable in your environment using below.

```shell
export JAVA_OPTS='--add-exports java.base/sun.nio.ch=ALL-UNNAMED'
```
