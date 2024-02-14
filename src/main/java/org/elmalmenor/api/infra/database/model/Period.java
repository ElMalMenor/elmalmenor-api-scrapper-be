package org.elmalmenor.api.infra.database.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.io.Serializable;
import java.util.Date;
import java.util.Set;

@Data
@Entity
@Table
@EqualsAndHashCode(exclude = {"politician", "publicFunction", "bloc", "district", "politicianCommissions"})
public class Period implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private Date startDate;
    private Date endDate;
    private Boolean active;

    @ManyToOne
    @ToString.Exclude
    private Politician politician;

    @ManyToOne
    private PublicFunction publicFunction;

    @ManyToOne
    private Bloc bloc;

    @ManyToOne
    private District district;

    @OneToMany(mappedBy = "period", cascade = {CascadeType.PERSIST})
    @ToString.Exclude
    private Set<PoliticianCommission> politicianCommissions;


}
