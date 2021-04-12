package kang.pack;

import org.gradle.api.artifacts.ConfigurationContainer;
import org.gradle.api.artifacts.dsl.ComponentMetadataHandler;
import org.gradle.api.artifacts.dsl.ComponentModuleMetadataHandler;
import org.gradle.api.artifacts.dsl.DependencyConstraintHandler;
import org.gradle.api.artifacts.type.ArtifactTypeContainer;
import org.gradle.api.attributes.AttributesSchema;
import org.gradle.api.internal.artifacts.VariantTransformRegistry;
import org.gradle.api.internal.artifacts.dsl.dependencies.DefaultDependencyHandler;
import org.gradle.api.internal.artifacts.dsl.dependencies.DependencyFactory;
import org.gradle.api.internal.artifacts.dsl.dependencies.PlatformSupport;
import org.gradle.api.internal.artifacts.dsl.dependencies.ProjectFinder;
import org.gradle.api.internal.artifacts.query.ArtifactResolutionQueryFactory;
import org.gradle.api.internal.model.NamedObjectInstantiator;
import org.gradle.api.plugins.ExtensionContainer;
import org.gradle.internal.Factory;

public class MyDependencyHandler extends DefaultDependencyHandler {
    public MyDependencyHandler(ConfigurationContainer configurationContainer,
                               DependencyFactory dependencyFactory,
                               ProjectFinder projectFinder, DependencyConstraintHandler dependencyConstraintHandler,
                               ComponentMetadataHandler componentMetadataHandler,
                               ComponentModuleMetadataHandler componentModuleMetadataHandler,
                               ArtifactResolutionQueryFactory resolutionQueryFactory,
                               AttributesSchema attributesSchema, VariantTransformRegistry transforms,
                               Factory<ArtifactTypeContainer> artifactTypeContainer,
                               NamedObjectInstantiator namedObjectInstantiator, PlatformSupport platformSupport) {
        super(configurationContainer, dependencyFactory, projectFinder,
                dependencyConstraintHandler, componentMetadataHandler,
                componentModuleMetadataHandler, resolutionQueryFactory, attributesSchema,
                transforms, artifactTypeContainer, namedObjectInstantiator, platformSupport);
    }


    @Override
    public ExtensionContainer getExtensions() {
        return null;
    }
}
