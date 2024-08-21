package posts.arrayfns

import org.apache.spark.sql.Row
import org.apache.spark.sql.types._
import utils.SparkSessionWrapper
import org.apache.spark.sql.functions._

object ArrayFnsMain extends SparkSessionWrapper {

//    val schema = StructType.fromDDL("name STRING, credits ARRAY<STRUCT<credit_pack_status: STRING, credits_remaining: INT>>")
    val schema = StructType(
        Array(
            StructField("name", StringType),
            StructField("credits", ArrayType(
                StructType(
                    Array(
                        StructField("credit_pack_status", StringType),
                        StructField("credits_remaining", IntegerType)
                    )
                )
            )
        )
    ))

    val data = Seq(
        Row("James", Array(Row("expired", 0), Row("inactive", 0))),
        Row("Michael", Array(Row("active", 200), Row("inactive", 10), Row("active", 300))),
        Row("Robert", Array(Row("expired", 400), Row("inactive", 0), Row("active", 500), Row("inactive", 0))),
        Row("Washington", Array(Row("inactive", 40), Row("active", 0), Row("inactive", 0)))
    )

    import spark.implicits._
    val df = spark.createDataFrame(spark.sparkContext.parallelize(data), schema)

//    df.printSchema()

    df.withColumn("active_credits", when(forall($"credits", x => x.getField("credits_remaining").gt(0)), "Active")
        .otherwise("Inactive"))
        .show(false)

    df.withColumn("active_credits", when(size(filter($"credits", x => x.getField("credits_remaining").gt(0))).gt(0), "Active")
        .otherwise("Inactive"))
        .show(false)

}
