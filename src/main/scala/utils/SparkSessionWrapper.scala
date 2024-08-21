package utils

import org.apache.spark.sql.SparkSession

trait SparkSessionWrapper extends App {
    lazy val spark: SparkSession = {
        SparkSession.builder()
            .appName("Test Spark Session")
            .config("spark.master", "local")
            .getOrCreate()
    }

    lazy val sc = spark.sparkContext

    lazy val sqlContext = spark.sqlContext

}
