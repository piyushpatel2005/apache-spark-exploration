package utils

import org.apache.spark.sql.SparkSession

trait SparkSessionTestWrapper {
    lazy val spark: SparkSession = {
        SparkSession.builder()
            .appName("Test Spark Session")
            .config("spark.master", "local")
            .getOrCreate()
    }

}
