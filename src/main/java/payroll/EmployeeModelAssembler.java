package payroll;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

@Component
class EmployeeModelAssembler implements RepresentationModelAssembler<Employee, EntityModel<Employee>> {
    @Override
    public EntityModel<Employee> toModel(final Employee employee) {

        return EntityModel.of(employee,
                linkTo(methodOn(EmployeeController.class).one(employee.getId())).withSelfRel(),
                linkTo(methodOn(EmployeeController.class).all()).withRel("employees"));
    }
}

/*
This simple interface has one method: toModel(). It is based on converting a non-model object (Employee) into a
model-based object (EntityModel<Employee>).

All the code you saw earlier in the controller can be moved into this class. And by applying Spring Framework’s
@Component annotation, the assembler will be automatically created when the app starts.

Spring HATEOAS’s abstract base class for all models is RepresentationModel. But for simplicity, I recommend using
EntityModel<T> as your mechanism to easily wrap all POJOs as models.
 */
