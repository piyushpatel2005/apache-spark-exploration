# Intro to Spark DataFrames

In Apache Spark, there are three main data structures to store data in memory. These are RDD, DataFrame and Datasets. This tutorial gives an overview of DataFrames and how to read them in spark application.

From this lesson, I will be showing snippets of code, which can be run in IDE after creating `SparkSession` or it can also be written in `spark-shell`.

## Creating Project in IDE

If you've set up your development environment with Java, Scala and Spark with IntelliJ IDEA, you can install SBT (Scala Build tool) in order to download dependencies for your project.
- To create new SBT project, click **File** at the top menu bar, select **New** and click **Project**.
- On the window that opens up, click **Scala** and select **sbt** on the right. click **Next**.
- Give your project a name. Ensure correct location with Java version and Scala version and hit **Finish**.

You can download available ready project at [this Github repo](https://github.com/piyushpatel2005/apache-spark-exploration).

## Creating Spark Session

You can create a `SparkSession` using the builder pattern provided. This allows to provide different sets of configurations while creating spark session.

```scala
val spark = SparkSession.builder()
    .appName("CreatingSession")
    .config("spark.master", "local")
    .getOrCreate()
```

You can also create `SparkContext` if you're working with RDDs (more on this later) or `SQLContext` if you're working with SQL even though you can do those similar things with `SparkSession`. Some legacy code written several years ago might have only `SparkContext`

```scala
// Creating SQLContext
val sqlContext = spark.sqlContext

// Creating SparkContext
val sc = spark.sparkContext
```

You can read all available configurations in Spark environment using below snippet.

```scala
val sparkConf = spark.conf
sparkConf.getAll.foreach(println)
```

## Create DataFrame from CSV File

There are several ways to create DataFrame. In this case, I have CSV file with below content.

```csv
Transaction_ID,Date,Customer_ID,Gender,Age,Product_Category,Quantity,Price_per_Unit,Total_Amount
1,2023-11-24,CUST001,Male,34,Beauty,3,50,150
2,2023-02-27,CUST002,,26,Clothing,2,500,1000
3,2023-01-13,CUST003,Male,50,Electronics,1,30,30
4,2023-05-21,CUST004,Male,37,Clothing,1,500,500
... ...
```

To create `DataFrame` from this CSV file content, you can use `read` method specifying type of file as `csv(filePath)` with file location as input to this method.

```scala
val df = spark.read
    .option("header", "true")
    .option("inferSchema", "true")
    .csv("src/main/resources/data/retail/sales_dataset.csv")
```

### Viewing Data

To view the data of this DataFrame `df`, you can use `show()` method. This method also takes `numRows` (number of rows to show) and `truncate` (whether to truncate the data if string is too large). By default, it will show 20 lines from the `df` DataFrame.

```scala
df.show(5, false)
```

```{ lineNos=false }
+--------------+----------+-----------+------+---+----------------+--------+--------------+------------+
|Transaction_ID|      Date|Customer_ID|Gender|Age|Product_Category|Quantity|Price_per_Unit|Total_Amount|
+--------------+----------+-----------+------+---+----------------+--------+--------------+------------+
|             1|2023-11-24|    CUST001|  Male| 34|          Beauty|       3|            50|         150|
|             2|2023-02-27|    CUST002|  NULL| 26|        Clothing|       2|           500|        1000|
|             3|2023-01-13|    CUST003|  Male| 50|     Electronics|       1|            30|          30|
|             4|2023-05-21|    CUST004|  Male| 37|        Clothing|       1|           500|         500|
|             5|2023-05-06|    CUST005|  Male| 30|          Beauty|       2|            50|         100|
+--------------+----------+-----------+------+---+----------------+--------+--------------+------------+
```

### Reading Schema of the DataFrame

The dataframe has a defined schema with types. Spark automatically infers the schema by reading few rows. This is also explicitly specified when we created this dataframe using `option("inferSchema", "true")`.

```scala
df.printSchema()
```

```{ lineNos=false }
root
 |-- Transaction_ID: integer (nullable = true)
 |-- Date: date (nullable = true)
 |-- Customer_ID: string (nullable = true)
 |-- Gender: string (nullable = true)
 |-- Age: integer (nullable = true)
 |-- Product_Category: string (nullable = true)
 |-- Quantity: integer (nullable = true)
 |-- Price_per_Unit: integer (nullable = true)
 |-- Total_Amount: integer (nullable = true)
```

### Count of Rows

You can check count of rows in Spark DataFrame using `count()` method.

```scala
val count = df.count()
println(s"Count of rows: $count") // Count of rows: 30
```

## Create DataFrame using Schema

In above code, you created `DataFrame` by schema inference. This method works but sometimes, you may need total control over data and want to explicitly specify which type each column is assigned. In such situation, you can define schema and assign the schema while reading data. The schema is defined as an array of `StructField` which specifies data type and column name for each field in data.
Also, when reading schema, you can assign `inferSchema` option to `false` value and specify schema using `schema()` method.

```scala
val salesSchema = StructType(
    Array(
        StructField("Transaction_ID", StringType),
        StructField("Date", StringType),
        StructField("Customer_ID", StringType),
        StructField("Gender", StringType),
        StructField("Age", IntegerType),
        StructField("Product_Category", StringType),
        StructField("Quantity", IntegerType),
        StructField("Price_per_Unit", DoubleType),
        StructField("Total_Amount", DoubleType)
    )
)

val dfWithSchema = spark.read
    .format("csv")
    .schema(salesSchema)
    .option("inferSchema", "false")
    .option("header", "true")
    .load("src/main/resources/data/retail/sales_dataset.csv")

dfWithSchema.show(5)

dfWithSchema.printSchema()
```

```{ lineNos=false }
+--------------+----------+-----------+------+---+----------------+--------+--------------+------------+
|Transaction_ID|      Date|Customer_ID|Gender|Age|Product_Category|Quantity|Price_per_Unit|Total_Amount|
+--------------+----------+-----------+------+---+----------------+--------+--------------+------------+
|             1|2023-11-24|    CUST001|  Male| 34|          Beauty|       3|          50.0|       150.0|
|             2|2023-02-27|    CUST002|  NULL| 26|        Clothing|       2|         500.0|      1000.0|
|             3|2023-01-13|    CUST003|  Male| 50|     Electronics|       1|          30.0|        30.0|
|             4|2023-05-21|    CUST004|  Male| 37|        Clothing|       1|         500.0|       500.0|
|             5|2023-05-06|    CUST005|  Male| 30|          Beauty|       2|          50.0|       100.0|
+--------------+----------+-----------+------+---+----------------+--------+--------------+------------+
only showing top 5 rows

root
 |-- Transaction_ID: string (nullable = true)
 |-- Date: string (nullable = true)
 |-- Customer_ID: string (nullable = true)
 |-- Gender: string (nullable = true)
 |-- Age: integer (nullable = true)
 |-- Product_Category: string (nullable = true)
 |-- Quantity: integer (nullable = true)
 |-- Price_per_Unit: double (nullable = true)
 |-- Total_Amount: double (nullable = true)
```

As you can see above, `Date` field is of type `string`, `Price_per_Unit` and `Total_Amount` fields have also changed to type `double` because we explicitly assigned those types using schema in `salesSchema`.

## Selecting Columns to Show

Spark provides several different methods to select required columns. The `col()` method is defined in `org.apache.spark.sql.functions` package. So, we need to import from there.

```scala
import org.apache.spark.sql.functions._
dfWithSchema.select("Transaction_ID", "Price_per_Unit", "Customer_ID")
    .show(2)

dfWithSchema
    .select(
        col("transaction_id"),
        col("price_per_unit"),
        col("customer_id")
    )
    .show(2)
```

### Using Scala Symbols

Similarly, in Scala, there are couple of Symbols defined for easily specifying columns you want to output. These are defined as `implicit` type in `SQLImplicits` which can be imported from `spark.implicits` package. Without importing this package, you can't use these symbols.

```scala
import spark.implicits._
dfWithSchema
    .select($"Transaction_ID", $"Price_per_Unit", $"Customer_ID")
    .show(2)
dfWithSchema
    .select('Transaction_ID, 'Price_per_Unit, 'Customer_ID)
    .show(2)
```

All the above `show()` method calls produce below output.

```{ lineNos=false }
+--------------+--------------+-----------+
|Transaction_ID|Price_per_Unit|Customer_ID|
+--------------+--------------+-----------+
|             1|          50.0|    CUST001|
|             2|         500.0|    CUST002|
+--------------+--------------+-----------+
only showing top 2 rows
```

### Using `expr` and `selectExpr`

There are `expr` and `selectExpr` methods. These methods take SQL expression as string and parses them to produce the output. For example, below code calculates 90% discount on `Price_per_Unit` and produces output with discounted price.

```scala
dfWithSchema
    .selectExpr("Transaction_ID", "Price_per_Unit * 0.9", "Customer_ID")
    .show(2)
dfWithSchema
    .select(
        expr("Transaction_ID"),
        expr("Price_per_Unit * 0.9"),
        expr("Customer_ID")
    ).show(2)
```

```{ lineNos=false }
+--------------+----------------------+-----------+
|Transaction_ID|(Price_per_Unit * 0.9)|Customer_ID|
+--------------+----------------------+-----------+
|             1|                  45.0|    CUST001|
|             2|                 450.0|    CUST002|
+--------------+----------------------+-----------+
```

## CSV File Configurations

Spark also provides lots of configuration options if we have a different delimiter or a different quotes character etc. The below table shows only important configuration options you can specify while reading a delimited file. Some of those options work only during `read` or `write` operations as mentioned in **Scope** column.

| Option Name   | Description                                                       | Scope             | Default Value     |
|:--------------|:------------------------------------------------------------------|:------------------|:------------------|
| sep           | Specifies the separator used between each field and value.        | read/write        | `,`               |
| quote         | Specifies single character used for escaping quoted values        | read/write        |`"`                |
| header        | Specifies whether the file contains first line as header for fields| read/write       | `false`           |
| inferSchema   | Infers the input schema automatically from data.                  | read              | `false`           |
| nullValue     | Sets the string representation for a `null` value in the file.    | read/write        |                   |
| lineSep       | Defines the line separator used to parse file or to write a file. | read/write        | `\r`, `\r\n` and `\n` for reading and `\n` for writing |
| dateFormat    | Specifies string that indicates a date format for `date` data type columns. | read/write | `yyyy-MM-dd`   |
| timestampFormat | Specifies string representing timestamp format for `timestamp` data type columns. | read/write | `yyyy-MM-dd'T'HH:mm:ss[.SSS][XXX]` |

More options for working with CSV files can be found [here](https://spark.apache.org/docs/latest/sql-data-sources-csv.html#data-source-option)

## Conclusion

This was a brief introduction to DataFrames. They are actually very similar to rows and columns in a SQL database table. Next, let's look at different ways to create a `DataFrame` in Spark.
