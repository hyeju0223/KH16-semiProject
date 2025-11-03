// src/main/java/com/muzic/dto/RouletteLogDto.java
package com.muzic.dto;

import java.sql.Timestamp;
import lombok.*;

@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class RouletteLogDto {
    private int rouletteLogNo;        // PK
    private int rouletteLogRoulette;  // FK -> roulette.roulette_no
    private String rouletteLogMember; // FK -> member.member_id
    private int rouletteLogPoint;     // >= 0
    private Timestamp rouletteLogTime;// default systimestamp
}
