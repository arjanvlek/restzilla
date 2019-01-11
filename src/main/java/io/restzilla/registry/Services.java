package io.restzilla.registry;

import io.restzilla.service.CrudService;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Registry for internal services.
 *
 * @author Jeroen van Schagen
 * @since Dec 10, 2015
 */
@SuppressWarnings("rawtypes")
class Services {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(Services.class);

    private final ConcurrentMap<Class<?>, CrudService<?, ?>> instances = new ConcurrentHashMap<>();
    
    /**
     * Retrieves the service by entity class.
     * 
     * @param entityClass the entity class
     * @return the retrieved entity class
     */
    CrudService<?, ?> getByEntityClass(Class<?> entityClass) {
        LOGGER.trace("Requested CrudService for entity class " + entityClass.getName());
        return instances.get(entityClass);
    }

    @Autowired(required = false)
    private void setCrudServices(List<CrudService<?, ?>> services) {
        LOGGER.debug("Scanned classpath for service beans...");

        services.forEach((service) -> {
            LOGGER.debug("Registering service {} for {}...", service.getClass(), service.getEntityClass());
            this.instances.putIfAbsent(service.getEntityClass(), service);
        });
    }
    
}