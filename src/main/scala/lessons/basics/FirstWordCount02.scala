package lessons.basics

import org.apache.spark.sql.{DataFrame, SparkSession}

object FirstDataFrame {
    // Create spark session
    val spark = SparkSession.builder()
        .appName("FirstWordCount")
        .config("spark.master", "local")
        .getOrCreate()

    val numbers = Seq(
        ("One", 1),
        ("Two", 2),
        ("Three", 3)
    )

    def main(args: Array[String]): Unit = {
        val numbersDf = getNumbersDf()
        numbersDf.show()

        // how to check schema
        numbersDf.printSchema()

        // how to check only 2 rows
        numbersDf.show(2)
        numbersDf.take(2).foreach(println)

        // how to check specific column
        numbersDf.select("word").show()
    }

    /**
     * 1. Create Dataframe from Seq
     * @return
     */
    def getNumbersDf(): DataFrame = {
        val numbersDf = spark.createDataFrame(numbers)
            .toDF("word", "number")
        numbersDf
    }
}
