package com.pragma.powerup.infrastructure.configuration;

import com.pragma.powerup.domain.util.IUserPasswordEncrypt;
import com.pragma.powerup.infrastructure.out.jpa.entity.CategoryEntity;
import com.pragma.powerup.infrastructure.out.jpa.entity.DishEntity;
import com.pragma.powerup.infrastructure.out.jpa.entity.EmployeeRestaurantEntity;
import com.pragma.powerup.infrastructure.out.jpa.entity.RestaurantEntity;
import com.pragma.powerup.infrastructure.out.jpa.entity.RoleEntity;
import com.pragma.powerup.infrastructure.out.jpa.entity.UserEntity;
import com.pragma.powerup.infrastructure.out.jpa.repository.ICategoryRepository;
import com.pragma.powerup.infrastructure.out.jpa.repository.IDishRepository;
import com.pragma.powerup.infrastructure.out.jpa.repository.IEmployeeRestaurantRepository;
import com.pragma.powerup.infrastructure.out.jpa.repository.IRestaurantRepository;
import com.pragma.powerup.infrastructure.out.jpa.repository.IRoleRepository;
import com.pragma.powerup.infrastructure.out.jpa.repository.IUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DataSeeder implements ApplicationRunner {

    private static final String ADMIN_CORREO = "admin@pragma.com";
    private static final String ADMIN_CLAVE = "Admin123*";

    private static final String PROPIETARIO_CORREO = "propietario@pragma.com";
    private static final String PROPIETARIO_CLAVE = "Propietario123*";

    private static final String EMPLEADO_CORREO = "empleado@pragma.com";
    private static final String EMPLEADO_CLAVE = "Empleado123*";

    private static final String CLIENTE_CORREO = "cliente@pragma.com";
    private static final String CLIENTE_CLAVE = "Cliente123*";
    private static final String CLIENTE_CELULAR = "+573183152846";

    private final IRoleRepository roleRepository;
    private final IUserRepository userRepository;
    private final IRestaurantRepository restaurantRepository;
    private final IEmployeeRestaurantRepository employeeRestaurantRepository;
    private final ICategoryRepository categoryRepository;
    private final IDishRepository dishRepository;
    private final IUserPasswordEncrypt userPasswordEncrypt;

    @Override
    public void run(ApplicationArguments args) {
        RoleEntity administrador = seedRole("ADMINISTRADOR", "Administrador de la plataforma");
        RoleEntity propietario = seedRole("PROPIETARIO", "Propietario de un restaurante");
        RoleEntity empleado = seedRole("EMPLEADO", "Empleado de un restaurante");
        RoleEntity cliente = seedRole("CLIENTE", "Cliente de la plazoleta de comidas");

        seedUser(ADMIN_CORREO, "Admin", "Pragma", 0L, "+573000000000", ADMIN_CLAVE, administrador);
        UserEntity propietarioUser =
                seedUser(PROPIETARIO_CORREO, "Propietario", "Prueba", 900000001L,
                        "+573001112233", PROPIETARIO_CLAVE, propietario);
        UserEntity empleadoUser =
                seedUser(EMPLEADO_CORREO, "Empleado", "Prueba", 900000002L,
                        "+573004445566", EMPLEADO_CLAVE, empleado);
        // Cliente de prueba para validar la notificación SMS de "pedido listo" en local.
        seedUser(CLIENTE_CORREO, "Cliente", "Prueba", 1234567890L,
                CLIENTE_CELULAR, CLIENTE_CLAVE, cliente);

        RestaurantEntity restaurante = seedRestaurant(propietarioUser.getId());
        seedEmployeeRestaurant(empleadoUser.getId(), restaurante.getId());
        CategoryEntity categoria = seedCategory("Comida rapida", "Hamburguesas, perros y mas");
        seedDish("Hamburguesa clasica", 18000, "Carne de res, queso y vegetales frescos",
                restaurante.getId(), categoria);
    }

    private RoleEntity seedRole(String nombre, String descripcion) {
        RoleEntity existing = roleRepository.findByNombre(nombre);
        if (existing != null) {
            return existing;
        }
        RoleEntity roleEntity = new RoleEntity();
        roleEntity.setNombre(nombre);
        roleEntity.setDescripcion(descripcion);
        return roleRepository.save(roleEntity);
    }

    private UserEntity seedUser(String correo, String nombre, String apellido, Long documento,
                                String celular, String clave, RoleEntity role) {
        UserEntity existing = userRepository.findByCorreo(correo);
        if (existing != null) {
            return existing;
        }
        UserEntity user = new UserEntity();
        user.setNombre(nombre);
        user.setApellido(apellido);
        user.setDocumentoIdentidad(documento);
        user.setCelular(celular);
        user.setCorreo(correo);
        user.setClave(userPasswordEncrypt.encryptPassword(clave));
        user.setRole(role);
        return userRepository.save(user);
    }

    private RestaurantEntity seedRestaurant(Long idPropietario) {
        RestaurantEntity existing = restaurantRepository.findFirstByIdPropietario(idPropietario);
        if (existing != null) {
            return existing;
        }
        RestaurantEntity restaurante = new RestaurantEntity();
        restaurante.setNombre("Restaurante Prueba");
        restaurante.setNit(123456789L);
        restaurante.setDireccion("Calle 123 #45-67");
        restaurante.setTelefono("+573009998877");
        restaurante.setUrlLogo("https://via.placeholder.com/150");
        restaurante.setIdPropietario(idPropietario);
        return restaurantRepository.save(restaurante);
    }

    private void seedEmployeeRestaurant(Long idEmpleado, Long idRestaurante) {
        if (employeeRestaurantRepository.findByIdEmpleado(idEmpleado) != null) {
            return;
        }
        EmployeeRestaurantEntity vinculo = new EmployeeRestaurantEntity();
        vinculo.setIdEmpleado(idEmpleado);
        vinculo.setIdRestaurante(idRestaurante);
        employeeRestaurantRepository.save(vinculo);
    }

    private CategoryEntity seedCategory(String nombre, String descripcion) {
        return categoryRepository.findAll().stream()
                .filter(c -> nombre.equals(c.getNombre()))
                .findFirst()
                .orElseGet(() -> {
                    CategoryEntity categoria = new CategoryEntity();
                    categoria.setNombre(nombre);
                    categoria.setDescripcion(descripcion);
                    return categoryRepository.save(categoria);
                });
    }

    private void seedDish(String nombre, Integer precio, String descripcion,
                          Long idRestaurante, CategoryEntity categoria) {
        boolean existe = dishRepository.findAll().stream()
                .anyMatch(d -> nombre.equals(d.getNombre()) && idRestaurante.equals(d.getIdRestaurante()));
        if (existe) {
            return;
        }
        DishEntity plato = new DishEntity();
        plato.setNombre(nombre);
        plato.setPrecio(precio);
        plato.setDescripcion(descripcion);
        plato.setUrlImagen("https://via.placeholder.com/300");
        plato.setIdRestaurante(idRestaurante);
        plato.setCategoria(categoria);
        plato.setActivo(true);
        dishRepository.save(plato);
    }
}
