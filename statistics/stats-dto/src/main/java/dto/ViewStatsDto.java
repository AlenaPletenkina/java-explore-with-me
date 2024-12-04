package dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
public class ViewStatsDto {
    private String app;
    private String uri;
    private Integer hits;
}
