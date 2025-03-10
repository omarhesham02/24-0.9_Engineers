package com.example.service;

import com.example.model.Cart;
import com.example.model.Order;
import com.example.model.Product;
import com.example.model.User;
import com.example.repository.CartRepository;
import com.example.repository.MainRepository;
import com.example.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class UserService extends MainService<User> {

    private final UserRepository userRepository;
    private final CartRepository cartRepository;
    private final MainRepository<Cart> cartMainRepository;

    @Autowired
    public UserService(UserRepository userRepository, CartRepository cartRepository, MainRepository<User> userMainRepository, MainRepository<Cart> cartMainRepository) {
        this.userRepository = userRepository;
        this.cartRepository = cartRepository;
        this.cartMainRepository = cartMainRepository;
    }

    public User addUser(User user) {
        if (user.getId() == null || user.getName() == null) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "User cannot be null");
        }

        User existingUser = userRepository.getUserById(user.getId());

        if (existingUser != null) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "User with this ID already exists");
        }

        userRepository.addUser(user);
        return user;
    }

    public ArrayList<User> getUsers() {
        return (ArrayList<User>) userRepository.getAllUsers();
    }

    public User getUserById(UUID id) {
        if (id == null) {
            throw new IllegalArgumentException("User ID cannot be null");
        }
        return userRepository.getUserById(id);
    }

    public List<Order> getOrdersByUserId(UUID userId) {
        if (userId == null) {
            throw new IllegalArgumentException("User ID cannot be null");
        }

        User user = userRepository.getUserById(userId);

        if (user == null) {
            return null;
        }

        return userRepository.getOrdersByUserId(userId);
    }


    // TODO: Check addOrderToUser(UUID userId) method
    public void addOrderToUser(UUID userId) {
        if (userId == null)
            throw new IllegalArgumentException("User ID cannot be null");

        Cart cart = cartRepository.getCartByUserId(userId);
        Order order = new Order(userId,
                cart.getProducts().stream().mapToDouble(Product::getPrice).sum(),
                cart.getProducts());

        emptyCart(userId);
        userRepository.addOrderToUser(userId, order);
    }

    // TODO: Check emptyCart(UUID userId) method
    public void emptyCart(UUID userId) {
        if (userId == null) {
            throw new IllegalArgumentException("User ID cannot be null");
        }

        Cart cart = cartRepository.getCartByUserId(userId);
        if (cart == null) {
            throw new HttpClientErrorException(HttpStatus.NOT_FOUND, "Cart not found");
        }

        cart.setProducts(new ArrayList<>());
        cartMainRepository.override(cart);
    }

    // TODO: Why is .getOrderById not finding the order?!
    public void removeOrderFromUser(UUID userId, UUID orderId) {

        if (userId == null) {
            throw new IllegalArgumentException("User ID cannot be null");
        }

        if (orderId == null) {
            throw new IllegalArgumentException("Order ID cannot be null");
        }

        User user = userRepository.getUserById(userId);
        Order order = userRepository.getOrderById(userId, orderId);

        if (user == null) {
            throw new HttpClientErrorException(HttpStatus.NOT_FOUND, "User not found");
        }

        if (order == null) {
            throw new HttpClientErrorException(HttpStatus.NOT_FOUND, "Order not found");
        }

        userRepository.removeOrderFromUser(userId, orderId);
    }

    public void deleteUserById(UUID userId) {
        if (userId == null) {
            throw new IllegalArgumentException("User ID cannot be null");
        }

        User user = userRepository.getUserById(userId);

        if (user == null) {
            throw new HttpClientErrorException(HttpStatus.NOT_FOUND, "User not found");
        }

        userRepository.deleteUser(userId);

        Cart cart = cartRepository.getCartByUserId(userId);

        if (cart != null) {
            cartRepository.deleteCartById(cart.getId());
        }
    }
}