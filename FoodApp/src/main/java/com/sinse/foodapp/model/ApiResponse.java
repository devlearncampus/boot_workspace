package com.sinse.foodapp.model;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

//Open API로 부터 받은 응답 정보를 담기 위한 객체
@Data
public class ApiResponse {
    private List<Body> body;
    private Header header;

    //Body를 정의 ( 내부 클래스로 정의한다, 왜? 분산시키면 유지관리 하기가 복잡할 수 있음 )
    @Data
    public static class Body{
        private String SIGUN; //관할 행정구역
        @JsonProperty("SIGUN")
        private String sigun;

        private double LO; // 경도
        @JsonProperty("LO")
        private double lo;

        private String MNMNU; //메인 메뉴
        @JsonProperty("MNMNU")
        private String mnmnu;

        private String SE;
        @JsonProperty("SE")
        private String se;

        private String CMPNM;
        @JsonProperty("CMPNM")
        private String cmpnm;


        private String MENU;
        @JsonProperty("MENU")
        private String menu;

        private String TELNO;
        @JsonProperty("TELNO")
        private String telno;


        //private String _URL;
        @JsonProperty("_URL")
        @JsonAlias({"url", "URL"})
        private String url;

        private String ADRES;
        @JsonProperty("ADRES")
        private String adres;

        private double LA; //위도
        @JsonProperty("LA")
        private double la;

        private String TIME;
        @JsonProperty("TIME")
        private String time;

        private String DC;
        @JsonProperty("DC")
        private String dc;
    }

    //Header를 정의
    @Data
    public static class Header{
        private int perPage;
        private String resultCode;
        private int totalRows;
        private int currentPage;
        private String resultMsg;
    }
}
