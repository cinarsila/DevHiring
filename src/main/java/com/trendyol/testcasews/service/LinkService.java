package com.trendyol.testcasews.service;

import com.trendyol.testcasews.exception.NotFoundException;
import org.springframework.http.ResponseEntity;

import java.net.URISyntaxException;

public interface LinkService {

	ResponseEntity<String> convertWebUrlToDeepLink(String webUrl) throws URISyntaxException;

	ResponseEntity<String> convertDeepLinkToWebUrl(String deepLink) throws NotFoundException;
}
