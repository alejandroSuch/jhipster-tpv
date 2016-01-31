package such.alex.tpv.config.custom.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;
import such.alex.tpv.domain.HistoricEntity;

import java.io.Serializable;

/**
 * Created by alejandro on 31/01/2016.
 */
@NoRepositoryBean
public interface HistoricRepository<T extends HistoricEntity, ID extends Serializable> extends JpaRepository<T, ID> {
    <S extends T> S saveWithHistoric(S historic);
}
