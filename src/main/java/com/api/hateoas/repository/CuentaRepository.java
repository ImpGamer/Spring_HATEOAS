package com.api.hateoas.repository;

import com.api.hateoas.model.Cuenta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface CuentaRepository extends JpaRepository<Cuenta,Integer> {
    /*
    * La Query nos dice lo siguiente en SQL: Actualizaremos en la entidad "Cuenta" el monto o saldo, donde
    * capturaremos el saldo actual de la cuenta y lo sumaremos el saldo que se desea agregar, donde el ID
    * de la cuenta sea del segundo parametro
    */
    @Query("UPDATE Cuenta c SET c.monto=c.monto +?1 WHERE c.id=?2")
    @Modifying //Anotacion para actualizaciones
    void depositarSueldo(float saldo,Integer id);
}
