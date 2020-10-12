package com.trendyol.testcasews.service;

import com.trendyol.testcasews.exception.NotFoundException;
import com.trendyol.testcasews.model.LinkCollection;
import com.trendyol.testcasews.model.Product;
import com.trendyol.testcasews.repository.LinkCollectionRepository;
import com.trendyol.testcasews.repository.ProductRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.*;
import java.util.stream.Collectors;

import static com.trendyol.testcasews.LinkConstants.*;

@Service
public class LinkServiceImpl implements LinkService {
	private final LinkCollectionRepository linkCollectionRepository;
	private final ProductRepository productRepository;

	public LinkServiceImpl(LinkCollectionRepository linkCollectionRepository, ProductRepository productRepository) {
		this.linkCollectionRepository = linkCollectionRepository;
		this.productRepository = productRepository;
	}

	private void addLinkCollection(String webUrl, String deepLink) {
		linkCollectionRepository.save(new LinkCollection(webUrl, deepLink));
	}

	@Override
	public ResponseEntity<String> convertWebUrlToDeepLink(String webUrl) throws URISyntaxException {
		if (webUrl == null || "".equals(webUrl.trim())) {
			throw new IllegalArgumentException("webUrl empty!");
		}
		final Optional<LinkCollection> optionalLinkCollection = linkCollectionRepository.findByUrl(webUrl);
		if (optionalLinkCollection.isPresent()) {
			return ResponseEntity.status(HttpStatus.OK).body(optionalLinkCollection.get().getDeepLink());
		}

		URI uri = new URI(webUrl);
		String path = uri.getPath();
		Map<String, String> queryParamMap = extractUrlParametersFromQuery(uri.getRawQuery());
		Product product = null;
		if (path.contains("-p-")) {
			String contentID = path.substring(path.lastIndexOf("-") + 1);
			List<String> nameAndBrandNameFromPath = extractNameAndBrandNameFromPath(path);
			Optional<Product> optionalProduct = productRepository.findById(Long.parseLong(contentID));
			if (optionalProduct.isPresent()) {
				product = optionalProduct.get();
			} else {
				final Product newProduct = new Product();
				newProduct.setContentId(Long.parseLong(contentID));
				newProduct.setMerchantId(queryParamMap.get("merchantId"));
				newProduct.setBoutiqueId(queryParamMap.get("boutiqueId"));
				newProduct.setBrandName(nameAndBrandNameFromPath.get(0));
				newProduct.setName(nameAndBrandNameFromPath.get(1));
				product = productRepository.save(newProduct);
			}
		}

		String responseFromRequest = createResponseFromRequest(product, path, queryParamMap);
		addLinkCollection(webUrl, responseFromRequest);
		return ResponseEntity.status(HttpStatus.OK).body(responseFromRequest);
	}


	@Override
	public ResponseEntity<String> convertDeepLinkToWebUrl(String deepLink) throws NotFoundException {
		if (deepLink == null || "".equals(deepLink.trim())) {
			throw new IllegalArgumentException("deepLink empty!");
		}
		final Optional<LinkCollection> optionalLinkCollection = linkCollectionRepository.findByDeepLink(deepLink);
		if (optionalLinkCollection.isPresent()) {
			return ResponseEntity.status(HttpStatus.OK).body(optionalLinkCollection.get().getDeepLink());
		}
		String response;
		String deepLinkWithoutUrl = deepLink.substring(deepLink.indexOf("&") + 1);
		Map<String, String> deepLinkParameterValues = extractUrlParametersFromQuery(deepLinkWithoutUrl);
		if (deepLink.contains("Product")) {
			Long contentID = Long.parseLong(deepLinkParameterValues.get("ContentId"));
			final Product product = productRepository.findById(contentID).orElseThrow(() -> new NotFoundException("product not found"));
			response = createProductDetailPageWebUrlFromDeepLinkResponse(product, contentID, deepLinkParameterValues);
		} else if (deepLink.contains("Search")) {
			response = createSearchPageResponseFromDeepLink(deepLinkParameterValues);
		} else {
			response = TRENDYOL_URI;
		}
		addLinkCollection(response, deepLink);
		return ResponseEntity.status(HttpStatus.OK).body(response);
	}

	private String createSearchPageResponseFromDeepLink(Map<String, String> deepLinkParameterValues) {
		return String.format("%s/%s?q=%s", TRENDYOL_URI, TUM_URUNLER, deepLinkParameterValues.get("Query"));
	}

	private String createProductDetailPageWebUrlFromDeepLinkResponse(Product existedProduct, Long contentID, Map<String, String> deepLinkParameterValues) {
		StringBuilder productDetailPageResponse = new StringBuilder();
		productDetailPageResponse.append(TRENDYOL_URI)
				.append("/").append(existedProduct.getBrandName())
				.append("/").append(existedProduct.getName())
				.append("-p-").append(contentID.toString())
				.append("?");
		if (deepLinkParameterValues.containsKey("CampaignId")) {
			if (productDetailPageResponse.charAt(productDetailPageResponse.length() - 1) != '?') {
				productDetailPageResponse.append("&");
			}
			productDetailPageResponse.append("boutiqueId=").append(deepLinkParameterValues.get("CampaignId"));
		}
		if (deepLinkParameterValues.containsKey("MerchantId")) {
			if (productDetailPageResponse.charAt(productDetailPageResponse.length() - 1) != '?') {
				productDetailPageResponse.append("&");
			}
			productDetailPageResponse.append("merchantId=").append(deepLinkParameterValues.get("MerchantId"));
		}
		return productDetailPageResponse.toString();
	}

	private List<String> extractNameAndBrandNameFromPath(String path) {
		List<String> pathVariables = new ArrayList<>();
		String[] splittedPath = path.split("/");
		String brandName = splittedPath[1];
		String name = splittedPath[2].substring(0, splittedPath[2].indexOf("-p"));
		pathVariables.add(brandName);
		pathVariables.add(name);
		return pathVariables;
	}

	private Map<String, String> extractUrlParametersFromQuery(String query) {
		if (query == null) {
			return Collections.emptyMap();
		}
		return Arrays.stream(query.split("&"))
				.map(next -> next.split("="))
				.collect(Collectors.toMap(entry -> entry[0], entry -> entry[1]));
	}

	private String createResponseFromRequest(Product product, String path, Map<String, String> queryParamMap) {
		StringBuilder response = new StringBuilder();
		response.append(DEEP_LINK_URI);
		if (path.contains("-p-")) {
			response.append(PRODUCT).append(CONTENT_ID).append(product.getContentId());
			if (queryParamMap.containsKey("boutiqueId")) {
				response.append("&").append(CAMPAIGN_ID).append(queryParamMap.get("boutiqueId"));
				product.setBoutiqueId(queryParamMap.get("boutiqueId"));
			}
			if (queryParamMap.containsKey("merchantId")) {
				response.append("&").append(MERCHANT_ID).append(queryParamMap.get("merchantId"));
				product.setMerchantId(queryParamMap.get("merchantId"));
			}
			productRepository.save(product);
		} else if (path.contains(TUM_URUNLER)) {
			response.append(SEARCH).append(QUERY).append(queryParamMap.get("q"));
		} else {
			response.append(HOME);
		}
		return response.toString();
	}
}
