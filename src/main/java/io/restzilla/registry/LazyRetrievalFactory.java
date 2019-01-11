/*
 * (C) 2014 42 bv (www.42.nl). All rights reserved.
 */
package io.restzilla.registry;

import io.restzilla.service.CrudService;
import io.restzilla.service.CrudServiceFactory;

import java.io.Serializable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.data.domain.Persistable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.support.Repositories;

/**
 * Service factory that lazy retrieves from the application context.
 * By lazy retrieval you can be certain that all beans are registered before retrieval.
 *
 * @author Jeroen van Schagen
 * @since Dec 10, 2015
 */
@SuppressWarnings("unchecked")
public class LazyRetrievalFactory implements CrudServiceFactory {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(LazyRetrievalFactory.class);

    private final Repositories repositories;
    
    private final Services services;
    
    private final CrudServiceFactory delegate;

    public LazyRetrievalFactory(ApplicationContext applicationContext, CrudServiceFactory delegate) {
        this.repositories = new Repositories(applicationContext);
        Services services = new Services();
        applicationContext.getAutowireCapableBeanFactory().autowireBean(services);
        this.services = services;
        this.delegate = delegate;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <T extends Persistable<ID>, ID extends Serializable> PagingAndSortingRepository<T, ID> buildRepository(Class<T> entityClass) {
        Object repository = repositories.getRepositoryFor(entityClass);
        if (repository instanceof PagingAndSortingRepository) {
            return (PagingAndSortingRepository<T, ID>) repository;
        } else {
            LOGGER.debug("Generating repository for {} as none is defined.", entityClass);
            return delegate.buildRepository(entityClass);
        }
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public <T extends Persistable<ID>, ID extends Serializable> CrudService<T, ID> buildService(Class<T> entityClass, PagingAndSortingRepository<T, ID> repository) {
        CrudService<?, ?> service = services.getByEntityClass(entityClass);
        if (service != null) {
            return (CrudService<T, ID>) service;
        } else {
            LOGGER.debug("Generating service for {} as none is defined.", entityClass);
            return delegate.buildService(entityClass, repository);
        }
    }

}
