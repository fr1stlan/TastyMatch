package com.cao.tastymatch;

public class Receipts {

    private String title, image, description, kitchen;

    public Receipts() {}

    public Receipts(String title, String image, String description, String kitchen) {
        this.title = title;
        this.image = image;
        this.description = description;
        this.kitchen = kitchen;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getKitchen() {
        return kitchen;
    }

    public void setKitchen(String kitchen) {
        this.kitchen = kitchen;
    }
}
