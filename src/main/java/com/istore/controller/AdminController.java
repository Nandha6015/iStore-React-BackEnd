package com.istore.controller;

import com.istore.common.ApiResponse;
import com.istore.dto.ProductsRequestDTO;
import com.istore.service.OrderService;
import com.istore.service.ProductService;
import com.istore.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin
@RestController
@RequestMapping("/admin")
public class AdminController {

    // ---------------User Controller Starts---------------

    @Autowired
    private UserService userService;

    @GetMapping("/users")
    public ApiResponse getAllUsers() {
        return userService.getAllUsers();
    }

    @PutMapping("/users/{id}")
    public ApiResponse makeUserDisableAndEnable(@PathVariable Long id,@RequestParam(name = "enable") boolean enable) {
        return userService.makeUserDisableAndEnable(id,enable);
    }

    // ---------------User Controller Ends---------------

    // ---------------Product Controller Starts---------------

    @Autowired
    private ProductService productService;

    @PostMapping("/products")
    public ApiResponse addProduct(@RequestBody ProductsRequestDTO product) {
        return productService.addProduct(product);
    }

    @PutMapping("/products/{id}")
    public ApiResponse updateProduct(@PathVariable Long id, @RequestBody ProductsRequestDTO product) {
        return productService.updateProduct(id, product);
    }

    @DeleteMapping("/products/{id}")
    public ApiResponse deleteProduct(@PathVariable Long id) {
        return productService.deleteProduct(id);
    }

    // ---------------Product Controller Ends---------------

    // ---------------Order Controller Starts---------------

    @Autowired
    private OrderService orderService;

    @GetMapping("/orders")
    public ApiResponse getAllOrders(){
        return orderService.getAllOrders();
    }

    @PutMapping("/orders/{id}")
    public ApiResponse updateTrack(@PathVariable Long id,@RequestParam(name = "track",required = true) String track){
        return orderService.updateTrack(id,track);
    }

    // ---------------Order Controller Ends---------------
    

}
