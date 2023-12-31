package com.ulaf.ste.ordering_system.Web.REST;

import com.ulaf.ste.ordering_system.Exceptions.NotFoundByIdException;
import com.ulaf.ste.ordering_system.Model.Product;
import com.ulaf.ste.ordering_system.Service.ProductService;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping
    public ResponseEntity<List<Product>> getAllProducts(@RequestParam(required = false) Boolean thumbnail) {
        List<Product> products;
        if(!thumbnail) {
            products = productService.getAllProducts();
        }
        else {
            products = productService.getAllProducts().stream().map(prod -> {
                try {
                    return productService.createProductWithThumbnail(prod);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }).collect(Collectors.toList());
        }
        return ResponseEntity.ok(products);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Product> getProductById(@PathVariable Long id) throws NotFoundByIdException {
        Product product = productService.getProductById(id);
        if (product != null) {
            return ResponseEntity.ok(product);
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/category/{id}")
    public ResponseEntity<List<Product>> findProductsByCategoryId(@PathVariable Long id) {
        List<Product> products = productService.getAllProductsByCategoryId(id);
        return ResponseEntity.ok(products);
    }
    @PostMapping("/{category_name}")
    public ResponseEntity<List<Product>> findAllProductsWithCategory(@PathVariable String category_name) throws NotFoundByIdException {
        List<Product> products = productService.findAllProductsWithCategory(category_name);
        return ResponseEntity.ok(products);
    }
    @GetMapping("/name/{name}")
    public ResponseEntity<Product> getProductByName(@PathVariable String name) {
        Product product = productService.getProductByName(name);
        if (product != null) {
            return ResponseEntity.ok(product);
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping
    @PreAuthorize("hasAuthority('SCOPE_ADMIN')")

    public ResponseEntity<Product> createProduct(@RequestBody Product product) {
        Product createdProduct = productService.createProduct(product);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdProduct);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('SCOPE_ADMIN')")

    public ResponseEntity<Product> updateProduct(@PathVariable Long id, @RequestBody Product product) throws NotFoundByIdException {
        Product updatedProduct = productService.updateProduct(id, product);
        if (updatedProduct != null) {
            return ResponseEntity.ok(updatedProduct);
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('SCOPE_ADMIN')")

    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/banner")
    public ResponseEntity<List<Product>> getImagesForBanner() {
        List<Product> products = productService.getAllProducts().stream().filter(prod -> Objects.equals(prod.getCategory().getName(), "Пица") && prod.getAvailable()).collect(Collectors.toList());
        if (products.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        Collections.shuffle(products);
        List<Product> randomProducts = products.subList(0, Math.min(products.size(), 4));
        return ResponseEntity.ok(randomProducts);
    }
    @PutMapping("/availability")
//    @PreAuthorize("hasAuthority('SCOPE_ADMIN')") TODO:SHOP OWNER AND ADMIN
    public ResponseEntity<List<Product>> changeProductAvailability(@RequestParam("id") String id) {
        List<Product> products = productService.changeProductAvailability(Long.parseLong(id));
        return ResponseEntity.ok(products);
    }

}
