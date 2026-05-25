package com.sps.controller;

import com.sps.dto.CompraRequest;
import com.sps.model.Cliente;
import com.sps.model.Compra;
import com.sps.service.ClienteService;
import com.sps.service.CompraService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/compras")
public class CompraController {

    @Autowired private CompraService  compraService;
    @Autowired private ClienteService clienteService;

    @PostMapping
    public ResponseEntity<?> crearCompra(@RequestBody CompraRequest req) {
        return clienteService.buscarPorCedula(req.getCedulaCliente())
                .map(cliente -> {
                    Compra compra = compraService.crearCompra(cliente, req.getPlanIds());
                    return ResponseEntity.ok(Map.of(
                            "mensaje", "Compra registrada. Le notificaremos por correo cuando pueda continuar.",
                            "codigoCompra", compra.getCodigo(),
                            "estadoCompra", compra.getEstadoCompra(),
                            "valorTotal", compra.getValorTotal()
                    ));
                })
                .orElse(ResponseEntity.badRequest().build());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Compra> obtenerPorId(@PathVariable Long id) {
        return compraService.obtenerPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/codigo/{codigo}")
    public ResponseEntity<Compra> obtenerPorCodigo(@PathVariable String codigo) {
        return compraService.obtenerPorCodigo(codigo)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/cliente/{cedula}")
    public ResponseEntity<List<Compra>> obtenerPorCliente(@PathVariable String cedula) {
        return clienteService.buscarPorCedula(cedula)
                .map(cliente -> ResponseEntity.ok(compraService.obtenerPorCliente(cliente.getId())))
                .orElse(ResponseEntity.notFound().build());
    }

    // El cliente confirma que leyó el aviso de pago → SPS marca como TERMINADA y envía a SHC/SAM
    @PostMapping("/{codigo}/confirmar")
    public ResponseEntity<?> confirmarRecepcion(@PathVariable String codigo) {
        boolean ok = compraService.confirmarRecepcion(codigo);
        if (ok) {
            return ResponseEntity.ok(Map.of(
                "mensaje", "Compra confirmada y marcada como TERMINADA.",
                "codigo", codigo
            ));
        }
        return ResponseEntity.badRequest().body(
            Map.of("mensaje", "La compra no existe o no está en estado PAGADO.")
        );
    }
}
