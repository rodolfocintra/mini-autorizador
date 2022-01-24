package br.com.vr.miniautorizador.model;

import com.sun.istack.NotNull;
import lombok.*;
import org.springframework.data.annotation.Version;
import org.springframework.data.domain.Persistable;
import org.springframework.data.jpa.domain.AbstractPersistable;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;

import javax.validation.constraints.Size;

@Entity
@Table(name ="Account")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AccountEntity  implements Serializable {


    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @Column(name = "cartao", length = 16, nullable = false, unique = true)
    private String card;

    @Version
    @Column(name = "senha", length = 50, nullable = false)
    private String password;

    @Version
    @Column(name = "saldo")
    private BigDecimal balance;

}
