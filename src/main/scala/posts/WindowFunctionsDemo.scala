package posts

import org.apache.spark.sql.Row
import org.apache.spark.sql.expressions.Window
import utils.SparkSessionWrapper
import org.apache.spark.sql.functions._

object WindowFunctionsDemo extends SparkSessionWrapper {
    val data = Seq(
        ("Electronics", "HP Laptop", 499.99),
        ("Electronics", "Lenovo Laptop", 399.99),
        ("Electronics", "Dell Laptop", 499.99),
        ("Shoes", "Adidas", 120.99),
        ("Shoes", "Reebok", 150.99),
        ("Shoes", "Nike", 120.99),
        ("Shoes", "Puma", 100.99),
        ("Clothing", "Levis", 99.99),
        ("Clothing", "UCB", 49.99),
        ("Clothing", "Zara", 79.99),
        ("Furniture", "Sofa", 1000.99),
        ("Furniture", "Dining Table", 1200.99),
        ("Furniture", "Chair", 500.99),
        ("Furniture", "Bed", 700.99),
        ("Electronics", "Apple iPhone", 999.99),
        ("Electronics", "Samsung Galaxy", 899.99),
        ("Electronics", "Samsung Galaxy", 799.99),
    )

    val productsDf = spark.createDataFrame(data).toDF("category", "product", "price")

    productsDf.printSchema()
    productsDf.show()

    productsDf.groupBy("category")
        .count()
        .show()

    productsDf.groupBy("category")
        .agg (
            sum("price").as("total_price"),
            avg("price").as("avg_price"),
            max("price").as("max_price"),
            min("price").as("min_price"),
            count("price").as("count"),
            collect_list("product").as("products"),
            collect_set("product").as("unique_products")
        ).show(false)

    val windowSpec = Window.partitionBy("category").orderBy("price")

    productsDf.withColumn("rank", rank().over(windowSpec))
        .withColumn("dense_rank", dense_rank().over(windowSpec))
        .withColumn("row_number", row_number().over(windowSpec))
        .withColumn("percent_rank", percent_rank().over(windowSpec))
        .withColumn("ntile", ntile(3).over(windowSpec))
        .withColumn("sum", sum("price").over(windowSpec))
        .withColumn("lag", lag("price", 1).over(windowSpec))
        .withColumn("lead", lead("price", 1).over(windowSpec))
        .show(false)

//  TODO: Figure out how to get rangeBetween and rowsBetween to work
    val rangeBetween = Window
        .partitionBy("category")
        .orderBy("price")
        .rowsBetween(Window.currentRow, 1)

    productsDf
//        .withColumn("rank", rank().over(rangeBetween))
//        .withColumn("dense_rank", dense_rank().over(rangeBetween))
        .withColumn("row_number", row_number().over(rangeBetween))
//        .withColumn("percent_rank", percent_rank().over(rangeBetween))
//        .withColumn("ntile", ntile(3).over(rangeBetween))
//        .withColumn("sum", sum("price").over(rangeBetween))
//        .withColumn("lag", lag("price", 1).over(rangeBetween))
//        .withColumn("lead", lead("price", 1).over(rangeBetween))
        .show(false)


}
