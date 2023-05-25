package com.example.MainProject.entities.users;

import com.example.MainProject.entities.Orders.Orders;
import com.example.MainProject.entities.cart.Cart;
import com.example.MainProject.entities.product.ProductReview;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrimaryKeyJoinColumn;
import jakarta.persistence.criteria.Order;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@PrimaryKeyJoinColumn(name = "userId")
public class Customer extends User {
   private String contact;

   @OneToMany(mappedBy = "customer",cascade = CascadeType.ALL,orphanRemoval = true)
   List<Address> addressList;

   @OneToMany(mappedBy = "customer",cascade = CascadeType.ALL,orphanRemoval = true)
   List<Orders>orders=new ArrayList<>();//taken list so that we can take duplicate order.

   @OneToMany(mappedBy = "customer",cascade = CascadeType.ALL,orphanRemoval = true)
   List<ProductReview>productReviewList;

   @OneToMany(mappedBy ="customer",cascade = CascadeType.ALL,orphanRemoval = true)
   List<Cart>cartList;
}
