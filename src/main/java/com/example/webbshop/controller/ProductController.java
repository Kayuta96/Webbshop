package com.example.webbshop.controller;

import com.example.webbshop.model.Product;
import com.example.webbshop.service.ProductService;
import com.example.webbshop.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Controller
@RequestMapping("/products")
public class ProductController {

    private final ProductService productService;
    private final CategoryService categoryService;

    @Autowired
    public ProductController(ProductService productService, CategoryService categoryService) {
        this.productService = productService;
        this.categoryService = categoryService;
    }

    @GetMapping
    public String listProducts(Model model) {
        model.addAttribute("products", productService.getAllProducts());
        return "product-list"; // Points to product-list.html
    }

    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("product", new Product());
        model.addAttribute("categories", categoryService.getAllCategories());
        return "product-form";
    }

    @PostMapping
    public String createProduct(@ModelAttribute Product product, Model model) {
        // Manual validation checks
        if (product.getName() == null || product.getName().isEmpty() ||
                product.getPrice() <= 0 || product.getQuantity() < 0) {
            model.addAttribute("error", "Please provide valid product details.");
            model.addAttribute("categories", categoryService.getAllCategories());
            return "product-form";
        }
        productService.saveProduct(product);
        return "redirect:/products";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        Optional<Product> product = productService.getProductById(id);
        if (product.isPresent()) {
            model.addAttribute("product", product.get());
            model.addAttribute("categories", categoryService.getAllCategories());
            return "product-form";
        } else {
            model.addAttribute("error", "Product not found");
            return "redirect:/products";
        }
    }

    @PostMapping("/update/{id}")
    public String updateProduct(@PathVariable Long id, @ModelAttribute Product product, Model model) {
        // Manual validation checks
        if (product.getName() == null || product.getName().isEmpty() ||
                product.getPrice() <= 0 || product.getQuantity() < 0) {
            model.addAttribute("error", "Please provide valid product details.");
            model.addAttribute("categories", categoryService.getAllCategories());
            return "product-form";
        }
        product.setId(id);
        productService.saveProduct(product);
        return "redirect:/products";
    }

    @GetMapping("/delete/{id}")
    public String deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
        return "redirect:/products";
    }
}
