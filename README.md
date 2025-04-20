# How to start?

```shell
java -Dserver.port=9443 -jar app.jar --spring.config.location=file:./application-properties.yml
```

# Where to Customize?

## Http Client
[HttpClientConfig.java](src/main/java/id/co/awan/gwproxy/config/HttpClientConfig.java)

## Register Route
[RouteConfig.java](src/main/java/id/co/awan/gwproxy/config/RouteConfig.java)

## Global Logging 
[GlobalRequestLoggingFilter.java](src/main/java/id/co/awan/gwproxy/filter/global/GlobalRequestLoggingFilter.java)
