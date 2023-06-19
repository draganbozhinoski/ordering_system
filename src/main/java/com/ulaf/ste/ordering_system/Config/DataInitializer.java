package com.ulaf.ste.ordering_system.Config;

import com.ulaf.ste.ordering_system.Exceptions.NotFoundByIdException;
import com.ulaf.ste.ordering_system.Model.*;
import com.ulaf.ste.ordering_system.Service.CategoryService;
import com.ulaf.ste.ordering_system.Service.IngredientService;
import com.ulaf.ste.ordering_system.Service.OrderService;
import com.ulaf.ste.ordering_system.Service.ProductService;
import jakarta.annotation.PostConstruct;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;
import org.springframework.util.StreamUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Component
public class DataInitializer {

    private final CategoryService categoryService;
    private final IngredientService ingredientService;
    private final ProductService productService;
    private final OrderService orderService;
    private final Product_QtyRepository product_qtyRepository;

    public DataInitializer(CategoryService categoryService, IngredientService ingredientService, ProductService productService, OrderService orderService) {
        this.categoryService = categoryService;
        this.ingredientService = ingredientService;
        this.productService = productService;
        this.orderService = orderService;
    }

    private byte[] getPizzaImageBytes(String imageName) {
        try {
            ClassPathResource resource = new ClassPathResource("images/" + imageName);
            return StreamUtils.copyToByteArray(resource.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public DataInitializer(CategoryService categoryService, IngredientService ingredientService, ProductService productService, OrderService orderService, Product_QtyRepository productQtyRepository) {
        this.categoryService = categoryService;
        this.ingredientService = ingredientService;
        this.productService = productService;
        this.orderService = orderService;
        product_qtyRepository = productQtyRepository;
    }

    @PostConstruct
    public void initializeData() throws NotFoundByIdException {
        Category category = categoryService.createCategory(new Category(1L, "Pizza", "Kategorija za pizza"));
        Ingredient ingredient1 = ingredientService.createIngredient(new Ingredient("Pechurke"));
        Ingredient ingredient2 = ingredientService.createIngredient(new Ingredient("Sirenje"));
        List<Category> productCategories = new ArrayList<>();
        productCategories.add(category);
        List<Ingredient> productIngredients = new ArrayList<>();
        productIngredients.add(ingredient1);
        List<Ingredient> productIngredients2 = new ArrayList<>();
        productIngredients2.add(ingredient2);

        byte [] bytesMargarita = getPizzaImageBytes("margarita.png");
        byte [] bytesKaprichioza = getPizzaImageBytes("kaprichioza.png");

        productService.createProduct(new Product(1L, "Margarita Pizza", 280.0, productCategories, productIngredients,bytesMargarita));
        productService.createProduct(new Product(2L, "Kaprichioza Pizza", 320.0, productCategories, productIngredients2,bytesKaprichioza));

        List<Product_Qty> listItems = new ArrayList<>();
        product_qtyRepository.save(new Product_Qty(1L, productService.getProductById(1L), 2));
        product_qtyRepository.save(new Product_Qty(2L, productService.getProductById(2L), 3));
        listItems.add(product_qtyRepository.findById(1L).orElseThrow());
        listItems.add(product_qtyRepository.findById(2L).orElseThrow());
        Order order1 = new Order(listItems, "Gorjan", "Tetovo", "070344899");
        orderService.createOrder(order1);
        categoryService.getCategoryById(1L).setProducts(productService.getAllProducts());
    }
//    @PostConstruct
//    public void initializeData() {
//        Category category = new Category("Pizza","Kategorija za pizza");
//        categoryService.createCategory(category);
//
//        Ingredient ingredient1 = new Ingredient("Pechurke");
//        ingredientService.createIngredient(ingredient1);
//        Ingredient ingredient2 = new Ingredient("Sirenje");
//        ingredientService.createIngredient(ingredient2);
//
//        List<Category> productCategories = new ArrayList<>();
//        productCategories.add(category);
//        List<Category> productCategories2 = new ArrayList<>();
//        productCategories2.add(category);
//
//        List<Ingredient> productIngredients = new ArrayList<>();
//        productIngredients.add(ingredient1);
//        List<Ingredient> productIngredients2 = new ArrayList<>();
//        productIngredients2.add(ingredient2);
//
//        byte [] bytesMargarita = getPizzaImageBytes("margarita.png");
//        byte [] bytesKaprichioza = getPizzaImageBytes("kaprichioza.png");
//        Product product1 = new Product("Margarita", 280, productCategories, productIngredients,bytesMargarita);
//        productService.createProduct(product1);
//        Product product2 = new Product("Kaprichioza", 320, productCategories2, productIngredients2, bytesKaprichioza);
//        productService.createProduct(product2);
//
//        List<Product_Qty> listItems = new ArrayList<>();
//        Product_Qty productQty = new Product_Qty(product1,2);
//        listItems.add(productQty);
//        Product_Qty productQty2 = new Product_Qty(product2,1);
//        listItems.add(productQty2);
//
//        List<Product_Qty> listItems2 = new ArrayList<>();
//        Product_Qty productQt3 = new Product_Qty(product1,5);
//        listItems2.add(productQt3);
//        Product_Qty productQty4 = new Product_Qty(product2,3);
//        listItems2.add(productQty4);
//
//        Order order1 = new Order(listItems,"Gorjan","Tetovo","070344899");
//        orderService.createOrder(order1);
//        Order order2 = new Order(listItems2,"Dragan","Tetovo","071519218");
//        orderService.createOrder(order2);
//}

}
