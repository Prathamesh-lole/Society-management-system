package com.example.societymainteinance.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.societymainteinance.entity.FlatEntity;
import com.example.societymainteinance.repo.FlatRepo;
import com.example.societymainteinance.repo.ResidentRepo;

@Service
public class FlatService {

    @Autowired
    private FlatRepo flatRepo;

    @Autowired
    private ActivityService activityService;

    // -------------------- CRUD --------------------

    public FlatEntity createFlat(FlatEntity flat) {

        FlatEntity savedFlat = flatRepo.save(flat);

        activityService.logActivity(
                "ADMIN",
                null,
                "CREATE",
                "Flat " + savedFlat.getFlatNumber() +
                " added in Block " + savedFlat.getBlock()
        );

        return savedFlat;
    }

    public List<FlatEntity> getAllFlats() {
        return flatRepo.findAll();
    }

    public Optional<FlatEntity> getFlatById(Long id) {
        return flatRepo.findById(id);
    }

    public FlatEntity updateFlat(Long id, FlatEntity updatedFlat) {

        return flatRepo.findById(id).map(flat -> {

            flat.setFlatNumber(updatedFlat.getFlatNumber());
            flat.setBlock(updatedFlat.getBlock());
            flat.setFloor(updatedFlat.getFloor());
            flat.setResident(updatedFlat.getResident());

            FlatEntity saved = flatRepo.save(flat);

            activityService.logActivity(
                    "ADMIN",
                    null,
                    "UPDATE",
                    "Flat " + saved.getFlatNumber() + " details updated"
            );

            return saved;

        }).orElseThrow(() -> new RuntimeException("Flat not found with id " + id));
    }

    public void deleteFlat(Long id) {

        FlatEntity flat = flatRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Flat not found with id " + id));

        flatRepo.deleteById(id);

        activityService.logActivity(
                "ADMIN",
                null,
                "DELETE",
                "Flat " + flat.getFlatNumber() +
                " removed from Block " + flat.getBlock()
        );
    }

    // -------------------- Assign Resident --------------------

    public FlatEntity assignResidentToFlat(Long flatId, Long residentId,
                                           ResidentRepo residentRepo) {

        FlatEntity flat = flatRepo.findById(flatId)
                .orElseThrow(() -> new RuntimeException("Flat not found with id " + flatId));

        var resident = residentRepo.findById(residentId)
                .orElseThrow(() -> new RuntimeException("Resident not found with id " + residentId));

        flat.setResident(resident);
        resident.setFlat(flat);

        flatRepo.save(flat);
        residentRepo.save(resident);

        activityService.logActivity(
                "ADMIN",
                null,
                "ASSIGN",
                "Resident " + resident.getName() +
                " assigned to Flat " + flat.getFlatNumber()
        );

        return flat;
    }

    // -------------------- Search Flats --------------------

    public List<FlatEntity> searchFlats(String query) {
        return flatRepo
                .findByFlatNumberContainingIgnoreCaseOrBlockContainingIgnoreCase(query, query);
    }
}
