package posts.least_coalesce

import utils.SparkSessionWrapper

import java.sql.Timestamp

object LeastCoalesce extends SparkSessionWrapper {
    val data = Seq(
        ("James", Timestamp.valueOf("2021-01-01 00:00:00"), 123),
        ("Michael", Timestamp.valueOf("2021-01-01 00:00:00"), 234),
        ("Robert", Timestamp.valueOf("2021-01-01 00:00:00"), 345),
        ("Washington", Timestamp.valueOf("2021-01-01 00:00:00"), 456),
        ("James", Timestamp.valueOf("2021-01-01 00:00:00"), 123),
        ("James", Timestamp.valueOf("2021-01-01 00:00:00"), 123),
        ("Robert", Timestamp.valueOf("2021-01-01 00:00:00"), 345)
    )

    import spark.implicits._

    val df = spark.sparkContext.parallelize(data).toDF("name", "date", "amount")

    // Find maximum
    // The behavior of LEAST and GREATEST is different from that of the SQL functions with the same names.
    // In spark, if those functions are given a column with values `NULL`, they can by default return `NULL`. They don't need coalesce.
    // In BigQuery, the behavior is different. If the column has a `NULL` value, the functions will return `NULL`. If you want to avoid `NULL`, you need to use `COALESCE`.
    // LEAST(1, NULL, 3) -> 1
    // GREATEST(1, NULL, 3) -> 3
    // In BigQuery,
    // LEAST(1, NULL, 3) -> NULL
    // GREATEST(1, NULL, 3) -> NULL
}
