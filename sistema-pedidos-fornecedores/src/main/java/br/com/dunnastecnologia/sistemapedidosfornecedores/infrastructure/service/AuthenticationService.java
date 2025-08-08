package br.com.dunnastecnologia.sistemapedidosfornecedores.infrastructure.service;

import java.util.Optional;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import br.com.dunnastecnologia.sistemapedidosfornecedores.infrastructure.repository.ClienteRepository;
import br.com.dunnastecnologia.sistemapedidosfornecedores.infrastructure.repository.FornecedorRepository;

@Service
public class AuthenticationService implements UserDetailsService {

    private final ClienteRepository clienteRepository;
    private final FornecedorRepository fornecedorRepository;

    public AuthenticationService(ClienteRepository clienteRepository, FornecedorRepository fornecedorRepository) {
        this.clienteRepository = clienteRepository;
        this.fornecedorRepository = fornecedorRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // Tenta encontrar como Cliente primeiro
        Optional<UserDetails> user = clienteRepository.findByUsuarioAndAtivoTrue(username).map(u -> u);

        if (user.isPresent()) {
            return user.get();
        }

        // Se não encontrar, tenta como Fornecedor
        return fornecedorRepository.findByUsuarioAndAtivoTrue(username)
                .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado ou inativo: " + username));
    }
}
