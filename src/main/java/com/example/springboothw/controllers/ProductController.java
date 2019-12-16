package com.example.springboothw.controllers;

import com.example.springboothw.entities.Cart;
import com.example.springboothw.entities.Category;
import com.example.springboothw.entities.Product;
import com.example.springboothw.services.CategoryService;
import com.example.springboothw.services.ProductService;
import com.example.springboothw.utils.ProductFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/products")
public class ProductController {
    private CategoryService categoryService;
    private ProductService productService;
    private Cart cart;

    @Autowired
    public ProductController(CategoryService categoryService, ProductService productService, Cart cart){
        this.productService=productService;
        this.categoryService=categoryService;
        this.cart=cart;
    }


    @GetMapping("/")
    public String index() {
        return "";
    }

    // http://localhost:8189/app/products/show
    @GetMapping(path = "/show")
    public String showAllProducts(Model model,@RequestParam Map<String, String> params) {
        int pageIndex = 0;
        if (params.containsKey("p")) {
            pageIndex = Integer.parseInt(params.get("p")) - 1;
        }
        Pageable pageRequest = PageRequest.of(pageIndex, 3);
        ProductFilter productFilter = new ProductFilter(params);
        Page<Product> page = productService.findAll(productFilter.getSpec(), pageRequest);
        List<Category> categories=categoryService.getAll();

        model.addAttribute("filtersDef", productFilter.getFilterDefinition());
        model.addAttribute("page",page);
        model.addAttribute("categories",categories);
        return "catalog_products";
    }

    // http://localhost:8189/app/products/edit/2
    @GetMapping(path = "/edit/{id}")
    public String editProduct(Model model,@PathVariable Long id) {
        Product product=productService.findById(id);
        List<Category> categories=categoryService.getAll();

        model.addAttribute("product", product);
        model.addAttribute("categories",categories);
        return "product_form";
    }

    // http://localhost:8189/app/products/edit
    @PostMapping("/edit")
    public String saveProduct(@ModelAttribute(name = "product") Product product) {
        productService.save(product);
        return "redirect:/products/show";
    }

    @GetMapping(path = "/cart/show")
    public String showCart(Model model,@RequestParam Map<String, String> params) {
        List<Product> products = cart.getAll();
        model.addAttribute("products",products);
        return "cart";
    }

    @PostMapping("/cart/add")
    @ResponseBody
    @ResponseStatus(value = HttpStatus.OK)
    public String cartAdd(@RequestParam Long index) {
        cart.addProductToCart(index);
        System.out.println("товар добавлен id="+cart.countProdCart());
        return "success";
    }

    @PostMapping("/cart/countRequest")
    @ResponseBody
    @ResponseStatus(value = HttpStatus.OK)
    public Integer getCartCount() {
        System.out.println("Запрашиваю кол-во товаров в корзине:"+cart.countProdCart());
        return cart.countProdCart();
    }

    @GetMapping("/cart/del/{id}")
    public String cartDelById(@PathVariable Long id) {
        cart.removeProductFromCart(id);
        System.out.println("товар удален id="+id);

        //return "/products/cart/show";
        return "redirect:/products/cart/show";
    }


}