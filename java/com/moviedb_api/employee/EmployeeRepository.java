package com.moviedb_api.employee;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository("EmployeeRepository")
public interface EmployeeRepository extends CrudRepository<Employee, Integer> {
    Optional<Employee> findById(Integer id);

}
