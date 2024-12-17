# Reading Data

You can read data from multiple different services in Azure using Databricks notebooks. For this, you need to store the data in a storage account and then read the data from the storage account. In order to access the data, you need to have the necessary permissions to access the storage account. You could also store credentials in Azure Key Vault and access the credentials from the Databricks notebook using secrets.

When working with Azure Blob storage, you can use different protocols to connect to the storage account. Blob storage is essentially a flat storage structure that is organized into containers. You can access files using one of these three methods.

1. `https` - HTTPS is used to access individual files in the storage account and this method is normally used in web applications to access binary files like videos or images. The endpoint for this method is looks like `https://<storage-account>.blob.core.windows.net/<container-name>/<file-name>`.
2. `wasbs` - WASBS (Windows Azure Storage Blob Secure) is used to access files in the storage account using Hadoop File System API. This method is used in Big Data applications to access files in the storage account. Basically HDFS compatibility was provided using WASBS driver to perform the mapping of HDFS file system semantics into equivalent object storage in Azure Blob Storage. The endpoint for this method is looks like `wasbs://<container-name>@<storage-account>.blob.core.windows.net/<file-name>`. This was used in HDInsight Hadoop clusters to access blob storage data.
3. `abfss` - ABFSS (Azure Blob File System Secure) is used to access files in the storage account using Azure Databricks File System API. This method is used in Databricks notebooks to access files in the storage account. This is used with Azure Data Lake Storage Gen2 which provides hierarchical storage with ACL on files and folders. This is used to access blob storage in HDInsight clusters as well as Databricks notebooks. The endpoint for this method is looks like `abfss://<container-name>@<storage-account>.dfs.core.windows.net/<file-name>`.

## Mounting Storage Account
The mounting pattern for Databricks with Azure Blob storage was the default method in the past. That has been deprecated. [This document](https://docs.databricks.com/en/dbfs/mounts.html) provides reference to that mount pattern with AWS S3 and Azure Gen2 Blob Storage.
The legacy confiurations for mounting a storage account are as follows.

```python
dbutils.fs.mount(
  source = "wasbs://<container-name>@<storage-account>.blob.core.windows.net",
  mount_point = "/mnt/<mount-name>",
  extra_configs = {"<conf-key>":dbutils.secrets.get(scope = "<scope>", key = "<key>")})

spark.read.csv("/mnt/<mount-name>/<file-name>.csv")

dbutils.fs.mounts() # List the mounted storage accounts

dbutils.fs.unmount("/mnt/<mount-name>") # Unmount the storage account
```

## Access Using Secrets
The new pattern uses Spark configuration to access the data. This is how you can access Azure storage using Account key.

```python
spark.conf.set(
    "fs.azure.account.key.<storage-account>.dfs.core.windows.net",
    dbutils.secrets.get(scope="<scope>", key="<storage-account-access-key>")
)

spark.read.csv("wasbs://<container-name>@<storage-account>.blob.core.windows.net/<file-name>.csv")
```

Above configurations work only for the current session. If you detach cluster and restart the cluster, you need to set the configurations again.

## Access Using SAS Token

SAS is shared access signature that provides delegated access to resources in your storage account. You can generate SAS token for your storage account and use that to access the data. You can create SAS by navigating to Settings for the storage account in Azure portal. SAS token can be configured to provide access to specific resources for a specific time period.

```python
spark.conf.set("fs.azure.account.auth.type.<storage-account>.dfs.core.windows.net", "SAS")
spark.conf.set("fs.azure.sas.token.provider.type.<storage-account>.dfs.core.windows.net", "org.apache.hadoop.fs.azurebfs.sas.FixedSASTokenProvider")
spark.conf.set("fs.azure.sas.fixed.token.<storage-account>.dfs.core.windows.net", dbutils.secrets.get(scope="<scope>", key="<sas-token-key>"))

spark.read.csv("abfss://<container-name>@<storage-account>.dfs.core.windows.net/<file-name>.csv")
```

## Using App Registration

You can also use App Registration to access the storage account. App registration is a recommended mechanism for two Azure cloud applications to communitcate. You can create an App Registration in Azure Active Directory and assign necessary permissions to the App Registration to access the storage account. Next, you can store the application id as well as application credential secret in Azure Secrets. You can then use the App Registration to access the storage account.

```python
service_credential = dbutils.secrets.get(scope="<scope>",key="<service-credential-key>")

spark.conf.set("fs.azure.account.auth.type.<storage-account>.dfs.core.windows.net", "OAuth")
spark.conf.set("fs.azure.account.oauth.provider.type.<storage-account>.dfs.core.windows.net", "org.apache.hadoop.fs.azurebfs.oauth2.ClientCredsTokenProvider")
spark.conf.set("fs.azure.account.oauth2.client.id.<storage-account>.dfs.core.windows.net", "<application-id>")
spark.conf.set("fs.azure.account.oauth2.client.secret.<storage-account>.dfs.core.windows.net", service_credential)
spark.conf.set("fs.azure.account.oauth2.client.endpoint.<storage-account>.dfs.core.windows.net", "https://login.microsoftonline.com/<directory-id>/oauth2/token")

spark.read.csv("abfss://<container-name>@<storage-account>.dfs.core.windows.net/<file-name>.csv")
```

To mount using these app registration credentials, you can use below syntax. You can get `<application-id>`, `<service-credential-key>`, and `<directory-id>` from the App Registration in Azure Active Directory.

```python
configs = {
    "fs.azure.account.auth.type.<storage-account>.dfs.core.windows.net": "OAuth",
    "fs.azure.account.oauth.provider.type.<storage-account>.dfs.core.windows.net": "org.apache.hadoop.fs.azurebfs.oauth2.ClientCredsTokenProvider",
    "fs.azure.account.oauth2.client.id": dbutils.secrets.get(scope="<scope>", key="<application-id>"),
    "fs.azure.account.oauth2.client.secret": dbutils.secrets.get(scope="<scope>", key="<service-credential-key>"),
    "fs.azure.account.oauth2.client.endpoint": "https://login.microsoftonline.com/<directory-id>/oauth2/token"
}

dbutils.fs.mount(
    source = "abfss://<container-name>@<storage-account>.dfs.core.windows.net",
    mount_point = "/mnt/<mount-name>",
    extra_configs = configs
)

dbutils.fs.mounts() // List available mounts
spark.read.json("/mnt/<mount-name>/<dir_name>/<file-name>.json")
```
