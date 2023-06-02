# HttpVsDbCompare
How to run the program:
mvn clean install
java -DDB_HOST="host of postgresql database" 
     -DDB_USER="user to login to database" 
     -DDB_PASSWORD="password to login to database" 
     -DDB_NAME="name of database"
     -DTENANT_ID="tenant id" // for example, <diku>
     -DHTTP_HOST="http(s) host of server with folio modules" // for example, http://localhost if locally
     -DHTTP_PORT="http(s) port of server with folio modules" // for example, 443 if https
     -DHTTP_USERNAME="username to login to folio application" 
     -DHTTP_PASSWORD="password to login to folio application" 
     -DHTTP_BATCH_SIZE="size of batch" // 500 default, depends on the memory limit of folio server
     -DDB_BATCH_SIZE="size of batch" // 500 default, depends on the memory limit of folio DB
     -jar HttpVsDbCompare-0.0.1-SNAPSHOT.jar
