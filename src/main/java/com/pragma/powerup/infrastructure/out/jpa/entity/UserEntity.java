package com.pragma.powerup.infrastructure.out.jpa.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "usuario")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class UserEntity implements Serializable {
    private static final long serialVersionUID = 1L;
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(length = 50)
    private String nombre;

    @Column(length = 50)
    private String apellido;

    @Column(length = 15, name = "documento_identidad")
    private Long documentoIdentidad;

    @Column(length = 13)
    private String celular;

    @Column(length = 50)
    private String correo;

    @Column(length = 50)
    private String clave;


    @Column(length = 10, name = "id_rol")
    private Long idRol;

    @ManyToOne
    @JoinColumn(name = "id_rol", insertable = false, updatable = false)
    private RoleEntity role;
}