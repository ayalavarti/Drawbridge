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

To restore data to your database, choose one of the .tar 
data files. 
(The dump file may restore referenced keys in the wrong order;
restoring the same data twice should correctly populate 
the database.)

*In the carpools database, open the query tool, and run
the following commands, consistent with
the environment variables set up earlier:*
```postgresql
CREATE USER <username> WITH PASSWORD '<password>';
GRANT ALL PRIVILEGES ON ALL TABLES IN SCHEMA public TO <username>;
GRANT USAGE, SELECT ON ALL SEQUENCES IN SCHEMA public TO <username>;
```

Maven will automatically install the JDBC driver needed 
to connect to the postgre database when "mvn package" is 
run, though it can be downloaded at 
https://jdbc.postgresql.org/download.html
and manually imported as an external jar file.

The test classes expect 4 databases in total:
* testCarpools - intended for testing interactions with
the database; data will be inserted and deleted
regularly. _Restore skeletonDump.tar_
* carpools - intended to provide a sample set of 
data to display to the front end. 
_Restore teamDataDump.tar_
* searchTester - intended for specific data sets on
which path-finding algorithms will be tested. 
_Restore testSearchData.tar_
* massData - intended for testing speed on massive
sets of data. _Restore then massData.tar or 
midSizeDataDump.tar_

## Deploying
We deploy on Heroku. See [deployment instructions](docs/deploymentInstructions.md) for detailed instructions on setting up the database and deploying. If you want to set up your own site - please check our license first. If you wish to connect to our production database (or need help with anything else), feel free to create an issue and mark it as question. We'll check out your request and decide how to best help you out!
