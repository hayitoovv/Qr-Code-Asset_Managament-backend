package uz.zarmed.qrcodeassetmanagement.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.zarmed.qrcodeassetmanagement.entity.Status;

public interface StatusRepository extends JpaRepository<Status, Long> {
}