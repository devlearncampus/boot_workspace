package com.sinse.xmlapp.model.bus;

import com.sinse.xmlapp.domain.Item;
import org.springframework.stereotype.Service;
import org.xml.sax.InputSource;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.StringReader;
import java.util.List;

@Service
public class BusService {
    private BusParser  busParser;

    public BusService(BusParser busParser) {
        this.busParser = busParser;
    }

    public List<Item> parse() throws Exception {
        return busParser.parse();
    }
}








