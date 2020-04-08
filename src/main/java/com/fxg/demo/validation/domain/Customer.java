package com.fxg.demo.validation.domain;

import com.fxg.demo.validation.annotation.ConditionNotNull;
import com.fxg.demo.validation.annotation.New;
import com.fxg.demo.validation.annotation.Update;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Data
public class Customer {

	@NotNull(message = "id 不能为空", groups = {Update.class})
	private Integer id;

	@NotEmpty(message = "nickname 不能为空", groups = {New.class})
	private String nickname;

	@Valid
	private Address address;

	private String name;

	private Integer age;

	@ConditionNotNull(targetFileName = "birthDay",dependFileNames = {"age"})
	private LocalDate birthDay;
}
