package ag.selm.manager.controller;

import ag.selm.manager.client.MyBadRequestException;
import ag.selm.manager.client.ProductRestClient;
import ag.selm.manager.controller.payload.UpdateProductPayload;
import ag.selm.manager.entity.Product;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;

import java.util.Locale;
import java.util.NoSuchElementException;

@Controller
@RequestMapping(("catalogue/products/{productId:\\d+}"))
@RequiredArgsConstructor
public class ProductController {

    private final ProductRestClient productRestClient;

    private final MessageSource messageSource;

    @ModelAttribute("product")
    public Product product(@PathVariable("productId") int productId) {
        return this.productRestClient.findProduct(productId)
                .orElseThrow(() -> new NoSuchElementException("catalogue.errors.product.not_found"));
    }

    @GetMapping
    public String getProduct() {
        return "catalogue/products/product";
    }

    @GetMapping("edit")
    public String getProductEditPage() {
        return "catalogue/products/edit";
    }

    @PostMapping("edit")
    public String updateProduct(@ModelAttribute(name = "product", binding = false) Product product,
                                UpdateProductPayload payload,
                                Model model) {
        try {
            this.productRestClient.updateProduct(product.id(), payload.title(), payload.details());
            return "redirect:/catalogue/products/%d".formatted(product.id());
        } catch (MyBadRequestException exception) {
            model.addAttribute("payload", payload);
            model.addAttribute("errors", exception.getErrors());
            return "catalogue/products/edit";
        }
    }

    @PostMapping("delete")
    public String deleteProduct(@ModelAttribute("product") Product product) {
        this.productRestClient.deleteProduct(product.id());
        return "redirect:/catalogue/products/list";
    }

    @ExceptionHandler(NoSuchElementException.class)
    public String handleNoSuchElementException(NoSuchElementException exception, Model model,
                                               HttpServletResponse response, Locale locale) {
        response.setStatus(HttpStatus.NOT_FOUND.value());
        model.addAttribute("error",
                this.messageSource.getMessage(exception.getMessage(), new Object[0],
                        exception.getMessage(), locale));
        return "errors/404";
    }
}
