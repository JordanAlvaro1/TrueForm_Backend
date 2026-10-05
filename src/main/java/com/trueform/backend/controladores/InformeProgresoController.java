package com.trueform.backend.controladores;

import com.trueform.backend.dto.ComparacionMensualDTO;
import com.trueform.backend.dto.ResumenProgresoDTO;
import com.trueform.backend.servicios.InformeProgresoServicio;
import com.trueform.backend.servicios.UsuarioServicio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import java.nio.charset.StandardCharsets;
import java.time.YearMonth;
import java.time.format.DateTimeParseException;
import java.util.Map;

@RestController
@RequestMapping("/api/progresos/usuario/{idUsuario}")
public class InformeProgresoController {
    @Autowired
    private InformeProgresoServicio informes;
    @Autowired
    private UsuarioServicio usuarios;

    @GetMapping("/comparacion-mensual")
    public ResponseEntity<?> comparar(@PathVariable Long idUsuario,
            @RequestParam String mesBase, @RequestParam String mesComparado) {
        if (usuarios.buscarPorId(idUsuario) == null) return ResponseEntity.notFound().build();
        try {
            if (!mesBase.matches("[0-9]{4}-[0-9]{2}") || !mesComparado.matches("[0-9]{4}-[0-9]{2}")) {
                return ResponseEntity.badRequest().body(Map.of("mensaje", "Usa meses con formato AAAA-MM."));
            }
            YearMonth base = YearMonth.parse(mesBase);
            YearMonth comparado = YearMonth.parse(mesComparado);
            if (base.equals(comparado)) {
                return ResponseEntity.badRequest().body(Map.of("mensaje", "Selecciona dos meses diferentes."));
            }
            ComparacionMensualDTO resultado = informes.comparar(idUsuario, base, comparado);
            return ResponseEntity.ok(resultado);
        } catch (DateTimeParseException ex) {
            return ResponseEntity.badRequest().body(Map.of("mensaje", "Usa meses válidos con formato AAAA-MM."));
        }
    }

    @GetMapping("/resumen")
    public ResponseEntity<ResumenProgresoDTO> resumen(@PathVariable Long idUsuario) {
        if (usuarios.buscarPorId(idUsuario) == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(informes.obtenerResumen(idUsuario));
    }

    @GetMapping("/exportar")
    public ResponseEntity<?> exportar(@PathVariable Long idUsuario) {
        if (usuarios.buscarPorId(idUsuario) == null) return ResponseEntity.notFound().build();
        ResumenProgresoDTO resumen = informes.obtenerResumen(idUsuario);
        if (!resumen.isHayDatos()) {
            return ResponseEntity.status(422).body(resumen);
        }
        byte[] csv = informes.generarCsv(resumen).getBytes(StandardCharsets.UTF_8);
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType("text/csv;charset=UTF-8"))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=progreso-" + idUsuario + ".csv")
                .body(csv);
    }
}
