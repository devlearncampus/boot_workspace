package com.sinse.electroshop.controller.store;

import com.sinse.electroshop.controller.dto.StoreDTO;
import com.sinse.electroshop.domain.Store;
import com.sinse.electroshop.model.shop.StoreService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Slf4j
@Controller
public class StoreController {

    private final StoreService storeService;

    //Lombok을 사용하지 않을 경우 아래와 같이 매개변수 있는 생성자를 명시하면 된다..자동 주입
    public StoreController(StoreService storeService) {
        this.storeService = storeService;
    }


    //로그인폼 요청 처리
    @GetMapping("/store/loginform")
    public String loginForm() {
        return "store/login";
    }

    //상점의 로그인 요청 처리
    @PostMapping("/store/login")
    @ResponseBody
    public ResponseEntity<String> login(StoreDTO storeDTO) {

        log.debug(storeDTO.toString());

        //파라미터가 담겨있는 객체는 DTO이므로, Model 객체인 Store로 옮기자
        Store store = new Store();
        store.setBusiness_id(storeDTO.getId());
        store.setPassword(storeDTO.getPwd());
        store.setStore_name(storeDTO.getStore_name());

        storeService.regist(store);

        return ResponseEntity.ok("success");
    }

    //상점 관리자 메인요청
    @GetMapping("/store/main")
    public String main() {
        return "store/index";
    }


}















