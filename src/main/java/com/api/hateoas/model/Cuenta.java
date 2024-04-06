package com.api.hateoas.model;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.hateoas.RepresentationModel;

@Entity
@Table(name = "CUENTAS")
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class Cuenta extends RepresentationModel<Cuenta> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(length = 20,nullable = false,unique = true)
    private String numeroCuenta;
    private float monto;
    public Cuenta(Integer id,String numeroCuenta) {
        this.id = id;
        this.numeroCuenta = numeroCuenta;
    }

    public void setId(Integer id) {
        this.id = id;
    }
    public void setNumeroCuenta(String numeroCuenta) {
        this.numeroCuenta = numeroCuenta;
    }

    public void setMonto(float monto) {
        this.monto = monto;
    }
    @Override
    public String toString() {
        return "Id: "+id+"\nN. Cuenta: "+numeroCuenta+"\nSaldo: "+monto+"\nlinks: "+getLinks();
    }
}
