package com.study.board.entitiy;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity //DB 테이블을 의미함 //JPA에서 관리할 객체를 선언합니다.
@Data
public class Board {
    @Id //JPA는 이 ID를 통해서 객체를 관리한다.
    //데이터베이스에 값을 넣기 전까지 기본키를 모르기때문에 관리가되지않음. 따라서 IDENTITY전략 사용.
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String title;
    private String content;
    private String filename;
    private String filepath;
}
