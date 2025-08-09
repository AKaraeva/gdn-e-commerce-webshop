package at.gdn.backend.service;

import at.gdn.backend.entities.Product;
import at.gdn.backend.persistence.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static java.lang.Long.valueOf;

@RequiredArgsConstructor
@Service
@Transactional
public class ProductService {

    private final ProductRepository productRepository;

    public List<Product>getAllProducts() {
        return productRepository.findAll();
    }

    public List<Product> getByCategory(String category) {
        return productRepository.findByCategories_categoryName(category);
    }

    public Optional <Product> getProductById(Long productId) {
        return productRepository.findById(new Product.ProductId(valueOf(productId)));
    }
}
