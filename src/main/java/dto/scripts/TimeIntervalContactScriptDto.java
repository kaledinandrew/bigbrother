package dto.scripts;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import models.scripts.TimeIntervalContactScript;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class TimeIntervalContactScriptDto extends BaseScriptDto {
    private Long id;
    private String scriptName;
    private Long id1;
    private Long id2;
    private Long from;
    private Long to;
    private Boolean success;

    public TimeIntervalContactScriptDto() {}

    public TimeIntervalContactScriptDto(Long id, String scriptName, Long id1, Long id2,
                                        Long from, Long to, Boolean success) {
        this.id = id;
        this.scriptName = scriptName;
        this.id1 = id1;
        this.id2 = id2;
        this.from = from;
        this.to = to;
        this.success = success;
    }

    public static TimeIntervalContactScriptDto fromTimeIntervalContactScript(TimeIntervalContactScript script) {
        return new TimeIntervalContactScriptDto(
                script.getId(),
                script.getScriptName(),
                script.getId1(),
                script.getId2(),
                script.getFrom(),
                script.getTo(),
                script.getSuccess()
        );
    }
}
