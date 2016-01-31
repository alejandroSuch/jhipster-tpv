package such.alex.tpv.config.custom.factory;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.support.JpaRepositoryFactory;
import org.springframework.data.jpa.repository.support.JpaRepositoryFactoryBean;
import org.springframework.data.repository.core.RepositoryInformation;
import org.springframework.data.repository.core.RepositoryMetadata;
import org.springframework.data.repository.core.support.RepositoryFactorySupport;
import such.alex.tpv.domain.HistoricEntity;
import such.alex.tpv.config.custom.repository.HistoricRepositoryImpl;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.io.Serializable;

public class TpvRepositoryFactoryBean<R extends JpaRepository<T, I>, T,
    I extends Serializable> extends JpaRepositoryFactoryBean<R, T, I> {

    private EntityManager entityManager;

    @Override
    @PersistenceContext
    public void setEntityManager(EntityManager entityManager) {
        this.entityManager = entityManager;
        super.setEntityManager(entityManager);
    }

    protected RepositoryFactorySupport createRepositoryFactory(EntityManager em) {
        return new MyRepositoryFactory(em);
    }

    private static class MyRepositoryFactory<T, I extends Serializable>
        extends JpaRepositoryFactory {

        private final EntityManager em;

        public MyRepositoryFactory(EntityManager em) {

            super(em);
            this.em = em;
        }

        protected Object getTargetRepository(RepositoryInformation information) {
            if (isHistoricEntity(information.getDomainType())) {
                return new HistoricRepositoryImpl(information.getDomainType(), em);

            }
            return super.getTargetRepository(information);
        }

        protected Class<?> getRepositoryBaseClass(RepositoryMetadata metadata) {
            if (isHistoricEntity(metadata.getDomainType())) {
                return HistoricRepositoryImpl.class;
            }

            return super.getRepositoryBaseClass(metadata);
        }

        private boolean isHistoricEntity(Class<?> clazz) {
            if(clazz.getSuperclass() == null) {
                return false;
            }

            if(clazz.equals(HistoricEntity.class)) {
                return true;
            }

            return isHistoricEntity(clazz.getSuperclass());
        }
    }
}


