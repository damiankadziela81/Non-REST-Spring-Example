package payroll;

class EmployeeNotFoundException extends RuntimeException {
    public EmployeeNotFoundException(final Long id) {
        super("Employee with id: " + id + " not found.");
    }
}
