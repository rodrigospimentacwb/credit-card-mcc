package br.com.pepper.credit_card_mcc.model.mechant;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import br.com.pepper.credit_card_mcc.enums.MccType;

@Entity
@Table(name = "merchant")
public class Merchant {

    public Merchant() {
    }

    public Merchant(Long id, String name, MccType mcc) {
        this.id = id;
        this.name = name;
        this.mcc = mcc;
        this.createdOn = LocalDateTime.now();
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private Long id;

    @Column(name = "created_on", nullable = false)
    private LocalDateTime createdOn;

    @Column(nullable = false)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private MccType mcc;

    public Long getId() {
        return id;
    }

    public LocalDateTime getCreatedOn() {
        return createdOn;
    }

    public String getName() {
        return name;
    }

    public MccType getMcc() {
        return mcc;
    }
}
