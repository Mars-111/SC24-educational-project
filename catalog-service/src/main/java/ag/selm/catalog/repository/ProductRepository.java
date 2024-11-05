package ag.selm.catalog.repository;

import ag.selm.catalog.entity.Product;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface ProductRepository extends CrudRepository<Product, Integer> {
    Iterable<Product> findAllByTitleLikeIgnoreCase(String filter);
    Iterable<Product> findAllByTitleContainingIgnoreCase(String filter);
}
