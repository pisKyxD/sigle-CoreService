package coreService.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "establecimientos")
@Data
@NoArgsConstructor
public class Establecimiento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    @NotBlank(message = "El nombre es obligatorio")
    private String nombre;

    @NotNull(message = "El tipo es obligatorio")
    @Enumerated(EnumType.STRING)
    private Tipo tipo;

    @NotBlank(message = "La región es obligatoria")
    private String region;

    private String direccion;
    private String telefono;
    private Boolean activo = true;

    public enum Tipo {
        HOSPITAL_ALTA, HOSPITAL_COM, CAP
    }
}