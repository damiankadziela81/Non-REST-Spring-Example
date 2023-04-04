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
    private String name;
    private String role;

    Employee() {}

    Employee (String name, String role) {
        this.name = name;
        this.role = role;
    }

    Long getId() {
        return id;
    }

    void setId(final Long id) {
        this.id = id;
    }

    String getName() {
        return name;
    }

    void setName(final String name) {
        this.name = name;
    }

    String getRole() {
        return role;
    }

    void setRole(final String role) {
        this.role = role;
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id, this.name, this.role);
    }

    @Override
    public boolean equals(final Object obj) {
        if(this == obj) return true;
        if(!(obj instanceof Employee)) return false;
        Employee employee = (Employee) obj;
        return Objects.equals(this.id, employee.id) && Objects.equals(this.name, employee.name)
                && Objects.equals(this.role, employee.role);
    }

    @Override
    public String toString() {
        return "Rmployee{" + "id=" + this.id + ", name='" + this.name + '\'' + ", role='" + this.role + '\'' + '}';
    }
}
