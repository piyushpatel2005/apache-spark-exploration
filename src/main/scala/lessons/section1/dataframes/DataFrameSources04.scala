package lessons.section1.dataframes

import org.apache.spark.sql.types.StructType
import org.apache.spark.sql.{DataFrame, SaveMode, SparkSession}

import lessons.domains._

object DataFrameSources04 {
    val spark = SparkSession.builder()
        .appName("DataFrameCreation")
        .config("spark.master", "local")
        .getOrCreate()

    // Create Dataframe from Seq
    def dfFromSeq(): DataFrame = {
        val numbers = Seq(
            ("John", "Technologogy", 23),
            ("Jenny", "Finance", 25),
            ("Tom", "HR", 27)
        )
        val numbersDf = spark.createDataFrame(numbers)
            .toDF("name", "department", "age")

        numbersDf.show()
        numbersDf
    }

    def dfFromCsv(): DataFrame = {
        val df = spark.read
            .option("header", "true")
            .option("inferSchema", "true")
            .csv("src/main/resources/data/retail/sales_dataset.csv")

        df.show()
        df
    }

    def dfFromRow(): DataFrame = {
        import org.apache.spark.sql.Row
        import org.apache.spark.sql.types.{StructType, StructField, StringType, IntegerType}

        val schema = StructType(
            Array(
                StructField("name", StringType),
                StructField("department", StringType),
                StructField("age", IntegerType)
            )
        )

        val rows = Seq(
            Row("John", "Technologogy", 23),
            Row("Jenny", "Finance", 25),
            Row("Tom", "HR", 27)
        )

        val rowDf = spark.createDataFrame(spark.sparkContext.parallelize(rows), schema)
        rowDf.show()

        rowDf
    }

    // Create Dataframe from JSON
    // https://www.kaggle.com/datasets/matheusgratz/world-university-rankings-2021
    // https://api.nobelprize.org/v1/prize.json
    def dfFromJson(): DataFrame = {
        val df = spark.read
            .json("src/main/resources/data/ecommerce/products.json")

        df.show()

        df
    }

    def dfFromJson(options: Map[String, String]): DataFrame = {
        val df = spark.read
            .options(options)
            .json("src/main/resources/data/ecommerce/products.json")

        df.show()

        df
    }

    def dfFromJsonWithSchema(schema: StructType, location: String): DataFrame = {
        val df = spark.read
            .schema(schema)
            .json(location)

        df.show()

        df
    }

    def writeOrc(df: DataFrame, path: String): Unit = {
        df.write
            .mode(SaveMode.Overwrite)
            .orc(path)
    }

    def writeParquet(df: DataFrame, path: String): Unit = {
        df.write
            .mode(SaveMode.Overwrite)
            .parquet(path)
    }

    def writeCsv(df: DataFrame, path: String): Unit = {
        df.write
            .mode(SaveMode.Overwrite)
            .option("header", "true")
            .csv(path)
    }

    def writeJson(df: DataFrame, path: String): Unit = {
        df.write
            .mode(SaveMode.Overwrite)
            .json(path)
    }

    def main(args: Array[String]): Unit = {
        dfFromSeq()
        dfFromCsv()
        val rowsDf = dfFromRow()
        dfFromJson()
        dfFromJson(
            Map(
                "multiLine" -> "false",
                "mode" -> "PERMISSIVE",
                "columnNameOfCorruptRecord" -> "_corrupt_record",
                "lineSep" -> "\n",
                "dropFieldIfAllNull" -> "false"
            )
        )
        dfFromJsonWithSchema(productsSchema, "src/main/resources/data/ecommerce/products.json")

//        writeOrc(rowsDf, "src/main/resources/warehouse/rows_orc/")
//        writeParquet(rowsDf, "src/main/resources/warehouse/rows_parquet/")
//        writeCsv(rowsDf, "src/main/resources/warehouse/rows_csv/")
    }

}
