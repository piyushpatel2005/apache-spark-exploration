package lessons.section1.dataframes

import org.apache.spark.sql.types._
import org.apache.spark.sql.functions._
import utils.SparkSessionWrapper

object DataFrameTransformations05 extends SparkSessionWrapper {
    val salesSchema = StructType(
        Array(
            StructField("Transaction_ID", StringType),
            StructField("Date", StringType),
            StructField("Customer_ID", StringType),
            StructField("Gender", StringType),
            StructField("Age", IntegerType),
            StructField("Product_Category", StringType),
            StructField("Quantity", IntegerType),
            StructField("Price_per_Unit", DoubleType),
            StructField("Total_Amount", DoubleType)
        )
    )

    val df = spark.read
        .format("csv")
        .schema(salesSchema)
        .option("inferSchema", "false")
        .option("header", "true")
        .load("src/main/resources/data/retail/sales_dataset.csv")

    // 1. Select columns
    val dfSelect = df.select("Transaction_ID", "Customer_ID", "Total_Amount")
    val dfSelect2 = df.select(col("Transaction_ID"), col("Customer_ID"), col("Total_Amount"))

    import spark.implicits._
    val dfSelect3 = df.select($"Transaction_ID", $"Customer_ID", $"Total_Amount")
    val dfSelect4 = df.select('Transaction_ID, 'Customer_ID, 'Total_Amount)
    val dfSelect5 = df.selectExpr("Transaction_ID", "Customer_ID", "Total_Amount")

    // 2. Filter by column value
    val dfFilter = df.filter(col("Product_Category") === "Electronics")
    val dfFilter2 = df.where(col("Product_Category") === "Electronics")

    val dfFilter3 = df.filter($"Product_Category" === "Electronics")
    val dfFilter4 = df.filter('Product_Category === "Electronics")

    // 3. Filter by multiple column values
    val dfFilterMultipleValues = df.filter(col("Product_Category").isin("Electronics", "Clothing"))

    // 4. Filter by multiple column values
    val dfFilterMultipleColumns = df.filter(col("Product_Category") === "Electronics" && col("Gender") === "Male")

    // 5. Renaming columns
    val dfRenamed = df.withColumnRenamed("Product_Category", "Category")

    // 6. Drop columns
    val dfDrop = df.drop("Transaction_ID", "Customer_ID")

    // 7. Adding columns
    val dfAdded = df.withColumn("AmountAfterTax", col("Total_Amount") * 1.1)

    // 8. Updating columns
    val dfUpdated = df.withColumn("Total_Amount", col("Total_Amount") * 1.1)

    // 9. Sorting
    val dfSorted = df.sort(col("Total_Amount").desc)
    val dfSorted2 = df.orderBy(col("Total_Amount").desc)

    // 10. Limit
    val dfLimited = df.limit(10)

    // 11. Distinct
    val dfDistinct = df.select("Product_Category").distinct()

    // 12. Drop duplicates
    val dfDropDuplicates = df.dropDuplicates("Product_Category")


    // 13. Aggregations
    val dfAggregated = df.groupBy("Product_Category").agg(
        sum("Total_Amount").as("Total_Amount"),
        avg("Total_Amount").as("Average_Amount")
    )

    // Actions
    dfSelect.show()

    dfSelect.count()

    dfSelect.collect()

    dfSelect.write.mode("overwrite").csv("src/main/resources/warehouse/write_demo")

}
