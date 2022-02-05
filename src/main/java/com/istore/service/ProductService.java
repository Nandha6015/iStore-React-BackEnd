package com.istore.service;

import java.util.ArrayList;
import java.util.List;

import com.istore.common.ApiResponse;
import com.istore.common.BadRequestException;
import com.istore.common.Error;
import com.istore.data.ProductData;
import com.istore.dto.ProductsRequestDTO;
import com.istore.dto.ProductsResponseDTO;
import com.istore.entity.Images;
import com.istore.entity.Product;
import com.istore.repository.ImagesRepo;
import com.istore.repository.ProductRepo;
import com.istore.validator.ProductValidator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
public class ProductService {

    @Autowired
    private ProductRepo productRepo;

    @Autowired
    private ProductValidator productValidator;

    @Autowired
    private ImagesRepo imagesRepo;

    public ApiResponse getAllProducts(boolean isAdmin) {
        ApiResponse apiResponse = new ApiResponse();
        ProductData productData = new ProductData();

        if (isAdmin) {
            productData.setProductsList(productRepo.findAll());
            apiResponse.setData(productData);
        } else {

            List<ProductsResponseDTO> pList = new ArrayList<>();
            List<Product> products = productRepo.findAll();
            for (Product product : products) {
                ProductsResponseDTO pDto = new ProductsResponseDTO();
                pDto.setId(product.getId());
                pDto.setName(product.getName());
                pDto.setImgSrc(product.getImgSrc());
                pDto.setKeyFeature1(product.getKeyFeature1());
                pDto.setKeyFeature2(product.getKeyFeature2());
                pDto.setKeyFeature3(product.getKeyFeature3());
                pDto.setPrice(product.getPrice());
                pDto.setCategory(product.getCategory());
                pDto.setStockStatus(product.getQuantityInStock() > 20 ? "InStock"
                        : product.getQuantityInStock() > 10 ? "few lefts"
                                : product.getQuantityInStock() == 0 ? "Out of Stock" : "Hurry, few lefts");
                pList.add(pDto);
            }
            productData.setProducts(pList);

            apiResponse.setStatus(HttpStatus.OK.value());
            apiResponse.setData(productData);
        }

        return apiResponse;
    }

    public ApiResponse getProduct(Long id) {
        ApiResponse apiResponse = new ApiResponse();

        ProductData productData = new ProductData();
        Product product = productRepo.findById(id).orElse(null);

        List<Error> errors = productValidator.isProductValid(id);
        if (errors.size() == 0) {
            ProductsResponseDTO pDto = new ProductsResponseDTO();
            pDto.setId(id);
            pDto.setName(product.getName());
            pDto.setImgSrc(product.getImgSrc());
            pDto.setKeyFeature1(product.getKeyFeature1());
            pDto.setKeyFeature2(product.getKeyFeature2());
            pDto.setKeyFeature3(product.getKeyFeature3());
            pDto.setDescription(product.getDescription());
            pDto.setPrice(product.getPrice());
            pDto.setQuantityInStock(product.getQuantityInStock());
            pDto.setImages(product.getImages());
            productData.setProduct(pDto);

            apiResponse.setStatus(HttpStatus.OK.value());
            apiResponse.setData(productData);
        } else {
            throw new BadRequestException("Bad Request", errors);
        }

        return apiResponse;
    }

    public ApiResponse addProduct(ProductsRequestDTO newProduct) {
        ApiResponse apiResponse = new ApiResponse();

        Product product = new Product();
        product.setName(newProduct.getName());
        product.setPrice(newProduct.getPrice());
        product.setDescription(newProduct.getDescription());
        product.setKeyFeature1(newProduct.getKeyFeature1());
        product.setKeyFeature2(newProduct.getKeyFeature2());
        product.setKeyFeature3(newProduct.getKeyFeature3());
        product.setImgSrc("img/3.png");
        product.setQuantityInStock(newProduct.getStock());
        product.setCategory(newProduct.getCategory());

        Images images = new Images();
        images.setImgSrc1("img/3.png");
        images.setImgSrc2("img/3.png");
        images.setImgSrc3("img/3.png");
        images.setImgSrc4("img/3.png");
        images.setImgSrc5("img/3.png");

        imagesRepo.save(images);

        product.setImages(images);
        productRepo.save(product);

        ProductData productData = new ProductData();
        productData.setMessage("Product Added");

        apiResponse.setStatus(HttpStatus.OK.value());
        apiResponse.setData(productData);

        return apiResponse;
    }

    public ApiResponse updateProduct(Long id, ProductsRequestDTO oldProduct) {
        ApiResponse apiResponse = new ApiResponse();

        Product product = productRepo.findById(id).orElse(null);
        product.setName(oldProduct.getName());
        product.setPrice(oldProduct.getPrice());
        product.setDescription(oldProduct.getDescription());
        product.setKeyFeature1(oldProduct.getKeyFeature1());
        product.setKeyFeature2(oldProduct.getKeyFeature2());
        product.setKeyFeature3(oldProduct.getKeyFeature3());
        product.setQuantityInStock(oldProduct.getStock());
        product.setCategory(oldProduct.getCategory());
        productRepo.save(product);

        ProductData productData = new ProductData();
        productData.setMessage("Product Updated");

        apiResponse.setStatus(HttpStatus.OK.value());
        apiResponse.setData(productData);

        return apiResponse;
    }

    public ApiResponse deleteProduct(Long id) {
        ApiResponse apiResponse = new ApiResponse();

        productRepo.deleteById(id);

        ProductData productData = new ProductData();
        productData.setMessage("Product Deleted");

        apiResponse.setStatus(HttpStatus.OK.value());
        apiResponse.setData(productData);

        return apiResponse;
    }

}
