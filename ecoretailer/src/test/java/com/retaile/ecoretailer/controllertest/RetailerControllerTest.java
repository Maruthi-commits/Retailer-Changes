package com.retaile.ecoretailer.controllertest;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.retaile.ecoretailer.controller.RetailerController;
import com.retaile.ecoretailer.model.CustomerPoints;
import com.retaile.ecoretailer.model.Transaction;
import com.retaile.ecoretailer.service.RetailerService;
import com.fasterxml.jackson.databind.ObjectMapper;

@ExtendWith(MockitoExtension.class)
@SpringBootTest
public class RetailerControllerTest {
	private MockMvc mockMvc;

	@Mock
	private RetailerService retailerService;

	@InjectMocks
	private RetailerController retailerController;

	@Autowired
	private ObjectMapper objectMapper;

	@BeforeEach
	public void setup() {
		mockMvc = MockMvcBuilders.standaloneSetup(retailerController).build();
	}

	// Test Case 1: Successful Calculation of Rewards
	@Test
	void TestcalculateRewards_ShouldReturn200_WhenValidTransactionsArePassed() throws Exception {

		List<Transaction> transactions = Arrays.asList(new Transaction("123", 100.0), new Transaction("124", 200.0));

		CustomerPoints customerPoints = new CustomerPoints(300); // Assuming a constructor with total points

		when(retailerService.calculatePoints(transactions)).thenReturn(customerPoints);

		// Perform POST request and validate response
		mockMvc.perform(MockMvcRequestBuilders.post("/api/rewards/calculate")
				.content(objectMapper.writeValueAsString(transactions)).contentType(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.content().json(objectMapper.writeValueAsString(customerPoints)));

		verify(retailerService, times(1)).calculatePoints(transactions); // Ensure service method was called once
	}

	// Test Case 2: Empty Transactions List (Bad Request)
	@Test
	public void testCalculateRewards_EmptyTransactions() throws Exception {
		List<Transaction> emptyTransactions = List.of();

		// Perform a POST request with an empty list and expect a 400 Bad Request
		mockMvc.perform(post("/api/rewards/calculate").contentType("application/json")
				.content(objectMapper.writeValueAsString(emptyTransactions))).andExpect(status().isBadRequest()) // Expecting 400 Bad Request due to empty list 																									
				.andDo(print());
	}

	// Test Case 3: Handling Service Layer Error (Internal Server Error)
	@Test
	public void testCalculateRewards_InternalServerError() throws Exception {
		List<Transaction> transactions = Arrays.asList();
		// Mocking the service to throw an exception when calculating points
		when(retailerService.calculatePoints(transactions)).thenThrow(new RuntimeException("Internal Server Error"));

		// Perform the POST request and expect a 500 Internal Server Error
		mockMvc.perform(post("/api/rewards/calculate").contentType("application/json")
				.content(objectMapper.writeValueAsString(transactions))).andExpect(status().isInternalServerError()) // Expecting 500 Internal Server Error																											
				.andDo(print());
	}

	// Test Case 4: Invalid Input (Malformed Transaction Object)
	@Test
	public void testCalculateRewards_InvalidTransaction() throws Exception {
		// Missing 'amount' field
		String invalidTransactionJson = "[{\"transactionId\": 1}]";

		// Perform the POST request with malformed data and expect a 400 Bad Request
		mockMvc.perform(post("/api/rewards/calculate").contentType("application/json").content(invalidTransactionJson))
				.andExpect(status().isBadRequest()) // Expecting 400 Bad Request due to invalid input
				.andDo(print());
	}

}
