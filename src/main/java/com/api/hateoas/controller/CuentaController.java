package com.api.hateoas.controller;

import com.api.hateoas.exception.CuentaNotFoundException;
import com.api.hateoas.model.Cuenta;
import com.api.hateoas.model.Monto;
import com.api.hateoas.services.CuentaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import java.util.List;

@RestController
@RequestMapping("/api/cuentas")
public class CuentaController {
    @Autowired
    private CuentaService cuentaService;
    @GetMapping
    private ResponseEntity<List<Cuenta>> listarCuentas() {
        List<Cuenta> listaCuentas = cuentaService.listarCuentas();

        if(listaCuentas.isEmpty()) {
            //En caso que la lista este vacia, no me enviara ningun dato en la respuesta
            return ResponseEntity.noContent().build();
        }
        for(Cuenta cuenta: listaCuentas) {
            cuenta.add(linkTo(methodOn(CuentaController.class).mostrarCuenta_ID(cuenta.getId())).withSelfRel());
            cuenta.add(linkTo(methodOn(CuentaController.class).depositarDinero(cuenta.getId(), null)).withRel("depositos"));
        }

        //Devuelve el objeto "Cuenta" en JSON con un estado 200 HTTP
        return new ResponseEntity<>(listaCuentas, HttpStatus.OK);
    }
    @GetMapping("/{id}")
    public ResponseEntity<Cuenta> mostrarCuenta_ID(@PathVariable Integer id) {
        try {
            Cuenta cuenta = cuentaService.buscarCuenta_ID(id);
            //Funcion propia de la clase "RepresentationModel" para estas funciones de HATEOAS
            /*
            * A la cuenta recuperada por el ID, le agregaremos links de respuesta, el cual sera un metodo
            * o funcion del controlador (clase controller)
             */
            cuenta.add(linkTo(methodOn(CuentaController.class).depositarDinero(cuenta.getId(), null)).withRel("depositos"));
            cuenta.add(linkTo(methodOn(CuentaController.class)
                    .mostrarCuenta_ID(cuenta.getId())).withSelfRel());
            return new ResponseEntity<>(cuenta,HttpStatus.OK);
        }catch (Exception e) {
            return ResponseEntity.noContent().build();
        }
    }
    @PostMapping("/agregar")
    //Anotacion RequesBody, para que el JSON lo convierta a un objeto que java comprenda y almacen
    ResponseEntity<Cuenta> crearCuenta(@RequestBody Cuenta cuenta) {
        try {
            Cuenta cuentaCreada = cuentaService.guardarCuenta(cuenta);
            cuentaCreada.add(linkTo(methodOn(CuentaController.class).mostrarCuenta_ID(cuentaCreada.getId())).withSelfRel());
            cuentaCreada.add(linkTo(methodOn(CuentaController.class).depositarDinero(cuentaCreada.getId(), null)).withRel("depositos"));

            return ResponseEntity.created(linkTo(methodOn(CuentaController.class)).toUri()).body(cuentaCreada);
        }catch (Exception e) {
            return ResponseEntity.noContent().build();
        }
    }
    @PutMapping("/editar")
        //Anotacion RequesBody, para que el JSON lo convierta a un objeto que java comprenda y almacene
    ResponseEntity<Cuenta> editarCuenta(@RequestBody Cuenta cuenta) {
        try {
            Cuenta cuentaAct = cuentaService.guardarCuenta(cuenta);
            cuentaAct.add(linkTo(methodOn(CuentaController.class).mostrarCuenta_ID(cuentaAct.getId())).withSelfRel());

            return new ResponseEntity<>(cuentaAct,HttpStatus.OK);
        }catch (Exception e) {
            return ResponseEntity.noContent().build();
        }
    }
    //Cuando solamente se desea actualizar un dato de un objeto es recomendable usar @PatchMapping a @PutMapping
    @PatchMapping("/deposito/{id}")
    public ResponseEntity<Cuenta> depositarDinero(@PathVariable Integer id,@RequestBody Monto monto) {
        Cuenta cuentaBBDD = cuentaService.depositar(monto.getMonto(),id);

        //Agregamos links al objeto para decirle a que links tiene acceso, o cuales les pertenece
        cuentaBBDD.add(linkTo(methodOn(CuentaController.class).mostrarCuenta_ID(cuentaBBDD.getId())).withSelfRel());
        cuentaBBDD.add(linkTo(methodOn(CuentaController.class).depositarDinero(cuentaBBDD.getId(), null)).withRel("depositos"));
        return new ResponseEntity<>(cuentaBBDD,HttpStatus.OK);
    }
    @PatchMapping("/retiro/{id}")
    public ResponseEntity<Cuenta> retirarDinero(@PathVariable Integer id,@RequestBody Monto monto) {
        Cuenta cuentaBBDD = cuentaService.retirar(monto.getMonto(),id);

        //Agregamos links al objeto para decirle a que links tiene acceso, o cuales les pertenece
        cuentaBBDD.add(linkTo(methodOn(CuentaController.class).mostrarCuenta_ID(cuentaBBDD.getId())).withSelfRel());
        cuentaBBDD.add(linkTo(methodOn(CuentaController.class).retirarDinero(cuentaBBDD.getId(), null)).withRel("retiros"));
        return new ResponseEntity<>(cuentaBBDD,HttpStatus.OK);
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminarCuenta(@PathVariable Integer id) {
        try {
            cuentaService.borrarCuenta(id);
        }catch (CuentaNotFoundException e) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.noContent().build();
    }

}