package com.muzic.dto;

import java.sql.Timestamp;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CertDto {
    private String certEmail;
    private String certNumber;
    private Timestamp certTime;
}
