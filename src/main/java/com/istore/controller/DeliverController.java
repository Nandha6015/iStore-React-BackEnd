package com.istore.controller;

import com.istore.common.ApiResponse;
import com.istore.service.OrderService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin
@RestController
@RequestMapping("/deliver/{id}")
public class DeliverController {

    @Autowired
    private OrderService orderService;

    // ---------------Order Controller Starts---------------

    @GetMapping("/orders")
    public ApiResponse getAllMyOrdersToDeliver(@PathVariable Long id){
        return orderService.getAllMyOrdersToDeliver(id);
    }

    @PutMapping("/orders/{oid}")
    public ApiResponse updateTrack(@PathVariable Long id,@PathVariable Long oid,@RequestParam(name = "track",required = true) String track){
        return orderService.updateTrack(oid,track);
    }

    // ---------------Order Controller Ends---------------
    
}
