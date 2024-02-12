package org.elmalmenor.api.infra.database.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;
import java.util.Set;

@Data
@Entity
@Table
public class Commission implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String name;

    @ManyToOne
    private CommissionType commissionType;

    @OneToMany(mappedBy = "commission", cascade = {CascadeType.MERGE})
    @ToString.Exclude
    private Set<PoliticianCommission> politicianCommissions;
}
