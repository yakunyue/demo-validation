package com.fxg.demo.validation;

import com.fxg.demo.validation.domain.Customer;
import org.junit.BeforeClass;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.event.annotation.BeforeTestClass;

import javax.annotation.PostConstruct;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import javax.validation.groups.Default;
import java.util.Iterator;
import java.util.Set;

import static org.junit.Assert.assertEquals;

class CustomerTests {

	private static Validator validator;

	static {
		ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
		validator = factory.getValidator();
	}

	@Test
	public void nameNotBlank() {
		Customer customer = new Customer();
		customer.setAge(19);
		Set<ConstraintViolation<Customer>> result = validator.validate(customer, Default.class);
		this.printError(result);

	}

	public void printError(Set<ConstraintViolation<Customer>> result) {
		System.out.println("错误数量为：" + result.size());
		if (!result.isEmpty()) {
			System.out.println("错误信息如下：");
			Iterator<ConstraintViolation<Customer>> iterator = result.iterator();
			while (iterator.hasNext()) {
				ConstraintViolation<Customer> next = iterator.next();
				System.out.println(
						"属性名：" + next.getPropertyPath() + ",校验结果：" + next.getMessage() + ",传入值：" + next.getInvalidValue());
			}
		}
	}
}
