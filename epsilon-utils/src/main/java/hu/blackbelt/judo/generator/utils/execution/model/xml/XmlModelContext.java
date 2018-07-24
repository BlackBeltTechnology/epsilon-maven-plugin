package hu.blackbelt.judo.generator.utils.execution.model.xml;

import com.google.common.collect.ImmutableMap;
import hu.blackbelt.judo.generator.utils.execution.Log;
import hu.blackbelt.judo.generator.utils.execution.ModelContext;
import hu.blackbelt.judo.generator.utils.execution.model.emf.EmfModelContext;
import lombok.Builder;
import lombok.Data;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.epsilon.eol.exceptions.models.EolModelLoadingException;
import org.eclipse.epsilon.eol.models.IModel;
import org.eclipse.epsilon.eol.models.ModelRepository;

import java.util.Map;

@Data
@Builder(builderMethodName = "buildXmlModelContext")
public class XmlModelContext extends EmfModelContext implements ModelContext {

    @Override
    public String toString() {
        return "XmlModel{" +
                "artifact='" + getArtifacts() + '\'' +
                ", name='" + getName() + '\'' +
                ", aliases=" + getAliases() +
                ", readOnLoad=" + isReadOnLoad() +
                ", storeOnDisposal=" + isStoreOnDisposal() +
                ", cached=" + isCached() +
                ", metaModelFile=" + getMetaModelFile() +
                ", platformAlias='" + getPlatformAlias() + '\'' +
                ", expand=" + isExpand() +
                ", metaModelUris=" + getMetaModelUris() +
                ", fileBasedMetamodelUris=" + getFileBasedMetamodelUris() +
                ", modelUri='" + getModelUri() + '\'' +
                ", reuseUnmodifiedFileBasedMetamodels=" + isReuseUnmodifiedFileBasedMetamodels() +
                '}';
    }

    @Override
    public IModel load(Log log, ResourceSet resourceSet, ModelRepository repository, Map<String, URI> uriMap) throws EolModelLoadingException {
        return XmlModelUtils.loadXml(log, resourceSet, repository, this, uriMap.get("xml"), uriMap.get("xsd"));
    }

}
