package ag.selm.catalog.controllers;

import ag.selm.catalog.controllers.payload.NewProductPayload;
import ag.selm.catalog.entity.Product;
import ag.selm.catalog.service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;
import java.util.Locale;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("catalogue-api/products")
public class ProductsRestController {
    private final ProductService productService;


    @GetMapping
    public Iterable<Product> findProducts(@RequestParam(name = "filter", required = false) String filter) {
        return productService.findAllProducts(filter);
    }

    @PostMapping("/create")
    public ResponseEntity<?> createProduct(@Valid @RequestBody NewProductPayload payload,
                                                 BindingResult bindingResult,
                                                 UriComponentsBuilder uriComponentsBuilder)
            throws BindException {
        if (bindingResult.hasErrors()) {
            if (bindingResult instanceof BindException exception) {
                throw exception;
            } else {
                throw new BindException(bindingResult);
            }
        }
        Product product = productService.createProduct(payload.title(), payload.details());
        return ResponseEntity
                .created(uriComponentsBuilder
                        .replacePath("catalogue-api/products/{productId}")
                        .build(Map.of("productId", product.getId())))
                .body(product);
    }
}

