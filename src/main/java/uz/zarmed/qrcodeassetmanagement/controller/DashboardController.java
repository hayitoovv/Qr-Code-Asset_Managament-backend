package uz.zarmed.qrcodeassetmanagement.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import uz.zarmed.qrcodeassetmanagement.dto.response.DashboardStatsDto;
import uz.zarmed.qrcodeassetmanagement.dto.response.RecentScanDto;
import uz.zarmed.qrcodeassetmanagement.service.DashboardService;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/dashboard")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class DashboardController {

    private final DashboardService dashboardService;

    @GetMapping("/stats")
//    @PreAuthorize("permitAll()")
    public ResponseEntity<DashboardStatsDto> getDashboardStats() {
        return ResponseEntity.ok(dashboardService.getDashboardStats());
    }

    @GetMapping("/recent-scans")
    public ResponseEntity<List<RecentScanDto>> getRecentScans() {
      //  return ResponseEntity.ok(dashboardService.getRecentScans());
        return ResponseEntity.ok().build();
    }

    @GetMapping("/assets-by-status")
    public ResponseEntity<Map<String, Long>> getAssetsByStatus() {
       // return ResponseEntity.ok(dashboardService.getAssetsByStatus());
        return ResponseEntity.ok().build();
    }

    @GetMapping("/assets-by-category")
    public ResponseEntity<Map<String, Long>> getAssetsByCategory() {
       // return ResponseEntity.ok(dashboardService.getAssetsByCategory());
        return ResponseEntity.ok().build();
    }
}