package com.mihmin98.cryptotrackerbackend.model;

import com.mihmin98.cryptotrackerbackend.dto.PortfolioCreateDTO;
import com.mihmin98.cryptotrackerbackend.dto.PortfolioUpdateDTO;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Objects;

@Entity
@Table(name = "portfolios")
@Getter
@Setter
public class Portfolio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String name;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Portfolio portfolio = (Portfolio) o;
        return Objects.equals(id, portfolio.id) && Objects.equals(name, portfolio.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    public static Portfolio of(String name) {
        Portfolio p = new Portfolio();
        p.setName(name);
        return p;
    }

    public static Portfolio of(PortfolioCreateDTO dto) {
        Portfolio p = new Portfolio();

        p.setId(null);
        p.setName(dto.name());

        return p;
    }

    public void setValues(PortfolioUpdateDTO dto) {
        if (dto.name() != null) {
            setName(dto.name());
        }
    }
}
