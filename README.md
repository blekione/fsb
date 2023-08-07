# FSB Game service

## How to run it

To run tests and build execute
```shell
./mvnw clean install
```

To run it from the command line execute
```shell
./mvnw spring-boot:run
```

The service will be running at http://localhost:8080

## Documentation

The service API documentation can be found at `http://localhost:8080/swagger-ui/index.html

## Healthcheck (monitoring)

The service has enabled the Spring actuator with the default `health/` endpoint:

- http://localhost:8080/actuator/health 



## Considerations

When I would have more time to spend on this project I would improve integration testing as they are
now depends on each other and need to be running in specified order.

The use of in memory store (map) definitely limits the design of the solution.
When working with real SQL database I would consider store events rather than 
updating records. As such status update operation would create a new record in 
the database, rather than changed the existing one.

Another consideration when real database would be used is making and endpoint to
get all games paginated, to protect against large number of records send at once

Obvious one is to use `TSL/SSL` for communication if this service is publicly available. But the way 
to approach it would depend on the infrastructure, as it might be enabled on load balancer/proxy level
and internal communication is over `http`. Would need to know more of the context to make an informed
decision.
