package br.com.vr.miniautorizador.service.impl;

import br.com.vr.miniautorizador.domain.AccountMapper;
import br.com.vr.miniautorizador.domain.CardRequest;
import br.com.vr.miniautorizador.domain.CardResponse;
import br.com.vr.miniautorizador.domain.CardTransationRequest;
import br.com.vr.miniautorizador.exception.ExistsException;
import br.com.vr.miniautorizador.exception.NotFoundException;
import br.com.vr.miniautorizador.exception.WithoutBalanceException;
import br.com.vr.miniautorizador.exception.WrongPasswordException;
import br.com.vr.miniautorizador.model.AccountEntity;
import br.com.vr.miniautorizador.repository.AccountRepository;
import br.com.vr.miniautorizador.service.AutorizatorService;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Optional;

@Service
@Transactional
public class AutorizatorServiceImpl implements AutorizatorService {

    private static final BigDecimal BALANCE_DEFAULT = new BigDecimal("500");

   private final AccountRepository repository;
   private final AccountMapper mapper;

    public AutorizatorServiceImpl(AccountRepository repository, AccountMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    @Transactional
    public CardResponse createCard(CardRequest card) {

        try {
            AccountEntity accountEntity = mapper.toEntity(card);
            accountEntity.setBalance(BALANCE_DEFAULT);
            return mapper.toDomin(repository.save(accountEntity));
        } catch (DataIntegrityViolationException e) {
            throw new ExistsException(e.getMessage());
        } catch (Exception e) {
            throw e;
        }
    }

    @Override
    @Transactional
    public BigDecimal getBalance(String card) {
        return repository.findAccountEntityByCard(card).orElseThrow(() -> new NotFoundException()).getBalance();
    }

    @Override
    @Transactional
    public void cardTransaction(CardTransationRequest cardTransationRequest) {

        Optional<AccountEntity> accountEntityOptional = repository.findAccountEntityByCard(cardTransationRequest.getCard());
        AccountEntity accountEntity = transactionIsValid(accountEntityOptional,cardTransationRequest);
        repository.update(accountEntity.getId(),cardTransationRequest.getCardTransactionValue());

    }

    private AccountEntity transactionIsValid(Optional<AccountEntity> accountEntity, CardTransationRequest transaction ){

        accountEntity.orElseThrow(() -> new NotFoundException());
        accountEntity.filter( a -> a.getPassword().equals(transaction.getPassword())).orElseThrow(() -> new WrongPasswordException());
        accountEntity.filter(a->a.getBalance().subtract(transaction.getCardTransactionValue()).compareTo(BigDecimal.ZERO)>=0).orElseThrow(() -> new WithoutBalanceException());;

        return accountEntity.get();
    }
}
