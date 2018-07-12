package com.chatbot.kahina.service.dto;

import java.time.LocalDate;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the Depence entity.
 */
public class DepenceDTO implements Serializable {

    private Long id;

    @NotNull
    private String name;

    @NotNull
    private Integer montant;

    @NotNull
    private LocalDate date;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getMontant() {
        return montant;
    }

    public void setMontant(Integer montant) {
        this.montant = montant;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        DepenceDTO depenceDTO = (DepenceDTO) o;
        if (depenceDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), depenceDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "DepenceDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", montant=" + getMontant() +
            ", date='" + getDate() + "'" +
            "}";
    }
}
