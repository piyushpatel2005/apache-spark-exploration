package section1.dataframes

import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.types._

object DataFrameIntro extends App {
    // 1. Create spark session

    val spark = SparkSession.builder()
        .appName("DataFrameIntro")
        .config("spark.master", "local") // for local run else yarn
        .getOrCreate()

    // 2. Create Dataframe from CSV file
    val df = spark.read
        .option("header", "true")
        .option("inferSchema", "true")
        .csv("src/main/resources/data/retail/sales_dataset.csv")

    // 3. Show the dataframe
    df.show()

    // 4. Print the schema
    df.printSchema()

    // 5. Show only 2 rows
    df.show(2)

    // 6. Take 2 rows
    df.take(5).foreach(println)

    // 7. Count rows
    val count = df.count()
    println(s"Count of rows: $count")

    // 8. Defining schema
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

    // 9. Using schema with format method
    val dfWithSchema = spark.read
        .format("csv")
        .schema(salesSchema)
        .option("inferSchema", "false")
        .option("header", "true")
        .load("src/main/resources/data/retail/sales_dataset.csv")

    dfWithSchema.show(5)

    dfWithSchema.printSchema()

    /**
     * Next read file sales_dataset_with_null_and_quotes.csv.
     * This file has couple of null values marked as "NA"
     * It also has quotes around string fields with character single quote (').
     * It has delimiter of colon (:)
     * It does not have header
     */

    val df2 = spark.read
        .schema(salesSchema)
        .option("header", "false")
        .option("inferSchema", "false")
        .option("sep", ":")
        .option("nullValue", "NA")
        .option("quote", "'")
        .csv("src/main/resources/data/retail/sales_dataset_with_null_and_quotes.csv")

    df2.show()

    // More options here: https://spark.apache.org/docs/latest/sql-data-sources-csv.html
}
