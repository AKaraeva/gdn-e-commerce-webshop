package at.gdn.backend.service;

import at.gdn.backend.entities.ProductInventory;
import at.gdn.backend.persistence.repository.ProductInventoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;
import java.util.stream.Stream;


@Transactional(readOnly = true)
@Service
public class ProductInventoryService2 {
    private final ProductInventoryRepository productInventoryRepository;

    public ProductInventoryService2(
            @Autowired ProductInventoryRepository productInventoryRepository){
        this.productInventoryRepository = productInventoryRepository;
    }
    public Stream<ProductInventory> findAll(Pageable pageable) {
        return productInventoryRepository.findAll( pageable ).stream();
    }
    public void deleteProduct(Set<ProductInventory> productInventorySet) {
        productInventoryRepository.deleteAll(productInventorySet);
    }
}
