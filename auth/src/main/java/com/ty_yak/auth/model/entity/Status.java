package com.ty_yak.auth.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import lombok.experimental.FieldDefaults;

import javax.persistence.*;

import static lombok.AccessLevel.PRIVATE;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "statuses")
@Accessors(chain = true)
@FieldDefaults(level = PRIVATE)
public class Status {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "statuses_id_seq")
    @SequenceGenerator(name = "statuses_id_seq",
            sequenceName = "statuses_id_seq", allocationSize = 1)
    Long id;
}
