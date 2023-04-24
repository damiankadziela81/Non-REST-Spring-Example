package payroll;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

@Component
class OrderModelAssembler implements RepresentationModelAssembler<Order, EntityModel<Order>> {


    @Override
    public EntityModel<Order> toModel(Order order) {

        // Unconditional links to single-item resource and aggregate root

        EntityModel<Order> orderModel = EntityModel.of(order,
                linkTo(methodOn(OrderController.class).one(order.getId())).withSelfRel(),
                linkTo(methodOn(OrderController.class).all()).withRel("orders"));

        // Conditional links based on state of the order

        if (order.getStatus() == Status.IN_PROGRESS) {
            orderModel.add(linkTo(methodOn(OrderController.class).cancel(order.getId())).withRel("cancel"));
            orderModel.add(linkTo(methodOn(OrderController.class).complete(order.getId())).withRel("complete"));
        }

        return orderModel;
    }
}

/*
This resource assembler always includes the self link to the single-item resource as well as a link back to the
aggregate root. But it also includes two conditional links to OrderController.cancel(id) as well as
OrderController.complete(id) (not yet defined). These links are ONLY shown when the order’s status is
Status.IN_PROGRESS.

If clients can adopt HAL and the ability to read links instead of simply reading the data of plain old JSON,
they can trade in the need for domain knowledge about the order system. This naturally reduces coupling between
client and server. And it opens the door to tuning the flow of order fulfillment without breaking clients in
the process.
 */
