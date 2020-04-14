package com.fxg.demo.validation.domain;

import com.fxg.demo.validation.annotation.CheckCase;
import com.fxg.demo.validation.annotation.ConditionNotNull;
import com.fxg.demo.validation.annotation.group.Custom;
import com.fxg.demo.validation.annotation.group.KeyId;
import com.fxg.demo.validation.annotation.group.New;
import com.fxg.demo.validation.annotation.group.Update;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import java.time.LocalDate;
import java.util.List;

@Data
@ConditionNotNull(targetFileName = "birthDay",dependFileNames = {"age"},groups = {Custom.class})
public class Customer {

	@KeyId(groups = {Update.class})
	private Integer id;

	@CheckCase(mode = CheckCase.CaseMode.UPPER,groups = {Custom.class})
	private String memberNo;

	@NotEmpty(message = "nickname 不能为空", groups = {New.class})
	private String nickname;

	@Pattern(regexp = "^([1][3,4,5,6,7,8,9])\\d{9}$")
	private String telephoneNo;

	@NotBlank
	private String name;

	private Integer age;

	private LocalDate birthDay;

	@Valid
	private Address address;

	@Valid
	private List<Address> historyAddressList;
}
