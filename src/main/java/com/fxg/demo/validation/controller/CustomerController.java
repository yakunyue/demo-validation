package com.fxg.demo.validation.controller;

import com.fxg.demo.validation.annotation.New;
import com.fxg.demo.validation.domain.Customer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Slf4j
@Validated
@RestController
@RequestMapping(value = "/customer")
public class CustomerController {



	@GetMapping(value = "/get")
	public String getCustomer( @NotNull Integer id){
		log.info("id is:{}",id);
		return "success";
	}

	@GetMapping(value = "/name/like")
	public String nameLike( @NotEmpty String name){
		log.info("name is:{}",name);
		return "success";
	}

	@PostMapping(value = "/add", consumes = "application/json;charset=UTF-8")
	public String addCustomer(@Validated(New.class) Customer customer){
		log.info("customer is:{}",customer);
		return "success";
	}


}
