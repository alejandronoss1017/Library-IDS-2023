# 📚 Library IDS-2023

## Index

- [Overall](./README.md#overall)
- [Tech stack](./README.md#tech-stack)
- [Environment set-up](./README.md#environment-set-up)

  - [Java JDK](./README.md#java-jdk)
  - [Maven](./README.md#maven)
  - [MySQL](./README.md#mysql)

- [Replication configuration](./README.md#replication-configuration)

  - [Master-Slave configuration](./README.md#master-slave-configuration)
  - [Slave-Master configuration](./README.md#slave-master-configuration)

- [Client](./server/README.md)
- [Server](./client/README.md)
- [Authors](./README.md#authors)

# 🗞️ Overall

This project aims to put into practice various communication concepts and patterns found in today's distributed systems.

This project is divided into 2 parts, one being the server and the other the client, the client component must make requests to the server through network protocols (**TCP**) and the server responds to the requests.

# 📶 Tech stack

Since this project is divided into two completely different components, the dependencies will vary, but the technologies used for the development of both are the same.

- [Java 17 LTS](https://www.oracle.com/java/technologies/javase/jdk17-archive-downloads.html)

- [Maven](https://maven.apache.org/)

- [Jeromq](https://zeromq.org/)

- [MySQL](https://www.mysql.com/)

# 💻 Environment set-up

In order for the program to work correctly, you first have to set the environment variables as follows:

## <img align="center" height="60" width="40" src="https://cdn.jsdelivr.net/gh/devicons/devicon/icons/java/java-original.svg" /> Java JDK

Java is a widely used programming language known for its versatility, portability, and robustness.

Java is an object-oriented programming language, which means it emphasizes the concept of objects and classes. It provides a rich set of APIs (Application Programming Interfaces) that developers can use to build a wide range of applications, from desktop software to web applications and mobile apps.

### <img align="center" height="60" width="40" src="https://cdn.jsdelivr.net/gh/devicons/devicon/icons/windows8/windows8-original.svg" /> Windows

1. Download and install **JDK 17** for Windows from the official Oracle website.

2. Once the installation is complete, open the Start Menu and search for "Environment Variables".

3. Click on the "Edit the system environment variables" option. This will open the System Properties window.

4. In the System Properties window, click on the "Environment Variables" button at the bottom.

5. In the Environment Variables window, under the "System variables" section, click on the "New" button.

6. Set the following values for the new environment variable:

   - Variable name: `JAVA_HOME`
   - Variable value: The installation path of **JDK 17** (e.g., `C:\Program Files\Java\jdk-17`)

7. Click "OK" to save the environment variable.

8. In the Environment Variables window, under the "System variables" section, scroll down to find the "Path" variable and click on the "Edit" button.

9. In the Edit Environment Variable window, click on the "New" button and add the following value:

   - `%JAVA_HOME%\bin`

10. Click "OK" to save the changes.

11. Open a new command prompt window and type `java -version`. It should display the Java version as **JDK 17**.

### <img align="center" height="60" width="40" src="https://cdn.jsdelivr.net/gh/devicons/devicon/icons/linux/linux-original.svg" /> Linux

You could use the repositories from your distro (**OpenJDK**) or installing a package from the Oracle website.

**Repository:**

```bash
    sudo apt install openjdk-17-jdk
```

**Oracle website:**

1. Download and install **JDK 17** for Linux from the official Oracle website.

2. Once the installation is complete, open a terminal.

3. Open the .bashrc file in a text editor using the following command:

```bash
    nano ~/.bashrc
```

4. At the end of the file, add the following line:

```bash
    export JAVA_HOME=/usr/lib/jvm/jdk-17
```

Replace `/usr/lib/jvm/jdk-17` with the actual installation path of **JDK 17**.

5. Save the file by pressing Ctrl + O, then press Enter. Exit the text editor by pressing Ctrl + X.

6. In the terminal, run the following command to apply the changes:

```bash
    source ~/.bashrc
```

7. Type `java --version` in the terminal. It should display the Java version as **JDK 17**.

## <img align="center" height="60" width="60" src="https://cdn.jsdelivr.net/gh/devicons/devicon/icons/apache/apache-original.svg" /> Maven

Maven is a build automation and project management tool for Java-based projects. It provides a comprehensive set of features and conventions to help streamline the development process and simplify project management.

Maven simplifies dependency management by automatically resolving and downloading project dependencies from remote repositories. It also supports dependency version management to ensure consistent and predictable builds.

### <img align="center" height="60" width="40" src="https://cdn.jsdelivr.net/gh/devicons/devicon/icons/windows8/windows8-original.svg" /> Windows

1. Download Maven from the [official website](https://maven.apache.org/download.cgi).

2. Extract the downloaded zip file to a directory of your choice (e.g., `C:\Program Files\Maven`).

3. Open the **Start Menu** and search for **"Environment Variables"**.

4. Click on the **"Edit the system environment variables"** option.

5. In the System Properties window, click on the **"Environment Variables"** button at the bottom.

6. Add a new system variable with the following values:

   - **Variable name:** `M2_HOME`
   - **Variable value:** The path to the Maven installation directory (e.g., `C:\Program Files\Maven`).

7. Edit the **"Path"** variable and add `%M2_HOME%\bin` to the list of paths.

8. Open a new command prompt window and type `mvn -version` to verify the installation.

### <img align="center" height="60" width="40" src="https://cdn.jsdelivr.net/gh/devicons/devicon/icons/linux/linux-original.svg" /> Linux

1. Install Maven using the package manager of your Linux distribution.

```bash
    sudo apt install maven
```

2. Open a terminal and verify the installation.

```bash
    mvn -version
```

## <img align="center" height="65" width="65" src="https://cdn.jsdelivr.net/gh/devicons/devicon/icons/mysql/mysql-original-wordmark.svg" /> MySQL

MySQL is an open-source relational database management system (RDBMS) that is widely used for storing and managing structured data. It provides a reliable, scalable, and efficient solution for managing databases in various applications.

MySQL offers features such as replication, which enables data to be replicated across multiple database servers for improved scalability and high availability. It also provides robust security measures, including user authentication, encryption, and access control, to protect sensitive data.

### <img align="center" height="60" width="40" src="https://cdn.jsdelivr.net/gh/devicons/devicon/icons/windows8/windows8-original.svg" /> Windows

1. Download the MySQL Installer for Windows from the [official website](https://dev.mysql.com/downloads/installer/).

2. Run the downloaded installer and follow the on-screen instructions.

   - Select "Developer Default" or "Custom" installation type.

3. Set a root password for the MySQL server during the installation.

   - You will be prompted to set a root password for the MySQL server. Choose a strong password and remember it.

4. Open a command prompt or MySQL Command Line Client and type `mysql -V` to verify the installation.

### <img align="center" height="60" width="40" src="https://cdn.jsdelivr.net/gh/devicons/devicon/icons/linux/linux-original.svg" /> Linux

1. Open a terminal.

2. Run the command `sudo apt update` to update the package lists.

3. Install the MySQL server package by running `sudo apt install mysql-server`.

4. Set a root password for the MySQL server during the installation.

5. Open a terminal and type `mysql -V` to verify the installation.

# 🔄 Replication configuration

For the development of this service a bidirectional replication scheme was implemented, based on the model (**MASTER-SLAVE**), where both databases are slaves and masters at the same time, which allows that any change made in any of the two instances will be reflected in the other, MYSQL offers this replication option, it is only necessary to configure the instances.

## Master-Slave configuration

1. Open the MySQL configuration file on the source server. The path to this file varies depending on the operating system:

**Windows**: C:\ProgramData\MySQL\MySQL Server x.x\my.ini
**Linux**: /etc/mysql/mysql.conf.d/mysqld.cnf

2. Locate the [mysqld] section inside the configuration file.

3. Add the following lines to enable binary logging and set a unique server ID:

```bash
log-bin=mysql-bin
server-id=1
```

4. Save the configuration file and restart the MySQL service.

5. Log in to the Master DB and create a user with replication privileges:

```bash
CREATE USER 'replication_user'@'%' IDENTIFIED BY 'password';
GRANT REPLICATION SLAVE ON *.* TO 'replication_user'@'%';
FLUSH PRIVILEGES;
```

6. Run the following SQL command to retrieve the binary log coordinates:

```bash
SHOW MASTER STATUS;
```

> Note down the values of the File and Position columns, as we'll need them in the next step.

## Slave-Master configuration

7. On the Slave DB, open the configuration file and add the following lines in the [mysqld] section:

```bash
log-bin=mysql-bin
server-id=2
```

8. Save the configuration file and restart the MySQL service.

9. Access the slave DB and run the following command to configure replication:

```bash
CHANGE MASTER TO
MASTER_HOST='serverA_IP',
MASTER_USER='replication_user',
MASTER_PASSWORD='password',
MASTER_LOG_FILE='mysql-bin.XXXXXX',
MASTER_LOG_POS=XXXXXX;
SOURCE_SSL=1;
```

> Put the values from the step 4

10. Starts replication on slave DB:

```bash
START SLAVE;
```

11. Repeat steps 2 and 3, reversing the roles of the Master and Slave DB's. Now Slave will become the master and Master will be the Slave.

With the above steps a bidirectional replication will be obtained.

# ✈️ Client

Check the [readme](./client/README.md) inside the client folder of the project.

# 🏗️ Server

Check the [readme](./server/README.md) inside the server folder of the project.

# ✏️ Authors

- [Camilo Alejandro Nossa](https://github.com/alejandronoss1017)
- [Stiven Ortiz Noreña](https://github.com/StiivenOrtiz)
- [Jorge Camilo Chantryt](https://github.com/cchantryt)
- [Jesus Daniel Molina](https://github.com/jesusdme)
