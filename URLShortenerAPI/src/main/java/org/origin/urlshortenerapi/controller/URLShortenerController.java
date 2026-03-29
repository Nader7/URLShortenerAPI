package org.origin.urlshortenerapi.controller;

import org.origin.urlshortenerapi.dto.ShortURLResponse;
import org.origin.urlshortenerapi.dto.URLInfoResponse;
import org.origin.urlshortenerapi.service.URLGeneratorService;
import org.origin.urlshortenerapi.service.URLInfoService;
import org.origin.urlshortenerapi.service.URLRedirectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
public class URLShortenerController {
    @Autowired
    private URLGeneratorService urlGeneratorService;

    @Autowired
    private URLRedirectService urlRedirectService;

    @Autowired
    private URLInfoService urlInfoService;

    /***
     * Takes a full sized URL, generates a 6 character key, and then returns the new URL
     * with the short.ly domain and the new key
     * @param url
     * @return ResponseEntity<ShortURLResponse>
     */
    @PostMapping("/shorturl")
    public ResponseEntity<ShortURLResponse> shortURL(@RequestParam("url") String url){
        ShortURLResponse response = urlGeneratorService.constructShortURL(url);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    /***
     * Takes the short 6 character key as a path variable, checks the in memory database
     * for the correlating full-sized URL. Then sends a redirect to the full URL address
     * @param shortURL
     * @return Redirect response
     */
    @GetMapping("/{shorturl}")
    public ResponseEntity<Void> redirect(@PathVariable("shorturl") String shortURL){
        String redirectURL = urlRedirectService.getFullURL(shortURL);
        return ResponseEntity
                .status(HttpStatus.PERMANENT_REDIRECT)
                .location(URI.create(redirectURL))
                .build();
    }

    /***
     * Takes the short 6 character key parameter, checks the in memory database
     * for the correlating database entry. Then returns an object with all the
     * information stored for that URL key.
     * @param shortURL
     * @return ResponseEntity<URLInfoResponse>
     */
    @PostMapping("/urlinfo")
    public ResponseEntity<URLInfoResponse> urlInfo(@RequestParam("shorturl") String shortURL){
        URLInfoResponse response = urlInfoService.findURLInfo(shortURL);
        return ResponseEntity
                .status(HttpStatus.FOUND)
                .body(response);
    }

}
