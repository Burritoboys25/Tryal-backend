# Backend Repository for H2AK


## Run Postgres with pgAdmin Locally

1.) Start Docker Desktop.

2.) Run Docker compose file or run existing containers in Docker Desktop.

3.) Start pgAdmin.

4.) Right-click on "Servers" > "Register" > "Server..."

5.) Fill in the below information:

| Tab          |   Field           | Value                      |
| :-------     | :-------          | :-------                   |
| `General`    | Name              | anything (e.g., Postgres)  |
| `Connection` | Host name/address | localhost                  |
| `Connection` | Port              | 5432                       |
| `Connection` | Username          | admin                      |
| `Connection` | Password          | password                   |

6.) Run main application.
