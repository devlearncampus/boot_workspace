package com.sinse.xmlapp.domain;

import lombok.Data;

@Data
public class Board {
    private int board_id;
    private String title;
    private String writer;
    private String content;
    private String regdate;
    private int hit;
}
