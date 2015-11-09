## avro-registry

Simple Rest Application to serve as a central registry for Avro Schemas.
Uses a RDBMS for storing schemas and version numbers, includes built in full avro validation

## Prerequisites
You will need [Leiningen][] 2.0.0 or above installed.
[leiningen]: https://github.com/technomancy/leiningen

## Running

To start a web server for the application, run:

    lein ring server

### Usage
check sample file
(needs restclient.el)

##Deploy as War
execute

		lein ring uberwar
		
and deploy the resulting war file

## TODO
* proper content types
* tests

## License

Copyright © 2015 FIXME
