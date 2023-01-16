package mapoints.lib.repository;

import mapoints.lib.model.BaseModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@NoRepositoryBean
public interface BaseRepository <T extends BaseModel> extends JpaRepository<T, Long> {
    Sort DEFAULT_SORT = Sort.by(Sort.Direction.DESC, "timeCreatedInMilliSeconds");

    Optional<T> findTopByOrderByTimeCreatedInMilliSecondsDesc();

    Optional<T> findByEntityId(String entityId);

    Page<T> findAll(Pageable pageable);

    List<T> findByTimeCreatedBetween(Date startDate, Date endDate);

    static Pageable defaultPageable(Integer pageNum, Integer pageSize) {
        return PageRequest.of(
                pageNum,
                pageSize,
                DEFAULT_SORT
        );
    }
}
