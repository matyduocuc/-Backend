package com.example.GestionDeInformes.service;

import com.example.GestionDeInformes.application.dto.MultasResumenDTO;
import com.example.GestionDeInformes.application.dto.PrestamosResumenDTO;
import com.example.GestionDeInformes.application.dto.UsuarioResumenDTO;
import com.example.GestionDeInformes.prestamos.model.MultaView;
import com.example.GestionDeInformes.prestamos.model.PrestamoView;
import com.example.GestionDeInformes.prestamos.repository.MultaInformeRepository;
import com.example.GestionDeInformes.prestamos.repository.PrestamoInformeRepository;
import com.example.GestionDeInformes.usuarios.model.UsuarioView;
import com.example.GestionDeInformes.usuarios.repository.UsuarioInformeRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class InformeService {

    private final PrestamoInformeRepository prestamoRepository;
    private final MultaInformeRepository multaRepository;
    private final UsuarioInformeRepository usuarioRepository;

    public InformeService(PrestamoInformeRepository prestamoRepository,
                          MultaInformeRepository multaRepository,
                          UsuarioInformeRepository usuarioRepository) {
        this.prestamoRepository = prestamoRepository;
        this.multaRepository = multaRepository;
        this.usuarioRepository = usuarioRepository;
    }

    @Transactional(readOnly = true)
    public PrestamosResumenDTO obtenerResumenPrestamos() {
        PrestamosResumenDTO resumen = new PrestamosResumenDTO();
        
        resumen.setTotalPrestamos(prestamoRepository.count());
        resumen.setActivos(prestamoRepository.countByEstado(PrestamoView.PrestamoEstado.ACTIVO));
        resumen.setAtraso(prestamoRepository.countByEstado(PrestamoView.PrestamoEstado.ATRASO));
        resumen.setDevueltos(prestamoRepository.countByEstado(PrestamoView.PrestamoEstado.DEVUELTO));
        resumen.setCancelados(prestamoRepository.countByEstado(PrestamoView.PrestamoEstado.CANCELADO));
        resumen.setPerdidos(prestamoRepository.countByEstado(PrestamoView.PrestamoEstado.PERDIDO));
        resumen.setMultasPendientes(multaRepository.countByEstado(MultaView.MultaEstado.PENDIENTE));
        resumen.setMultasPagadas(multaRepository.countByEstado(MultaView.MultaEstado.PAGADA));
        
        return resumen;
    }

    @Transactional(readOnly = true)
    public UsuarioResumenDTO obtenerResumenUsuario(Long usuarioId) {
        UsuarioView usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado con ID: " + usuarioId));
        
        UsuarioResumenDTO resumen = new UsuarioResumenDTO();
        resumen.setUsuarioId(usuario.getId());
        resumen.setNombreCompleto(usuario.getNombre() + " " + usuario.getApellido());
        resumen.setEmail(usuario.getEmail());
        resumen.setTotalPrestamos(prestamoRepository.countByUsuarioId(usuarioId));
        resumen.setPrestamosActivos(prestamoRepository.countByUsuarioIdAndEstado(
                usuarioId, PrestamoView.PrestamoEstado.ACTIVO));
        resumen.setPrestamosAtraso(prestamoRepository.countByUsuarioIdAndEstado(
                usuarioId, PrestamoView.PrestamoEstado.ATRASO));
        resumen.setMultasPendientes(multaRepository.countByPrestamo_UsuarioIdAndEstado(
                usuarioId, MultaView.MultaEstado.PENDIENTE));
        
        return resumen;
    }

    @Transactional(readOnly = true)
    public MultasResumenDTO obtenerResumenMultas() {
        MultasResumenDTO resumen = new MultasResumenDTO();
        
        resumen.setTotalMultas(multaRepository.count());
        resumen.setMultasPendientes(multaRepository.countByEstado(MultaView.MultaEstado.PENDIENTE));
        resumen.setMultasPagadas(multaRepository.countByEstado(MultaView.MultaEstado.PAGADA));
        resumen.setMultasExentas(multaRepository.countByEstado(MultaView.MultaEstado.EXENTA));
        
        return resumen;
    }
}

