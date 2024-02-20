package com.feedback.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.feedback.entities.Department;
import com.feedback.payloads.department_dto.AddDepartemntDto;
import com.feedback.payloads.department_dto.DepartmentListDto;
import com.feedback.service.DepartmentService;

/**
 * DepartmentController.
 *
 * @author jagat
 * @version 001.
 */
@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/dept")
public class DepartmentController {

  /**
   * DepartmentService object.
   */
  @Autowired
  private DepartmentService departmentService;

  /**
   * DepartmentController.
   *
   * @param departmentService2
   *
   */
  public DepartmentController(final DepartmentService departmentService2) {
    this.departmentService = departmentService2;
  }

  /**
   * Add department by admin.
   *
   * @param dept1
   *
   * @return ResponseEntity of department list.
   */
  @PostMapping("/addDept")
  public ResponseEntity<Department> addDept(@Valid @RequestBody final AddDepartemntDto departmentInDto ){
	  Department responseDepartmentData = departmentService.addDept(departmentInDto);
	  return new ResponseEntity<Department>(responseDepartmentData,HttpStatus.CREATED);
  }

  /**
   * Get all department.
   *
   * @return list of department.
   */
  @GetMapping("/allDepartment")
  public ResponseEntity<List<DepartmentListDto>> getAllDepartments() {
    List<DepartmentListDto> departmentList = departmentService
        .getAllDepartments();
      return ResponseEntity.ok(departmentList);
  }

  /**
   * Get all department.
   *
   *@param currentPage
   *
   * @return list of department.
   */
  @GetMapping("/allDepartment/{currentPage}")
  public ResponseEntity<List<DepartmentListDto>> getAllDepartments(
      @PathVariable("currentPage") final Integer currentPage) {
    List<DepartmentListDto> departmentList = departmentService
        .getAllDepartments(currentPage);
      return ResponseEntity.ok(departmentList);
  }

  /**
   * Delete a department by its name.
   *
   * @param deptName
   *
   * @return deleted Department name.
   */
  @PostMapping("/deleteDept/{deptName}")
  public ResponseEntity<?> deleteDeptByName(
      @PathVariable final String deptName) {
    String deletedDept = departmentService.deleteDept(deptName);
    return ResponseEntity.status(HttpStatus.OK).body(deletedDept);
  }
}
