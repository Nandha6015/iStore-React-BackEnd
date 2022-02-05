package com.istore.controller;

import com.istore.common.ApiResponse;
import com.istore.dto.AddressDTO;
import com.istore.dto.ProfileDTO;
import com.istore.service.CartService;
import com.istore.service.OrderService;
import com.istore.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin
@RestController
@RequestMapping("/user/{id}")
public class UserController {

    // ---------------Profile Controller Starts---------------

    @Autowired
    private UserService userService;

    @GetMapping("")
    public ApiResponse getProfile(@PathVariable Long id) {
        return userService.getProfile(id);
    }

    @PutMapping("")
    public ApiResponse updateProfile(@PathVariable Long id, @RequestBody ProfileDTO profileDTO, @RequestParam(name = "img",required = false) String img ) {
        return userService.updateProfile(id, profileDTO,img);
    }

    @GetMapping("/address")
    public ApiResponse getAddress(@PathVariable Long id){
        return userService.getAddress(id);
    }

    @PutMapping("/address")
    public ApiResponse updateAddress(@PathVariable Long id, @RequestBody AddressDTO addressDto){
        return userService.updateAddress(id, addressDto);
    }

    // ---------------Profile Controller Ends---------------

    // ---------------Cart Controller Starts---------------

    @Autowired
    private CartService cartService;

    @GetMapping("/carts")
    public ApiResponse getAllFromCart(@PathVariable Long id) {
        return cartService.getAllFromCart(id);
    }

    @GetMapping("/carts/count")
    public ApiResponse getCount(@PathVariable Long id){
        return cartService.getCount(id);
    }

    @GetMapping("/carts/{pid}")
    public ApiResponse isInCart(@PathVariable Long id, @PathVariable Long pid) {
        return cartService.isInCart(id, pid);
    }

    @PostMapping("/carts/{pid}")
    public ApiResponse addToCart(@PathVariable Long id, @PathVariable Long pid) {
        return cartService.addToCart(id, pid);
    }

    @PutMapping("/carts/{pid}")
    public ApiResponse updateProductInCart(@PathVariable Long id, @PathVariable Long pid,
            @RequestParam(name = "nos", required = true) int nos) {
        return cartService.updateProductInCart(id, pid, nos);
    }

    @DeleteMapping("/carts/{pid}")
    public ApiResponse deleteProductFromCart(@PathVariable Long id, @PathVariable Long pid) {
        return cartService.deleteProductFromCart(id, pid);
    }

    // ---------------Cart Controller Ends---------------

    // ---------------Order Controller Starts---------------

    @Autowired
    private OrderService orderService;

    @GetMapping("/orders")
    public ApiResponse getAllFromOrder(@PathVariable Long id) {
        return orderService.getAllFromOrder(id);
    }

    @GetMapping("/orders/{oid}")
    public ApiResponse getTrack(@PathVariable Long id,@PathVariable Long oid){
        return orderService.getTrack(id,oid);
    }

    @PostMapping("/orders")
    public ApiResponse addToOrder(@PathVariable Long id) {
        return orderService.addToOrder(id);
    }

    // ---------------Order Controller Ends---------------

}
