# DataFrame Aggregations

Spark provides multiple options for aggregating data in a group of rows. Aggregations are used to summarize the information. This tutorial will show how to aggregate data using `groupBy`, `cube` and `rollup`.

Let's create a simple dataset to work with. In this case, I have a fictitious dataset of Olympics athletes from several coutries. It includes four fields as mentioned below.

- `continent`: continent of the Athlete
- `country`: Country of the athlete
- `name`: Name of the athlete
- `game`: Sports in which the athlete competes
- `score`: The score he/she achieved in the game

For brevity, I have excluded some other fields which might be of interest

```scala
import spark.implicits._

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
```

When you look at this dataset, you will see below table-like structure

```scala
df.show(false)
```

## Aggregating Data across Full Dataset

Spark provides a simple way to aggregate data across full dataset using `selectExpr` expression or method.

```scala
df.select(max("score"))
```

You can perform the same operation in multiple ways as shown below.

```scala
df.select("max(score) as max_score")
```

You can also perform multiple aggregations across the full dataset using `selectExpr`.

```scala
df.selectExpr("max(score) as max_score", "count(1) as total_rows", "avg(score) as avg_score")
```

Spark also provides a way to calculate the distinct values for a given column using `countDistinct`.

```scala
df.select(countDistinct("continent")).show(false)
```

If you have a very large dataset, you could use `approx_count_distinct` to quickly find out approximate number of rows for the large dataset. In this case, it relies on statistics to perform the approximation on the dataset.

```scala
df.select(approx_count_distinct("continent")).show(false)
```

Spark provides several other aggregations like `sum`, `count`, `avg`, `min`, `max`, `mean`, `stddev`, etc.

## Grouping Data

Next, let's say you want to find sum of scores per continent. In this case, you would need to aggregate the data across all rows and group them by `continent` field and calculate the sum of `score` field. You can do that using Spark like this.

```scala
 val scoresByContinent = df.groupBy(col("continent"))
    .sum("score")
scoresByContinent.show(false)
```

This will show the scores for each of the continent and in this dataset, it will produce just three rows (one for each continent).

```plaintext
+-------------+----------+
|continent    |sum(score)|
+-------------+----------+
|Europe       |72        |
|North America|43        |
|Asia         |38        |
+-------------+----------+
```

### Grouping by Two columns with Order by

You can also aggregate data by multiple columns. In this case, I want to aggregate the data by `continent` and `country` and calculate the sum of `score`s by country. I also want to order the results by `continent` in descending order and by `country` in ascending order. You can order the dataframe results by using `orderBy` method.

```scala
val scoresByCountry = df.groupBy("continent", "country")
    .sum("score")
    .orderBy(desc("continent"), asc("country"))
```

This will produce rows by country, but notice the ordering of `continent` column and the `country` column.

```plaintext
+-------------+--------+----------+
|continent    |country |sum(score)|
+-------------+--------+----------+
|North America|Canada  |12        |
|North America|Mexico  |10        |
|North America|USA     |21        |
|Europe       |France  |23        |
|Europe       |Germany |26        |
|Europe       |Russia  |13        |
|Europe       |Spain   |10        |
|Asia         |India   |27        |
|Asia         |Pakistan|11        |
+-------------+--------+----------+
```

If you want to rename the field `sum(score)` as `score_by_country`, you can use `as("new_name")` method as shown below.

```scala
val scoresByCountry = df.groupBy("continent", "country")
    .sum("score").as("score_by_country")
    .orderBy(desc("continent"), asc("country"))
```

### `collect_list`

Next, let's say you want to gather the name of athletes by `game`. In this case, you do not want to calculate some statistics, but want to collect names of those athletes. You can use `collect_list` for this. In this case, I am using `agg` method to group the aggregations I want to perform. In this case, I have only `collect_list` as one of the aggregations which is collecting all athlete `name`s.

```scala
val athleteNamesPerGame = df.groupBy("game")
    .agg(
        collect_list(col("name")).as("athletes")
    )
athleteNamesPerGame.show(false)
```

Here, you will notice that we have two athletes with name `Juan` for `Swimming` as you can see in the result.

```plaintext
+--------+--------------------------------+
|game    |athletes                        |
+--------+--------------------------------+
|Boxing  |[Sergey, Iqbal, Nikhil, Murugan]|
|Swimming|[Edgar, Juan, Sara, Jeff, Juan] |
|Soccer  |[Henry, Vasia, Peter]           |
+--------+--------------------------------+
```

### `collect_set`

If you only need unique athlete names, you could use `collect_set`. It will only show collection of unique names.

```scala
val uniqueAthleteNamesPerGame = df.filter(col("game").equalTo("Swimming"))
    .groupBy("game")
    .agg(
        collect_set(col("name")).as("athletes")
    )
uniqueAthleteNamesPerGame.show(false)
```

In this case, I am filtering the results to only game of "Swimming" and then collecting the names of those athletes. `collect_set` shows only unique names as you can see below.

```plaintext
+--------+-------------------------+
|game    |athletes                 |
+--------+-------------------------+
|Swimming|[Juan, Jeff, Sara, Edgar]|
+--------+-------------------------+
```

## Performing multiple aggregations

If you want to perform aggregations by similar group, you can perform them all in single `groupBy` method. This is more performant than doing aggregations for each value you want to calculate because once Spark performs grouping, it has those groups available in memory and all it needs is to group the results in different ways.

```scala
val multipleAggregations = df.groupBy("country")
    .agg(
        sum("score").as("total_score"),
        count("*").as("total_athletes"),
        collect_list(col("name")).as("athletes"),
        avg("score").as("average_score")
    ).orderBy(col("country").asc, col("total_score").desc_nulls_last)

multipleAggregations.show(false)
```

Notice that, I have ordered the results by `country` in ascending order and then by `total_score` in descending order with `desc_nulls_last`. The `desc_nulls_last` will sort the results by non-values first and then `null`s. `NULL` values can be difficult to handle so spark provides `desc_nulls_last` and `desc_nulls_first` to move those records either at the end or at the top. The result for above code will look like this.

```plaintext
+--------+-----------+--------------+-----------------+-------------+
|country |total_score|total_athletes|athletes         |average_score|
+--------+-----------+--------------+-----------------+-------------+
|Canada  |12         |1             |[Jeff]           |12.0         |
|France  |23         |1             |[Edgar]          |23.0         |
|Germany |26         |2             |[Henry, Sara]    |13.0         |
|India   |27         |2             |[Nikhil, Murugan]|13.5         |
|Mexico  |10         |1             |[Juan]           |10.0         |
|Pakistan|11         |1             |[Iqbal]          |11.0         |
|Russia  |13         |1             |[Sergey]         |13.0         |
|Spain   |10         |1             |[Juan]           |10.0         |
|USA     |21         |2             |[Vasia, Peter]   |10.5         |
+--------+-----------+--------------+-----------------+-------------+
```
