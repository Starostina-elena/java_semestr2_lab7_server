package org.lia.models;

import java.io.Serializable;
import java.util.Objects;

/**Organization class*/
public class Organization implements Comparable<Organization>, Serializable {
    private static final long serialVersionUID = 17464768755190753L;
    private Long id; //Значение поля должно быть больше 0, Значение этого поля должно быть уникальным, Значение этого поля должно генерироваться автоматически
    private String name; //Поле не может быть null, Строка не может быть пустой
    private String fullName; //Поле может быть null
    private Integer employeesCount; //Поле может быть null, Значение поля должно быть больше 0

    private static Long currentId = 1L;

    public Organization(String name, String fullName, Integer employeesCount) {
        this.setName(name);
        this.setFullName(fullName);
        this.setEmployeesCount(employeesCount);
        id = currentId++;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    /**sets name to the organization. Name cannot be null or empty*/
    public void setName(String name) throws IllegalArgumentException {
        if (name.isEmpty()) {
            throw new IllegalArgumentException("name cannot be empty");
        }
        this.name = name;
    }

    public String getFullName() {
        return fullName;
    }

    /**Sets fullname to the organization. Field can be null*/
    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public Integer getEmployeesCount() {
        return employeesCount;
    }

    /**Sets employeesCount. Field can pe null but its value should be greater than 0*/
    public void setEmployeesCount(Integer employeesCount) {
        if (employeesCount > 0) {
            this.employeesCount = employeesCount;
        } else if (employeesCount == 0) {
            this.employeesCount = null;
        } else {
            throw new IllegalArgumentException("employeesCount should be more than 0");
        }
    }

    public static void updateId(long newId) {
        currentId = newId + 1;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Organization that)) return false;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(fullName, that.fullName) && Objects.equals(employeesCount, that.employeesCount);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, fullName, employeesCount);
    }

    @Override
    public String toString() {
        return "Organization{" +
                "name='" + name + '\'' +
                ", fullName='" + fullName + '\'' +
                ", employeesCount=" + employeesCount +
                '}';
    }

    @Override
    public int compareTo(Organization o) {
        if (o == null) return 1;
        if (this.id > o.getId()) {
            return 1;
        } else if (Objects.equals(this.id, o.getId())) {
            return 0;
        } else {
            return -1;
        }
    }

}
