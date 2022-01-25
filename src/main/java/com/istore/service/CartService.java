package com.istore.service;

import java.util.ArrayList;
import java.util.List;

import com.istore.common.ApiResponse;
import com.istore.common.BadRequestException;
import com.istore.common.Error;
import com.istore.common.UserNotFoundException;
import com.istore.data.CartData;
import com.istore.dto.CartResponseDTO;
import com.istore.dto.ProductDTO;
import com.istore.entity.Cart;
import com.istore.entity.UserProductDetails;
import com.istore.repository.CartRepo;
import com.istore.repository.ProductRepo;
import com.istore.repository.UserProductDetailsRepo;
import com.istore.repository.UserRepo;
import com.istore.validator.CartValidator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class CartService {

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private ProductRepo productRepo;

    @Autowired
    private CartRepo cartRepo;

    @Autowired
    private CartValidator cartValidator;

    @Autowired
    private UserProductDetailsRepo userProductDetailsRepo;

    public ApiResponse getAllFromCart(Long id) {
        ApiResponse apiResponse = new ApiResponse();

        List<Error> errors = cartValidator.isUserValid(id);

        if (errors.size() == 0) {
            CartData cartData = new CartData();
            List<ProductDTO> productDTOs = new ArrayList<>();

            List<Cart> carts = cartRepo.findAllByUserId(id);

            for (Cart cart : carts) {
                ProductDTO productDTO = new ProductDTO();
                productDTO.setId(cart.getUserProductDetails().getProduct().getId());
                productDTO.setImg(cart.getUserProductDetails().getProduct().getImgSrc());
                productDTO.setNos(cart.getUserProductDetails().getProductNos());
                productDTO.setPrice(cart.getUserProductDetails().getProduct().getPrice());
                productDTO.setName(cart.getUserProductDetails().getProduct().getName());
                productDTOs.add(productDTO);
            }

            CartResponseDTO cDto = new CartResponseDTO();
            cDto.setProducts(productDTOs);

            cartData.setCart(cDto);

            apiResponse.setStatus(HttpStatus.OK.value());
            apiResponse.setData(cartData);
        } else {
            throw new UserNotFoundException("User Not Found", errors);
        }
        return apiResponse;
    }

    public ApiResponse isInCart(Long id, Long pid) {
        ApiResponse apiResponse = new ApiResponse();
        apiResponse.setStatus(HttpStatus.OK.value());

        CartData cartData = new CartData();

        List<Cart> carts = cartRepo.findAllByUserId(id);

        for (Cart cart : carts) {
            if (cart.getUserProductDetails().getProduct().getId() == pid) {
                cartData.setMessage("Product exists");
                apiResponse.setData(cartData);
                return apiResponse;
            }
        }
        cartData.setMessage("Not Found");
        apiResponse.setData(cartData);

        return apiResponse;
    }

    public ApiResponse addToCart(Long id, Long pid) {
        ApiResponse apiResponse = new ApiResponse();
        List<Error> errors = cartValidator.validateAddToCart(id, pid);

        if (errors.size() == 0) {
            UserProductDetails userProductDetails = new UserProductDetails();
            userProductDetails.setProduct(productRepo.findById(pid).orElse(null));
            userProductDetails.setProductNos(1);
            userProductDetailsRepo.save(userProductDetails);

            Cart cart = new Cart();
            cart.setUserProductDetails(userProductDetails);
            cart.setUser(userRepo.findById(id).orElse(null));
            cartRepo.save(cart);

            CartData cartData = new CartData();
            cartData.setMessage("Product added to Cart...!!!");

            apiResponse.setStatus(HttpStatus.OK.value());
            apiResponse.setData(cartData);
        } else {
            throw new BadRequestException("Bad Request", errors);
        }
        return apiResponse;
    }

    public ApiResponse updateProductInCart(Long id, Long pid, int nos) {
        ApiResponse apiResponse = new ApiResponse();

        List<Cart> carts = cartRepo.findAllByUserId(id);
        for (Cart cart : carts) {
            if (cart.getUserProductDetails().getProduct().getId() == pid) {
                cart.getUserProductDetails().setProductNos(nos);
                userProductDetailsRepo.save(cart.getUserProductDetails());
                break;
            }
        }

        CartData cartData = new CartData();
        cartData.setMessage("Product updated in Cart...!!!");

        apiResponse.setStatus(HttpStatus.OK.value());
        apiResponse.setData(cartData);
        return apiResponse;
    }

    public ApiResponse deleteProductFromCart(Long id, Long pid) {

        ApiResponse apiResponse = new ApiResponse();

        List<Long> updList = userProductDetailsRepo.findAllByProductId(pid);

        List<Cart> carts = cartRepo.findAllByUserId(id);

        for (Cart cart : carts) {
            if (updList.contains(cart.getUserProductDetails().getId())) {
                userProductDetailsRepo.delete(cart.getUserProductDetails());
                cartRepo.delete(cart);
                break;
            }
        }

        CartData cartData = new CartData();
        cartData.setMessage("Product deleted from Cart...!!!");

        apiResponse.setStatus(HttpStatus.OK.value());
        apiResponse.setData(cartData);
        return apiResponse;
    }

    public ApiResponse getCount(Long id) {
        ApiResponse apiResponse = new ApiResponse();

        List<Cart> carts = cartRepo.findAllByUserId(id);

        CartResponseDTO cartResponseDTO =new CartResponseDTO();
        cartResponseDTO.setCount(carts.size());

        CartData cartData = new CartData();
        cartData.setCart(cartResponseDTO);

        apiResponse.setStatus(HttpStatus.OK.value());
        apiResponse.setData(cartData);

        return apiResponse;
    }

}
