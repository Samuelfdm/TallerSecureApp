package edu.eci.arep.TallerBono.model;

import jakarta.persistence.*;

@Entity
@Table(name = "properties")
public class Property {
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Long id;
    private String address;
    private long price;
    private int size;
    private String description;

    public Property(String address, long price, int size,String description) {
        this.address = address;
        this.price = price;
        this.size = size;
        this.description = description;
    }

    public Property() {

    }

    @Override
    public String toString() {
        return String.format(
                "Property[id=%d, address='%s', price='%d', size='%d', description='%s']",
                id, address, price, size, description);
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public long getPrice() {
        return price;
    }

    public void setPrice(long price) {
        this.price = price;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}