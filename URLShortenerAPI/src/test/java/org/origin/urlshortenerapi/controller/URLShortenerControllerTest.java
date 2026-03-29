package org.origin.urlshortenerapi.controller;

import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import org.mockito.Mockito;
import org.origin.urlshortenerapi.dto.ExceptionResponse;
import org.origin.urlshortenerapi.dto.ShortURLResponse;
import org.origin.urlshortenerapi.dto.URLInfoResponse;
import org.origin.urlshortenerapi.service.URLGeneratorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.resttestclient.autoconfigure.AutoConfigureRestTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;
import org.springframework.test.web.servlet.client.RestTestClient;

@SpringBootTest
@AutoConfigureRestTestClient
public class URLShortenerControllerTest {

    @Value("${domain.prefix}")
    private String domainPrefix;

    private String localHost = "http://localhost:8080/";

    @Autowired
    private RestTestClient restTestClient;

    @MockitoSpyBean
    private URLGeneratorService urlGeneratorService;

    @Test
    public void testShortURL() {
        ShortURLResponse shortURLResponse = restTestClient.post()
                .uri(localHost + "shorturl?url=https://www.originenergy.com.au/electricity-gas/plans.html")
                .exchange()
                .expectStatus().isCreated()
                .expectBody(ShortURLResponse.class)
                .returnResult()
                .getResponseBody();
        String shortURLPath = shortURLResponse.shortURL().substring(domainPrefix.length());

        Assertions.assertTrue(StringUtils.isAlphanumeric(shortURLPath));
        Assertions.assertEquals(6, shortURLPath.length());
    }

    @Test
    public void testShortURLInvalid() {
        ExceptionResponse exceptionResponse = restTestClient.post()
                .uri(localHost + "shorturl?url=https://www.origine  nergy.com.au/electricity-gas/plans.html")
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody(ExceptionResponse.class)
                .returnResult()
                .getResponseBody();

        Assertions.assertEquals("URL is not valid", exceptionResponse.message());
    }

    @Test
    public void testShortURLDuplicateGenerationError() {
        Mockito.doThrow(DataIntegrityViolationException.class).when(urlGeneratorService).constructShortURL("https://www.originenergy.com.au/electricity-gas/plans.html");

        restTestClient.post()
                .uri(localHost + "shorturl?url=https://www.originenergy.com.au/electricity-gas/plans.html")
                .exchange()
                .expectBody(ExceptionResponse.class);
    }

    @Test
    public void testRedirect(){
        ShortURLResponse shortURLResponse = restTestClient.post()
                .uri(localHost + "shorturl?url=https://www.originenergy.com.au/electricity-gas/plans.html")
                .exchange()
                .expectBody(ShortURLResponse.class)
                .returnResult()
                .getResponseBody();

        String shortURLPath = shortURLResponse.shortURL().substring(domainPrefix.length());

        restTestClient.get()
                .uri(localHost + shortURLPath)
                .exchange()
                .expectStatus().isPermanentRedirect()
                .expectHeader().valueEquals("Location", "https://www.originenergy.com.au/electricity-gas/plans.html");
    }

    @Test
    public void testRedirectNotFound(){
        String shortURLPath = "123456";

        ExceptionResponse exceptionResponse = restTestClient.get()
                .uri(localHost + shortURLPath)
                .exchange()
                .expectStatus().isNotFound()
                .expectBody(ExceptionResponse.class)
                .returnResult()
                .getResponseBody();

        Assertions.assertEquals("URL not found", exceptionResponse.message());
    }

    @Test
    public void testRedirectInvalid(){
        String shortURLPath = "1234567";

        ExceptionResponse exceptionResponse = restTestClient.get()
                .uri(localHost + shortURLPath)
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody(ExceptionResponse.class)
                .returnResult()
                .getResponseBody();

        Assertions.assertEquals("URL is not valid", exceptionResponse.message());

        shortURLPath = "123";

        exceptionResponse = restTestClient.get()
                .uri(localHost + shortURLPath)
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody(ExceptionResponse.class)
                .returnResult()
                .getResponseBody();

        Assertions.assertEquals("URL is not valid", exceptionResponse.message());
    }

    @Test
    public void testURLInfo() {
        ShortURLResponse shortURLResponse = restTestClient.post()
                .uri(localHost + "shorturl?url=https://www.originenergy.com.au/electricity-gas/plans.html")
                .exchange()
                .expectBody(ShortURLResponse.class)
                .returnResult()
                .getResponseBody();

        String shortURLPath = shortURLResponse.shortURL().substring(domainPrefix.length());

        URLInfoResponse urlInfoResponse = restTestClient.post()
                .uri(localHost + "urlinfo?shorturl=" + shortURLPath)
                .exchange()
                .expectStatus().isFound()
                .expectBody(URLInfoResponse.class)
                .returnResult()
                .getResponseBody();

        Assertions.assertEquals(shortURLResponse.shortURL(), urlInfoResponse.shortURL());
        Assertions.assertEquals("https://www.originenergy.com.au/electricity-gas/plans.html", urlInfoResponse.fullURL());
        Assertions.assertNotNull(urlInfoResponse.createdDate());
    }

    @Test
    public void testURLInfoNotFound() {
        String shortURLPath = "123456";

        ExceptionResponse exceptionResponse = restTestClient.post()
                .uri(localHost + "urlinfo?shorturl=" + shortURLPath)
                .exchange()
                .expectStatus().isNotFound()
                .expectBody(ExceptionResponse.class)
                .returnResult()
                .getResponseBody();

        Assertions.assertEquals("URL not found", exceptionResponse.message());
    }

    @Test
    public void testURLInfoNotInvalid() {
        String shortURLPath = "1234567";

        ExceptionResponse exceptionResponse = restTestClient.post()
                .uri(localHost + "urlinfo?shorturl=" + shortURLPath)
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody(ExceptionResponse.class)
                .returnResult()
                .getResponseBody();

        Assertions.assertEquals("URL is not valid", exceptionResponse.message());

        shortURLPath = "123";

        exceptionResponse = restTestClient.post()
                .uri(localHost + "urlinfo?shorturl=" + shortURLPath)
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody(ExceptionResponse.class)
                .returnResult()
                .getResponseBody();

        Assertions.assertEquals("URL is not valid", exceptionResponse.message());

        shortURLPath = "";

        exceptionResponse = restTestClient.post()
                .uri(localHost + "urlinfo?shorturl=" + shortURLPath)
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody(ExceptionResponse.class)
                .returnResult()
                .getResponseBody();

        Assertions.assertEquals("URL is not valid", exceptionResponse.message());
    }
}
