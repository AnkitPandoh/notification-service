package com.notification.api;

import static org.junit.Assert.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import com.notification.config.NotificationConfig;
import com.notification.response.ApiWrapper;

/**
 * Use @WebMvcTest - Spring Boot is only instantiating the web layer, not the
 * whole context Use @SpringBootTest(webEnvironment =
 * WebEnvironment.RANDOM_PORT) for end to end testing by spinning up a server
 */
@RunWith(SpringRunner.class)
@WebMvcTest(value = { NotificationApi.class, ApiWrapper.class, NotificationConfig.class })
public class NotificationApiIntegrationTest {

	@Autowired
	private MockMvc mvc;

	private static final String TEST_BODY = "{\"to\": \"pandoh.2007@gmail.com\",\"subject\": \"test email service\",\"template\": \"notify\",\"message\": \"Hello, This is a test email from notification service\"}";

	@Before
	public void setup() {
	}

	@Test
	public void testWithUnknownNotificationType() throws Exception {
		MvcResult response = mvc
				.perform(post("/notify/v1/wrongType").content(TEST_BODY).accept(MediaType.APPLICATION_JSON))
				.andReturn();
		assertEquals(500, response.getResponse().getStatus());
		String expectedResponse = "{\"error\": \"Something went wrong while processing your request\",\"ok\":false}";
		JSONAssert.assertEquals(expectedResponse, response.getResponse().getContentAsString(), false);
	}

	@Test
	public void testForOkResponse() throws Exception {
		MvcResult response = mvc.perform(post("/notify/v1/email").content(TEST_BODY).accept(MediaType.APPLICATION_JSON))
				.andReturn();
		assertEquals(200, response.getResponse().getStatus());
		String expectedResponse = "{\"response\": \"Message sent successfully\",\"ok\":true}";
		JSONAssert.assertEquals(expectedResponse, response.getResponse().getContentAsString(), false);
	}

}
