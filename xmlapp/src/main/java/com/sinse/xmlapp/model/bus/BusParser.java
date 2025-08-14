package com.sinse.xmlapp.model.bus;

import com.sinse.xmlapp.domain.Item;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.xml.sax.InputSource;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.List;

@Slf4j
@Component
public class BusParser {

    private BusHandler busHandler;

    public BusParser(BusHandler busHandler) {
        this.busHandler = busHandler;
    }

    public List<Item> parse() throws Exception {
        String servicekey="gqyjgm7YCCfzUhERdAskcLXMrhYpClNgxHq12hR59LpYnVs9enXgcQPvDsLkhOAN8fTAsSqt8j%2BJ1H6%2BHPfoFw%3D%3D";

        StringBuilder urlBuilder = new StringBuilder("https://apis.data.go.kr/6260000/BusanBIMS/busStopList"); /*URL*/
        urlBuilder.append("?" + URLEncoder.encode("serviceKey","UTF-8") + "="+servicekey); /*Service Key*/
        urlBuilder.append("&" + URLEncoder.encode("pageNo","UTF-8") + "=" + URLEncoder.encode("1", "UTF-8")); /*페이지번호*/
        urlBuilder.append("&" + URLEncoder.encode("numOfRows","UTF-8") + "=" + URLEncoder.encode("10", "UTF-8")); /*한 페이지 결과 수*/
        //urlBuilder.append("&" + URLEncoder.encode("bstopnm","UTF-8") + "=" + URLEncoder.encode("부산시청", "UTF-8")); /*정류소 명*/
        //urlBuilder.append("&" + URLEncoder.encode("resultType","UTF-8") + "=" + URLEncoder.encode("xml", "UTF-8"));


        URL url = new URL(urlBuilder.toString());

        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("Accept", "application/xml");
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

        log.debug(sb.toString());

        SAXParserFactory factory = SAXParserFactory.newInstance();
        SAXParser saxParser= factory.newSAXParser();
        InputSource is = new InputSource(new StringReader(sb.toString()));

        saxParser.parse(is, busHandler);

        return busHandler.getItemList();

    }
}
