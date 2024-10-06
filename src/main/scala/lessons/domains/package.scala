package lessons

import org.apache.spark.sql.types._

package object domains {
    val productsSchema = StructType(
        Array(
            StructField("id", IntegerType),
            StructField("title", StringType),
            StructField("price", DoubleType),
            StructField("description", StringType),
            StructField("category", StringType),
            StructField("image", StringType),
            StructField("rating",
                StructType(
                    Array(
                        StructField("rate", DoubleType),
                        StructField("count", IntegerType)
                    )
                )
            )
        )
    )

    val salesSchema: StructType = StructType(
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
}
