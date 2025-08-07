package br.com.dunnastecnologia.sistemapedidosfornecedores.infrastructure.service;

import br.com.dunnastecnologia.sistemapedidosfornecedores.infrastructure.repository.ClienteRepository;
import br.com.dunnastecnologia.sistemapedidosfornecedores.infrastructure.repository.FornecedorRepository; 
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationService implements UserDetailsService {

    private final ClienteRepository clienteRepository;
    // private final FornecedorRepository fornecedorRepository; // Descomentar quando existir

    public AuthenticationService(ClienteRepository clienteRepository) { // Adicionar FornecedorRepository depois
        this.clienteRepository = clienteRepository;
        // this.fornecedorRepository = fornecedorRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // Tenta encontrar o usuário primeiro no repositório de clientes
        UserDetails user = clienteRepository.findByUsuario(username).orElse(null);

        // Se não encontrou, futuramente procurará no de fornecedores
        // if (user == null) {
        //     user = fornecedorRepository.findByUsuario(username).orElse(null);
        // }

        if (user != null) {
            return user;
        }

        throw new UsernameNotFoundException("Usuário não encontrado: " + username);
    }
}