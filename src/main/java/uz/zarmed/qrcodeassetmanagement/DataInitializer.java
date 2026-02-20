package uz.zarmed.qrcodeassetmanagement;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import uz.zarmed.qrcodeassetmanagement.entity.*;
import uz.zarmed.qrcodeassetmanagement.entity.enums.Condition;
import uz.zarmed.qrcodeassetmanagement.repository.*;

import java.time.LocalDate;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final DepartmentRepository departmentRepository;
    private final CategoryRepository categoryRepository;
    private final StatusRepository statusRepository;
    private final EmployeeRepository employeeRepository;
    private final AssetRepository assetRepository;
    private final QrCodeRepository qrCodeRepository;
    private final AuditLogRepository auditLogRepository;

    @Override
    public void run(String... args) {
        if (departmentRepository.count() > 0) {
            log.info("✅ Data already initialized. Skipping...");
            return;
        }

        log.info("🚀 Starting data initialization...");

        // 1. Departments
        Department itDept = departmentRepository.save(
                Department.builder()
                        .departmentName("Information Technology")
                        .departmentCode("IT-001")
                        .departmentDescription("Barcha IT infratuzilma va texnik qo'llab-quvvatlash")
                        .build()
        );

        Department hrDept = departmentRepository.save(
                Department.builder()
                        .departmentName("Human Resources")
                        .departmentCode("HR-001")
                        .departmentDescription("Xodimlarni boshqarish va rivojlantirish")
                        .build()
        );

        Department financeDept = departmentRepository.save(
                Department.builder()
                        .departmentName("Finance")
                        .departmentCode("FIN-001")
                        .departmentDescription("Moliya va buxgalteriya bo'limi")
                        .build()
        );

        Department medDept = departmentRepository.save(
                Department.builder()
                        .departmentName("Medical Department")
                        .departmentCode("MED-001")
                        .departmentDescription("Tibbiy xizmat va diagnostika bo'limi")
                        .build()
        );

        log.info("✅ Departments saved: {}", departmentRepository.count());

        // 2. Categories
        Category laptopCat = categoryRepository.save(
                Category.builder().name("Laptop").icon("Laptop").build()
        );
        Category monitorCat = categoryRepository.save(
                Category.builder().name("Monitor").icon("Monitor").build()
        );
        Category keyboardCat = categoryRepository.save(
                Category.builder().name("Keyboard").icon("Keyboard").build()
        );
        Category mouseCat = categoryRepository.save(
                Category.builder().name("Mouse").icon("Mouse").build()
        );
        Category printerCat = categoryRepository.save(
                Category.builder().name("Printer").icon("Printer").build()
        );
        Category phoneCat = categoryRepository.save(
                Category.builder().name("Phone").icon("Phone").build()
        );
        Category serverCat = categoryRepository.save(
                Category.builder().name("Server").icon("Server").build()
        );

        log.info("✅ Categories saved: {}", categoryRepository.count());

        // 3. Statuses
        Status available = statusRepository.save(Status.builder().status("AVAILABLE").build());
        Status inUse     = statusRepository.save(Status.builder().status("IN_USE").build());
        Status repair    = statusRepository.save(Status.builder().status("IN_REPAIR").build());
        Status retired   = statusRepository.save(Status.builder().status("RETIRED").build());
        Status lost      = statusRepository.save(Status.builder().status("LOST").build());

        log.info("✅ Statuses saved: {}", statusRepository.count());

        // 4. Employees
        Employee emp1 = employeeRepository.save(
                Employee.builder()
                        .fullName("Alisher Karimov")
                        .email("alisher.karimov@zarmed.uz")
                        .phoneNumber("+998901234567")
                        .position("Senior IT Engineer")
                        .department(itDept)
                        .build()
        );

        Employee emp2 = employeeRepository.save(
                Employee.builder()
                        .fullName("Malika Yusupova")
                        .email("malika.yusupova@zarmed.uz")
                        .phoneNumber("+998907654321")
                        .position("HR Manager")
                        .department(hrDept)
                        .build()
        );

        Employee emp3 = employeeRepository.save(
                Employee.builder()
                        .fullName("Bobur Toshmatov")
                        .email("bobur.toshmatov@zarmed.uz")
                        .phoneNumber("+998991112233")
                        .position("Financial Analyst")
                        .department(financeDept)
                        .build()
        );

        Employee emp4 = employeeRepository.save(
                Employee.builder()
                        .fullName("Dilorom Nazarova")
                        .email("dilorom.nazarova@zarmed.uz")
                        .phoneNumber("+998934445566")
                        .position("Chief Doctor")
                        .department(medDept)
                        .build()
        );

        Employee emp5 = employeeRepository.save(
                Employee.builder()
                        .fullName("Jasur Rakhimov")
                        .email("jasur.rakhimov@zarmed.uz")
                        .phoneNumber("+998997778899")
                        .position("System Administrator")
                        .department(itDept)
                        .build()
        );

        log.info("✅ Employees saved: {}", employeeRepository.count());

        // 5. Assets
        Asset asset1 = assetRepository.save(
                Asset.builder()
                        .assetName("Dell XPS 15 Laptop")
                        .category(laptopCat)
                        .serialNumber("DXP-2024-001")
                        .status(inUse)
                        .condition(Condition.GOOD)
                        .purchaseDate(LocalDate.of(2024, 1, 15))
                        .price(1500.00)
                        .warrantyEndDate(LocalDate.of(2027, 1, 15))
                        .description("IT bo'limi uchun asosiy ish laptop")
                        .building("A")
                        .floor(3)
                        .room("301")
                        .employee(emp1)
                        .scanCount(5)
                        .build()
        );

        Asset asset2 = assetRepository.save(
                Asset.builder()
                        .assetName("Samsung 27\" Monitor")
                        .category(monitorCat)
                        .serialNumber("SMS-MON-2024-001")
                        .status(inUse)
                        .condition(Condition.EXCELLENT)
                        .purchaseDate(LocalDate.of(2024, 2, 10))
                        .price(350.00)
                        .warrantyEndDate(LocalDate.of(2027, 2, 10))
                        .description("Full HD 27 dyumli monitor")
                        .building("A")
                        .floor(3)
                        .room("301")
                        .employee(emp1)
                        .scanCount(2)
                        .build()
        );

        Asset asset3 = assetRepository.save(
                Asset.builder()
                        .assetName("HP LaserJet Printer")
                        .category(printerCat)
                        .serialNumber("HPL-PRN-2023-005")
                        .status(repair)
                        .condition(Condition.FAIR)
                        .purchaseDate(LocalDate.of(2023, 6, 20))
                        .price(450.00)
                        .warrantyEndDate(LocalDate.of(2026, 6, 20))
                        .description("HR bo'limi printer - ta'mirda")
                        .building("B")
                        .floor(1)
                        .room("105")
                        .employee(null)
                        .scanCount(12)
                        .build()
        );

        Asset asset4 = assetRepository.save(
                Asset.builder()
                        .assetName("Logitech MX Keys Keyboard")
                        .category(keyboardCat)
                        .serialNumber("LOG-KEY-2024-010")
                        .status(inUse)
                        .condition(Condition.GOOD)
                        .purchaseDate(LocalDate.of(2024, 3, 5))
                        .price(120.00)
                        .warrantyEndDate(LocalDate.of(2026, 3, 5))
                        .description("Wireless mexanik klaviatura")
                        .building("A")
                        .floor(2)
                        .room("210")
                        .employee(emp2)
                        .scanCount(1)
                        .build()
        );

        Asset asset5 = assetRepository.save(
                Asset.builder()
                        .assetName("Dell PowerEdge Server")
                        .category(serverCat)
                        .serialNumber("DPE-SRV-2022-001")
                        .status(inUse)
                        .condition(Condition.GOOD)
                        .purchaseDate(LocalDate.of(2022, 11, 1))
                        .price(8500.00)
                        .warrantyEndDate(LocalDate.of(2027, 11, 1))
                        .description("Asosiy server - serverxonada")
                        .building("A")
                        .floor(0)
                        .room("SERVER-ROOM")
                        .employee(emp5)
                        .scanCount(0)
                        .build()
        );

        Asset asset6 = assetRepository.save(
                Asset.builder()
                        .assetName("iPhone 14 Pro")
                        .category(phoneCat)
                        .serialNumber("APL-IPH-2023-003")
                        .status(available)
                        .condition(Condition.EXCELLENT)
                        .purchaseDate(LocalDate.of(2023, 9, 15))
                        .price(1200.00)
                        .warrantyEndDate(LocalDate.of(2025, 9, 15))
                        .description("Korporativ telefon - bo'sh")
                        .building("B")
                        .floor(2)
                        .room("IT-STORAGE")
                        .employee(null)
                        .scanCount(3)
                        .build()
        );

        Asset asset7 = assetRepository.save(
                Asset.builder()
                        .assetName("Logitech MX Master Mouse")
                        .category(mouseCat)
                        .serialNumber("LOG-MOU-2024-007")
                        .status(retired)
                        .condition(Condition.POOR)
                        .purchaseDate(LocalDate.of(2021, 4, 10))
                        .price(80.00)
                        .warrantyEndDate(LocalDate.of(2023, 4, 10))
                        .description("Eskirgan sichqoncha - hisobdan chiqarilgan")
                        .building(null)
                        .floor(null)
                        .room(null)
                        .employee(null)
                        .scanCount(8)
                        .build()
        );

        log.info("✅ Assets saved: {}", assetRepository.count());

        // 6. QR Codes
        List<Object[]> qrData = List.of(
                new Object[]{asset1, "AST-2026-00001", "/qr-images/AST-2026-00001.png"},
                new Object[]{asset2, "AST-2026-00002", "/qr-images/AST-2026-00002.png"},
                new Object[]{asset3, "AST-2026-00003", "/qr-images/AST-2026-00003.png"},
                new Object[]{asset4, "AST-2026-00004", "/qr-images/AST-2026-00004.png"},
                new Object[]{asset5, "AST-2026-00005", "/qr-images/AST-2026-00005.png"},
                new Object[]{asset6, "AST-2026-00006", "/qr-images/AST-2026-00006.png"},
                new Object[]{asset7, "AST-2026-00007", "/qr-images/AST-2026-00007.png"}
        );

        for (Object[] row : qrData) {
            qrCodeRepository.save(
                    QrCode.builder()
                            .asset((Asset) row[0])
                            .qrCode((String) row[1])
                            .qrImageUrl((String) row[2])
                            .build()
            );
        }

        log.info("✅ QR Codes saved: {}", qrCodeRepository.count());

        // 7. Audit Logs
        auditLogRepository.save(
                AuditLog.builder()
                        .entityType("Asset")
                        .entityId(asset1.getId())
                        .action("CREATE")
                        .performedBy("admin@zarmed.uz")
                        .oldValue(null)
                        .newValue("{\"assetName\":\"Dell XPS 15 Laptop\",\"status\":\"IN_USE\"}")
                        .ipAddress("192.168.1.100")
                        .userAgent("Mozilla/5.0")
                        .build()
        );

        auditLogRepository.save(
                AuditLog.builder()
                        .entityType("Asset")
                        .entityId(asset3.getId())
                        .action("UPDATE")
                        .performedBy("alisher.karimov@zarmed.uz")
                        .oldValue("{\"status\":\"IN_USE\"}")
                        .newValue("{\"status\":\"IN_REPAIR\"}")
                        .ipAddress("192.168.1.101")
                        .userAgent("Mozilla/5.0")
                        .build()
        );

        auditLogRepository.save(
                AuditLog.builder()
                        .entityType("Asset")
                        .entityId(asset7.getId())
                        .action("UPDATE")
                        .performedBy("admin@zarmed.uz")
                        .oldValue("{\"status\":\"IN_USE\",\"condition\":\"FAIR\"}")
                        .newValue("{\"status\":\"RETIRED\",\"condition\":\"POOR\"}")
                        .ipAddress("192.168.1.100")
                        .userAgent("Chrome/120.0")
                        .build()
        );

        auditLogRepository.save(
                AuditLog.builder()
                        .entityType("Employee")
                        .entityId(emp1.getId())
                        .action("CREATE")
                        .performedBy("admin@zarmed.uz")
                        .oldValue(null)
                        .newValue("{\"fullName\":\"Alisher Karimov\",\"department\":\"IT-001\"}")
                        .ipAddress("192.168.1.100")
                        .userAgent("Chrome/120.0")
                        .build()
        );

        log.info("✅ Audit Logs saved: {}", auditLogRepository.count());
        log.info("🎉 Data initialization completed successfully!");
    }
}
