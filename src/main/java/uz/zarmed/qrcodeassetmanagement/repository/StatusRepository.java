package uz.zarmed.qrcodeassetmanagement.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.zarmed.qrcodeassetmanagement.entity.Status;

import java.util.Optional;

public interface StatusRepository extends JpaRepository<Status, Long> {
    Optional<Status> findByStatus(String status);
}