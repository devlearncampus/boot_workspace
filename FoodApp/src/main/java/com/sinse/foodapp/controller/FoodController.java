package com.sinse.foodapp.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sinse.foodapp.model.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;

@Slf4j
@RestController
public class FoodController {
    private String serviceKey="gqyjgm7YCCfzUhERdAskcLXMrhYpClNgxHq12hR59LpYnVs9enXgcQPvDsLkhOAN8fTAsSqt8j%2BJ1H6%2BHPfoFw%3D%3D";

    /*
    HttpURLConnection 는 동작은 하지만, 최신의 방식은 아님
    코드가 복잡하다
    */
    @GetMapping("/old/stores")
    public String getList(String store_name) throws IOException {
        StringBuilder urlBuilder = new StringBuilder("http://apis.data.go.kr/6430000/cbRecreationalFoodInfoService/getRecreationalFoodInfo"); /*URL*/
        urlBuilder.append("?" + URLEncoder.encode("serviceKey","UTF-8") + "="+serviceKey); /*Service Key*/
        urlBuilder.append("&" + URLEncoder.encode("currentPage","UTF-8") + "=" + URLEncoder.encode("1", "UTF-8")); /*페이지 번호*/
        urlBuilder.append("&" + URLEncoder.encode("perPage","UTF-8") + "=" + URLEncoder.encode("20", "UTF-8")); /*한 페이지 결과 수*/
        //urlBuilder.append("&" + URLEncoder.encode("CMPNM","UTF-8") + "=" + URLEncoder.encode(store_name, "UTF-8")); /*상호명*/
        URL url = new URL(urlBuilder.toString());
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("Content-type", "application/json");
        System.out.println("Response code: " + conn.getResponseCode());
        BufferedReader rd;
        if(conn.getResponseCode() >= 200 && conn.getResponseCode() <= 300) {
            rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        } else {
            rd = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
        }
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = rd.readLine()) != null) {
            sb.append(line);
        }
        rd.close();
        conn.disconnect();
        System.out.println(sb.toString());
        return sb.toString();
    }

    @GetMapping("/stores")
    public ApiResponse getStores(String store_name) throws IOException, InterruptedException {
        String baseUrl="http://apis.data.go.kr/6430000/cbRecreationalFoodInfoService/getRecreationalFoodInfo";

        //if(store_name.length()<1)store_name="";

        //파라미터 설정
        String url=baseUrl+"?" +
            "serviceKey="+serviceKey+
            "&currentPage="+URLEncoder.encode("1", StandardCharsets.UTF_8)+
                "&perPage="+URLEncoder.encode("20", StandardCharsets.UTF_8)+
                "&CMPNM="+URLEncoder.encode(store_name, StandardCharsets.UTF_8);

        //HttpUrlConnection 보다 개선
        HttpClient client = HttpClient.newHttpClient();

        //요청 정보 객체 생성
        HttpRequest request=HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("Content-Type", "application/json")
                .GET()
                .build();

        //Open API 서버에 요청 시도
        HttpResponse<String> response =client.send(request, HttpResponse.BodyHandlers.ofString());

        //String 자체를 클라이언트에게 전송해버리면, 클라이언트가 문자열을 json 으로 파싱해야 하고,
        //이 방법은 권장되지 않음
        //따라서 지금부터, Open API의 String 결과물을 자바의 클래스로 매핑시키되, jackson 을 이용하여 자동화
        //시키자!! 클라이언트에게 응답 정보로 사용할 수 있을 뿐 아니라, 객체화 되어 있기 때문에 서버에서도
        //활용할 수 있다..
        ObjectMapper objectMapper=new ObjectMapper();
        ApiResponse apiResponse=objectMapper.readValue(response.body(), ApiResponse.class);

        return apiResponse;
    }

}
