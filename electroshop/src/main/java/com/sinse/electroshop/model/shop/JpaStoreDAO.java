package com.sinse.electroshop.model.shop;

import com.sinse.electroshop.domain.Store;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor //public JpaStoreDAO(StoreRepository storeRepository){}
public class JpaStoreDAO implements StoreDAO {

    private final StoreRepository storeRepository;

    @Override
    public Store save(Store store) {
        return storeRepository.save(store);
    }

    @Override
    public Store findById(int store_id) {
        return null;
    }

    @Override
    public List<Store> findAll() {
        return List.of();
    }

    @Override
    public Store update(Store store) {
        return null;
    }

    @Override
    public void deleteById(int store_id) {

    }
}
