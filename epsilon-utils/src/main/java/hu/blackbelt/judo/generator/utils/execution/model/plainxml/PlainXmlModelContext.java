package hu.blackbelt.judo.generator.utils.execution.model.plainxml;

import hu.blackbelt.judo.generator.utils.execution.Log;
import hu.blackbelt.judo.generator.utils.execution.ModelContext;
import lombok.Builder;
import lombok.Data;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.epsilon.eol.exceptions.models.EolModelLoadingException;
import org.eclipse.epsilon.eol.models.IModel;
import org.eclipse.epsilon.eol.models.ModelReference;
import org.eclipse.epsilon.eol.models.ModelRepository;

import java.util.List;

@Data
@Builder
public class PlainXmlModelContext implements ModelContext {

    String artifact;

    String name;

    String xsd;

    List<String> aliases;

    String platformAlias;

    @Builder.Default
    boolean readOnLoad = true;

    @Builder.Default
    boolean storeOnDisposal = false;

    @Builder.Default
    boolean cached = true;


    @Override
    public String toString() {
        return "PlainXmlModelContext{" +
                "artifact='" + artifact + '\'' +
                ", xsd='" + xsd + '\'' +
                ", name='" + name + '\'' +
                ", aliases=" + aliases +
                ", readOnLoad=" + readOnLoad +
                ", storeOnDisposal=" + storeOnDisposal +
                ", cached=" + cached +
                ", platformAlias='" + platformAlias + '\'' +
                '}';
    }

    @Override
    public void addAliases(ModelRepository repository, ModelReference ref) {
        ref.setName(this.getName());
        if (this.getAliases() != null) {
            for (String alias : this.getAliases()) {
                ref.getAliases().add(alias);
            }
        }
        repository.addModel(ref);
    }

    @Override
    public IModel load(Log log, ResourceSet resourceSet, ModelRepository repository, URI uri) throws EolModelLoadingException {
        return PlainXmlModelUtil.loadPlainXml(log, resourceSet, repository, this, uri);
    }

}
