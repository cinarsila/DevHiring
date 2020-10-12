package com.trendyol.testcasews.controller;

import com.trendyol.testcasews.exception.NotFoundException;
import com.trendyol.testcasews.service.LinkService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.net.URISyntaxException;

@RestController
@RequestMapping("testcasews")
public class LinkConverterController {

	private final LinkService linkService;

	public LinkConverterController(LinkService linkService) {
		this.linkService = linkService;
	}

	@PostMapping(value = "/convertWebUrlToDeepLink")
	public ResponseEntity<String> convertWebUrlToDeepLink(@RequestParam("weburl") String weburl) throws URISyntaxException {
		return linkService.convertWebUrlToDeepLink(weburl);
	}

	@PostMapping(value = "/convertDeepLinkToWebUrl")
	public ResponseEntity<String> convertDeepLinkToWebUrl(@RequestParam("deeplink") String deepLink) throws NotFoundException {
		return linkService.convertDeepLinkToWebUrl(deepLink);
	}
}
