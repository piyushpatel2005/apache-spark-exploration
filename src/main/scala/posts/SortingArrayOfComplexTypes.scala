package posts

import utils.SparkSessionWrapper
import org.apache.spark.sql.functions._

object SortingArrayOfComplexTypes extends SparkSessionWrapper {
    val data = Seq(
        ("James", List(("Python", 349), ("Scala", 134))),
        ("Michael", List(("Python", 349), ("CSharp", 239), ("Scala", 134))),
        ("Robert", List(("Python", 349), ("Scala", 134), ("SQL", 456), ("Java", 323))),
        ("Washington", List(("SQL", 456), ("Python", 349), ("Scala", 134)))
    )

    import spark.implicits._
    val df = data.toDF("name", "languages")

    df.printSchema()
    // See the dataset
    df.show(false)

    df.select(col("name"), array_sort(
        transform($"languages", elem => elem.getItem("_1"))
    ).as("sorted_languages"))
        .show(false)

    // Order the languages by their name
    df.selectExpr("name", "array_sort(transform(languages, x -> x._1)) as sorted_languages")
        .show(false)

    // Order the languages with their rating by their rating in ascending order
    df.selectExpr("name", "array_sort(languages, (left, right) -> case when left._2 < right._2 then -1 when left._2 > right._2 then 1 else 0 end) as sorted_languages")
        .show(false)

    // Order the languages with their rating by their rating in ascending order
    df.select(col("name"),
        array_sort(
            $"languages", (left, right) => when(left.getField("_2") < right.getField("_2"), -1)
                .when(left.getField("_2") > right.getField("_2"), 1)
                .otherwise(0)
        ).as("sorted_languages"))
        .show(false)

    df.createOrReplaceTempView("skills")
    spark.sql("SELECT name, array_sort(languages, (left, right) -> case when left._2 < right._2 then -1 when left._2 > right._2 then 1 else 0 end) as sorted_languages FROM skills")
        .show(false)

}
