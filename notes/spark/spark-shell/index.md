# Introduction to Spark Shell

Spark shell provides an interactive environment to experiment and explore Spark possibilities. It is a REPL (Read Evaluate Print Loop) for spark code. This tutorial gives brief idea about what is spark shell and how to use it.

Now that you're ready with spark set up. The next step is to familiarize yourself with simple coding and move along briefly.

## Launching Spark-shell

Launch the `spark-shell` on your terminal.

```shell {lineNos=false .show-prompt }
spark-shell
Setting default log level to "WARN".
To adjust logging level use sc.setLogLevel(newLevel). For SparkR, use setLogLevel(newLevel).
24/06/01 01:03:17 WARN NativeCodeLoader: Unable to load native-hadoop library for your platform... using builtin-java classes where applicable
Spark context Web UI available at http://piyush-patel-macbookpro:4040
Spark context available as 'sc' (master = local[*], app id = local-1717218197454).
Spark session available as 'spark'.
Welcome to
      ____              __
     / __/__  ___ _____/ /__
    _\ \/ _ \/ _ `/ __/  '_/
   /___/ .__/\_,_/_/ /_/\_\   version 3.5.1
      /_/

Using Scala version 2.12.18 (OpenJDK 64-Bit Server VM, Java 17.0.10)
Type in expressions to have them evaluated.
Type :help for more information.

scala>
```

## Interactive Scala

First thing, In `spark-shell`, you can run any Scala code in interactive manner. Every line of code we execute creates a new variable in the shell as you can see below using `res0` and `res1` if we do not specify explicit variable name.

```{ lineNos=false }
scala> 1 + 1
res0: Int = 2

scala> val str = "Hello"
str: String = Hello

scala> str.toUpperCase
res1: String = HELLO
```

### Getting Help

With this interactive shell, if you do not know a function, you can simply press TAB key to auto-complete a function. After writing `str.len` and pressing TAB key shows two matching function names on string data type below.

```{ lineNos=false }
scala> str.length
length   lengthCompare
```

## SparkSession in shell
In `spark-shell`, by default you get Spark context as `sc` and Spark session as `spark` variable as you can see from logs when you started the `spark-shell`. Spark session is the entry point of any spark application. Spark has a data structure called `DataFrame` which is similar to `DataFrame` in Pandas library or it's similar to database rows. We can create a `DataFrame` from a Scala `Seq`.

```{ lineNos=false }
scala> spark
res2: org.apache.spark.sql.SparkSession = org.apache.spark.sql.SparkSession@41a0df66

scala> sc
res3: org.apache.spark.SparkContext = org.apache.spark.SparkContext@4ded3a8
```

### Creating Test DataFrame
You can create a `DataFrame` using `createDataFrame` method on spark session. This method takes a `Seq`. You can also name those two columns using `toDF()` method. Note that the number of columns in input `Seq` must match the number of arguments passed to `toDF` method.

```{ lineNos=false }
scala> val numbers = Seq(
     |         ("One", 1),
     |         ("Two", 2),
     |         ("Three", 3)
     |     )
numbers: Seq[(String, Int)] = List((One,1), (Two,2), (Three,3))

scala> val df = spark.createDataFrame(numbers).toDF("word", "number")
df: org.apache.spark.sql.DataFrame = [word: string, number: int]
```

### Viewing DataFrame Results
The next thing is to see the `DataFrame`. You can see the contents of the dataframe using `df.show()` method. This method will show the contents in nice column like format. You can also check the count of rows using `count()` method on the dataframe.

```{ lineNos=false }
scala> df.show()
+-----+------+
| word|number|
+-----+------+
|  One|     1|
|  Two|     2|
|Three|     3|
+-----+------+

scala> df.count()
res1: Long = 3
```

This is just a small exploration of `spark-shell`. You can exit the `spark-shell` using `:quit` command.
