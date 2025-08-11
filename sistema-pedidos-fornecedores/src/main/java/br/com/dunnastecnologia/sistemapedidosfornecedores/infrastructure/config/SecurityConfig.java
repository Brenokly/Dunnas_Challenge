package br.com.dunnastecnologia.sistemapedidosfornecedores.infrastructure.config;

import java.util.List;

import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import br.com.dunnastecnologia.sistemapedidosfornecedores.infrastructure.security.JwtAuthenticationFilter;
import jakarta.servlet.DispatcherType;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public static PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationProvider authenticationProvider(UserDetailsService userDetailsService) {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public static CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of("http://localhost:8080", "http://localhost:3000"));
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(List.of("Authorization", "Content-Type"));
        configuration.setAllowCredentials(true);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web) -> web.ignoring().requestMatchers(PathRequest.toStaticResources().atCommonLocations());
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, JwtAuthenticationFilter jwtAuthFilter)
            throws Exception {
        http
                // 1. Configuração de Autorização (O mais importante)
                .authorizeHttpRequests(auth -> auth
                        // Regra 1: Libera todos os recursos estáticos. Esta é a forma moderna e
                        // recomendada.
                        .requestMatchers(PathRequest.toStaticResources().atCommonLocations()).permitAll()

                        // Regra 2: Libera os redirecionamentos internos do JSP para evitar o loop.
                        .dispatcherTypeMatchers(DispatcherType.FORWARD).permitAll()

                        // Regra 3: Libera as páginas web públicas.
                        .requestMatchers("/", "/login", "/perform_login", "/cadastro-cliente", "/cadastro-fornecedor")
                        .permitAll()

                        // Regra 4: Libera os endpoints públicos da API.
                        .requestMatchers("/api/v1/auth/**").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/v1/clientes", "/api/v1/fornecedores").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/v1/fornecedores/**", "/api/v1/produtos/**").permitAll()

                        // Regra 5: Qualquer outra requisição (seja web ou API) precisa de autenticação.
                        .anyRequest().authenticated())

                // 2. Gerenciamento de Sessão: A API não criará sessões.
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                // 3. Login via Formulário (para a parte Web/JSP)
                .formLogin(form -> form
                        .loginPage("/login")
                        .loginProcessingUrl("/perform_login")
                        .defaultSuccessUrl("/dashboard", true)
                        .failureUrl("/login?error=true"))

                // 4. Logout (para a parte Web/JSP)
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/login?logout=true"))

                // 5. Filtro JWT (para a parte API)
                // Ele é inteligente e só agirá se encontrar um header "Authorization: Bearer
                // ..."
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)

                // 6. Desabilita o CSRF. Em um projeto real, configuraríamos para ignorar apenas
                // as rotas da API.
                // Para o escopo do desafio, esta simplificação é aceitável.
                .csrf(AbstractHttpConfigurer::disable);

        return http.build();
    }
}