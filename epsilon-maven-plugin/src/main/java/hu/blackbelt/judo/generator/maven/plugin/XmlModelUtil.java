package hu.blackbelt.judo.generator.maven.plugin;

import org.apache.maven.plugin.MojoExecutionException;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.URIConverter;
import org.eclipse.epsilon.common.util.StringProperties;
import org.eclipse.epsilon.emc.plainxml.PlainXmlModel;
import org.eclipse.epsilon.eol.exceptions.models.EolModelLoadingException;
import org.eclipse.epsilon.eol.models.ModelRepository;

import static java.util.stream.Collectors.joining;

public class XmlModelUtil {

    public static PlainXmlModel loadXml(Log log, ResourceSet resourceSet, ModelRepository repository, XmlModel xmlModel, URI uri) throws MojoExecutionException {

        final PlainXmlModel model = new PlainXmlModel();

        final StringProperties properties = new StringProperties();
        properties.put(PlainXmlModel.PROPERTY_NAME, xmlModel.getName() + "");
        if (xmlModel.getAliases() != null) {
            properties.put(PlainXmlModel.PROPERTY_ALIASES, xmlModel.getAliases().stream().collect(joining(",")) + "");
        } else {
            properties.put(PlainXmlModel.PROPERTY_ALIASES, "");
        }
        properties.put(PlainXmlModel.PROPERTY_READONLOAD, xmlModel.isReadOnLoad()+ "");
        properties.put(PlainXmlModel.PROPERTY_STOREONDISPOSAL, xmlModel.isStoreOnDisposal() + "");
        properties.put(PlainXmlModel.PROPERTY_CACHED, xmlModel.isCached() + "");

        properties.put(PlainXmlModel.PROPERTY_URI, uri);
        // model.setFile(new File(uri.toFileString()));
        /*
        log.info("Registering MODEL_URI:" + uri.toString());
        */

        if (xmlModel.getPlatformAlias() != null && !xmlModel.getPlatformAlias().trim().equals("")) {
            properties.put(PlainXmlModel.PROPERTY_URI, xmlModel.getPlatformAlias());
            log.info(String.format("Registering MODEL_URI: %s Alias URI: %s" , uri.toString(), xmlModel.getPlatformAlias()));
            URIConverter.INSTANCE.URI_MAP.put(URI.createURI(xmlModel.getPlatformAlias()), uri);
        } else {
            log.info(String.format("Registering MODEL_URI: %s", uri.toString()));
        }

        /* TODO: Find a way to handle relative pathes on models */
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
            model.setName(xmlModel.getName());
            repository.addModel(model);
            return model;
        } catch (EolModelLoadingException e) {
            throw new MojoExecutionException("Cannot load model: " + uri.toString(), e);
        }
    }


}
