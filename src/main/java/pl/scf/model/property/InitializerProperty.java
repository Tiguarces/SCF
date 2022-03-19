package pl.scf.model.property;

import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class InitializerProperty {
    private Map<String, String> titles;
    private List<String> roles;
}
