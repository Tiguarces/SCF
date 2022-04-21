package pl.scf.model.property;

import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class InitializerProperty {
    private UserImages images;
    private List<String> roles;
    private Map<String, String> titles;
}
