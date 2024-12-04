package dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EndpointHitDto {
   private Integer id;
   private String app;
   private String uri;
   private String ip;
   @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
   private LocalDateTime timestamp;
}