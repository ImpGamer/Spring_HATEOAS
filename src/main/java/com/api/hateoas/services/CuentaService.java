package com.api.hateoas.services;

import com.api.hateoas.exception.CuentaNotFoundException;
import com.api.hateoas.model.Cuenta;
import com.api.hateoas.repository.CuentaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class CuentaService {
    @Autowired
    private CuentaRepository cuentaRepository;
    public List<Cuenta> listarCuentas() {
        return cuentaRepository.findAll();
    }
    public Cuenta buscarCuenta_ID(Integer id) {
        return cuentaRepository.findById(id).orElse(null);
    }
    public Cuenta guardarCuenta(Cuenta cuenta) {
        return cuentaRepository.save(cuenta);
    }
    public void borrarCuenta(Integer id)throws CuentaNotFoundException {
        Cuenta cuentaBorrar = cuentaRepository.findById(id).orElse(null);
        if(cuentaBorrar == null) {
            throw new CuentaNotFoundException("La cuenta con el ID: "+id+" no se a encontrado");
        }
        cuentaRepository.delete(cuentaBorrar);
    }
    public Cuenta depositar(float monto,Integer id) {
        cuentaRepository.depositarSueldo(monto,id);
        return cuentaRepository.findById(id).orElse(null);
    }

    public Cuenta retirar(float monto,Integer id) {
        cuentaRepository.depositarSueldo(-monto,id);
        return cuentaRepository.findById(id).orElse(null);
    }

}
