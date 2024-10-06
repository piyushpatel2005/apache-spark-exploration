# Spark Data Sources

This tutorial is going to cover the concept of reading data from different data sources in Spark.

## Data Sources

Spark can read data from multiple data sources. Some of the common data sources are:

- CSV
- JSON
- Parquet
- ORC
- Avro
- Sequence Files
- Text Files
- JDBC
- Protocol Buffers

Spark can also read and write from and to different other data sources using connectors. Some of the common connectors are Cassandra, BigQuery, Snowflake, MongoDB, various cloud storage systems across major cloud providers.

## CSV Source

For reading data from CSV files, you can use the `spark.read.csv` method which is very convenient method to read data from CSV files.

```scala
val df = spark.read
    .option("header", "true")
    .option("inferSchema", "true")
    .csv("src/main/resources/data/retail/sales_dataset.csv")
```

You also saw various options with CSV data source in the previous lesson.

There is also convenient method common for most data formats using `load` method. The difference being that you need to specify `format` in this method and the path of the files is specified in the `load` method.

```scala
val df = spark.read
    .format("csv")
    .option("header", "true")
    .option("inferSchema", "true")
    .load("src/main/resources/data/retail/sales_dataset.csv")
```

To **write data** in CSV format, you can use the `write` method with the `csv` format.

```scala
df.write
    .mode(SaveMode.Overwrite)
    .option("header", "true")
    .csv("src/main/resources/warehouse/rows_csv/")
```

Alternatively, you can use the `save` method with the `format` method.

```scala
df.write
    .mode(SaveMode.Overwrite)
    .format("csv")
    .option("header", "true")
    .save("src/main/resources/warehouse/rows_csv/")
```

## JSON

To read data from in the JSON format, you can use the `spark.read.json` method.

```scala
val df = spark.read
    .json("src/main/resources/data/ecommerce/products.json")
```

Again, just like `csv`, you can use the `load` method to read JSON data.

```scala
val df = spark.read
    .format("json")
    .load("src/main/resources/data/ecommerce/products.json")
```

You could specify the schema of JSON data using the `schema` method. For example, the schema of the JSON data in the `products.json` file is as follows:

```json
{
    "id": 1,
    "title": "Some Product",
    "price": 100.0,
    "description": "Some description",
    "category": "Some category",
    "image": "Some image",
    "rating": {
        "rate": 4.5,
        "count": 100
    }
}
```

This schema can be defined as follows:

```scala
val productsSchema = StructType(
        Array(
            StructField("id", IntegerType),
            StructField("title", StringType),
            StructField("price", DoubleType),
            StructField("description", DoubleType),
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
```

While reading this data, you could specify the schema of the data as follows.

```scala
val df = spark.read
    .schema(productsSchema)
    .json("src/main/resources/data/ecommerce/products.json")
```

You could also specify multiple options as a `Map` to read JSON data. Just for example, you could pass options as shown below with `options` method.

```scala
val df = spark.read
    .format("json")
    .options(Map(
        "multiLine" -> "true",
        "mode" -> "PERMISSIVE",
        "columnNameOfCorruptRecord" -> "_corrupt_record",
        "lineSep" -> "\n",
        "dropFieldIfAllNull" -> "false"
    ))
    .load("src/main/resources/data/ecommerce/products.json")
```

To write data as JSON, you can use the `write` method with the `json` convenient method or you could use `save` with `format` method.

```scala
df.write
    .mode(SaveMode.Overwrite)
    .json("src/main/resources/warehouse/rows_json/")

// OR

df.write
    .mode(SaveMode.Overwrite)
    .format("json")
    .save("src/main/resources/warehouse/rows_json/")
```

Now, just like CSV, you can also specify various options while reading JSON data.

| Option | Description                                                                                                                                                                                                                                                                                                                    | Scope      | Default |
| --- |--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|------------| --- |
| `multiLine` | Whether to allow reading files in multiple lines                                                                                                                                                                                                                                                                               | read       | `false` |
| `mode` | Allows a mode for dealing with corrupt records during parsing. This can have these value. `PERMISSIVE` which puts malformed data into a field configured by `columnNameOfCorruptRecord` option. `DROPMALFORMED` will ignore the malformed data. `FAILFAST` will throw an exception when it finds any malformed record in JSON. | read       | `PERMISSIVE` |
| `columnNameOfCorruptRecord` | The name of the column that will be used for storing the malformed records.                                                                                                                                                                                                                                                    | read       | `_corrupt_record` |
| `dateFormat` | The format of the date columns in the JSON data.                                                                                                                                                                                                                                                                               | read/write | `yyyy-MM-dd` |
| `timestampFormat` | The format of the timestamp columns in the JSON data.                                                                                                                                                                                                                                                                         | read/write | `yyyy-MM-dd'T'HH:mm:ss.SSSXXX` |
| `inferSchema` | Whether to infer the schema of the JSON data.                                                                                                                                                                                                                                                                                  | read       | `false` |
| `primitivesAsString` | Whether to infer all primitive values as a string type.                                                                                                                                                                                                                                                                       | read       | `false` |
| `encoding` | The encoding of the JSON data.                                                                                                                                                                                                                                                                                                 | read/write | `UTF-8` |
| `lineSep` | The line separator of the JSON data.                                                                                                                                                                                                                                                                                          | read/write | `"\n"` |
| `dropFieldIfAllNull` | Whether to ignore column of all null values or empty array.                                                                                                                                                                                                                                                                   | read | `false` |
| `allowSingleQuotes` | Whether to allow single quotes in addition to double quotes.                                                                                                                                                                                                                                                                  | read | `true` |
| `allowComments` | Whether to allow comments in JSON data.                                                                                                                                                                                                                                                                                       | read | `false` |
| `allowUnquotedFieldNames` | Whether to allow unquoted field names in JSON data.                                                                                                                                                                                                                                                                           | read | `false` |
| `allowBackslashEscapingAnyCharacter` | Whether to allow backslash escaping any character.                                                                                                                                                                                                                                                                           | read | `false` |

To find all the available options for JSON Datasource, refer to official [Spark documentation](https://spark.apache.org/docs/latest/sql-data-sources-json.html#data-source-option).

## Parquet

Parquet is a columnar storage format which is very efficient for reading data. To read data from Parquet files, you can use the `spark.read.parquet` method. Parquet is a binary data format which includes the schema of the data.

```scala
val df = spark.read
    .parquet("src/main/resources/data/retail/sales_dataset.parquet")
```

If you want to read only selected fields, you can explicitly specify the schema of the fields you want to read.

## ORC

ORC is another columnar storage format which is very efficient for reading data. To read data from ORC files, you can use the `spark.read.orc` method. Again, this data type also includes the schema of the data in the file itself like Parquet format.

```scala
val df = spark.read
    .orc("src/main/resources/data/retail/sales_dataset.orc")
```

## Text Files



## Avro

Avro is a row-based storage format which is very efficient for reading data. To read data from Avro files, you can use the `spark.read.format("avro")` method.

```scala
val df = spark.read
    .format("avro")
    .load("src/main/resources/data/retail/sales_dataset.avro")
```

## JDBC

To read data from a JDBC source, you can use the `spark.read.jdbc` method. You need to specify the JDBC URL, table name, and other options like `user`, `password`, etc.

```scala
val df = spark.read
    .format("jdbc")
    .option("url", "jdbc:postgresql://localhost:5432/ecommerce")
    .option("dbtable", "public.products")
    .option("user", "postgres")
    .option("password", "password")
    .load()
```

