package such.alex.tpv.config.custom.repository;

import org.springframework.data.jpa.provider.PersistenceProvider;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.data.jpa.repository.support.JpaEntityInformationSupport;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.util.Assert;
import such.alex.tpv.domain.HistoricEntity;

import javax.persistence.EntityManager;
import java.io.Serializable;

/**
 * Created by alejandro on 31/01/2016.
 */
@NoRepositoryBean
public class HistoricRepositoryImpl<T extends HistoricEntity, ID extends Serializable> extends SimpleJpaRepository<T, ID> implements HistoricRepository<T, ID> {
    EntityManager entityManager;
    JpaEntityInformation entityInformation;

    public HistoricRepositoryImpl(JpaEntityInformation<T, ?> entityInformation, EntityManager entityManager) {
        super(entityInformation, entityManager);
        this.entityManager = entityManager;
        this.entityInformation = entityInformation;
    }

    public HistoricRepositoryImpl(Class<T> domainClass, EntityManager em) {
        super(domainClass, em);
        this.entityManager = em;
        this.entityInformation = JpaEntityInformationSupport.getEntityInformation(domainClass, em);
    }


    @Override
    public <S extends T> S saveWithHistoric(S historic) {
        if (this.entityInformation.isNew(historic)) {
            return this.save(historic);
        } else {
            final S clone = (S) historic.clone();
            final HistoricEntity reference = this.entityManager.getReference(this.getDomainClass(), entityInformation.getId(historic));
            reference.setActive(false);
            super.save((S)reference);

            return this.save(clone);
        }
    }
}
