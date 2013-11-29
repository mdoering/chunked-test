## Description
Simple fat jar project that runs an embedded jetty server and executes json POST requests every 2 seconds.
The server and the address to POST against can be configured on startup so that one can POST directly against the server
or use an intermediate varnish server to test chunked transfer encodings which are currently not supported by varnish.

The jetty server simply returns plain text infos about the request including all headers,
requested URI and Content information plus the complete body entity itself.

## Usage
Run the chunked-test.jar in this project from the commandline like this to send post requests against the port 8080
which is the default port of the embedded jetty server:

   $ java -jar chunked-test.jar 8080


If you want to run jetty (the varnish backend) on a different port supply a second parameter.
This sends requests to varnish running on port 7001 with the jetty, the backend, configured in VCL running on port 8082:

   $ java -jar chunked-test.jar 7001 8082