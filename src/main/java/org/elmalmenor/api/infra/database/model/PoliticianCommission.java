package org.elmalmenor.api.infra.database.model;

import jakarta.persistence.*;
import lombok.Data;

import java.io.Serializable;

@Data
@Entity
@Table
public class PoliticianCommission implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    private Period period;

    @ManyToOne(cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    private Commission commission;

    @ManyToOne(cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    private CommissionPosition commissionPosition;

}
