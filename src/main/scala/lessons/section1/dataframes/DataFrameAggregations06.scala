package lessons.section1.dataframes

import org.apache.spark.sql.functions._
import utils.SparkSessionWrapper

object DataFrameAggregations06 extends SparkSessionWrapper {
    import spark.implicits._
    // Athlete scores
    val df = Seq(
        ("Europe", "France", "Edgar", "Swimming", 23),
        ("Europe", "Germany", "Henry", "Soccer", 12),
        ("Europe", "Spain", "Juan", "Swimming",  10),
        ("Europe", "Russia", "Sergey", "Boxing", 13),
        ("Europe", "Germany", "Sara", "Swimming", 14),
        ("Asia", "Pakistan", "Iqbal", "Boxing", 11),
        ("Asia", "India", "Nikhil", "Boxing", 15),
        ("Asia", "India", "Murugan", "Boxing", 12),
        ("North America", "USA", "Vasia", "Soccer", 10),
        ("North America", "Canada", "Jeff", "Swimming", 12),
        ("North America", "Mexico", "Juan", "Swimming", 10),
        ("North America", "USA", "Peter", "Soccer", 11),
    ).toDF("continent", "country", "name", "game", "score")

    //    df.select(max("score"))
    val maxScore = df.selectExpr("max(score) as max_score", "count(1) as total_rows", "avg(score) as avg_score")

//    maxScore.show(false)

    val distinctCount = df.select(countDistinct("continent"))
    df.select(approx_count_distinct("continent"))
    distinctCount.show(false)

    df.select(approx_count_distinct("continent"))

    val scoresByContinent = df.groupBy(col("continent"))
        .sum("score")
//    scoresByContinent.show(false)

    val scoresByCountry = df.groupBy("continent", "country")
        .sum("score").as("score_by_country")
        .orderBy(desc("continent"), asc("country"))

//    scoresByCountry.show(false)

    // Find athelete names who completed in Swimming
    val athleteNamesPerGame = df.groupBy("game")
        .agg(
            collect_list(col("name")).as("athletes")
        )
//    athleteNamesPerGame.show(false)

    val uniqueAthleteNamesPerGame = df.filter(col("game").equalTo("Swimming"))
        .groupBy("game")
        .agg(
            collect_set(col("name")).as("athletes")
        )
//    uniqueAthleteNamesPerGame.show(false)

    val multipleAggregations = df.groupBy("country")
        .agg(
            sum("score").as("total_score"),
            count("*").as("total_athletes"),
            collect_list(col("name")).as("athletes"),
            avg("score").as("average_score")
        ).orderBy(col("country").asc, col("total_score").desc_nulls_last)

    multipleAggregations.show(false)
}
