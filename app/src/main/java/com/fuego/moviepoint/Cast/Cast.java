package com.fuego.moviepoint.Cast;

public class Cast {

    private String name;
    private String role;
    private String image;

    public Cast() {
    }

    public Cast(String name, String role, String image) {
        this.name = name;
        this.role = role;
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    @Override
    public String toString() {
        return "Cast{" +
                "name='" + name + '\'' +
                ", role='" + role + '\'' +
                '}';
    }
}
