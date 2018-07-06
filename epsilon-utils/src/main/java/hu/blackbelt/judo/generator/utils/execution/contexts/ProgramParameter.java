package hu.blackbelt.judo.generator.utils.execution.contexts;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class ProgramParameter {

    String name;

    String value;
}
