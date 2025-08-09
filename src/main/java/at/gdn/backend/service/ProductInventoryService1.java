package at.gdn.backend.service;

import at.gdn.backend.entities.Product;
import at.gdn.backend.persistence.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.domain.Pageable;
import java.util.Set;
import java.util.stream.Stream;


@Transactional(readOnly = true)
@Service
public class ProductInventoryService1 {
    private final ProductRepository productRepository;

    public ProductInventoryService1(
            @Autowired ProductRepository productRepository){
        this.productRepository = productRepository;
    }
    public Stream<Product> findAll(Pageable pageable) {
        return productRepository.findAll( pageable ).stream();
    }

    public void deleteProduct(Set<Product> productSet) {
        productRepository.deleteAll(productSet);
    }
}