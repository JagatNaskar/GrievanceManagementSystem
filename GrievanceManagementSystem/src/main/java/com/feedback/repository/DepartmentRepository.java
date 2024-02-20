package com.feedback.repository;


import com.feedback.entities.Department;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository interface for managing Department entities.
 */
@Repository
public interface DepartmentRepository extends
    JpaRepository<Department, Integer> {

  /**
  * Find a department by its name.
  *
  * @param deptName The name of the department to retrieve.
  *
  * @return The department with the specified name.
  */
  Department findByDeptName(String deptName);

}
