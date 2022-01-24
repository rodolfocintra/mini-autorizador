package br.com.vr.miniautorizador.repository;

import br.com.vr.miniautorizador.model.AccountEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.LockModeType;
import java.math.BigDecimal;
import java.util.Optional;

public interface AccountRepository extends JpaRepository<AccountEntity,String> {

      @Lock(value = LockModeType.PESSIMISTIC_WRITE)
      Optional<AccountEntity>  findAccountEntityByCard(String card);

      @Transactional
      @Modifying
      @Query("update AccountEntity  set balance = balance-:cardTransactionValue where id = :id")
      void update(@Param("id") Long id, @Param("cardTransactionValue") BigDecimal cardTransactionValue);
}
