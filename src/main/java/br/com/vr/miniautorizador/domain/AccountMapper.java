package br.com.vr.miniautorizador.domain;


import br.com.vr.miniautorizador.model.AccountEntity;
import org.mapstruct.Mapper;

@Mapper
public interface AccountMapper {


    AccountEntity toEntity(CardRequest card);

    CardResponse toDomin(AccountEntity accountEntity);
}
