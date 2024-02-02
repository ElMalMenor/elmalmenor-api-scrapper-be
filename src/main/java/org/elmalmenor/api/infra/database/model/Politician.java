package org.elmalmenor.api.infra.database.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.sql.Date;
import java.util.Set;

@Data
@Entity
@Table
@EqualsAndHashCode
public class Politician implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private Date birthDate;
    private String imagePath;

    @OneToMany(mappedBy = "politician", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Period> periods;

    @ManyToMany
    @JoinTable(name = "politician_project",
            joinColumns = { @JoinColumn(name = "politician_id")},
            inverseJoinColumns = { @JoinColumn(name = "project_id")}
    )
    private Set<Project> projects;

    @ManyToMany
    @JoinTable(name = "politician_profession",
            joinColumns = { @JoinColumn(name = "politician_id")},
            inverseJoinColumns = { @JoinColumn(name = "profession_id")}
    )
    private Set<Profession> professions;


}
