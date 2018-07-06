package hu.blackbelt.judo.generator.maven.plugin;

import hu.blackbelt.judo.generator.utils.execution.Log;
import org.apache.maven.plugin.MojoExecutionException;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.URIConverter;
import org.eclipse.epsilon.common.util.StringProperties;
import org.eclipse.epsilon.eol.exceptions.models.EolModelLoadingException;
import org.eclipse.epsilon.eol.models.ModelRepository;

import static java.util.stream.Collectors.joining;

public class XmlModelUtil {

    public static org.eclipse.epsilon.emc.plainxml.PlainXmlModel loadXml(Log log, ResourceSet resourceSet, ModelRepository repository, PlainXmlModel plainXmlModel, URI uri) throws MojoExecutionException {

        final org.eclipse.epsilon.emc.plainxml.PlainXmlModel model = new org.eclipse.epsilon.emc.plainxml.PlainXmlModel();

        final StringProperties properties = new StringProperties();
        properties.put(org.eclipse.epsilon.emc.plainxml.PlainXmlModel.PROPERTY_NAME, plainXmlModel.getName() + "");
        if (plainXmlModel.getAliases() != null) {
            properties.put(org.eclipse.epsilon.emc.plainxml.PlainXmlModel.PROPERTY_ALIASES, plainXmlModel.getAliases().stream().collect(joining(",")) + "");
        } else {
            properties.put(org.eclipse.epsilon.emc.plainxml.PlainXmlModel.PROPERTY_ALIASES, "");
        }
        properties.put(org.eclipse.epsilon.emc.plainxml.PlainXmlModel.PROPERTY_READONLOAD, plainXmlModel.isReadOnLoad()+ "");
        properties.put(org.eclipse.epsilon.emc.plainxml.PlainXmlModel.PROPERTY_STOREONDISPOSAL, plainXmlModel.isStoreOnDisposal() + "");
        properties.put(org.eclipse.epsilon.emc.plainxml.PlainXmlModel.PROPERTY_CACHED, plainXmlModel.isCached() + "");

        properties.put(org.eclipse.epsilon.emc.plainxml.PlainXmlModel.PROPERTY_URI, uri);
        // model.setFile(new File(uri.toFileString()));
        /*
        log.info("Registering MODEL_URI:" + uri.toString());
        */

        if (plainXmlModel.getPlatformAlias() != null && !plainXmlModel.getPlatformAlias().trim().equals("")) {
            properties.put(org.eclipse.epsilon.emc.plainxml.PlainXmlModel.PROPERTY_URI, plainXmlModel.getPlatformAlias());
            log.info(String.format("Registering MODEL_URI: %s Alias URI: %s" , uri.toString(), plainXmlModel.getPlatformAlias()));
            URIConverter.INSTANCE.URI_MAP.put(URI.createURI(plainXmlModel.getPlatformAlias()), uri);
        } else {
            log.info(String.format("Registering MODEL_URI: %s", uri.toString()));
        }

        /* TODO: Find a way to handle relative pathes on ecoreModels */
        /*
        if (emfModel.getUrlAliases() != null) {
            for (String urlAlias : emfModel.getUrlAliases()) {
                if (!urlAlias.trim().equals("")) {
                    log.info(String.format("Adding URL alias: %s", urlAlias));
                    URIConverter.INSTANCE.URI_MAP.put(URI.createFileURI(urlAlias), uri);
                }
            }
        } */

        try {
            model.load(properties);
            model.setName(plainXmlModel.getName());
            repository.addModel(model);
            return model;
        } catch (EolModelLoadingException e) {
            throw new MojoExecutionException("Cannot loadEmf model: " + uri.toString(), e);
        }
    }


}
