package payroll;

import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {
}

/*
To support interacting with orders in the database, you must define a corresponding Spring Data repository:

Spring Data JPA’s JpaRepository base interface
 */