Simple servlet that returns any POST/GET/PUT and returns plain text infos about the request including all headers,
requested URI and Content information plus the body entity itself.

GreetingClient is a jersey client which can POST to the servlet and use apache client or the default jersey client.
