package com.feedback.custom_exception;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class AlreadyExistExceptonTest {

  @Test
  void testDefaultConstructor() {
    DepartmentAlreadyExistExcepton exception = new DepartmentAlreadyExistExcepton();

    assertEquals("Already exist", exception.getMessage());
  }

}
