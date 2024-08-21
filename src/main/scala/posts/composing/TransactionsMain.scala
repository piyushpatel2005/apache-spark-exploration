package posts.composing

import org.apache.spark.sql.{Column, DataFrame}
import utils.SparkSessionWrapper
import org.apache.spark.sql.functions._

/**
 * More examples at kb.databricks.com/data/chained-transformations
 */
object TransactionsMain extends SparkSessionWrapper {
    import spark.implicits._

    type Transform = DataFrame => DataFrame

    val data = Seq(
        Transaction("paid by A", 499.99, java.sql.Timestamp.valueOf("2021-01-01 00:00:00")),
        Transaction("paid by C", 399.99, java.sql.Timestamp.valueOf("2021-01-02 00:00:00")),
        Transaction("paid by B", 499.99, java.sql.Timestamp.valueOf("2021-01-03 00:00:00")),
        Transaction("paid by B", 120.99, java.sql.Timestamp.valueOf("2021-01-01 00:00:00")),
        Transaction("paid by A", 150.99, java.sql.Timestamp.valueOf("2021-01-02 00:00:00")),
        Transaction("paid by A", 120.99, java.sql.Timestamp.valueOf("2021-01-03 00:00:00")),
        Transaction("paid by A", 100.99, java.sql.Timestamp.valueOf("2021-01-01 00:00:00")),
        Transaction("paid by C", 99.99, java.sql.Timestamp.valueOf("2021-01-01 00:00:00")),
        Transaction("paid by A", 49.99, java.sql.Timestamp.valueOf("2021-01-02 00:00:00")),
        Transaction("paid by B", 79.99, java.sql.Timestamp.valueOf("2021-01-03 00:00:00")),
        Transaction("paid by B", 1000.99, java.sql.Timestamp.valueOf("2021-01-01 00:00:00")),
        Transaction("paid by B", 1200.99, java.sql.Timestamp.valueOf("2021-01-02 00:00:00")),
        Transaction("paid by C", 500.99, java.sql.Timestamp.valueOf("2021-01-03 00:00:00")),
        Transaction("paid by A", 700.99, java.sql.Timestamp.valueOf("2021-01-01 00:00:00")),
        Transaction("paid by A", 999.99, java.sql.Timestamp.valueOf("2021-01-01 00:00:00")),
        Transaction("paid by B", 899.99, java.sql.Timestamp.valueOf("2021-01-02 00:00:00")),
        Transaction("paid by A", 799.99, java.sql.Timestamp.valueOf("2021-01-03 00:00:00"))
    )

    val transactionsDf = data.toDF()

    def sumAmounts(by: Column*): Transform = df => {
        df.groupBy(by: _*).agg(sum("amount").as("total_amount"))
    }

    def extractPayerBeneficiary(columnName: String): Transform = df => {
        df.withColumn(s"${columnName}_payer", regexp_extract(col(columnName), "paid by (\\w)", 1))
          .withColumn(s"${columnName}_beneficiary", regexp_extract(col(columnName), "paid by (\\w)", 1))
    }

    transactionsDf
        .transform(extractPayerBeneficiary("details"))
        .transform(sumAmounts(date_trunc("day", $"ts"), col("details_payer")))
        .show(false)

    transactionsDf
        .transform(extractPayerBeneficiary("details") andThen sumAmounts(date_trunc("day", $"ts"), $"details_payer"))
        .show(false)

    transactionsDf
        .transform(sumAmounts(date_trunc("day", $"ts"), $"details_payer") compose extractPayerBeneficiary("details"))
        .show(false)

    transactionsDf
        .transform(Function.chain(
            Seq(
                extractPayerBeneficiary("details"),
                sumAmounts(date_trunc("day", $"ts"), $"details_payer")
            )
        ))
        .show(false)

}
