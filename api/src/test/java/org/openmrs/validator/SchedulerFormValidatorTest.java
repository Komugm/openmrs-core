/**
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 *
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */
package org.openmrs.validator;

import org.junit.Assert;
import org.junit.Test;
import org.openmrs.scheduler.TaskDefinition;
import org.openmrs.test.Verifies;
import org.springframework.validation.BindException;
import org.springframework.validation.Errors;

/**
 * Tests methods on the {@link SchedulerFormValidator} class.
 */
public class SchedulerFormValidatorTest {
	
	/**
	 * @see {@link SchedulerFormValidator#validate(Object,Errors)}
	 */
	@Test
	@Verifies(value = "should fail validation if name is null or empty or whitespace", method = "validate(Object,Errors)")
	public void validate_shouldFailValidationIfNameIsNullOrEmptyOrWhitespace() throws Exception {
		TaskDefinition def = new TaskDefinition();
		def.setName(null);
		def.setRepeatInterval(3600000L);
		def.setTaskClass("org.openmrs.scheduler.tasks.HelloWorldTask");
		
		Errors errors = new BindException(def, "def");
		new SchedulerFormValidator().validate(def, errors);
		Assert.assertTrue(errors.hasFieldErrors("name"));
		
		def.setName("");
		errors = new BindException(def, "def");
		new SchedulerFormValidator().validate(def, errors);
		Assert.assertTrue(errors.hasFieldErrors("name"));
		
		def.setName(" ");
		errors = new BindException(def, "def");
		new SchedulerFormValidator().validate(def, errors);
		Assert.assertTrue(errors.hasFieldErrors("name"));
	}
	
	/**
	 * @see {@link SchedulerFormValidator#validate(Object,Errors)}
	 */
	@Test
	@Verifies(value = "should fail validation if taskClass is empty or whitespace", method = "validate(Object,Errors)")
	public void validate_shouldFailValidationIfTaskClassIsEmptyOrWhitespace() throws Exception {
		TaskDefinition def = new TaskDefinition();
		def.setName("Chores");
		def.setRepeatInterval(3600000L);
		def.setTaskClass("");
		
		Errors errors = new BindException(def, "def");
		new SchedulerFormValidator().validate(def, errors);
		Assert.assertTrue(errors.hasFieldErrors("taskClass"));
		
		def.setTaskClass(" ");
		errors = new BindException(def, "def");
		new SchedulerFormValidator().validate(def, errors);
		Assert.assertTrue(errors.hasFieldErrors("taskClass"));
	}
	
	/**
	 * @see {@link SchedulerFormValidator#validate(Object,Errors)}
	 */
	@Test
	@Verifies(value = "should fail validation if repeatInterval is null or empty or whitespace", method = "validate(Object,Errors)")
	public void validate_shouldFailValidationIfRepeatIntervalIsNullOrEmptyOrWhitespace() throws Exception {
		TaskDefinition def = new TaskDefinition();
		def.setName("Chores");
		def.setTaskClass("org.openmrs.scheduler.tasks.HelloWorldTask");
		
		Errors errors = new BindException(def, "def");
		new SchedulerFormValidator().validate(def, errors);
		Assert.assertTrue(errors.hasFieldErrors("repeatInterval"));
		
		def.setTaskClass(" ");
		errors = new BindException(def, "def");
		new SchedulerFormValidator().validate(def, errors);
		Assert.assertTrue(errors.hasFieldErrors("repeatInterval"));
	}
	
	/**
	 * @see {@link SchedulerFormValidator#validate(Object,Errors)}
	 */
	@Test
	@Verifies(value = "should fail validation if class is not instance of Task", method = "validate(Object,Errors)")
	public void validate_shouldFailValidationIfClassIsNotInstanceOfTask() throws Exception {
		TaskDefinition def = new TaskDefinition();
		def.setName("Chores");
		def.setRepeatInterval(3600000L);
		def.setTaskClass("org.openmrs.Obs");
		
		Errors errors = new BindException(def, "def");
		new SchedulerFormValidator().validate(def, errors);
		
		Assert.assertTrue(errors.hasFieldErrors("taskClass"));
		Assert.assertEquals("Scheduler.taskForm.classDoesNotImplementTask", errors.getFieldError("taskClass").getCode());
	}
	
	/**
	 * @see {@link SchedulerFormValidator#validate(Object,Errors)}
	 */
	@Test
	@Verifies(value = "should fail validation if class is not accessible", method = "validate(Object,Errors)")
	public void validate_shouldFailValidationIfClassIsNotAccessible() throws Exception {
		TaskDefinition def = new TaskDefinition();
		def.setName("Chores");
		def.setRepeatInterval(3600000L);
		def.setTaskClass("???"); //TODO: Find a way to trigger an IllegalAccessException
		
		Errors errors = new BindException(def, "def");
		new SchedulerFormValidator().validate(def, errors);
		
		Assert.assertTrue(errors.hasFieldErrors("taskClass"));
		Assert.assertEquals("Scheduler.taskForm.classNotFoundException", errors.getFieldError("taskClass").getCode());
	}
	
	/**
	 * @see {@link SchedulerFormValidator#validate(Object,Errors)}
	 */
	@Test
	@Verifies(value = "should fail validation if class cannot be instantiated", method = "validate(Object,Errors)")
	public void validate_shouldFailValidationIfClassCannotBeInstantiated() throws Exception {
		TaskDefinition def = new TaskDefinition();
		def.setName("Chores");
		def.setRepeatInterval(3600000L);
		def.setTaskClass("org.openmrs.BaseOpenmrsData");
		
		Errors errors = new BindException(def, "def");
		new SchedulerFormValidator().validate(def, errors);
		
		Assert.assertTrue(errors.hasFieldErrors("taskClass"));
		Assert.assertEquals("Scheduler.taskForm.instantiationException", errors.getFieldError("taskClass").getCode());
	}
	
	/**
	 * @see {@link SchedulerFormValidator#validate(Object,Errors)}
	 */
	@Test
	@Verifies(value = "sshould fail validation if class not found", method = "validate(Object,Errors)")
	public void validate_shouldFailValidationIfClassNotFound() throws Exception {
		TaskDefinition def = new TaskDefinition();
		def.setName("Chores");
		def.setRepeatInterval(3600000L);
		def.setTaskClass("org.openmrs.ScaryRobot");
		
		Errors errors = new BindException(def, "def");
		new SchedulerFormValidator().validate(def, errors);
		
		Assert.assertTrue(errors.hasFieldErrors("taskClass"));
		Assert.assertEquals("Scheduler.taskForm.classNotFoundException", errors.getFieldError("taskClass").getCode());
	}
	
	/**
	 * @see {@link SchedulerFormValidator#validate(Object,Errors)}
	 */
	@Test
	@Verifies(value = "should pass validation if all required fields have proper values", method = "validate(Object,Errors)")
	public void validate_shouldPassValidationIfAllRequiredFieldsHaveProperValues() throws Exception {
		TaskDefinition def = new TaskDefinition();
		def.setName("Chores");
		def.setRepeatInterval(3600000L);
		def.setTaskClass("org.openmrs.scheduler.tasks.HelloWorldTask");
		
		Errors errors = new BindException(def, "def");
		new SchedulerFormValidator().validate(def, errors);
		
		Assert.assertFalse(errors.hasErrors());
	}
}
