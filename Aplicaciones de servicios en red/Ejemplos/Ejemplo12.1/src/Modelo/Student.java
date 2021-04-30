package Modelo;

import java.io.Serializable;

public class Student implements Serializable {

    private static final long serialVersionUID = 5950169519310163575L;
    private int age;
    private String name;

    public Student(int age, String name) {
        this.age = age;
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setId(int age) {
        this.age = age;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String toString() {
        return "Age = " + getAge() + " ; Name = " + getName();
    }
}
