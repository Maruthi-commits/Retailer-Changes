package com.Retail.Retailer.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.Retail.Retailer.Model.CustomerPoints;
import com.Retail.Retailer.Model.Transaction;
import com.Retail.Retailer.Service.RetailerService;

@RestController
@RequestMapping("/api/rewards")
public class RetailerController {
	
	@Autowired
    private RetailerService retailerService;
	// Endpoint to calculate reward points
	@PostMapping("/calculate")
    public ResponseEntity<CustomerPoints> calculateRewards(@RequestBody List<Transaction> transactions) {
		CustomerPoints response=  retailerService.calculatePoints(transactions);
        
		return new ResponseEntity<>(response, HttpStatus.OK);
    }

}
