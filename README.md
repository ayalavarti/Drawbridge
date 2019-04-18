# Drawbridge Carpool Planner

## Setup and Run Configurations

### Init Script
To build the Java Maven project, run
```
mvn package
```
in the root directory.
Environment variables for the database username/password and Mapbox API key must be properly configured. A short init script was written to streamline this process. Run
```
./init <DB_USER> <DB_PASS>
```
to set environment variables and start the application on port 8000.

### Manual Setup
To manually set environment variables, run
```
export DB_USER=<USERNAME>
export DB_PASS=<PASSWORD>
export MAPBOX_KEY=<KEY>
```
in the root directory. Then run 
```
./run –gui --port <PORT>
```
to start the application on the provided port.

### PostgreSQL Setup
To set up a local database on postgre, download 
PostgreSQL11 from https://www.postgresql.org/download/.
Install PGAdmin4 from https://www.pgadmin.org/download/
as well. Open PGAdmin and create a new database named
"carpools".

Right click on the carpools database and select "restore".
Use the carpoolsDump.tar file to build the database. Open
Restore Options and select the following: "Only schema",
and all options under "do not save".

*In the carpools database, open the query tool (lightning
symbol), and run the following commands, consistent with
the environment variables set up earlier:*
```postgresql
CREATE USER <username> WITH PASSWORD '<password>';
GRANT ALL PRIVILEGES ON ALL TABLES IN SCHEMA public TO <username>;
GRANT USAGE, SELECT ON ALL SEQUENCES IN SCHEMA public TO <username>;
```
To restore data to your database, select "restore" and
use one of the data files (dummyDataDump, for example).
Under "Restore Options," select "Only data" and all options
under "do not save".

Maven will automatically install the JDBC driver needed 
to connect to the postgre database when "mvn package" is 
run, though it can be downloaded at 
https://jdbc.postgresql.org/download.html
and manually imported as an external jar file.

Recommended: For testing purposes, create 4 databases
in total:
* testCarpools - intended for testing interactions with
the database; data will be inserted and deleted
regularly. _Restore carpools schema only_
* carpools - intended to provide a sample set of 
data to display to the front end. 
_Restore carpools schema, then teamDataDump_
* searchTester - intended for specific data sets on
which path-finding algorithms will be tested. 
_Restore carpools schema, then testSearchData_
* massData - intended for testing speed on massive
sets of data. _Restore carpools schema, then massData_

(If you do not intend to run tests over the databases,
comment out all the test files in the database
package; alternatively, set up the urls
to your specific database structure)

*If you already have data existing data in the database
run the following command to clear your database 
before restoring data:*
```postgresql
TRUNCATE users, trips RESTART IDENTITY CASCADE;
```

