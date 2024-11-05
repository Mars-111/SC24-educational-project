package ag.selm.manager.controller;

import ag.selm.manager.client.MyBadRequestException;
import ag.selm.manager.client.ProductRestClient;
import ag.selm.manager.controller.payload.NewProductPayload;
import ag.selm.manager.entity.Product;
import lombok.RequiredArgsConstructor;

import org.apache.coyote.BadRequestException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Arrays;

@Controller
@RequiredArgsConstructor
@RequestMapping("catalogue/products")
public class ProductsController {

    private final ProductRestClient productRestClient;

    @GetMapping("list")
    public String getProductsList(Model model, @RequestParam(name = "filter", required = false) String filter) {
        model.addAttribute("products", this.productRestClient.findAllProducts(filter));
        model.addAttribute("filter", filter);
        return "/catalogue/products/list";
    }

    @GetMapping("create")
    public String getNewProductPage() {
        return "catalogue/products/new_product";
    }

    @PostMapping("create")
    public String createProduct(NewProductPayload payload,
                                Model model) {
        try {
            Product product = this.productRestClient.createProduct(payload.title(), payload.details());
            return "redirect:/catalogue/products/%d".formatted(product.id());
        } catch (MyBadRequestException exception) {
            model.addAttribute("errors", exception.getErrors());
            model.addAttribute("payload", payload);
            return "catalogue/products/new_product";
        }

    }
}