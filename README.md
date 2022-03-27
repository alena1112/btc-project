# BTC Project

BTC Project helps to save btc records and show history

* [Repository](https://github.com/alena1112/btc-project)

# Structure
* `com/anymind/btc/exception` - contains base class exceptions
* `com/anymind/btc/model` - contains project entities
* `com/anymind/btc/repository` - contains classes for working with Database
* `com/anymind/btc/resources` - contains resource classes which allow to Web API
* `com/anymind/btc/services` - contains services which contain business logic

## Database

Application using H2 in-memory database. Database scheme is located in `sql_schema/schema.sql`".

## Run
1. Alter database host, port, location in `application.properties`
2. Build the project
```
   ./gradlew clean build
```
3. Setting a JVM Argument in order to change default timezone
```
   -Duser.timezone=UTC
```
3. Run class `BtcProjectApplication.kt`

## Run tests

Application contains integration and junit tests.
1. Alter database host, port, location in `application-testing.properties`
2. Run command
```
   ./gradlew test
```

## Web API
1. In order to save btc record need to use this endpoint:
```
http://localhost:8085/btc/save
```
2. In order to get btc records history need to use this endpoint:
```
http://localhost:8085/btc/history
```
PS: file `requests.http` contains base requests.