package payroll;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

import java.util.Objects;

@Entity
class Employee {
    @Id
    @GeneratedValue
    private Long id;
    private String firstName;
    private String lastName;
    private String role;

    Employee() {}

    Employee (String firstName, String lastName, String role) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.role = role;
    }

    public Long getId() {
        return id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public String getName() {
        return this.firstName + " " + this.lastName;
    }

    public void setName(final String name) {
        String[] parts = name.split(" ");
        this.firstName = parts[0];
        this.lastName = parts[1];
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(final String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(final String lastName) {
        this.lastName = lastName;
    }

    public String getRole() {
        return role;
    }

    public void setRole(final String role) {
        this.role = role;
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id, this.firstName, this.lastName, this.role);
    }

    @Override
    public boolean equals(final Object obj) {
        if(this == obj) return true;
        if(!(obj instanceof Employee)) return false;
        Employee employee = (Employee) obj;
        return Objects.equals(this.id, employee.id) && Objects.equals(this.firstName, employee.firstName)
                && Objects.equals(this.lastName, employee.lastName) && Objects.equals(this.role, employee.role);
    }

    @Override
    public String toString() {
        return "Rmployee{" + "id=" + this.id + ", firstName='" + this.firstName + '\'' + ", lastName='" + this.lastName
                + '\'' + ", role='" + this.role + '\'' + '}';
    }
}

/*
Despite being small, this Java class contains much:

@Entity is a JPA annotation to make this object ready for storage in a JPA-based data store.

id, name, and role are attributes of our Employee domain object. id is marked with more JPA annotations to indicate it’s
the primary key and automatically populated by the JPA provider.

a custom constructor is created when we need to create a new instance, but don’t yet have an id.

With this domain object definition, we can now turn to Spring Data JPA to handle the tedious database interactions.

Spring Data JPA repositories are interfaces with methods supporting creating, reading, updating, and deleting records
against a back end data store. Some repositories also support data paging, and sorting, where appropriate. Spring Data
synthesizes implementations based on conventions found in the naming of the methods in the interface.
 */