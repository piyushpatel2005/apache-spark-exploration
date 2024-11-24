# Spark DataFrame Operations

Spark provides various transformations and actions that can be applied to DataFrames. In this section, we will explore some of the most common operations that can be applied to DataFrames.

DataFrame operations can be categorized into the following:

1. **Transformations**: Transformations are operations that are applied to DataFrames to create new DataFrames. These are lazy operations performed by Spark on Dataframes. Spark creates a logical plan for these operations and optimizes these operations Examples of transformations include `select`, `filter`, `groupBy`, `orderBy`, `dropDuplicates`, `drop`, `withColumn`, etc.
2. **Actions**: Actions are operations that are applied to DataFrames to trigger computation and return results. Examples of actions include `show`, `count`, `collect`, `take`, `first`, `head`, `foreach`, etc.


## DataFrame Transformations

Again in this lesson, we start off by reading a CSV file into a DataFrame.


```scala
import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.functions._
import org.apache.spark.sql.types._

object DataFrameTransformation extends App {
    val spark = SparkSession.builder()
        .appName("DataFrameTransformations")
        .config("spark.master", "local")
        .getOrCreate()

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

    val df = spark.read
        .format("csv")
        .schema(salesSchema)
        .option("inferSchema", "false")
        .option("header", "true")
        .load("src/main/resources/data/retail/sales_dataset.csv")
}
```

### Select

The `select` transformation is used to select one or more columns from a DataFrame. The `select` transformation returns a new DataFrame with the selected columns. You've seen this operation in previous lesson, but it's worth mentioning it again. Make sure you import the `functions` package to use the `col` function and also the `spark.implicits` package to use the `$` syntax or `'` syntax to select columns. These can be convenient way as you don't have to type those many characters to select columns.

```scala
val dfSelect = df.select("Transaction_ID", "Customer_ID", "Total_Amount")
val dfSelect2 = df.select(col("Transaction_ID"), col("Customer_ID"), col("Total_Amount"))

import spark.implicits._
val dfSelect3 = df.select($"Transaction_ID", $"Customer_ID", $"Total_Amount")
val dfSelect4 = df.select('Transaction_ID, 'Customer_ID, 'Total_Amount)
val dfSelect5 = df.selectExpr("Transaction_ID", "Customer_ID", "Total_Amount")
```

### Filter

The `filter` transformation is used to filter rows from a DataFrame based on a condition. The `filter` transformation returns a new DataFrame with rows that satisfy the condition. The condition is specified as a column expression.

```scala
val dfFilter = df.filter(col("Product_Category") === "Electronics")
```

Again, you can use Scala symbols by importing `spark.implicits._` to make the code more concise.

```scala
import spark.implicits._
val dfFilter2 = df.filter($"Product_Category" === "Electronics")
val dfFilter3 = df.filter('Product_Category === "Electronics")
```

You can filter rows based on multiple values of a column using `isin` function.

```scala
val dfFilterMultipleValues = df.filter(col("Product_Category").isin("Electronics", "Clothing"))
```

You could use multiple conditions in a filter operation using `&&` or `||` operators.

```scala
val dfFilterMultipleColumns = df.filter(col("Product_Category") === "Electronics" && col("Gender") === "Male")
```

#### `where` Transformation

You can also use `where` transformation to filter rows from a DataFrame based on a condition. The `where` transformation is an alias for the `filter` transformation.

```scala
val dfFilter2 = df.where(col("Product_Category") === "Electronics")
```

### Renaming a column

You can rename a column in a DataFrame using the `withColumnRenamed` transformation. The `withColumnRenamed` transformation returns a new DataFrame with the specified column renamed. The `withColumnRenamed` transformation takes two arguments: the name of the column to be renamed and the new name of the column.

```scala
val dfRenamed = df.withColumnRenamed("Product_Category", "Category")
```

### Adding/Updating a new column

You can add a new column to a DataFrame using the `withColumn` transformation. The `withColumn` transformation returns a new DataFrame with the specified column added. The `withColumn` transformation takes two arguments: the name of the new column and the column expression.

```scala
val dfWithNewColumn = df.withColumn("AmountAfterTax", col("Total_Amount") * 1.1)
```

To update an existing column, you can use the `withColumn` transformation with the same column name. It will return new dataframe with newly calculated values for the column being updated.

```scala
val dfWithUpdatedColumn = df.withColumn("Total_Amount", col("Total_Amount") * 1.15)
```

### Dropping a column

You can drop a column from a DataFrame using the `drop` transformation. The `drop` transformation returns a new DataFrame with the specified column(s)dropped. The `drop` transformation can take any number of arguments which are columns to be dropped.

```scala
val dfDrop = df.drop("Transaction_ID", "Customer_ID")
```

### Sorting by column

You can sort a DataFrame by one or more columns using the `orderBy` transformation. The `orderBy` transformation returns a new DataFrame with rows sorted by the specified column(s). The `orderBy` transformation takes one or more column names as arguments. You can specify to sort in ascending or descending order using the `asc` or `desc` functions.

```scala
val dfSorted = df.orderBy(col("Total_Amount").desc)
```

You can alternatively use the `sort` transformation which is an alias for the `orderBy` transformation.

```scala
val dfSorted2 = df.sort(col("Total_Amount").desc)
```

### Limiting the number of rows

You can limit the number of rows in a DataFrame using the `limit` transformation. The `limit` transformation returns a new DataFrame with only specified number of rows.

```scala
val dfLimited = df.limit(10)
```

### Distinct rows

You can find distinct rows in a DataFrame using the `distinct` transformation. The `distinct` transformation returns a new DataFrame with only distinct rows based on given columns.

```scala
val dfDistinct = df.select("Product_Category").distinct()
```

Related to this is the `dropDuplicates` transformation which can be used to drop duplicate rows from a DataFrame based on given columns.

```scala
val dfDropDuplicates = df.dropDuplicates("Product_Category")
```

## Actions

In Spark, actions are what materialized the DAG and execute the transformations. Actions are operations that trigger the computation of the DAG and return the result to the driver program or write it to the external storage system. Below are some of the most commonly used actions in Spark.

### Show

The `show` action is used to display the first n rows of a DataFrame in tabular format. The `show` action is useful for quickly inspecting the contents of a DataFrame. You can also pass the number of results to see and whether to truncate the results or not.

```scala
df.show()
df.show(10)
df.show(10, false) // do not truncate the results if they are too long
```

### Collect

The `collect` action is used to retrieve all the rows of a DataFrame to the driver program. The `collect` action returns an array of rows. The `collect` action should be avoided where possible as it retrieves all the rows of a DataFrame to the driver program and can result in out of memory errors if the DataFrame is too big to store in driver memory.

```scala
val rows = df.collect()
```

### Count

The `count` action can be used to count the number of rows in a DataFrame. This can be used to validate DataFrame results.

```scala
val count = df.count()
```

### Writing DataFrame

You can write the contents of a DataFrame using `write` action. The `write` action returns a `DataFrameWriter` object which can be used to configure the write operation with whether you want to .

```scala
df.write.mode("overwrite").csv("src/main/resources/warehouse/write_demo")
```

This was brief overview of the some of the most commonly used transformations and actions in Spark. I will cover few more transformations in the upcoming lessons in this series.
