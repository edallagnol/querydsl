/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.apt;

import java.lang.annotation.Annotation;
import java.util.List;

import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;

import com.mysema.commons.lang.Assert;
import com.mysema.query.annotations.QueryProjection;

/**
 * @author tiwe
 *
 */
public class Configuration {
    
    private String namePrefix = "Q";
    
    protected final Class<? extends Annotation> entityAnn, superTypeAnn, embeddableAnn, skipAnn;
    
    private boolean useFields = true, useGetters = true;
    
    public Configuration(
            Class<? extends Annotation> entityAnn, 
            Class<? extends Annotation> superTypeAnn,
            Class<? extends Annotation> embeddableAnn,
            Class<? extends Annotation> skipAnn) {
        this.entityAnn = Assert.notNull(entityAnn);
        this.superTypeAnn = superTypeAnn;
        this.embeddableAnn = embeddableAnn;
        this.skipAnn = skipAnn;             
    }
    
    public VisitorConfig getConfig(TypeElement e, List<? extends Element> elements){
        if (useFields){
            if (useGetters){
                return VisitorConfig.ALL;        
            }else{
                return VisitorConfig.FIELDS_ONLY;
            }
        }else if (useGetters){
            return VisitorConfig.METHODS_ONLY;
        }else{
            return VisitorConfig.NONE;
        }
        
    }
    
    public boolean isValidConstructor(ExecutableElement constructor) {
        return constructor.getModifiers().contains(Modifier.PUBLIC)
            && constructor.getAnnotation(QueryProjection.class) != null
            && !constructor.getParameters().isEmpty();
    }
    
    public boolean isValidField(VariableElement field) {
        return field.getAnnotation(skipAnn) == null
            && !field.getModifiers().contains(Modifier.TRANSIENT) 
            && !field.getModifiers().contains(Modifier.STATIC);
    }

    public boolean isValidGetter(ExecutableElement getter){
        return getter.getAnnotation(skipAnn) == null
            && !getter.getModifiers().contains(Modifier.STATIC);
    }

    public Class<? extends Annotation> getEntityAnn() {
        return entityAnn;
    }

    public Class<? extends Annotation> getSuperTypeAnn() {
        return superTypeAnn;
    }

    public Class<? extends Annotation> getEmbeddableAnn() {
        return embeddableAnn;
    }

    public Class<? extends Annotation> getSkipAnn() {
        return skipAnn;
    }

    public void setUseGetters(boolean b) {
        this.useGetters = b;        
    }
    
    public void setUseFields(boolean b){
        this.useFields = b;
    }

    public String getNamePrefix() {
        return namePrefix;
    }

    public void setNamePrefix(String namePrefix) {
        this.namePrefix = namePrefix;
    }
    
}
