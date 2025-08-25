package com.sinse.electroshop.model.shop;

import com.sinse.electroshop.domain.Store;

import java.util.List;

public interface StoreDAO {

    //상점 등록
    public Store save(Store store);

    //상점 한건 가져오기
    public Store findById(int store_id);

    //모든 상점 가져오기
    public List<Store> findAll();

    //상점 정보 수정
    public Store update(Store store);

    //상점 정보 삭제
    public void deleteById(int store_id);
}








