package dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class ContactDto {
    private Long id1;
    private Long id2;
    // time in the format of UNIX Timestamp
    private Long time;
}
