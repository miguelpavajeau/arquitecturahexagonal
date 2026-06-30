package com.pragma.powerup.infrastructure.input.rest;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Verifica que las reglas de autorización declaradas en la capa web se cumplen
 * de extremo a extremo (cadena de filtros de Spring Security incluida).
 */
@SpringBootTest
@AutoConfigureMockMvc
class UserRestControllerSecurityTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void getAllUsers_withoutAuthentication_returnsUnauthorized() throws Exception {
        mockMvc.perform(get("/api/v1/user"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(roles = "CLIENTE")
    void getAllUsers_asNonAdmin_returnsForbidden() throws Exception {
        mockMvc.perform(get("/api/v1/user"))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = "ADMINISTRADOR")
    void getAllUsers_asAdmin_returnsOk() throws Exception {
        mockMvc.perform(get("/api/v1/user"))
                .andExpect(status().isOk());
    }

    @Test
    void createClient_isPublic_andValidatesBody() throws Exception {
        // Endpoint público: no debe responder 401/403, sino 400 por validación del body vacío.
        mockMvc.perform(post("/api/v1/user/client")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(roles = "CLIENTE")
    void createUser_asNonAdmin_returnsForbidden() throws Exception {
        // Body válido para que la validación pase y se evalúe la autorización (@PreAuthorize).
        String validUser = """
                {
                  "nombre": "Juan",
                  "apellido": "Perez",
                  "documentoIdentidad": 123456789,
                  "celular": "+573001234567",
                  "correo": "juan@mail.com",
                  "clave": "Clave123*"
                }
                """;
        mockMvc.perform(post("/api/v1/user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(validUser))
                .andExpect(status().isForbidden());
    }
}
