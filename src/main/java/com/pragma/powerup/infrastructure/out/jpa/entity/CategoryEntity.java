package com.pragma.powerup.infrastructure.out.jpa.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Setter
@Getter
@Table(name = "categoria")
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class CategoryEntity {
    // Cada categoria tiene 3 campos: id,nombre y descripción
    //-el nombre de la categoria no se puede repetir
    //-todas las categoria deben tener una descripcion
    //-el tamaño maximo del nombre debe ser de 50 caracteres
    //-el tamaño maximo de la descripcion debe ser de 90 caracteres

    @Id
    @GeneratedValue
    private Long id;
    @Column(unique = true, length = 50)
    private String nombre;
    @Column(length = 90)
    private String descripcion;

}