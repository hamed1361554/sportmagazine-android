package com.mitranetpars.sportmagazine.Cart;

/**
 * Implements this interface for any product which can be added to shopping cart
 */
public interface Saleable {
    double getPrice();

    String getName();
}
