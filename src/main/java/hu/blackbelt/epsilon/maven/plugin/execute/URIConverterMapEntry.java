package hu.blackbelt.epsilon.maven.plugin.execute;

import lombok.Data;
import org.apache.maven.plugins.annotations.Parameter;

@Data
public class URIConverterMapEntry {

    @Parameter(name = "fromURI")
    String fromURI;

    @Parameter(name = "toURI")
    String toURI;
}
