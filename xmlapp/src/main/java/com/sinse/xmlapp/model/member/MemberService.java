package com.sinse.xmlapp.model.member;

import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.util.List;

public interface MemberService {
    public List<Member> parse() throws IOException, ParserConfigurationException, SAXException;
}
