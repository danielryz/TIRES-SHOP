package org.tireshop.tiresshopapp.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.tireshop.tiresshopapp.dto.request.CreateTireRequest;
import org.tireshop.tiresshopapp.dto.response.TireResponse;
import org.tireshop.tiresshopapp.entity.Tire;
import org.tireshop.tiresshopapp.repository.TireRepository;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TireService {

    private final TireRepository tireRepository;

    //GET
    public List<TireResponse> getAllTire() {
        return tireRepository.findAll().stream()
                .map(this::mapToResponse)
                .toList();
    }

    public Optional<TireResponse> getTireById(Long id) {
        return tireRepository.findById(id).map(this::mapToResponse);
    }

    public List<TireResponse> getTireBySeason(String seasonName) {
        return tireRepository.getTireBySeason(seasonName);
    }

    public List<TireResponse> getTireBySize(String sizeName) {
        return tireRepository.getTireBySize(sizeName);
    }

    //POST
    public TireResponse createNewTire(CreateTireRequest request) {
        Tire tire = new Tire();
        tire.setName(request.name());
        tire.setPrice(request.price());
        tire.setDescription(request.description());
        tire.setStock(request.stock());
        tire.setType(request.type());
        tire.setSeason(request.season());
        tire.setSize(request.size());

        return mapToResponse(tireRepository.save(tire));
    }

    //PATCH
    public TireResponse updateTire(Long id, CreateTireRequest request) {
        Tire tire = tireRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Opona o id " + id + " nie znaleziona"));

        if (request.name() != null && request.name().isBlank()) tire.setName(request.name());
        if (request.price() != null) tire.setPrice(request.price());
        if (request.description() != null && request.description().isBlank())
            tire.setDescription(request.description());
        if (request.stock() < 0) tire.setStock(request.stock());
        if (request.type() != null) tire.setType(request.type());
        if (request.season() != null && request.season().isBlank()) tire.setSeason(request.season());
        if (request.size() != null) tire.setSize(request.size());

        return mapToResponse(tireRepository.save(tire));
    }

    //DELETE
    public void deleteTire(Long id) {
        if (!tireRepository.existsById(id)) {
            throw new RuntimeException("Opona o id " + id + " nie znaleziona");
        }
        tireRepository.deleteById(id);

    }

    private TireResponse mapToResponse(Tire tire) {
        return new TireResponse(
                tire.getId(),
                tire.getName(),
                tire.getPrice(),
                tire.getDescription(),
                tire.getStock(),
                tire.getType(),
                tire.getSeason(),
                tire.getSize()
        );
    }
}
