import javafx.geometry.Pos;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;

import java.time.DayOfWeek;
import java.time.LocalDate;

/*
Representa um dia dentro do calendário.
Ao setar a data, o texto do dia é automaticamente atualizado.
 */

public class CelulaData extends HBox {

    private LocalDate data;
    private Text texto;

    public CelulaData(Pos alignment){
        this.texto = new Text();
        this.getChildren().add(texto);
        this.setAlignment(alignment);
        this.getStyleClass().addAll("dia");
    }

    public LocalDate getData() {
        return data;
    }

    public void setData(LocalDate data) {
        this.data = data;
        this.texto.setText(data.getDayOfMonth()+"");
    }

    public void ajustaEstilos(){
        if(data.equals(LocalDate.now())){
            this.getStyleClass().add("hoje");
        }
        if(data.getDayOfWeek() == DayOfWeek.SUNDAY || data.getDayOfWeek() == DayOfWeek.SATURDAY){
            this.getStyleClass().add("fimSemana");
        }
    }

    public void limpaEstilos(){
        this.getStyleClass().removeAll("hoje","fimSemana","selecionado");
    }

    public void seleciona(){
        this.getStyleClass().add("selecionado");
    }

    public void deseleciona(){
        this.getStyleClass().remove("selecionado");
    }


    public void clear(){
        this.texto.setText("");
    }
}
