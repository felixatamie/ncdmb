package com.ncdmb.canteen.configuration;

import com.ncdmb.canteen.entity.Cadre;
import com.ncdmb.canteen.entity.Permission;
import com.ncdmb.canteen.entity.Role;
import com.ncdmb.canteen.repository.CadreRepository;
import com.ncdmb.canteen.repository.PermissionRepository;
import com.ncdmb.canteen.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

@Component
@RequiredArgsConstructor

public class DataSeeder implements CommandLineRunner {

    private final RoleRepository roleRepository;
    private final PermissionRepository permissionRepository;
    private final CadreRepository cadreRepository;

    @Override
    public void run(String... args) {

        // Create permissions if they don't exist
        Permission manageUsers = createPermission("MANAGE_USERS", "Manage application users");
        Permission viewTransactions = createPermission("VIEW_TRANSACTIONS", "View transactions");
        Permission processOrders = createPermission("PROCESS_ORDERS", "Process customer orders");
        Permission manageMenu = createPermission("MANAGE_MENU", "Manage menu items");

        // Create roles and assign permissions
        Role caterer = createRole("CATERER", Set.of(processOrders, manageMenu),"The Caterer is responsible for managing the menu and food services in the canteen");
        Role cashier = createRole("CASHIER", Set.of(processOrders, viewTransactions),"The Cashier handles customer transactions in the canteen");

        // Admin should have all permissions
        Set<Permission> allPermissions = new HashSet<>();
        permissionRepository.findAll().forEach(allPermissions::add);

        Role admin = createRole("ADMIN", allPermissions,"The Admin has full access to the canteen management system");

        createCadre("MANAGEMENT","NCDMB MANAGEMENT STAFF",50,50);// management
        createCadre("NON_MANAGEMENT","NCDMB NON MANAGEMENT STAFF",40,60);// non management
        createCadre("nysc/it","nysc and or it staff",0,100);// nysc/it staff

    }

    private Permission createPermission(String name, String description) {
        return permissionRepository.findByName(name)
                .orElseGet(() -> permissionRepository.save(new Permission(null, name, description)));
    }

    private Role createRole(String name, Set<Permission> permissions,String des) {
        return roleRepository.findByName(name)
                .orElseGet(() -> roleRepository.save(new Role(null, name, des, permissions)));
    }

    private void createCadre(String name, String description, Integer staffPercent, Integer ncdmbPercent)
    {
        Cadre cadre = new Cadre();
        cadre.setName(name.toUpperCase());
        cadre.setDescription(description);
        cadre.setStaffPercent(BigDecimal.valueOf(staffPercent));
        cadre.setNcdmbPercent(BigDecimal.valueOf(ncdmbPercent));
        cadreRepository.findByName(name.toUpperCase()).orElseGet(()->cadreRepository.save(cadre));
    }
}
