package vn.anpha.storage.Option.Repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import vn.anpha.storage.Option.DTO.Projection.OptionDTO;
import vn.anpha.storage.Option.DTO.Request.OptionCreationRequest;
import vn.anpha.storage.Option.DTO.Request.OptionUpdateRequest;
import vn.anpha.storage.Option.Entity.Option;

public interface OptionRepository extends JpaRepository<Option, String> {
    @Modifying
    @Query(value = """
            INSERT INTO options (option_id, name, value, price, created_at, updated_at)
            VALUES (:#{#option.optionId}, :#{#option.name}, :#{#option.value}, :#{#option.price}, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP)
            """, nativeQuery = true)
    void insertOption(@Param("option") OptionCreationRequest option);

    @Modifying
    @Query(value = """
            UPDATE options
            SET
                name = COALESCE(:#{#option.name}, name),
                value = COALESCE(:#{#option.value}, value),
                price = COALESCE(:#{#option.price}, price),
                updated_at = CURRENT_TIMESTAMP
            WHERE option_id = :optionId
            """, nativeQuery = true)
    void updateOption(@Param("optionId") String optionId, @Param("option") OptionUpdateRequest option);

    @Query(value = """
            SELECT
                o.option_id as optionId,
                o.name as name,
                o.description as description,
                o.value as value,
                o.price as price
            FROM options o
            WHERE o.option_id = :optionId
            LIMIT 1
            """, nativeQuery = true)
    Optional<OptionDTO> findOptionById(@Param("optionId") String optionId);

    @Query(value = """
            SELECT
                o.option_id as optionId,
                o.name as name,
                o.description as description,
                o.value as value,
                o.price as price
            FROM options o
            """, nativeQuery = true)
    List<OptionDTO> findAllOptions();
}
