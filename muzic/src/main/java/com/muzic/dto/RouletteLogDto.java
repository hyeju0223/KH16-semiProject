

package com.muzic.dto;

import java.sql.Date;
import java.sql.Timestamp;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
 
@Builder @AllArgsConstructor @NoArgsConstructor @Data
public class RouletteLogDto {

	private int rouletteLogNo;
	private int rouletteLogRoulette;
	private String rouletteLogMember;
	private int  rouletteLogPoint;
	private Timestamp rouletteLogTime;


}
