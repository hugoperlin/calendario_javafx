import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static javafx.application.Application.launch;

public class Main extends Application {

    public static void main(String[] args) {
        launch(args);
    }


    private List<EventoIntf> listaEventos(LocalDate data){

        ArrayList<EventoIntf> lista = new ArrayList();

        lista.add(new Evento(LocalDateTime.now().minusDays(5),"Evento 1"));
        lista.add(new Evento(LocalDateTime.now().minusDays(4),"Evento 2"));
        lista.add(new Evento(LocalDateTime.now().minusDays(3),"Evento 3"));
        lista.add(new Evento(LocalDateTime.now().minusDays(2),"Evento 4"));
        lista.add(new Evento(LocalDateTime.now().minusDays(1),"Evento 5"));
        lista.add(new Evento(LocalDateTime.now(),"Evento 6"));

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
