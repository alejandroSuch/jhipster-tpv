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

    public HistoricRepositoryImpl(JpaEntityInformation<T, ?> entityInformation, EntityManager entityManager) {
        super(entityInformation, entityManager);
        this.entityManager = entityManager;
    }

    public HistoricRepositoryImpl(Class<T> domainClass, EntityManager em) {
        super(domainClass, em);
        this.entityManager = em;
    }


    @Override
    public <S extends T> S saveWithHistoric(S historic) {
        S clone = (S)historic.clone();

        if(entityManager.contains(historic)) {
            historic.setActive(false);
            super.save(historic);

            return this.save(clone);
        } else {
            return this.save((S)historic);
        }
    }
}
