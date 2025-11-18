package com.example.AviacionBackend.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "mantenimiento")
public class Mantenimiento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_mantenimiento")
    private Long idMantenimiento;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_avioneta", nullable = false)
    private Avioneta avioneta;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String descripcion;

    @Column(nullable = false)
    private String tipo;

    @Convert(converter = EstadoConverter.class)
    @Column(nullable = false)
    private Estado estado = Estado.En_proceso;

    @Column(columnDefinition = "TEXT")
    private String notas;

    @Column(name = "fecha_inicio")
    private LocalDateTime fechaInicio = LocalDateTime.now();

    @Column(name = "fecha_fin")
    private LocalDateTime fechaFin;

    public enum Estado {
        En_proceso,
        Finalizado,
        Pausado
    }

    @Converter(autoApply = true)
    public static class EstadoConverter implements AttributeConverter<Estado, String> {

        @Override
        public String convertToDatabaseColumn(Estado estado) {
            return switch (estado) {
                case En_proceso -> "En proceso";
                case Finalizado -> "Finalizado";
                case Pausado -> "Pausado";
            };
        }

        @Override
        public Estado convertToEntityAttribute(String dbData) {
            return switch (dbData) {
                case "En proceso" -> Estado.En_proceso;
                case "Finalizado" -> Estado.Finalizado;
                case "Pausado" -> Estado.Pausado;
                default -> throw new IllegalArgumentException("Valor desconocido: " + dbData);
            };
        }
    }
}
