package basics

import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.SparkSession.setActiveSession

object CreatingSession01 extends App {
    val spark = SparkSession.builder()
        .appName("CreatingSession")
        .config("spark.master", "local")
        .getOrCreate()

    val sqlContext = spark.sqlContext

    val sc = spark.sparkContext

    val sparkConf = spark.conf

    sparkConf.getAll.foreach(println)

    val count = spark.sparkContext.parallelize(1 to 100).count()
    println(s"Count is $count")

    spark.stop()

    // Cannot call methods on a stopped SparkContext
//    spark.sparkContext.parallelize(1 to 100).count()
}
