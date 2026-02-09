package org.example.ecomproj.controller;

import org.example.ecomproj.model.Product;
import org.example.ecomproj.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/api")
public class ProductController {
    @Autowired
    private ProductService service;
    @RequestMapping("/")
    public String greet(){
        return "Hello World";
    }
    @GetMapping("/products")
    public ResponseEntity<List<Product>> getAllProducts(){
        return new ResponseEntity<>(service.getAllProducts(), HttpStatus.OK);
    }
    @GetMapping("/product/{id}")
    public ResponseEntity<Product> getProduct(@PathVariable int id){
        Product product = service.getProductbyId(id);
        if(product != null)
            return new ResponseEntity<>(product,HttpStatus.OK);
        else
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
    @PostMapping("/product")
    public ResponseEntity<?> addProduct(@RequestPart Product product,
                                        @RequestPart MultipartFile imageFile){
        try{
            Product temp =  service.addProduct(product,imageFile);
            return new ResponseEntity<>(temp,HttpStatus.CREATED);
        }
        catch (Exception e){
            return new ResponseEntity<>(e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @GetMapping("/product/{id}/image")
    public ResponseEntity<byte[]> getproductbyId(@PathVariable int id){
        Product product = service.getProductbyId(id);
        byte[] imageData = product.getImageDate();
        return ResponseEntity.ok().contentType(MediaType.valueOf(product.getImageType())).body(imageData);
    }
    @PutMapping("/product/{id}")
    public ResponseEntity<String> updateProduct(
            @PathVariable int id,
            @RequestPart Product product,
            @RequestPart MultipartFile imageFile) {
        Product temp = null;
        try {
             temp = service.updateProduct(product, imageFile);
        } catch (IOException e) {
            return new ResponseEntity<>("Didn't Updated ", HttpStatus.BAD_REQUEST);
        }
        if(temp != null){
            return new ResponseEntity<>("Updated Successfully", HttpStatus.OK);
        }
        else{
            return new ResponseEntity<>("Failed to Update ", HttpStatus.BAD_REQUEST);
        }
    }
    @DeleteMapping("/product/{id}")
    public ResponseEntity<String> deleteProduct(@PathVariable int id){
        Product temp = service.getProductbyId(id);
        if(temp != null){
            service.deleteProduct(id);
            return new  ResponseEntity<>("Deleted Successfully",HttpStatus.OK);
        }
        else{
            return new ResponseEntity<>("Product Not found",HttpStatus.NOT_FOUND);
        }
    }
    @GetMapping("/products/search")
    public ResponseEntity<List<Product>> searchProduct(String keyword){
        System.out.println("Searching " + keyword);
        return new ResponseEntity<>(service.searchProduct(keyword),HttpStatus.OK);
    }
}
