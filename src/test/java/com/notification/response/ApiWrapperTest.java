package com.notification.response;

import static org.junit.Assert.assertEquals;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.notification.exception.NotificationException;

public class ApiWrapperTest {

	private ObjectMapper mapper;
	private ObjectWriter writer;
	private ApiWrapper wrapper;

	@Before
	public void setup() {
		wrapper = new ApiWrapper();
		mapper = new ObjectMapper();
		writer = mapper.writerWithDefaultPrettyPrinter();
	}

	@Test
	public void testExecuteWithInputsWhenOkResponse() throws NotificationException {
		String requestBody = "{\"a\":1, \"b\":2}";
		ResponseEntity<String> response = wrapper.execute(new TypeReference<Inputs>() {
		}, requestBody, (inputs) -> {
			int result = inputs.a + inputs.b;
			return result;
		});
		assertEquals(HttpStatus.OK, response.getStatusCode());
	}

	@Test
	public void testExecuteWithInputsWhenBadRequest() throws NotificationException {
		String requestBody = "{\"a\":1, \"b\":abd}";
		ResponseEntity<String> response = wrapper.execute(new TypeReference<Inputs>() {
		}, requestBody, (inputs) -> {
			int result = inputs.a + inputs.b;
			return result;
		});
		assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
	}

	@Test
	public void testExecuteWithInputsWhenResponseNotNull()
			throws NotificationException, JsonParseException, JsonMappingException, IOException {
		String requestBody = "{\"a\":1, \"b\":2}";
		ResponseEntity<String> response = wrapper.execute(new TypeReference<Inputs>() {
		}, requestBody, (inputs) -> {
			int result = inputs.a + inputs.b;
			return result;
		});
		ApiResponse<String> res = mapper.readValue(response.getBody(), new TypeReference<ApiResponse<String>>() {
		});
		assertEquals("3", res.getResponse());
	}

	@Test
	public void testExecuteWithInputsWhenErrorResponse()
			throws NotificationException, JsonParseException, JsonMappingException, IOException {
		String requestBody = "{\"a\":1, \"b\":0}";
		ResponseEntity<String> response = wrapper.execute(new TypeReference<Inputs>() {
		}, requestBody, (inputs) -> {
			int result = inputs.a / inputs.b;
			return result;
		});
		ApiResponse<String> res = mapper.readValue(response.getBody(), new TypeReference<ApiResponse<String>>() {
		});
		assertEquals(false, res.isOk());
	}

	@Test
	public void testExecuteWithInputsWhenInternalServerError() throws NotificationException {
		String requestBody = "{\"a\":1, \"b\":0}";
		ResponseEntity<String> response = wrapper.execute(new TypeReference<Inputs>() {
		}, requestBody, (inputs) -> {
			throw new NotificationException("Something went wrong while processing your request");
		});
		assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
	}

	static class Inputs {
		int a;
		int b;

		public int getA() {
			return a;
		}

		public int getB() {
			return b;
		}
	}

}
