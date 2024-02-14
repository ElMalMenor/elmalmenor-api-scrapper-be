package org.elmalmenor.api.infra.database.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.sql.Date;

@Data
@Entity
@Table
@EqualsAndHashCode(exclude = {"projectType"})
public class Project implements Serializable {

    @Id
    private String id;
    private String name;
    private Date date;

    @ManyToOne
    private ProjectType projectType;

}
