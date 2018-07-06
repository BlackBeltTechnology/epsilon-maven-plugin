package hu.blackbelt.judo.generator.utils.execution.model.emf;

import hu.blackbelt.judo.generator.utils.execution.Log;
import lombok.SneakyThrows;
import org.apache.maven.plugin.MojoExecutionException;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EDataType;
import org.eclipse.emf.ecore.EEnum;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EcorePackage;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.URIConverter;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.epsilon.common.util.StringProperties;
import org.eclipse.epsilon.emc.emf.DefaultXMIResource;
import org.eclipse.epsilon.emc.emf.EmfModel;
import org.eclipse.epsilon.eol.exceptions.models.EolModelLoadingException;
import org.eclipse.epsilon.eol.models.IModel;
import org.eclipse.epsilon.eol.models.IReflectiveModel;
import org.eclipse.epsilon.eol.models.ModelReference;
import org.eclipse.epsilon.eol.models.ModelRepository;
import org.eclipse.epsilon.eol.models.ReflectiveModelReference;
import org.eclipse.uml2.uml.UMLPackage;
import org.eclipse.uml2.uml.resource.UMLResource;
import org.eclipse.uml2.uml.resources.util.UMLResourcesUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import static hu.blackbelt.judo.generator.utils.execution.EmfUtils.convertFileToUri;
import static java.util.stream.Collectors.joining;

public final class EmfModelUtils {

    public static EmfModel loadEmf(Log log, ResourceSet resourceSet, ModelRepository repository, EmfModelContext emfModel, URI uri) throws EolModelLoadingException {

        final EmfModel model = createEmfModel(resourceSet);

        final StringProperties properties = new StringProperties();
        properties.put(EmfModel.PROPERTY_NAME, emfModel.getName() + "");
        if (emfModel.getAliases() != null) {
            properties.put(EmfModel.PROPERTY_ALIASES, emfModel.getAliases().stream().collect(joining(",")) + "");
        } else {
            properties.put(EmfModel.PROPERTY_ALIASES, "");
        }
        properties.put(EmfModel.PROPERTY_READONLOAD, emfModel.isReadOnLoad()+ "");
        properties.put(EmfModel.PROPERTY_STOREONDISPOSAL, emfModel.isStoreOnDisposal() + "");
        properties.put(EmfModel.PROPERTY_EXPAND, emfModel.isExpand() + "");
        properties.put(EmfModel.PROPERTY_CACHED, emfModel.isCached() + "");
        properties.put(EmfModel.PROPERTY_REUSE_UNMODIFIED_FILE_BASED_METAMODELS, emfModel.isReuseUnmodifiedFileBasedMetamodels() + "");

        String metamodelUri = emfModel.getMetaModelUris().stream().collect(joining(","));
        //File modelFile = emfModel.getModelFile();
        String modelUri = emfModel.getMetaModelUris().stream().collect(joining(","));
        File metamodelFile = emfModel.getMetaModelFile();

        if (metamodelUri != null) {
            properties.put(EmfModel.PROPERTY_METAMODEL_URI, metamodelUri + "");
        }

        /*
        if (modelFile != null && modelUri != null) {
            throw new MojoExecutionException("Only one of modelFile or modelUri may be used");
        } else if (modelUri != null) {
            properties.put(EmfModel.PROPERTY_MODEL_URI, modelUri);
        } else {
            properties.put(EmfModel.PROPERTY_MODEL_URI, convertFileToUri(modelFile));
        }
        */
        properties.put(EmfModel.PROPERTY_MODEL_URI, uri);
        /*
        log.info("Registering MODEL_URI:" + uri.toString());
        */
        
        if (metamodelFile != null) {
            log.info("Using file base metamodel: " + metamodelFile);
            properties.put(EmfModel.PROPERTY_FILE_BASED_METAMODEL_URI, convertFileToUri(metamodelFile));
        }

        if (emfModel.getPlatformAlias() != null && !emfModel.getPlatformAlias().trim().equals("")) {
            properties.put(EmfModel.PROPERTY_MODEL_URI, emfModel.getPlatformAlias());
            log.info(String.format("Registering MODEL_URI: %s Alias URI: %s" , uri.toString(), emfModel.getPlatformAlias()));
            URIConverter.INSTANCE.URI_MAP.put(URI.createURI(emfModel.getPlatformAlias()), uri);
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
        
        model.load(properties);
        model.setName(emfModel.getName());
        repository.addModel(model);
        return model;
    }

    public static EmfModel createEmfModel(ResourceSet resourceSet) {
        return new OptimizedEmfModel();
        //return new EmfModel();
    }
}
