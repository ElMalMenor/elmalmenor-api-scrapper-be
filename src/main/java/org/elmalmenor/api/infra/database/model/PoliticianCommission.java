package org.elmalmenor.api.infra.database.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

@Data
@Entity
@Table
@EqualsAndHashCode(exclude = {"period", "commission", "commissionPosition"})
public class PoliticianCommission implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(cascade = {CascadeType.MERGE})
    private Period period;

    @ManyToOne(cascade = {CascadeType.MERGE})
    private Commission commission;

    @ManyToOne(cascade = {CascadeType.MERGE})
    private CommissionPosition commissionPosition;

}
