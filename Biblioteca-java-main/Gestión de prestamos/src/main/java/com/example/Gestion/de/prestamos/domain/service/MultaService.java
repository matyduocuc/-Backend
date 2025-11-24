package com.example.Gestion.de.prestamos.domain.service;

import com.example.Gestion.de.prestamos.application.dto.MultaDTO;
import com.example.Gestion.de.prestamos.application.mapper.MultaMapper;
import com.example.Gestion.de.prestamos.domain.model.Multa;
import com.example.Gestion.de.prestamos.domain.model.Prestamo;
import com.example.Gestion.de.prestamos.domain.repository.MultaRepository;
import com.example.Gestion.de.prestamos.domain.repository.PrestamoRepository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class MultaService {

    private final MultaRepository multaRepository;
    private final PrestamoRepository prestamoRepository;

    public MultaService(MultaRepository multaRepository, PrestamoRepository prestamoRepository) {
        this.multaRepository = multaRepository;
        this.prestamoRepository = prestamoRepository;
    }

    @Transactional(readOnly = true)
    public List<MultaDTO> listarPorPrestamo(Long prestamoId) {
        Prestamo prestamo = prestamoRepository.findById(prestamoId)
                .orElseThrow(() -> new IllegalArgumentException("Préstamo no encontrado con ID: " + prestamoId));
        
        return multaRepository.findByPrestamo(prestamo)
                .stream()
                .map(MultaMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public MultaDTO pagar(Long id) {
        Multa multa = multaRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Multa no encontrada con ID: " + id));
        
        multa.setEstado(Multa.MultaEstado.PAGADA);
        multa.setFechaPago(Instant.now());
        
        Multa guardada = multaRepository.save(multa);
        return MultaMapper.toDTO(guardada);
    }

    @Transactional
    public MultaDTO eximir(Long id, String motivo) {
        Multa multa = multaRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Multa no encontrada con ID: " + id));
        
        multa.setEstado(Multa.MultaEstado.EXENTA);
        multa.setFechaPago(Instant.now());
        multa.setMotivo(motivo);
        
        Multa guardada = multaRepository.save(multa);
        return MultaMapper.toDTO(guardada);
    }
}

