package payroll;

import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
class EmployeeController {

    private final EmployeeRepository repository;

    private final EmployeeModelAssembler assembler;

    EmployeeController(final EmployeeRepository repository, final EmployeeModelAssembler assembler) {
        this.repository = repository;
        this.assembler = assembler;
    }

    @GetMapping("/employees")
    CollectionModel<EntityModel<Employee>> all() {
        List<EntityModel<Employee>> employees = repository.findAll().stream()
                .map(assembler::toModel)
                .toList();
        return CollectionModel.of(employees,
                linkTo(methodOn(EmployeeController.class).all()).withSelfRel());
    }

    @PostMapping("/employees")
    ResponseEntity<?> newEmployee(@RequestBody Employee newEmployee) {
        EntityModel<Employee> entityModel = assembler.toModel(repository.save(newEmployee));
        return ResponseEntity
                .created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri())
                .body(entityModel);
    }
/*
The new Employee object is saved as before. But the resulting object is wrapped using the EmployeeModelAssembler.

Spring MVC’s ResponseEntity is used to create an HTTP 201 Created status message. This type of response typically
includes a Location response header, and we use the URI derived from the model’s self-related link.

Additionally, return the model-based version of the saved object.

With these tweaks in place, you can use the same endpoint to create a new employee resource, and use the legacy name
field:
*/

    @GetMapping("/employees/{id}")
    EntityModel<Employee> one(@PathVariable Long id) {
        Employee employee = repository.findById(id)
                .orElseThrow(() -> new EmployeeNotFoundException(id));
        return assembler.toModel(employee);
    }

    @PutMapping("/employees/{id}")
    ResponseEntity<?> replaceEmployee(@RequestBody Employee newEmployee, @PathVariable Long id) {
        Employee updatedEmployee = repository.findById(id)
                .map(e -> {
                    e.setName(newEmployee.getName());
                    e.setRole(newEmployee.getRole());
                    return repository.save(e);
                })
                .orElseGet(() -> {
                    newEmployee.setId(id);
                    return repository.save(newEmployee);
                });

        EntityModel<Employee> entityModel = assembler.toModel(updatedEmployee);

        return ResponseEntity
                .created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri())
                .body(entityModel);
    }
    /*
The Employee object built from the save() operation is then wrapped using the EmployeeModelAssembler into an
EntityModel<Employee> object. Using the getRequiredLink() method, you can retrieve the Link created by the
EmployeeModelAssembler with a SELF rel. This method returns a Link which must be turned into a URI with the
toUri method.

Since we want a more detailed HTTP response code than 200 OK, we will use Spring MVC’s ResponseEntity wrapper.
It has a handy static method created() where we can plug in the resource’s URI. It’s debatable if HTTP 201 Created
carries the right semantics since we aren’t necessarily "creating" a new resource. But it comes pre-loaded with a
Location response header, so run with it.
     */

    @DeleteMapping("/employees/{id}")
    ResponseEntity<?> deleteMapping(@PathVariable Long id) {
        repository.deleteById(id);

        return ResponseEntity.noContent().build();
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
