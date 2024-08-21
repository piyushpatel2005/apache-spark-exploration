package posts

import utils.SparkSessionWrapper

object NestedJson extends SparkSessionWrapper {
    val schema = "Name STRING, Age STRING, Account STRING, address STRUCT<city: STRING, state: STRING>"

    val df = spark.read.schema(schema).option("multiline", "true").json("src/main/resources/posts/nested.json")

    df.show(false)

    df.printSchema

    df.select("Name", "Age", "Account", "address.city", "address.state").show(false)
}
