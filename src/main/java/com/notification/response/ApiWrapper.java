package com.notification.response;

import java.io.IOException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.notification.exception.NotificationException;

public class ApiWrapper {

	private final ObjectMapper mapper;
	private final ObjectWriter writer;

	public ApiWrapper() {
		mapper = new ObjectMapper();
		writer = mapper.writerWithDefaultPrettyPrinter();
	}

	public <Q, R> ResponseEntity<String> execute(TypeReference<Q> requestType, String requestBody,
			BusinessLogic<Q, R> businessLogic) throws NotificationException {
		Q request;
		try {
			request = mapper.readValue(requestBody, requestType);
		} catch (IOException ioex) {
			return new ResponseEntity<>(formatResponse(new ApiResponse<>("Please check your request")),
					HttpStatus.BAD_REQUEST);
		}
		R response = null;
		try {
			response = businessLogic.runLogic(request);
			return new ResponseEntity<>(formatResponse(new ApiResponse<>(response)), HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(
					formatResponse(new ApiResponse<>("Something went wrong while processing your request")),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	public <R> ResponseEntity<String> executeWithoutInputs(BusinessLogicWithoutInputs<R> businessLogic)
			throws NotificationException {
		R response = null;
		try {
			response = businessLogic.runLogic();
			return new ResponseEntity<>(formatResponse(new ApiResponse<>(response)), HttpStatus.OK);
		} catch (Exception ex) {
			return new ResponseEntity<>(
					formatResponse(new ApiResponse<>("Something went wrong while processing your request")),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	private <R> String formatResponse(ApiResponse<R> response) throws NotificationException {
		try {
			return writer.writeValueAsString(response);
		} catch (JsonProcessingException jex) {
			throw new NotificationException("Error while sending the response.");
		}
	}

	@FunctionalInterface
	public interface BusinessLogic<Q, R> {
		R runLogic(Q request) throws NotificationException;
	}

	@FunctionalInterface
	public interface BusinessLogicWithoutInputs<R> {
		R runLogic() throws NotificationException;
	}
}
