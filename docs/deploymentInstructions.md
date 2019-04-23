# Deployment Instructions

## Setting up your own environment
There's one major change you'll have to make to your own environment in order to run the deployment-ready version locally. If you use the `init` script to run, edit the line `export DATABASE_URL="<db-name>"` to now instead say `export DATABASE_URL="postgres://<username>:<password>@localhost:5432/<db-name>"`. If you use the `./run` script, simply change your environment variable for that configuration in the same way.

## Setting up pgAdmin
Open up pgAdmin and right-click on "Servers". Choose "Create -> Server...". Use the following settings:
**General**
 - Name: Drawbridge Production (not critical but a good, descriptive name)
 - Connect now: checked
 
**Connection**
For these settings open up the project on Heroku. Go to the Overview tab and click on the Heroku Postgres add-on. On that page, go to the Settings tab and click "View Credentials". The settings in pgAdmin line up in the following way (pgAdmin field: Heroku value):
 - Host name: _Host_
 - Port: _Port_
 - Maintenance database: _Database_
 - Username: _User_
 - Password: _Password_
 - Save password?: checked (to save headaches of having to remember the random string heroku generates)
 - Role & Service: leave blank
 
**Advanced**
To not have to scroll past ~2300 other databases you can't access, go to the Advanced tab and add the value you put for _Database_ above into "DB restriction".

That's it! Click Save and you should be connected.

**NOTE: This is the production database. Anything you do here will affect everyone using the site. Be VERY careful.**
