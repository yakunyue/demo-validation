package com.fxg.demo.validation.domain;

import com.fxg.demo.validation.annotation.group.Custom;
import com.fxg.demo.validation.annotation.group.New;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class Address {

	private Integer id;

	@NotNull(message = "longitude 不能为空")
	private String longitude;

	@NotNull(message = "latitude 不能为空",groups = {Custom.class})
	private String latitude;

	@NotBlank(message = "description 不能为空",groups = {New.class})
	private String description;
}
