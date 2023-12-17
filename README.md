Quick implementation for aynchronously generating requests for load testing a service.

The folder `sampletarget` has a jar already generated for convenience, and can be run as `java -jar LoadTester-0.0.1-SNAPSHOT-jar-with-dependencies.jar targetRPS=25 durationMinutes=2 authKey=abc`.
Parameters:
`targetRPS`: The target requests per second.
`durationMinutes`: The number of request generation should run.
`authKey`: The authentication key for the requests.
