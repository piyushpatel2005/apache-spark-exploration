package posts.aggregations

import org.apache.spark.sql.expressions.Window
import org.apache.spark.sql.functions
import org.apache.spark.sql.functions._
import utils.SparkSessionWrapper

import java.sql.Date
import java.time.LocalDate

/**
 * The problem is to aggregate between two rows in a window.
 * Here, we are given the date, name and the amount of promotion per day.
 * The promotions amount may overlap between two consecutive days. We want to find the sum of maximum promotions across all days.
 * For example, if we have the following data:
 * +----------+----+-----+
 * |      date|name|promo|
 * +----------+----+-----+
 * |2020-01-01| Bob|  200|
 * |2020-01-02| Bob|  100|
 * |2020-01-03| Bob|  300|
 * |2020-01-04| Bob|  400|
 * |2020-01-05| Bob|  500|
 * +----------+----+-----+
 *
 * The output should be:
 * +----------+----+-----+------+
 * |      date|name|promo|maxPromo|
 * +----------+----+-----+--------+
 * |2020-01-01| Bob|  100|     200|
 * |2020-01-02| Bob|  200|     200|
 * |2020-01-03| Bob|  300|     300|
 * |2020-01-04| Bob|  400|     400|
 * |2020-01-05| Bob|  500|     500|
 * +----------+----+-----+--------+
 *
 * and finally we want to find sum of maxPromo across all days. Here, because we have to find the maximum promo across two day windows.
 *
 * that will be 200 + 400 + 500 = 1100
 */
object AggregateBetweenTwoRowsWindow extends SparkSessionWrapper {
    LocalDate.of(2020, 1, 1)
    val data = Seq(
        (LocalDate.of(2020, 1, 1), "Bob", 200),
        (LocalDate.of(2020, 1, 2), "Bob", 100),
        (LocalDate.of(2020, 1, 3), "Bob", 300),
        (LocalDate.of(2020, 1, 4), "Bob", 400),
        (LocalDate.of(2020, 1, 5), "Bob", 500)
    )

    val df = spark.createDataFrame(data).toDF("date", "name", "promo")

    val window = Window.partitionBy("name").orderBy("date")

    // create grouping using row_number and then group by the row_number to find the max promo across two day window
    df.withColumn("row_number", row_number().over(window))
        .withColumn("row_number", (col("row_number").plus(1)).divide(2).cast("int"))
        .groupBy("row_number").agg{
            max("promo").as("max_promo")
        }.show(false)
}
