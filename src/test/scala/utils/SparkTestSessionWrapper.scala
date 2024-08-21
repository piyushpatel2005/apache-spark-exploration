package utils

import org.apache.spark.sql.SparkSession

trait SparkTestSessionWrapper {
    lazy val spark: SparkSession = {
        SparkSession.builder()
            .appName("Test Spark Session")
            .config("spark.master", "local")
            .getOrCreate()
    }

}
