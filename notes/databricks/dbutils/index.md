# Databricks DBUtils

`dbutils` is a Databricks utility that allows you to read/write files, mount storage, and manage secrets. It is a powerful tool that can be used to interact with the underlying file system and other Databricks services.

This tutorial will cover some of the important commands from `dbutils` that can be useful in Databricks environment.

## Getting Help

If you don't remember any of the commands, you can refer to the help documentation for `dbutils` by using the `help` command.

```python
dbutils.help()
```

This will list down the available commands with little information about those.

- `dbutils.fs` - File system utilities for interacting with DBFS.
- `dbutils.notebook` - Utilities for control flow of a notebook. This can be used to run another notebook or exit the notebook on specific condition.
- `dbutils.widgets` - Utilities for creating widgets in a notebook. This can be used to pass parameters to a notebook.
- `dbutils.secrets` - This provides utilities for interacting with secrets withing notebooks.
- `dbutils.library` - This is used for session isolated libraries.
- `dbutils.credentials` - This is used for managing credentials in a notebook.

## File System Utilities

- `dbutils.fs.help()` will provide the help documentations for the file system utilities.
- `dbutils.fs.cp(from, to, recursive): boolean` - Copy files from source to destination. If `recursive` is set to true, it will copy the files recursively.
- `dbutils.fs.ls(path): Seq` - List the files in the given path.
- `dbutils.fs.mkdirs(path): boolean` - Create directories in the given path.
- `dbutils.fs.rm(path, recursive): boolean` - Remove files from the given path. If `recursive` is set to true, it will remove the files recursively.
- `dbutils.fs.head(path, maxBytes): String` - Read the first `maxBytes` from the file. The default values for `maxBytes` is 65536.
- `dbutils.fs.put(path, contents, overwrite): boolean` - Write the contents to the file. If `overwrite` is set to true, it will overwrite the file if it already exists.

```python
dbutils.fs.help()
dbutils.fs.mkdirs("/FileStore/tables/archive")
dbutils.fs.ls("/FileStore/tables")
dbutils.fs.put("/FileStore/tables/archive/test.txt", "Hello Databricks", true) # Write file
dbutils.fs.head("/FileStore/tables/archive/test.txt", 100) # Read first 100 bytes
dbutils.fs.rm("/FileStore/tables/archive/test.txt", true) # Remove file
dbutils.fs.cp("/FileStore/tables/archive/test.txt", "/FileStore/tables/archive/test2.txt", true) # Copy files
dbutils.fs.head("/FileStore/tables/archive/test2.txt", 100) # Read first 100 bytes
```

## Notebook Utilities

- `dbutils.notebook.help()` will provide the help documentations for the notebook utilities.
- `dbutils.notebook.exit(value: String): void` - Exit the notebook with the given value. This can be useful when you want to prematurely exit with error or with success.
- `dbutils.notebook.run(path, timeoutSeconds, arguments): String` - Run another notebook with the given path. You can also pass the arguments to the notebook.

```python
dbutils.notebook.help()
dbutils.notebook.exit("Notebook exited successfully")
dbutils.notebook.run("/Users/username/notebook", 60, {"param1": "value1", "param2": "value2"})
```

## Library Utilities

- `dbutils.library.help()` will provide the help documentations for the library utilities.
- `dbutils.library.installPyPI(packages, repo, clusterId): void` - Install the Python packages from PyPI repository to the cluster with the given cluster ID.
- `dbutils.library.installMaven(coordinates, repo, clusterId): void` - Install the Maven packages from Maven repository to the cluster with the given cluster ID.
- `dbutils.library.list(): Seq` - List the libraries installed in the cluster.
- `dbutils.library.restartPython(): void` - Restart the Python interpreter.

```python
dbutils.library.help()
dbutils.library.installPyPI("pandas")
dbutils.library.restartPython()
dbutils.library.list()
dbutils.library.installMaven("org.apache.spark:spark-sql_2.12:3.1.2")
```

The libraries installed with these commands are installed at session level and it will not be available in another notebook.

## Widgets Utilities

- `dbutils.widgets.help()` will provide the help documentations for the widgets utilities. You can create different types of widgets and get their values.
- `dbutils.widgets.get(name): String` - Get the value of the widget with the given name.
- `dbutils.widgets.text(name, defaultValue, label): void` - Create a text widget with the given name, default value, and label.
- `dbutils.widgets.removeAll()` - Remove all the widgets from the notebook.
- `dbutils.widgets.remove(name): void` - Remove the widget with the given name from the notebook.
- `dbutils.widgets.multiselect(name, defaultValue, options, label): void` - Create a multiselect widget with the given name, default value, options, and label. This can be used to select multiple values from the `options`. When you retrieve these values, it will be a comma separated string.
- `dbutils.widgets.dropdown(name, defaultValue, options, label): void` - Create a dropdown widget with the given name, default value, options, and label. This can be used to select one value from the `options`.

```python
dbutils.widgets.help()
dbutils.widgets.text("name", "John Doe", "Enter your name")
dbutils.widgets.get("name") // Get the value of the widget
dbutils.widgets.removeAll() // Remove all widgets
dbutils.widgets.multiselect("process_date", "2024-12-01", Seq("2024-12-01", "2024-12-02", "2024-12-03"), "Select options")
dbutils.widgets.get("process_date") # Get the value of the widget
```

While executing a notebook from another notebook, you can pass these values as arguments to the notebook.

```python
%run /Users/username/DemoNotebook $process_date='2024-12-01' $name='John Doe'
dbutils.notebook.run("/Users/username/DemoNotebook", 60, {"process_date": "2024-12-01", "name":"John Doe"})
```

In above code, `process_date` will be passed as `2024-12-01` and `name` will be passed as `John Doe` to the notebook `DemoNotebook`.

## Secrets Utilities

- `dbutils.secrets.help()` will provide the help documentations for the secrets utilities.
- `dbutils.secrets.get(scope, key): String` - Get the secret value from the given scope and key.
- `dbutils.secrets.listScopes(): Seq` - List the available scopes.
