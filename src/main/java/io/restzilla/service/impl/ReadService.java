/*
 * (C) 2014 42 bv (www.42.nl). All rights reserved.
 */
package io.restzilla.service.impl;

import io.restzilla.service.CrudServiceRegistry;

import java.io.Serializable;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Persistable;
import org.springframework.data.domain.Sort;

/**
 * Service capable of reading any type of entity.
 * <br/><br/>
 * <b>This service is strictly for internal usage and should
 * not be exposed to the outside.</b>
 *
 * @author Jeroen van Schagen
 * @since Nov 6, 2015
 */
public class ReadService {
    
    private final CrudServiceRegistry registry;
    
    /**
     * Initialize a new read service.
     * 
     * @param registry the service registry, used to retrieve delegate services
     */
    public ReadService(CrudServiceRegistry registry) {
        this.registry = registry;
    }

    /**
     * Retrieve all entities with a certain sort.
     * 
     * @param entityClass the entity class
     * @param sort the sorting
     * @return the sorted entities
     */
    public <T extends Persistable<ID>, ID extends Serializable> List<T> findAll(Class<T> entityClass, Sort sort) {
        return registry.getService(entityClass).findAll(sort);
    }

    /**
     * Retrieve a page of entities.
     * 
     * @param entityClass the entity class
     * @param pageable the pageable
     * @return the page of entities
     */
    public <T extends Persistable<ID>, ID extends Serializable> Page<T> findAll(Class<T> entityClass, Pageable pageable) {
        return registry.getService(entityClass).findAll(pageable);
    }

    /**
     * Retrieve a specific entity, with an identifier.
     * 
     * @param entityClass the entity class
     * @param id the identifier
     * @return the result entity, if any
     */
    public <T extends Persistable<ID>, ID extends Serializable> T getOne(Class<T> entityClass, ID id) {
        return registry.getService(entityClass).findOne(id);
    }

}
