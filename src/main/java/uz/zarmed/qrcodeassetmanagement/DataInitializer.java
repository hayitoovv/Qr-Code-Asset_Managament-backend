package uz.zarmed.qrcodeassetmanagement;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import uz.zarmed.qrcodeassetmanagement.entity.*;
import uz.zarmed.qrcodeassetmanagement.entity.enums.Condition;
import uz.zarmed.qrcodeassetmanagement.repository.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final CategoryRepository categoryRepository;
    private final StatusRepository statusRepository;
    private final DepartmentRepository departmentRepository;
    private final EmployeeRepository employeeRepository;
    private final AssetRepository assetRepository;
    private final QrCodeRepository qrCodeRepository;

    @Override
    public void run(String... args) {

        if (categoryRepository.count() > 0) {
            System.out.println("✅ Data allaqachon mavjud, qayta yuklanmaydi.");
            return;
        }

        // ============================================
        // 1. CATEGORIES
        // ============================================
        Category laptop = categoryRepository.save(
                Category.builder().name("Laptop").icon("Laptop").build());

        Category monitor = categoryRepository.save(
                Category.builder().name("Monitor").icon("Monitor").build());

        Category keyboard = categoryRepository.save(
                Category.builder().name("Keyboard").icon("Keyboard").build());

        Category printer = categoryRepository.save(
                Category.builder().name("Printer").icon("Printer").build());

        Category phone = categoryRepository.save(
                Category.builder().name("Phone").icon("Smartphone").build());

        Category tablet = categoryRepository.save(
                Category.builder().name("Tablet").icon("Tablet").build());

        Category server = categoryRepository.save(
                Category.builder().name("Server").icon("Server").build());

        Category other = categoryRepository.save(
                Category.builder().name("Other").icon("Package").build());

        // ============================================
        // 2. STATUSES
        // ============================================
        Status available = statusRepository.save(
                Status.builder().status("Available").build());

        Status assigned = statusRepository.save(
                Status.builder().status("Assigned").build());

        Status repair = statusRepository.save(
                Status.builder().status("In Repair").build());

        Status lost = statusRepository.save(
                Status.builder().status("Lost").build());

        Status retired = statusRepository.save(
                Status.builder().status("Retired").build());

        // ============================================
        // 3. DEPARTMENTS
        // ============================================
        Department engineering = departmentRepository.save(Department.builder()
                .departmentName("Engineering")
                .departmentCode("ENG-001")
                .departmentDescription("Software development, DevOps, and infrastructure")
                .build());

        Department design = departmentRepository.save(Department.builder()
                .departmentName("Design")
                .departmentCode("DSN-001")
                .departmentDescription("UI/UX design, product design, and brand identity")
                .build());

        Department marketing = departmentRepository.save(Department.builder()
                .departmentName("Marketing")
                .departmentCode("MKT-001")
                .departmentDescription("Digital marketing, content creation, and brand strategy")
                .build());

        Department it = departmentRepository.save(Department.builder()
                .departmentName("IT")
                .departmentCode("IT-001")
                .departmentDescription("IT infrastructure, system administration, and tech support")
                .build());

        Department hr = departmentRepository.save(Department.builder()
                .departmentName("HR")
                .departmentCode("HR-001")
                .departmentDescription("Human resources, recruitment, and employee engagement")
                .build());

        Department finance = departmentRepository.save(Department.builder()
                .departmentName("Finance")
                .departmentCode("FIN-001")
                .departmentDescription("Financial planning, accounting, and budget management")
                .build());

        Department operations = departmentRepository.save(Department.builder()
                .departmentName("Operations")
                .departmentCode("OPS-001")
                .departmentDescription("Business operations, logistics, and supply chain management")
                .build());

        Department sales = departmentRepository.save(Department.builder()
                .departmentName("Sales")
                .departmentCode("SLS-001")
                .departmentDescription("Sales strategy, client relations, and revenue generation")
                .build());

        // ============================================
        // 4. EMPLOYEES
        // ============================================
        Employee alice = employeeRepository.save(Employee.builder()
                .fullName("Alice Chen")
                .email("alice@company.com")
                .phoneNumber("+1 555-0101")
                .position("Senior Developer")
                .department(engineering)
                .build());

        Employee bob = employeeRepository.save(Employee.builder()
                .fullName("Bob Martinez")
                .email("bob@company.com")
                .phoneNumber("+1 555-0102")
                .position("UI/UX Lead")
                .department(design)
                .build());

        Employee carol = employeeRepository.save(Employee.builder()
                .fullName("Carol White")
                .email("carol@company.com")
                .phoneNumber("+1 555-0103")
                .position("Marketing Manager")
                .department(marketing)
                .build());

        Employee david = employeeRepository.save(Employee.builder()
                .fullName("David Kim")
                .email("david@company.com")
                .phoneNumber("+1 555-0104")
                .position("System Admin")
                .department(it)
                .build());

        Employee eva = employeeRepository.save(Employee.builder()
                .fullName("Eva Johnson")
                .email("eva@company.com")
                .phoneNumber("+1 555-0105")
                .position("HR Specialist")
                .department(hr)
                .build());

        Employee frank = employeeRepository.save(Employee.builder()
                .fullName("Frank Lee")
                .email("frank@company.com")
                .phoneNumber("+1 555-0106")
                .position("Backend Developer")
                .department(engineering)
                .build());

        Employee grace = employeeRepository.save(Employee.builder()
                .fullName("Grace Park")
                .email("grace@company.com")
                .phoneNumber("+1 555-0107")
                .position("Accountant")
                .department(finance)
                .build());

        Employee henry = employeeRepository.save(Employee.builder()
                .fullName("Henry Wang")
                .email("henry@company.com")
                .phoneNumber("+1 555-0108")
                .position("DevOps Engineer")
                .department(engineering)
                .build());

        // ============================================
        // 5. ASSETS
        // ============================================
        List<Asset> assets = assetRepository.saveAll(List.of(

            // --- LAPTOPS ---
            Asset.builder()
                .assetName("MacBook Pro 16\"")
                .category(laptop)
                .serialNumber("MBP-A2485-001")
                .status(assigned)
                .condition(Condition.EXCELLENT)
                .purchaseDate(LocalDate.of(2025, 6, 15))
                .price(2499.0)
                .warrantyEndDate(LocalDate.of(2026, 6, 15))
                .description("16-inch MacBook Pro with M3 Max chip, 36GB RAM, 1TB SSD")
                .building("HQ").floor(3).room("301")
                .scanCount(12)
                .lastScanned(LocalDateTime.of(2026, 2, 12, 10, 30))
                .employee(alice)
                .build(),

            Asset.builder()
                .assetName("Dell XPS 15")
                .category(laptop)
                .serialNumber("DELL-XPS-002")
                .status(assigned)
                .condition(Condition.GOOD)
                .purchaseDate(LocalDate.of(2025, 7, 1))
                .price(1899.0)
                .warrantyEndDate(LocalDate.of(2026, 7, 1))
                .description("Dell XPS 15 with Intel i9, 32GB RAM, 512GB SSD")
                .building("HQ").floor(2).room("205")
                .scanCount(8)
                .lastScanned(LocalDateTime.of(2026, 2, 11, 14, 20))
                .employee(bob)
                .build(),

            Asset.builder()
                .assetName("ThinkPad X1 Carbon")
                .category(laptop)
                .serialNumber("LEN-X1C-003")
                .status(repair)
                .condition(Condition.FAIR)
                .purchaseDate(LocalDate.of(2025, 3, 20))
                .price(1649.0)
                .warrantyEndDate(LocalDate.of(2026, 3, 20))
                .description("Lenovo ThinkPad X1 Carbon Gen 11, 16GB RAM, 512GB SSD")
                .building("HQ").floor(1).room("102")
                .scanCount(15)
                .lastScanned(LocalDateTime.of(2026, 2, 9, 9, 45))
                .employee(carol)
                .build(),

            Asset.builder()
                .assetName("HP EliteBook 850")
                .category(laptop)
                .serialNumber("HP-EB850-004")
                .status(assigned)
                .condition(Condition.GOOD)
                .purchaseDate(LocalDate.of(2025, 8, 10))
                .price(1399.0)
                .warrantyEndDate(LocalDate.of(2026, 8, 10))
                .description("HP EliteBook 850 G10, 16GB RAM, 256GB SSD")
                .building("HQ").floor(-1).room("Server Room")
                .scanCount(22)
                .lastScanned(LocalDateTime.of(2026, 2, 13, 8, 0))
                .employee(david)
                .build(),

            Asset.builder()
                .assetName("MacBook Air M3")
                .category(laptop)
                .serialNumber("MBA-M3-005")
                .status(available)
                .condition(Condition.NEW)
                .purchaseDate(LocalDate.of(2025, 9, 1))
                .price(1299.0)
                .warrantyEndDate(LocalDate.of(2026, 9, 1))
                .description("MacBook Air 15\" M3, 16GB RAM, 512GB SSD")
                .building("HQ").floor(1).room("Storage")
                .scanCount(3)
                .lastScanned(LocalDateTime.of(2026, 2, 8, 16, 30))
                .employee(null)
                .build(),

            Asset.builder()
                .assetName("Surface Laptop 5")
                .category(laptop)
                .serialNumber("MS-SL5-006")
                .status(assigned)
                .condition(Condition.GOOD)
                .purchaseDate(LocalDate.of(2025, 5, 15))
                .price(1499.0)
                .warrantyEndDate(LocalDate.of(2026, 5, 15))
                .description("Microsoft Surface Laptop 5, 16GB RAM, 512GB SSD")
                .building("HQ").floor(2).room("210")
                .scanCount(6)
                .lastScanned(LocalDateTime.of(2026, 2, 10, 12, 0))
                .employee(eva)
                .build(),

            Asset.builder()
                .assetName("MacBook Pro 14\"")
                .category(laptop)
                .serialNumber("MBP-14-007")
                .status(lost)
                .condition(Condition.GOOD)
                .purchaseDate(LocalDate.of(2025, 2, 10))
                .price(1999.0)
                .warrantyEndDate(LocalDate.of(2026, 2, 10))
                .description("14-inch MacBook Pro M3 Pro, 18GB RAM, 512GB SSD")
                .building("Unknown").floor(0).room("-")
                .scanCount(5)
                .lastScanned(LocalDateTime.of(2026, 1, 15, 9, 0))
                .employee(grace)
                .build(),

            Asset.builder()
                .assetName("HP ProBook 450")
                .category(laptop)
                .serialNumber("HP-PB450-008")
                .status(retired)
                .condition(Condition.POOR)
                .purchaseDate(LocalDate.of(2023, 1, 15))
                .price(899.0)
                .warrantyEndDate(LocalDate.of(2024, 1, 15))
                .description("HP ProBook 450 G9, Intel i5, 8GB RAM, 256GB SSD")
                .building("HQ").floor(-1).room("Storage")
                .scanCount(30)
                .lastScanned(LocalDateTime.of(2025, 12, 1, 10, 0))
                .employee(null)
                .build(),

            // --- MONITORS ---
            Asset.builder()
                .assetName("Dell UltraSharp 27\"")
                .category(monitor)
                .serialNumber("DELL-U2723-011")
                .status(assigned)
                .condition(Condition.EXCELLENT)
                .purchaseDate(LocalDate.of(2025, 6, 15))
                .price(619.0)
                .warrantyEndDate(LocalDate.of(2028, 6, 15))
                .description("Dell UltraSharp 27\" 4K USB-C Hub Monitor")
                .building("HQ").floor(3).room("301")
                .scanCount(4)
                .lastScanned(LocalDateTime.of(2026, 2, 12, 10, 32))
                .employee(alice)
                .build(),

            Asset.builder()
                .assetName("LG 32UN880")
                .category(monitor)
                .serialNumber("LG-32UN-012")
                .status(assigned)
                .condition(Condition.GOOD)
                .purchaseDate(LocalDate.of(2025, 7, 1))
                .price(699.0)
                .warrantyEndDate(LocalDate.of(2028, 7, 1))
                .description("LG 32\" UltraFine Ergo 4K Monitor with Ergo Stand")
                .building("HQ").floor(2).room("205")
                .scanCount(3)
                .lastScanned(LocalDateTime.of(2026, 2, 11, 14, 22))
                .employee(bob)
                .build(),

            Asset.builder()
                .assetName("Samsung 34\" Curved")
                .category(monitor)
                .serialNumber("SAM-34C-013")
                .status(available)
                .condition(Condition.NEW)
                .purchaseDate(LocalDate.of(2025, 9, 10))
                .price(549.0)
                .warrantyEndDate(LocalDate.of(2028, 9, 10))
                .description("Samsung 34\" Ultra-Wide Curved Monitor")
                .building("HQ").floor(1).room("Storage")
                .scanCount(2)
                .lastScanned(LocalDateTime.of(2026, 2, 5, 16, 0))
                .employee(null)
                .build(),

            // --- KEYBOARDS ---
            Asset.builder()
                .assetName("Logitech MX Keys")
                .category(keyboard)
                .serialNumber("LOG-MXK-016")
                .status(assigned)
                .condition(Condition.GOOD)
                .purchaseDate(LocalDate.of(2025, 6, 15))
                .price(99.0)
                .warrantyEndDate(LocalDate.of(2026, 6, 15))
                .description("Logitech MX Keys Advanced Wireless Keyboard")
                .building("HQ").floor(3).room("301")
                .scanCount(2)
                .lastScanned(LocalDateTime.of(2026, 2, 12, 10, 35))
                .employee(alice)
                .build(),

            Asset.builder()
                .assetName("Apple Magic Keyboard")
                .category(keyboard)
                .serialNumber("APL-MK-017")
                .status(assigned)
                .condition(Condition.GOOD)
                .purchaseDate(LocalDate.of(2025, 7, 1))
                .price(129.0)
                .warrantyEndDate(LocalDate.of(2026, 7, 1))
                .description("Apple Magic Keyboard with Touch ID and Numeric Keypad")
                .building("HQ").floor(2).room("205")
                .scanCount(2)
                .lastScanned(LocalDateTime.of(2026, 2, 11, 14, 25))
                .employee(bob)
                .build(),

            Asset.builder()
                .assetName("Keychron K3 Pro")
                .category(keyboard)
                .serialNumber("KEY-K3P-018")
                .status(available)
                .condition(Condition.NEW)
                .purchaseDate(LocalDate.of(2025, 11, 1))
                .price(89.0)
                .warrantyEndDate(LocalDate.of(2026, 11, 1))
                .description("Keychron K3 Pro Low-Profile Mechanical Keyboard")
                .building("HQ").floor(1).room("Storage")
                .scanCount(1)
                .lastScanned(LocalDateTime.of(2026, 1, 15, 14, 0))
                .employee(null)
                .build(),

            // --- PRINTERS ---
            Asset.builder()
                .assetName("HP LaserJet Pro")
                .category(printer)
                .serialNumber("HP-LJP-019")
                .status(assigned)
                .condition(Condition.GOOD)
                .purchaseDate(LocalDate.of(2025, 4, 1))
                .price(399.0)
                .warrantyEndDate(LocalDate.of(2026, 4, 1))
                .description("HP LaserJet Pro MFP M428fdn printer/scanner")
                .building("HQ").floor(2).room("Print Room")
                .scanCount(18)
                .lastScanned(LocalDateTime.of(2026, 2, 13, 8, 5))
                .employee(david)
                .build(),

            Asset.builder()
                .assetName("Brother MFC-L8900CDW")
                .category(printer)
                .serialNumber("BRO-MFC-020")
                .status(repair)
                .condition(Condition.FAIR)
                .purchaseDate(LocalDate.of(2025, 1, 15))
                .price(549.0)
                .warrantyEndDate(LocalDate.of(2026, 1, 15))
                .description("Brother Color Laser All-in-One Printer")
                .building("HQ").floor(1).room("Print Room")
                .scanCount(25)
                .lastScanned(LocalDateTime.of(2026, 2, 8, 14, 5))
                .employee(david)
                .build(),

            // --- SERVER ---
            Asset.builder()
                .assetName("Dell PowerEdge R740")
                .category(server)
                .serialNumber("DELL-PE-R740-021")
                .status(assigned)
                .condition(Condition.EXCELLENT)
                .purchaseDate(LocalDate.of(2024, 6, 1))
                .price(8999.0)
                .warrantyEndDate(LocalDate.of(2027, 6, 1))
                .description("Dell PowerEdge R740 2U Rack Server, 2x Xeon, 256GB RAM")
                .building("HQ").floor(-1).room("Server Room")
                .scanCount(50)
                .lastScanned(LocalDateTime.of(2026, 2, 13, 7, 0))
                .employee(henry)
                .build(),

            // --- PHONE ---
            Asset.builder()
                .assetName("iPhone 15 Pro")
                .category(phone)
                .serialNumber("APL-IP15P-022")
                .status(assigned)
                .condition(Condition.EXCELLENT)
                .purchaseDate(LocalDate.of(2025, 10, 1))
                .price(999.0)
                .warrantyEndDate(LocalDate.of(2026, 10, 1))
                .description("Apple iPhone 15 Pro, 256GB, Space Black")
                .building("HQ").floor(3).room("305")
                .scanCount(7)
                .lastScanned(LocalDateTime.of(2026, 2, 12, 15, 0))
                .employee(frank)
                .build(),

            // --- TABLET ---
            Asset.builder()
                .assetName("iPad Pro 12.9\"")
                .category(tablet)
                .serialNumber("APL-IPAD-023")
                .status(available)
                .condition(Condition.NEW)
                .purchaseDate(LocalDate.of(2025, 11, 15))
                .price(1099.0)
                .warrantyEndDate(LocalDate.of(2026, 11, 15))
                .description("Apple iPad Pro 12.9\" M2, 256GB, WiFi + Cellular")
                .building("HQ").floor(1).room("Storage")
                .scanCount(1)
                .lastScanned(LocalDateTime.of(2026, 1, 20, 10, 0))
                .employee(null)
                .build()
        ));

        // ============================================
        // 6. QR CODES
        // ============================================
        for (int i = 0; i < assets.size(); i++) {
            String qrValue = String.format("AST-2026-%05d", i + 1);
            qrCodeRepository.save(QrCode.builder()
                    .qrCode(qrValue)
                    .qrImageUrl("/qr-images/" + qrValue + ".png")
                    .asset(assets.get(i))
                    .build());
        }

        // ============================================
        // NATIJA
        // ============================================
        System.out.println("\n====================================");
        System.out.println("✅ Mock data muvaffaqiyatli yuklandi!");
        System.out.println("====================================");
        System.out.println("📦 Categories  : " + categoryRepository.count());
        System.out.println("🔵 Statuses    : " + statusRepository.count());
        System.out.println("🏢 Departments : " + departmentRepository.count());
        System.out.println("👥 Employees   : " + employeeRepository.count());
        System.out.println("💻 Assets      : " + assetRepository.count());
        System.out.println("📱 QR Codes    : " + qrCodeRepository.count());
        System.out.println("====================================\n");
    }
}
