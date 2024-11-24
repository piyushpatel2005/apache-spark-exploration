# Spark Exploration

This repository contains a series of Spark Scala code that explore the capabilities of Apache Spark.
The code is written in Scala and can be executed in local spark mode or in a cluster.

## Requirements

- Java 17
- Scala 2.12
- Apache Spark 3.5.1
- SBT

## Installation

1. Clone the repository
2. Install the requirements
3. Go over each pacakge code and execute them.
4. Test can be executed using `sbt test` command.
5. Enjoy!

## Packages in this repository

- **Basics**: Basic operations with Spark.
- **Sections 1: DataFrames**: Basic operations with Spark DataFrames.
  - DataFrame Introduction: Create from CSV file, selecting fields, printing schema, and show data.
  - DataFrame Creation from CSV, JSON, Parquet, Sequence, and Text files.
  - DataFrame Operations: filter, map, groupBy, agg, orderBy
  - DataFrame Joins: inner, outer, left, right, semi, anti, cross

- **Posts**: Contains the code for the blog post series.


## Tutorials

If you're new to Spark, check out these tutorials which cover Spark in fair bit of details.

- [Spark Overview](notes/spark/apache-spark-overview/index.md)
- [Spark Architecture](notes/spark/apache-spark-architecture/index.md)
- [Spark Development Setup](notes/spark/apache-spark-dev-setup/index.md)
- [Spark Shell](notes/spark/spark-shell/index.md)
- [Spark DataFrames](notes/spark/intro-to-dataframes/index.md)
- [Spark Data Sources](notes/spark/data-sources/index.md)
- [Spark DataFrames Operations](notes/spark/dataframe-operations/index.md)
- [Spark Joins](notes/spark/types-of-joins/index.md)
- [Spark Aggregations](notes/spark/aggregations/index.md)
- [Spark SQL](notes/spark-sql/index.md)

## License

This project is licensed under the MIT License - see the [LICENSE.md](LICENSE.md) file for details.
