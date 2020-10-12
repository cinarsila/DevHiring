package com.trendyol.testcasews.controller;

import com.trendyol.testcasews.service.LinkService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = LinkConverterController.class)
@ActiveProfiles("test")
class LinkConverterControllerTest {
	private static final String WEB_URL = "https://www.trendyol.com/casio/saat-p-1925865?boutiqueId=439892&merchantId=105064";

	private static final String DEEP_LINK = "ty://?Page=Product&ContentId=1925865&CampaignId=439892&MerchantId=105064";

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private LinkService linkService;

	@BeforeEach
	void setUp() {
	}

	@Test
	public void convertWebUrlToDeepLinkWillThrowURISyntaxException() throws Exception {
		given(linkService.convertWebUrlToDeepLink(WEB_URL)).willReturn(ResponseEntity.status(HttpStatus.OK).body(DEEP_LINK));
		mockMvc.perform(post("/testcasews/convertWebUrlToDeepLink")
				.param("weburl", WEB_URL))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$").value(DEEP_LINK));
	}

	@Test
	void convertDeepLinkToWebUrl() throws Exception {
		given(linkService.convertDeepLinkToWebUrl(DEEP_LINK)).willReturn(ResponseEntity.status(HttpStatus.OK).body(WEB_URL));
		mockMvc.perform(post("/testcasews/convertDeepLinkToWebUrl")
				.param("deeplink", DEEP_LINK))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$").value(WEB_URL));
	}
}