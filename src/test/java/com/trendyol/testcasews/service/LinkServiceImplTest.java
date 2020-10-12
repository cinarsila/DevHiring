package com.trendyol.testcasews.service;

import com.trendyol.testcasews.exception.NotFoundException;
import com.trendyol.testcasews.model.LinkCollection;
import com.trendyol.testcasews.model.Product;
import com.trendyol.testcasews.repository.LinkCollectionRepository;
import com.trendyol.testcasews.repository.ProductRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;

import java.net.URISyntaxException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
class LinkServiceImplTest {

	private static final String WEB_URL = "https://www.trendyol.com/casio/saat-p-1925865?boutiqueId=439892&merchantId=105064";

	private static final String DEEP_LINK = "ty://?Page=Product&ContentId=1925865&CampaignId=439892&MerchantId=105064";

	@InjectMocks
	private LinkServiceImpl LinkService;

	@Mock
	private LinkCollectionRepository linkCollectionRepository;

	@Mock
	private ProductRepository productRepository;

	@Test
	public void convertWebUrlToDeepLinkShouldThrowIllegalArgumentException() {
		assertThrows(IllegalArgumentException.class, () -> {
			LinkService.convertWebUrlToDeepLink(null);
		});
	}

	@Test
	public void convertWebUrlToDeepLinkShouldReturnResponseFromLinkCollection() throws URISyntaxException {
		when(linkCollectionRepository.findByUrl(WEB_URL)).thenReturn(Optional.of(new LinkCollection(WEB_URL, DEEP_LINK)));
		ResponseEntity<String> responseEntity = LinkService.convertWebUrlToDeepLink(WEB_URL);
		assertNotNull(responseEntity);
		assertNotNull(responseEntity.getBody());
		assertEquals(DEEP_LINK, responseEntity.getBody());
	}

	@Test
	public void convertWebUrlToDeepLinkShouldThrowURISyntaxException() {
		assertThrows(URISyntaxException.class, () -> {
			LinkService.convertWebUrlToDeepLink("http://");
		});
	}

	@Test
	public void convertWebUrlToDeepLinkShouldReturnHomeLink() throws URISyntaxException {
		when(linkCollectionRepository.findByUrl(anyString())).thenReturn(Optional.empty());
		ResponseEntity<String> responseEntity = LinkService.convertWebUrlToDeepLink("https://www.trendyol.com/casio/");
		assertNotNull(responseEntity);
		assertNotNull(responseEntity.getBody());
		assertEquals("ty://?Page=Home", responseEntity.getBody());
	}

	@Test
	public void convertWebUrlToDeepLinkShouldReturnDeepLinkFromExistedProduct() throws URISyntaxException {
		final Product product = new Product();
		product.setContentId(1925865L);
		product.setMerchantId("105064");
		product.setBoutiqueId("439892");
		product.setBrandName("casio");
		product.setName("saat");

		when(linkCollectionRepository.findByUrl(anyString())).thenReturn(Optional.empty());
		when(productRepository.findById(anyLong())).thenReturn(Optional.of(product));
		ResponseEntity<String> responseEntity = LinkService.convertWebUrlToDeepLink(WEB_URL);
		assertNotNull(responseEntity);
		assertNotNull(responseEntity.getBody());
		assertEquals(DEEP_LINK, responseEntity.getBody());
	}

	@Test
	public void convertWebUrlToDeepLinkShouldReturnDeepLinkFromNewProduct() throws URISyntaxException {
		final Product product = new Product();
		product.setContentId(1925865L);
		product.setMerchantId("105064");
		product.setBoutiqueId("439892");
		product.setBrandName("casio");
		product.setName("saat");

		when(linkCollectionRepository.findByUrl(anyString())).thenReturn(Optional.empty());
		when(productRepository.findById(anyLong())).thenReturn(Optional.empty());
		when(productRepository.save(any())).thenReturn(product);
		ResponseEntity<String> responseEntity = LinkService.convertWebUrlToDeepLink(WEB_URL);
		assertNotNull(responseEntity);
		assertNotNull(responseEntity.getBody());
		assertEquals(DEEP_LINK, responseEntity.getBody());
	}

	@Test
	public void convertDeepLinkToWebUrlShouldThrowIllegalArgumentException() {
		assertThrows(IllegalArgumentException.class, () -> {
			LinkService.convertDeepLinkToWebUrl(null);
		});
	}

	@Test
	public void convertDeepLinkToWebUrlShouldReturnResponseFromLinkCollection() throws NotFoundException {
		when(linkCollectionRepository.findByDeepLink(DEEP_LINK)).thenReturn(Optional.of(new LinkCollection(WEB_URL, DEEP_LINK)));
		ResponseEntity<String> responseEntity = LinkService.convertDeepLinkToWebUrl(DEEP_LINK);
		assertNotNull(responseEntity);
		assertNotNull(responseEntity.getBody());
		assertEquals(DEEP_LINK, responseEntity.getBody());
	}

	@Test
	public void convertDeepLinkToWebUrlShouldReturnResponseFromProductRepository() throws NotFoundException {
		final Product product = new Product();
		product.setContentId(1925865L);
		product.setMerchantId("105064");
		product.setBoutiqueId("439892");
		product.setBrandName("casio");
		product.setName("saat");

		when(linkCollectionRepository.findByDeepLink(DEEP_LINK)).thenReturn(Optional.empty());
		when(productRepository.findById(anyLong())).thenReturn(Optional.of(product));
		ResponseEntity<String> responseEntity = LinkService.convertDeepLinkToWebUrl(DEEP_LINK);
		assertNotNull(responseEntity);
		assertNotNull(responseEntity.getBody());
		assertEquals(WEB_URL, responseEntity.getBody());
	}
}