# Overview of Databricks

Databricks is a cloud software service developed by the team behind Apache Spark, that provides a collaborative
environment for data scientists, data engineers, and business analysts. It is designed to be easy to use and to
support data analytics and machine learning workflows. It provides a managed Apache Spark cluster and also supports
interactive notebooks using languages such as Python, Scala, R, and SQL.

## Brief Overview of Apache Spark

Apache Spark is a data analytics engine designed to process huge volumes of data quickly and efficiently. The data
processing is basically handled in-memory for very fast processing times. Spark was started as a research project at Berkeley's AMPLab in 2009, and was open sourced in 2010.

Spark can be used to perform batch processing and real-time processing of data, and it can be integrated with other
big data ecosystems such as Hadoop, HBase, Cassandra and other NoSQL databases. It provides high level APIs using
data structures like RDDs, DataFrames, and Datasets. Spark also provides various language bindings for Python, Scala,
Java and R. Some of the example use cases of Spark are below.

- ETL (Extract, Transform, Load) operations
- Data processing and analytics
- Machine learning
- Real-time stream processing

These can be used for various applications like understanding customer behavior in real-time, predicting churn rates
in a business, fraud detection by identifying various customer behvaiors, etc. Most data science applications are
backed by vast amounts of data and Spark can be a great tool for ingesting those vast amounts of data and
transforming them into useful structure for machine learning models.

## Features of Databricks
Databricks provides several features that make it a powerful tool for working with Spark.

- Databricks promotes collaboration between data scientists, data engineers, and business analysts.
- Databricks provides a unified analytics platform that supports data engineering, data science, and business analytics.
- Databricks provides a managed Apache Spark cluster that can be easily scaled up or down based on the workload.
- Databricks supports interactive notebooks that allow users to write and execute code in languages such as Python, Scala, R, and SQL.
- It provides a rich set of libraries and tools for data analytics and machine learning, including MLflow, Delta Lake,
  and Koalas.
- Databricks provides a secure and compliant environment for data workloads.
- Databricks integrates with popular data sources and data storage systems, such as AWS S3, Azure Data Lake Storage, and
  Google Cloud Storage.
- Databricks provides the ability to schedule and automate data workflows using Jobs.
- It also provides ability to start a cluster on-demand and shut it down when not in use.

## What is Azure Databricks

These set of tutorials focus more on Databricks functionality for spark development. If you are completely new to
Spark, please first refer to [Spark Tutorials](../../spark/). These set of
tutorials
focus on
Azure Databricks but it
may be similar with other Cloud providers. With Azure Databricks, Databricks will be integrated with Azure services.
When you create a Databricks workspace in Azure, the compute and storage resources are deployed in your Azure
subscription. You can also use Azure Active Directory to manage access to your Databricks workspace. Basically Azure
will provide the underlying infrastructure and Databricks will provide the Spark environment.

### Azure Databricks Architecture

At a high level, Azure Databricks architecture has two components.
1. **Control Plane**: This is the Azure Databricks control plane, which manages the Databricks workspace and provides
   access to the Databricks web application.
2. **Compute Plane**: This is the Apache Spark compute plane where data is processed. This can again use classic
   Azure compute or serverless compute.

![High level architecture of Azure Databricks](./architecture-azure-databricks.png "Azure Databricks
Architecture - Source: Microsoft")

When you create a workspace, Azure Databricks also creates an account in Azure subscription called workspace storage
account.
1. *DBFS*: This account contains DBFS (Databricks File System) which is distributed file system that uses Azure Blob
storage as the underlying storage. This is where the notebooks, libraries, and data reside. It can be accessed using
`dbfs:/` namespace. Storing and accessing data using DBFS root or DBFS mount points is the recommended way to access
data. It's recommended to use Unity catalog volumes.
2. **Unity Catalog**: This is a metadata catalog that stores metadata about databases, tables, and views. It is used by
   Databricks SQL Analytics and Databricks Machine Learning to store metadata about data objects. All users in
   workspace can create assets in the default schema in the catalog.
3. **System data**: This is the system data that is used by Databricks to store metadata about the workspace and
   notebooks. This is not accessible to users. It includes notebook revisions, job information, spark logs, etc.


