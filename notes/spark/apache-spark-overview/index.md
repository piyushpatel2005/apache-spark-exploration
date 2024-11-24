# Apache Spark Overview

Apache spark is an in-memory data processing engin that provides speed and scalability while allowing simplified programming. It is open-source and free to use. This lesson provides brief idea on this technology.

## What is Apache Spark?

Apache Spark is a fast data processing engine. Before Spark, the big data processing was performed using MapReduce on Hadoop clusters. However, MapReduce being based on FileIO was relatively slow. Spark began as a research project at UC Berkeley's AMPLabs in 2009 and was open-sourced next year. Since 2014, Spark was designated as top-level project by Apache Software Foundation. Apache Spark is written in Scala and provides a unified programming interface with support for languages like Scala, Java, Python, R and SQL. Spark supports multiple cluster managers. It can run as standalone to run locally. It can also run on YARN which is a resource manager in Hadoop clusters. Spark can also run on Kubernetes.

### Features of Spark
Some of the main features of Apache Spark are mentioned below.
- **Speed:** Apache Spark uses in-memory data structures in order to process data. This reduces disk IO at intermediate stages unlike MapReduce framework. This can also be a drawback of Spark because it means Spark will require lot more RAM in order to perform data processing.
- **Support for Multiple Languages:** Spark has programming API with multiple langugaes. It supports languages like Scala, Java, Python, R making it flexible for programmers with different skills and background.
- **Scalability:** Apache Spark can distribute the workload across multiple workers in a cluster of machines.
- **Unified Processing:** Spark handles large datasets efficiently. With new Structured Streaming, it provides unified processing for Batch and real-time processing of big data.

## Use cases for Spark

- **Real-time Analytics:** Spark can be used to process data in near real-time. It provides dataframe API for Spark streaming which enables applications like fraud detection, sentiment analysis, etc.
- **Batch Processing:** For batch processing, Spark can easily handle large volumes of data. It distributes the data processing across multiple worker machines and processing data in parallel providing efficient processing of big data.
- **Machine Learning:** Spark has a module MLLib which allows you to train machine learning model on massive datasets. This can be used to build applications like recommendation engine.
- **Graph Processing:** Spark can also handle complex graph processing using GraphX library of Spark. This allows to build social graphs of a person using their connections.
- **Data Exploration:** Spark empowers data scientists to explore and analyze data interactively, helping them identify patterns and trends. Spark allows allows exploratory data analysis using SQL on existing datasets which is convenient for data scientists and data analysts.
