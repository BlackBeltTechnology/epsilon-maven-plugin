package hu.blackbelt.judo.generator.utils.execution;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.epsilon.eol.exceptions.models.EolModelLoadingException;
import org.eclipse.epsilon.eol.models.IModel;
import org.eclipse.epsilon.eol.models.ModelReference;
import org.eclipse.epsilon.eol.models.ModelRepository;

import java.util.List;

public interface ModelContext {
    IModel load(Log log, ResourceSet resourceSet, ModelRepository repository, URI uri) throws EolModelLoadingException;
    void addAliases(ModelRepository repository, ModelReference ref);
    List<String> getAliases();
    String getArtifact();

}
