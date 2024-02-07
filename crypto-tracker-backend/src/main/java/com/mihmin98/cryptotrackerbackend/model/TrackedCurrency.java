package com.mihmin98.cryptotrackerbackend.model;

import com.mihmin98.cryptotrackerbackend.enums.CurrencyEnum;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.util.Objects;

@Entity
@Table(name = "tracked_currencies", uniqueConstraints = {@UniqueConstraint(columnNames = {"currency", "portfolio"})})
@Getter
@Setter
public class TrackedCurrency {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private CurrencyEnum currency;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "portfolio_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Portfolio portfolio;

    public static TrackedCurrency of(CurrencyEnum currency) {
        TrackedCurrency tc = new TrackedCurrency();
        tc.setCurrency(currency);
        return tc;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TrackedCurrency that = (TrackedCurrency) o;
        return currency == that.currency;
    }

    @Override
    public int hashCode() {
        return Objects.hash(currency);
    }
}
