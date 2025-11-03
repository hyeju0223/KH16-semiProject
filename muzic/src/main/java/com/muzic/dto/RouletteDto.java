// src/main/java/com/muzic/dto/RouletteDto.java
package com.muzic.dto;

import java.sql.Date; // Oracle DATE <-> java.sql.Date
import lombok.*;

@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class RouletteDto {
    private int  rouletteNo;          // PK
    private String rouletteName;      // NOT NULL
    private int  rouletteDailyCount;  // NOT NULL
    private int  rouletteMaxPoint;    // NOT NULL
    private int  rouletteMinPoint;    // NOT NULL
    private Date rouletteDate;        // NOT NULL (yyyy-mm-dd)
}
