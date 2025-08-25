package com.sinse.electroshop.model.shop;

import com.sinse.electroshop.domain.Store;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class StoreServiceImpl implements StoreService {

    private final StoreDAO storeDAO;

    @Override
    public Store regist(Store store) {
        return storeDAO.save(store);
    }
}
