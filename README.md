## avro-registry

Simple Rest Application to serve as a central registry for Avro Schemas


## TODO
* achema validation
* proper content types

## Prerequisites

You will need [Leiningen][] 2.0.0 or above installed.

[leiningen]: https://github.com/technomancy/leiningen

## Running

To start a web server for the application, run:

    lein ring server


## Usage

    curl http://localhost:3000/ping
    curl -X PUT http://localhost:3000/subject1
    curl -X PUT http://localhost:3000/subject1/register  -H "Content-Type: application/json" -d '{ "a" : "asd" }'

    curl -X GET http://localhost:3000/subject1/all
    curl -X GET http://localhost:3000/subject1/latest
    curl -X GET http://localhost:3000/subject1/id/1


## License

Copyright © 2015 FIXME
