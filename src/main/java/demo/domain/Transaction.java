package demo.domain;

import lombok.Data;

import java.util.Date;

@Data
public final class Transaction {
    private Date time;
    private String country;
    private String city;
    private String theater;
    private String room;
    private int seats;
    private double price;
    private long movieId;
    private String title;
}