
package com.muzic.dto;

import java.sql.Date;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder @AllArgsConstructor @NoArgsConstructor @Data
public class RouletteDto {
	
	private int rouletteNo;
	private String rouletteName;
	private int rouletteDailyCount;
	private int  rouletteMaxPoint;
	private int  rouletteMinPoint;
	private Date rouletteDate;

}
