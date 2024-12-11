# Databricks User Interface

This tutorial focuses on features of Databricks UI that can be used to develop Spark applications in interactive
fashion. It will cover concepts like workspace, notebooks, clusters, libraries, jobs, and SQL Analytics.

Once you've logged into the Databricks instance using your credentials, you will be presented with the Databricks
web interface. The interface is divided into several sections, each of which serves a specific purpose. The main
sections are as follows:

- Workspace
- Notebooks
- Clusters
- Libraries
- Jobs
- SQL Analytics
- Machine Learning
- Data
- Models
- Admin Console
- Account Settings
- Help
- Notifications
- Search
- User Profile


## Workspace

A workspace is an environment for interacting with Databricks assets. The workspace organizes assets into different
folders.
- **Dashboard**: The dashboard provides an overview of the workspace, including recent activities, pinned items, and
  workspace settings.
- **Notebooks**: Notebooks are interactive documents that contain code, visualizations, and narrative text. Notebooks
  can be used to develop and run Spark applications.
- **Libraries**: Libraries are packages that contain code and dependencies that can be used in notebooks. Libraries can
  be attached to clusters to make them available for use. You can import libraries from Maven, PyPI, or upload them
  for use in your workspace.
- **Data**: Data contains data assets that can be used in notebooks. Data can be uploaded to the workspace or
  connected to external data sources.
- **Jobs**: Jobs allow us to schedule workloads on schedule or on ad-hoc basis.
- **Experiment**: Experiments are used to track and compare the performance of machine learning models. Experiments
  can be used to evaluate different models and hyperparameters.

## Data

- **Databricks File System (DBFS)**: DBFS is a distributed file system that is used to store data and notebooks in
  Databricks. DBFS is built on top of cloud storage services like Azure Blob Storage, AWS S3, and Google Cloud Storage.
  You can access DBFS using the `dbfs:/` namespace.
- **Database**: Databases are used to organize tables and views in Databricks. You can create databases to store
  related data assets and query them using SQL.
- **Tables**: Tables are structured data representations that can be queried using SQL. Tables can be created from
  data stored in DBFS or external data sources.
- **Metastore**: Metastore is a metadata catalog that stores information about databases, tables, and views in
  Databricks. Metastore is used by SQL Analytics and Machine Learning to store metadata about data objects. You can
  configure Metastore as a Hive metastore or a Delta Lake metastore.

## Compute

- **Clusters**: Clusters are group of virtual machines that are used to run Spark jobs in Databricks. Clusters can be
  created
  with different configurations based on the workload requirements from Databricks UI. Once created, you can attach
  the cluster to the notebook in order to process the data. The cluster can be all-purpose or job-specific.
  All-purpose cluster can be used for interactive querying and development. Job-specific clusters are used for
  running daily jobs or scheduled jobs. It is highly recommended to use job-specific clusters for production jobs as
  it is ephemeral in nature and is cost-effective.
- **Pool**: Pools are a collection of idle instances that can be used to run Spark jobs. It allows us to warmed up
  instances which can be used with existing clusters or new clusters. This can be used to reduce the startup time of
  the clusters by pre-allocating instances. Pools can be shared across multiple clusters and can be used to run jobs.
  If the pool doesn't have enough instances, it will automatically scale up to meet the demand. When any cluster is
  terminated, the instances are returned to the pool which can later be used for other clusters.
- **Jobs**: Jobs are scheduled workflows that run notebooks or scripts at specified intervals. Jobs can be used to
  automate data processing tasks. You can create jobs from the Jobs tab in the workspace. You can schedule the job to
  run at a specific time or trigger it based on an event. You can also configure the job to send notifications on
  success or failure.
- **Databricks Runtime**: These are set of core components that run on the clusters managed by Databricks. These
  include several runtimes. Runtimes allow you to select the version of Apache Spark, Scala, Python, and other
  libraries or GPU support. There are two types of runtimes: Databricks Runtime and Databricks Runtime ML.
  Databricks Runtime is used for general-purpose computing and Databricks Runtime ML is optimized for machine
  learning and can be used for training and deploying machine learning models.
