package payroll;

class OrderNotFoundException extends RuntimeException {
    public OrderNotFoundException(final Long id) {
        super("Order with id: " + id + " not found.");
    }
}
