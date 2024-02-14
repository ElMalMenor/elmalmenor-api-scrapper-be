package org.elmalmenor.api.infra.database.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.Set;

@Data
@Entity
@Table
@EqualsAndHashCode(exclude = {"politicianCommissions"})
public class CommissionPosition {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String name;

    @OneToMany(mappedBy = "commissionPosition", cascade = {CascadeType.MERGE})
    @ToString.Exclude
    private Set<PoliticianCommission> politicianCommissions;
}
