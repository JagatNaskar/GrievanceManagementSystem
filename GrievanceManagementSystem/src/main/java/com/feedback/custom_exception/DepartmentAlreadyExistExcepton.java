package com.feedback.custom_exception;

/**
 * DepartmentAlreadyExistExcepton class.
 *
 * @author jagat.
 */
public class DepartmentAlreadyExistExcepton extends RuntimeException {
  /**
   *serialVersionUID.
   */
  private static final long serialVersionUID = 1L;

  /**
   * no argument constructor.
   */
  public DepartmentAlreadyExistExcepton() {
    super("Department Already exist");
  }
}
