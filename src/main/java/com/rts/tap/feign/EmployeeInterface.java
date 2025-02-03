package com.rts.tap.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.rts.tap.constants.APIConstants;
import com.rts.tap.model.Employee;


@FeignClient("EMPLOYEE-SERVICE")
public interface EmployeeInterface {

    @GetMapping(APIConstants.GET_EMPLOYEE_BY_ID)
    ResponseEntity<Employee> getEmployeeById(@PathVariable Long employeeId);

}
