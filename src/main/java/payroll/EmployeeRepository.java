package payroll;

import org.springframework.data.jpa.repository.JpaRepository;

interface EmployeeRepository extends JpaRepository<Employee, Long> {
}

/*
Spring makes accessing data easy. By simply declaring the following EmployeeRepository interface we automatically will
be able to

Create new Employees

Update existing ones

Delete Employees

Find Employees (one, all, or search by simple or complex properties)

To get all this free functionality, all we had to do was declare an interface which extends Spring Data JPA’s
JpaRepository, specifying the domain type as Employee and the id type as Long.
 */
