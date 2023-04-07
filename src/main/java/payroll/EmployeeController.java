package payroll;

import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
class EmployeeController {

    private final EmployeeRepository repository;

    EmployeeController(final EmployeeRepository repository) {
        this.repository = repository;
    }

    @GetMapping("/employees")
//    List<Employee> all() {
//        return repository.findAll();
//    }
    CollectionModel<EntityModel<Employee>> all() {
        List<EntityModel<Employee>> employees = repository.findAll().stream()
                .map(employee -> EntityModel.of(employee,
                        linkTo(methodOn(EmployeeController.class).one(employee.getId())).withSelfRel(),
                        linkTo(methodOn(EmployeeController.class).all()).withRel("employees")))
                .collect(Collectors.toList());

        return CollectionModel.of(employees,
                linkTo(methodOn(EmployeeController.class).all()).withSelfRel());
    }
    /*
Wow! That method, which used to just be repository.findAll(), is all grown up! Not to worry. Let’s unpack it.

CollectionModel<> is another Spring HATEOAS container; it’s aimed at encapsulating collections of resources—instead
of a single resource entity, like EntityModel<> from earlier. CollectionModel<>, too, lets you include links.

Don’t let that first statement slip by. What does "encapsulating collections" mean? Collections of employees?

Not quite.

Since we’re talking REST, it should encapsulate collections of employee resources.

That’s why you fetch all the employees, but then transform them into a list of EntityModel<Employee> objects.
(Thanks Java 8 Streams!)
     */

    @PostMapping("/employees")
    Employee newEmployee(@RequestBody Employee newEmployee) {
        return repository.save(newEmployee);
    }

    @GetMapping("/employees/{id}")
//      Employee one(@PathVariable Long id) {
//        return repository.findById(id).orElseThrow(() -> new EmployeeNotFoundException(id));
//    }
    EntityModel<Employee> one(@PathVariable Long id) {
        Employee employee = repository.findById(id)
                .orElseThrow(() -> new EmployeeNotFoundException(id));

        return EntityModel.of(employee,
                linkTo(methodOn(EmployeeController.class).one(id)).withSelfRel(),
                linkTo(methodOn(EmployeeController.class).all()).withRel("employees"));
    }
    /*
This is very similar to what we had before, but a few things have changed:

The return type of the method has changed from Employee to EntityModel<Employee>. EntityModel<T> is a generic container
from Spring HATEOAS that includes not only the data but a collection of links.

linkTo(methodOn(EmployeeController.class).one(id)).withSelfRel() asks that Spring HATEOAS build a link to the
EmployeeController 's one() method, and flag it as a self link.

linkTo(methodOn(EmployeeController.class).all()).withRel("employees") asks Spring HATEOAS to build a link to the
aggregate root, all(), and call it "employees".
     */

    @PutMapping("/employee/{id}")
    Employee replaceEmployee(@RequestBody Employee newEmployee, @PathVariable Long id) {
        return repository.findById(id)
                .map(e -> {
                    e.setName(newEmployee.getName());
                    e.setRole(newEmployee.getRole());
                    return repository.save(e);
                })
                .orElseGet(() -> {
                    newEmployee.setId(id);
                    return repository.save(newEmployee);
                });
    }

    @DeleteMapping("/employee/{id}")
    void deleteMapping(@PathVariable Long id) {
        repository.deleteById(id);
    }
}

/*
@RestController indicates that the data returned by each method will be written straight into the response body instead
of rendering a template.

An EmployeeRepository is injected by constructor into the controller.

We have routes for each operation (@GetMapping, @PostMapping, @PutMapping and @DeleteMapping, corresponding to HTTP GET,
POST, PUT, and DELETE calls). (NOTE: It’s useful to read each method and understand what they do.)

EmployeeNotFoundException is an exception used to indicate when an employee is looked up but not found.
 */
