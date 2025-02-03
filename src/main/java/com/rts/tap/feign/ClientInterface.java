package com.rts.tap.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.rts.tap.constants.APIConstants;
import com.rts.tap.model.Client;

@FeignClient("CLIENT-SERVICE")
public interface ClientInterface {

	@GetMapping(APIConstants.GET_CLIENT_BY_ID)
	ResponseEntity<Client> getClientDetailsById(@PathVariable("clientId") Long clientId);
}
