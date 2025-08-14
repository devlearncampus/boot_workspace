package com.sinse.xmlapp.controller;

import com.sinse.xmlapp.domain.Board;
import com.sinse.xmlapp.domain.Item;
import com.sinse.xmlapp.model.bus.BusService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.List;

@Slf4j
@RestController
public class RestBusController {

    private BusService busService;

    public RestBusController(BusService busService) {
        this.busService = busService;
    }

    //@GetMapping(value="/buses",produces =MediaType.APPLICATION_XML_VALUE)
    @GetMapping(value="/buses")
    public List<Item> getList() throws Exception {
        return busService.parse();
    }
}
