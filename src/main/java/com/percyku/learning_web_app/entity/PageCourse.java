package com.percyku.learning_web_app.entity;


import java.util.List;

public class PageCourse {


    private Integer id;
    private String title;
    private String description;
    private int price;
    private User instructor;
    private List<Long> students;
    private boolean registered ;

    public PageCourse(Integer id, String title, String description, int price,  List<Long> students) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.price = price;
        this.students = students;
    }
    public PageCourse(Integer id, String title, String description, int price, User instructor, List<Long> students) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.price = price;
        this.instructor = instructor;
        this.students = students;
    }
    public PageCourse(Integer id, String title, String description, int price, User instructor, List<Long> students, boolean registered) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.price = price;
        this.instructor = instructor;
        this.students = students;
        this.registered= registered;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public User getInstructor() {
        return instructor;
    }

    public void setInstructor(User instructor) {
        this.instructor = instructor;
    }

    public List<Long> getStudents() {
        return students;
    }

    public void setStudents(List<Long> students) {
        this.students = students;
    }

    public boolean isRegistered() {
        return registered;
    }

    public void setRegistered(boolean registered) {
        this.registered = registered;
    }

    @Override
    public String toString() {
        return "PageCourse{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", price=" + price +
                ", instructor=" + instructor +
                ", students=" + students +
                '}';
    }
}
