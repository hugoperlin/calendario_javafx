import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class Evento implements EventoIntf{

    private LocalDateTime dataHora;
    private String descricao;

    public Evento(LocalDateTime dataHora, String descricao) {
        this.dataHora = dataHora;
        this.descricao = descricao;
    }

    @Override
    public LocalDate getData() {
        return LocalDate.from(dataHora);
    }

    @Override
    public LocalTime getHora() {
        return LocalTime.from(dataHora);
    }

    @Override
    public String getDescricao() {
        return descricao;
    }
}
