package com.sinse.restapp.domain;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class ApiResponse {
    private List<Body> body;
    private Header header;

    @Data
    public static class Body {
        private String SIGUN;
        private double LO;
        private String MNMNU;
        private String SE;
        @JsonProperty("SIGUN")  // JSON 키와 1:1 매핑
        private String sigun;

        @JsonProperty("LO")
        private double lo;

        @JsonProperty("MNMNU")
        private String mnmnu;

        @JsonProperty("SE")
        private String se;

        @JsonProperty("CMPNM")
        private String cmpnm;

        @JsonProperty("MENU")
        private String menu;

        @JsonProperty("TELNO")
        private String telno;

        @JsonProperty("_URL")           // 특수 키: 필드명은 url로 인코딩
        @JsonAlias({"url", "URL"})      // 혹시 다른 소스가 소문자/대문자 url로 줄 때도 대비
        private String url;

        @JsonProperty("ADRES")
        private String adres;

        @JsonProperty("LA")
        private double la;

        @JsonProperty("TIME")
        private String time;

        @JsonProperty("DC")
        private String dc;
    }

    @Data
    public static class Header {
        private int perPage;
        private String resultCode;
        private int totalRows;
        private int currentPage;
        private String resultMsg;
    }
}
