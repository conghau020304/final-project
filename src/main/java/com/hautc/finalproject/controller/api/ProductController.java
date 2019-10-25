package com.hautc.finalproject.controller.api;

import com.hautc.finalproject.exception.ResourceNotFoundException;
import com.hautc.finalproject.model.Product;
import com.hautc.finalproject.service.IProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping(value = "/user/api/products")
public class ProductController {

    @Autowired
    private IProductService productService;

    @GetMapping()
    public List<Product> getAllProduct(@RequestParam(value = "tk", name = "tk", required = false) String tk) {
        if (tk != null) {
            return productService.findByIdOrName(tk, tk.toUpperCase());
        }
        return productService.getAllProduct();
    }

    @GetMapping(value = "/{productId}")
    public ResponseEntity<Product> getProductById(@PathVariable(value = "productId") String productId) throws ResourceNotFoundException {
        Product product = productService.findProductById(productId).orElseThrow(() -> new ResourceNotFoundException("Invalid user Id:" + productId + " !"));
        return ResponseEntity.ok().body(product);
    }

    @PostMapping
    public ResponseEntity<Object> insertProduct(@Valid @RequestBody Product product) {
        Optional<Product> p = productService.findProductById(product.getProductId());
        if(p.isPresent()){
            Map<String, String> error = new HashMap<>();
            error.put("productId", product.getProductId() + " duplicate in database!");
            return new ResponseEntity(error, HttpStatus.CONFLICT);
        }
        Product productInsert = productService.insertAndUpdateProduct(product);
        return ResponseEntity.ok().body(productInsert);
    }

    @PutMapping(value = "/{productId}")
    ResponseEntity<Product> updateProduct(@PathVariable(value = "productId") String productId, @Valid @RequestBody Product productDetails) throws ResourceNotFoundException {
        Product product = productService.findProductById(productId).orElseThrow(() -> new ResourceNotFoundException("Invalid product Id: " + productId + " !"));
        product.setProductName(productDetails.getProductName());
        product.setQuantity(productDetails.getQuantity());
        product.setPrice(productDetails.getPrice());
        product.setDateAdded(productDetails.getDateAdded());
        product.setMeasureWord(productDetails.getMeasureWord());
        Product updateProduct = productService.insertAndUpdateProduct(product);
        return ResponseEntity.ok().body(updateProduct);
    }

    @DeleteMapping(value = "/{productId}")
    public Map<String, Boolean> deleteProduct(@PathVariable(value = "productId") String id) throws ResourceNotFoundException {
        Product product = productService.findProductById(id).orElseThrow(() -> new ResourceNotFoundException("Invalid product Id: " + id + " !"));
        productService.deleteProduct(id);
        Map<String, Boolean> response = new HashMap<>();
        response.put("deleted", Boolean.TRUE);
        return response;
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Map<String, String> handleValidationExceptions(
            MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return errors;
    }
}
