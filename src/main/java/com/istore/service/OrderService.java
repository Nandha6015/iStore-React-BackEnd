package com.istore.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.istore.common.ApiResponse;
import com.istore.data.OrdersData;
import com.istore.dto.OrderResponseDTO;
import com.istore.dto.ProductDTO;
import com.istore.entity.Cart;
import com.istore.entity.Orders;
import com.istore.entity.Product;
import com.istore.repository.CartRepo;
import com.istore.repository.OrderRepo;
import com.istore.repository.ProductRepo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
public class OrderService {

    @Autowired
    private ProductRepo productRepo;

    @Autowired
    private CartRepo cartRepo;

    @Autowired
    private OrderRepo orderRepo;

    public ApiResponse getAllFromOrder(Long id) {
        ApiResponse apiResponse = new ApiResponse();
        OrdersData ordersData = new OrdersData();

        List<ProductDTO> productDTOs = new ArrayList<>();
        List<Orders> orders = orderRepo.findAllByUserId(id);

        for (Orders order : orders) {
            ProductDTO productDTO = new ProductDTO();
            productDTO.setId(order.getId());
            productDTO.setImg(order.getUserProductDetails().getProduct().getImgSrc());
            productDTO.setName(order.getUserProductDetails().getProduct().getName());
            productDTO.setNos(order.getUserProductDetails().getProductNos());
            productDTO.setPrice(order.getUserProductDetails().getProduct().getPrice());
            productDTOs.add(productDTO);
        }
        OrderResponseDTO oDto = new OrderResponseDTO();
        oDto.setProducts(productDTOs);

        ordersData.setOrders(oDto);

        apiResponse.setStatus(HttpStatus.OK.value());
        apiResponse.setData(ordersData);
        return apiResponse;
    }

    public ApiResponse addToOrder(Long id) {
        ApiResponse apiResponse = new ApiResponse();

        List<Cart> carts = cartRepo.findAllByUserId(id);

        for (Cart cart : carts) {
            Orders orders = new Orders();
            orders.setOrderedAt(LocalDateTime.now());
            orders.setUser(cart.getUser());
            Product product = productRepo.findById(cart.getUserProductDetails().getProduct().getId()).orElse(null);
            int count = cart.getUserProductDetails().getProductNos();
            if(product.getQuantityInStock()-count>=0){
            product.setQuantityInStock(product.getQuantityInStock()-count);
            }
            else{
                product.setQuantityInStock(0);
                cart.getUserProductDetails().setProductNos(count-product.getQuantityInStock());
            }
            productRepo.save(product);
            orders.setUserProductDetails(cart.getUserProductDetails());
            orders.setTracker("Stage 1");
            orderRepo.save(orders);
            cartRepo.deleteById(cart.getId());
        }

        OrdersData ordersData = new OrdersData();
        ordersData.setMessage("Products Ordered Successfully...!!!");

        apiResponse.setStatus(HttpStatus.OK.value());
        apiResponse.setData(ordersData);
        return apiResponse;
    }

    public ApiResponse getAllOrders() {
        ApiResponse apiResponse=new ApiResponse();

        List<ProductDTO> productDTOs = new ArrayList<>();
        List<Orders> orders = orderRepo.findAll();

        for (Orders order : orders) {
            ProductDTO productDTO = new ProductDTO();
            productDTO.setId(order.getId());
            productDTO.setImg(order.getUserProductDetails().getProduct().getImgSrc());
            productDTO.setName(order.getUser().getName());
            productDTO.setPname(order.getUserProductDetails().getProduct().getName());
            productDTO.setTracker(order.getTracker());
            productDTOs.add(productDTO);
        }

        OrdersData ordersData = new OrdersData();
        OrderResponseDTO oDto = new OrderResponseDTO();
        oDto.setProducts(productDTOs);
        ordersData.setOrders(oDto);

        apiResponse.setStatus(HttpStatus.OK.value());
        apiResponse.setData(ordersData);

        return apiResponse;
    }

    public ApiResponse updateTrack(Long id, String track) {
        ApiResponse apiResponse = new ApiResponse();

        Orders orders = orderRepo.findById(id).orElse(null);
        orders.setTracker(track);
        orderRepo.save(orders);

        OrdersData ordersData = new OrdersData();
        ordersData.setMessage("Tracker Updated");

        apiResponse.setStatus(HttpStatus.OK.value());
        apiResponse.setData(ordersData);
        return apiResponse;
    }

    public ApiResponse getTrack(Long id, Long oid) {
        ApiResponse apiResponse = new ApiResponse();
        OrdersData ordersData = new OrdersData();
        ordersData.setMessage(orderRepo.findById(oid).orElse(null).getTracker());
        apiResponse.setStatus(HttpStatus.OK.value());
        apiResponse.setData(ordersData);
        return apiResponse;
    }

}
