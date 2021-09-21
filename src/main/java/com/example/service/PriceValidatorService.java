package com.example.service;

import com.example.domain.checkout.CartItem;

import static com.example.util.CommonUtil.delay;

public class PriceValidatorService {

    public boolean isCartItemInvalid(CartItem cartItem){
        int cartId = cartItem.getItemId();
        //log("isCartItemInvalid : "+ cartItem);
        delay(500);
        return cartId == 7 || cartId == 9 || cartId == 11;
    }
}
