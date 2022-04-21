package pl.scf.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum TokenType {
    ACCESS("access"), REFRESH("refresh");
    final String name;
}
