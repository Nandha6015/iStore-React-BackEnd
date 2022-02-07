package com.istore.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.istore.common.ApiResponse;
import com.istore.data.DeliveryData;
import com.istore.data.OrdersData;
import com.istore.dto.DeliveryDTO;
import com.istore.dto.OrderResponseDTO;
import com.istore.dto.ProductDTO;
import com.istore.dto.TrackerDTO;
import com.istore.entity.Address;
import com.istore.entity.Cart;
import com.istore.entity.Orders;
import com.istore.entity.Product;
import com.istore.entity.User;
import com.istore.repository.AddressRepo;
import com.istore.repository.CartRepo;
import com.istore.repository.OrderRepo;
import com.istore.repository.ProductRepo;
import com.istore.repository.UserRepo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
public class OrderService {

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private ProductRepo productRepo;

    @Autowired
    private CartRepo cartRepo;

    @Autowired
    private OrderRepo orderRepo;

    @Autowired
    private AddressRepo addressRepo;

    public ApiResponse getAllFromOrder(Long id) {
        ApiResponse apiResponse = new ApiResponse();
        OrdersData ordersData = new OrdersData();

        List<ProductDTO> productDTOs = new ArrayList<>();
        List<Orders> orders = orderRepo.findAllByUserIdOrderByIdDesc(id);

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
        User user = userRepo.findById(id).orElse(null);

        for (Cart cart : carts) {
            Orders orders = new Orders();
            orders.setOrderedAt(LocalDateTime.now());
            orders.setUser(cart.getUser());
            Address address = new Address();
            address.setName(user.getAddress().getName());
            address.setEmail(user.getAddress().getEmail());
            address.setAddress(user.getAddress().getAddress());
            address.setCity(user.getAddress().getCity());
            address.setPhoneNumber(user.getAddress().getPhoneNumber());
            address.setPincode(user.getAddress().getPincode());
            address.setState(user.getAddress().getState());
            addressRepo.save(address);
            orders.setAddress(address);
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
            List<User> users = userRepo.findAllUserByRoleAndByCity("DELIVER",address.getCity());
            orders.setDelivery(users.get(0));
            orderRepo.save(orders);
            cartRepo.deleteById(cart.getId());
        }

        OrdersData ordersData = new OrdersData();
        ordersData.setMessage("Products Ordered Successfully...!!!");

        apiResponse.setStatus(HttpStatus.OK.value());
        apiResponse.setData(ordersData);
        return apiResponse;
    }

    public ApiResponse cancelOrder(Long id,Long oid){
        ApiResponse apiResponse = new ApiResponse();
        Orders order = orderRepo.findById(oid).orElse(null);
        order.setIsCancel(true);
        orderRepo.save(order);

        OrdersData ordersData = new OrdersData();
        ordersData.setMessage("Order Cancelled");

        apiResponse.setStatus(HttpStatus.OK.value());
        apiResponse.setData(ordersData);
        return apiResponse;
    }

    public ApiResponse getAllOrders() {
        ApiResponse apiResponse=new ApiResponse();

        List<ProductDTO> productDTOs = new ArrayList<>();
        List<Orders> orders = orderRepo.findAllOrderByIdDesc();

        for (Orders order : orders) {
            ProductDTO productDTO = new ProductDTO();
            productDTO.setId(order.getId());
            productDTO.setImg(order.getUserProductDetails().getProduct().getImgSrc());
            productDTO.setName(order.getUser().getName());
            productDTO.setPname(order.getUserProductDetails().getProduct().getName());
            productDTO.setTracker(order.getTracker());
            if(order.getIsCancel()){
                productDTO.setTracker("cancelled");
            }
            if(order.getTracker().equals("Stage 4")){
                productDTO.setDname(order.getDelivery().getName());
            }
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
        Orders order = orderRepo.findById(oid).orElse(null);
        TrackerDTO tracker = new TrackerDTO();
        tracker.setTracker(order.getTracker());
        tracker.setIsCancel(order.getIsCancel());
        ordersData.setTracker(tracker);
        apiResponse.setStatus(HttpStatus.OK.value());
        apiResponse.setData(ordersData);
        return apiResponse;
    }
    
    public ApiResponse getAllMyOrdersToDeliver(Long id) {
        ApiResponse apiResponse = new ApiResponse();
        DeliveryData data = new DeliveryData();
        List<Orders> orders = orderRepo.findAllByDeliveryId(id);
        List<DeliveryDTO> deliveryDTOs = new ArrayList<>();

        for (Orders order : orders) {
            DeliveryDTO deliveryDTO = new DeliveryDTO();
            deliveryDTO.setPname(order.getUserProductDetails().getProduct().getName());
            deliveryDTO.setUname(order.getUser().getName());
            deliveryDTO.setAddress(order.getAddress());
            deliveryDTO.setAmount(order.getUserProductDetails().getProduct().getPrice()*order.getUserProductDetails().getProductNos());
            deliveryDTO.setPayment(order.getPaymentType());
            deliveryDTO.setTracker(order.getTracker());
            if(order.getIsCancel()){
                deliveryDTO.setTracker("cancelled");
            }
            deliveryDTOs.add(deliveryDTO);
        }
        data.setDeliveries(deliveryDTOs);
        apiResponse.setStatus(HttpStatus.OK.value());
        apiResponse.setData(data);
        return apiResponse;
    }
}
