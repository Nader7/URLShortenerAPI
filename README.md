# URLShortenerAPI

A simple URL shortening springboot API.

## Example Inputs:
### ShortURL Endpoint
![ShortUrlExample](https://i.imgur.com/2wnafTA.png)
Request: http://localhost:8080/shorturl?url=https://www.originenergy.com.au/electricity-gas/plans.html

Response: { "shortURL": "http://short.ly/jlojhp" }

### Redirect Endpoint
![RedirectExample](https://i.imgur.com/eAx3Uix.png)

Request: http://localhost:8080/jlojhp

### URLInfo Endpoint
![URLInfoExample](https://i.imgur.com/j4CSClX.png)

Request: http://localhost:8080/urlinfo?shorturl=jlojhp

Response: 
{
"shortURL": "http://short.ly/jlojhp",
"fullURL": "https://www.originenergy.com.au/electricity-gas/plans.html",
"createdDate": "2026-03-30T09:03:08.037712"
}

## Running The Application
### Just clone the app, install with maven and run it with maven and springboot in your console

mvn clean install

mvn spring-boot:run

### Or clone it into an IDE and run it as a SpringBoot Application.