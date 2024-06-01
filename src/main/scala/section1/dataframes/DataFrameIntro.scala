package section1.dataframes

import org.apache.spark.sql.SparkSession

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
        .csv("src/main/resources/data/retail-data/by-day/2010-12-01.csv")

    // 3. Show the dataframe
    df.show()

    // 4. Print the schema
    df.printSchema()

    // 5. Show only 2 rows
    df.show(2)

    // 6. Take 2 rows
    df.take(2).foreach(println)
}
