
# Workshop Reservation System

## Overview

This is a Workshop Reservation System application which handles reservations on rooms.


### Exploring the REST API with a built in Swagger UI client

Start the application. This application comes with a built in Swagger UI client. It is exposed at the following endpoint [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)

This endpoint is normally not available in production system thus the application is configured to expose it only when the current active profile is development mode (`dev_xxxservice`). 

The UI should look similar to [Swagger-UI](src/main/resources/images/Swagger-UI.png)



## Rest APIs

On Intellij, "Enable annotation processing" checkbox in `Settings->Compiler->Annotation Processors`

See the `rest/` directory to get Postman Collections.


## Config

Provide the appropriate profile to applications to run on dev/test/uat/prod mode:
```
*    -Dspring.profiles.active=dev_eurekaservice
*    -Dspring.profiles.active=dev_entityservice
*    -Dspring.profiles.active=dev_reservationservice
```

