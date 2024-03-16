package com.testApp.testApp.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Home {

    @Id
    @SequenceGenerator(
            name = "home_id_seq",
            sequenceName = "home_id_seq",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "home_id_seq"
    )
    private Long id;

    @Column
    private String address;


    @ManyToOne
    @JoinColumn(name = "host_id", referencedColumnName = "id")
    private User host;

    @ManyToMany
    @JoinTable(
            name = "Home_Usr",
            joinColumns = @JoinColumn(name = "home_id"),
            inverseJoinColumns = @JoinColumn(name = "usr_id")
    )
    private Set<User> users = new HashSet<>();



}
