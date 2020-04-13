package com.fxg.demo.validation.validator;

import com.fxg.demo.validation.annotation.CheckCase;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * @CheckCase 注解对应的校验器
 */
public class CheckCaseValidator implements ConstraintValidator<CheckCase, String> {

	private CheckCase.CaseMode caseMode;

	@Override
	public void initialize(CheckCase annotation) {
		this.caseMode = annotation.mode();
	}

	@Override
	public boolean isValid(String v, ConstraintValidatorContext context) {
		return this.caseMode.getAdapter().adaptive(v);
	}
}
