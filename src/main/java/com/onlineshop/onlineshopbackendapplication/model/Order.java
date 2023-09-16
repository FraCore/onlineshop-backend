package com.onlineshop.onlineshopbackendapplication.model;

import jakarta.persistence.*;

import java.util.Date;

@Entity
@Table(name="orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer order_id;

    private Date order_date;
    private OrderState order_state;


    public Order(Integer order_id, Date order_date, OrderState order_state) {
        this.order_id = order_id;
        this.order_date = order_date;
        this.order_state = order_state;
    }

    public Integer getOrder_id() {
        return order_id;
    }

    public void setOrder_id(Integer order_id) {
        this.order_id = order_id;
    }

    public Date getOrder_date() {
        return order_date;
    }

    public void setOrder_date(Date order_date) {
        this.order_date = order_date;
    }

    public OrderState getOrder_state() {
        return order_state;
    }

    public void setOrder_state(OrderState order_state) {
        this.order_state = order_state;
    }
}
