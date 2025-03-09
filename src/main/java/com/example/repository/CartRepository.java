package com.example.repository;

import com.example.model.Cart;
import com.example.model.Product;
import com.example.model.User;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Repository
@SuppressWarnings("rawtypes")
public class CartRepository extends MainRepository<Cart> {

    public CartRepository() {}

    @Override
    protected String getDataPath() {
        return "src/main/java/com/example/data/carts.json";
    }

    @Override
    protected Class<Cart[]> getArrayType() {
        return Cart[].class;
    }

    public Cart addCart(Cart cart){
        save(cart);
        return cart;
    }

    public ArrayList<Cart> getCarts(){
        return findAll();
    }

    public Cart getCartById(UUID id){
        return findAll().stream()
                .filter(cart -> cart.getId().equals(id))
                .findFirst()
                .orElse(null);
    }

    public Cart getCartByUserId(UUID userId){
        return findAll().stream()
                .filter(cart -> cart.getUserId().equals(userId))
                .findFirst()
                .orElse(null);
    }

    // There is possibly two approaches, one is to allow duplicates (since there is no
    // indicator of the amount of a product), or to only allow for one instance of a
    // product to be in the cart at a given time. The following method allows for duplicates.
    public void addProductToCart(UUID cartId, Product product){
        Cart cart = getCartById(cartId);
        if (cart == null) {return;}
        cart.getProducts().add(product);
        save(cart);
    }

    public void deleteProductFromCart(UUID cartId, Product product){
        Cart cart = getCartById(cartId);
        if (cart == null) {return;}
        // Handle if product not in cart
        // Needs Product class to be implemented
        // removeIf removes all occurences
        // need to loop over array and remove manually if we want to remove first occ only

//        cart.getProducts().removeIf(p -> p.getId().equals(product.getId));
        save(cart);
    }

    public void deleteCartById(UUID cartId){
        List<Cart> carts = findAll();
        boolean removed = carts.removeIf(cart -> cart.getId().equals(cartId));
        if (!removed) {
            throw new RuntimeException("Cart with ID " + cartId + " not found");
        }
        saveAll(new ArrayList<>(carts));
    }
}
