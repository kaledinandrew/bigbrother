package dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import models.Attr;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class AttrDto {
    private Long id;
    private String name;

    public AttrDto() {

    }

    public AttrDto(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public static AttrDto fromAttr(Attr attr) {
        return new AttrDto(attr.getId(), attr.getName());
    }
}
