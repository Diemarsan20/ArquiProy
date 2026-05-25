package com.sps.service;

// Basado en ClienteService de 05_textos_h2
import com.sps.model.Cliente;
import com.sps.repository.ClienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ClienteService {

    @Autowired
    private ClienteRepository clienteRepository;

    public List<Cliente> obtenerTodos() {
        return clienteRepository.findAll();
    }

    public Optional<Cliente> obtenerPorId(Long id) {
        return clienteRepository.findById(id);
    }

    public Cliente guardar(Cliente cliente) {
        return clienteRepository.save(cliente);
    }

    public void eliminar(Long id) {
        clienteRepository.deleteById(id);
    }

    public Optional<Cliente> buscarPorCedula(String cedula) {
        return clienteRepository.findByCedula(cedula);
    }

    public Optional<Cliente> buscarPorCorreo(String correo) {
        return Optional.ofNullable(clienteRepository.findByCorreo(correo));
    }

    public Optional<Cliente> autenticar(String cedula, String password) {
        return clienteRepository.findByCedula(cedula)
                .filter(c -> c.getPassword().equals(password));
    }
}
