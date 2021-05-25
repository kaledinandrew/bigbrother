package dto.scripts;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class CountContactScriptDto {
    private Long id;
    private String scriptName;
    private Long id1;
    private Long id2;
    private Long count;

    public CountContactScriptDto() {

    }

    public CountContactScriptDto(Long id, String scriptName, Long id1, Long id2, Long count) {
        this.id = id;
        this.scriptName = scriptName;
        this.id1 = id1;
        this.id2 = id2;
        this.count = count;
    }
}
