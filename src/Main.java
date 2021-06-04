import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;

import static javafx.application.Application.launch;

public class Main extends Application {

    public static void main(String[] args) {
        launch(args);
    }


    private List<EventoIntf> listaEventos(LocalDate data){

        ArrayList<EventoIntf> lista = new ArrayList();

        LocalDateTime dataHora = LocalDateTime.from(data.atTime(LocalTime.of(8,0)));

        Random rnd = new Random();

        for(int i=0;i<10;i++){
            lista.add(new Evento(dataHora.plusMinutes(rnd.nextInt(300)),"Evento "+i));
        }

        Collections.sort(lista, Comparator.comparing(EventoIntf::getHora));


        return lista;
    }


    @Override
    public void start(Stage stage) throws Exception {

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/fxml/calendario.fxml"));

        Parent conteudo = loader.load();
        Controler controler = (Controler) loader.getController();
        controler.setGetEventos((data)->{return listaEventos(data);});
        stage.setScene(new Scene(conteudo,800,600));

        stage.show();


    }
}
